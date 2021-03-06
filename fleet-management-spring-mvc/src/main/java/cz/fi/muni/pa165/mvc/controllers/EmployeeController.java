package cz.fi.muni.pa165.mvc.controllers;

import cz.fi.muni.pa165.dto.EmployeeDTO;
import cz.fi.muni.pa165.dto.JourneyDTO;
import cz.fi.muni.pa165.dto.VehicleDTO;
import cz.fi.muni.pa165.facade.EmployeeFacade;
import cz.fi.muni.pa165.facade.JourneyFacade;
import cz.fi.muni.pa165.facade.VehicleFacade;
import cz.fi.muni.pa165.service.DateTimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;


/**
 * Created by MBalicky on 14/12/2016.
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController {

    final static Logger log = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private VehicleFacade vehicleFacade;
    @Autowired
    private JourneyFacade journeyFacade;
    @Autowired
    private EmployeeFacade employeeFacade;
    @Autowired
    private DateTimeService dateTimeService;

    @RequestMapping(value = "/vehicleListView/{id}", method = RequestMethod.GET)
    public String vehicleList(@PathVariable long id, Model model) {
        model.addAttribute("vehicles", this.vehicleFacade.findVehiclesToBeBorrowedByUser(id));
        model.addAttribute("employee", this.employeeFacade.findEmployeeById(id));
        return "employee/vehicleListView";
    }

    @RequestMapping(value = "/journeyListView/{id}", method = RequestMethod.GET)
    public String journeyList(@PathVariable long id, Model model) {
        model.addAttribute("journeys", this.journeyFacade.getJourneysByEmployee(id));
        return "employee/journeyListView";
    }

    @RequestMapping(value = "/borrowed-vehicles", method = RequestMethod.GET)
    public String myBorrowedVehicles(Model model) {
        model.addAttribute("journeys", journeyFacade.getUnfinishedJourneysOfUser(getCurrentlyLoggedEmployee().getId()));
        return "employee/myBorrowedVehiclesView";
    }

    @RequestMapping(value = "/vehicles-to-borrow", method = RequestMethod.GET)
    public String vehiclesToBorrowByMe(Model model) {
        model.addAttribute("vehicles", vehicleFacade.findVehiclesToBeBorrowedByUser(getCurrentlyLoggedEmployee().getId()));
        return "employee/vehiclesToBorrowView";
    }

    @RequestMapping(value = "/borrow-vehicle/{vehicleId}", method = RequestMethod.GET)
    public String borrowVehicle(
            @PathVariable long vehicleId,
            RedirectAttributes redirectAttributes,
            UriComponentsBuilder uriBuilder
    ) {
        redirectAttributes.addFlashAttribute("alert_success", "Vehicle borrowed.");
        journeyFacade.beginJourney(vehicleId, getCurrentlyLoggedEmployee().getId(), dateTimeService.getCurrentDate());
        return "redirect:" + uriBuilder.path("/employee/vehicles-to-borrow").buildAndExpand().encode().toUriString();

    }

    @RequestMapping(value = "/vehicleAddJourneyView/{vehicleId}/{employeeId}", method = RequestMethod.GET)
    public String addJourneyToVehicle(@PathVariable long vehicleId, @PathVariable long employeeId, Model model) {
        EmployeeDTO e = this.employeeFacade.findEmployeeById(employeeId);
        VehicleDTO v = this.vehicleFacade.findVehicleById(vehicleId);
        JourneyDTO d = new JourneyDTO(Date.from(Instant.now()), v, e);

        model.addAttribute("journeyCreate", d);
        return "employee/vehicleAddJourneyView";
    }

    // create new journey
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid @ModelAttribute("journeyCreate") JourneyDTO formBean, BindingResult bindingResult,
                         Model model, RedirectAttributes redirectAttributes, UriComponentsBuilder uriBuilder) {
        log.debug("create(journeyCreate={})", formBean);
        Long vehicleId = formBean.getVehicle().getId();
        Long employeeId = formBean.getEmployee().getId();
        //in case of validation error forward back to the the form
        if (bindingResult.hasErrors()) {
            for (ObjectError ge : bindingResult.getGlobalErrors()) {
                log.trace("ObjectError: {}", ge);
            }
            for (FieldError fe : bindingResult.getFieldErrors()) {
                model.addAttribute(fe.getField() + "_error", true);
                log.trace("FieldError: {}", fe);
            }
            return "employee/vehicleAddJourneyView";
        }
        //create journey
        this.journeyFacade.addJourney(formBean);
        //report success
        redirectAttributes.addFlashAttribute("alert_success", "Journey " + " was created and added to vehicle and employee");
        return "redirect:" + uriBuilder.path("/employee/journeyListView/"+employeeId).buildAndExpand().encode().toUriString();
    }

    @RequestMapping(value = "/finish-journey/{journeyId}", method = RequestMethod.GET)
    public String finishJourney(
            @PathVariable("journeyId") Long journeyId,
            @RequestParam(value = "drivenDistance") float drivenDistance,
            UriComponentsBuilder uriBuilder,
            RedirectAttributes redirectAttributes
    ) {
        journeyFacade.finishJourney(journeyId, drivenDistance, dateTimeService.getCurrentDate());
        redirectAttributes.addFlashAttribute("alert_success", "Journey was finished and vehicle returned");
        return "redirect:" + uriBuilder.path("/employee/borrowed-vehicles").buildAndExpand().encode().toUriString();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(true);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
    }

    private EmployeeDTO getCurrentlyLoggedEmployee()
    {
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return employeeFacade.findEmployeeByEmail(user.getUsername());
    }
}

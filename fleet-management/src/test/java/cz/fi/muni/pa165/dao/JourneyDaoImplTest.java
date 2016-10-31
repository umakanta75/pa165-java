package cz.fi.muni.pa165.dao;

import cz.fi.muni.pa165.InMemoryDatabaseTestContext;
import cz.fi.muni.pa165.dao.interfaces.EmployeeDao;
import cz.fi.muni.pa165.dao.interfaces.JourneyDao;
import cz.fi.muni.pa165.entity.Employee;
import cz.fi.muni.pa165.entity.Journey;
import cz.fi.muni.pa165.entity.Vehicle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Year;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * @author Richard Trebichavský
 */
@ContextConfiguration(classes = InMemoryDatabaseTestContext.class)
@TestExecutionListeners(TransactionalTestExecutionListener.class)
@Transactional
public class JourneyDaoImplTest extends AbstractTransactionalTestNGSpringContextTests {

    @Autowired
    private JourneyDao uut;

    private Employee employee;
    private Vehicle vehicle;
    private Journey journey;

    @BeforeMethod
    public void setUp() {
        employee = new Employee("John", "Doe");
        vehicle = new Vehicle("VRP", "Type", Year.of(1999), "EngineType", "VIN", (long) 7658.54);
        journey = new Journey(new Date(), vehicle, employee);
        uut.persist(journey);
    }

    @Test
    public void testFindById() {
        // Act
        Journey foundJourney = uut.findById(journey.getId());

        // Assert
        Assert.assertEquals(foundJourney, journey);
    }

    @Test
    public void testFindAll() {
        // Arrange
        Journey journey2 = new Journey(new Date(), vehicle, employee);
        Journey journey3 = new Journey(new Date(), vehicle, employee);
        uut.persist(journey2);
        uut.persist(journey3);

        // Act
        List<Journey> foundJourneys = uut.findAll();

        // Assert
        Assert.assertEquals(foundJourneys.get(0), journey);
        Assert.assertEquals(foundJourneys.get(1), journey2);
        Assert.assertEquals(foundJourneys.get(2), journey3);
    }

    @Test
    public void testPersist() {
        // Arrange
        int itemCountBefore = uut.findAll().size();

        // Act
        Journey entity = new Journey(new Date(), vehicle, employee);
        uut.persist(entity);

        // Assert
        Assert.assertEquals(uut.findAll().size(), itemCountBefore + 1);
    }

    @Test
    public void testMerge() {
        // Arrange
        Journey foundJourney = uut.findById(journey.getId());
        foundJourney.returnVehicle(new Date(), 67.5f);

        // Act
        uut.merge(foundJourney);

        // Assert
        Journey foundJourneyAfterMerge = uut.findById(journey.getId());
        Assert.assertNotNull(foundJourneyAfterMerge.getReturnedAt());
    }

    @Test
    public void testRemove() {
        // Arrange
        int itemCountBefore = uut.findAll().size();

        // Act
        uut.remove(journey);

        // Assert
        Assert.assertEquals(uut.findAll().size(), itemCountBefore - 1);
    }

    @Test
    public void testRemoveById() {
        // Arrange
        int itemCountBefore = uut.findAll().size();

        // Act
        uut.removeById(journey.getId());

        // Assert
        Assert.assertEquals(uut.findAll().size(), itemCountBefore - 1);
    }
}
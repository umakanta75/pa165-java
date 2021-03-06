package cz.fi.muni.pa165.dto;


/**
 * @author Martin Schmidt
 */
public class VehicleDTO {

    private Long id;

    private String vrp;

    private String type;

    private int productionYear;

    private String engineType;

    private String vin;

    private Long initialKilometrage;

    private VehicleCategoryDTO vehicleCategory;

    //added manually
    private Boolean active;


    public VehicleDTO() {
    }


    public VehicleDTO(String vrp, String type, int productionYear, String engineType, String vin, Long initialKilometrage, Boolean active) {
        this.vrp = vrp;
        this.type = type;
        this.productionYear = productionYear;
        this.engineType = engineType;
        this.vin = vin;
        this.initialKilometrage = initialKilometrage;
        this.active = active;
    }

    public VehicleCategoryDTO getVehicleCategory() {
        return vehicleCategory;
    }

    public void setVehicleCategory(VehicleCategoryDTO vehicleCategory) {
        this.vehicleCategory = vehicleCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVrp() {
        return vrp;
    }

    public void setVrp(String vrp) {
        this.vrp = vrp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getProductionYear() {
        return productionYear;
    }

    public void setProductionYear(int productionYear) {
        this.productionYear = productionYear;
    }

    public String getEngineType() {
        return engineType;
    }

    public void setEngineType(String engineType) {
        this.engineType = engineType;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public Long getInitialKilometrage() {
        return initialKilometrage;
    }

    public void setInitialKilometrage(Long initialKilometrage) {
        this.initialKilometrage = initialKilometrage;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || (!(o instanceof VehicleDTO))) return false;

        VehicleDTO vehicle = (VehicleDTO) o;

        return vin.equals(vehicle.getVin());

    }

    @Override
    public int hashCode() {
        return vin.hashCode();
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", vrp='" + vrp + '\'' +
                ", type='" + type + '\'' +
                ", productionYear=" + productionYear +
                ", engineType='" + engineType + '\'' +
                ", vin='" + vin + '\'' +
                ", initialKilometrage=" + initialKilometrage +
                ", vehicleCategory=" + vehicleCategory +
                ", active=" + this.active +
                '}';
    }


}

package cz.fi.muni.pa165.entity;

import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Richard Trebichavský
 */
@Entity
public class Journey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float distance;

    @Column(nullable = false)
    private Date borrowedAt;

    private Date returnedAt;

    @OneToOne(cascade = {CascadeType.ALL})
    private Vehicle vehicle;

    @OneToOne(cascade = {CascadeType.ALL})
    private Employee employee;

    /**
     * Creates entity in initial phase where vehicle is borrowed.
     *
     * @param borrowedAt Date, when vehicle was borrowed.
     * @param vehicle    Vehicle which was borrowed.
     * @param employee   Employee who borrowed a vehicle.
     */
    public Journey(Date borrowedAt, Vehicle vehicle, Employee employee) {
        this.borrowedAt = borrowedAt;
        this.vehicle = vehicle;
        this.employee = employee;
    }

    /**
     * Updated entity to final phase where vehicle is returned.
     *
     * @param returnedAt Date, when vehicle was returned.
     * @param distance   Distance driven with this vehicle during the journey in kilometres.
     */
    public void returnVehicle(Date returnedAt, Float distance) {
        this.returnedAt = returnedAt;
        this.distance = distance;
    }

    public Long getId() {
        return id;
    }

    public Float getDistance() {
        return distance;
    }

    public void setDistance(Float distance) {
        Assert.notNull(this.returnedAt);
        this.distance = distance;
    }

    public Date getBorrowedAt() {
        return borrowedAt;
    }

    public void setBorrowedAt(Date borrowedAt) {
        this.borrowedAt = borrowedAt;
    }

    public Date getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(Date returnedAt) {
        Assert.notNull(this.distance);
        this.returnedAt = returnedAt;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Journey journey = (Journey) o;

        return id != null ? id.equals(journey.id) : journey.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Journey{" +
                "id=" + id +
                ", distance=" + distance +
                ", borrowedAt=" + borrowedAt +
                ", returnedAt=" + returnedAt +
                ", vehicle=" + vehicle +
                ", employee=" + employee +
                '}';
    }
}
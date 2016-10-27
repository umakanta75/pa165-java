package cz.fi.muni.pa165.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by MBalicky on 23/10/2016.
 * @author Michal Balický
 */

@Entity
public class Inspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Date performedAt;

    public Inspection(Date performedAt){
        this.performedAt = performedAt;
    }

    public int getId(){
        return this.id;
    }

    public Date getPerformedAt(){
        return this.performedAt;
    }

    public void setPerformedAt(Date performedAt){
        this.performedAt = performedAt;
    }
}

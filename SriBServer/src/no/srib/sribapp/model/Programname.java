package no.srib.sribapp.model;

import javax.persistence.*;

/**
 * The persistent class for the programname database table.
 * 
 */
@Entity
@Table(name = "app_programname")
@NamedQuery(name = "Programname.findAll", query = "SELECT p FROM Programname p")
public class Programname extends AbstractModel {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    public Programname() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

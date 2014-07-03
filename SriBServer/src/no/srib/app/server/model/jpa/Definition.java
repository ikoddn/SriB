package no.srib.app.server.model.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlType;

import no.srib.app.server.model.AbstractModel;

/**
 * The persistent class for the definition database table.
 * 
 */
@Entity
@Table(name = "DEFINITION")
@NamedQuery(name = "Definition.findAll", query = "SELECT d FROM Definition d")
@XmlType(name = "")
// Remove "@type" from the marshalled JSON
public class Definition extends AbstractModel {
    private static final long serialVersionUID = 1L;
    private int defnr;
    private String name;

    public Definition() {
    }

    public Definition(final int defnr, final String name) {
        this.defnr = defnr;
        this.name = name;
    }

    @Id
    @Column(insertable = false, unique = true, nullable = false)
    public int getDefnr() {
        return this.defnr;
    }

    public void setDefnr(int defnr) {
        this.defnr = defnr;
    }

    @Column(insertable = false, updatable = false, length = 254)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

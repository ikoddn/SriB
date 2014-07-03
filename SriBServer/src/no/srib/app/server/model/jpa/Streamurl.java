package no.srib.app.server.model.jpa;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlType;

import no.srib.app.server.model.AbstractModel;

/**
 * The persistent class for the streamurl database table.
 * 
 */
@Entity
@Table(name = "app_streamurl")
@NamedQuery(name = "Streamurl.findAll", query = "SELECT s FROM Streamurl s")
@XmlType(name = "")
// Remove "@type" from the marshalled JSON
public class Streamurl extends AbstractModel {

    private static final long serialVersionUID = 1L;

    private int id;
    private String name;
    private String url;

    public Streamurl() {
    }

    public Streamurl(final String name, final String url) {
        this.name = name;
        this.url = url;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(nullable = false, length = 100)
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false, length = 150)
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}

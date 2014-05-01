package no.srib.app.client.model;

import java.io.Serializable;
import java.util.Calendar;

public class StreamSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String url;
    private Calendar time;
    
    public StreamSchedule() {
    }
    
    public StreamSchedule(final String name, final String url, final Calendar time) {
        this.name = name;
        this.url = url;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }
}

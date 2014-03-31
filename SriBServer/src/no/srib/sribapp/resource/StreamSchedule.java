package no.srib.sribapp.resource;

import java.io.Serializable;
import java.util.Calendar;

public class StreamSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    private String url;
    private Calendar time;
    
    public StreamSchedule() {
    }
    
    public StreamSchedule(final String url, final Calendar time) {
        this.url = url;
        this.time = time;
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

package no.srib.sribapp.resource.helper;

import java.io.Serializable;

public class StreamSchedule implements Serializable {

    private static final long serialVersionUID = 1L;
    private String name;
    private String url;
    private long time;

    public StreamSchedule() {
    }

    public StreamSchedule(final String name, final String url, final long time) {
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}

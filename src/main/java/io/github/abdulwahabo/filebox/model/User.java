package io.github.abdulwahabo.filebox.model;

import java.util.List;

public class User {

    private String email;
    private String timezone; // Get user IP address and send it to free IP location API to get TZ info.
    private List<File> files;
    private List<Event> events;

    public String getEmail() {
        return email;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

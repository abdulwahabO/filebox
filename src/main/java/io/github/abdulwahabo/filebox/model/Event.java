package io.github.abdulwahabo.filebox.model;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

public class Event {

    private EventType type;
    private ZonedDateTime time;

    public static Event newEvent(EventType type, String timezone) {

        // instantiate a new ZDT using user's IP info

        return new Event(null, null);
    }

    private Event(EventType type, ZonedDateTime time) {
        this.type = type;
        this.time = time;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }
}

package com.finalproject.backend.calendar.model;

import java.time.LocalTime;

//global display window (08:00–20:00) applied to all calendars
public final class CalendarHours {

    public static final LocalTime OPEN = LocalTime.of(8, 0);
    public static final LocalTime CLOSE = LocalTime.of(20, 0);

    private CalendarHours() {}
}

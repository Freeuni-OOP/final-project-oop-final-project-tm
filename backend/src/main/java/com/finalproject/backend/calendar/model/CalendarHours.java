package com.finalproject.backend.calendar.model;

import java.time.LocalTime;

//global display window (00:00–23:59) applied to all calendars
//23:59 because LocalTime has no 24:00 value
public final class CalendarHours {

    public static final LocalTime OPEN = LocalTime.of(0, 0);
    public static final LocalTime CLOSE = LocalTime.of(23, 59);

    private CalendarHours() {}
}

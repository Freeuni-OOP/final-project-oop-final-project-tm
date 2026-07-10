package com.finalproject.backend.calendar.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

//top-level response for both /calendar/service/{id} and /calendar/user/{id}
//openTime/closeTime are always CalendarHours.OPEN/CLOSE, not service-specific, despite living on this per-response DTO
public class WeekCalendarDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate weekStart;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate weekEnd;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime openTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime closeTime;

    private List<DayColumnDTO> days;

    public WeekCalendarDTO() {}

    public WeekCalendarDTO(LocalDate weekStart, LocalDate weekEnd,
                           LocalTime openTime, LocalTime closeTime,
                           List<DayColumnDTO> days) {
        this.weekStart = weekStart;
        this.weekEnd = weekEnd;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.days = days;
    }

    public LocalDate getWeekStart() { return weekStart; }
    public void setWeekStart(LocalDate weekStart) { this.weekStart = weekStart; }

    public LocalDate getWeekEnd() { return weekEnd; }
    public void setWeekEnd(LocalDate weekEnd) { this.weekEnd = weekEnd; }

    public LocalTime getOpenTime() { return openTime; }
    public void setOpenTime(LocalTime openTime) { this.openTime = openTime; }

    public LocalTime getCloseTime() { return closeTime; }
    public void setCloseTime(LocalTime closeTime) { this.closeTime = closeTime; }

    public List<DayColumnDTO> getDays() { return days; }
    public void setDays(List<DayColumnDTO> days) { this.days = days; }
}
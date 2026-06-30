package com.finalproject.backend.calendar.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public class WeeklyCalendarDTO {

    private Integer userId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate weekStart;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate weekEnd;
    private List<DayDTO> days;

    public WeeklyCalendarDTO() {}

    public WeeklyCalendarDTO(Integer userId, LocalDate weekStart, LocalDate weekEnd, List<DayDTO> days) {
        this.userId = userId;
        this.weekStart = weekStart;
        this.weekEnd = weekEnd;
        this.days = days;
    }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public LocalDate getWeekStart() { return weekStart; }
    public void setWeekStart(LocalDate weekStart) { this.weekStart = weekStart; }

    public LocalDate getWeekEnd() { return weekEnd; }
    public void setWeekEnd(LocalDate weekEnd) { this.weekEnd = weekEnd; }

    public List<DayDTO> getDays() { return days; }
    public void setDays(List<DayDTO> days) { this.days = days; }
}

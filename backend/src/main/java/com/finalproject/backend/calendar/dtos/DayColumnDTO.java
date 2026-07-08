package com.finalproject.backend.calendar.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

//one day's worth of calendar segments; segments only contains non-FREE periods
//any time not covered by a segment is implicitly free
public class DayColumnDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;

    private String dayOfWeek;
    private List<SegmentDTO> segments;

    public DayColumnDTO() {}

    public DayColumnDTO(LocalDate date, String dayOfWeek, List<SegmentDTO> segments) {
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.segments = segments;
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public List<SegmentDTO> getSegments() { return segments; }
    public void setSegments(List<SegmentDTO> segments) { this.segments = segments; }
}
package com.finalproject.backend.calendar.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

//wire-level version of a Timeline.Segment; status is the SlotStatus name as a string
//start/end are clamped to CalendarHours open/close by the service before this is built
public class SegmentDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime start;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime end;

    private String status;

    public SegmentDTO() {}

    public SegmentDTO(LocalTime start, LocalTime end, String status) {
        this.start = start;
        this.end = end;
        this.status = status;
    }

    public LocalTime getStart() { return start; }
    public void setStart(LocalTime start) { this.start = start; }

    public LocalTime getEnd() { return end; }
    public void setEnd(LocalTime end) { this.end = end; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
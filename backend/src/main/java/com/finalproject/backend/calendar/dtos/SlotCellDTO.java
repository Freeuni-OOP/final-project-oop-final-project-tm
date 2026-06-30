package com.finalproject.backend.calendar.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public class SlotCellDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime time;
    private String status;

    public SlotCellDTO() {}

    public SlotCellDTO(LocalTime time, String status) {
        this.time = time;
        this.status = status;
    }

    public LocalTime getTime() { return time; }
    public void setTime(LocalTime time) { this.time = time; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
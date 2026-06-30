package com.finalproject.backend.calendar.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public class DayDTO {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String dayOfWeek;
    private List<SlotCellDTO> slots;

    public DayDTO() {}

    public DayDTO(LocalDate date, String dayOfWeek, List<SlotCellDTO> slots) {
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.slots = slots;
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(String dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public List<SlotCellDTO> getSlots() { return slots; }
    public void setSlots(List<SlotCellDTO> slots) { this.slots = slots; }
}
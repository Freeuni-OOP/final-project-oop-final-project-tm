package com.finalproject.backend.calendar.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class BookingSlotDTO {

    private Long id;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime slotDateTime;
    private String status;

    public BookingSlotDTO() {}

    public BookingSlotDTO(Long id, LocalDateTime slotDateTime, String status) {
        this.id = id;
        this.slotDateTime = slotDateTime;
        this.status = status;
    }

    public Long getId()                             { return id; }
    public void setId(Long id)                      { this.id = id; }

    public LocalDateTime getSlotDateTime()                   { return slotDateTime; }
    public void setSlotDateTime(LocalDateTime slotDateTime)  { this.slotDateTime = slotDateTime; }

    public String getStatus()                       { return status; }
    public void setStatus(String status)            { this.status = status; }
}

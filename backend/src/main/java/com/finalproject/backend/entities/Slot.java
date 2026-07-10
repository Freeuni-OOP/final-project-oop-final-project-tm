package com.finalproject.backend.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "slots")
public class Slot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer slotId;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service serviceId;

    @Column(name = "start_time", nullable = true)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = true)
    private LocalDateTime endTime;

    public Slot() {}

    public Slot(int slotId, Service serviceId, LocalDateTime startTime, LocalDateTime endTime) {
        this.slotId = slotId;
        this.serviceId = serviceId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public Integer getSlotId() {
        return slotId;
    }

    public Service getServiceId() {
        return serviceId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setSlotId(int slotId) {
        this.slotId = slotId;
    }

    public void setServiceId(Service serviceId) {
        this.serviceId = serviceId;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}

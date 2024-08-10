package com.infina.corso.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SystemDate {
    @Id
    private int id;
    private LocalDate date;
    private boolean isDayClosed;
    private boolean isDayClosedStarted;
}

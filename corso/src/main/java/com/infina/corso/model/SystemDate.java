package com.infina.corso.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "system_date")
public class SystemDate {
    @Id
    private int id;
    private LocalDate date;
//    private boolean isDayClosed;
    private boolean isDayClosedStarted;
}

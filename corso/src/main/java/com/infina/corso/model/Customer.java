package com.infina.corso.model;

import com.infina.corso.model.enums.CustomerType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private String tcKimlikNo;
    private String companyNo;
    private CustomerType musteriTur;
    private String email;

     @OneToMany(mappedBy = "customer", cascade = CascadeType.REMOVE)
     private List<Account> accounts;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}


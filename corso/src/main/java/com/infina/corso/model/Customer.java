package com.infina.corso.model;

import com.infina.corso.model.enums.CustomerStatus;
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
    private String companyName;
    private String tcKimlikNo; // bireysel müşteriler için tc kimlik numarası
    private String vkn; // kurumsal müşteriler için vergi numarası
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;
    private String email;
    private String phone;
    @Enumerated(EnumType.STRING)
    private CustomerStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Account> accounts;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Iban> ibans;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}


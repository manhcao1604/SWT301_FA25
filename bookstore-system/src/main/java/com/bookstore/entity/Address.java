package com.bookstore.entity;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "addresses.html")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "NVARCHAR(500)")
    private String street;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String city;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String country;
}
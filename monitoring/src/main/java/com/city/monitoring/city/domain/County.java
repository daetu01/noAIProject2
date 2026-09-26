package com.city.monitoring.city.domain;

import jakarta.persistence.*;

@Entity
public class County {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    private City city;

    private String countyName;
}

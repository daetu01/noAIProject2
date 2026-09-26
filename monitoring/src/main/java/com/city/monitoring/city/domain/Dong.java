package com.city.monitoring.city.domain;

import jakarta.persistence.*;

@Entity
public class Dong {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private County county;

    private String dongName;
}

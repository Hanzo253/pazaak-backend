package com.capstone.pazaak.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY);
    private Long id;

    @Column
    private String userName;

    @Column(unique = true)
    private String emailAddress;
}

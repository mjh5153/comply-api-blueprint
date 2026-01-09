package com.init_spring_bean_mvn.demo.entity;

import jakarta.persistence.*;

@Entity // marks class as jpa entity and maps to database table
@Table(name = "companys")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // configures primary key for id field and increments
    private Long id;

    @Column(name="name", nullable = false)
    private String name; //if two words like firstName jpa will make first_name

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(unique = true)
    private String email;

    // Default no-argument constructor (required by JPA/Hibernate)
    public Company() {
    }

    public Company(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}

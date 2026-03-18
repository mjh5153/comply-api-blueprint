package com.init_spring_bean_mvn.demo.emsbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
// now make jpa entity
@Entity
@Table(name = "analysts")
public class Analyst {
    @Id // jakartha.persistance package
    @GeneratedValue(strategy = GenerationType.IDENTITY) // uses database autoincrement feature to incrememnt primary key
    private Long id;
    // column mapping
    @Column(name = "first_name") // if we don't specificy - jpa will give column name default as field_name
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email_id", nullable = false, unique=true) // email should be unique and not null
    private String email;
}
// Lombok annotations require annotation
// now jpa entity has been created and when we run the SpringBoot ems application the table and columns should be created in mysql via mysql connector and hibernate auto ddl feature. We can also use flyway or liquibase for database migration and versioning.
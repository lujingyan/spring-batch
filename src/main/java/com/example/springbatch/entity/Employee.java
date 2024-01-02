package com.example.springbatch.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.context.annotation.Profile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Setter
@Getter
@Entity
@ToString
@Profile("local")
public class Employee {

    @Id
    @Column(name = "id")
    private Long id;

    @Column( name = "name")
    private String name;

    @Column( name = "age")
    private int age;

    @Column( name = "sex")
    private int sex;
}
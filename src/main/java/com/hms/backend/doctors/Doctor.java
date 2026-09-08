package com.hms.backend.doctors;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity 
@AllArgsConstructor 
@Getter 
@Setter 
public class Doctor {
    
    @Id
    private Long id;
    private String name;
    private String speciality;
    
}

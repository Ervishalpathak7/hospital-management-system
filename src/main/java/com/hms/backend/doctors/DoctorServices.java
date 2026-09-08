package com.hms.backend.doctors;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DoctorServices {

    public Doctor getDoctorById(Long id) {
        try {
            System.out.println("Inside getDoctorById Service");
            return null;
        } catch (Exception e) {
            System.out.println("Error occured inside { getDoctorById Service }  : " + e.getMessage());
            return null;
        }
    }

    public List<Doctor> getAllDoctors() {
        try {
            System.out.println("Inside getAllDoctors Service");
            return null;
        } catch (Exception e) {
            System.out.println("Error occured inside { getAllDoctors Service }  : " + e.getMessage());
            return null;
        }
    }

    public Doctor registerDoctor(Doctor doctor) {
        try {
            System.out.println("Inside registerDoctor Service");
            return null;
        } catch (Exception e) {
            System.out.println("Error occured inside { registerDoctor Service }  : " + e.getMessage());
            return null;
        }
    }

    public void deleteDoctorById(Long id) {
        try {
            System.out.println("Inside deleteDoctorById Service");
        } catch (Exception e) {
            System.out.println("Error occured inside { deleteDoctorById Service}  : " + e.getMessage());
        }
    }
}

package com.hms.backend.doctors;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/doctor")
public class DoctorControllers {

    @GetMapping("/")
    public String getAllDoctors() {
        return "Fetching all the doctors";
    }

    @PostMapping("/")
    public String registerNewDoctor(@RequestBody Doctor doctor) {
        return "registering a new doctor";
    }

    @GetMapping("/{id}")
    public String getDoctorById(@PathVariable Long id) {
        return "fetching docker with id " + id;
    }

    @PutMapping("/{id}")
    public String updateDoctorDetails(@PathVariable String id, @RequestBody Doctor updatedDoctor) {
        return "updating doctor with id " + id;
    }

    @DeleteMapping("/{id}")
    public String deleteDoctorById(@PathVariable Long id) {
        return "Deleting doctor by id " + id;

    }

}

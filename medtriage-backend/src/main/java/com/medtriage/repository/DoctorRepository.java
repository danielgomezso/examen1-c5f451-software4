package com.medtriage.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.medtriage.model.Doctor;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
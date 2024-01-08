package com.bluepal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bluepal.model.Workers;

public interface workerRepo extends JpaRepository<Workers, Integer>{

}

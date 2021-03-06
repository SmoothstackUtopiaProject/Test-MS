package com.ss.utopia.repositories;

import com.ss.utopia.models.AirplaneType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirplaneTypeRepository extends JpaRepository<AirplaneType, Integer> {}
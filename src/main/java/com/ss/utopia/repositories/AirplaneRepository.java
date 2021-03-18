package com.ss.utopia.repositories;

import java.util.List;
import java.util.Optional;

import com.ss.utopia.models.Airplane;
import com.ss.utopia.models.AirplaneType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, Integer> {
	
	@Query(value = "SELECT * FROM airplane_type", nativeQuery = true)
	List<AirplaneType> findAllAirplaneTypes();

	@Query(value = "SELECT * FROM airplane WHERE type_id = ?1", nativeQuery = true)
	List<Airplane> findAirplanesByTypeId(Integer airplaneTypeId);
	
	@Query(value = "SELECT * FROM airplane_type WHERE id = ?1", nativeQuery = true)
	Optional<AirplaneType> findAirplaneTypeByAirplaneTypeId(Integer airplaneTypeId);
}
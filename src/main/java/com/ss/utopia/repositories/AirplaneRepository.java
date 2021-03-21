package com.ss.utopia.repositories;

import java.util.List;

import com.ss.utopia.models.Airplane;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AirplaneRepository extends JpaRepository<Airplane, Integer> {

	@Query(value = "SELECT * FROM airplane WHERE type_id = ?1", nativeQuery = true)
	List<Airplane> findAirplanesByTypeId(Integer airplaneTypeId);

}

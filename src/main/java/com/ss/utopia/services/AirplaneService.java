package com.ss.utopia.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ss.utopia.exceptions.AirplaneNotFoundException;
import com.ss.utopia.exceptions.AirplaneTypeNotFoundException;
import com.ss.utopia.models.Airplane;
import com.ss.utopia.models.AirplaneType;
import com.ss.utopia.repositories.AirplaneRepository;

@Service
public class AirplaneService {
	
	@Autowired
	private AirplaneRepository airplaneRepository;

	public List<Airplane> findAll() {
		return airplaneRepository.findAll();
	}
	
	public Airplane findById(Integer id) throws AirplaneNotFoundException {
		Optional<Airplane> optionalAirplane = airplaneRepository.findById(id);
		if(!optionalAirplane.isPresent()) throw new AirplaneNotFoundException("No Airplane with ID: " + id + " exists!");
		return optionalAirplane.get();
	}
	
	public List<Airplane> findByTypeId(Integer typeId) throws AirplaneTypeNotFoundException {
		Optional<AirplaneType> optionalAirplaneType = airplaneRepository.findAirplaneTypeById(typeId);
		if(!optionalAirplaneType.isPresent()) throw new AirplaneTypeNotFoundException("No AirplaneType with ID: " + typeId + " exist!");
		return airplaneRepository.findByTypeId(typeId);
	}

	public Airplane insert(Airplane airplane) throws AirplaneTypeNotFoundException {
		Optional<AirplaneType> optionalType = airplaneRepository.findAirplaneTypeById(airplane.getTypeId());
		if(!optionalType.isPresent()) throw new AirplaneTypeNotFoundException("No AirplaneType with ID: " + airplane.getTypeId() + " exist!");
		airplane.setTypeId(optionalType.get().getId());
		return airplaneRepository.save(airplane);
	}

	public void delete(Integer id) throws AirplaneNotFoundException {
		Optional<Airplane> optionalAirplane = airplaneRepository.findById(id);
		if(!optionalAirplane.isPresent()) throw new AirplaneNotFoundException("No Airplane with ID: " + id + " exists!");
		airplaneRepository.deleteById(id);
	}

	public Airplane update(Airplane airplane) throws AirplaneNotFoundException, AirplaneTypeNotFoundException {
		Optional<Airplane> optionalAirplane = airplaneRepository.findById(airplane.getId());
		if(!optionalAirplane.isPresent()) throw new AirplaneNotFoundException("No Airplane with ID: " + airplane.getId() + " exists!");
		return insert(airplane);
	}
}
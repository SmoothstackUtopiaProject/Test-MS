//package com.ss.utopia.repositories;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//import com.ss.utopia.models.Role;
//
//import java.util.Optional;
//
//@Repository
//public interface UserRoleRepository extends JpaRepository<Role, Integer> {
//  
//  @Query(value="SELECT * FROM user_role WHERE name = ?1", nativeQuery=true)
//	Optional<Role> findByName(String name);
//  
//}
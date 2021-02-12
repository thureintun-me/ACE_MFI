package com.mfi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mfi.model.COA;

public interface CoaAccountRepository extends JpaRepository<COA, String> {
	@Query("select c from COA c where c.glType=?1") 
	COA findbyCode(String code);
}

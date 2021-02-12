package com.mfi.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mfi.model.LoanAccount;

public interface LoanAccountRepository extends JpaRepository<LoanAccount, String> {
	@Query("select l from LoanAccount l where l.createdDate between ?1 and ?2")
	List<LoanAccount> accountFilter(Date start, Date end);
}

package com.mfi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.mfi.model.Customer;
import com.mfi.model.LoanInfo;
import com.mfi.model.LoanSchedule;

public interface LoanScheduleRepository extends JpaRepository<LoanSchedule, Integer> {
	
	 public List<LoanSchedule> findByLoanInfo(LoanInfo loaninfo);
	 
	 @Modifying
	 @Query("delete from LoanSchedule ls where ls.loanInfo.LoanInfoId=?1")
	 public void deleteLoanSchedule(Integer loanInfo);

	 @Query("select ls from LoanSchedule ls where ls.loanInfo.LoanInfoId = ?1")
	 public List<LoanSchedule> getByLoanScheduleByloanId(Integer loanId);
}

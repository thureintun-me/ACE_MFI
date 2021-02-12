package com.mfi.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mfi.model.LoanInfo;
import com.mfi.model.SavingAccount;
import com.mfi.repository.SavingAccountRepository;

@Service
public class SavingAccountService {
	@Autowired
	SavingAccountRepository repo;
	public void save(SavingAccount savingAccount) {
		repo.save(savingAccount);
	}
	
	public List<SavingAccount> selectAll(){
		return repo.findAll();
	}
	
	public SavingAccount getSavingAccount(String code) {
		return repo.findbyCode(code);
	}

}

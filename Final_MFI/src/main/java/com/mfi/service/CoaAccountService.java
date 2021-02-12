package com.mfi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mfi.model.COA;
import com.mfi.model.CurrentAccount;
import com.mfi.repository.CoaAccountRepository;

@Service
public class CoaAccountService {
	@Autowired
	CoaAccountRepository repo;
	public void update(COA coa) {
		repo.save(coa);
	}
	public COA getCOA(String code) {
		return repo.findbyCode(code);
	}
}

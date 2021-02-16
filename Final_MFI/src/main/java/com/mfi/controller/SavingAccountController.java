package com.mfi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.mfi.model.CurrentAccount;
import com.mfi.model.SavingAccount;
import com.mfi.model.Transaction;
import com.mfi.service.SavingAccountService;
import com.mfi.service.TransactionService;
@Controller
public class SavingAccountController {

	@Autowired
	SavingAccountService savingAccountService;
	@Autowired
	TransactionService transactionService;
	
	@PostMapping("/savingAccountSearch")
	public String currentSearch(@Param("savingAccountNumber")String savingAccountNumber, Model model) {
		
		SavingAccount account = savingAccountService.getSavingAccount(savingAccountNumber);
		if(account!=null) {
			model.addAttribute("account",account);
			return "mfi/account/MFI_SAC_02";	
		}else {
			model.addAttribute("notfound",true);
			return "mfi/account/MFI_SAC_02";
		}
	
	}
	
	@GetMapping("/savingDetail/{number}")
	public String currentDetail(@PathVariable("number")String savingAccountNumber, Model model) {
		
		SavingAccount account = savingAccountService.getSavingAccount(savingAccountNumber);
		List<Transaction> trasactionList = transactionService.accountTransaction(savingAccountNumber);
		model.addAttribute("account",account);
		model.addAttribute("trasactionList",trasactionList);
		//System.out.println(account.getCustomer().getName());
		return "mfi/account/MFI_SAC_04";

	}
}
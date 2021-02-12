package com.mfi.controller;

import java.sql.Date;
import java.time.LocalDate;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.mfi.model.COA;
import com.mfi.model.CurrentAccount;
import com.mfi.model.Transaction;
import com.mfi.service.CoaAccountService;
import com.mfi.service.CurrentAccountService;
import com.mfi.service.TransactionService;

@Controller
public class DepositeController {
	
	@Autowired
	TransactionService transactionService;
	@Autowired
	CurrentAccountService currentAccountService;
	@Autowired
	CoaAccountService coaService;
	
	@PostMapping("/deposit")
	public String deposit(@ModelAttribute("depositBean")@Valid Transaction transaction , BindingResult result, Model model) {
		if(result.hasErrors()) {
			model.addAttribute("transaction", transaction);
			return "transaction/MFI_DSP_01";
		}
		LocalDate now = LocalDate.now();
		Date date = Date.valueOf(now);
//		get amount
		Double amount = transaction.getAmount();
		
		CurrentAccount currentAccount = currentAccountService.getAccountNumber(transaction.getAccountNumber());
		Double balance = currentAccount.getBalance()+amount;
		currentAccount.setBalance(balance);
		currentAccount.setUpdateDate(date);
		currentAccountService.update(currentAccount);
		
		COA coa = coaService.getCOA("Cash");
		Double coaBalance= coa.getAmount()-amount;
		coa.setAmount(coaBalance);
		coa.setUpdateDate(date);
		coaService.update(coa);
		
		
//		transaction.setDate(date);
		transaction.setTransactionType("Deposit");
		transaction.setCoaId(5);
		transaction.setCreatedDate(date);
		transactionService.deposit(transaction);
		
		Transaction bankTran = new Transaction();
		bankTran.setTransactionType("Cash");
		bankTran.setAccountName("Bank Cash Account");
		bankTran.setCoaId(coa.getCoaId());
		bankTran.setAccountNumber(coa.getAccountNumber());
		bankTran.setAmount(amount);
		bankTran.setNarration("Customer Deposit");
		bankTran.setCreatedDate(date);
		transactionService.bankTran(bankTran);
		model.addAttribute("depositBean", new Transaction());
		return "transaction/MFI_DSP_01";
	}
	
}

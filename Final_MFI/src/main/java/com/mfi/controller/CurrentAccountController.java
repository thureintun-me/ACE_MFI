package com.mfi.controller;


import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mfi.model.COA;
import com.mfi.model.CurrentAccount;
import com.mfi.model.Transaction;
import com.mfi.service.CoaAccountService;
import com.mfi.service.CurrentAccountService;
import com.mfi.service.TransactionService;

@Controller
public class CurrentAccountController {
	@Autowired
	CurrentAccountService currentService;
	@Autowired
	TransactionService transactionService;
	@Autowired
	CoaAccountService coaService;
	
	@PostMapping("/currentAccountSearch")
	public String currentSearch(@Param("currentAccountNumber")String currentAccountNumber, Model model) {
		
		CurrentAccount account = currentService.getAccountNumber(currentAccountNumber);
		
		
		if(account!=null) {
			model.addAttribute("account",account);
			return "mfi/account/MFI_CAC_02";	
		}else {
			model.addAttribute("notfound",true);
			return "mfi/account/MFI_CAC_02";
		}
	
		
	}
	
	@GetMapping("/currentDetail/{number}")
	public String currentDetail(@PathVariable("number")String currentAccountNumber, Model model) {
		
		CurrentAccount account = currentService.getAccountNumber(currentAccountNumber);
		List<Transaction> trasactionList = transactionService.accountTransaction(currentAccountNumber);
		model.addAttribute("account",account);
		model.addAttribute("trasactionList",trasactionList);
		//System.out.println(account.getCustomer().getName());
		return "mfi/account/MFI_CAC_04";
	}
	@GetMapping("/closeCurrentAccount/{number}")
	public String closeCurrentAccount(@PathVariable("number")String currentAccountNumber,Model model) {
		
		System.out.println(currentAccountNumber);
		LocalDate now = LocalDate.now();
		Date date = Date.valueOf(now);
		
		CurrentAccount account = currentService.getAccountNumber(currentAccountNumber);
		Double amount = account.getBalance();
		
		account.setBalance(0.0);
		account.setAccountStatus(false);
		account.setUpdateDate(date);
		currentService.update(account);
		
		COA coa = coaService.getCOA("Cash");
		coa.setAmount(coa.getAmount()+amount);
		coa.setUpdateDate(date);
		coaService.update(coa);
		
		//current account transaction
		Transaction currentTransaction = new Transaction();
		currentTransaction.setTransactionType("Withdraw");
		currentTransaction.setAccountName(account.getCustomer().getName());
		currentTransaction.setCoaId(5);
		currentTransaction.setAccountNumber(account.getCurrentAccountNumber());
		currentTransaction.setAmount(amount);
		currentTransaction.setNarration("Account Closing");
		currentTransaction.setCreatedDate(date);
		transactionService.deposit(currentTransaction);
		
		
//		coa account transaction
		Transaction bankTran = new Transaction();
		bankTran.setTransactionType("Cash");
		bankTran.setAccountName("Bank Cash Account");
		bankTran.setCoaId(coa.getCoaId());
		bankTran.setAccountNumber(coa.getAccountNumber());
		bankTran.setAmount(coa.getAmount()+amount);
		bankTran.setNarration("Account Closing");
		bankTran.setCreatedDate(date);
		transactionService.bankTran(bankTran);
		
		return "mfi/account/MFI_CAC_02";
	}
	
	@GetMapping("/reopenCurrentAccount/{number}")
	public String reopenCurrentAccount(@PathVariable("number")String currentAccountNumber,Model model) {
		
		System.out.println(currentAccountNumber);
		LocalDate now = LocalDate.now();
		Date date = Date.valueOf(now);
		
		CurrentAccount account = currentService.getAccountNumber(currentAccountNumber);
		Double amount =1000.00;
		
		account.setBalance(amount);
		account.setAccountStatus(true);
		account.setUpdateDate(date);
		currentService.update(account);
		
		COA coa = coaService.getCOA("Cash");
		coa.setAmount(coa.getAmount()-amount);
		coa.setUpdateDate(date);
		coaService.update(coa);
		
		//current account transaction
		Transaction currentTransaction = new Transaction();
		currentTransaction.setTransactionType("Deposit");
		currentTransaction.setAccountName(account.getCustomer().getName());
		currentTransaction.setCoaId(5);
		currentTransaction.setAccountNumber(account.getCurrentAccountNumber());
		currentTransaction.setAmount(amount);
		currentTransaction.setNarration("Account Closing");
		currentTransaction.setCreatedDate(date);
		transactionService.deposit(currentTransaction);
		
		
//		coa account transaction
		Transaction bankTran = new Transaction();
		bankTran.setTransactionType("Cash");
		bankTran.setAccountName("Bank Cash Account");
		bankTran.setCoaId(coa.getCoaId());
		bankTran.setAccountNumber(coa.getAccountNumber());
		bankTran.setAmount(coa.getAmount()-amount);
		bankTran.setNarration("Account Closing");
		bankTran.setCreatedDate(date);
		transactionService.bankTran(bankTran);
		
		return "mfi/account/MFI_CAC_02";
	}
	
	@PostMapping("/currentAccountTransactionSearch/{number}")
	public String transactionSearch(@Param("start")String start, @Param("end")String end,@PathVariable("number")String currentAccountNumber, Model model) {
		
		
			Date startdate = Date.valueOf(start);
			Date enddate = Date.valueOf(end);
			List<Transaction> trasactionList = transactionService.recordByAccount(startdate, enddate, currentAccountNumber);
			model.addAttribute("trasactionList",trasactionList);
			
		
		
		CurrentAccount account = currentService.getAccountNumber(currentAccountNumber);
		model.addAttribute("account",account);
		
		return "mfi/account/MFI_CAC_04";
	}

}
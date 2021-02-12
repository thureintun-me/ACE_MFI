package com.mfi.controller;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mfi.formmodel.LoanInfoForm;
import com.mfi.model.COA;
import com.mfi.model.CurrentAccount;
import com.mfi.model.LoanInfo;
import com.mfi.model.SavingAccount;
import com.mfi.model.Transaction;
import com.mfi.service.CoaAccountService;
import com.mfi.service.CurrentAccountService;
import com.mfi.service.DisbursementService;
import com.mfi.service.LoanInfoService;
import com.mfi.service.SavingAccountService;
import com.mfi.service.TransactionService;

@Controller
public class DisbursementController {
	@Autowired
	DisbursementService disService;
	@Autowired
	private TransactionService transactionService;
	@Autowired
	private LoanInfoService loanService;
	@Autowired
	private CurrentAccountService currentService;
	@Autowired
	CoaAccountService coaService;
	@Autowired
	private SavingAccountService savingService;
	
	@RequestMapping("/addDisbursement/{id}")
	public String disbursement(@PathVariable("id")int id,Model model) {
		LocalDate now = LocalDate.now();
		Date date = Date.valueOf(now);
		
		LoanInfo loanInfo = loanService.selectOne(id);
		String customerCode = loanInfo.getCustomer().getCustomerCode();
		double loanAmount =  loanInfo.getLoanAmount();
		double balance = loanAmount- 1000.0;
		
		CurrentAccount currentAccount = currentService.getCurrentAccount(customerCode);
		currentAccount.setBalance(currentAccount.getBalance()+balance);
		currentAccount.setUpdateDate(date);
//		System.out.println(currentAccount.getBalance());
		currentService.save(currentAccount);
		
		SavingAccount savingAccount = savingService.getSavingAccount(customerCode);
		savingAccount.setBalance(savingAccount.getBalance()+1000.0);
		savingAccount.setUpdateDate(date);
//		System.out.println(savingAccount.getSavingAccountNumber());
		savingService.save(savingAccount);
		
		COA coa = coaService.getCOA("Cash");
		coa.setAmount(coa.getAmount()- loanAmount);
		coa.setUpdateDate(date);
		coaService.update(coa);
		
//		current account transaction
		Transaction currentTransaction = new Transaction();
		currentTransaction.setTransactionType("Deposit");
		currentTransaction.setAccountName(loanInfo.getCustomer().getName());
		currentTransaction.setCoaId(5);
		currentTransaction.setAccountNumber(currentAccount.getCurrentAccountNumber());
		currentTransaction.setAmount(balance);
		currentTransaction.setNarration("Loan Disbursement");
		currentTransaction.setCreatedDate(date);
		transactionService.disbursement(currentTransaction);
		
//		saving account transaction
		Transaction transaction = new Transaction();
		transaction.setTransactionType("Deposit");
		transaction.setAccountName(loanInfo.getCustomer().getName());
		transaction.setCoaId(4);
		transaction.setAccountNumber(savingAccount.getSavingAccountNumber());
		transaction.setAmount(1000.0);
		transaction.setNarration("Loan Disbursement");
		transaction.setCreatedDate(date);
		transactionService.disbursement(transaction);
		
//		coa account transaction
		Transaction bankTran = new Transaction();
		bankTran.setTransactionType("Cash");
		bankTran.setAccountName("Bank Cash Account");
		bankTran.setCoaId(coa.getCoaId());
		bankTran.setAccountNumber(coa.getAccountNumber());
		bankTran.setAmount(loanAmount);
		bankTran.setNarration("Loan Disbursement");
		bankTran.setCreatedDate(date);
		transactionService.bankTran(bankTran);
		
		loanInfo.setStatus("Disbursement");
		loanInfo.setUpdateDate(now);
		loanService.save(loanInfo);
		return "redirect:/disbursement";
	}
}

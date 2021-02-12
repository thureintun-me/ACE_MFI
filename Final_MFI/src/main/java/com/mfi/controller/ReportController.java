package com.mfi.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mfi.model.CurrentAccount;
import com.mfi.model.Customer;
import com.mfi.model.LoanAccount;
import com.mfi.model.LoanInfo;
import com.mfi.model.LoanSchedule;
import com.mfi.model.SavingAccount;
import com.mfi.model.Transaction;
import com.mfi.service.LoanInfoService;
import com.mfi.service.ReportService;

@Controller
public class ReportController {
	@Autowired
	ReportService reportService;
	@Autowired
	LoanInfoService loanInfoService;
	

	@PostMapping("/transactionSearch")
	public String transactionSearch(@Param("start")String start, @Param("end")String end, Model model) {
		Date startdate = Date.valueOf(start);
		Date enddate = Date.valueOf(end);
//		System.out.println(enddate);
		List<Transaction> transList = reportService.selectByStartDate(startdate,enddate);
		model.addAttribute("tranList",transList);
		return "mfi/reports/MFI_RPT_TRS";
	}
	
	@PostMapping("/customerFilter")
	public String customerFilter(@Param("start")String start, @Param("end")String end, Model model) {
		Date startdate = Date.valueOf(start);
		Date enddate = Date.valueOf(end);
		List<Customer> customerList = reportService.customerFilter(startdate, enddate);
		model.addAttribute("customerList",customerList);
		return "mfi/reports/MFI_RPT_CUS";
	}
	
	@PostMapping("/overdueFilter")
	public String overdueFilter(@Param("start")String start, @Param("end")String end, Model model) {
		
		LocalDate startdate = LocalDate.parse(start);
		LocalDate enddate = LocalDate.parse(end);
		List<LoanSchedule> LoanSchdule = reportService.overDueFilter(startdate,enddate,"active");
		model.addAttribute("loanSchdule",LoanSchdule);
		return "mfi/reports/MFI_RPT_ODD";
	}
	
	@PostMapping("/accountFilter")
	public String accountFilter(@Param("start")String start, @Param("end")String end, Model model) {
		
		Date startdate = Date.valueOf(start);
		Date enddate = Date.valueOf(end);
		
		List<CurrentAccount> currentAccount = reportService.currentAccountFilter(startdate, enddate);
		List<SavingAccount> savingAccount = reportService.savingAccountFilter(startdate,enddate);
		List<LoanAccount> loanAccount = reportService.loanAccountFilter(startdate,enddate);
		model.addAttribute("currentAccount",currentAccount);
		model.addAttribute("savingAccount",savingAccount);
		model.addAttribute("loanAccount",loanAccount);
		return "mfi/reports/MFI_RPT_CLB";
	}
	
	@RequestMapping("/loanOutstanding")
	public String loanOutstandingBalance(Model model) {
		List<LoanInfo> loanInfo = loanInfoService.selectSatus("Disbursement");
		List<LoanSchedule> loan = new ArrayList<LoanSchedule>();
		int allLoan = 0;
		
		for(int i=0; i<loanInfo.size(); i++) {
			Integer loanInfoID =  loanInfo.get(i).getLoanInfoId();
			LoanSchedule completeSchdule = reportService.completeSchdule("Complete",loanInfoID);
			
			loan.add(completeSchdule);
			
			List<LoanSchedule> findAllLoan = reportService.findAllLoan(i);
			allLoan=findAllLoan.size();
			
			model.addAttribute("allLoan",allLoan);
			Double completeAmount = reportService.getSum("Complete", loanInfoID);
			System.out.println(completeAmount);
			
			model.addAttribute("completeAmount",completeAmount);
		}
		
		model.addAttribute("completeSchdule",loan);
	
			return "mfi/reports/MFI_RPT_LOB";
	}
}

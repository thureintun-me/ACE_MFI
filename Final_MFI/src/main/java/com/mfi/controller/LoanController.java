package com.mfi.controller;

import java.sql.Date;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;



import java.util.ArrayList;

import java.util.List;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;


import com.mfi.formmodel.LoanInfoForm;
import com.mfi.model.CurrentAccount;
import com.mfi.model.Customer;
import com.mfi.model.LoanAccount;
import com.mfi.model.LoanInfo;
import com.mfi.model.LoanSchedule;
import com.mfi.model.SavingAccount;
import com.mfi.service.CurrentAccountService;
import com.mfi.service.LoanAccountService;
import com.mfi.service.LoanInfoService;
import com.mfi.service.LoanScheduleService;
import com.mfi.service.SavingAccountService;

@Controller
public class LoanController {
	 private static DecimalFormat df2 = new DecimalFormat("#.##");
	@Autowired
	LoanInfoService loanservice;
	
	@Autowired
	CurrentAccountService currentService;
	@Autowired
	SavingAccountService savingService;
	@Autowired
	LoanAccountService loanAccountService;
	
	@Autowired
	LoanScheduleService loanScheduleservice;

	@RequestMapping("/searchCRM")
	public ModelAndView searchCustomer(Model model,
			@RequestParam(name = "crmSearch", required = false) String crmSearch) {
		List<Customer> customers = new ArrayList<>();
		customers = loanservice.searchCustomer(crmSearch);
		model.addAttribute("customerList", customers);
		model.addAttribute("crmSearch", crmSearch);
		return new ModelAndView("mfi/loan/MFI_LON_01");
	}
	
	
	//LoanInfoSearching
	@RequestMapping("/searchloanInfo")
	public ModelAndView searchloanInfo(Model model,
			@RequestParam(name = "loanSearch", required = false) String loanSearch) {
		List<LoanInfo> loanInfo = new ArrayList<>();
//		System.out.println("Sending data"+loanSearch);
		loanInfo = loanservice.getLoanInfo(loanSearch);
//		System.out.println("Loan Info data"+loanInfo);
		model.addAttribute("loanInfolist", loanInfo);
		model.addAttribute("loanSearch", loanSearch);
		return new ModelAndView("loan/MFI_LON_02");
	}
	
	
	
	@RequestMapping("/caculateCreditScore/{id}")
	public String calculateCreditScore(@PathVariable("id") String crmCode, Model model) {
		Customer customer = loanservice.searchCrmByCode(crmCode);
		int creditTotal = 0;
		int creditIncome = 0;
		int creditAge = 0;
		int creditGender = 0;
		// Customer MonthlyIncome
		double crmIncome = customer.getMonthlyIncome();
		if (crmIncome >= 200000 && crmIncome < 500000) {
			creditIncome = 20;
		}
		else if (crmIncome >= 500000 && crmIncome < 1000000) {
			creditIncome = 25;
		} else if (crmIncome >= 1000000 && crmIncome < 1500000) {
			creditIncome = 30;
		} 
		else {
			creditIncome = 35;
		}
		System.out.println("creditIncome" + creditIncome);
		// Customer Age
		DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
		Date crmDob = customer.getDob();
		String crmAge = dateFormat.format(crmDob);
		LocalDate now = LocalDate.now();
		String currentDate = now.toString();
		int crmYear = Integer.parseInt(crmAge.substring(0, 4));
		int currentYear = Integer.parseInt(currentDate.substring(0, 4));
		int cumRealAge = currentYear - crmYear;
		// Customer Gender
		String crmGender = customer.getGender();
		if (cumRealAge >= 18 && cumRealAge < 25) {
			creditAge = 20;
		} else if (cumRealAge >= 25 && cumRealAge < 35) {
			creditAge = 35;
		} else if (cumRealAge >= 35 && cumRealAge < 45) {
			creditAge = 30;
		} else {
			creditAge = 25;
		}
		if (crmGender == "Male") {
			creditGender = 10;
		} else {
			creditGender = 15;
		}
		creditTotal = creditIncome + creditAge + creditGender;
		LoanInfoForm loan = new LoanInfoForm();
		loan.setCreditScore(creditTotal);
		loan.setCustomerCode(customer.getCustomerCode());
		model.addAttribute("loanInfoForm",loan);
		System.out.println("LoanInfoForm object"+loan);
		//model.addAttribute("customer", customer.getCustomerCode());
		//model.addAttribute("creditTotal", creditTotal);
		
		return "mfi/loan/MFI_LON_01_01";

	}
	
	
	 @PostMapping("/addLoanInfo")
	    public String addLoanInfo(@Valid  @ModelAttribute("loanInfoForm")  LoanInfoForm loanInfoForm,BindingResult bindingResult,Model model) {
	   

	     
		 if (bindingResult.hasErrors()) {
	        	model.addAttribute("loanInfoForm", loanInfoForm);
	        	 return "mfi/loan/MFI_LON_01_01";
	        }
	        
	       int createdUser=1;
	       int updatedUser=0;
	       LocalDate createdDate=LocalDate.now();
	       LocalDate updatedDate=null;
	       java.util.Date date = new java.util.Date();
	       java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
	      
	       double interstRate=loanInfoForm.getInterestRate()/100;
	       Customer customer=loanservice.searchCrmByCode(loanInfoForm.getCustomerCode());
	       LoanInfo loanInfo = loanservice.addLoanInfo( customer, loanInfoForm.getLoanAmount(), loanInfoForm.getRegisterDate(), loanInfoForm.getLoanTermYear(),
	    		   interstRate, loanInfoForm.getDescription(), loanInfoForm.getCreditScore(), "pending",
	    		   createdUser,createdDate ,updatedUser , updatedDate);
	      Integer loanId= loanInfo.getLoanInfoId();
	      LoanInfo loan=loanservice.getLoanInfobyId(loanId);
	      int months=loan.getLoanTermYear()*12;
			 double monthlyIR=loan.getInterestRate()/12;
			 Double amount=loan.getLoanAmount();
			 LocalDate futureRegisterDate;
			 LocalDate registerDate=loan.getRegisterDate().toLocalDate().plusDays(5);
		       Double balance=amount;
		       double monthlyPayment=Double.parseDouble(df2.format(loanScheduleservice.calMonthlyPayment(amount, monthlyIR, months))) ;
				double irPaid;
				double amountPaid;
				double newBalance;
				
				for(int payment_tern_no=1;payment_tern_no<=months;payment_tern_no++) {
					futureRegisterDate=registerDate;
					irPaid=Double.parseDouble(df2.format(balance*monthlyIR));
					amountPaid=Double.parseDouble(df2.format(monthlyPayment-irPaid));
					newBalance=Double.parseDouble(df2.format(balance-amountPaid));
					
					loanScheduleservice.addLoanScheduleForm(payment_tern_no, futureRegisterDate, balance, null, monthlyPayment, irPaid, null, 
							newBalance,"active", createdUser, createdDate, updatedUser, updatedDate, loan,amountPaid);
					balance=newBalance;
					registerDate=futureRegisterDate.plusMonths(1);
				}
//	       return "redirect:/addLoanSchedule?date="+timestamp;
	       return "mfi/loan/MFI_LON_01";
	       
	    }
	 
 
	 @GetMapping("/loanViewDetail/{id}")
	    public String loanViewDetail(@PathVariable("id") Integer loaninfo_id , Model model) {
	    	   LoanInfo loaninfo = loanservice.getLoanInfobyId(loaninfo_id);
//	    	   System.out.println("loaninfo"+loaninfo);
	            model.addAttribute("loaninfo", loaninfo);
	            List<LoanSchedule> loanschedule=loanScheduleservice.getLoanSchByLoanInfoId(loaninfo);
	            model.addAttribute("loanschedules",loanschedule);
	            return "mfi/loan/MFI_LON_04";
	    }
	 
	 
	 @GetMapping("/loanEditView/{id}")
		public String loanEditView(@PathVariable("id") Integer loaninfo_id , Model model) {
		 LoanInfo loaninfo = loanservice.getLoanInfobyId(loaninfo_id);
		 LoanInfoForm loaninfoForm=new LoanInfoForm();
		 loaninfoForm.setLoanInfoId(loaninfo.getLoanInfoId());
		 loaninfoForm.setCustomerCode(loaninfo.getCustomer().getCustomerCode());
		 loaninfoForm.setLoanAmount(loaninfo.getLoanAmount());
		 loaninfoForm.setRegisterDate(loaninfo.getRegisterDate());
		 loaninfoForm.setLoanTermYear(loaninfo.getLoanTermYear());
		 loaninfoForm.setInterestRate(loaninfo.getInterestRate()*100);
		 loaninfoForm.setDescription(loaninfo.getDescription());
		 loaninfoForm.setCreditScore(loaninfo.getCreditScore());
		 loaninfoForm.setStatus(loaninfo.getStatus());
		 loaninfoForm.setCreatedUser(loaninfo.getCreatedUser());
//		 DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
//		 String createdDate = dateFormat.format(loaninfo.getCreatedDate());
//		 Date date = java.sql.Date.valueOf(createdDate);
		 loaninfoForm.setCreatedDate(loaninfo.getCreatedDate());
		 loaninfoForm.setUpdateUser(loaninfo.getUpdateUser());
		 loaninfoForm.setUpdateDate(loaninfo.getUpdateDate());
		 model.addAttribute("loaninfo", loaninfoForm);
		 return "mfi/loan/MFI_LON_03";
	 }
	 
	 
	 @PostMapping("/editLoanInfo/{id}")
	    public String editLoanInfoe(@Valid  @ModelAttribute("loaninfo")  LoanInfoForm loanInfoForm,BindingResult bindingResult,@PathVariable("id") Integer loaninfo_id , Model model) {
		 if (bindingResult.hasErrors()) {
	        	model.addAttribute("loanInfoForm", loanInfoForm);
	        	 return "mfi/loan/MFI_LON_03";
	        }
		  int updatedUser=0;
		  LocalDate updatedDate=LocalDate.now();
		  double interstRate=loanInfoForm.getInterestRate()/100;
		 LoanInfo updateLoanInfo=loanservice.getLoanInfobyId(loaninfo_id); 
		 Customer customer=loanservice.searchCrmByCode(loanInfoForm.getCustomerCode());
		 updateLoanInfo.setCustomer(customer);
		 updateLoanInfo.setLoanAmount(loanInfoForm.getLoanAmount());
		 updateLoanInfo.setRegisterDate(loanInfoForm.getRegisterDate());
		 updateLoanInfo.setLoanTermYear(loanInfoForm.getLoanTermYear());
		 updateLoanInfo.setInterestRate(interstRate);
		 updateLoanInfo.setDescription(loanInfoForm.getDescription());
		 updateLoanInfo.setCreditScore(loanInfoForm.getCreditScore());
		 updateLoanInfo.setStatus(loanInfoForm.getStatus());
		 updateLoanInfo.setCreatedUser(loanInfoForm.getCreatedUser());
		 updateLoanInfo.setCreatedDate(loanInfoForm.getCreatedDate());
		 updateLoanInfo.setUpdateUser(updatedUser);
		 updateLoanInfo.setUpdateDate(updatedDate);
		 LoanInfo loanInfo=loanservice.updateLoanInfo(updateLoanInfo);
	       
////	       Customer customer=loanservice.searchCrmByCode(loanInfoForm.getCustomerCode());
//	       LoanInfo loanInfo = loanservice.addLoanInfo( customer, loanInfoForm.getLoanAmount(), loanInfoForm.getRegisterDate(), loanInfoForm.getLoanTermYear(),
//	    		   interstRate, loanInfoForm.getDescription(), loanInfoForm.getCreditScore(), "pending",
//	    		   createdUser,createdDate ,updatedUser , updatedDate);
	      Integer loanId= loanInfo.getLoanInfoId();
	      LoanInfo loan=loanservice.getLoanInfobyId(loanId);
	      List<LoanSchedule> loanschedule=loanScheduleservice.getLoanSchByLoanInfoId(loan);
//	      System.out.println(loanschedule);
	      loanScheduleservice.deleteLoanschByLoanInfoId(loanId);

	      int months=loan.getLoanTermYear()*12;
			 double monthlyIR=loan.getInterestRate()/12;
			 Double amount=loan.getLoanAmount();
			 LocalDate futureRegisterDate;
			 LocalDate registerDate=loan.getRegisterDate().toLocalDate().plusDays(5);
		       Double balance=amount;
		       double monthlyPayment=Double.parseDouble(df2.format(loanScheduleservice.calMonthlyPayment(amount, monthlyIR, months))) ;
				double irPaid;
				double amountPaid;
				double newBalance;
				
				for(int payment_tern_no=1;payment_tern_no<=months;payment_tern_no++) {
					futureRegisterDate=registerDate;
					irPaid=Double.parseDouble(df2.format(balance*monthlyIR));
					amountPaid=Double.parseDouble(df2.format(monthlyPayment-irPaid));
					newBalance=Double.parseDouble(df2.format(balance-amountPaid));
					
					loanScheduleservice.addLoanScheduleForm(payment_tern_no, futureRegisterDate, balance, null, monthlyPayment, irPaid, null, 
							newBalance,"active", loan.getCreatedUser(), loan.getCreatedDate(), updatedUser, updatedDate, loan,amountPaid);
					balance=newBalance;
					registerDate=futureRegisterDate.plusMonths(1);
				}
		 
		 return "mfi/loan/MFI_LON_02";
	 }
	 
	 @GetMapping("/checkerLoanList")
		public String checkerLoanList(Model model) {
			String status = "pending";
			model.addAttribute("loanList",loanservice.selectSatus(status));
			return "mfi/loan/MFI_LON_03_01";
		}
	 
	 @GetMapping("/makerLoanList")
		public String makerLoanList(Model model) {
			
			model.addAttribute("loanList",loanservice.selectAll());
			return "mfi/loan/MFI_LON_03_02";
		}

	 @GetMapping("/checkerLoanDetail/{id}")
	    public String checkerLoanDetail(@PathVariable("id") Integer loaninfo_id , Model model) {
	    	   LoanInfo loaninfo = loanservice.getLoanInfobyId(loaninfo_id);
//	    	   System.out.println("loaninfo"+loaninfo);
	            model.addAttribute("loaninfo", loaninfo);
	            List<LoanSchedule> loanschedule=loanScheduleservice.getLoanSchByLoanInfoId(loaninfo);
	            model.addAttribute("loanschedules",loanschedule);
	            return "mfi/loan/MFI_LON_04_01";
	    }
	 
	 @RequestMapping("approveLoan/{id}")
		public String approveLoan(@PathVariable("id")int id,Model model) {
			LocalDate now = LocalDate.now();
			Date date = Date.valueOf(now);
			LoanInfo loanList = loanservice.selectOne(id);
			
//			Current Account Create
//			Double balance = loanList.getLoanAmount() - 1000;
			CurrentAccount current = new CurrentAccount();
			current.setCustomer(loanList.getCustomer());
			current.setAccountStatus(true);
			current.setBalance(0.0);
			
			List<CurrentAccount> currentList= currentService.selectAll();
			if(currentList.isEmpty()) {
				current.setCurrentAccountNumber("1101000011110001");
			}
			else
			{
				int i = currentList.size();
				int gid = i+1;
				String number = String.format("110100001111%04d", gid);
				current.setCurrentAccountNumber(number);
			}
			
			current.setCreatedDate(date);
			currentService.save(current);

//			saving account create 
			SavingAccount saving = new SavingAccount();
			saving.setCustomer(loanList.getCustomer());
			saving.setAccountStatus(true);
			saving.setBalance(0.0);
			
			List<SavingAccount> savingList= savingService.selectAll();
			if(savingList.isEmpty()) {
				saving.setSavingAccountNumber("1102000011110001");
			}
			else
			{
				int i = savingList.size();
				int gid = i+1;
				String number = String.format("110200001111%04d", gid);
				saving.setSavingAccountNumber(number);
			}
			saving.setCreatedDate(date);
			savingService.save(saving);
			
//			loan account create
			SavingAccount savingAccNumber = savingService.getSavingAccount(loanList.getCustomer().getCustomerCode());
			CurrentAccount currentAccNumber = currentService.getCurrentAccount(loanList.getCustomer().getCustomerCode());
			System.out.println(savingAccNumber.getCustomer().getCustomerCode());
			LoanAccount loan = new LoanAccount();
			loan.setLoanAmount(loanList.getLoanAmount());
			loan.setAccountStatus(true);
			loan.setCustomer(loanList.getCustomer());
			loan.setSavingAccount(savingAccNumber);
			loan.setCurrentAccount(currentAccNumber);
			loan.setLoanInfo(loanList);
			List<LoanAccount> list= loanAccountService.selectAll();
			if(list.isEmpty()) {
				loan.setLoanAccountNumber("1103000011110001");
			}
			else
			{
				int i = list.size();
				int gid = i+1;
				String number = String.format("110300001111%04d", gid);
				loan.setLoanAccountNumber(number);
			}
			loan.setCreatedDate(date);
			loanAccountService.save(loan);
			
			loanList.setStatus("approve");
			loanList.setUpdateDate(now);
			loanservice.save(loanList);
			
			return "redirect:/checkerLoanList";
		}
	 
}
















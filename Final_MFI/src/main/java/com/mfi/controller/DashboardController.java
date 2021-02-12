package com.mfi.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mfi.model.BlackList;
import com.mfi.model.CurrentAccount;
import com.mfi.model.Customer;
import com.mfi.model.LoanAccount;
import com.mfi.model.LoanInfo;
import com.mfi.model.LoanSchedule;
import com.mfi.model.Role;
import com.mfi.model.SavingAccount;
import com.mfi.model.Transaction;
import com.mfi.model.User;
import com.mfi.service.CurrentAccountService;
import com.mfi.service.CustomerService;
import com.mfi.service.DisbursementService;
import com.mfi.service.LoanAccountService;
import com.mfi.service.LoanInfoService;
import com.mfi.service.RepaymentService;
import com.mfi.service.ReportService;
import com.mfi.service.RoleService;
import com.mfi.service.SavingAccountService;
import com.mfi.service.TransactionService;
import com.mfi.service.UserService;

@Controller
public class DashboardController {
	@Autowired
	LoanInfoService loanInfoService;
	@Autowired
	DisbursementService disService;
	@Autowired
	RepaymentService repaymentService;
	@Autowired
	ReportService reportService;
	@Autowired
	CustomerService cusService;
	@Autowired
	CurrentAccountService currentService;
	@Autowired
	SavingAccountService savingService;
	@Autowired
	LoanAccountService loanAccountService;
	@Autowired
	RoleService roleService;
	@Autowired
	UserService userService;

	@RequestMapping("/checker")
	public String checkerDashboard(Model model) {
		String status = "pending";
		List<LoanInfo> loanList = loanInfoService.selectSatus(status);
		int pendingloanList = loanList.size();
		// customer
		List<Customer> customerList = cusService.selectAll();
		int totalCus = customerList.size();
		model.addAttribute("totalCus", totalCus);
		model.addAttribute("pendingloanList", pendingloanList);
		// User
		List<User> userList = userService.selectAll();
		int totalCur = userList.size();
		model.addAttribute("totalCur", totalCur);

		// totalLoanAmount
		List<LoanAccount> loanAcc = loanAccountService.selectAll();
		double totalLoan = 0;
		for (LoanAccount loan : loanAcc) {
			totalLoan += loan.getLoanAmount();
		}
		model.addAttribute("totalLoan", totalLoan);

		return "mfi/dashboard/checker_dashboard";
	}

	@RequestMapping("/maker")
	public String makerDashboard(Model model) {
		String status = "progress";
		List<LoanInfo> loanList = loanInfoService.selectSatus(status);
		int progressloanList = loanList.size();
		model.addAttribute("progressloanList", progressloanList);
		
		// customer
		List<Customer> customerList = cusService.selectAll();
		int totalCus = customerList.size();
		model.addAttribute("totalCus", totalCus);

		// User
		List<User> userList = userService.selectAll();
		int totalCur = userList.size();
		model.addAttribute("totalCur", totalCur);

		// totalLoanAmount
		List<LoanAccount> loanAcc = loanAccountService.selectAll();
		double totalLoan = 0;
		for (LoanAccount loan : loanAcc) {
			totalLoan += loan.getLoanAmount();
		}
		model.addAttribute("totalLoan", totalLoan);
		return "mfi/dashboard/maker_dashboard";
	}

	@RequestMapping("/role")
	public String role(Model model) {
		model.addAttribute("roleBean", new Role());
		model.addAttribute("role", roleService.selectAll());
		return "mfi/user/MFI_ROL_01";
	}

	@RequestMapping("/user")
	public String user(Model model) {
		model.addAttribute("userBean", new User());
		return "mfi/user/MFI_CUR_02";
	}

	@RequestMapping("/customerRegister")
	public String customerRegistration(Model model) {
		model.addAttribute("crmBean", new Customer());
		return "mfi/customer/MFI_CRM_01";
	}

	@RequestMapping("/customerSearch")
	public String customerSearch(Model model) {
		model.addAttribute("crmBean", new Customer());
		return "mfi/customer/MFI_CRM_02";
	}

	@RequestMapping("/loanRegister")
	public String loanRegistration() {

		return "mfi/loan/MFI_LON_01";
	}

	@RequestMapping("/loanSearch")
	public String loanSearch() {
		return "mfi/loan/MFI_LON_02";
	}

	@RequestMapping("/currentAccount")
	public String currentAccount(Model model) {

		return "mfi/account/MFI_CAC_02";
	}

	@RequestMapping("/savingAccount")
	public String savingAccount() {
		return "mfi/account/MFI_SAC_02";
	}

	@RequestMapping("/loanAccount")
	public String loanAccount() {
		return "mfi/account/MFI_LAC_02";
	}

	@RequestMapping("/deposit")
	public String deposit(Model model) {
		model.addAttribute("depositBean", new Transaction());
		return "mfi/transaction/MFI_DSP_01";
	}

	@RequestMapping("/withdraw")
	public String withdraw(Model model) {
		model.addAttribute("withdrawBean", new Transaction());
		return "mfi/transaction/MFI_WTD_01";
	}

	@RequestMapping("/disbursement")
	public String disbursement(Model model) {
		String status = "approve";
		model.addAttribute("disList", disService.selectDisbursement(status));
		return "mfi/transaction/MFI_DIS_01";
	}

	@RequestMapping("/repayment")
	public String repayment() {
		return "mfi/transaction/MFI_RPM_02";
	}

	@RequestMapping("/coa")
	public String coa() {
		return "mfi/coa/MFI_COA_02";
	}

	@RequestMapping("/createBlackList")
	public String createBlackList() {
		return "mfi/blacklist/MFI_BLT_01";
	}

	@RequestMapping("/searchBlackList")
	public String searchBlackList() {
		return "mfi/blacklist/MFI_BLT_02";
	}

	@RequestMapping("triBalance")
	public String triBalance() {
		return "mfi/reports/MFI_RPT_TRI";
	}

	@RequestMapping("transactionListing")
	public String transactionListing(Model model) {
		LocalDate now = LocalDate.now();
		Date date = Date.valueOf(now);
		List<Transaction> tranList = reportService.selectAllTransaction(date);

		model.addAttribute("tranList", tranList);
		return "mfi/reports/MFI_RPT_TRS";
	}

	@RequestMapping("customerListing")
	public String customerListing(Model model) {
		LocalDate now = LocalDate.now();
		Date date = Date.valueOf(now);
		List<Customer> customerList = reportService.findCustomerByTodayDate(date);
		model.addAttribute("customerList", customerList);
		return "mfi/reports/MFI_RPT_CUS";
	}

	@RequestMapping("accountListing")
	public String accountListing(Model model) {
		List<CurrentAccount> currentAccount = currentService.selectAll();
		List<SavingAccount> savingAccount = savingService.selectAll();
		List<LoanAccount> loanAccount = loanAccountService.selectAll();
		model.addAttribute("currentAccount", currentAccount);
		model.addAttribute("savingAccount", savingAccount);
		model.addAttribute("loanAccount", loanAccount);
		return "mfi/reports/MFI_RPT_ACC";
	}

	@RequestMapping("overdueListing")
	public String overdueListing(Model model) {
		LocalDate now = LocalDate.now();
		List<LoanSchedule> LoanSchdule = repaymentService.selectByDueDate(now, "active");
		model.addAttribute("loanSchdule", LoanSchdule);
		return "mfi/reports/MFI_RPT_ODD";
	}

	@RequestMapping("customerLedger")
	public String customerLedgerBalance(Model model) {
		List<CurrentAccount> currentAccount = currentService.selectAll();
		List<SavingAccount> savingAccount = savingService.selectAll();
		List<LoanAccount> loanAccount = loanAccountService.selectAll();
		model.addAttribute("currentAccount", currentAccount);
		model.addAttribute("savingAccount", savingAccount);
		model.addAttribute("loanAccount", loanAccount);

		return "mfi/reports/MFI_RPT_CLB";
	}

}

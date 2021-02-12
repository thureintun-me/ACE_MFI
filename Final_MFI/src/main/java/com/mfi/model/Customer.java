package com.mfi.model;



import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.Id;import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;

@Entity
public class Customer {

	@Id
	private String customerCode;
	@NotEmpty
	private String name;
	private String nrc;
	private String phone;
	private Date dob;
	private String email;
	private String gender;
	private String address;
	private String currentJob;
	private String positon;
	private String companyName;
	private double monthlyIncome;
	private boolean delStatus;
	private int createdUser;
	private Date createdDate;
	private int updateUser;
	private Date updateDate;
	
	@OneToOne(mappedBy = "customer")
	@JoinColumn(name="loan_account")
	private LoanAccount loanAccount;
	
	@OneToOne(mappedBy = "customer")
	@JoinColumn(name="saving_account")
	private SavingAccount savingAccount;
	
	@OneToOne(mappedBy = "customer")
	@JoinColumn(name="current_account")
	private CurrentAccount currentAccount;
	

	
	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNrc() {
		return nrc;
	}

	public void setNrc(String nrc) {
		this.nrc = nrc;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCurrentJob() {
		return currentJob;
	}

	public void setCurrentJob(String currentJob) {
		this.currentJob = currentJob;
	}

	public String getPositon() {
		return positon;
	}

	public void setPositon(String positon) {
		this.positon = positon;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public double getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(double monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public boolean isDelStatus() {
		return delStatus;
	}

	public void setDelStatus(boolean delStatus) {
		this.delStatus = delStatus;
	}

	public int getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(int createdUser) {
		this.createdUser = createdUser;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(int updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}

	public LoanAccount getLoanAccount() {
		return loanAccount;
	}

	public void setLoanAccount(LoanAccount loanAccount) {
		this.loanAccount = loanAccount;
	}

	public SavingAccount getSavingAccount() {
		return savingAccount;
	}

	public void setSavingAccount(SavingAccount savingAccount) {
		this.savingAccount = savingAccount;
	}

	public CurrentAccount getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(CurrentAccount currentAccount) {
		this.currentAccount = currentAccount;
	}

	public Customer(String customerCode, @NotEmpty String name, String nrc, String phone, Date dob, String email,
			String gender, String address, String currentJob, String positon, String companyName, double monthlyIncome,
			boolean delStatus, int createdUser, Date createdDate, int updateUser, Date updateDate,
			LoanAccount loanAccount, SavingAccount savingAccount, CurrentAccount currentAccount) {
		super();
		this.customerCode = customerCode;
		this.name = name;
		this.nrc = nrc;
		this.phone = phone;
		this.dob = dob;
		this.email = email;
		this.gender = gender;
		this.address = address;
		this.currentJob = currentJob;
		this.positon = positon;
		this.companyName = companyName;
		this.monthlyIncome = monthlyIncome;
		this.delStatus = delStatus;
		this.createdUser = createdUser;
		this.createdDate = createdDate;
		this.updateUser = updateUser;
		this.updateDate = updateDate;
		this.loanAccount = loanAccount;
		this.savingAccount = savingAccount;
		this.currentAccount = currentAccount;
	}

	public Customer() {
		super();
	}
	
	
	
}

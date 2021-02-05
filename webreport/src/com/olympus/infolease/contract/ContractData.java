package com.olympus.infolease.contract;

public class ContractData {
	
	private String contractID;
	private String custName;
	private String leaseType;
	private String dateBooked;
	private String dateCommenced;
	private String term;
	private String termDate;
	private String leadDays;
	private String firstPayDate;
	private String lastPayDate;
	private String equipCost;
	private String equipCost14;
	private String outOfBalance;
	private String screen3Yield;
	private String screen10Yield;
	private String screen14Yield;
	private String screen3Residual;
	private String totalOLResidual;
	private String screen14Residual;
	private String poNumber;
	private String poExpireDate;
	private String payOption;
	private String invoiceCode;
	private String invoiceFormat;
	private String contractPurchOpt;
	private String totalCppProcs;
	private String lcGracePeriod;	
	private String programType;
	private String productGroup;
	private String cppFirstReadDate;
	private String ach;
	private String insurStatus;
	private String lateChargeExempt;
	private String lateChargePercent;
	// add new fields -- 2019-04-30
	
	private String useTax;
	private String amAddlEmail;
	private String alphaNum2;
	private String vaVisn;
	
	// add new fields -- 2019-05-16
	private String userDate1;
	private String umTable1;
	
	
	public String getUmTable1() {
		return umTable1;
	}
	public void setUmTable1(String umTable1) {
		this.umTable1 = umTable1;
	}
	public String getVaVisn() {
		return vaVisn;
	}
	public void setVaVisn(String vaVisn) {
		this.vaVisn = vaVisn;
	}
	public String getUseTax() {
		return useTax;
	}
	public void setUseTax(String useTax) {
		this.useTax = useTax;
	}
	public String getAmAddlEmail() {
		return amAddlEmail;
	}
	public void setAmAddlEmail(String amAddlEmail) {
		this.amAddlEmail = amAddlEmail;
	}
	public String getAlphaNum2() {
		return alphaNum2;
	}
	public void setAlphaNum2(String alphaNum2) {
		this.alphaNum2 = alphaNum2;
	}
	public String getUserDate1() {
		return userDate1;
	}
	public void setUserDate1(String userDate1) {
		this.userDate1 = userDate1;
	}
	/****************************************************************************************************************************************************/

	public String getContractID() {
		return contractID;
	}
	public void setContractID(String contractID) {
		this.contractID = contractID;
	}
	
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	
	public String getLeaseType() {
		return leaseType;
	}
	public void setLeaseType(String leaseType) {
		this.leaseType = leaseType;
	}
	public String getDateBooked() {
		return dateBooked;
	}
	public void setDateBooked(String dateBooked) {
		this.dateBooked = dateBooked;
	}
	public String getDateCommenced() {
		return dateCommenced;
	}
	public void setDateCommenced(String dateCommenced) {
		this.dateCommenced = dateCommenced;
	}
	public String getLeadDays() {
		return leadDays;
	}
	public void setLeadDays(String leadDays) {
		this.leadDays = leadDays;
	}
	public String getFirstPayDate() {
		return firstPayDate;
	}
	public void setFirstPayDate(String firstPayDate) {
		this.firstPayDate = firstPayDate;
	}
	public String getLastPayDate() {
		return lastPayDate;
	}
	public void setLastPayDate(String lastPayDate) {
		this.lastPayDate = lastPayDate;
	}
	public String getEquipCost() {
		return equipCost;
	}
	public void setEquipCost(String equipCost) {
		this.equipCost = equipCost;
	}
	public String getEquipCost14() {
		return equipCost14;
	}
	public void setEquipCost14(String equipCost14) {
		this.equipCost14 = equipCost14;
	}
	public String getOutOfBalance() {
		return outOfBalance;
	}
	public void setOutOfBalance(String outOfBalance) {
		this.outOfBalance = outOfBalance;
	}
	public String getScreen3Yield() {
		return screen3Yield;
	}
	public void setScreen3Yield(String screen3Yield) {
		this.screen3Yield = screen3Yield;
	}
	public String getScreen10Yield() {
		return screen10Yield;
	}
	public void setScreen10Yield(String screen10Yield) {
		this.screen10Yield = screen10Yield;
	}
	public String getScreen14Yield() {
		return screen14Yield;
	}
	public void setScreen14Yield(String screen14Yield) {
		this.screen14Yield = screen14Yield;
	}
	public String getScreen3Residual() {
		return screen3Residual;
	}
	public void setScreen3Residual(String screen3Residual) {
		this.screen3Residual = screen3Residual;
	}
	public String getScreen14Residual() {
		return screen14Residual;
	}
	public void setScreen14Residual(String screen14Residual) {
		this.screen14Residual = screen14Residual;
	}
	public String getTotalOLResidual() {
		return totalOLResidual;
	}
	public void setTotalOLResidual(String totalOLResidual) {
		this.totalOLResidual = totalOLResidual;
	}
	public String getProgramType() {
		return programType;
	}
	public void setProgramType(String programType) {
		this.programType = programType;
	}
	public String getProductGroup() {
		return productGroup;
	}
	public void setProductGroup(String productGroup) {
		this.productGroup = productGroup;
	}
	public String getPoExpireDate() {
		return poExpireDate;
	}
	public void setPoExpireDate(String poExpireDate) {
		this.poExpireDate = poExpireDate;
	}
	public String getPayOption() {
		return payOption;
	}
	public void setPayOption(String payOption) {
		this.payOption = payOption;
	}
	public String getInvoiceCode() {
		return invoiceCode;
	}
	public void setInvoiceCode(String invoiceCode) {
		this.invoiceCode = invoiceCode;
	}
	public String getInvoiceFormat() {
		return invoiceFormat;
	}
	public void setInvoiceFormat(String invoiceFormat) {
		this.invoiceFormat = invoiceFormat;
	}
	public String getLcGracePeriod() {
		return lcGracePeriod;
	}
	public void setLcGracePeriod(String lcGracePeriod) {
		this.lcGracePeriod = lcGracePeriod;
	}
	public String getAch() {
		return ach;
	}
	public void setAch(String ach) {
		this.ach = ach;
	}
	public String getInsurStatus() {
		return insurStatus;
	}
	public void setInsurStatus(String insurStatus) {
		this.insurStatus = insurStatus;
	}
	public String getLateChargeExempt() {
		return lateChargeExempt;
	}
	public void setLateChargeExempt(String lateChargeExempt) {
		this.lateChargeExempt = lateChargeExempt;
	}
	public String getLateChargePercent() {
		return lateChargePercent;
	}
	public void setLateChargePercent(String lateChargePercent) {
		this.lateChargePercent = lateChargePercent;
	}
	public String getTerm() {
		return term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public String getTermDate() {
		return termDate;
	}
	public void setTermDate(String termDate) {
		this.termDate = termDate;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
	public String getContractPurchOpt() {
		return contractPurchOpt;
	}
	public void setContractPurchOpt(String contractPurchOpt) {
		this.contractPurchOpt = contractPurchOpt;
	}
	public String getTotalCppProcs() {
		return totalCppProcs;
	}
	public void setTotalCppProcs(String totalCppProcs) {
		this.totalCppProcs = totalCppProcs;
	}
	public String getCppFirstReadDate() {
		return cppFirstReadDate;
	}
	public void setCppFirstReadDate(String cppFirstReadDate) {
		this.cppFirstReadDate = cppFirstReadDate;
	}
	
	
	
	
	/****************************************************************************************************************************************************/

	
	
}

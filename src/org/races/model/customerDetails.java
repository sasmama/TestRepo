package org.races.model;

import java.util.Date;

public class customerDetails {
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public String getCustomerDetails() {
		return customerDetails;
	}
	public void setCustomerDetails(String customerDetails) {
		this.customerDetails = customerDetails;
	}
	public String getChasisNumber() {
		return chasisNumber;
	}
	public void setChasisNumber(String chasisNumber) {
		this.chasisNumber = chasisNumber;
	}
	public String getEngineNumber() {
		return engineNumber;
	}
	public void setEngineNumber(String engineNumber) {
		this.engineNumber = engineNumber;
	}
 
	int customerId;
	String customerDetails;
	String chasisNumber;
	String engineNumber;
	public String getDateOFsale() {
		return dateOFsale;
	}
	public void setDateOFsale(String dateOFsale) {
		this.dateOFsale = dateOFsale;
	}
	public String getInstalledDate() {
		return installedDate;
	}
	public void setInstalledDate(String installedDate) {
		this.installedDate = installedDate;
	}

	String dateOFsale;
	String installedDate;
	
	

}

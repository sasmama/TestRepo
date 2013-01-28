package org.races.dao;

import java.util.List;

import org.races.model.PendingService;
import org.races.model.ReportFilter;
import org.races.model.ServiceActual;
import org.races.model.UserDetails;
import org.races.model.customerDetails;
import org.races.model.serviceDetails;

public interface RacesDao {
	
	public UserDetails getLoginDetails(String userName,String password);
	public UserDetails getEmailId(String userName);
	public void insertData(Object[] objects);
	public void insertSpareDetails(Object[] obj);
	public void insertServiceData(Object[] objects);
	public void insertCustomerData(List<customerDetails> listOfCustomers);
	public List<ServiceActual> getServiceList(String serviceCutoff);
	public List<String> getChasisNoList(String serviceCutoff);
	public Object getServiceDetailByName(String serviceName,final String chasisNumber);
	public String getCustomerDetails_byChasisNo(String chasisNumber);
	public Object getCustomerDetails(String ChasisNo);
	public List<serviceDetails> getServiceDetails(String ChasisNo);
	public List<serviceDetails> getServiceDetailsByName(String ChasisNo,String service);
	public Object getServiceDetail(String ChasisNo);
	public List<PendingService> getFirstServicePendinglist(int fromMonth,int toMonth,int fromYear,int toYear);
	public List<PendingService> getSecondServicePendinglist(int fromMonth,int toMonth,int fromYear,int toYear);
	public List<PendingService> getThirdServicePendinglist(int fromMonth,int toMonth,int fromYear,int toYear);
	public List<PendingService> getFourthServicePendinglist(int fromMonth,int toMonth,int fromYear,int toYear);
	public List<PendingService> getFifthServicePendinglist(int fromMonth,int toMonth,int fromYear,int toYear);
	public List<PendingService> getSixthServicePendinglist(int fromMonth,int toMonth,int fromYear,int toYear);
	public List<PendingService> getSeventhServicePendinglist(int fromMonth,int toMonth,int fromYear,int toYear);
	public List<PendingService> getEigthServicePendinglist(int fromMonth,int toMonth,int fromYear,int toYear);
	public Object[] getDataForChart();
	public List<PendingService> getPendingServicesBySelectedMonth(ReportFilter reportFilter);

}

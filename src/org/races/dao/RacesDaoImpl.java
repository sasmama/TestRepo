package org.races.dao;

import java.sql.ResultSet;    
import java.sql.SQLException;
import java.text.SimpleDateFormat; 
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set; 
import org.apache.log4j.Logger; 
import org.races.model.PendingService;
import org.races.model.ReportFilter;
import org.races.model.ServiceActual;
import org.races.model.UserDetails;
import org.races.model.customerDetails;
import org.races.model.serviceDetails;
import org.races.model.spares_details;
import org.races.util.ReadExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class RacesDaoImpl implements RacesDao {
	
	@Autowired 
	JdbcTemplate jdbctemp;
	@Autowired
	Dao_Constants dao_constants;
	@Autowired
	ReadExcel read_excel;
    SimpleDateFormat ft = 
 	       new SimpleDateFormat ("yyyy-MM-dd");
	  SimpleDateFormat simpleDateformatYear=new SimpleDateFormat("yyyy");
	  SimpleDateFormat simpleDateformatMonth=new SimpleDateFormat("MM");
    private static Logger log = Logger.getLogger(RacesDaoImpl.class);
    public UserDetails getLoginDetails(String userName,String password)
    {
    	final UserDetails userdetail = new UserDetails();
		try{
    	if(jdbctemp != null)
    	{
    		jdbctemp.queryForObject(dao_constants.GET_LOGIN_DETAILS,new Object[]{userName,password},new RowMapper<UserDetails>(){

				@Override
				public UserDetails mapRow(ResultSet rs, int count) throws SQLException {
					userdetail.setUserName(rs.getString("userName"));
					userdetail.setPassword(rs.getString("password"));
					userdetail.setEmail_id((rs.getString("email_id")));
					userdetail.setLoginType((rs.getString("LoginType")));
					userdetail.setEid(rs.getInt("eid"));
					// TODO Auto-generated method stub
					return userdetail;
				}
    			
    		});
    	}
    	else
    	{
    		return null;
    	}
		}
		catch(Exception ex)
		{
			System.out.println("NO USER FOUND !!!!");
			log.debug("NO USER FOUND : "+ex);
			//userdetail.setErrorMessage("No User Found");
			return userdetail;
		}
    	return userdetail;
    }
    
    public UserDetails getEmailId(String userName)
    {
    	final UserDetails userdetail = new UserDetails();
		try{
    	if(jdbctemp != null)
    	{
    		jdbctemp.queryForObject(dao_constants.GET_LOGIN_DETAILS,new Object[]{userName},new RowMapper<UserDetails>(){

				@Override
				public UserDetails mapRow(ResultSet rs, int count) throws SQLException {
					userdetail.setUserName(rs.getString("userName"));
					userdetail.setPassword(rs.getString("password"));
					userdetail.setEmail_id((rs.getString("email_id")));
					userdetail.setLoginType((rs.getString("LoginType")));
					userdetail.setEid(rs.getInt("eid"));
					// TODO Auto-generated method stub
					return userdetail;
				}
    			
    		});
    	}
    	else
    	{
    		return null;
    	}
		}
		catch(Exception ex)
		{
			System.out.println("NO USER FOUND !!!!");
			log.debug("NO USER FOUND : "+ex);
			return null;
		}
    	return userdetail;
    }
    
	public void insertData(Object[] objects)
	{ 
		List<customerDetails> custDetailsList = (List<customerDetails>) objects[0];
		List<serviceDetails> servDetailsList = (List<serviceDetails>) objects[1];
		if (jdbctemp != null)
		{
			System.out.println("Customer Size : "+custDetailsList.size());
			System.out.println("Details Size : "+servDetailsList.size());
			
			try
			{
			if(custDetailsList.size() > 0)
			{
 				for(customerDetails cd : custDetailsList)
				{
					try
					{
						System.out.println("inserting started ..... CUSTOMER DATA");
						jdbctemp.update(dao_constants.INSERT_DATA_CUSTOMER, new Object[]{cd.getCustomerId(),cd.getCustomerDetails(),cd.getChasisNumber(),cd.getEngineNumber(),cd.getDateOFsale(),cd.getInstalledDate()});
						jdbctemp.update(dao_constants.INSERT_SERVICE_ACTUAL,new Object[]{cd.getChasisNumber(),cd.getDateOFsale(),cd.getDateOFsale(),cd.getDateOFsale(),cd.getDateOFsale(),cd.getDateOFsale(),cd.getDateOFsale(),cd.getDateOFsale(),cd.getDateOFsale(),cd.getDateOFsale()});
						System.out.println("insert completed ....");
					}
					catch(org.springframework.dao.DuplicateKeyException ex)
					{
						System.out.println("Duplicate data Exception @ customer details insert data - :"+cd.getChasisNumber());
						jdbctemp.update(dao_constants.UPDATE_INSTALLATION_DATE, new Object[]{cd.getInstalledDate(),cd.getChasisNumber()});
						System.out.println("updating the installation date for : "+cd.getChasisNumber());
						log.debug("Duplicate data Exception @ customer details insert data -"+ex);
					}
					
				}
			}
			}
			catch(Exception ex)
			{
				log.debug("Exception @ customer details insert data - :"+ex);
				System.out.println("Exception @ customer details insert data - :"+ex);
			}
			
			try
			{
				System.out.println("inserting started ..... SERVICE DATA");
				
				if(servDetailsList.size() > 0)
				{
 					for(serviceDetails sd : servDetailsList)
					{
						jdbctemp.update(dao_constants.INSERT_DATA_SERVICE,new Object[]{sd.getChasisNumber(),sd.getServiceName(),sd.getServiceDate(),sd.getJobcardNo(),sd.getHours(),sd.getCostCharged()});
					}
				}
			
			}
			catch(Exception ex)
			{
				log.debug("Exception @ service details insert data - :"+ex);
				
				System.out.println("Exception @ service details insert data - :"+ex);
			}
 
		}
		else
		{
			System.out.println("JDBC IS NULL");
		}
	}
	
	public void insertSpareDetails(Object[] obj)
	{
		List<spares_details> sparesDetails = (List<spares_details>) obj[0];
		if (jdbctemp != null)
		{
		if(sparesDetails.size() > 0)
		{
			System.out.println("INSERT SPARES DETAILS STARTED..... SIZE :"+sparesDetails.size());
			for(spares_details sd : sparesDetails)
			{
				if(sd != null)
				{
				try
				{
					jdbctemp.update(dao_constants.INSERT_SPARES_DETAILS,new Object[]{sd.getSpare_code(),sd.getPart_description(),"NO",sd.getCost(),sd.getMin_order()});				 
				}
				catch(org.springframework.dao.DuplicateKeyException ex)
				{
					log.debug("Duplicate Exception at insertSparesDetails : "+ex.getMessage()+" for :"+sd.getSpare_code());
					System.out.println("Duplicate Exception at insertSparesDetails : "+ex.getMessage()+" for :"+sd.getSpare_code());
				}
				catch(Exception ex)
				{
					log.debug("Exception insert spare parts : "+ex);
					System.out.println("Exception insert spare parts : "+ex);
				}
				}
			}
			System.out.println("INSERT SPARES DETAILS COMPLETED.....SUCCESS");

		}	
		}
		else
		{
			System.out.println("jdbctemp is NULL");
		}
	}
	
	
	public void insertServiceData(Object[] objects)
	{
		List<customerDetails> customerList =(List<customerDetails>) objects[0];
		List<serviceDetails> serviceList = (List<serviceDetails>) objects[1];
		System.out.println("customer size : "+customerList.size());
		System.out.println("Service list size : "+serviceList.size());
		
		
		try
		{
		if(customerList.size() > 0)
		{
			for(customerDetails cd : customerList)
			{
 				jdbctemp.update(dao_constants.UPDATE_INSTALLATION_DATE, new Object[]{cd.getInstalledDate(),cd.getChasisNumber()});
			}
			log.info("CUSTOMER INSTALLATION DETAILS UPDATION COMPLETE ...... ");
			System.out.println("CUSTOMER INSTALLATION DETAILS UPDATION COMPLETE ...... ");
		}
		}
		catch(Exception ex)
		{
			log.debug("Exception @ customer updating customer installation data - :"+ex);
			System.out.println("Exception @ customer updating customer installation data - :"+ex);
		}
		
		try
		{
			if(serviceList.size() > 0)
			{
 				for(serviceDetails sd : serviceList)
				{
					jdbctemp.update(dao_constants.INSERT_DATA_SERVICE,new Object[]{sd.getChasisNumber(),sd.getServiceName(),sd.getServiceDate(),sd.getJobcardNo(),sd.getHours(),sd.getCostCharged()});
				}
 				log.info("SERVICE DETAILS INSERT COMPLETED.....");
				System.out.println("SERVICE DETAILS INSERT COMPLETED.....");
			}
		
		}
		catch(Exception ex)
		{
			log.debug("Exception @ service details insert data - :"+ex);
			
			System.out.println("Exception @ service details insert data - :"+ex);
		}
		
		
	}
	
	
	
	public void insertCustomerData(List<customerDetails> listOfCustomers)
	{ 
 
		if (jdbctemp != null)
		{
			System.out.println("Customer Size From Insert Method : "+listOfCustomers.size());
 
			
			try
			{
			if(listOfCustomers.size() > 0)
			{
				//jdbctemp.execute(dao_constants.DELETE_DATA_CUSTOMER);
				System.out.println("CUSTOMER TABLE IS CLEARED SUCCESSFULLY");
				for(customerDetails cd : listOfCustomers)
				{
					try{
					jdbctemp.update(dao_constants.INSERT_DATA_CUSTOMER, new Object[]{cd.getCustomerId(),cd.getCustomerDetails(),cd.getChasisNumber(),cd.getEngineNumber(),cd.getDateOFsale(),cd.getInstalledDate()});
					jdbctemp.update(dao_constants.INSERT_SERVICE_ACTUAL,new Object[]{cd.getChasisNumber(),cd.getDateOFsale(),cd.getDateOFsale(),cd.getDateOFsale(),cd.getDateOFsale(),cd.getDateOFsale(),cd.getDateOFsale(),cd.getDateOFsale(),cd.getDateOFsale(),cd.getDateOFsale()});
					}
					catch(org.springframework.dao.DuplicateKeyException ex)
					{
						log.debug("Duplicate data Exception @ customer details insert data - :"+cd.getChasisNumber());
						System.out.println("Duplicate data Exception @ customer details insert data - :"+cd.getChasisNumber());
					}
					
				}
				log.info("CUSTOMER TABLE IS UPDATED SUCCESSFULLY");
				System.out.println("CUSTOMER TABLE IS UPDATED SUCCESSFULLY");

			}
			}
			
 
			catch(Exception ex)
			{
				log.debug("Exception @ customer details insert data - :"+ex);
				System.out.println("Exception @ customer details insert data - :"+ex);
			}
			
			
 
		}
		else
		{
			log.debug("JDBC IS NULL");
			System.out.println("JDBC IS NULL");
		}
	}
	
	
	
	
	
	public List<ServiceActual> getServiceList(String serviceCutoff)
	{
		return (List<ServiceActual>) jdbctemp.query(dao_constants.SELECT_ACTUAL_SERVICE_BY_MONTH,new Object[]{serviceCutoff,serviceCutoff,serviceCutoff,serviceCutoff,serviceCutoff,serviceCutoff,serviceCutoff,serviceCutoff,serviceCutoff,serviceCutoff,serviceCutoff,serviceCutoff,serviceCutoff,serviceCutoff,serviceCutoff,serviceCutoff}, new RowMapper<ServiceActual>(){

			@Override
			public ServiceActual mapRow(ResultSet rs, int arg1)
					throws SQLException {
				ServiceActual sa = new ServiceActual();
				sa.setChasisnumber(rs.getString(2));
				sa.setFirstServiceDate(rs.getDate(3));
				sa.setFirstServiceHours(rs.getInt(4));
				sa.setSecondServiceDate(rs.getDate(5));
				sa.setSecondServiceHours(rs.getInt(6));
				sa.setThirdServiceDate(rs.getDate(7));
				sa.setThirdServiceHours(rs.getInt(8));
				sa.setFourthServiceDate(rs.getDate(9));
				sa.setFourthServiceHours(rs.getInt(10));
				sa.setFifthServiceDate(rs.getDate(11));
				sa.setFifthServiceHours(rs.getInt(12));
				sa.setSixthServiceDate(rs.getDate(13));
				sa.setSixthServiceHours(rs.getInt(14));
				sa.setSeventhServiceDate(rs.getDate(15));
				sa.setSeventhServiceHours(rs.getInt(16));
				sa.setEigthServiceDate(rs.getDate(17));
				sa.setEigthServiceHours(rs.getInt(18)); 
				return sa;
			} 
		});
	}
	public List<String> getChasisNoList(String serviceCutoff)
	{
		List<String> chasisList = null;
		try{
		chasisList = jdbctemp.queryForList(dao_constants.SELECT_FIRST_SERVICE_BYDATE,new Object[]{serviceCutoff,serviceCutoff},String.class);
		}
		catch(Exception e)
		{
			log.debug("Exception at getChasisNoList : "+e);
			System.out.println("Exception at getChasisNoList : "+e);
		}
		return chasisList;
	} 
	  
	public Object getServiceDetailByName(String serviceName,final String chasisNumber)
	{
		Object chasisList = null; 
		try{
		chasisList = jdbctemp.queryForObject(dao_constants.SELECT_SERVICEDETAILS_BYNAME,new Object[]{chasisNumber,serviceName},new RowMapper<serviceDetails>(){

			@Override
			public serviceDetails mapRow(ResultSet rs, int count)
					throws SQLException {
				serviceDetails sd = new serviceDetails();
				sd.setServiceName(rs.getString(1));
				sd.setJobcardNo(rs.getString(3));
				sd.setServiceDate(ft.format(rs.getDate(2)));
				sd.setHours(rs.getInt(4));
				sd.setChasisNumber(chasisNumber);
				return sd;
			}
			
		});
		}
		catch(org.springframework.dao.EmptyResultDataAccessException aa)
		{
			log.debug("Exception at getServiceDetailByName : "+aa);
			//System.out.println("DATA IS NULL !!!! FOR CURRENT DATA");
		}
		catch(Exception e)
		{
			log.debug("Exception at getServiceDetailByName : "+e);
			System.out.println("Exception at getServiceDetailByName : "+e);
		}
		return chasisList;
	}
	public String getCustomerDetails_byChasisNo(String chasisNumber)
	{
		String customerDetails="";
		try
		{
			customerDetails = (String)jdbctemp.queryForObject(dao_constants.SELECT_CUSTOMERDETAILS_BY_CHASISNO, new Object[]{chasisNumber}, String.class);
		}
		catch(Exception e)
		{
			log.debug("Exception at getCustomerDetails_byChasisNo : "+e);

			System.out.println("Exception at getCustomerDetails_byChasisNo : "+e);
		}
		return customerDetails;
	}
	
	
	public Object getCustomerDetails(String ChasisNo)
	{
		return (Object) jdbctemp.queryForObject(dao_constants.SELECT_CUSTOMER_BY_CHASISNO,new Object[]{ChasisNo}, new RowMapper<Object>(){

			@Override
			public customerDetails mapRow(ResultSet rs, int arg1)
					throws SQLException {
				customerDetails sa = new customerDetails();
				sa.setChasisNumber(rs.getString(3));
				sa.setCustomerDetails(rs.getString(2));
				sa.setCustomerId(rs.getInt(1));
				sa.setDateOFsale(ft.format(rs.getDate(5)));
				sa.setEngineNumber(rs.getString(4));
				sa.setInstalledDate(ft.format(rs.getDate(6)));
 				return sa;
			} 
		});
	}
	public List<serviceDetails> getServiceDetails(String ChasisNo)
	{
		return (List<serviceDetails>) jdbctemp.query(dao_constants.SELECT_SERVICE_BY_CHASISNO,new Object[]{ChasisNo}, new RowMapper<serviceDetails>(){

			@Override
			public serviceDetails mapRow(ResultSet rs, int arg1)
					throws SQLException {
				serviceDetails sa = new serviceDetails();
				sa.setChasisNumber(rs.getString(2));
				sa.setServiceName(rs.getString(3));
				sa.setServiceDate(ft.format(rs.getDate(4)));
				sa.setJobcardNo(rs.getString(5));
				sa.setHours(rs.getInt(6));
				sa.setCostCharged(new Integer(rs.getInt(7)).toString());
				return sa;
			} 
		});
	}	

	public List<serviceDetails> getServiceDetailsByName(String ChasisNo,String service)
	{
		return (List<serviceDetails>) jdbctemp.query(dao_constants.SELECT_SERVICE_BY_CHASISNO,new Object[]{ChasisNo}, new RowMapper<serviceDetails>(){

			@Override
			public serviceDetails mapRow(ResultSet rs, int arg1)
					throws SQLException {
				serviceDetails sa = new serviceDetails();
				sa.setChasisNumber(rs.getString(2));
				sa.setServiceName(rs.getString(3));
				sa.setServiceDate(ft.format(rs.getDate(4)));
				sa.setJobcardNo(rs.getString(5));
				sa.setHours(rs.getInt(6));
				sa.setCostCharged(new Integer(rs.getInt(7)).toString());
				return sa;
			} 
		});
	}
	
	public Object getServiceDetail(String ChasisNo)
	{
		return (Object) jdbctemp.query(dao_constants.SELECT_SERVICE_BY_CHASISNO,new Object[]{ChasisNo}, new RowMapper<serviceDetails>(){

			@Override
			public serviceDetails mapRow(ResultSet rs, int arg1)
					throws SQLException {
				serviceDetails sa = new serviceDetails();
				sa.setChasisNumber(rs.getString(2));
				sa.setServiceName(rs.getString(3));
				sa.setServiceDate(ft.format(rs.getDate(4)));
				sa.setJobcardNo(rs.getString(5));
				sa.setHours(rs.getInt(6));
				sa.setCostCharged(new Integer(rs.getInt(7)).toString());
				return sa;
			} 
		});
	}
	

	public List<PendingService> getFirstServicePendinglist(int fromMonth,int toMonth,int fromYear,int toYear)
	{
		return (List<PendingService>)jdbctemp.query(dao_constants.SELECT_SERVICE_PENDING_BYMONTH_FIRST,new Object[]{fromMonth,toMonth,fromYear,toYear,fromMonth,toMonth,fromYear,toYear}, new RowMapper<PendingService>(){

			@Override
			public PendingService mapRow(ResultSet rs, int recCount) throws SQLException {
				// TODO Auto-generated method stub
				PendingService ps = new PendingService();
				ps.setChasisNumber(rs.getString(1));
				ps.setActual_date_of_service(rs.getDate(2));
				ps.setCustomerDetails(rs.getString(3));
				ps.setServiceName("1st Service");
				return ps;
			} 
		});
	}
	public List<PendingService> getSecondServicePendinglist(int fromMonth,int toMonth,int fromYear,int toYear)
	{
		return (List<PendingService>)jdbctemp.query(dao_constants.SELECT_SERVICE_PENDING_BYMONTH_SECOND,new Object[]{fromMonth,toMonth,fromYear,toYear,fromMonth,toMonth,fromYear,toYear}, new RowMapper<PendingService>(){

			@Override
			public PendingService mapRow(ResultSet rs, int recCount) throws SQLException {
				// TODO Auto-generated method stub
				PendingService ps = new PendingService();
				ps.setChasisNumber(rs.getString(1));
				ps.setActual_date_of_service(rs.getDate(2));
				ps.setCustomerDetails(rs.getString(3));
				ps.setServiceName("2nd Service");
				return ps;
			} 
		});
	}
	public List<PendingService> getThirdServicePendinglist(int fromMonth,int toMonth,int fromYear,int toYear)
	{
		return (List<PendingService>)jdbctemp.query(dao_constants.SELECT_SERVICE_PENDING_BYMONTH_THIRD,new Object[]{fromMonth,toMonth,fromYear,toYear,fromMonth,toMonth,fromYear,toYear}, new RowMapper<PendingService>(){

			@Override
			public PendingService mapRow(ResultSet rs, int recCount) throws SQLException {
				// TODO Auto-generated method stub
				PendingService ps = new PendingService();
				ps.setChasisNumber(rs.getString(1));
				ps.setActual_date_of_service(rs.getDate(2));
				ps.setCustomerDetails(rs.getString(3));
				ps.setServiceName("3rd Service");
				return ps;
			} 
		});
	}
	public List<PendingService> getFourthServicePendinglist(int fromMonth,int toMonth,int fromYear,int toYear)
	{
		return (List<PendingService>)jdbctemp.query(dao_constants.SELECT_SERVICE_PENDING_BYMONTH_FOURTH,new Object[]{fromMonth,toMonth,fromYear,toYear,fromMonth,toMonth,fromYear,toYear}, new RowMapper<PendingService>(){

			@Override
			public PendingService mapRow(ResultSet rs, int recCount) throws SQLException {
				// TODO Auto-generated method stub
				PendingService ps = new PendingService();
				ps.setChasisNumber(rs.getString(1));
				ps.setActual_date_of_service(rs.getDate(2));
				ps.setCustomerDetails(rs.getString(3));
				ps.setServiceName("4th Service");
				return ps;
			} 
		});
	}
	public List<PendingService> getFifthServicePendinglist(int fromMonth,int toMonth,int fromYear,int toYear)
	{
		return (List<PendingService>)jdbctemp.query(dao_constants.SELECT_SERVICE_PENDING_BYMONTH_FIFTH,new Object[]{fromMonth,toMonth,fromYear,toYear,fromMonth,toMonth,fromYear,toYear}, new RowMapper<PendingService>(){

			@Override
			public PendingService mapRow(ResultSet rs, int recCount) throws SQLException {
				// TODO Auto-generated method stub
				PendingService ps = new PendingService();
				ps.setChasisNumber(rs.getString(1));
				ps.setActual_date_of_service(rs.getDate(2));
				ps.setCustomerDetails(rs.getString(3));
				ps.setServiceName("1st Service");
				return ps;
			} 
		});
	}
	public List<PendingService> getSixthServicePendinglist(int fromMonth,int toMonth,int fromYear,int toYear)
	{
		return (List<PendingService>)jdbctemp.query(dao_constants.SELECT_SERVICE_PENDING_BYMONTH_SIXTH,new Object[]{fromMonth,toMonth,fromYear,toYear,fromMonth,toMonth,fromYear,toYear}, new RowMapper<PendingService>(){

			@Override
			public PendingService mapRow(ResultSet rs, int recCount) throws SQLException {
				// TODO Auto-generated method stub
				PendingService ps = new PendingService();
				ps.setChasisNumber(rs.getString(1));
				ps.setActual_date_of_service(rs.getDate(2));
				ps.setCustomerDetails(rs.getString(3));
				ps.setServiceName("2nd Service");
				return ps;
			} 
		});
	}
	public List<PendingService> getSeventhServicePendinglist(int fromMonth,int toMonth,int fromYear,int toYear)
	{
		return (List<PendingService>)jdbctemp.query(dao_constants.SELECT_SERVICE_PENDING_BYMONTH_SEVENTH,new Object[]{fromMonth,toMonth,fromYear,toYear,fromMonth,toMonth,fromYear,toYear}, new RowMapper<PendingService>(){

			@Override
			public PendingService mapRow(ResultSet rs, int recCount) throws SQLException {
				// TODO Auto-generated method stub
				PendingService ps = new PendingService();
				ps.setChasisNumber(rs.getString(1));
				ps.setActual_date_of_service(rs.getDate(2));
				ps.setCustomerDetails(rs.getString(3));
				ps.setServiceName("3rd Service");
				return ps;
			} 
		});
	}
	public List<PendingService> getEigthServicePendinglist(int fromMonth,int toMonth,int fromYear,int toYear)
	{
		return (List<PendingService>)jdbctemp.query(dao_constants.SELECT_SERVICE_PENDING_BYMONTH_EIGTH,new Object[]{fromMonth,toMonth,fromYear,toYear,fromMonth,toMonth,fromYear,toYear}, new RowMapper<PendingService>(){

			@Override
			public PendingService mapRow(ResultSet rs, int recCount) throws SQLException {
				// TODO Auto-generated method stub
				PendingService ps = new PendingService();
				ps.setChasisNumber(rs.getString(1));
				ps.setActual_date_of_service(rs.getDate(2));
				ps.setCustomerDetails(rs.getString(3));
				ps.setServiceName("4th Service");
				return ps;
			} 
		});
	}
	
	/* METHOD TO GET THE DATA FOR CHART ... */
	public Object[] getDataForChart()
	{
 		List<Map<String, Object>> firstService= jdbctemp.queryForList(dao_constants.GET_DURATION_COUNT_1ST_SERVICE);
		List<Map<String, Object>> secondService = jdbctemp.queryForList(dao_constants.GET_DURATION_COUNT_2ND_SERVICE);
		List<Map<String, Object>> thirdService = jdbctemp.queryForList(dao_constants.GET_DURATION_COUNT_3RD_SERVICE);
		List<Map<String, Object>> fourthService = jdbctemp.queryForList(dao_constants.GET_DURATION_COUNT_4TH_SERVICE);
		List<Map<String, Object>> fifthService = jdbctemp.queryForList(dao_constants.GET_DURATION_COUNT_5TH_SERVICE);
		List<Map<String, Object>> sixthService = jdbctemp.queryForList(dao_constants.GET_DURATION_COUNT_6TH_SERVICE);
		List<Map<String, Object>> seventhService = jdbctemp.queryForList(dao_constants.GET_DURATION_COUNT_7TH_SERVICE);
		List<Map<String, Object>> eigthService = jdbctemp.queryForList(dao_constants.GET_DURATION_COUNT_8TH_SERVICE);
		
		List<Map<String,Object>> completedService = jdbctemp.queryForList(dao_constants.GET_DURATION_COUNT_COMPLETED_SERVICE);
		System.out.println("%%%% : "+completedService.toString());
		LinkedHashMap<String, Object> combinedCompletedServices = getCombined(completedService);
		
		List list = new ArrayList();
		list.add(firstService);
		list.add(secondService);
		list.add(thirdService);
		list.add(fourthService);
		list.add(fifthService);
		list.add(sixthService);
		list.add(seventhService);
		list.add(eigthService);
		
 		List service1 = (List) list.get(0);
		List service2 = (List) list.get(1);
		List service3 = (List) list.get(2);
		List service4 = (List) list.get(3);
		List service5 = (List) list.get(4);
		List service6 = (List) list.get(5);
		List service7 = (List) list.get(6);
		List service8 = (List) list.get(7);
		
		
		LinkedHashMap combinedservice1 = getCombined(service1);
		LinkedHashMap combinedservice2 = getCombined(service2);
		LinkedHashMap combinedservice3 = getCombined(service3);
		LinkedHashMap combinedservice4 = getCombined(service4);
		LinkedHashMap combinedservice5 = getCombined(service5);
		LinkedHashMap combinedservice6 = getCombined(service6);
		LinkedHashMap combinedservice7 = getCombined(service7);
		LinkedHashMap combinedservice8 = getCombined(service8);
 		
		List listOfServices = new ArrayList();
		
		listOfServices.add(combinedservice1);
		listOfServices.add(combinedservice2);
		listOfServices.add(combinedservice3);
		listOfServices.add(combinedservice4);
		listOfServices.add(combinedservice5);
		listOfServices.add(combinedservice6);
		listOfServices.add(combinedservice7);
		listOfServices.add(combinedservice8); 
		
		//System.out.println("List : "+test.size());
		//Map mappie = test;
		
		LinkedHashMap<String,Integer> combinedPendingServices =  getAddedMap(listOfServices);  
		System.out.println("Pending Services : getDataForChart ::"+(combinedPendingServices).toString());
		System.out.println("Completed Sevices : getDataForChart :"+combinedCompletedServices.toString());
		Object[] objects = new Object[]{combinedPendingServices,combinedCompletedServices};
		return objects;
		
    }
	

	
	private  LinkedHashMap<String,Object> getCombined(List getList)
	{
		LinkedHashMap<String,Object> combineMap = new LinkedHashMap<String,Object>();
 		
		try
		{
		for(int i = 0;i<getList.size();i++)
		{
			//System.out.println("getList.get(i).toString() : "+getList.get(i).toString());
			if(getList.get(i).toString().length() >3)
			combineMap.put(getList.get(i).toString().substring(18,23).replace('}',' ').trim(),getList.get(i).toString().substring(37,39).replace('}',' ').trim());
			//System.out.println("Key :"+getList.get(i).toString().substring(18,23).replace('}',' ').trim()+" Value : "+getList.get(i).toString().substring(37,39).replace('}',' ').trim());
			//System.out.println("i : "+i);
		} 
		}
		catch(Exception e)
		{
			System.out.println("Exception : st getCombine "+e);
		}
		System.out.println("** : "+combineMap.toString());
		return combineMap;
	}
	
	private LinkedHashMap<String,Integer> getAddedMap(List list)
	{
 		 //To get the maximum size of the map
        int count = 0;
        int listposition = 0;
        try{
 
        for(int i=0;i<list.size();i++)
        {
            System.out.println("Size : Of "+i+" is : "+((LinkedHashMap)list.get(i)).size());
          //  if((i+1) < list.size())
            {
            if(count < ((LinkedHashMap)list.get(i)).size())
            {
                count = ((LinkedHashMap)list.get(i)).size(); 
                listposition = i;
            }
            }
        }
        log.debug("Max Size : "+count);
        log.debug("Max Size  position : "+listposition);
        
        log.debug("Max list size is : "+((LinkedHashMap)list.get(listposition)).toString());
        }
        catch(Exception e)
        {
        	log.debug("exception in getting the Max size : "+e.getMessage());
        }
        
         try{
        Set keyset = ((LinkedHashMap)list.get(listposition)).keySet();
         String[] keysetArray = new String[((LinkedHashMap)list.get(listposition)).size()];
         Iterator ite = keyset.iterator();
         int x =0;
         while(ite.hasNext())
        {
            
            keysetArray[x] = ite.next().toString();
            x++;
         }  
         for(int i=0;i<list.size();i++)
        {  
             if(i != listposition)
            {
                 for(int j=0;j<keysetArray.length;j++)
                {
                      if(((LinkedHashMap)list.get(i)).containsKey(keysetArray[j]))
                    {
                    	 if(((LinkedHashMap)list.get(listposition)).get(keysetArray[j]).toString().length() > 0 && ((LinkedHashMap)list.get(i)).get(keysetArray[j]).toString().length() > 0 )
                    	 {  
                    		 int tempcount = Integer.parseInt((String) ((LinkedHashMap)list.get(listposition)).get(keysetArray[j]).toString().trim());
                    		 int tempcount1 = Integer.parseInt((String) ((LinkedHashMap)list.get(i)).get(keysetArray[j]).toString().trim());
                     		 ((LinkedHashMap)list.get(listposition)).put(keysetArray[j],tempcount+tempcount1);
                    	 }
                   }
                }
            }
            
        }
        }
        catch(Exception ex)
        {
           log.debug("Exception in adding data "+ex.getMessage());
        }
        System.out.println("TOTAL CONTENT : "+((LinkedHashMap)list.get(listposition)).toString());
        
        return ((LinkedHashMap)list.get(listposition));
	}
	
 
	

	public List<PendingService> getPendingFirstServiceForSeletedMonth(ReportFilter reportFilter)
	{
		
		int _Month = Integer.parseInt(simpleDateformatMonth.format(java.sql.Date.valueOf((reportFilter.getForCurrentMonth().toString()))));
		int _Year = Integer.parseInt(simpleDateformatYear.format(java.sql.Date.valueOf((reportFilter.getForCurrentMonth().toString()))));
		
		log.debug("_Month : "+_Month+" Year :"+_Year);
		return (List<PendingService>)jdbctemp.query(dao_constants.SELECT_SERVICE_PENDING_BYSELECTED_MONTH_FIRST,new Object[]{_Month,_Year,_Month,_Year}, new RowMapper<PendingService>(){

			@Override
			public PendingService mapRow(ResultSet rs, int recCount) throws SQLException {
				// TODO Auto-generated method stub
				PendingService ps = new PendingService();
				ps.setChasisNumber(rs.getString(1));
				ps.setActual_date_of_service(rs.getDate(2));
				ps.setCustomerDetails(rs.getString(3));
				ps.setServiceName(rs.getString(4));
				return ps;
			} 
		});
	}
	public List<PendingService> getPendingSecondServiceForSeletedMonth(ReportFilter reportFilter)
	{
		int _Month = Integer.parseInt(simpleDateformatMonth.format(java.sql.Date.valueOf((reportFilter.getForCurrentMonth().toString()))));
		int _Year = Integer.parseInt(simpleDateformatYear.format(java.sql.Date.valueOf((reportFilter.getForCurrentMonth().toString()))));
	
		return (List<PendingService>)jdbctemp.query(dao_constants.SELECT_SERVICE_PENDING_BYSELECTED_MONTH_SECOND,new Object[]{_Month,_Year,_Month,_Year}, new RowMapper<PendingService>(){

			@Override
			public PendingService mapRow(ResultSet rs, int recCount) throws SQLException {
				// TODO Auto-generated method stub
				PendingService ps = new PendingService();
				ps.setChasisNumber(rs.getString(1));
				ps.setActual_date_of_service(rs.getDate(2));
				ps.setCustomerDetails(rs.getString(3));
				ps.setServiceName(rs.getString(4));
				return ps;
			} 
		});
	}
	public List<PendingService> getPendingThirdServiceForSeletedMonth(ReportFilter reportFilter)
	{
		int _Month = Integer.parseInt(simpleDateformatMonth.format(java.sql.Date.valueOf((reportFilter.getForCurrentMonth().toString()))));
		int _Year = Integer.parseInt(simpleDateformatYear.format(java.sql.Date.valueOf((reportFilter.getForCurrentMonth().toString()))));
		
		return (List<PendingService>)jdbctemp.query(dao_constants.SELECT_SERVICE_PENDING_BYSELECTED_MONTH_THIRD,new Object[]{_Month,_Year,_Month,_Year}, new RowMapper<PendingService>(){

			@Override
			public PendingService mapRow(ResultSet rs, int recCount) throws SQLException {
				// TODO Auto-generated method stub
				PendingService ps = new PendingService();
				ps.setChasisNumber(rs.getString(1));
				ps.setActual_date_of_service(rs.getDate(2));
				ps.setCustomerDetails(rs.getString(3));
				ps.setServiceName(rs.getString(4));
				return ps;
			} 
		});
	}
	public List<PendingService> getPendingFourthServiceForSeletedMonth(ReportFilter reportFilter)
	{
		int _Month = Integer.parseInt(simpleDateformatMonth.format(java.sql.Date.valueOf((reportFilter.getForCurrentMonth().toString()))));
		int _Year = Integer.parseInt(simpleDateformatYear.format(java.sql.Date.valueOf((reportFilter.getForCurrentMonth().toString()))));
	
		return (List<PendingService>)jdbctemp.query(dao_constants.SELECT_SERVICE_PENDING_BYSELECTED_MONTH_FOURTH,new Object[]{_Month,_Year,_Month,_Year}, new RowMapper<PendingService>(){

			@Override
			public PendingService mapRow(ResultSet rs, int recCount) throws SQLException {
				// TODO Auto-generated method stub
				PendingService ps = new PendingService();
				ps.setChasisNumber(rs.getString(1));
				ps.setActual_date_of_service(rs.getDate(2));
				ps.setCustomerDetails(rs.getString(3));
				ps.setServiceName(rs.getString(4));
				return ps;
			} 
		});
	}
	public List<PendingService> getPendingFifthServiceForSeletedMonth(ReportFilter reportFilter)
	{
		int _Month = Integer.parseInt(simpleDateformatMonth.format(java.sql.Date.valueOf((reportFilter.getForCurrentMonth().toString()))));
		int _Year = Integer.parseInt(simpleDateformatYear.format(java.sql.Date.valueOf((reportFilter.getForCurrentMonth().toString()))));
	
		return (List<PendingService>)jdbctemp.query(dao_constants.SELECT_SERVICE_PENDING_BYSELECTED_MONTH_FIFTH,new Object[]{_Month,_Year,_Month,_Year}, new RowMapper<PendingService>(){

			@Override
			public PendingService mapRow(ResultSet rs, int recCount) throws SQLException {
				// TODO Auto-generated method stub
				PendingService ps = new PendingService();
				ps.setChasisNumber(rs.getString(1));
				ps.setActual_date_of_service(rs.getDate(2));
				ps.setCustomerDetails(rs.getString(3));
				ps.setServiceName(rs.getString(4));
				return ps;
			} 
		});
	}
	public List<PendingService> getPendingSixthServiceForSeletedMonth(ReportFilter reportFilter)
	{
		int _Month = Integer.parseInt(simpleDateformatMonth.format(java.sql.Date.valueOf((reportFilter.getForCurrentMonth().toString()))));
		int _Year = Integer.parseInt(simpleDateformatYear.format(java.sql.Date.valueOf((reportFilter.getForCurrentMonth().toString()))));
	
		return (List<PendingService>)jdbctemp.query(dao_constants.SELECT_SERVICE_PENDING_BYSELECTED_MONTH_SIXTH,new Object[]{_Month,_Year,_Month,_Year}, new RowMapper<PendingService>(){

			@Override
			public PendingService mapRow(ResultSet rs, int recCount) throws SQLException {
				// TODO Auto-generated method stub
				PendingService ps = new PendingService();
				ps.setChasisNumber(rs.getString(1));
				ps.setActual_date_of_service(rs.getDate(2));
				ps.setCustomerDetails(rs.getString(3));
				ps.setServiceName(rs.getString(4));
				return ps;
			} 
		});
	}
	public List<PendingService> getPendingSeventhServiceForSeletedMonth(ReportFilter reportFilter)
	{
		int _Month = Integer.parseInt(simpleDateformatMonth.format(java.sql.Date.valueOf((reportFilter.getForCurrentMonth().toString()))));
		int _Year = Integer.parseInt(simpleDateformatYear.format(java.sql.Date.valueOf((reportFilter.getForCurrentMonth().toString()))));
	
		return (List<PendingService>)jdbctemp.query(dao_constants.SELECT_SERVICE_PENDING_BYSELECTED_MONTH_SEVENTH,new Object[]{_Month,_Year,_Month,_Year}, new RowMapper<PendingService>(){

			@Override
			public PendingService mapRow(ResultSet rs, int recCount) throws SQLException {
				// TODO Auto-generated method stub
				PendingService ps = new PendingService();
				ps.setChasisNumber(rs.getString(1));
				ps.setActual_date_of_service(rs.getDate(2));
				ps.setCustomerDetails(rs.getString(3));
				ps.setServiceName(rs.getString(4));
				return ps;
			} 
		});
	}
	public List<PendingService> getPendingEigthServiceForSeletedMonth(ReportFilter reportFilter)
	{
		int _Month = Integer.parseInt(simpleDateformatMonth.format(java.sql.Date.valueOf((reportFilter.getForCurrentMonth().toString()))));
		int _Year = Integer.parseInt(simpleDateformatYear.format(java.sql.Date.valueOf((reportFilter.getForCurrentMonth().toString()))));
	
		return (List<PendingService>)jdbctemp.query(dao_constants.SELECT_SERVICE_PENDING_BYSELECTED_MONTH_EIGTH,new Object[]{_Month,_Year,_Month,_Year}, new RowMapper<PendingService>(){

			@Override
			public PendingService mapRow(ResultSet rs, int recCount) throws SQLException {
				// TODO Auto-generated method stub
				PendingService ps = new PendingService();
				ps.setChasisNumber(rs.getString(1));
				ps.setActual_date_of_service(rs.getDate(2));
				ps.setCustomerDetails(rs.getString(3));
				ps.setServiceName(rs.getString(4));
				return ps;
			} 
		});
	}
	
	
	public List<PendingService> getPendingServicesBySelectedMonth(ReportFilter reportFilter)
	{
		log.debug("entering  getPendingServicesBySelectedMonth !!!");
		List<PendingService> CompletePendingService = new ArrayList<PendingService>();
		
		//ListUtils.union(list1,list2);
		List<PendingService> PendingFirstService = getPendingFirstServiceForSeletedMonth(reportFilter);
		List<PendingService> PendingSecondService = getPendingSecondServiceForSeletedMonth(reportFilter);
		List<PendingService> PendingThirdService = getPendingThirdServiceForSeletedMonth(reportFilter);
		List<PendingService> PendingFourthService = getPendingFourthServiceForSeletedMonth(reportFilter);
		List<PendingService> PendingFifthService = getPendingFifthServiceForSeletedMonth(reportFilter);
		List<PendingService> PendingSixthService = getPendingSixthServiceForSeletedMonth(reportFilter);
		List<PendingService> PendingSeventhService = getPendingSeventhServiceForSeletedMonth(reportFilter);
		List<PendingService> PendingEigthService = getPendingEigthServiceForSeletedMonth(reportFilter);
		try{
		CompletePendingService.addAll(PendingFirstService);
		CompletePendingService.addAll(PendingSecondService);
		CompletePendingService.addAll(PendingThirdService);
		CompletePendingService.addAll(PendingFourthService);
		CompletePendingService.addAll(PendingFifthService);
		CompletePendingService.addAll(PendingSixthService);
		CompletePendingService.addAll(PendingSeventhService);
		CompletePendingService.addAll(PendingEigthService);
		log.debug("Exiting  getPendingServicesBySelectedMonth !!! : size of list :"+CompletePendingService.size());
		}
		catch(Exception ex)
		{
			log.debug("Exception while Adding the complete Pending Report :"+ex);
		}
		return CompletePendingService;
	}

}

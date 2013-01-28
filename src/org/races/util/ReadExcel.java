package org.races.util;
import jxl.*;   

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.apache.log4j.Logger; 

import org.races.model.customerDetails;
import org.races.model.serviceDetails;
 
public class ReadExcel {
    SimpleDateFormat ft = 
 	       new SimpleDateFormat ("yyyy-MM-dd");
    
    private static Logger log = Logger.getLogger(ReadExcel.class);
    
    public Object[] readExcelSheet(String destFile)
    { 
         File excelSheet = null;
         Workbook workbook = null;   
    
         List<customerDetails> listOfCustomerdetails= new ArrayList<customerDetails>();
         List<serviceDetails> listOfserviceDetails= new ArrayList<serviceDetails>();

         
           try {
                Workbook wb = Workbook.getWorkbook(new File(destFile));
                System.out.println(wb.getNumberOfSheets());

                
                   for(int sheetNo=0; sheetNo<wb.getNumberOfSheets();sheetNo++)
                   {                
                     Sheet sheet = wb.getSheet(sheetNo);
                     int columns = sheet.getColumns();
                     int rows = sheet.getRows();
                     String data; 
                     System.out.println("Sheet Name\t"+wb.getSheet(sheetNo).getName());
                             if(rows > 0 || columns > 0)
                             {
                                  //System.out.println("rows : "+rows + " cols : "+columns);
                     			for(int row = 4;row < rows;row++) {
                     				System.out.println("***** Row Started *******  : "+row);	
                     				log.info("***** Row Started *******  : "+row);
                     		      customerDetails custDet = new customerDetails();
                     		      serviceDetails serDet = new serviceDetails();	
                                 
                                 for(int col = 0;col < columns;col++) {
                                  data = sheet.getCell(col, row).getContents();
                                   if(!data.isEmpty())
                                  {
                                   	System.out.println("row : "+row +" cols :"+col);
                                   	log.info("row : "+row +" cols :"+col);
                                   	
                                   	if(col == 0)
                                   	{
                                   		System.out.println(getColumnName(col)+" : ");
                                   		System.out.println("Serial No :");                       
                                   		System.out.println(data);
                                   		log.info(getColumnName(col)+" : "+data);
                                   		try{
                                   		custDet.setCustomerId(Integer.parseInt(data));
                                   		}
                                   		catch(Exception e)
                                   		{
                                   			System.out.println("Exception at parsing customer ID : "+e);
                                   			log.fatal("Exception at parsing customer ID : "+e);
                                   		}
                                   		System.out.println("\n");
                                   	}
                                   	else if(col == 1)
                                   	{
                                   		// For Contact Details
                                   		 System.out.println(getColumnName(col)+" : ");
                                   		 log.info(getColumnName(col)+" : "+data);
                                   		custDet.setCustomerDetails(data);
                                   		System.out.println(data);
                                   		System.out.println("\n");
                                   		
                                   	}
                                   	else if(col == 2)
                                   	{
                                   		// For Parsing Chasis and engine no
                                   		System.out.println(getColumnName(col)+" : ");
                                   		log.info(getColumnName(col)+" : "+data);
                                   		parseVehicleChasisetails(col, data,custDet);
                                   		System.out.println("\n");
                                       	
                                   	}
                                  else if(col == 3)
                                   	{
                               	   	// For Date of Sale
                               	    	System.out.println("----- PARSE D.O.S ----");
                               	    	log.info(getColumnName(col)+" : "+data);
                               	    	parseDOS(col, data,custDet);
                                   		
                                   		 System.out.println("\n");
                                   	}
                                  else if(col == 4)
                              			{
                               	   		// For Installation Details.
                                	   		System.out.println("----- PARSE INSTALLATION ----");
                                	   		log.info(getColumnName(col)+" : "+data);
                                	   		parseInstallationDate(col, data,custDet);
                                	   		System.out.println("\n");
                              			}
                                  else if(col == 5 || col == 6)
                                 	{
                               	      try
                               	      {
                                        	 if(col == 5)
                                        	{
                                        		 System.out.println("----- PARSE 1ST SERVICE ----");
                                        		 log.info(getColumnName(col)+" : "+data);
                                        		 parseServiceDate(col, data,serDet);
                                   			if((serDet.getServiceDate() != null) || (serDet.getHours() > 0) )
                                   			{
                                   				serDet.setChasisNumber(custDet.getChasisNumber());
                                   			}
                                           	System.out.println("\n");
                                        	}
                                        	else{
                                          	   System.out.println("----- PARSE 1ST SERVICE HOURS----");
                                          	 log.info("1ST SERVICE HOURS  : "+data);
                                          	//parseServiceDate(col, data);
                                          	 System.out.println("1ST SERVICE HOURS : "+data);
                                          	 if(data.length() > 1)
                                          	 {
                                          	serDet.setHours(Integer.parseInt(data.trim()));
                                          	 }
                                          	if(serDet.getJobcardNo() != null)
                                          	{
                                          	if(serDet.getJobcardNo().length() != 0)
                                          	{
                                          	listOfserviceDetails.add(serDet);
                                          	}
                                          	}
                                          	serDet = null;
                                          	 System.out.println("\n");
                                           	}
                               	    	  
                               	      }
                               	      catch(Exception Ex)
                               	      {
                               	    	  log.fatal("Exception 1st service : "+col+" -- > "+Ex);
                               	    	  System.out.println("Exception : "+col+" -- > "+Ex);
                               	      }
                               	

                                 } 
                                  else if(col == 7 || col == 8)
                                 	{
                               	      try{
                               	    	  
                                      	   if(col == 7)
                                      	   {
                                   		   serDet = new serviceDetails();
                                   		   System.out.println("----- PARSE 2ND SERVICE ----");
                                   		   log.info("----- PARSE 2ND SERVICE ---- "+data);
                                      	    parseServiceDate(col, data,serDet);
                                  			if((serDet.getServiceDate() != null) || (serDet.getHours() > 0) )
                                  			{
                                  				serDet.setChasisNumber(custDet.getChasisNumber());
                                  			}
                                      	   }
                                   	   else
                                   	   {
                                    	     System.out.println("----- PARSE 2nd SERVICE HOURS----");
                                    	     log.info("2ND SERVICE HOURS : "+data);
                                       	 System.out.println("2ND SERVICE HOURS : "+data);
                                       	 serDet.setHours(Integer.parseInt(data.trim()));
                                     	if(serDet.getJobcardNo() != null)
                                      	{
                                          	if(serDet.getJobcardNo().length() != 0)
                                         	{
                                         	listOfserviceDetails.add(serDet);
                                         	}
                                        }
                                       	 serDet = null;
                                       	 System.out.println("\n");
                                   	   }                            	    	  
                               	    	  
                               	      }
                               	      catch(Exception ex)
                               	      {
                               	    	  log.fatal("Exception 2st service : "+col+" -- > "+ex);
                               	    	  System.out.println("Exception at : "+col+"--> "+ex);
                               	      }

                               	}
                                 	
                                  else if(col == 9 || col == 10)
                                 	{
                               	      try
                               	      {
                               	    	  
                                      	   if(col == 9)
                                   	   {
                                   		   serDet = new serviceDetails();
                                   		   
                                   		   System.out.println("----- PARSE 3ND SERVICE ----");
                                   		   log.info("----- PARSE 3ND SERVICE ---- : "+data);
                                      			parseServiceDate(col, data,serDet);
                                      			if((serDet.getServiceDate() != null) || (serDet.getHours() > 0) )
                                      			{
                                      				serDet.setChasisNumber(custDet.getChasisNumber());
                                      			}
                                   	   }
                                   	   else
                                   	   {
                                        	   System.out.println("----- PARSE 3RD SERVICE HOURS----");
                                         	   System.out.println("3RD SERVICE HOURS : "+data);	
                                         	   log.info("3RD SERVICE HOURS : "+data);
                                         	   serDet.setHours(Integer.parseInt(data.trim()));
                                         		if(serDet.getJobcardNo() != null)
                                              	{
                                            	if(serDet.getJobcardNo().length() != 0)
                                             	{
                                             	listOfserviceDetails.add(serDet);
                                             	}
                                            	}
                                         	   serDet = null;
                                         	   System.out.println("\n");
                                   	   }                            	    	  
                               	      }
                               	      catch(Exception ex)
                               	      {
                               	    	  log.fatal("Exception 3rt service : "+col+" -- > "+ex);
                               	    	  System.out.println("Exception at : "+col+"--> "+ex);
                               	      }


                               	}
                                 		
                                  else if(col == 11 || col == 12)
                                	{
                               	      try
                               	      {
                                      	   if(col == 11)
                                   	   {
                                   		   	serDet = new serviceDetails();                           		   
                                   		   	System.out.println("----- PARSE 4ND SERVICE ----");
                                     	   	log.info("----- PARSE 4ND SERVICE ---- : "+data);
                                     	   	
                                   		   	parseServiceDate(col, data,serDet);
                                      			if((serDet.getServiceDate() != null) || (serDet.getHours() > 0) )
                                      			{
                                      				serDet.setChasisNumber(custDet.getChasisNumber());
                                      			}
                                   	   }
                                   	   else
                                   	   {
                                        	   System.out.println("----- PARSE 4RD SERVICE HOURS----");
                                        	   System.out.println("4TH SERVICE HOURS : "+data);
                                        	   log.info("4TH SERVICE HOURS : "+data);
                                        	   serDet.setHours(Integer.parseInt(data.trim()));
                                        		if(serDet.getJobcardNo() != null)
                                              	{
                                            	if(serDet.getJobcardNo().length() != 0)
                                             	{
                                             	listOfserviceDetails.add(serDet);
                                             	}
                                            	}
                                         	   serDet = null;
                                        	   System.out.println("\n");
                                   	   }                            	    	  
                               	      }
                               	      catch(Exception ex)
                               	      {
                               	    	  log.fatal("Exception 4th service : "+col+" -- > "+ex);
                               	    	  System.out.println("Exception at : "+col+"--> "+ex);
                               	      }


                               	}	
                                  else if(col == 13 || col == 14)
                                	{
                               	      try
                               	      {
                                      	   if(col == 13)
                                   	   {
                                      		serDet = new serviceDetails();
                                      		   System.out.println("----- PARSE 5ND SERVICE ----");
                                      		   log.info("----- PARSE 5ND SERVICE ---- : "+data);
                                      		   parseServiceDate(col, data,serDet);
                                    			if((serDet.getServiceDate() != null) || (serDet.getHours() > 0) )
                                    			{
                                    				serDet.setChasisNumber(custDet.getChasisNumber());
                                    			}
                                   	   }
                                   	   else
                                   	   {
                                   		   System.out.println("----- PARSE 5TH SERVICE HOURS----");
                                   		   System.out.println("5TH SERVICE HOURS : "+data);                                  		   
                                   		   log.info("5TH SERVICE HOURS : "+data);
                                   		   serDet.setHours(Integer.parseInt(data.trim()));
                                   		if(serDet.getJobcardNo() != null)
                                      	{
                                      				if(serDet.getJobcardNo().length() != 0)
                                      				{
                                      					listOfserviceDetails.add(serDet);
                                      				}
                                      	}
                                   		serDet = null;
                                      				System.out.println("\n");
                                   	   }                            	    	  
                               	      }
                               	      catch(Exception ex)
                               	      {
                               	    	  log.fatal("Exception 5th service : "+col+" -- > "+ex);
                               	    	  System.out.println("Exception at parsing column: "+col+"--> "+ex);
                               	      }

                               	 } 	
                                  else if(col == 15 || col == 16)
                                	{
                               	      try
                               	      {
                                      	   if(col == 15)
                                   	   {
                                      		serDet = new serviceDetails();
                                   		   System.out.println("----- PARSE 6ND SERVICE ----");
                                   		   log.info("----- PARSE 6ND SERVICE ---- : "+data);
                                   		   
                                   		   parseServiceDate(col, data,serDet);
                                   			if((serDet.getServiceDate() != null) || (serDet.getHours() > 0) )
                                   			{
                                   				serDet.setChasisNumber(custDet.getChasisNumber());
                                   			}
                                   	   }
                                   	   else
                                   	   { 
                                   		   System.out.println("----- PARSE 6th SERVICE HOURS----");
                                   		   System.out.println("6TH SERVICE HOURS : "+data);	
                                   		   log.info("6TH SERVICE HOURS : "+data);
                                   		   serDet.setHours(Integer.parseInt(data.trim()));
                                   		if(serDet.getJobcardNo() != null)
                                      	{
                                 				if(serDet.getJobcardNo().length() != 0)
                                 				{
                                 					listOfserviceDetails.add(serDet);
                                 				}
                                      	}
                                   		serDet = null;
                                 				System.out.println("\n");
                                   	   }                            	    	  
                               	      
                               	      }
                               	      catch(Exception ex)
                               	      {
                               	    	  log.fatal("Exception 6th service : "+col+" -- > "+ex);
                               	    	  System.out.println("Exception at : "+col+"--> "+ex);
                               	      }

                               	   }
 
                                  else if(col == 17 || col == 18)
                                	{
                               	      try
                               	      {
                                      	   if(col==17)
                                   	   {
                                      		serDet = new serviceDetails();
                                     	   System.out.println("----- PARSE HYDRAULIC SERVICE ----");
                                     	   log.info("----- PARSE HYDRAULIC SERVICE ---- : "+data);
                                     	   parseServiceDate(col, data,serDet);
                                     	   	if((serDet.getServiceDate() != null) || (serDet.getHours() > 0) )
                                     	   	{
                                     	   		serDet.setChasisNumber(custDet.getChasisNumber());
                                     	   	}
                                   	   }
                                   	   else
                                   	   {
                                   		   	System.out.println("----- PARSE HYDRAULIC SERVICE HOURS----");
                                    	   		System.out.println("HYDRAULIC SERVICE HOURS : "+data);	
                                    	   		log.info("HYDRAULIC SERVICE HOURS : "+data);
                                    	  		serDet.setHours(Integer.parseInt(data.trim()));
                                    	  		if(serDet.getJobcardNo() != null)
                                              	{
                                				if(serDet.getJobcardNo().length() != 0)
                                				{
                                					listOfserviceDetails.add(serDet);
                                				}
                                				}
                                				serDet = null;
                                				System.out.println("\n");
                                   	   	}                            	    	  
                               	    	  
                               	      }
                               	      
                               	      catch(Exception ex)
                               	      {
                               	    	  log.fatal("Exception 7th service : "+col+" -- > "+ex);
                               	    	  System.out.println("Exception at : "+col+"--> "+ex);
                               	      }

                               	   }
                                  else if(col == 19 || col == 20)
                                	{
                               	      try
                               	      {
                                      	   if(col==19)
                                   	   {
                                      		serDet = new serviceDetails();
                                     	   System.out.println("----- PARSE 7TH SERVICE ----");
                                     	   log.info("----- PARSE 7TH SERVICE ----  : "+data);
                                     	   parseServiceDate(col, data,serDet);
                                     	   	if((serDet.getServiceDate() != null) || (serDet.getHours() > 0) )
                                     	   	{
                                     	   		serDet.setChasisNumber(custDet.getChasisNumber());
                                     	   	}
                                   	   }
                                   	   else
                                   	   {
                                   		   	System.out.println("----- PARSE 8TH SERVICE HOURS----");
                                    	   		System.out.println("8TH SERVICE HOURS : "+data);	
                                    	   		log.info("8TH SERVICE HOURS : "+data);
                                    	  		serDet.setHours(Integer.parseInt(data.trim()));
                                    	  		if(serDet.getJobcardNo() != null)
                                              	{
                                				if(serDet.getJobcardNo().length() != 0)
                                				{
                                					listOfserviceDetails.add(serDet);
                                				}
                                				}
                                				serDet = null;
                                				System.out.println("\n");
                                   	   	}                            	    	  
                               	    	  
                               	      }
                               	      
                               	      catch(Exception ex)
                               	      {
                               	    	  log.fatal("Exception 8th service : "+col+" -- > "+ex);
                               	    	  System.out.println("Exception at : "+col+"--> "+ex);
                               	      }

                               	   }
                                  else if(col == 21 || col == 22)
                                	{
                               	      try
                               	      {
                                      	   if(col==21)
                                   	   {
                                      		serDet = new serviceDetails();
                                     	   System.out.println("----- PARSE 8ND SERVICE ----");
                                     	   log.info("----- PARSE 8ND SERVICE ----  : "+data);
                                     	   parseServiceDate(col, data,serDet);
                                     	   	if((serDet.getServiceDate() != null) || (serDet.getHours() > 0) )
                                     	   	{
                                     	   		serDet.setChasisNumber(custDet.getChasisNumber());
                                     	   	}
                                   	   }
                                   	   else
                                   	   {
                                   		   	System.out.println("----- PARSE 8TH SERVICE HOURS----");
                                    	   		System.out.println("8TH SERVICE HOURS : "+data);	
                                    	   		log.info("8TH SERVICE HOURS : "+data);
                                    	  		serDet.setHours(Integer.parseInt(data.trim()));
                                    	  		if(serDet.getJobcardNo() != null)
                                              	{
                                				if(serDet.getJobcardNo().length() != 0)
                                				{
                                					listOfserviceDetails.add(serDet);
                                				}
                                				}
                                				serDet = null;
                                				System.out.println("\n");
                                   	   	}                            	    	  
                               	    	  
                               	      }
                               	      
                               	      catch(Exception ex)
                               	      {
                               	    	  log.fatal("Exception 8th service : "+col+" -- > "+ex);
                               	    	  System.out.println("Exception at : "+col+"--> "+ex);
                               	      }

                               	   }  
     
                                  }
 
                               
                             }
                                 listOfCustomerdetails.add(custDet);
                                 //listOfserviceDetails.add(serDet);
                                 System.out.println("***** Row Completed *******  : "+row);
                                 log.info("***** Row Completed *******  : "+row);
                             System.out.println("\n");
                         }
                     				
                     			}
                         }
                     } catch(Exception ioe) {
                          ioe.printStackTrace();
                     }
                return new Object[]{listOfCustomerdetails,listOfserviceDetails};
             }
    
    
 
 
 private String getColumnName(int columnNumber)
 {
	 Map<Integer,String> columnMapper = new HashMap<Integer,String>();
	 columnMapper.put(0,"Sl.No");
	 columnMapper.put(1,"Customer Name/ Address Phone number");
	 columnMapper.put(2,"Chassis no.Engine No.");
	 columnMapper.put(3,"D.O.S");
	 columnMapper.put(4,"Instalation");
	 columnMapper.put(5,"1st Service");
	 columnMapper.put(7,"2nd Service");
	 columnMapper.put(9,"3rd Service");
	 columnMapper.put(11,"4th Service");
	 columnMapper.put(13,"5th Service");
	 columnMapper.put(15,"6th Service");
 	 columnMapper.put(17,"Hydraulic Service");
	 columnMapper.put(19,"7th Service");
	 columnMapper.put(21,"8th Service");
	 return columnMapper.get(columnNumber);
 }
 
 
 
 private String parseVehicleChasisetails(int col,String data,customerDetails custDet)
 {
	 try{
		 System.out.println("parse chasis details : "+getColumnName(col));
		String[] ss = data.split("\\n");
		System.out.println("\n");
		System.out.println("Chasis No : "+ss[0]);
		custDet.setChasisNumber(ss[0].trim());
		//serDet.setChasisNumber(ss[0]);
		System.out.println("Vehicle No : "+ss[1]);
		custDet.setEngineNumber(ss[1].trim());	
		System.out.println("\n");
	 }
	 catch(Exception e)
	 {
		 System.out.println("Exception at parseVehicleChasisetails : "+e.getMessage());
	 }
		return null;
 }
 
 private String parseDOS(int col,String data,customerDetails custDet)
 {
	 StringBuffer dos=new StringBuffer(""); 
	 System.out.println(" ###### parseDOS :: called :: "+data+" ColumnName : "+getColumnName(col));
	 try{
		 
		if(data.length()==8)
		{
		System.out.println("DD : "+data.substring(0, 2));
	 	dos.append(data.substring(3,5) + "/");
		System.out.println("MM : "+data.substring(3, 5));
	 	dos.append(data.substring(0,2) + "/");
		System.out.println("YY : "+data.substring(6, 8));
	 	dos.append(data.substring(6,8));
		}
		System.out.println("Date without format : 1 : "+dos);
		Date date = new Date(dos.toString());
		 System.out.println("DATTE : "+ft.format(date));
		custDet.setDateOFsale(ft.format(date));
		System.out.println("\n");
	 }
	 catch(Exception e)
	 {
			System.out.println("Exception :at parseDOS/Installation : "+e.getMessage());
				 
	 }
		return null;
 }

 private String parseInstallationDate(int col,String data,customerDetails custDet)
 {
	 StringBuffer dos=new StringBuffer(""); 
	 System.out.println(" ###### parseDOS :: called :: "+data+" ColumnName : "+getColumnName(col));
	 try{
		 
		if(data.length()==8)
		{
		System.out.println("DD : "+data.substring(0, 2));
	 	dos.append(data.substring(3,5) + "/");
		System.out.println("MM : "+data.substring(3, 5));
	 	dos.append(data.substring(0,2) + "/");
		System.out.println("YY : "+data.substring(6, 8));
	 	dos.append(data.substring(6,8));
		}
		else if(data.length()==10)
		{
			System.out.println("DD : "+data.substring(0, 2));
			 dos.append(data.substring(3,5) + "/");
			System.out.println("MM : "+data.substring(3, 5));
		 	dos.append(data.substring(0,2) + "/");
			System.out.println("YY : "+data.substring(6, 10));
			dos.append(data.substring(6,10));
		}
		System.out.println("Date without format : 1 : "+dos);
		Date date = new Date(dos.toString());
		 System.out.println("DATTE : "+ft.format(date));
		custDet.setInstalledDate(ft.format(date));
		System.out.println("\n");
	 }
	 catch(Exception e)
	 {
			log.fatal("Exception :at parseServiceDate : "+e+" DATA: "+data);
			System.out.println("Exception :at parseDOS/Installation : "+e.getMessage());
				 
	 }
		return null;
 }
 private void parseServiceDate(int col,String data,serviceDetails serDet)
 {
	 if(data.length() > 2)
	 {
	 if(serDet == null)
	 {
		 System.out.println("aservice date is null @ parsing method ....");
	 }
	 
	try{
		StringBuffer dos=new StringBuffer(""); 
		System.out.println("Column Name : "+getColumnName(col));
	 System.out.println("parseServiceDate :: called :: "+data);
	 String[] ss = data.split("\\n");
	 System.out.println("DATE TO PARSE IN ..METHOD : "+ss[0]);
	 
	 
	 

	 
		if(ss[0].length()==8)
		{
		System.out.println("DD : "+ss[0].substring(0, 2));
		 dos.append(ss[0].substring(3,5) + "/");
		System.out.println("MM : "+ss[0].substring(3, 5));
	 	dos.append(ss[0].substring(0,2) + "/");
		System.out.println("YY : "+ss[0].substring(6, 8));
		dos.append(ss[0].substring(6,8));
		}
		else if(ss[0].length()==10)
		{
			System.out.println("DD : "+ss[0].substring(0, 2));
			 dos.append(ss[0].substring(3,5) + "/");
			System.out.println("MM : "+ss[0].substring(3, 5));
		 	dos.append(ss[0].substring(0,2) + "/");
			System.out.println("YY : "+ss[0].substring(6, 10));
			dos.append(ss[0].substring(6,10));
			
		}

		System.out.println("Date without format : 2 :"+dos);
		Date date = new Date(dos.toString());
		 System.out.println("formatted DATE : "+ft.format(date));
		 log.info("formatted DATE : "+ft.format(date));
 		 serDet.setServiceName(getColumnName(col));
 		 serDet.setServiceDate(ft.format(date));
 		 
		 if(ss.length == 2)
		 {
		 System.out.println("JOBCARD NO TO PARSE IN ..METHOD "+ss[1]);  
		 log.info("JOBCARD NO TO PARSE IN ..METHOD "+ss[1]);
		 serDet.setJobcardNo(ss[1]);
		 }
		 else
		 {
			 log.fatal("JOBCARD NO FOUND !!!!");
			 System.out.println("JOBCARD NO FOUND !!!!");
		 }
		 System.out.println("\n");
	}
	catch(Exception e)
	{
		log.fatal("Exception :at parseServiceDate : "+e+" DATA: "+data);
		System.out.println("Exception :at parseServiceDate : "+e+"DATA : "+data);
	}
		
	 }
 }
 
 
 
      public static void main(String arg[]){
            ReadExcel excel = new ReadExcel(); 
         // Object[] objects = excel.readExcelSheet("/Volumes/Sashi/races_customer/fulldatalatest.xls");
          //Object[] objects = excel.readExcelSheet("/Volumes/Sashi/races_customer/data used in db/service data for db/service_nov12-to-oct13.xls");
         // Object[] objects = excel.readExcelSheet("/Volumes/Sashi/races_customer/data used in db/service data for db/service_Apr12-mar13.xls");
          
            //Object[] objects = excel.readExcelSheet("/Volumes/Sashi/races_customer/data used in db/TestData100113/testdataforchart1.xls");
	    	 Object[] objects = excel.readExcelSheet("/Volumes/Sashi/races_customer/data used in db/TestData100113/200to300Data.xls");

          
          
         // Object[] objects = excel.readExcelSheet("/Volumes/Sashi/races_customer/testme.xls");
          
          List<customerDetails> listofcustDetails = (List<customerDetails>) objects[0];
          List<serviceDetails> listofserviceDetails = (List<serviceDetails>) objects[1]; 
          System.out.println("Count for Customer : "+listofcustDetails.size());
          System.out.println("Count for Service : "+listofserviceDetails.size());
          System.out.println("###############################################################");
          try
          {
          printData(listofcustDetails,listofserviceDetails);
          
          
          }
          catch(Exception E)
          {
        	  System.out.println("Exception : MAIN "+E);
          }
          
      }
      public static void printData(List<customerDetails> customerDetailslist,List<serviceDetails> listofserviceDetails)
      {
    	  for(customerDetails cd : customerDetailslist)
    	  {
    		  if(cd != null)
    		  {System.out.println("---***************************************************---");
    		  System.out.println("customer id : "+cd.getCustomerId());
    		  System.out.println("customer details : "+cd.getCustomerDetails());
    		  System.out.println("Chasis No : "+cd.getChasisNumber());
    		  System.out.println("Engine No : "+cd.getEngineNumber());
    		  System.out.println("Date Of Sale : "+cd.getDateOFsale());
    		  System.out.println("Installation Date : "+cd.getInstalledDate());
    		  }
    	  }
    	  for(serviceDetails sd : listofserviceDetails)
    	  {
    		  if(sd != null)
    		  {
    		  System.out.println("---@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@---");
    		  System.out.println("Service Name :"+sd.getServiceName());
    		  System.out.println("JobcardNo :"+sd.getJobcardNo());
    		  System.out.println(" ServiceDate :"+sd.getServiceDate());
    		  System.out.println(" Service Hours :"+sd.getHours());
    		  System.out.println("Cost Charged : "+sd.getCostCharged());
    		  System.out.println("Chasis Number : "+sd.getChasisNumber()); 
    		  }
    	  }
    	  
      }
      
      

}
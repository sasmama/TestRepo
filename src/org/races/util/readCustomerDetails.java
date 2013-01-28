package org.races.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.races.model.customerDetails;

import jxl.Sheet;
import jxl.Workbook;

public class readCustomerDetails {


    SimpleDateFormat ft = 
 	       new SimpleDateFormat ("yyyy-MM-dd");
    private static Log log = LogFactory.getLog(readCustomerDetails.class);
 public Object[] readExcelSheet(String destFile){  
 
      List<customerDetails> listOfCustomerdetails= new ArrayList<customerDetails>(); 
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
                   			for(int row = 0;row < rows;row++) {
                  				System.out.println("***** Row Started *******  : "+row);	
                  		      customerDetails custDet = new customerDetails();
                               
                              for(int col = 0;col < columns;col++) {
                               data = sheet.getCell(col, row).getContents();
                                if(!data.isEmpty())
                               {
                                 	
                                	if(col == 0)
                                	{
                                 		System.out.println(getColumnName(col)+" : "+Integer.parseInt(data));
 
                                		try{
                                		custDet.setCustomerId(Integer.parseInt(data));
                                		}
                                		catch(Exception e)
                                		{
                                			System.out.println("Exception at parsing customer ID : "+e);
                                		}
                                		System.out.println("\n");
                                	}
                                	else if(col == 2)
                                	{
                                 		//CUSTOMER DETAILS
                                		System.out.println(getColumnName(col)+" : "+data);
                                		custDet.setCustomerDetails(data);
                                	 	System.out.println("\n");
                                		
                                	}
 
                                	else if(col == 4)
                                	{
                                 		//CHASIS NUMBER
                                		System.out.println(getColumnName(col)+" : "+data);
                                		custDet.setChasisNumber(data);
                                		//parseVehicleChasisetails(col, data,custDet);
                                		System.out.println("\n");
                                    	
                                	}
                                	else if(col == 5)
                                	{
                                 		//ENGINE NUMBER
                                		System.out.println(getColumnName(col)+" : "+data);
                                		custDet.setEngineNumber(data);
                                		//parseVehicleChasisetails(col, data,custDet);
                                		System.out.println("\n");
                                    	
                                	}
                                	
                               else if(col == 6)
                                	{
                             	   		//DATE OF SALE
                            	    	 System.out.println("----- PARSE D.O.S ----");
                                		 parseDOS(col, data,custDet);
                                		 System.out.println("\n");
                                	}
                             
              
                            
                               }
 
                            
                          }
                              listOfCustomerdetails.add(custDet);
                               System.out.println("***** Row Completed *******  : "+row);
                          System.out.println("\n");
                      }
                  				
                  			}
                      }
                  } catch(Exception ioe) {
                	  log.fatal("Exception at Read Customer Details : "+ioe);
                       ioe.printStackTrace();
                  }
             return new Object[]{listOfCustomerdetails};
          }
 
 private String getColumnName(int columnNumber)
 {
	 Map<Integer,String> columnMapper = new HashMap<Integer,String>();
	 columnMapper.put(0,"Sl.No");
	 columnMapper.put(2,"Customer Name/ Address Phone number");
 	 columnMapper.put(4,"Chassis no");
	 columnMapper.put(5,"Engine No.");
	 columnMapper.put(6,"D.O.S");
	 return columnMapper.get(columnNumber);
 } 
 
 private String parseDOS(int col,String data,customerDetails custDet)
 {
	 StringBuffer dos=new StringBuffer(""); 
	 System.out.println(" ###### parseDOS :: called :: "+data+" ColumnName : "+getColumnName(col));
	 try{
		 
		if(data.trim().length()==8)
		{
		System.out.println("DD : "+data.substring(0, 2));
	 	dos.append(data.substring(3,5) + "/");
		System.out.println("MM : "+data.substring(3, 5));
	 	dos.append(data.substring(0,2) + "/");
		System.out.println("YY : "+data.substring(6, 8));
	 	dos.append(data.substring(6,8));
		}
		else if(data.trim().length()==10)
		{
		System.out.println("DD : "+data.substring(0, 2));
	 	dos.append(data.substring(3,5) + "/");
		System.out.println("MM : "+data.substring(3, 5));
	 	dos.append(data.substring(0,2) + "/");
		System.out.println("YY : "+data.substring(6, 10));
	 	dos.append(data.substring(6,10));
		}
		System.out.println("Date without format :  : "+dos);
		Date date = new Date(dos.toString());
		 System.out.println("DATTE : "+ft.format(date));
		custDet.setDateOFsale(ft.format(date));
		System.out.println("\n");
	 }
	 catch(Exception e)
	 {
		 	log.fatal("Exception in parsing DOS / Installation : "+e);
			System.out.println("Exception :at parseDOS/Installation : "+e.getMessage());
				 
	 }
		return null;
 }
  
 
 
 
      public static void main(String arg[]){
    	  readCustomerDetails excel = new readCustomerDetails(); 
         // Object[] objects = excel.readExcelSheet("/Volumes/Sashi/races_customer/fulldatalatest.xls");
         // Object[] objects = excel.readExcelSheet("/Volumes/Sashi/races_customer/data used in db/customerdetails.xls");
          
          Object[] objects = excel.readExcelSheet("/Volumes/Sashi/races_customer/sampletotest.xls");
          
          
           
          List<customerDetails> listofcustDetails = (List<customerDetails>) objects[0];  
          System.out.println("Count for Customer : "+listofcustDetails.size());
           try
          {
          //printData(listofcustDetails);
          
          
          }
          catch(Exception E)
          {
        	  System.out.println("Exception : MAIN "+E);
          }
          
      }
      public static void printData(List<customerDetails> customerDetailslist)
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
 
    	  
      }
      
      



}

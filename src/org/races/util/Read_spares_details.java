package org.races.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.races.model.spares_details;

import jxl.Sheet;
import jxl.Workbook;

public class Read_spares_details {
	
	private static Log log = LogFactory.getLog(Read_spares_details.class);
	
	public Object[] readExcelSheet(String destFile){  
		 
	      List<spares_details> listOfSparesdetails= new ArrayList<spares_details>(); 
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
	                   			for(int row = 3;row < rows;row++) 
	                   			{
	                  				System.out.println("***** Row Started *******  : "+row);	
	                  		      spares_details sparesDetail = new spares_details();
	                               
	                              for(int col = 0;col < columns;col++) {
	                               data = sheet.getCell(col, row).getContents();
	                                if(!data.isEmpty())
	                               { 
	                                	if(col == 1)
	                                	{
	                                 		//Part No 
	                                		System.out.println(getColumnName(col)+" : "+data);
	                                		sparesDetail.setSpare_code(data);
	                                	 	System.out.println("\n");
	                                		
	                                	}
	 
	                                	else if(col == 2)
	                                	{
	                                 		//Part Description
	                                		System.out.println(getColumnName(col)+" : "+data);
	                                		sparesDetail.setPart_description(data);
 	                                		System.out.println("\n");
	                                    	
	                                	}
	                                	else if(col == 3)
	                                	{
	                                 		//MINIMUM ORDER VALUE
	                                		System.out.println(getColumnName(col)+" : "+data);
	                                		sparesDetail.setMin_order(Integer.parseInt(data));
	                                		//parseVehicleChasisetails(col, data,custDet);
	                                		System.out.println("\n"); 
	                                	}
	                                	else if(col == 5)
	                                	{
	                                 		//COST PER UNIT
	                                		System.out.println(getColumnName(col)+" : "+data);
	                                		sparesDetail.setCost(Float.parseFloat(data));
 	                                		System.out.println("\n"); 
	                                	} 	                            
	                               }
	 
	                            
	                          }
	                              listOfSparesdetails.add(sparesDetail);
	                               System.out.println("***** Row Completed *******  : "+row);
	                          System.out.println("\n");
	                      }
	                  				
	                  			}
	                      }
	                  } catch(Exception ioe) {
	                	  log.fatal("Exception in parsing data of spares details : "+ioe);
	                       ioe.printStackTrace();
	                  }
	             return new Object[]{listOfSparesdetails};
	          }
	
	 private String getColumnName(int columnNumber)
	 {
		 Map<Integer,String> columnMapper = new HashMap<Integer,String>();
		 columnMapper.put(1,"Part No :");
		 columnMapper.put(2,"Part Description :");
	 	 columnMapper.put(3,"Minimum Qty :");
		 columnMapper.put(5,"Cost :"); 
		 return columnMapper.get(columnNumber);
	 } 

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Read_spares_details excel = new Read_spares_details(); 

		Object[] objects = excel.readExcelSheet("/Volumes/Sashi/races_customer/spare parts details/JD Parts list.xls");
		List<spares_details> sdList =(ArrayList<spares_details>)objects[0];
		System.out.println("spares count :"+sdList.size());

	}

}

package org.races.service;

import java.io.ByteArrayOutputStream;   
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger; 

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.races.Constants.ReportConstants;
import org.races.dao.RacesDao;
import org.races.model.PendingService;
import org.races.model.ReportFilter;
import org.races.model.ServiceChart;
import org.races.model.UserDetails;
import org.races.util.FormatType;
import org.races.util.JasperUtil;
import org.races.util.SendMail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.CannotGetJdbcConnectionException;


public class RacesServiceImpl implements RacesService {

	@Autowired
	RacesDao daoObject;
	@Autowired
	SendMail sendMail;
	@Autowired
	JasperUtil jasperUtil;

    /**
     * Class path where the usage trend chart is stored.
     */
	@Value("${usageTrend.chart.location}")
	 private String usageTrendFile;
	 
	   /**
	     * Absolute path where the JASPER HTML exporter puts its image file into
	     * disk.
	   */
	 	@Value("${usageTrend.image.location}")
	    public String userTrendImage;
	    
	    /**
	     * JASPER file identifier to read file from class path. This .jasper file is
	     * used to generate usage trend chart.
	     */
	    @Value("${usageTrend.chart.jasper}")
	    public String usageTrendIdentifier;
	    
	    /**
	     * JASPER file identifier to read file from class path. This .jasper file is
	     * used to generate pending service report.
	     */
	    @Value("${pendingReport.jasper}")
	    public String pendingReport;
	    
	    
	    /**
	     * JASPER file location, used to generate failure report to be shown in snap
	     * shot and usage trend chart location on any failures.
	     */
	    @Value("${dashboard.failurereport.jasper}")
	    public String failureReport;
	    
	private static Logger log = Logger.getLogger(RacesServiceImpl.class);
	@Override
	public String getAuthenticationValue(String userName,String password) {	
		UserDetails user_detail = daoObject.getLoginDetails(userName, password);
		try {
		if(user_detail != null)
		{
			if(user_detail.getLoginType() != null)
			{
				log.info("obtained the login type");
				return user_detail.getLoginType().toUpperCase();
			}
			else
			{
				log.info("obtained the login type : FAIL");
				return "FAIL";
			}
		}
		else
		{
			log.info("obtained the login type : user details id NULL");
			return "FAIL";
		}
 	}
	catch(NullPointerException nullException)
	{
		System.out.println("USER DETAILS IS NULL");
		log.debug(" user details is NULL");
		return "FAIL";
	} 
}

	@Override
	public String getForgotPassword(String userName) {
		UserDetails userdetails = daoObject.getEmailId(userName);
		String message = sendMail(userdetails.getEmail_id(),userdetails.getPassword());
		return message;
	}

	@Override
	public String sendMail(String email_id,String password) {
		// TODO Auto-generated method stub
		String message = sendMail.forgotPassword(email_id, password);
		return message;
	}
	
    /**
     * Saves the created usage trend chart into required '.png' image format.
     */
    private void saveUsageTrend() {
        log.info("Saving usage trend chart as .png image.");
        System.out.println("Saving usage trend chart as .png image.");
        String jasperImage = ReportConstants.USER_HOME + userTrendImage;
        System.out.println("jasperImage loaction : "+jasperImage);
        File imageFile = new File(jasperImage);
        if (imageFile.exists()) {
            String usageTrendImage = ReportConstants.USER_HOME + usageTrendFile;
            System.out.println("usageTrendImage location : "+usageTrendImage);
            File usageTrendImageFile = new File(usageTrendImage);
            // Deletes the old file.
            if (usageTrendImageFile.exists()) {
                boolean isDeleted = usageTrendImageFile.delete();
                log.debug("usageTrendFile deleted status: " + isDeleted);
            }
            boolean isRenamed = imageFile.renameTo(new File(usageTrendImage));
            log.debug("usageTrendFile reNamed status: " + isRenamed);
        }
        log.info("Uage trend chart saved.");
    }
    
    
    
    /**
     * Creates usage trend chart for ALUM Dash board. Values are pulled from DB
     * and filled into a JASPER chart and exported as HTML. The exporter is
     * instructed to save the image file in a local disk location.
     * 
     * Stored image is the required chart as file. Later the file is changed
     * into respective .png format and stored in the same location.
     * 
     * @param formatType
     *            - Type of the file (a PDF, HTML, etc) to be created.
     * @return byte array of the usage trend chart.
     */
    @Override
    public ByteArrayOutputStream createUsageTrend(FormatType formatType) {
        //log.info("Entering create usage trend service.");
        System.out.println("Entering create usage trend service.");
        ByteArrayOutputStream jasperByteArray = null;
 
        if (jasperByteArray == null) {
            // Gets JASPER file for generating usage trend chart as input
            // stream.
            InputStream usageTrendStream = this.getClass().getResourceAsStream(
                    usageTrendIdentifier);
            // Gets usage trend data from DB.
            try {
            	Object[] objectList = daoObject.getDataForChart();
                List<ServiceChart> usageTrendList = getServiceChartDao((LinkedHashMap)objectList[0],(LinkedHashMap)objectList[1]);
                	System.out.println("usageTrendList SIZE : "+usageTrendList.size());
                if (usageTrendList != null && !usageTrendList.isEmpty()) {
                    Map<String, Object> parameters = new HashMap<String, Object>();
                    parameters.put("Servicechartdata", usageTrendList);
                     JasperPrint jasperPrint = JasperFillManager.fillReport(
                            usageTrendStream, parameters,
                            new JRBeanCollectionDataSource(usageTrendList));

                    
                    jasperByteArray = jasperUtil.jasperPrintToByteArray(
                            jasperPrint, formatType);
                    if (formatType == FormatType.IMAGE) {
                        // Saves usage trend chart as .png image.
                        saveUsageTrend();
                    }
                    System.out.println("Size Of : jasperByteArray "+jasperByteArray.size());
                } else {
                	log.debug("Jasper Error.... !!! usageTrendList NULL or Empty");
                	System.out.println("Jasper Error.... !!! usageTrendList NULL or Empty");
                   // jasperByteArray = getFailureReport(noDataMessage);
                }
            } catch (JRException jasperException) {
                log.error("Excpetion while filling jasper report",
                        jasperException);
            } catch (CannotGetJdbcConnectionException dataAccessException) {
                //jasperByteArray = getFailureReport(connectionFailure);
            }
            finally {
                closeInputStream(usageTrendStream);
            }
         }
        log.info("Exiting create usage trend service.");
        return jasperByteArray;
    }
    
    
    

    /**
     * Generates the failure report for the given failure message.
     * 
     * The report generated will be used in dash board main page, to announce
     * the failures to users.
     * 
     * @param failureMessage
     *            - Message to be displayed in report.
     * @return Failure report with the given message.
     */
    @Override
    public ByteArrayOutputStream getFailureReport(String failureMessage) {
        log.info("Entering create usage trend service.");
        ByteArrayOutputStream reportByteArray = null;
        // Gets JASPER file for generating failure report as stream.
        InputStream failureReportStream = this.getClass().getResourceAsStream(
                failureReport);
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("errorMessage", failureMessage);
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    failureReportStream, parameters, new JREmptyDataSource());
            reportByteArray = jasperUtil.jasperPrintToByteArray(jasperPrint,
                    FormatType.HTML);
        } catch (JRException jasperException) {
            log.error("Excpetion while filling jasper report",
                    jasperException);
        } finally {
            closeInputStream(failureReportStream);
        }
        log.info("Exiting create usage trend service.");
        return reportByteArray;
    }
    
    
	private List<ServiceChart> getServiceChartDao(LinkedHashMap pendingServices,LinkedHashMap completedServices)
	{
		List<ServiceChart> ServiceChartList = new ArrayList<ServiceChart>();
		
		try{
		Set pendingSeviceSet = pendingServices.keySet();
		Set completedServiceSet = completedServices.keySet();

		Object[] pendingSeviceKeyArray = pendingSeviceSet.toArray();
		Object[] completedSeviceKeyArray = completedServiceSet.toArray();
		
		System.out.println("pendingSeviceSet :"+pendingSeviceSet.size());
		System.out.println("completedServiceSet : "+completedServiceSet.size());

 
		Iterator pendingSeviceIterator = pendingSeviceSet.iterator();
		Iterator comletedSeviceIterator = completedServiceSet.iterator();
		
		if(pendingSeviceSet.size() > completedServiceSet.size())
		{ int i=0;
			int j=0;
			ServiceChart serviceChart;
			try
			{
			while(pendingSeviceIterator.hasNext())
			{
				serviceChart = new ServiceChart();
				pendingSeviceIterator.next();
 				serviceChart.setDuration(pendingSeviceKeyArray[i].toString());
				serviceChart.setPending_count(Integer.parseInt(pendingServices.get(pendingSeviceKeyArray[i]).toString().trim()));
				if(completedServices.containsKey(pendingSeviceKeyArray[i]))
				{
					serviceChart.setCompleted_count(Integer.parseInt(completedServices.get(pendingSeviceKeyArray[i]).toString().trim()));
				}
				else
				{
					serviceChart.setCompleted_count(Integer.parseInt("0"));
				}
				ServiceChartList.add(serviceChart);
				i++;
			} }
			catch(Exception e)
			{
				System.out.println("pending > completed : "+e);
			}
		}
		else if(pendingSeviceSet.size() < completedServiceSet.size())
		{
			try
			{
			int i=0;
			int j=0;
			ServiceChart serviceChart;
			while(comletedSeviceIterator.hasNext())
			{
				serviceChart = new ServiceChart();
				comletedSeviceIterator.next();
				 if(completedSeviceKeyArray[i] != null)
				 {
					 serviceChart.setDuration(completedSeviceKeyArray[i].toString());
					  
						 serviceChart.setCompleted_count(Integer.parseInt(completedServices.get(completedSeviceKeyArray[i]).toString().trim()));
					 
					  	 System.out.println("1st IF : No Data For PendingService !!!!");
				 	 
				if(pendingServices.containsKey(completedSeviceKeyArray[i]))
				{
						serviceChart.setPending_count(Integer.parseInt(pendingServices.get(completedSeviceKeyArray[i]).toString().trim()));
				 		System.out.println("2nd IF : No Data For PendingService !!!!");
				}
				else
				{
					serviceChart.setPending_count(Integer.parseInt("0"));
				}
				ServiceChartList.add(serviceChart);
				i++;
				 }
			} 
			}
			catch(Exception e)
			{
				System.out.println("pending < completed : "+e);
			}
			
		}
		else
		{
			try
			{
			int i=0;
			int j=0;
			ServiceChart serviceChart;
			while(pendingSeviceIterator.hasNext())
			{ 
				 serviceChart = new ServiceChart();
				pendingSeviceIterator.next();
 				serviceChart.setDuration(pendingSeviceKeyArray[i].toString());
				serviceChart.setPending_count(Integer.parseInt(pendingServices.get(pendingSeviceKeyArray[i]).toString().trim()));
				if(completedServices.containsKey(pendingSeviceKeyArray[i]))
				{
					serviceChart.setCompleted_count(Integer.parseInt(completedServices.get(pendingSeviceKeyArray[i]).toString().trim()));
				}
				else
				{
					serviceChart.setCompleted_count(Integer.parseInt("0"));
				}
				ServiceChartList.add(serviceChart);
				i++;
			} 
			}
			catch(Exception ex)
			{
				System.out.println("Exception at : pending = actual : "+ex);
			}
		}
 		}
		catch(Exception ex)
		{
			System.out.println("Exception in getting Chart Pojo :"+ex);
		}
		System.out.println("Size of ServiceChartList : "+ServiceChartList.size());
		return ServiceChartList;
	}
    
    /**
     * Utility to close the given input stream.
     * 
     * @param inputStream
     *            - input stream to be closed.
     */
    private void closeInputStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException ioException) {
                log.error("IO Exception while closing the stream",
                        ioException);
            }
        }
    }
    
    /**
     * Gets the Top Search terms, will be displayed in report page. It contains
     * Search term and count.
     * 
     * If searchReportList is empty or SQLException occurs while interacting
     * with DB then error JASPER will be displayed.
     * 
     * @param reportFilter
     *            - Has event details like search term.
     * @return Byte Array of the event report generated.
     */
    @Override
    //getTopSearchTopic
    public ByteArrayOutputStream getPendingReport(ReportFilter reportFilter) {
        log.debug("Entering Report Service to getPendingReport.");
        ByteArrayOutputStream reportByteArray = null;
        Map<String, Object> parameters = new HashMap<String, Object>();
        try {
            InputStream reportStream;
            List<PendingService> listOfPendingServices = null;  
                listOfPendingServices = daoObject.getPendingServicesBySelectedMonth(reportFilter); 
                parameters.put("PendingReportdata",new JRBeanCollectionDataSource(listOfPendingServices));
                log.debug("Parameter Size : "+parameters.size());
                reportStream = this.getClass().getResourceAsStream(pendingReport); 
                reportByteArray = getReportByteArray(reportStream, reportFilter,
                        parameters);
            closeInputStream(reportStream);
            log.debug("Existing getPendingReport service method.");
        } catch (Exception e) {
            log.error("SQLException occurred while calling from : "+e);
          //  reportByteArray = getDBErrorReport(reportFilter,
          //          ReportConstants.ERROR_MSG, parameters);
            log.info("Jasper with error message is displayed");
        }
        return reportByteArray;
    }
    
    /**
     * Gets the byte array for all the reports.
     * 
     * @param reportStream
     *            - report Stream of the report.
     * @param reportFilter
     *            - Has event details like search term.
     * @param parameters
     *            - Report parameters.
     * @return byte array of report.
     */
    private ByteArrayOutputStream getReportByteArray(InputStream reportStream,
            ReportFilter reportFilter, Map<String, Object> parameters) {
        log.info("Entering into getReportByteArray service method.");
        ByteArrayOutputStream reportByteArray = null;
        try {
        	reportFilter.setFormatType(FormatType.HTML);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    reportStream, parameters, new JREmptyDataSource());
            reportByteArray = jasperUtil.jasperPrintToByteArray(jasperPrint,
                    reportFilter.getFormatType());
            if(reportByteArray != null)
            {
            log.debug("ByteArray size : " + reportByteArray.size());
            }
            else
            {
            	log.error("reportByteArray is NULL !!!!");
            }
        } catch (JRException jrException) {
            log.error("Jasper exception occured: ", jrException);
        }
        catch(Exception ex)
        {
        	log.error("Exception at getReportByteArray : "+ex);
        }
        log.info("Existing into getReportByteArray service method.");
        return reportByteArray;
    }
    
    
    
	}

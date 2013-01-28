package org.races.controller;

import java.io.BufferedInputStream;   
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import net.sf.jasperreports.web.servlets.ReportServlet;

import org.apache.log4j.Logger; 
import org.races.Constants.ReportConstants;
import org.races.model.ReportFilter;
import org.races.service.RacesService;
import org.races.util.FormatType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView; 
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;;

@Controller
public class RacesController {
	@Autowired
	RacesService racesService;
	
    /**
     * class path name of usageTrendChart.jasper(a '.jasper' file to generate
     * usage trend chart).
     */
    @Value("${usageTrend.chart.location}")
    private String usageTrendFile;

	private static Logger log = Logger.getLogger(RacesController.class);
@RequestMapping("getReport.do")
public ModelAndView getReport(HttpServletRequest request, HttpServletResponse response,ReportFilter reportFilter)
{
	ModelAndView mav = null;

	try {
		System.out.println("@ controller.....");
		log.info("Entered getReport ..");
		log.info("Current Date" + reportFilter.getForCurrentMonth());
		reportFilter.setForCurrentMonth("2008-02-01");
		byte[] reportArray = racesService.getPendingReport(reportFilter).toByteArray();
		String reportString = new String(reportArray,ReportConstants.CHAR_SET);
		mav = new ModelAndView("racesreport");
		mav.addObject("reportString",reportString );
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}

	return mav;
} 

@RequestMapping("authenticateUser.do")
public ModelAndView doAuthentication(HttpServletRequest request, HttpServletResponse response)
{   ModelAndView mav = null;
    mav = new ModelAndView("raceshome");
	String username = request.getParameter("userName");
	String password = request.getParameter("password");
	System.out.println("@ Authentication.....username"+username); 
	log.info("Authentication.....");
	String message = racesService.getAuthenticationValue(username,password);
	if(message == "FAIL") {
		mav = new ModelAndView("index");
		mav.addObject("message", "No User Found");
	}
	return mav;
}


@RequestMapping("racesHome.do")
public ModelAndView loadLoginpage(HttpServletRequest request, HttpServletResponse response)
{
	System.out.println("@ Loading login page....."); 
	return new ModelAndView("index");
}


@RequestMapping("forgotPassword")
public ModelAndView forgotPassword()
{
	log.info("Forgot Password .....");
	System.out.println("@ Forgot Password....."); 
	racesService.getForgotPassword("");
	return new ModelAndView("test");
}
@RequestMapping("createSalesReport.do")
public void createUsageTrend(HttpServletRequest request,
        HttpServletResponse response) {
    long startTime = System.currentTimeMillis();
    log.info("Creating Sales Report.--------" + startTime);
    try {
        response.setContentType(ReportConstants.HTML_CONTENT_TYPE);
       // response.getOutputStream().write(
        		//racesService.createUsageTrend(FormatType.IMAGE)
                //        .toByteArray());
        response.flushBuffer();
    } catch (IOException ioException) {
        log.error("IO exception has occurred.", ioException);
    }
    log.info("Done! Creating Sales Report.-------- ");
}

@RequestMapping("getSalesReportChart.do")
public ModelAndView getServiceReportChart(HttpServletRequest request,
        HttpServletResponse response) {
    long startTime = System.currentTimeMillis();
    log.info("Entering getImage to get Sales report chart image.----"
            + startTime);
    response.setContentType(ReportConstants.IMAGE_TYPE);
    ModelAndView mav = new ModelAndView(ReportConstants.EXCEPTION_PAGE);
    try {
        String usageTrendChart = ReportConstants.USER_HOME + usageTrendFile;
        InputStream inputStream = new BufferedInputStream(
                new FileInputStream(usageTrendChart));
        ImageIO.write(ImageIO.read(inputStream),
                ReportConstants.FORMAT_TYPE, response.getOutputStream());
        inputStream.close();
        log.info("Writing image into output stream.");
    } catch (FileNotFoundException filesException) {
        log.error("Image not found in the respective location.",
                filesException);
    } catch (IOException ioException) {
        log.error("Unable to write image.", ioException);
        mav.addObject(ReportConstants.ERROR_MESSAGE,
                "IO Exception while getting report for event.");
        return mav;
    }
    return null;
}

@RequestMapping("createServiceReport.do")
public void createServiceReport(HttpServletRequest request,
        HttpServletResponse response) {
    long startTime = System.currentTimeMillis();
    log.info("Creating Service Report.--------" + startTime);
    System.out.println("createServiceReport.do --- Started");
    try {
        response.setContentType(ReportConstants.HTML_CONTENT_TYPE);
        response.getOutputStream().write(
        		racesService.createUsageTrend(FormatType.IMAGE)
                        .toByteArray());
        response.flushBuffer();
    } catch (IOException ioException) {
        log.error("IO exception has occurred.", ioException);
    }
    catch(java.lang.NullPointerException NPE)
    {
    	System.out.println("Exception at createServiceReport.do : "+NPE);
    }
    log.info("Done! Creating Service Report.-------- ");
    System.out.println("createServiceReport.do ----- Completed");
}

@RequestMapping("getServiceReportChart.do")
public ModelAndView getUsageTrendChart(HttpServletRequest request,
        HttpServletResponse response) {
    long startTime = System.currentTimeMillis();
    log.info("Entering getImage to get Service report chart image.----"
            + startTime);
    System.out.println("SASHI 1");
    System.out.println("getServiceReportChart.do : STARTED");
    response.setContentType(ReportConstants.IMAGE_TYPE);
    ModelAndView mav = new ModelAndView(ReportConstants.EXCEPTION_PAGE);
    try {
        String usageTrendChart = ReportConstants.USER_HOME + usageTrendFile;
        InputStream inputStream = new BufferedInputStream(
                new FileInputStream(usageTrendChart));
        ImageIO.write(ImageIO.read(inputStream),
                ReportConstants.FORMAT_TYPE, response.getOutputStream());
        inputStream.close();
        log.info("Writing image into output stream.");
        System.out.println("Writing image into output stream.");
        System.out.println("SASHI 2");
    } catch (FileNotFoundException filesException) {
        log.error("Image not found in the respective location.",
                filesException);
    } catch (IOException ioException) {
        log.error("Unable to write image.", ioException);
        mav.addObject(ReportConstants.ERROR_MESSAGE,
                "IO Exception while getting report for event.");
        return mav;
    }
    return null;
}
}

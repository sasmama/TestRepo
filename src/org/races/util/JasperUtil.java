package org.races.util;

import java.io.ByteArrayOutputStream; 
import java.io.File;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRCsvExporterParameter;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import org.apache.log4j.Logger; 

import org.races.Constants.ReportConstants;
import org.springframework.beans.factory.annotation.Value;


/**
 * Jasper util class interact with jasper and export the report as CSV, HTML and
 * PDF format.
 * 
 * Creates the export location to aid jasper to place its exported image.
 */
public class JasperUtil {
	
	@Value("${usageTrend.export.location}")
	 private String fileExportLocation;
	
	private static Logger logger = Logger.getLogger(JasperUtil.class);
    public ByteArrayOutputStream jasperPrintToByteArray(
            JasperPrint jasperprint, FormatType formatType) {
        ByteArrayOutputStream byteArrayOutputStream;
        JRHtmlExporter htmlExporter;
        
        switch (formatType) {
        
        case CSV:

            byteArrayOutputStream = new ByteArrayOutputStream();
            JRCsvExporter csvExporter = new JRCsvExporter();
            csvExporter.setParameter(JRCsvExporterParameter.JASPER_PRINT,
                    jasperprint);
            csvExporter.setParameter(JRCsvExporterParameter.OUTPUT_FILE_NAME,
                    ReportConstants.CSV_EXPORTER_PARAM);
            csvExporter.setParameter(JRCsvExporterParameter.CHARACTER_ENCODING,
                    "UTF-8");
            csvExporter.setParameter(JRCsvExporterParameter.OUTPUT_STREAM,
                    byteArrayOutputStream);
            logger.debug("Exporting report as CSV format");
            try {
                csvExporter.exportReport();
            } catch (JRException e) {
                logger.debug("Jasper Exception :" + e);
            }
            logger.debug("Size of stream of csv : "
                    + byteArrayOutputStream.size());
            logger.info("Exit from getting byte array from jasper report.");
            return byteArrayOutputStream;
            
        case HTML:

            byteArrayOutputStream = new ByteArrayOutputStream();
            htmlExporter = new JRHtmlExporter();
            htmlExporter.setParameter(JRExporterParameter.JASPER_PRINT,
                    jasperprint);
            htmlExporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "");
            htmlExporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");
            htmlExporter.setParameter(
                    JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
            htmlExporter.setParameter(
                    JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
                    Boolean.TRUE);
            htmlExporter.setParameter(
                    JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
                    Boolean.FALSE);
            htmlExporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,
                    ReportConstants.IMAGE_URI);
            htmlExporter.setParameter(JRHtmlExporterParameter.OUTPUT_STREAM,
                    byteArrayOutputStream);
            logger.debug("Exporting report as HTML format");
            try {
                htmlExporter.exportReport();
            } catch (JRException e) {
                logger.error("Jasper Exception : " + e);
            }
            logger.debug("Size of HTML : " + byteArrayOutputStream.size());
            logger.info("Exit from getting byte array from jasper report.");
            return byteArrayOutputStream;
            
        case PDF:

            JRPdfExporter pdfExporter = new JRPdfExporter();
            byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT,
                        jasperprint);
                pdfExporter.setParameter(JRExporterParameter.OUTPUT_STREAM,
                        byteArrayOutputStream);
                pdfExporter.exportReport();
                logger.debug("PDF Size of : " + byteArrayOutputStream.size());
            } catch (JRException e) {
                logger.error("Jasper Exception : " + e);
            }
            logger.info("Exit from getting byte array from jasper report.");
            return byteArrayOutputStream;
        default:

            logger.info("Entering DEFAULT option to get bytearray from jasper");
            System.out.println("Entering DEFAULT option to get bytearray from jasper");
            JasperPrint jprint = jasperprint;
            byteArrayOutputStream = new ByteArrayOutputStream();
            htmlExporter = new JRHtmlExporter();
            htmlExporter.setParameter(JRExporterParameter.JASPER_PRINT, jprint);
            htmlExporter.setParameter(JRHtmlExporterParameter.HTML_HEADER, "");
            htmlExporter.setParameter(JRHtmlExporterParameter.HTML_FOOTER, "");
            htmlExporter.setParameter(
                    JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
            htmlExporter.setParameter(
                    JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
                    Boolean.TRUE);
            htmlExporter.setParameter(
                    JRHtmlExporterParameter.IS_OUTPUT_IMAGES_TO_DIR,
                    Boolean.TRUE);
            htmlExporter.setParameter(
                    JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN,
                    Boolean.FALSE);
            htmlExporter.setParameter(JRHtmlExporterParameter.IMAGES_DIR_NAME,
                    getFileExportLocation());
            htmlExporter.setParameter(JRHtmlExporterParameter.IMAGES_URI,
                    ReportConstants.IMAGE_URI);
            htmlExporter.setParameter(JRHtmlExporterParameter.OUTPUT_STREAM,
                    byteArrayOutputStream);
            try {
                htmlExporter.exportReport();
            } catch (JRException jasperException) {
                logger.error("Jasper Exception :" + jasperException);
                System.out.println("Exception in jasperutil :"+jasperException);
            }
            logger.info("Exit from getting byte array from jasper report.");
            return byteArrayOutputStream;  
            
        }
        
    }
    
    /**
     * Creates the export location to aid jasper to place its exported image. A
     * new folder (as prescribed in report.properties) will be created to store
     * the usage trend image temporary. On every request a new chart will be
     * created.
     * 
     * @return file path as export location.
     */
    public String getFileExportLocation() {
        String exportLocation = ReportConstants.USER_HOME + fileExportLocation;
        System.out.println("Export Location : "+exportLocation);
        File exportedFile = new File(exportLocation);
        if (!exportedFile.exists()) {
            boolean isMakeDirectory = exportedFile.mkdir();
            logger.debug("Directory created status: " + isMakeDirectory);
            System.out.println("Directory created status: " + isMakeDirectory);
        }
        return exportLocation;
    }

}

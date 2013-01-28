package org.races.service;

import java.io.ByteArrayOutputStream;

import org.races.model.ReportFilter;
import org.races.model.UserDetails;
import org.races.util.FormatType;

public interface RacesService {

	public String getAuthenticationValue(String userName,String password);
	public String getForgotPassword(String userName);
	public String sendMail(String email_id,String password);
	ByteArrayOutputStream createUsageTrend(FormatType formatType);
	ByteArrayOutputStream getFailureReport(String failureMessage);
 	ByteArrayOutputStream getPendingReport(ReportFilter reportFilter);
	 
}

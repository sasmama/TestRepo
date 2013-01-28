package org.races.model;

import java.util.Date; 
import org.races.util.FormatType;

public class ReportFilter {
	String forCurrentMonth;
	public String getForCurrentMonth() {
		return forCurrentMonth;
	}
	public void setForCurrentMonth(String forCurrentMonth) {
		this.forCurrentMonth = forCurrentMonth;
	}
	Date fromDate;
	Date toDate;
	FormatType formatType;
	public FormatType getFormatType() {
		return formatType;
	}
	public void setFormatType(FormatType formatType) {
		this.formatType = formatType;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

}

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!doctype html>
<html>
<head>
<meta charset="utf8">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<title>Races</title>
<link rel="stylesheet" href="css/style.css" />
<link rel="stylesheet" href="css/styles.css" />
<script src="js/jquery-1.4.2.js"></script>
<link rel="stylesheet" href="css/jquery-ui.css" />
<script src="js/jquery-1.8.3.js"></script>
<script src="js/jquery-ui-1.9.2.js"></script>

<script>
$(function() {
$( "#accordion" ).accordion({
	animated: "bounceslide"
});
});
</script>
<script src="js/login.js"></script>
</head>
<body style="background: #BABABA"
	onload="getSalesReport(),getServiceReport()">
	<div>
		<div id="leftPanel">
			<div>
				<img src="images/images.jpg" width="100%" />
			</div>
			<div id="accordion">
				<h3>Section 1</h3>
				<div>
					<p>Races</p>
				</div>
				<h3>Section 2</h3>
				<div>
					<p></p>
				</div>
				<h3>Section 3</h3>
				<div>
					<p></p>
					<ul>
						<li>List item one</li>
						<li>List item two</li>
						<li>List item three</li>
					</ul>
				</div>
				<h3>Section 4</h3>
				<div></div>
			</div>

		</div>
		<div id="wrap">
			<!--<div id="bar"></div>-->
			<div id="content">


				<div id="mainFull">
					<div id="topBar">
						<div id="titleDiv"
							style="text-align: center; width: 100%; height: 25px; line-height: 45px;">
							<span class="TabHead">Races Report Analytics Dashboard</span>
						</div>
					</div>
					<table width="100%" border="0" cellspacing="2" cellpadding="2">
						<tr>
							<td width="45%">
								<div id="block1"
									style="float: left; width: 100%; height: 405px; padding: 5px;">
									<div class="nobelwinner">
										Sales Report <span
											style="color: #F00; font-size: 10px; font-weight: bold"></span>
									</div>
									<div id="salesReportDiv"
										style="border-radius: 6px 6px 6px 6px; float: left; width: 100%; height: 340px; border: 1px solid #ccc; text-align: center">
									</div>
									<div id="graphDiv"
										style="float: left; width: 100%; height: 33px; text-align: center; padding: 10px;">
										<form name="usageTrend">
											<input id="salesReportSave" name="usageTrendSave"
												type="button" class="" value=" Save Sales Report "
												onclick="saveSalesReport(usageTrend)" />
										</form>
									</div>
								</div>
							</td>

							<td width="52%" style="padding-right: 10px;">
								<div id="block2"
									style="float: left; width: 100%; height: 405px; padding: 5px 5px 5px 5px;">
									<div class="nobelwinner">
										Service Report -
										<c:out value='${currentDate}' />
										<span style="color: #F00; font-size: 10px; font-weight: bold"></span>
									</div>
									<div id="serviceReportDiv"
										style="border-radius: 6px 6px 6px 6px; float: left; width: 100%; height: 340px; overflow-x: auto; overflow-y: auto; border: 1px solid #ccc;">

									</div>
									<div id="graphDiv"
										style="float: left; width: 100%; height: 33px; text-align: center; padding: 10px;">
										<form name="snapshot">
											<input id="serviceReportSave" name="Submit" type="submit"
												value=" Save Service Report "
												onclick="saveServiceReport(snapshot)" />
										</form>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="2">&nbsp;</td>
						</tr>
						<tr>
							<td align="left" valign="middle" style="font-size: 16px;">
								<div id="block3"
									style="border-radius: 6px 0px 6px 6px; float: left; width: 100%; height: 25px; border: 1px solid #ccc; border-right: 0px; padding: 5px; color: #005a84; text-decoration: underline;">
									<div style="width: 100%">
										<a id="researchAreaLink"
											style="padding-left: 35%; text-align: center"
											onclick="setTimeout('showLoadingIcon()',1),disableRunButton()"
											href="getReport.do?forCurrentMonth=<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()) %>"><strong>Report
												Analysis</strong> </a>
									</div>
								</div>
							</td>
							<td style="font-size: 16px;">
								<div style="width: 98%">
									<div id="block3"
										style="border-radius: 6px 6px 6px 0px; float: left; width: 100%; height: 25px; border: 1px solid #ccc; border-left: 0px; padding: 5px; color: #005a84; text-decoration: underline;">
										<a id="researchAreaLink"
											style="padding-left: 42%; text-align: center;"
											onclick="setTimeout('showLoadingIcon()',1),disableRunButton()"
											href="getReport.do"><strong>Data Entry</strong> </a>
									</div>
								</div>

							</td>
						</tr>
					</table>

				</div>
			</div>
			<!--end mainFull -->


			<div id="footer-wrap">
				<div id="footer">
					<address>
						&copy;
						<%= new java.util.Date().getYear() + 1900 %>
						Powered by Races
					</address>
				</div>
				<!-- end footer -->
			</div>
			<!-- end footer-wrap -->


		</div>
		<!-- end content-->
	</div>
	</div>
</body>
</html>
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
		$("#accordion").accordion({
			animated : "bounceslide"
		});
	});
</script>
<script src="js/login.js"></script>
</head>
<body style="background: #BABABA" onload="">
	<div>
		<div id="leftPanel">
			<div>
				<img src="images/images.jpg" width="100%" />
			</div>
			<div id="accordion">
				<h3>Monthly Report</h3>
				<div>
					<p>
						<a>Free Service Report</a> <a>Particular Service Report</a>
					</p>
				</div>
				<h3>Report For Selected Duration</h3>
				<div>
					<p></p>
				</div>
				<h3>Complete Report</h3>
				<div>
					<p></p>
					<ul>
						<li>List item one</li>
						<li>List item two</li>
						<li>List item three</li>
					</ul>
				</div>
				<h3>Report Based on Service Engineer</h3>
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
							<span class="TabHead">Races Report</span>
						</div>
					</div>
					<div>
						<div></div>
						<table width="100%" border="0" cellspacing="10" cellpadding="10">
							<tr>
								<td>
									<div id="reportContentDiv"
										style="border-radius: 6px 6px 6px 6px; float: left; width: 100%; height: 60px; border: 1px solid #ccc; text-align: center">
									</div>
								</td>
							</tr>
							<tr>
								<td width="">
									<div id="reportDiv"
										style="border-radius: 6px 6px 6px 6px; float: left; width: 100%; height: 400px; border: 1px solid #ccc; text-align: center">
										<c:out value="${reportString}" escapeXml="false"></c:out>
									</div>
								</td>
							</tr>
						</table>

					</div>
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
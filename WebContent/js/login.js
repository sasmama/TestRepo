// Login Form
function loginPage(){
$(function() {
    var button = $('#loginButton');
    var box = $('#loginBox');
    var form = $('#loginForm');
    button.removeAttr('href');
    button.mouseup(function(login) {
        box.toggle();
        button.toggleClass('active');
    });
    form.mouseup(function() { 
        return false;
    });
    $(this).mouseup(function(login) {
        if(!($(login.target).parent('#loginButton').length > 0)) {
            button.removeClass('active');
            box.hide();
        }
    });
});
}


/*$(document).ready(function(){
    $('#salesReportImage').width(200);
    $('#salesReportImage').mouseover(function()
    {
       $(this).css("cursor","pointer");
       $(this).animate({width: "500px"}, 'slow');
    });
 
 $('#salesReportImage').mouseout(function()
   {   
       $(this).animate({width: "200px"}, 'slow');
    });
});
*/

function getSalesReport() {
	  $('#salesReportDiv').html('<img width="20" style=margin-top:85px; height="20" alt="" src="images/ajax-loader.gif">');
	  $.get('createSalesReport.do', function(response) {
	      $('#salesReportDiv').html(
	          '<img id="salesReportImage" src="getSalesReportChart.do" alt="" width="100%" height="96%" />');
	  });
}


function getServiceReport() {
	  $('#serviceReportDiv').html('<img width="20" style=margin-top:85px; height="20" alt="" src="images/ajax-loader.gif">');
	  $.get('createServiceReport.do', function(response) {
	      $('#serviceReportDiv').html(
	          '<img id="salesReportImage" src="getServiceReportChart.do" alt="" width="100%" height=96%" />');
	  });
}
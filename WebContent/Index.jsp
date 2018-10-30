<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>匯率DEMO</title>
<link id="themecss" rel="stylesheet" type="text/css"
	href="//www.shieldui.com/shared/components/latest/css/light-bootstrap/all.min.css" />
<script type="text/javascript" src="js/jquery-3.3.1.min.js"></script>
<script type="text/javascript"
	src="//www.shieldui.com/shared/components/latest/js/shieldui-all.min.js"></script>
<script type="text/javascript" charset="UTF-8" src="js/jscal2.js"></script>
<script type="text/javascript" charset="UTF-8" src="js/cn.js"></script>
<link rel="Stylesheet" type="text/css" href="css/jscal2.css" />

<script type="text/javascript">
	var time = new Array();
	var buyrate = new Array();
	var sellrate = new Array();
	$(document).ready(function() {
		$('#bttsubmit').click(function() {
			var countryID = $('#countryID').val();
			$.ajax({
				type : 'POST',
				data : $("#form1").serialize(),
				dataType : "json",
				url : 'FirstServlet',
				async : false,
				cache : false,
				success : function(result) {
					$.each(result, function(idx, obj) {												
						buyrate[idx] = obj.BuyRate;
						sellrate[idx] = obj.SellRate;	
						time[idx] = obj.Time;
					});
				},
				error : function(xhr, ajaxOptions, thrownError) {
					messagePopup(mErrorMessage);
				}
			});

			drawchart();

		});
		
		function drawchart() {
			$("#chart").shieldChart(
					{
						theme : "bootstrap",
						exportOptions : {
							image : false,
							print : false
						},
						axisX : {
							categoricalValues : time,
							ticksRepeat: 30							
						},
						axisY : {
							axisTickText : {
								format : "{text:c}"
							},
							title : {
								text : "即期匯率"
							}
						},
						tooltipSettings : {
							chartBound : true
						},
						seriesSettings : {
							line : {
								enablePointSelection : true,
								pointMark : {
									activeSettings : {
										pointSelectedState : {
											drawWidth : 4,
											drawRadius : 4
										}
									}
								}
							}
						},
						primaryHeader : {
							text : "匯率"
						},
						dataSeries : [
								{
									seriesType : 'line',
									collectionAlias : '賣匯',
									data : sellrate
								},
								{
									seriesType : 'line',
									collectionAlias : '買匯',
									data : buyrate
								}
								 ]
					});
		}
	});
</script>

</head>
<body>
	<form id="form1">
		<p>
			幣別: <select id="countryID" name="countryID" size="1">
			<option value="USD">美元(USD)</option>
			<option value="CNY">人民幣(CNY)</option>
			<option value="AUD">澳幣(AUD)</option>
			<option value="ZAR">南非幣(ZAR)</option>
			<option value="NZD">紐西蘭幣(NZD)</option>
			<option value="EUR">歐元(EUR)</option>
			<option value="HKD">港幣(HKD)</option>
			</select>
		</p>
		查詢日期<input id="startDate" name="startDate" type="text" />&nbsp;<img id='date1' src="https://www.esunbank.com.tw/bank/images/esunbank/deposit/calendar.gif" align="middle"/>
		<script type="text/javascript">
                        Calendar.setup({
                            inputField: "startDate",
                            trigger: "date1",
                            dateFormat: "%Y-%m-%d",
                            onSelect: function() { this.hide(); }
                        });</script>
                        
        ~ <input id="endDate" name="endDate" type="text" />&nbsp;<img id='date2' src="https://www.esunbank.com.tw/bank/images/esunbank/deposit/calendar.gif" align="middle"/>                
        <script type="text/javascript">
                        Calendar.setup({
                            inputField: "endDate",
                            trigger: "date2",
                            dateFormat: "%Y-%m-%d",
                            onSelect: function() { this.hide(); }
                        });</script>                
		<br>	
		<input type="button" value="查詢" id="bttsubmit"> <br>
		
		
			
		
	</form>
	<div id="chart"></div>
</body>
</html>

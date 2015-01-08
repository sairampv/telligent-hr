<table  style="width:100%;"  border="0" cellspacing="1" cellpadding="2" >
   	<tr>
   		<td style="width: 40px"><input type="button" class="button" value="Personal" id="personal" onclick="javascript:showPersonalPage()"></td>
   		<td style="width: 40px"><input type="button" class="button" value="Compensation" id="compensation" onclick="javascript:showCompensationPage()"></td>
   		<td style="width: 40px"><input type="button" class="button" value="Employement" id="employement" onclick="javascript:showEmployementPage()"></td>
   		<td style="width: 40px"><input type="button" class="button" value="Position" id="position" onclick="javascript:showPositionPage()"></td>
   		<td><input type="button" class="button" value="Other Data" id="otherData" onclick="javascript:showOtherDataPage()"></td>
   		<td><span style="float: right"><a href="dashboard.htm" style="text-decoration: none;"><b>Back</b></a></span></td>
   	</tr>
</table>
<script type="text/javascript">
function showPersonalPage(){
	document.forms[0].action="showEmployeeDetails.htm";
	document.forms[0].method = "post";
	document.forms[0].submit();
}
function showCompensationPage(){
	document.forms[0].action="empCompensationPage.htm";
	document.forms[0].method = "post";
	document.forms[0].submit();
}
function showEmployementPage(){
	document.forms[0].action="empEmployementPage.htm";
	document.forms[0].method = "post";
	document.forms[0].submit();
}
function showPositionPage(){
	document.forms[0].action="empPosition.htm";
	document.forms[0].method = "post";
	document.forms[0].submit();
}
function showOtherDataPage(){
	document.forms[0].action="empOtherData.htm";
	document.forms[0].method = "post";
	document.forms[0].submit();
}
</script>
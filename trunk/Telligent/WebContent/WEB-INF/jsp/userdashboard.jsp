<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>User Dashboard</title>
<link href="view/css/style.css" rel="stylesheet" type="text/css" />
<form id="formId">
	<div class="contentArea1">
	<div class="boxesArea">
	<a href="meritAdministration.htm"><div class="boxes">Merit Administration</div></a>
	<a href="javascript:void(0);"><div class="boxes">Performance Pay</div></a>
	<a href="javascript:void(0);"><div class="boxes">Bonus/Payout Administration</div></a>
	<a href="javascript:void(0);"><div class="boxes">Succession</div></a>
	<a href="showTeams.htm"><div class="boxes">Show Teams</div></a>
	<a href="employee.htm"><div class="boxes">Employee</div></a>
	<a href="referenceTables.htm"><div class="boxes">Reference Tables</div></a>
	<a href="#" onclick="javascript:submit('appUser.htm')"><div class="boxes">App User</div></a>
	<a href="#" onclick="javascript:submit('securityGroup.htm')"><div class="boxes">Security Group</div></a>
	</div>
	</div>
</form>
<script>
function submit(url){
	document.forms[0].action=url;
	document.forms[0].method = "post";
	document.forms[0].submit();
}
</script>


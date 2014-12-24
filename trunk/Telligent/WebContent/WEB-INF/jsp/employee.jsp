<%@ include file="taglib.jsp" %>
<title>Dashboard</title>
<link href="view/css/style.css" rel="stylesheet" type="text/css" />
<link href="view/css/colorbox.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="view/css/popup.css">
<link rel="stylesheet" type="text/css" href="view/css/jquery/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="view/css/jquery/themes/icon.css">
<link rel="stylesheet" type="text/css" href="view/css/jquery/demo.css">
<script type="text/javascript" src="view/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="view/js/app/telligentCommon.js"></script>

<div id="contentArea">
<div class="loader"></div>
<div class="contentArea">
	<div class="innerleft"  id="col1">
    	<table class="leftAccordion" cellspacing="1" cellpadding="5" id="employeeSearchTable">
    		<tr>
		        <th colspan="2" class="head" height="25" align="left">Search</th>
		    </tr>
		    <tr>
		    	<td>Last Name</td>
		    	<td>
		    		<input class="easyui-combobox" style="width:200px" data-options="
						url:'serachLastName.htm',
						method:'post',
						mode: 'remote',
						valueField: 'id',
						textField: 'value',
						selectOnNavigation:false,
						onSelect:getEmployeeDetails
						">
		    	</td>
		    </tr>
		    <tr>
		    	<td>First Name</td>
		    	<td>
		    		<input class="easyui-combobox" style="width:200px" data-options="
						url:'serachFirstName.htm',
						method:'post',
						mode: 'remote',
						valueField: 'id',
						textField: 'value',
						selectOnNavigation:false,
						onSelect:getEmployeeDetails
						">
		    	</td>
		    </tr>
		    <tr>
		    	<td>Employee Id</td>
		    	<td>
		    		<input class="easyui-combobox" style="width:200px" data-options="
						url:'serachEmpId.htm',
						method:'post',
						mode: 'remote',
						valueField: 'id',
						textField: 'value',
						selectOnNavigation:false,
						onSelect:getEmployeeDetails
						">
		    	</td>
		    </tr>
		    <tr>
		    	<td>Team Name</td>
		    	<td>
		    		<input class="easyui-combobox" style="width:200px" data-options="
						url:'serachTeamEmployees.htm',
						method:'post',
						mode: 'remote',
						valueField: 'id',
						textField: 'value',
						selectOnNavigation:false,
						onSelect:getEmployeeDetails
						">
		    	</td>
		    </tr>
    	</table>  
	</div>
 	<div class="innerright"  id="flow">
	 	<a href="javascript:toggle()" title="Hide Nav 13" id="flowtab"></a>
	 	<div style="margin:0px;cursor:auto;" id="tab">
	    <div class="wrap">  
		    <table width="100%" border="0" cellspacing="1" cellpadding="5" bgcolor="E3E3E3" align="right">
		      <tr>
		        <th class="head1" height="25" align="left">
		        <div class="innerpage-breadcrum">
					<a href="dashboard.htm">Dashboard</a> &nbsp;&gt;&nbsp; 
					<a href="javascript:void(0);">Employee</a> &nbsp;&gt;&nbsp; 
					<a href="javascript:void(0);" class="select">Employee Details</a>
					<span style="float: right"><a href="dashboard.htm">Back</a></span>
				</div>
				</th>
		      </tr>
		    </table>
	  </div>
	  </div>
  </div>
  </div>
</div> 
<script type="text/javascript" src="view/js/docknavigation.js"></script>
<script type="text/javascript">
	function getEmployeeDetails(rec){
		alert("you have selected "+rec.id);
	}
</script>  
<!-- </div> -->

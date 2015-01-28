<%@ include file="taglib.jsp" %>
<title>Dashboard</title>
<link href="view/css/colorbox.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="view/css/popup.css">
<link rel="stylesheet" type="text/css" href="view/css/jquery/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="view/css/jquery/themes/icon.css">
<link rel="stylesheet" type="text/css" href="view/css/jquery/demo.css">
<script type="text/javascript" src="view/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="view/js/jquery/jquery.usphone.js"></script>
<script type="text/javascript" src="view/js/app/telligentCommon.js"></script>
<script type="text/javascript" src="view/js/popup.js"></script>
<form:form commandName="employeePosition" id="employeePosition" modelAttribute="employeePosition" enctype="multipart/form-data">
<form:hidden path="successMessage"/>
<form:hidden path="errorMessage"/>
<form:hidden path="operation"/>
<input type="hidden" id="updateble">
<div id="contentArea">
<div class="loader"></div>
<div class="contentArea">
	<%@include file="employeeLeft.jsp" %>
 	<div class="innerright" style="min-height: 480px" id="flow">
	 	<a href="javascript:toggle()" title="Hide Nav 13" id="flowtab"></a>
	 	<div style="margin:0px;cursor:auto;" id="tab">
	    <div class="wrap">
	    <table width="100%" border="0" cellspacing="1" cellpadding="5" bgcolor="E3E3E3" align="right">
		      <tr>
		        <th class="head1" height="25" align="left">
		        <div class="innerpage-breadcrum">
					<a href="dashboard.htm">Dashboard</a> &nbsp;&gt;&nbsp; 
					<a href="javascript:void(0);">Employee</a> &nbsp;&gt;&nbsp; 
					<a href="javascript:void(0);" class="select">Employee Position Details</a>
					<span style="float: right"><a href="dashboard.htm">Back</a></span>
				</div>
				</th>
		      </tr>
		    </table>
    	<%@include file="employeeButtons.jsp" %>  
		<div>
		<table   width="100%" border="0" cellspacing="1" cellpadding="5" class="data-table-new">
			<tr>
				<th colspan="8" style="text-align: left;padding-left: 10px" class="head">
		    			Employee Position
		    	</th>
			</tr>
			<tr>
	    		<td colspan="8">
	    			<a href="#" onclick="javascript:save()" class="easyui-linkbutton" iconCls="icon-save">Save</a>
	    			<span id="empEffectiveDt" style="float: right; color: orange; padding-right: 20px"> </span>
	    		</td>
	    	</tr>
			<tr>
				<td><label>Employee</label></td>
				<td><form:input path="employeeId" readonly="true" cssClass="required" placeholder="Employee Id"/></td>
				<td><form:input path="lastName" readonly="true" placeholder="Last Name"/></td>
				<td><form:input path="middleName" readonly="true" placeholder="Middle Name"/></td>
				<td><form:input path="firstName" readonly="true" placeholder="First Name"/></td>
				<td><label>Effective Date</label></td>
				<td>
					<form:hidden path="effectiveDate"/>
		    		<input id="effectiveDateBox" class="required easyui-datebox" required="true" width="150px"/>
		    	</td>
			</tr>
			<tr>
				<th colspan="8" style="text-align: left;padding-left: 10px" class="head">
		    	</th>
			</tr>
		</table>   
		<table   width="100%" border="0" cellspacing="1" cellpadding="5" class="data-table-new">
			<tr>
				<td style="width: 12%"><label>Status Code</label></td>
				<td style="width: 15%">
					<form:select path="statusCode" >
	    				<form:option value="">Select</form:option>
	    				<form:options items="${statusCodeList}" itemValue="id" itemLabel="value"/>
	    			</form:select>
				</td>
				<td style="width: 14%"><label>Status Reason</label></td>
				<td style="width: 30%">
					<form:select path="statusReason">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${statusReasonList}" itemValue="id" itemLabel="value"/>
	    			</form:select>
		    	</td>
			</tr>
			<tr>
				<th colspan="8" style="text-align: left;padding-left: 10px" class="head">
		    	</th>
			</tr>
		</table>
		<table   width="100%" border="0" cellspacing="0" cellpadding="2" class="data-table-new">
			<tr>
				<td style="width: 15%"><label>Supervisor</label></td>
				<td style="width: 15%">
	    			<form:select path="supervisor">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${supervisorList}" itemValue="id" itemLabel="value"/>
	    			</form:select>
				</td>
				<td style="width: 15%"><label>Org1</label> </td>
				<td style="width: 21%">
	    			<form:select path="org1">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${org1List}" itemValue="id" itemLabel="value"/>
	    			</form:select>
		    	</td>
		    	<td style="width: 15%"><label>Org6</label></td>
				<td>	
	    			<form:select path="org6">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${org6List}" itemValue="id" itemLabel="value"/>
	    			</form:select>
		    	</td>
			</tr>
			<tr>
				<td style="width: 15%"><label>Team</label></td>
				<td style="width: 15%">
	    			<form:select path="team">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${teamList}" itemValue="id" itemLabel="value"/>
	    			</form:select>
				</td>
				<td style="width: 15%"><label>Org2</label> </td>
				<td style="width: 21%">
	    			<form:select path="Org2">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${org2List}" itemValue="id" itemLabel="value"/>
	    			</form:select>
		    	</td>
		    	<td style="width: 15%"><label>Org7</label></td>
				<td>	
	    			<form:select path="org7">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${org7List}" itemValue="id" itemLabel="value"/>
	    			</form:select>
		    	</td>
			</tr>
			
			<tr>
				<td style="width: 15%"><label>Position</label></td>
				<td style="width: 15%">
	    			<form:select path="position" id="position1">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${positionList}" itemValue="id" itemLabel="value"/>
	    			</form:select>
				</td>
				<td style="width: 15%"><label>Org3</label> </td>
				<td style="width: 21%">
	    			<form:select path="org3">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${org3List}" itemValue="id" itemLabel="value"/>
	    			</form:select>
		    	</td>
		    	<td style="width: 15%"><label>Org8</label></td>
				<td>	
	    			<form:select path="org8">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${org8List}" itemValue="id" itemLabel="value"/>
	    			</form:select>
		    	</td>
			</tr>
			
			<tr>
				<td style="width: 15%"><label>Position Level</label></td>
				<td style="width: 15%">
	    			<form:select path="positionLevel">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${positionLevelList}" itemValue="id" itemLabel="value"/>
	    			</form:select>
				</td>
				<td style="width: 15%"><label>Org4</label> </td>
				<td style="width: 21%">
	    			<form:select path="org4">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${org4List}" itemValue="id" itemLabel="value"/>
	    			</form:select>
		    	</td>
		    	<td style="width: 15%"><label>Org9</label></td>
				<td>	
	    			<form:select path="org9">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${org9List}" itemValue="id" itemLabel="value"/>
	    			</form:select>
		    	</td>
			</tr>
			
			<tr>
				<td style="width: 15%"><label>Primary Job</label></td>
				<td style="width: 15%">
	    			<form:select path="primaryJob">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${primaryJobList}" itemValue="id" itemLabel="value"/>
	    			</form:select>
				</td>
				<td style="width: 15%"><label>Org5</label> </td>
				<td style="width: 21%">
	    			<form:select path="org5">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${org5List}" itemValue="id" itemLabel="value"/>
	    			</form:select>
		    	</td>
		    	<td style="width: 15%"><label>Org10</label></td>
				<td>	
	    			<form:select path="org10">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${org10List}" itemValue="id" itemLabel="value"/>
	    			</form:select>
		    	</td>
			</tr>
			
			<tr>
				<td style="width: 15%"><label>Primary Job Leave</label></td>
				<td style="width: 15%" colspan="6">
	    			<form:select path="primaryJobLeave">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${primaryJobLeaveList}" itemValue="id" itemLabel="value"/>
	    			</form:select>
				</td>
			</tr>
			<tr>
				<td style="width: 15%"><label>Union Code</label></td>
				<td style="width: 15%" colspan="6">
	    			<form:select path="unionCode">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${unionCodeList}" itemValue="id" itemLabel="value"/>
	    			</form:select>
				</td>
			</tr>
			
			<tr>
				<th colspan="6" style="text-align: left;padding-left: 10px" class="head">
		    	</th>
			</tr>
		</table>
		<table id="employeePositionHistoryTable"  class="easyui-datagrid" title="History Table"  style="width:100%;height:170px;table-layout: fixed;"
						data-options="collapsible:true
										,method: 'post'
										,pagination:false
										,emptyMsg: 'No records found'
										,collapsed:false " >
						<thead data-options="frozen:true">
							<tr>
								<th data-options="field:'effectiveDate',width:100"  formatter="formatDetail">Eff Date</th>
								<th data-options="field:'seqNo',width:100" hidden="true">seq no</th>
							</tr>
						</thead>
						<thead>
							<tr>
								<th data-options="field:'statusCode',width:100">Status Code</th>
								<th data-options="field:'statusReason',width:100">StatusReason</th>
								<th data-options="field:'supervisor',width:100">Supervisor</th>
								<th data-options="field:'team',width:100">Team</th>
								<th data-options="field:'position',width:100">Position</th>
								<th data-options="field:'positionLevel',width:100">Position Level</th>
								<th data-options="field:'primaryJob',width:100">Primary Job</th>
								<th data-options="field:'primaryJobLeave',width:100">Primary Job Level</th>
								<th data-options="field:'unionCode',width:100">Union Code</th>
								<th data-options="field:'updatedDate',width:100">Updated Date</th>
								<th data-options="field:'updatedBy',width:100">Updated By</th>
							</tr>
						</thead>
			</table> 
		</div>
		</div>
	</div>
  </div>
</div>
</div> 
</form:form>
<script type="text/javascript" src="view/js/docknavigation.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	highLightTab();
	$("#positionButton").attr('class', 'buttonSelect');
	var empId = document.getElementById("employeeId").value;
	if(empId!=null && empId !="")
		getEmployeePositionDetails(empId);
});
function searchLastNameSelect(rec){
	$('#firstNameInputId').combobox('clear');
	$('#employeeInpuId').combobox('clear');
	$('#teamInputId').combobox('clear');
	getEmployeeDetails(rec);
}
function searchFirstNameSelect(rec){
	$('#lastNameInputId').combobox('clear');
	$('#employeeInpuId').combobox('clear');
	$('#teamInputId').combobox('clear');
	getEmployeeDetails(rec);
}
function searchEmpIdSelect(rec){
	$('#lastNameInputId').combobox('clear');
	$('#firstNameInputId').combobox('clear');
	$('#teamInputId').combobox('clear');
	getEmployeeDetails(rec);
}
function searchTeamEmployeesSelect(rec){
	$('#lastNameInputId').combobox('clear');
	$('#firstNameInputId').combobox('clear');
	$('#employeeInpuId').combobox('clear');
	getEmployeeDetails(rec);
}
function getEmployeeDetails(rec){
	loading();
	// need to change from ajax to submit
	$.ajax({
		url:"getEmployeeDetails.htm?empId="+rec.id,
		type: "post",
		dataType: 'json',
		error: function(obj){
			closeloading();
			alert(obj);
		},
		success: function(obj){
			document.getElementById("updateble").value="yes";
			$.each(obj, function(i, item){
		  		if(i=='firstName' ||i=='lastName' ||i=='middleName' ||i=='employeeId'){
			  		$("#"+i).val(item);   				  		
			  	}			  		
			});
			getEmployeePositionDetails(rec.id);
		}});
}
function getEmployeePositionDetails(rec){
	loading();
	// need to change from ajax to submit
	$.ajax({
		url:"getEmployeePositionDetails.htm?empId="+rec,
		type: "post",
		dataType: 'json',
		error: function(obj){
			closeloading();
			alert(obj);
		},
		success: function(obj){
			document.getElementById("updateble").value="yes";
			setEmpPositionDetails(obj);
			if(rec!=null)
				empHistory(rec);
			closeloading();
		}});
}
function empHistory(empId){
	$('#employeePositionHistoryTable').datagrid('options').loadMsg = 'Processing, please wait .... ';  // change to Position message
	$('#employeePositionHistoryTable').datagrid('loading');  // 
	$.ajax({
		url:"getEmployeePositionDetailsHistory.htm?empId="+empId,
		type: "post",
		dataType: 'json',
		error: function(obj){
			$('#employeePositionHistoryTable').datagrid('loaded');  // hide loading message
		},
		success: function(obj){
			$('#employeePositionHistoryTable').datagrid('loadData',obj); 
			$('#employeePositionHistoryTable').datagrid('loaded');  // hide loading message
		}});
}
function formatDetail(value,row,index){
	return '<a href="#" onclick="javascript:getEmployeePositionDetailsFromHistoryAjax('+row.seqNo+')">'+value+'</a>';
}
function getEmployeePositionDetailsFromHistoryAjax(id){
	loading();
	$.ajax({
		url:"getEmployeePositionDetailsFromHistoryAjax.htm?seqNo="+id,
		type: "post",
		dataType: 'json',
		error: function(obj){
			closeloading();
			alert(obj);
		},
		success: function(obj){
			document.getElementById("updateble").value="no";
			setEmpPositionDetails(obj);
			closeloading();
		}});
}
function setEmpPositionDetails(obj){
	$.each(obj, function(i, item){
	  	if(i=='effectiveDate'){
  			//document.getElementById(i).value=effectiveDate;
  			$('#effectiveDateBox').datebox('setValue', item);
  		}else if($("#"+i) != undefined)
			$("#"+i).val(item);  
	});
	var effDate = $('#effectiveDateBox').datebox('getValue');
	document.getElementById('empEffectiveDt').innerHTML= 'Effective Date &nbsp;&nbsp;&nbsp;&nbsp;   ' +effDate;
	$('#effectiveDateBox').datebox('setValue', '');
}
function save(){
	if(document.getElementById("updateble").value == "no"){
		alert("You cannot update the History record.");
		return false;
	}
	var effectiveDate = $('#effectiveDateBox').datebox('getValue');
	if($('#employeePosition').valid() && effectiveDate!=''){
		document.getElementById("effectiveDate").value=effectiveDate;
		loading();
		$.ajax({
			url:"saveEmployeePosition.htm",
			type: "post",
			data : $("#employeePosition").serialize(),
			error: function(obj){
				alert(obj);
				closeloading();
			},
			success: function(obj){
				if(obj == "Details Saved Succuessfully"){
					alert("Details saved successfully");
					empHistory(document.getElementById("employeeId").value);
				}else{
					var str = obj.split(":;");
					alert(str[1]);
				}
				closeloading();
			}});
	}
}
</script>

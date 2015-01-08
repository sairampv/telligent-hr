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
<form:form commandName="employeeOther" id="employeeOther" modelAttribute="employeeOther" enctype="multipart/form-data">
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
    	<%@include file="employeeButtons.jsp" %>  
		<div style="background-color: #F5F6F7">
		<table   width="100%" border="0" cellspacing="1" cellpadding="5" class="empPageTable">
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
				<td><form:input path="employeeId" readonly="true"/></td>
				<td><form:input path="lastName" readonly="true"/></td>
				<td><form:input path="middleName" readonly="true"/></td>
				<td><form:input path="firstName" readonly="true"/></td>
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
		<table   width="100%" border="0" cellspacing="0" cellpadding="2" class="empPageTable">
			<tr>
				<td style="width: 15%"><label>Gender</label><span style="color:red">*</span></td>
				<td style="width: 15%">
					<form:select path="gender" cssClass="required">
	    				<form:option value="">Select</form:option>
	    				<form:option value="male">Male</form:option>
	    				<form:option value="female">Female</form:option>
	    			</form:select>
				</td>
				<td style="width: 15%"><label>CitizenShip</label> </td>
				<td style="width: 21%">
					<form:input path="citizenShip"/>
		    	</td>
		    	<td style="width: 15%"><label>Veteran Status</label></td>
				<td>	
					<form:select path="veteranStatus">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${veteranList}" itemValue="id" itemLabel="value"/>
	    			</form:select>
		    	</td>
			</tr>
			<tr>
				<td style="width: 15%"><label>Ethinicity</label></td>
				<td style="width: 15%">
					<form:select path="ethinicity">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${ethinicityList}" itemValue="id" itemLabel="value"/>
	    			</form:select>
				</td>
				<td style="width: 15%"><label>Visa Type</label> </td>
				<td style="width: 21%" colspan="4">
					<form:select path="visaType">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${visaTypeList}" itemValue="id" itemLabel="value"/>
	    			</form:select>
		    	</td>
			</tr>
			
			<tr>
				<td style="width: 15%"><label>Marital Status</label></td>
				<td style="width: 15%">
					<form:select path="maritalStatus">
	    				<form:option value="">Select</form:option>
	    				<form:options items="${maritalList}" itemValue="id" itemLabel="value"/>
	    			</form:select>
				</td>
				<td style="width: 15%"><label>i9 Exp Date</label> </td>
				<td style="width: 21%" colspan="4">
					<form:hidden path="i9ExpDate"/>
					<input id="i9ExpDateBox" class="easyui-datebox" width="150px"/>
		    	</td>
			</tr>
			
			<tr>
				<td style="width: 15%"><label>Disabled</label></td>
				<td style="width: 15%">
					<form:select path="disability" onchange="disabilityChange(this.value)">
	    				<form:option value="">Select</form:option>
	    				<form:option value="yes">Yes</form:option>
	    				<form:option value="no">No</form:option>
	    			</form:select>
				</td>
				<td style="width: 15%" id="disabilityId"><label>Disability Description</label> </td>
				<td style="width: 21%" colspan="4">
					<form:input path="disabilityDesc" cssClass="required" />
		    	</td>
			</tr>
		</table>
		
		
		<table   width="100%" border="0" cellspacing="0" cellpadding="2" class="empPageTable">
			<tr>
		    	<th colspan="8" style="text-align: left;padding-left: 10px" class="head">
		    		Emergency Information
		    	</th>
		    </tr>
			<tr>
				<td style="width: 15%"><label>Last Name</label></td>
				<td style="width: 15%">
					<form:input path="emergencyLastName" maxlength="25"/>
				</td>
				<td style="width: 15%"><label>First Name</label> </td>
				<td style="width: 21%">
					<form:input path="emergencyFirstName" maxlength="25"/>
		    	</td>
		    	<td style="width: 15%"><label>Relation</label></td>
				<td>	
					<form:input path="emergencyRelationShip" maxlength="25"/>
		    	</td>
			</tr>
			<tr>
				<td style="width: 15%"><label>Email</label></td>
				<td style="width: 15%">
					<form:input path="emergencyEmail"/>
				</td>
				<td style="width: 15%"><label>Home Phone</label> </td>
				<td style="width: 21%">
					<form:input path="emergencyHomePhone"/>
		    	</td>
		    	<td style="width: 15%"><label>Mobile Phone</label></td>
				<td>	
					<form:input path="emergencyMobilePhone"/>
		    	</td>
			</tr>
			<tr>
				<th colspan="6" style="text-align: left;padding-left: 10px" class="head">
		    	</th>
			</tr>
		</table>
		
		
		
		<!-- <table id="employeePersonalHistoryTable"  class="easyui-datagrid" title="History Table"  style="width:100%;height:170px;table-layout: fixed;"
						data-options="collapsible:true
										,method: 'post'
										,pagination:false
										,emptyMsg: 'No records found'
										,collapsed:false " >
						<thead data-options="frozen:true">
							<tr>
								<th data-options="field:'effectiveDate',width:100"  formatter="formatDetail">Eff Date</th>
								<th data-options="field:'employeeId',width:100">Emp Id</th>
								<th data-options="field:'seqNo',width:100" hidden="true">seq no</th>
							</tr>
						</thead>
						<thead>
							<tr>
								<th data-options="field:'badgeNo',width:100">Badge</th>
								<th data-options="field:'socialSecNo',width:100">Social Sec No</th>
								<th data-options="field:'firstName',width:100">First Name</th>
								<th data-options="field:'middleName',width:100">Middle Name</th>
								<th data-options="field:'lastName',width:100">Last Name</th>
								<th data-options="field:'homePhone',width:100">Home Phone</th>
								<th data-options="field:'mobilePhone',width:100">Mobile Phone</th>
								<th data-options="field:'addressLine1',width:100">Address 1</th>
								<th data-options="field:'addressLine2',width:100">Address 2</th>
								<th data-options="field:'city',width:100">City</th>
								<th data-options="field:'state',width:100">State</th>
								<th data-options="field:'zipcode',width:100">ZIP</th>
								<th data-options="field:'personalEmail',width:100">Perosonal Email</th>
								<th data-options="field:'dateOfBirth',width:100">DOB</th>
								<th data-options="field:'minor',width:100">Minor</th>
								<th data-options="field:'workPhone',width:100">Work Phone</th>
								<th data-options="field:'workMobilePhone',width:100">Work Mobile Phone</th>
								<th data-options="field:'workEmail',width:100">Work Email</th>
								<th data-options="field:'updatedDate',width:100">Updated Date</th>
								<th data-options="field:'updatedBy',width:100">Updated By</th>
							</tr>
						</thead>
			</table> -->   
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
	$("#otherData").attr('class', 'buttonSelect');
	$("#disabilityId").toggle(false);
	$("#disabilityDesc").toggle(false);
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
			closeloading();
		}});
}
function save(){
	if(document.getElementById("updateble").value == "no"){
		alert("You cannot update the History record.");
		return false;
	}
	var effectiveDate = $('#effectiveDateBox').datebox('getValue');
	var i9ExpDate = $('#i9ExpDateBox').datebox('getValue');
	if($('#employeeOther').valid() && effectiveDate!=''){
		document.getElementById("effectiveDate").value=effectiveDate;
		document.getElementById("i9ExpDate").value=i9ExpDate;
		loading();
		$.ajax({
			url:"saveEmployeeOtherDetails.htm",
			type: "post",
			data : $("#employeeOther").serialize(),
			error: function(obj){
				alert(obj);
				closeloading();
			},
			success: function(obj){
				alert(obj);
				closeloading();
			}});
	}
}
function disabilityChange(val){
	if(val == 'yes'){
		$("#disabilityId").toggle(true);
		$("#disabilityDesc").toggle(true);
	}else{
		$("#disabilityId").toggle(false);
		$("#disabilityDesc").toggle(false);
	}
}
</script>

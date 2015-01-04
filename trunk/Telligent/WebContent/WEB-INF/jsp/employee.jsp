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
<form:form commandName="employee" id="employeeForm" modelAttribute="employee" enctype="multipart/form-data">
<form:hidden path="successMessage"/>
<form:hidden path="errorMessage"/>
<form:hidden path="operation"/>
<div id="contentArea">
<div class="loader"></div>
<div class="contentArea">
	<%@include file="employeeLeft.jsp" %>
 	<div class="innerright" style="min-height: 480px" id="flow">
	 	<a href="javascript:toggle()" title="Hide Nav 13" id="flowtab"></a>
	 	<div style="margin:0px;cursor:auto;" id="tab">
	    <div class="wrap">
	    	<%@include file="employeeButtons.jsp" %>  
		    <table width="100%" border="0" cellspacing="1" cellpadding="5" bgcolor="E3E3E3" align="right">
		      <tr>
		        <th class="head1" height="25" align="left">
		        <div class="innerpage-breadcrum">
					<a href="dashboard.htm">Dashboard</a> &nbsp;&gt;&nbsp; 
					<a href="javascript:void(0);">Employee</a> &nbsp;&gt;&nbsp; 
					<a href="javascript:void(0);" class="select">Employee Personal Details</a>
					<span style="float: right"><a href="dashboard.htm">Back</a></span>
				</div>
				</th>
		      </tr>
		    </table>
		    <div style="padding-top:40px;background-color: #F5F6F7">
		    <table  width="100%" border="0" cellspacing="1" cellpadding="5" id="employeeInformationTable" class="data-table">
		    	<!-- <tr>
		    		<th colspan="6" style="text-align: left;padding-left: 10px" class="head">
		    			Employee Information
		    		</th>
		    	</tr> -->
		    	<tr>
		    		<td colspan="8">
		    			<a href="#" onclick="javascript:reset()" class="easyui-linkbutton" iconCls="icon-add">Create Employee</a>
		    			<a href="#" onclick="javascript:save()" class="easyui-linkbutton" iconCls="icon-save">Save</a>
		    		</td>
		    	</tr>
		    	<tr>
		    		<td style="width: 10%;" nowrap="nowrap">Employee No.</td>
		    		<td style="width: 15%"><form:input path="employeeNo" title="Auto Generated Employee No" readonly="true"  placeholder="Auto Generated No"/></td>
		    		<td style="width: 10%" nowrap="nowrap">Employee Id <span style="color: red">*</span></td>
		    		<td style="width: 15%"><form:input path="employeeId" cssClass="required"  maxlength="14"/></td>
		    		<td style="width: 10%" nowrap="nowrap">Badge</td>
		    		<td style="width: 15%"><form:input path="badgeNo" maxlength="6"/></td>
		    		<td style="width: 15%" rowspan="3" nowrap="nowrap">Picture</td>
		    		<td rowspan="3">
		    			<img id="image" src="view/images/no_image.jpg" height="100" width="150"/><br />
						<input id="picture" type="file" name="picture" onchange="PreviewImage();" />
		    		</td>
		    	</tr>
		    	<tr>
		    		<td nowrap="nowrap">First Name <span style="color: red">*</span></td>
		    		<td><form:input path="firstName" cssClass="required" maxlength="25"/></td>
		    		<td nowrap="nowrap">Middle Name <span style="color: red">*</span></td>
		    		<td><form:input path="middleName" cssClass="required" maxlength="25"/></td>
		    		<td nowrap="nowrap">Last Name <span style="color: red">*</span></td>
		    		<td><form:input path="lastName" cssClass="required" maxlength="25"/></td>
		    		
		    	</tr>
		    	<tr>
		    		<td nowrap="nowrap">Home Phone</td>
		    		<td><form:input path="homePhone"/></td>
		    		<td nowrap="nowrap">Mobile Phone</td>
		    		<td><form:input path="mobilePhone"/></td>
		    		<td nowrap="nowrap">Address 1 <span style="color: red">*</span></td>
		    		<td><form:input path="addressLine1" cssClass="required" maxlength="65"/></td>
		    	</tr>
		    	<tr>
		    		<td nowrap="nowrap">Address 2 <span style="color: red">*</span></td>
		    		<td><form:input path="addressLine2" cssClass="required" maxlength="65"/></td>
		    		<td nowrap="nowrap">City <span style="color: red">*</span></td>
		    		<td><form:input path="city" cssClass="required" maxlength="65"/></td>
		    		<td nowrap="nowrap">State <span style="color: red">*</span></td>
		    		<td><form:input path="state" cssClass="required" maxlength="56"/></td>
		    		<td nowrap="nowrap">ZIP <span style="color: red">*</span></td>
		    		<td><form:input path="zipcode" cssClass="required" maxlength="15"/></td>
		    	</tr>
		    	<tr>
		    		<td nowrap="nowrap">Personal Email</td>
		    		<td><form:input path="personalEmail" maxlength="65"/></td>
		    		<td style="width: 10%" nowrap="nowrap">Effective Date <span style="color: red">*</span></td>
		    		<td style="width: 10%">
		    			<form:hidden path="effectiveDate"/>
		    			<input id="effectiveDateBox" class="required easyui-datebox" required="true" width="150px"/>
		    		</td>
		    		<td nowrap="nowrap">Date Of Birth <span style="color: red">*</span></td>
		    		<td>
		    			<form:hidden path="dateOfBirth"/>
		    			<input id="dateOfBirthBox" class="required easyui-datebox" required="true" onkeypress="return false;" data-options="onSelect:dateOfBirthSelect" width="150px"/>
		    		</td>
		    		<td nowrap="nowrap">Is Minor</td>
		    		<td><form:checkbox path="minor" id="minor"  onclick="return false" title="Depends on Data of Birth Selection"/></td>
		    	</tr>
		    	<tr>
		    		<td nowrap="nowrap">Work Phone</td>
		    		<td><form:input path="workPhone"/></td>
		    		<td nowrap="nowrap">Work Mobile Phone</td>
		    		<td><form:input path="workMobilePhone"/></td>
		    		<td nowrap="nowrap">Work Email</td>
		    		<td colspan="4"><form:input path="workEmail" maxlength="65"/></td>
		    	</tr>
		    	<tr>
		    		<th colspan="8" style="text-align: left;padding-left: 10px" class="head">
		    			Emergency Information
		    		</th>
		    	</tr>
		    	<tr>
		    		<td nowrap="nowrap">Last Name</td>
		    		<td><form:input path="emergencyLastName" maxlength="25"/></td>
		    		<td nowrap="nowrap">First Name</td>
		    		<td><form:input path="emergencyFirstName" maxlength="25"/></td>
		    		<td nowrap="nowrap">Relation</td>
		    		<td><form:input path="emergencyRelationShip" maxlength="25"/></td>
		    		<td nowrap="nowrap">Email</td>
		    		<td><form:input path="emergencyEmail" maxlength="25"/></td>
		    	</tr>
		    	<tr>
		    		<td nowrap="nowrap">Home Phone</td>
		    		<td><form:input path="emergencyHomePhone"/></td>
		    		<td nowrap="nowrap">Mobile Phone</td>
		    		<td><form:input path="emergencyMobilePhone"/></td>
		    		<td nowrap="nowrap">&nbsp;</td>
		    		<td>&nbsp;</td>
		    		<td nowrap="nowrap">&nbsp;</td>
		    		<td>&nbsp;</td>
		    	</tr>
		    </table>
			<table id="employeePersonalHistoryTable"  class="easyui-datagrid" title="History Table"  style="width:100%;height:170px;table-layout: fixed;"
						data-options="collapsible:true
										,method: 'post'
										,pagination:false
										,emptyMsg: 'No records found'
										,collapsed:false " >
						<thead data-options="frozen:true">
							<tr>
								<th data-options="field:'employeeId',width:100" >Emp No</th>
								<th data-options="field:'employeeNo',width:100" >Emp Id</th>
							</tr>
						</thead>
						<thead>
							<tr>
								<th data-options="field:'badgeNo',width:100">Badge</th>
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
								<th data-options="field:'effectiveDate',width:100">Eff Date</th>
								<th data-options="field:'dateOfBirth',width:100">DOB</th>
								<th data-options="field:'minor',width:100">Minor</th>
								<th data-options="field:'workPhone',width:100">Work Phone</th>
								<th data-options="field:'workMobilePhone',width:100">Work Mobile Phone</th>
								<th data-options="field:'workEmail',width:100">Work Email</th>
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
<script type="text/javascript" src="view/js/app/employee.js"></script>


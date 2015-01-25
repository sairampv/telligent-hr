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
<form:form commandName="appUser" id="appUser" modelAttribute="appUser" enctype="multipart/form-data">
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
		<div>
			<table width="100%" border="0" cellspacing="1" cellpadding="5" class="data-table-new" align="right">
		      	<tr>
			     <th colspan="6" style="text-align: left;padding-left: 10px" class="head">
	    			Application User Record
	    		 </th>
		      	<tr>
	    			<td colspan="8">
	    				<a href="#" onclick="javascript:reset()" class="easyui-linkbutton" iconCls="icon-add">Add App User</a>
	    				<a href="#" onclick="javascript:save()" class="easyui-linkbutton" iconCls="icon-save">Save</a>
	    			</td>
	    	  	</tr>
	    		<tr>
	    			<td style="width:10%" nowrap="nowrap"><label>User Id</label><span class="mandatory">*</span></td>
	    			<td><form:input path="userId" cssClass="required"/> </td>
	    			<td style="width:10%" nowrap="nowrap"><label>Password</label><span class="mandatory">*</span></td>
	    			<td><form:password path="password" cssClass="required"/></td>
	    			<td></td>
	    			<td><input type="button" class="buttonSelect" value="Reset Password" id="resetPassword"></td>
	    		</tr>
	    		<tr>
	    			<td nowrap="nowrap"><label>Employee Id</label><span class="mandatory">*</span></td>
	    			<td>
	    				<form:hidden path="employeeId"/>
	    				<input class="easyui-combobox" id="lastNameInputId" style="width:200px" data-options="
							url:'searchEmpId.htm',
							method:'post',
							mode: 'remote',
							valueField: 'id',
							textField: 'value',
							selectOnNavigation:false,
							onSelect:selectEmpId
							">
	    			</td>
	    			<td colspan="6" rowspan="16" valign="top">
	    				<table class="data-table-appUser">
	    					<tr  class="head" height="20px">
	    						<th>Team</th>
	    						<th>Merit Approval Level</th>
	    						<th>Effective Date</th>
	    						<th>End Date</th>
	    					</tr>
	    					<c:forEach items="${appUser.appUserList}" var="typeListitem" varStatus="i">
	    						<tr>
	    							<td>
	    								<select name="appUserList[${i.index}].team" id="${i.index}team">
	    									<option value="">Select</option>
	    								</select>
	    							</td>
	    							<td>
	    								<select name="appUserList[${i.index}].meritApprovalLevel" id="${i.index}meritApprovalLevel">
	    									<option value="">Select</option>
	    									<c:forEach begin="0" var="j" step="1" end="99">
				                               <option value="${j}">
				                               		<c:out value="${j}"/>
				                               </option>
				                             </c:forEach>
	    								</select>
	    							</td>
	    							<td>
	    								<input id="${i.index}effectiveDateBox" name="appUserList[${i.index}].effectiveDate" class="easyui-datebox" width="150px"/>
	    							</td>
	    							<td>
	    								<input id="${i.index}]endDateBox" name="appUserList[${i.index}].endDate" class="easyui-datebox" width="150px"/>
	    							</td>
	    						</tr>
	    					</c:forEach>
	    				</table>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td nowrap="nowrap"><label>Email ID</label></td>
	    			<td><form:input path="emailId" /> </td>
	    		</tr>
	    		<tr>
	    			<td nowrap="nowrap"><label>Effective Date</label><span class="mandatory">*</span></td>
					<td>
						<form:hidden path="effectiveDate"/>
			    		<input id="effectiveDateBox" class="required easyui-datebox" required="true" width="150px"/>
			    	</td>
	    		</tr>
	    		<tr>
	    			<td nowrap="nowrap"><label>User Ip</label></td>
	    			<td><form:input path="userIp"/> </td>
	    		</tr>
	    		<tr>
	    			<td nowrap="nowrap"><label>Bad Login Count</label></td>
	    			<td><form:checkbox path="badLoginCount"/> </td>
	    		</tr>
	    		<tr>
	    			<td nowrap="nowrap"><label>Status</label><span class="mandatory">*</span></td>
	    			<td>
	    				<form:select path="status" cssClass="required">
	    					<form:option value="">Select</form:option>
	    					<form:option value="A">Active</form:option>
	    					<form:option value="I">Inactive</form:option>
	    					<form:option value="S">Suspended for password voilation</form:option>
	    				</form:select>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td nowrap="nowrap"><label>Security Group</label></td>
	    			<td>
	    				<form:select path="securityGroup">
	    					<form:option value="">Select</form:option>
	    				</form:select>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td nowrap="nowrap"><label>General Role</label></td>
	    			<td>
	    				<form:select path="generalRole">
	    					<form:option value="">Select</form:option>
	    				</form:select>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td nowrap="nowrap"><label>Merit Admin Approval Role</label></td>
	    			<td>
	    				<form:select path="meritAdminApprovalRole">
	    					<form:option value="">Select</form:option>
	    					 <c:forEach begin="0" var="i" step="1" end="99">
                               <option value="${i}">
                               		<c:out value="${i}"/>
                               </option>
                             </c:forEach>
	    				</form:select>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td nowrap="nowrap"><label>Performance Admin Role</label></td>
	    			<td>
	    				<form:select path="performanceAdminRole">
	    					<form:option value="">Select</form:option>
	    				</form:select>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td nowrap="nowrap"><label>Bonus Admin Role</label></td>
	    			<td>
	    				<form:select path="bonusAdminRole">
	    					<form:option value="">Select</form:option>
	    				</form:select>
	    			</td>
	    		</tr>
	    		<tr>
	    			<td nowrap="nowrap"><label>Succession Role</label></td>
	    			<td>
	    				<form:select path="successionRole">
	    					<form:option value="">Select</form:option>
	    				</form:select>
	    			</td>
	    		</tr>
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
});
function searchLastNameSelect(rec){
	$('#firstNameInputId').combobox('clear');
	$('#employeeInpuId').combobox('clear');
	$('#teamInputId').combobox('clear');
	//getEmployeeDetails(rec);
}
function searchFirstNameSelect(rec){
	$('#lastNameInputId').combobox('clear');
	$('#employeeInpuId').combobox('clear');
	$('#teamInputId').combobox('clear');
	//getEmployeeDetails(rec);
}
function searchEmpIdSelect(rec){
	$('#lastNameInputId').combobox('clear');
	$('#firstNameInputId').combobox('clear');
	$('#teamInputId').combobox('clear');
	//getEmployeeDetails(rec);
}
function searchTeamEmployeesSelect(rec){
	$('#lastNameInputId').combobox('clear');
	$('#firstNameInputId').combobox('clear');
	$('#employeeInpuId').combobox('clear');
	//getEmployeeDetails(rec);
}
function selectEmpId(rec){
	document.getElementById("employeeId").value = rec.id;
}
function save(){
	var effectiveDate = $('#effectiveDateBox').datebox('getValue');
	if($('#appUser').valid() && effectiveDate!=''){
		document.getElementById("effectiveDate").value=effectiveDate;
		loading();
		$.ajax({
			url:"saveAppUserDetails.htm",
			type: "post",
			data : $("#appUser").serialize(),
			error: function(obj){
				alert(obj);
				closeloading();
			},
			success: function(obj){
				closeloading();
			}});
	}
}
function reset(){
	document.forms[0].action='appUser.htm';
	document.forms[0].method = "post";
	document.forms[0].submit();
}
</script>

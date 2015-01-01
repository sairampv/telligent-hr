<div class="innerleft"  id="col1">
    	<table class="leftAccordion" cellspacing="1" cellpadding="5" id="employeeSearchTable">
    		<tr>
		        <th colspan="2" class="head" height="25" align="left">Search</th>
		    </tr>
		    <tr>
		    	<td>Last Name</td>
		    	<td>
		    		<input class="easyui-combobox" id="lastNameInputId" style="width:200px" data-options="
						url:'searchLastName.htm',
						method:'post',
						mode: 'remote',
						valueField: 'id',
						textField: 'value',
						selectOnNavigation:false,
						onSelect:searchLastNameSelect
						">
		    	</td>
		    </tr>
		    <tr>
		    	<td>First Name</td>
		    	<td>
		    		<input class="easyui-combobox" id="firstNameInputId" style="width:200px" data-options="
						url:'searchFirstName.htm',
						method:'post',
						mode: 'remote',
						valueField: 'id',
						textField: 'value',
						selectOnNavigation:false,
						onSelect:searchFirstNameSelect
						">
		    	</td>
		    </tr>
		    <tr>
		    	<td>Employee Id</td>
		    	<td>
		    		<input class="easyui-combobox" id="employeeInpuId" style="width:200px" data-options="
						url:'searchEmpId.htm',
						method:'post',
						mode: 'remote',
						valueField: 'id',
						textField: 'value',
						selectOnNavigation:false,
						onSelect:searchEmpIdSelect
						">
		    	</td>
		    </tr>
		    <tr>
		    	<td>Team Name</td>
		    	<td>
		    		<input class="easyui-combobox" id="teamInputId" style="width:200px" data-options="
						url:'searchTeamEmployees.htm',
						method:'post',
						mode: 'remote',
						valueField: 'id',
						textField: 'value',
						selectOnNavigation:false,
						onSelect:searchTeamEmployeesSelect
						">
		    	</td>
		    </tr>
    	</table>  
	</div>
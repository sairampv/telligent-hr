$('#mobilePhone').usphone();
$('#homePhone').usphone();
$('#workPhone').usphone();
$('#workMobilePhone').usphone();
$('#emergencyMobilePhone').usphone();
$('#emergencyHomePhone').usphone();
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
	$.ajax({
		url:"getEmployeeDetails.htm?empId="+rec.id,
		type: "post",
		dataType: 'json',
		error: function(obj){
			console.log("error");
			alert(obj);
		},
		success: function(obj){
			$.each(obj, function(i, item){
			  	if(i=='minor'){
			  		if(item)
			  			document.getElementById(i).checked = true;
			  		else
			  			document.getElementById(i).checked = false;
			  	}else{
			  		$("#"+i).val(item);   				  		
			  	}
			});
			closeloading();
		}});
}
function reset(){
	document.getElementById("employeeForm").reset();
}
function save(){
	var effectiveDate = $('#effectiveDateBox').datebox('getValue');
	var dateOfBirth = $('#dateOfBirthBox').datebox('getValue');
	if($('#employeeForm').valid() && dateOfBirth!='' && effectiveDate!=''){
		document.getElementById("effectiveDate").value=effectiveDate;
		document.getElementById("dateOfBirth").value=dateOfBirth;
		loading();
		$.ajax({
			url:"saveEmployeeDetails.htm",
			type: "post",
			data : $("#employeeForm").serialize(),
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
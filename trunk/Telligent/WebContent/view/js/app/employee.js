$('#mobilePhone').usphone();
$('#homePhone').usphone();
$('#workPhone').usphone();
$('#workMobilePhone').usphone();
$('#emergencyMobilePhone').usphone();
$('#emergencyHomePhone').usphone();

$(function(){
	$('#dateOfBirthBox').datebox().datebox('calendar').calendar({
		validator: function(date){
			var now = new Date();
			var d2 = new Date(now.getFullYear(), now.getMonth(), now.getDate());
			return date<=d2;
		}
	});
	$('#dateOfBirthBox').datebox('textbox').bind('keypress',function(e){
		return false;
	});
});

$(document).ready(function(){
	if(document.getElementById("successMessage").value == 'success'){
		alert("Employee Details Saved Successfully");
		getEmployeeDetailsAjax(document.getElementById("empId").value);
	}else if(document.getElementById("errorMessage").value !=''){
		alert(document.getElementById("errorMessage").value);
	}
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
	getEmployeeDetailsAjax(rec.id);
}
function getEmployeeDetailsAjax(id){
	$.ajax({
		url:"getEmployeeDetails.htm?empId="+id,
		type: "post",
		dataType: 'json',
		error: function(obj){
			closeloading();
			alert(obj);
		},
		success: function(obj){
			document.getElementById('employeeId').disabled='true';
			document.getElementById('employeeId').title='Disabled on Edit';
			$.each(obj, function(i, item){
			  		if(i=='picture'){
			  			//var imageVal = 'data:image/jpg;base64,'+item.bytes;
			  			//document.getElementById('image').src = imageVal;
			  		}else if(i=='effectiveDate'){
			  			document.getElementById(i).value=effectiveDate;
			  			$('#effectiveDateBox').datebox('setValue', item);
			  		}else if(i=='dateOfBirth'){
			  			document.getElementById(i).value=dateOfBirth;
			  			$('#dateOfBirthBox').datebox('setValue', item);
			  		}else if(i=='minor'){
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
		document.forms[0].action="saveEmployeeDetails.htm";
		document.forms[0].method = "post";
		document.forms[0].submit();
		/*loading();
		$.ajax({
			url:"saveEmployeeDetails.htm",
			type: "post",
			data : $("#employeeForm").serialize(),
			enctype : 'multipart/form-data',
			error: function(obj){
				alert(obj);
				closeloading();
			},
			success: function(obj){
				alert(obj);
				closeloading();
			}});*/
	}
}

function dateOfBirthSelect(date){
	var age = calculateAge(date.getMonth()+1,date.getDate(),date.getFullYear());
	if(age >= 18){
		document.getElementById('minor').checked = true;
	}else{
		document.getElementById('minor').checked = false;
	}
}
function calculateAge(birthMonth, birthDay, birthYear)
{
  var todayDate = new Date();
  var todayYear = todayDate.getFullYear();
  var todayMonth = todayDate.getMonth();
  var todayDay = todayDate.getDate();
  var age = todayYear - birthYear;
  if (todayMonth < birthMonth - 1)
  {
    age--;
  }
  if (birthMonth - 1 == todayMonth && todayDay < birthDay)
  {
    age--;
  }
  return age;
}

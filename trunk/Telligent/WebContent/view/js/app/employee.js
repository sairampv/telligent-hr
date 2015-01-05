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
		if(confirm("Do you want to update Compensation details")){
			alert("Yet to be implemented");
		}else{
			getEmployeeDetailsAjax(document.getElementById("employeeId").value);
		}
	}else if(document.getElementById("errorMessage").value !=''){
		$('#effectiveDateBox').datebox('setValue', document.getElementById("effectiveDate").value);
		$('#dateOfBirthBox').datebox('setValue', document.getElementById("dateOfBirth").value);
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
	getEmployeeDetailsAjax(rec.id);
}
function getEmployeeDetailsAjax(id){
	loading();
	$.ajax({
		url:"getEmployeeDetails.htm?empId="+id,
		type: "post",
		dataType: 'json',
		error: function(obj){
			closeloading();
			alert(obj);
		},
		success: function(obj){
			document.getElementById("updateble").value="yes";
			setEmployeeDetails(obj,id);
		}});
}
function setEmployeeDetails(obj,id){
	var state = null;
	getCityList();
	document.getElementById('employeeId').readOnly='true';
	document.getElementById('employeeId').title='Disabled on Edit';
	document.getElementById('operation').value='edit';
	$.each(obj, function(i, item){
			if(i=='picture'){
				//document.getElementById('picture').value = item;
  			}else if(i=='pictureBase64'){
  			  document.getElementById('image').src= 'data:image/bmp;base64,'+item;
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
		  	}else if(i=='state'){
		  		state = item;
		  	}else{
		  		$("#"+i).val(item);   				  		
		  	}			  		
	});
	document.getElementById('empEffectiveDt').innerHTML= 'Effective Date &nbsp;&nbsp;&nbsp;&nbsp;   ' +$('#effectiveDateBox').datebox('getValue');
	getStateList(document.getElementById('city').value,state);
	if(id!=null)
		empHistory(id);
	closeloading();
}
function empHistory(empId){
	$('#employeePersonalHistoryTable').datagrid('options').loadMsg = 'Processing, please wait .... ';  // change to other message
	$('#employeePersonalHistoryTable').datagrid('loading');  // 
	$.ajax({
		url:"getEmployeeDetailsHistory.htm?empId="+empId,
		type: "post",
		dataType: 'json',
		error: function(obj){
			$('#employeePersonalHistoryTable').datagrid('loaded');  // hide loading message
		},
		success: function(obj){
			$('#employeePersonalHistoryTable').datagrid('loadData',obj); 
			$('#employeePersonalHistoryTable').datagrid('loaded');  // hide loading message
		}});
}
function reset(){
	document.getElementById("updateble").value="yes";
	//document.getElementById("employeeForm").reset();
	document.forms[0].action="employee.htm";
	document.forms[0].method = "post";
	document.forms[0].submit();
}
function save(){
	if(document.getElementById("updateble").value == "no"){
		alert("You cannot update the History record.");
		return false;
	}
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
		document.getElementById('minor').checked = false;
	}else{
		document.getElementById('minor').checked = true;
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
function PreviewImage() {
    var oFReader = new FileReader();
    oFReader.readAsDataURL(document.getElementById("picture").files[0]);

    oFReader.onload = function (oFREvent) {
        document.getElementById("image").src = oFREvent.target.result;
    };
}
function getCityList(){
	$.ajax({
		url:"getCityListAjax.htm",
		type: "post",
		dataType: 'json',
		error: function(obj){
			closeloading();
		},
		success: function(data){
			var len = data.length;
			var html = "";
            for(var i=0; i<len; i++){
                 html += '<option value="' + data[i].id + '">' + data[i].city + '</option>';
             }
            $('#city').append(html);
            closeloading();
		}});
}
function getStateList(val,state){
	$('#state').empty();
	$('#state').append('<option value="">Select</option>');
	loading();
	$.ajax({
		url:"getStateDetails.htm?cityId="+val,
		type: "post",
		dataType: 'json',
		error: function(obj){
			closeloading();
		},
		success: function(data){
			var len = data.length;
			var html = "";
            for(var i=0; i<len; i++){
                 html += '<option value="' + data[i].id + '">' + data[i].stateName + '</option>';
             }
            $('#state').append(html);
            $("#state").val(state); 
            closeloading();
		}});
}
function formatDetail(value,row,index){
	return '<a href="#" onclick="javascript:getEmployeeDetailsFromHistoryAjax('+row.seqNo+')">'+value+'</a>';
}
function getEmployeeDetailsFromHistoryAjax(id){
	loading();
	$.ajax({
		url:"getEmployeeDetailsFromHistoryAjax.htm?seqNo="+id,
		type: "post",
		dataType: 'json',
		error: function(obj){
			closeloading();
			alert(obj);
		},
		success: function(obj){
			document.getElementById("updateble").value="no";
			setEmployeeDetails(obj,null);
		}});
}
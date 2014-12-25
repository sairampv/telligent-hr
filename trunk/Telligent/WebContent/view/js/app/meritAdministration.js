/**
 * 
 */
$.extend($.fn.datagrid.methods, {
		editCell : function(jq, param) {
			return jq.each(function() {
				var opts = $(this).datagrid('options');
				var fields = $(this).datagrid('getColumnFields', true).concat(
						$(this).datagrid('getColumnFields'));
				for (var i = 0; i < fields.length; i++) {
					var col = $(this).datagrid('getColumnOption', fields[i]);
					col.editor1 = col.editor;
					if (fields[i] != param.field) {
						col.editor = null;
					}
				}
				$(this).datagrid('beginEdit', param.index);
				for (var i = 0; i < fields.length; i++) {
					var col = $(this).datagrid('getColumnOption', fields[i]);
					col.editor = col.editor1;
				}
			});
		}
	});

	var editIndex = undefined;
	function endEditing() {
		if (editIndex == undefined) {
			return true;
		}
		if ($('#tt').datagrid('validateRow', editIndex)) {
			$('#tt').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	function onClickCell(index, field) {
		if (endEditing()) {
			$('#tt').datagrid('editCell', {
				index : index,
				field : field
			});
			//.datagrid('updateRow',{index: index,row:{}});
			editIndex = index;
		}
	}
	function update() {
		$('#tt').datagrid('acceptChanges');
		var rows = $('#tt').datagrid('getSelections');
		for (var i = 0; i < rows.length; i++) {
			alert(rows[i].perfGrade+"==="+rows[i].incrementPercentage);
		}
	}

var perfGradeList = [
		    {value:'A',name:'A'},
		    {value:'B',name:'B'},
		    {value:'C',name:'C'}
		];
var updateList = new Array();
$(document).ready(function(){
	  //$('#container').layout();
	  showGridMessage('#tt','Please select Team on left side panel to display your team employees');
	  showGridMessage('#budgetTable','Please select Team on left side panel to display team wise Summary');
	  showGridMessage('#budgetTable1','Please select Team on left side panel to display team wise budget');
});
String.prototype.startsWith = function (str){
      return this.indexOf(str) == 0;
};
$("#teamListTable input[type='checkbox']").click( function(){
	var numberOfChBox = $("#teamListTable input[type='checkbox']").length;
    var checkArray = ""; 
	for(var i = 0; i < numberOfChBox; i++) {
	     if($('#teamCheckBoxId'+i).is(':checked')) {
            if(checkArray != "")
	    	 	checkArray = checkArray+$('#teamCheckBoxId'+i).val()+",";
            else
            	checkArray = $('#teamCheckBoxId'+i).val()+",";
        }
    }
	checkArray = checkArray.substring(0, checkArray.length-1);
	showTeamEmployees("", checkArray);
});
function showTeamEmployeesAjax(){
	closeloading();
	var numberOfChBox = $("#teamListTable input[type='checkbox']").length;
    var checkArray = ""; 
	for(var i = 0; i < numberOfChBox; i++) {
	     if($('#teamCheckBoxId'+i).is(':checked')) {
            if(checkArray != "")
	    	 	checkArray = checkArray+$('#teamCheckBoxId'+i).val()+",";
            else
            	checkArray = $('#teamCheckBoxId'+i).val()+",";
        }
    }
	checkArray = checkArray.substring(0, checkArray.length-1);
	showTeamEmployees("", checkArray);
}
  $('#teamListTable a ').click(function() {
		$('#teamListTable a').removeClass('selectLink');
		$(this).closest('a').addClass('selectLink');
	});
function getSelected(){
  var row = $('#tt').datagrid('getSelected');
	  if (row){
	 	 $.messager.alert('Info', row.coworker_name+":"+row.employeeId+":"+row.supervisor);
	  }
  }
function sendForApproval(){
    var ids = [];
    var rows = $('#tt').datagrid('getSelections');
    for(var i=0; i<rows.length; i++){
    	ids.push(rows[i].coworker_name);
    }
    alert(ids.join('\n'));
}
	function rowSelected(index,row){
		$('#tt').datagrid('checkRow',index);
		$('#tt').datagrid('selectRow',index);
	}
  	/*function rowSelected(){
  		var rows = $('#tt').datagrid('getSelections');
  		if(rows.length > 0){
  			 $("#updateRateTableId").toggle(true);
  		}else{
  			$("#updateRateTableId").toggle(false);
  		}
  	}*/
   function updateEmployeeDetails(){
	   updateList = new Array();
	    var incrementPercentage = $("#incrementPercentage").val().trim(); 
      	var perfGrade = $("#perfGrade").val().trim();
      	if(incrementPercentage == '' && perfGrade == '')
      		alert("Please select either Performance Grade or Enter  Percentage");
   		else{
   			loading();
       		var rows = $('#tt').datagrid('getSelections');
               	for(var i=0; i<rows.length; i++){
   	      			  var salarPlanningDTO = new Object();
   	      		      salarPlanningDTO.employeeId = rows[i].employeeId;
   	          		  salarPlanningDTO.newRate = rows[i].newRate;
   	          		  salarPlanningDTO.rate = rows[i].rate;
   	          		  salarPlanningDTO.maximum = rows[i].maximum;
   	          		  salarPlanningDTO.lumsum = rows[i].lumsum;
   	          		  salarPlanningDTO.perfGrade = perfGrade;
   	          		  salarPlanningDTO.incrementPercentage = incrementPercentage;
   	          		  updateList.push(salarPlanningDTO);
   	          	}
               	$.ajax({
           			url:"updateEmployeeDetails.htm",
           			type: "post",
           			data: JSON.stringify(updateList) ,
           			contentType : "application/json; charset=utf-8",
           			error: function(obj){
           				console.log("error");
           				alert(obj);
           			},
           			success: function(obj){
           				alert(obj);
           				//location.reload(true);
           				$("#updateRateTableId").toggle(false);
           				$("#incrementPercentage").val(""); 
           	          	$("#perfGrade").val("");
           				showTeamEmployeesAjax();
           				return false;
           			}});
    }
   }
   function updateEmployeeDetailsSelected(){
	    $('#tt').datagrid('acceptChanges');
	   	updateList = new Array();
		var rows = $('#tt').datagrid('getSelections');
       	if(rows.length > 0){
       		loading();
       		for(var i=0; i<rows.length; i++){
    			  var salarPlanningDTO = new Object();
    		      salarPlanningDTO.employeeId = rows[i].employeeId;
        		  salarPlanningDTO.newRate = rows[i].newRate;
        		  salarPlanningDTO.rate = rows[i].rate;
        		  salarPlanningDTO.maximum = rows[i].maximum;
        		  salarPlanningDTO.lumsum = rows[i].lumsum;
        		  salarPlanningDTO.type = rows[i].type;
        		  salarPlanningDTO.perfGrade = rows[i].perfGrade;
        		  salarPlanningDTO.incrementPercentage = rows[i].incrementPercentage;
        		  updateList.push(salarPlanningDTO);
        	}
         	$.ajax({
     			url:"updateEmployeeDetails.htm",
     			type: "post",
     			data: JSON.stringify(updateList) ,
     			contentType : "application/json; charset=utf-8",
     			error: function(obj){
     				console.log("error");
     				alert(obj);
     			},
     			success: function(obj){
     				alert(obj);
     				//location.reload(true);
     				/*$("#updateRateTableId").toggle(false);
     				$("#incrementPercentage").val(""); 
     	          	$("#perfGrade").val("");*/
     				showTeamEmployeesAjax();
     				return false;
     			}});
       	}else{
       		alert("No Changes to Apply");
       	}
   }
   function setValuesButton(){
   	$('#popUphrefId').click();
   }
   function setValues(){
         var rows = $('#tt').datagrid('getSelections');
         if(rows.length == 1){
       	  $(".inline").colorbox({inline:true, width:"40%"});
             $("#click").click(function(){
                     $('#click').css({"background-color":"#f00", "color":"#fff", "cursor":"inherit"}).text("Open this window again and this message will still be here.");
                     return true;
             });
         }else if(rows.length > 1){
       	  if(confirm("You have selected multiple. All records will update with same Rate. Do you wish to continue ?")){
       		  $(".inline").colorbox({inline:true, width:"40%"});
                 $("#click").click(function(){
                         $('#click').css({"background-color":"#f00", "color":"#fff", "cursor":"inherit"}).text("Open this window again and this message will still be here.");
                         return true;
                 });
       	  }else
             	return false;
         }else{
             alert("Please select row for further process");
             window.parent.jQuery.colorbox.close();
             return false;
         }
   }
   
   function anualBudgetSummary(){
	$.ajax({
		url:"anualBudgetSummary.htm",
		type: "get",
		data: $('#budgetSummary').serialize(),
		error: function(obj){
			alert(obj);
		},
		success: function(obj){
			alert(obj);
			window.parent.jQuery.colorbox.close();
			return false;
		}});
}
   function showSalRangeButton(){
   	$('#salRangePopUphrefId').click();
   }
   function showSalRange(){
  	 $(".salRangePopUpClass").colorbox({inline:true, width:"80%"});
	
       //Example of preserving a JavaScript event for inline calls.
       $("#click").click(function(){
               $('#click').css({"background-color":"#f00", "color":"#fff", "cursor":"inherit"}).text("Open this window again and this message will still be here.");
               return true;
       });
  }
  function showTeamEmployees(teamName,teamId){
   $('#tt').datagrid('options').loadMsg = 'Processing, please wait .... ';  // change to other message
   $('#tt').datagrid('loading');  // display loading message
   
   $('#budgetTable1').datagrid('options').loadMsg = 'Processing, please wait .... ';  // change to other message
   $('#budgetTable1').datagrid('loading');  // 
   
   $('#budgetTable').datagrid('options').loadMsg = 'Processing, please wait .... ';  // change to other message
   $('#budgetTable').datagrid('loading');  // 
	$.ajax({
		url:"getSalaryPlanningDetails.htm?teamName="+teamName+"&teamId="+teamId,
		type: "post",
		dataType: 'json',
		error: function(obj){
			$('#tt').datagrid('loaded');  // hide loading message
		},
		success: function(obj){
			removeGridMessage("#tt");
			$('#tt').datagrid('loadData',obj); 
			$('#tt').prop('title', teamName+" Employee List");
			$('#tt').datagrid('loaded');  // hide loading message
			loadBudgetSummaryTeamWise(teamName,teamId);
		}});
}
   function loadBudgetSummaryTeamWise(teamName,teamId){
   	$.ajax({
		url:"anualBudgetSummary.htm?teamName="+teamName+"&teamId="+teamId,
		type: "post",
		dataType: 'json',
		error: function(obj){
			$('#budgetTable1').datagrid('loaded');  // hide loading message
		},
		success: function(obj){
			removeGridMessage("#budgetTable1");
			$('#budgetTable1').datagrid({
				rowStyler:function(index,row){
					if (row.anualBudgetType == 'Total'){
						return 'color:blue;font-weight:bold;';
					}
				}
			});
			$('#budgetTable1').datagrid('loadData',obj); 
			$('#budgetTable1').prop('title', teamName+" Employee List");
			$('#budgetTable1').datagrid('loaded');  // hide loading message
			loadRatingsAndIncreaseSummaryTeamWise(teamName,teamId);
		}});
   }
   function loadRatingsAndIncreaseSummaryTeamWise(teamName,teamId){
   	$.ajax({
		url:"ratingsAndIncreaseSummary.htm?teamName="+teamName+"&teamId="+teamId,
		type: "post",
		dataType: 'json',
		error: function(obj){
			$('#budgetTable').datagrid('loaded');  // hide loading message
		},
		success: function(obj){
			removeGridMessage("#budgetTable");
			$('#budgetTable').datagrid('loadData',obj); 
			$('#budgetTable').prop('title', teamName+" Employee List");
			$('#budgetTable').datagrid('loaded');  // hide loading message
		}});
   }
   
function showGridMessage(target,displayText) {
	var vc = $(target).datagrid('getPanel').children(
			'div.datagrid-view');
	vc.children('div.datagrid-empty').remove();
	if (!$(target).datagrid('getRows').length) {
		var d = $('<div class="datagrid-empty"></div>').html(displayText).appendTo(vc);
		d.css({
			position : 'absolute',
			left : 0,
			top : 50,
			width : '100%',
			textAlign : 'center'
		});
	} else {
		vc.children('div.datagrid-empty').remove();
	}
}
function removeGridMessage(target){
	var vc = $(target).datagrid('getPanel').children(
	'div.datagrid-view');
	vc.children('div.datagrid-empty').remove();
}
function onResizeColumn(field,width){
	$.ajax({
		url:"updateSalaryPlanningColumnWidth.htm?field="+field+"&width="+width,
		type: "GET",
		dataType: 'json',
		error: function(obj){
		},
		success: function(obj){
		}});
}
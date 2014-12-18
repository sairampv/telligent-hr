<%@ include file="taglib.jsp" %>
<title>Dashboard</title>
<link href="view/css/style.css" rel="stylesheet" type="text/css" />
<link href="view/css/colorbox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="view/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="view/js/jquery/jquery.validate.js"></script>
<script src="view/js/jquery/jquery.colorbox.js"></script>
<link rel="stylesheet" type="text/css" href="view/css/jquery/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="view/css/jquery/themes/icon.css">
<link rel="stylesheet" type="text/css" href="view/css/jquery/demo.css">
<script type="text/javascript" src="view/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="view/js/app/telligentCommon.js"></script>
<div id="contentArea">
<div class="contentArea">
	<div class="innerleft"  id="col1">
     <table class="leftAccordion" cellspacing="1" cellpadding="5" id="teamListTable">
      <tr>
        <th class="head" height="25" align="left">My Teams</th>
      </tr>
      <c:forEach items="${teams}" var="item" varStatus="i">
	      <tr>
	        <td height="20">
	        	<input type="checkbox" name="teamCheckBoxName" id="teamCheckBoxId${i.index}"  value="${item.teamId}">${item.teamName}</input>
	        	<%-- <a href="javascript:showTeamEmployees('${item.teamName}','${item.teamId}');">${item.teamName}</a> --%>
	        </td>
	      </tr>
      </c:forEach>
    </table>  
 </div>
 <div class="innerright"  id="flow">
 	<a href="javascript:toggle()" title="Hide Nav 13" id="flowtab"></a>
 	<div style="margin:0px;cursor:auto;" id="tab">
        <div class="wrap">  
        <!-- <a href="#popUp" id="popUphrefId" class="button inline" onclick="setValues();"  style="display: none;">Update Rate</a> -->
        <a href="#salRangePopUp" id="salRangePopUphrefId" class="button salRangePopUpClass" onclick="showSalRange();"  style="display: none;">General Guidelines</a>
    <table width="100%" border="0" cellspacing="1" cellpadding="5" bgcolor="E3E3E3" align="right">
      <tr>
        <th class="head1" height="25" align="left">
        <div class="innerpage-breadcrum">
			<a href="dashboard.htm">Dashboard</a> &nbsp;&gt;&nbsp; 
			<a href="exciseDashboard.htm">Merit Administration</a> &nbsp;&gt;&nbsp; 
			<a href="javascript:void(0);" class="select">Employee Salary Planning</a>
			<span style="float: right"><a href="dashboard.htm">Back</a></span>
		</div>
		</th>
      </tr>
    </table>
    <br>
    <table>
    	<tr>
    		<td><input type="button" class="button" value="Assign Default Merit Increases" onclick="javascript:getSelected();"></td>
    		<td><input type="button" class="button" value="Validate Merit Increases"></td>
    		<td><input type="button" class="button" value="Send for Approval" onclick="javascript:sendForApproval()"></td>
    		<td><input type="button" class="button" value="Print / Reports"></td>
            <!-- <td><input type="button" class="button" value="Update Rate" onclick="setValuesButton();" ></td> -->
            <td><input type="button" class="button" value="General Guidelines" onclick="showSalRangeButton();" ></td>
    	</tr>
    </table>
    
    <form id="updateRateForm">
	    <table  width="100%" border="0" cellspacing="0" cellpadding="0" id="updateRateTableId" style="display: none;" class="data-table"> 
	       <tr>
	  			<td valign="middle" width="10%" >Perf Grade<span class="mandatory">*</span></td>
		        <td valign="middle" width="3%">:</td>
		        <td valign="middle" width="10%">
		        	<select id="perfGrade" class="required">
		        		<option value="">Select</option>
		        		<option value="A">A</option>
		        		<option value="B">B</option>
		        		<option value="C">C</option>
		        	</select>
		        </td>
	  			<td valign="middle" width="16%">Increment Percentage<span class="mandatory">*</span></td>
		        <td valign="middle" width="3%">:</td>
		        <td valign="middle" width="20%"><input id="incrementPercentage" name="incrementPercentage" class="required" onKeyPress="return numbersonly(event, true,this.value)"></input></td>
		        <td valign="middle">&nbsp;</td>
		        <td valign="middle" height="2" ><input type="button" onclick="javascript:updateEmployeeDetails();" value="Apply" class="loginButton"/></td>
		      </tr>
	  	</table>
    </form>
    
    <table id="tt" class="easyui-datagrid" title="Employee Salary Planning" style="width:100%;height:300px;table-layout: fixed;"
			data-options="collapsible:true
							,pagination:true
							,emptyMsg: 'No records found'
							,pageSize:20
							,onSelect:rowSelected
							,onUnselect:rowSelected
							,onSelectAll:rowSelected
							,onUnSelectAll:rowSelected
							,onCheckAll:rowSelected
							,onUncheckAll:rowSelected
							,onCheckAll:rowSelected
							,onResizeColumn:onResizeColumn">
			<thead data-options="frozen:true">
				<tr>
					<th field="ck" checkbox="true"></th>
					<th data-options="field:'id'" hidden="true" align="right">Id</th>
					<th data-options="field:'employeeId',width:${gridViewMap['employeeId']}" sortable="true" align="right">Employee ID</th>
					<th data-options="field:'coworker_name',width:${gridViewMap['coworker_name']}" sortable="true" align="left">Employee Name</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th data-options="field:'type',width:${gridViewMap['type']}" align="left">Type</th>
					<th data-options="field:'rate',width:${gridViewMap['rate']}" align="right">Current Rate</th>
					<th data-options="field:'perfGrade',width:${gridViewMap['perfGrade']}" align="left">Perf Grade</th>
					<th data-options="field:'incrementPercentage',width:${gridViewMap['incrementPercentage']}" align="right">Incr %</th>
					<th data-options="field:'newRate',width:${gridViewMap['newRate']}" align="right">New Rate</th>
					<th data-options="field:'lumsum',width:${gridViewMap['lumsum']}" align="right">Lum Sum</th>
					<th data-options="field:'jobTitle',width:${gridViewMap['jobTitle']}" align="left">Job Title</th>
					<th data-options="field:'updatedDate',width:${gridViewMap['updatedDate']}" align="left">Updated Date</th>
					<th data-options="field:'grade',width:${gridViewMap['grade']}" align="left">Grade</th>
					<th data-options="field:'compaRatio',width:${gridViewMap['compaRatio']}" align="right">Compa Ratio</th>
					<th data-options="field:'minimum',width:${gridViewMap['minimum']}" align="right">Minimum</th>
					<th data-options="field:'midpoint',width:${gridViewMap['midpoint']}" align="right">Midpoint</th>
					<th data-options="field:'maximum',width:${gridViewMap['maximum']}" align="right">Maximum</th>
					<th data-options="field:'quartile',width:${gridViewMap['quartile']}" align="left">Quartile</th>
				</tr>
			</thead>
		</table>
		<div class="footer"></div>
	<table style="width: 100%;height: 100%;padding-top: 20px">
			<tr>	
				<td style="width: 50%">
					<table id="budgetTable"  class="easyui-datagrid" title="Ratings & Increase Summary" style="width:100%;height:215px;padding-left: 100px"
						data-options="collapsible:true
										,method: 'get'
										,pagination:false
										,emptyMsg: 'No records found'">
						<thead>
							<tr>
								<th colspan="3">Hourly</th>
								<th colspan="3">Office</th>
							</tr>
						</thead>
						<thead data-options="frozen:true">
							<tr>
								<th data-options="field:'type',width:185" >Performance Rating</th>
							</tr>
						</thead>
						<thead>
							<tr>
								<th data-options="field:'hourlyA',width:70" align="right" >A</th>
								<th data-options="field:'hourlyB',width:70" align="right" >B</th>
								<th data-options="field:'hourlyC',width:70" align="right" >C</th>
								<th data-options="field:'officeA',width:70" align="right" >A</th>
								<th data-options="field:'officeB',width:70" align="right" >B</th>
								<th data-options="field:'officeC',width:70" align="right" >C</th>
							</tr>
						</thead>
					</table>
				</td>
				<td style="width: 50%">
					<table id="budgetTable1"  class="easyui-datagrid" title="Budget Summary" style="width:100%;height:215px; padding-left: 100px"
						data-options="collapsible:true
										,method: 'get'
										,pagination:false
										,emptyMsg: 'No records found'">
						<thead data-options="frozen:true">
							<tr>
								<th data-options="field:'anualBudgetType',width:150"  align="left" >Annual Budget</th>
							</tr>
						</thead>
						<thead>
							<tr >
								<th data-options="field:'currentBudget',width:100" align="right" >Current</th>
								<th data-options="field:'newBudget',width:100" align="right" >New</th>
								<th data-options="field:'changeBudget',width:140" align="right" >%Change</th>
							</tr>
						</thead>
					</table>
				</td>
			</tr>
		</table>
  </div>
  </div>
  </div>
  <div style="display: none;">
 <%--  <div id="popUp" style="padding:10px; background:#fff;" style="width:100px">
      <form:form action="salaryPalnning.htm"  method="post" commandName="salaryPlanning" id="salaryPlanning">
          <form:hidden path="lumsum"></form:hidden>
          <form:hidden path="rate"></form:hidden>
          <form:hidden path="newRate"></form:hidden>
          <form:hidden path="maximum"></form:hidden>
  	<table  width="100%" border="0" cellspacing="0" cellpadding="0">
                
                <tr>
  		<td align="left" valign="middle" width="25%" height="35">Employee Name</td>
	        <td align="left" valign="middle" width="3%">:</td>
                <td align="left" valign="middle" width="80%"><form:input path="employeeId" readonly="true"></form:input></td>
  		</tr>
                <tr>
  		<td align="left" valign="middle" width="25%" height="35">Perf Grade</td>
	        <td align="left" valign="middle" width="3%">:</td>
	        <td align="left" valign="middle" width="80%">
	        	<form:select path="perfGrade" id="perfGrade">
	        		<form:option value="">Select</form:option>
	        		<form:option value="A">A</form:option>
	        		<form:option value="B">B</form:option>
	        		<form:option value="C">C</form:option>
	        	</form:select>
	        </td>
  		</tr>
  		<tr>
  		<td align="left" valign="middle" width="25%" height="35">Increment Percentage</td>
	        <td align="left" valign="middle" width="3%">:</td>
	        <td align="left" valign="middle" width="80%"><form:input path="incrementPercentage" id="incrementPercentage" onKeyPress="return numbersonly(event, true,this.value)"></form:input></td>
  		</tr>
  		<tr>
	        <td align="left" valign="middle" height="35">&nbsp;</td>
	        <td align="left" valign="middle">&nbsp;</td>
	        <td align="left" valign="middle"><input type="button" onclick="javascript:updateEmployeeDetails();" value="Submit" class="loginButton"/></td>
	      </tr>
  	</table>
  	 </form:form>
  </div> --%>
   
  <div id="salRangePopUp"  style="padding:10px; background:#fff;" style="width:100px">
  	<br clear="all"/>
	<table width="100%" border="0" cellspacing="1" cellpadding="5" bgcolor="E3E3E3" align="right" >
	  <tr>
	     <th class="head1" height="25" align="left">
		  	<div class="innerpage-breadcrum">
		  		<table>
		  			<tr><td>General Guidelines</td></tr>
		  		</table>
		  	</div>
		 </th>
	  </tr>
	</table>
  	<br clear="all"/>
 	<table cellspacing="0" cellpadding="5" border="1" >
 		<tr>
 			<td style="width: 20%" colspan="2"></td>
 			<td colspan="4" class="innertop2" style="text-align: center;color: white">Position in Salary Range</td>
 		</tr>
 		<tr class="innertop2">
 			<td style="width: 20%;text-align: center;color: white">Overall Performance Rating</td>
 			<td style="width: 20%;text-align: center;color: white">Aggregate Expected Ratings Distribution @ Co.level</td>
 			<td style="width: 15%;text-align: center;color: white">1 st Quartile</td>
 			<td style="width: 15%;text-align: center;color: white">2 nd Quartile</td>
 			<td style="width: 15%;text-align: center;color: white">3 rd Quartile</td>
 			<td style="width: 15%;text-align: center;color: white">4 th Quartile</td>
 		</tr>
 		
 		<c:forEach  items="${salaryPositionRangeDetails}" var="item" varStatus="i">
 			<tr>
	 			<td style="width: 20%;text-align: center;">${item.overallPerformanceRating}</td>
	 			<td style="width: 20%;text-align: center;">${item.aggregateExpected}</td>
	 			<td style="width: 15%;text-align: center;">${item.firstQuartile}</td>
	 			<td style="width: 15%;text-align: center;">${item.secondQuartile}</td>
	 			<td style="width: 15%;text-align: center;">${item.thirdQuartile}</td>
	 			<td style="width: 15%;text-align: center;">${item.fourthQuartile}</td>
 			</tr>
 		</c:forEach>
 	
 	</table>
  </div>             
  </div>
  </div>
</div> 
  <script type="text/javascript">
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
      	var updateList = new Array();
		  $(document).ready(function(){
			  showGridMessage('#tt','Please select Team on left side panel to display your team employees');
			  showGridMessage('#budgetTable','Please select Team on left side panel to display team wise Summary');
			  showGridMessage('#budgetTable1','Please select Team on left side panel to display team wise budget');
		  });
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
	  	function rowSelected(){
	  		var rows = $('#tt').datagrid('getSelections');
	  		if(rows.length > 0){
	  			 $("#updateRateTableId").toggle(true);
	  		}else{
	  			$("#updateRateTableId").toggle(false);
	  		}
	  	}
       function updateEmployeeDetails(){
	       	if($("#updateRateForm").valid()){
	       		var rows = $('#tt').datagrid('getSelections');
	           	var incrementPercentage = $("#incrementPercentage").val().trim(); 
	           	var perfGrade = $("#perfGrade").val().trim();
	           	if(perfGrade == null || perfGrade == ''){
	             	  alert("Please enter Perf Grade percentage")
	             	  return false;
	               }else if(incrementPercentage == null || incrementPercentage ==''){
	             	  alert("Please enter Increment percentage");
	             	  return false;
	               }else{
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
	           				showTeamEmployeesAjax();
	           				return false;
	           			}});
	               }
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
   			type: "GET",
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
   			type: "GET",
   			dataType: 'json',
   			error: function(obj){
   				$('#budgetTable1').datagrid('loaded');  // hide loading message
   			},
   			success: function(obj){
   				removeGridMessage("#budgetTable1");
   				$('#budgetTable1').datagrid('loadData',obj); 
   				$('#budgetTable1').prop('title', teamName+" Employee List");
   				$('#budgetTable1').datagrid('loaded');  // hide loading message
   				loadRatingsAndIncreaseSummaryTeamWise(teamName,teamId);
   			}});
       }
       function loadRatingsAndIncreaseSummaryTeamWise(teamName,teamId){
       	$.ajax({
   			url:"ratingsAndIncreaseSummary.htm?teamName="+teamName+"&teamId="+teamId,
   			type: "GET",
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
   			error: function(obj){
   			},
   			success: function(obj){
   			}});
	}
	</script>
	<script type="text/javascript" src="view/js/docknavigation.js"></script>
  
<!-- </div> -->

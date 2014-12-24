<%@ include file="taglib.jsp" %>
<title>Dashboard</title>
<link href="view/css/style.css" rel="stylesheet" type="text/css" />
<link href="view/css/colorbox.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="view/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="view/js/jquery/jquery.validate.js"></script>
<script src="view/js/jquery/jquery.colorbox.js"></script>
<script src="view/js/popup.js"></script>
<link rel="stylesheet" type="text/css" href="view/css/popup.css">
<link rel="stylesheet" type="text/css" href="view/css/jquery/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="view/css/jquery/themes/icon.css">
<link rel="stylesheet" type="text/css" href="view/css/jquery/demo.css">
<script type="text/javascript" src="view/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="view/js/app/telligentCommon.js"></script>

<div id="contentArea">
<div class="loader"></div>
<div class="contentArea">
	<div class="innerleft"  id="col1">
     <table class="leftAccordion" cellspacing="1" cellpadding="5" id="teamListTable">
      <tr>
        <th class="head" height="25" align="left">My Teams</th>
      </tr>
      <c:forEach items="${teams}" var="item" varStatus="i">
	      <tr>
	        <td height="20">
	        	<input type="checkbox" name="teamCheckBoxName" id="teamCheckBoxId${i.index}"  value="${item.teamId}">${item.teamName}
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
            <!-- <td><input type="button" class="button" value="Update Rate" onclick="update();" ></td> --> 
            <!-- <td><input type="button" class="button" value="General Guidelines" onclick="showSalRangeButton();" ></td> -->
    	</tr>
    </table>
    
    <!-- <form id="updateRateForm">
	    <table  width="100%" border="0" cellspacing="0" cellpadding="0" id="updateRateTableId" style="display: none;" class="data-table"> 
	       <tr>
	  			<td valign="middle" width="20%" >Enter Grade / Increase % <span class="mandatory">*</span></td>
		        <td valign="middle" width="10%">
		        	<select id="perfGrade">
		        		<option value="">Select</option>
		        		<option value="A">A</option>
		        		<option value="B">B</option>
		        		<option value="C">C</option>
		        	</select>
		        </td>
		        <td valign="middle" width="20%"><input id="incrementPercentage" name="incrementPercentage" onKeyPress="return numbersonly(event, true,this.value)"></input></td>
		        <td valign="middle" height="2" ><input type="button" onclick="javascript:updateEmployeeDetails();" value="Apply" title="Apply Selected Rows with Same Values" class="loginButton"/></td>
		        <td valign="middle" height="2" ><input type="button" onclick="javascript:updateEmployeeDetailsSelected();" value="Apply Selected" title="Apply Selected Rows with Selected Values" class="loginButton"/></td>
		      </tr>
	  	</table>
    </form> -->
    
    <table id="tt" class="easyui-datagrid" title="Employee Salary Planning" style="width:100%;height:300px;table-layout: fixed;"
			data-options="collapsible:true
							,pagination:true
							,emptyMsg: 'No records found'
							,pageSize:20
							,onResizeColumn:onResizeColumn
							,onClickCell: onClickCell
							,toolbar:'#tb'
							,iconCls: 'icon-edit'
							,onSelect:rowSelected
							,onUnselect:rowSelected
							,onSelectAll:rowSelected
							,onUnSelectAll:rowSelected
							,onCheckAll:rowSelected
							,onUncheckAll:rowSelected
							,onCheckAll:rowSelected
							,checkOnSelect: false
							,selectOnCheck: false">
			<thead data-options="frozen:true">
				<tr>
					<th data-options="field:'ck',checkbox:'true'" ></th>
					<th data-options="field:'id'" hidden="true" align="right">Id</th>
					<th data-options="field:'employeeId',width:${gridViewMap['employeeId']}" sortable="true" align="right">Employee ID</th>
					<th data-options="field:'coworker_name',width:${gridViewMap['coworker_name']}" sortable="true" align="left">Employee Name</th>
				</tr>
			</thead>
			<thead>
				<tr>
					<th data-options="field:'type',width:${gridViewMap['type']}" align="left">Type</th>
					<th data-options="field:'rate',width:${gridViewMap['rate']}" align="right">Current Rate</th>
					<th data-options="field:'perfGrade',editor:{type:'combobox',options:{valueField:'value',textField:'name',data:perfGradeList}},width:${gridViewMap['perfGrade']}" align="left">Perf Grade</th>
					<th data-options="field:'incrementPercentage',editor:{type:'numberbox',options:{precision:1}},width:${gridViewMap['incrementPercentage']}" align="right">Incr %</th>
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
		
		<table id="guideLinesTable"  class="easyui-datagrid" title="General Guidelines"  style="width:100%;height:170px;table-layout: fixed;"
						data-options="collapsible:true
										,method: 'post'
										,url: 'salaryPositionRangeDetails.htm'
										,pagination:false
										,emptyMsg: 'No records found'
										,collapsed:true " >
							<thead style="width: 5000">
							<tr>
								<th colspan="2" data-options="field:'type',width:500"></th> 
								<th colspan="4" data-options="width:186"><b>Position in Salary Range</b></th>
							</tr>
						</thead>
						<thead >
							<tr >
								<th data-options="field:'overallPerformanceRating',width:250" align="left" >Overall Performance Rating</th>
								<th data-options="field:'aggregateExpected',width:220" align="right" >Aggregate Expected Ratings <br/>Distribution @ Co.level</th>
								<th data-options="field:'firstQuartile',width:125" align="right" >1 st Quartile</th>
								<th data-options="field:'secondQuartile',width:125" align="right" >2 nd Quartile</th>
								<th data-options="field:'thirdQuartile',width:125" align="right" >3 rd Quartile</th>
								<th data-options="field:'fourthQuartile',width:125" align="right" >4 th Quartile</th>
							</tr>
							
	 		</thead>

</table>
		
	<table style="width: 100%;height: 100%;padding-top: 20px">
			<tr>	
				<td style="width: 55%">
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
								<th data-options="field:'type',width:150" >Performance Rating</th>
							</tr>
						</thead>
						<thead>
							<tr>
								<th data-options="field:'hourlyA',width:60" align="right" >A</th>
								<th data-options="field:'hourlyB',width:60" align="right" >B</th>
								<th data-options="field:'hourlyC',width:60" align="right" >C</th>
								<th data-options="field:'officeA',width:60" align="right" >A</th>
								<th data-options="field:'officeB',width:60" align="right" >B</th>
								<th data-options="field:'officeC',width:60" align="right" >C</th>
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
								<th data-options="field:'anualBudgetType',width:130"  align="left" >Annual Budget</th>
							</tr>
						</thead>
						<thead>
							<tr >
								<th data-options="field:'currentBudget',width:100" align="right" >Current</th>
								<th data-options="field:'newBudget',width:100" align="right" >New</th>
								<th data-options="field:'changeBudget',width:100" align="right" >%Change</th>
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
<div id="tb" style="height:auto">
	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="updateEmployeeDetailsSelected()">Apply</a>
</div>
<script type="text/javascript" src="view/js/app/meritAdministration.js"></script>
<script type="text/javascript" src="view/js/docknavigation.js"></script>
  
<!-- </div> -->

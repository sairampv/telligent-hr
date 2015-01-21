<form id="refTablesLeft">
	<div class="innerleft" style="min-height: 480px" id="col1">
	   	<table class="leftAccordion" cellspacing="1" cellpadding="5" id="refTablesLeft">
	   		<tr>
		        <th colspan="2" class="head" height="25" align="left">Search</th>
		    </tr>
		    <tr>
				<td><input style="width: 100%;text-align: left;" type="button" class="button" value="Base Rate Frequency" id="baseRateFreq" onclick="javascript:submitted('referenceTables.htm')"></td>
		    </tr>
		    <tr>
		    	<td><input style="width: 100%;text-align: left;" type="button" class="button" value="Bonus Plan" id="bonusPlan" onclick="javascript:submitted('bonusPlan.htm')"></td>
		    </tr>
		    
		    <!--  start -->
		      <tr>
		    	<td><input style="width: 100%;text-align: left;" type="button" class="button" value="Employee Classification" id="classification" onclick="javascript:submitted('classification.htm')"></td>
		    </tr>
		    
		      <tr>
		    	<td><input style="width: 100%;text-align: left;" type="button" class="button" value="Comp Action" id="compensationAction" onclick="javascript:submitted('compensationAction.htm')"></td>
		    </tr>
		    
		      <tr>
		    	<td><input style="width: 100%;text-align: left;" type="button" class="button" value="Comp Reason" id="compensationActionReason" onclick="javascript:submitted('compensationActionReason.htm')"></td>
		    </tr>
		    
		      <tr>
		    	<td><input style="width: 100%;text-align: left;" type="button" class="button" value="Earning Code" id="defaultEarningCode" onclick="javascript:submitted('defaultEarningCode.htm')"></td>
		    </tr>
		    
		      <tr>
		    	<td><input style="width: 100%;text-align: left;" type="button" class="button" value="Hours Frequency" id="defaultHoursFrequency" onclick="javascript:submitted('defaultHoursFrequency.htm')"></td>
		    </tr>
		      <!--  end -->
		   
	   	</table>  
	</div>
</form>
<script type="text/javascript">
	function submitted(val){
		document.forms[0].action=val;
		document.forms[0].method = "post";
		document.forms[0].submit();
	}
	function getDetails(url){
		$('#dataTable').datagrid('options').loadMsg = 'Processing, please wait .... ';  // change to other message
		$('#dataTable').datagrid('loading');  // 
		$.ajax({
			url:url,
			type: "post",
			dataType: 'json',
			error: function(obj){
				$('#dataTable').datagrid('loaded');  // hide loading message
			},
			success: function(obj){
				$('#dataTable').datagrid('loadData',obj); 
				$('#dataTable').datagrid('loaded');  // hide loading message
			}});
	}
	function getDetailsById(url){
		loading();
		$.ajax({
			url:url,
			type: "post",
			dataType: 'json',
			error: function(obj){
			},
			success: function(obj){
				$.each(obj, function(i, item){
					if(i == 'isActive'){
						if(item == "true")
							document.getElementById('isActive').checked = true;
						else
							document.getElementById('isActive').checked = false;
					}else
						$("#"+i).val(item);  
				});
				document.getElementById("operation").value="update";
				document.getElementById("value").readOnly=true;
				closeloading();
			}});
	}
</script>
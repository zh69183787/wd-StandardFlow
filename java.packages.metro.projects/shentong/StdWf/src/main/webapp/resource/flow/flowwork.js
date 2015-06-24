
function openFlow(flowUid,flowCode,objectId,objectName,searchbtn,skipStartThread)
{
	if(flowUid==null||flowUid==''||flowUid=='null'){
		 //if(confirm("是否立即开启审核流程？")){
			 $.ajax({
			        type: "post",
			        url: "../flowwork/flowInfoAjax",
			        dataType: "json",
			        data:"flowCode="+flowCode+"&objectName="+objectName+"&objectId="+objectId+"&ts="+new Date().getTime(),
			        success: function (data) {
			        	if(skipStartThread){
			        		rollToNext(data.starThread.id);
			        	}else{
			        		alert("操作成功！");
				        	if(searchbtn!=null&&searchbtn!=''){
				        		$("#"+searchbtn).click();
				        	}
				        	
				        	tabAddFlow(data.flowProcess.flowUid);	
			        	}
			        	//$("#"+searchbtnId).click();
			              //  $("input#showTime").val(data[0].demoData);
			              //var flowThreads = 
			        },
			        error: function (XMLHttpRequest, textStatus, errorThrown) {
			                alert("请刷新该页面后，在进行操作！");
			        }
			});
		 //}
	}else{
		tabAddFlow(flowUid);
	}
}
function tabAddFlow(flowUid){
	parent.f_addTab('flowInfo'+flowUid,'流程详情','../flowwork/flowInfo?flowUid='+flowUid+"&ts="+new Date().getTime());
}
function rollToNext(threadId){
	 $.ajax({
	        type: "post",
	        url: "../flowworkThread/saveData",
	        dataType: "json",
	        data:"flowThread.operationType=0&flowThread.operationContent=提交&flowThread.id="+threadId+"&ts="+new Date().getTime(),
	        success: function (data) {
   				$.ligerDialog.alert('操作成功!', '提示',function(){
   					if(gridManager){
   						gridManager.loadData(true);						
   					}
   					if(waitDialog){
   						waitDialog.close();
   					}						
   					if(winDialog!=null){
   						winDialog.close();
   					}	        		 						
   				});  	        	
	        },
	        error: function (XMLHttpRequest, textStatus, errorThrown) {
	                alert("请刷新该页面后，在进行操作！");
	        }
	});	
}

function openFlow(flowUid,flowCode,objectId,objectName)
{
	if(flowUid==null||flowUid==''||flowUid=='null'){
		 if(confirm("是否开启审核流程？")){
			 $.ajax({
			        type: "get",
			        url: "../flowwork/flowInfoAjax?flowCode="+flowCode+"&objectName="+objectName+"&objectId="+objectId+"&ts="+new Date().getTime(),
			        dataType: "json",
			        success: function (data) {
			        	alert(data);
			        	tabAddFlow(data.flowProcess.flowUid);
			        	
			        	//$("#"+searchbtnId).click();
			              //  $("input#showTime").val(data[0].demoData);
			              //var flowThreads = 
			        },
			        error: function (XMLHttpRequest, textStatus, errorThrown) {
			                alert("请刷新该页面后，在进行操作！");
			        }
			});
		 }
	}else{
		tabAddFlow(flowUid);
	}
}
function tabAddFlow(flowUid){
	parent.f_addTab('flowInfo'+flowUid,'流程详情','../flowwork/flowInfo?flowUid='+flowUid+"&ts="+new Date().getTime());
}
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<link href="../resource/lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" /> 
 
<script src="../resource/lib/jquery/jquery-1.7.1.min.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/core/base.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerForm.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerDateEditor.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerComboBox.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerTree.js" type="text/javascript"></script>

<script src="../resource/lib/ligerUI/js/plugins/ligerButton.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerSpinner.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerTextBox.js" type="text/javascript"></script> 
<script src="../resource/lib/ligerUI/js/plugins/ligerTip.js" type="text/javascript"></script>
<script src="../resource/lib/jquery-validation/jquery.validate.min.js" type="text/javascript"></script> 
<script src="../resource/lib/jquery-validation/jquery.metadata.js" type="text/javascript"></script>
<script src="../resource/lib/jquery-validation/messages_cn.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/jquery.form.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerMessageBox.js" type="text/javascript"></script>
<script src="../resource/lib/jquery.jqprint.js" type="text/javascript"></script> 
<script src="../resource/lib/jquery.jqprint.js" type="text/javascript"></script>

<link href="../resource/flow/css/info.css" rel="stylesheet" type="text/css" /> 
<link href="../resource/lib/jquery-jsonp/jquery.jsonp.js" rel="stylesheet" media=print type="text/css" /> 
<script type="text/javascript">
var nextOneId = null, nextOneAltCode = null;
$(function (){
	if(parent != null && parent.reloadTabid != null){
	}else{
		$('#print').attr('style', 'display: none');
	}
	$("input[name='flowThread.operationType']").click(function(){
		$("#operationContent").val($(this).next("font").text());
	});
	
  	$(".fn_inputbg").bind("click",clickadduser);
    //addusers();
    

/* 	 $("#print").ligerButton({ text : '打印',width:40,
	    	click: function () {
	    		$("#printdiv").jqprint(); 
		    }
	}); */
	 $("#save_add").ligerButton({ text : '提交',width:40});
	 
	
	/*  $("img").each(function(){
		 alert($(this).fileSize);
         if(this.fileSize<=0){
        	 this.src="dddddddddddddddddd";
         }
            
     }); */
	 
	 $("textarea").eq(0).focus();
     
    // $(".fn_main").mousemove(function(e){
    	 $(".attachGroupDetail iframe").each(function(){
        	 
        	 var src = $(this).attr("srch");
        	 if(src!=null&&src!=''){
        		 $(this).attr("src",src);
        		 $(this).attr("srch","");
        	 }
         });
    	 $(".fn_main").unbind("mousemove");
    // });
     
    $(".nc_sign").each(function(){
    	var imgname= $(this).attr("imgname");
    	loadImg(imgname,$(this));
    });
    
    
    showBut();
});


//删除后处理
function afterDel(objid,treads){
	var oindex = $("#fn_thread_"+objid).attr("oindex");
	
	var nodeName = $("#fn_thread_"+objid).attr("nodeName");
	var len = $(".fn_mcon[oindex='"+oindex+"']").length;
	var lastId = objid;
	if(len==1){//标识 该节点没有并行的其他节点
		var flag = false;
		$(".fn_mcon").each(function(){
			if(flag){$(this).remove();}
			if($(this).attr("id")=="fn_thread_"+objid){flag = true;}
		});
		for(var i=0;i<treads.length;i++){
			afterHtml(lastId,treads[i],nodeName);
			lastId = treads[i].id;
			nodeName = treads[i].nodeName;
		}
	}
	$("#fn_thread_"+objid).remove();
	
	$(".fn_inputbg").unbind("click",clickadduser).bind("click",clickadduser);
}
//新增后处理
function afterAdd(objid,treads){
	var oindex = $("#fn_thread_"+objid).attr("oindex");
	
	var nodeName = $("#fn_thread_"+objid).attr("nodeName");
	var len = $(".fn_mcon[oindex='"+oindex+"']").length;
	var lastId = objid;
	var flag = false;
	if(len==1){//标识 该节点没有并行的其他节点
		$(".fn_mcon").each(function(){
			if(flag){$(this).remove();}
			if($(this).attr("id")=="fn_thread_"+objid){flag = true;}
		});
		for(var i=0;i<treads.length;i++){
			afterHtml(lastId,treads[i],nodeName);
			lastId = treads[i].id;
			nodeName = treads[i].nodeName;
		}
	}else{
		var obj = $(".fn_mcon[oindex='"+oindex+"']").last();
		lastId = ($(obj).attr("id")).split("_thread_")[1];
		$(".fn_mcon").each(function(){
			if(flag){$(this).remove();}
			if($(this).attr("id")=="fn_thread_"+lastId){flag = true;}
		});
		for(var i=0;i<treads.length;i++){
			afterHtml(lastId,treads[i],nodeName);
			lastId = treads[i].id;
			nodeName = treads[i].nodeName;
		}
 	}
	//$("#fn_thread_"+objid).remove();
	$(".fn_inputbg").unbind("click",clickadduser).bind("click",clickadduser);
	//$(".fn_inputbg").bind("click",clickadduser);
}
//新增后处理并行
function afterAddBing(objid,treads){
	var oindex = $("#fn_thread_"+objid).attr("oindex");
	
	var nodeName = $("#fn_thread_"+objid).attr("nodeName");
	var lastId = objid;
		var obj = $(".fn_mcon[oindex='"+oindex+"']").last();
		lastId = ($(obj).attr("id")).split("_thread_")[1];
		for(var i=0;i<treads.length;i++){
			afterHtml(lastId,treads[i],nodeName);
			lastId = treads[i].id;
			nodeName = treads[i].nodeName;
		}
	//$("#fn_thread_"+objid).remove();
	$(".fn_inputbg").unbind("click",clickadduser).bind("click",clickadduser);
	//$(".fn_inputbg").bind("click",clickadduser);
}
//更改操作人
function afterReplace(objid,treads){
	$("#fn_thread_"+objid+" .fn_name").text($("#fn_thread_"+objid+" #addUsers").val());
	$(".fn_inputbg").unbind("click",clickadduser).bind("click",clickadduser);
}
//在对象后添加 人员处理信息
function afterHtml(objid,tread,nodeName){
	//{thread.defOrderIndex+1}位处理人第${thread.loopIndex+1}
	var lineHtml="";
	if(nodeName!=tread.nodeName&&tread.nodeName!=''&&tread.nodeName!=null){
		lineHtml="<div class='fn_mcon'>"+tread.nodeName+"</div>";
	}
	
	var dindex = tread.orderIndex*1+1;
	//var lindex = tread.loopIndex*1+1;
	var html =lineHtml+"<div  class='fn_mcon ' nodename='"+tread.nodeName+"' id='fn_thread_"+tread.id+"' oindex='"+tread.orderIndex+"' dindex='"+tread.defOrderIndex+"' state='"+tread.state+"' >"+
	"<form class='formflowDeal"+tread.id+"' id='formflowDeal"+tread.id+"' name='formflowDeal"+tread.id+"'>"+
	"<ul class='fn_start' >"+
	"	<li title='第"+dindex+"位处理人' class='fn_num fn_unlight'>"+dindex+"</li>"+
	"	<li class='fn_name'>"+tread.userName+"</li>"+
	"	<input type='hidden' value='"+tread.id+"' name='threadId'>"+
	"	<input type='hidden' value='0' id='addFlag"+tread.id+"' name='addFlag'>"+
	"	<input type='hidden' name='addNodeName' id='nodeName"+tread.id+"' value='"+tread.nodeName+"'/>"+
	"	<li class='fn_del noprint fn_hid'><a href='javascript:dealForm(2,"+tread.id+");'>删除</a></li>"+
	"	<li class='fn_inputbg noprint fn_hid' ></li>"+
	"	<li class='noprint fn_hid'>&nbsp;"+
	"		<a class='fn_addc' href='javascript:dealForm(0,"+tread.id+");' >串行添加</a>&nbsp;&nbsp;"+
	"		<a class='fn_addb' href='javascript:dealForm(1,"+tread.id+");' >并行添加</a>"+
	"	</li>"+
	"</ul>"+
	"</form>"+
	"<ul class='fn_con'></ul>"+
	"<ul class='fn_fin'>"+
	"	<li class='fn_state fn_unlight'>"+tread.stateStr+"</li>"+
	"	<li class='fn_time fn_unlight'></li>"+
	"	<li class='fn_unlight'>-</li>"+
	"	<li class='fn_time fn_unlight'>"+(tread.startTime==null?'':tread.startTime)+"</li>"+
	"	<li class='fn_info fn_unlight'></li>"+
	"</ul>"+
	"</div>";
	$("#fn_thread_"+objid).after(html);
	
}

function clickadduser(){
	  addusers(nextOneAltCode);
	  accountManager.selectBox.hide();
	  accountManager.boxToggling = false;
	  $("#defAddUser").appendTo($(this));
	  accountManager._toggleSelectBox(false);
	 // $(".fn_inputbg").bind("click",clickadduser);
	  //$(this).unbind("click",clickadduser);
}
var winDialog = null;
function dealFlow(conId){
	openDialog(conId,$(".f_title").eq(0).text(),600,300);
}

//弹出相关页面
function openDialog(id,title,h,w){
	h==null?300:h;
	w==null?600:w;
	$.ligerDialog.open({ target: $("#"+id),title :title,height:300,width:600 });

}
//保存简报
function saveForm(threadid){
	if(nextOneId){
		var operationType = $("input[name='flowThread.operationType']:checked").val();
		if(operationType == '0'){
			var nextHandler = $("#fn_thread_"+nextOneId+" .fn_name").text();
			if(!nextHandler){
				$.ligerDialog.alert('请选择接收人!', '提示',function(){});
				return false;
			}			
		}
	}
	$("#pageloading").show();
	 var options = {
		cache:false,
		type:'post',
		url:'../flowworkThread/saveData',
		beforeSubmit:function(){waitDialog = $.ligerDialog.waitting('正在操作中,请稍候...');},
		dataType: 'text',
		success:function(data){
			if(data) {
				var tempData = eval("(" + data + ")");
				$("#pageloading").hide();
				//$.ligerMessageBox.alert('提示', '保存成功');
				waitDialog.close();
				if(tempData.success){
					$.ligerDialog.alert('操作成功!', '提示',function(){
					});
				}else{
					$.ligerMessageBox.error('提示', tempData.message);
				}
				
			} else {
				$.ligerMessageBox.error('提示', '操作失败！');
			}
			
			
			
			reloadtab($("#flowUid").val());//parent.reloadTabid("flowInfo"+$("#flowUid").val());
			//waitDialog.close();
			return false;
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
        	$("#pageloading").hide();
        	waitDialog.close();
        	$.ligerDialog.error("操作失败,请刷新后操作!", '提示',function(){
        		reloadtab($("#flowUid").val());
        	});
    	}
	};
	 	
	$.ligerDialog.confirm("您确定要保存数据？",function (r){ 
		if(r){
			$("#formflow_"+threadid).ajaxSubmit(options);
		}
	});
};

function addTask(){
	$.jsonp({
	    url: '${converterUrl}',			//替换成要访问的跨域路径 !!!
	    data: inData,
	    callbackParameter: "callback",
	    success: function (json) {
	    	$("#loading").hide();
	        if(json.IsSuccess == 'true'){
	        	loadSwf();
	        }else{
	        	alert('文件编码有误，无法转化，请联系开发人员。您可以先点击下载按钮，直接下载源文件进行查看');
	        	window.close();
	        }
	    },
	    error: function (xOptions, textStatus) {
	    	$("#loading").hide();
	    	alert('文件编码有误，无法转化，请联系开发人员！您可以先点击下载按钮，直接下载源文件进行查看');
	    	window.close();
	    }
	});
}
// 提交
function mySubmit(threadid){
	if($("#formflow_"+threadid).valid()){
		var errorHtml = getErrorHtml("formflow_"+threadid);
		//alert($("textarea").eq(0).val());
		if(errorHtml==""){
			saveForm(threadid);
		}else{
			alert(errorHtml);
		}
		
	}
}

function reloadtab(flowUid){
	/* parent.reloadTabid("NC_HOME_Home");
	if(parent.tab.isTabItemExist("NC_FLOWWORK_DEAL")){
		parent.reloadTabid("NC_FLOWWORK_DEAL");
	} */
	if(parent.tab && parent.tab.isTabItemExist("flowInfo"+flowUid)){
		parent.reloadTabid("flowInfo"+flowUid);
	}else{
		window.location.reload();
	}
	
}


var accountManager = null;
//添加下一步处理人
function addusers(nextOneAltCode){
	accountManager =$("#addUsers").ligerComboBox({
	        width: 120,
	        selectBoxWidth: 120,
	        selectBoxHeight: 100, textField: 'name',
	        tree: { 
	        	nodeWidth: 70,
	        	url: '../index/findAllAccountByRole/'+nextOneAltCode,
	        	checkbox: false,
	        	textFieldName: 'name',
	            onBeforeSelect: function(node){
	            	if(node.data.children) return;
	            	$('#addUserIds').val(node.data.id);
	            },
	            onBeforeCancelSelect: function(node){
	            	$('#addUserIds').val('');
	            },  
	            onAfterAppend: function (parentNode, newdata){},
	            onAppend: function(parentNode, newdata){},
	        	onBeforeAppend:  function(parentNode, data){
	            	for(var i = 0; i < data.length; i++){
	            		var o = data[i];
	            		if(o.nodetype){   //部门属性
	                		o.children = [];
	                		o.isexpand = false;    			
	            		}
	            	}
	            	return true;
	            } , 
	        	onBeforeExpand: function(note){
	            	if (note.data.children && note.data.children.length == 0){
	            		accountManager.treeManager.loadData(note.target,'../index/findSubAllOfGroupByCode/'+note.data.groupcode);	 // edit
	                }
	            },
	        	onClick:  function(node){
	            	if(node.data.children) return;
	            	accountManager._toggleSelectBox(true);            // edit
	            }
	        }
	    });
}


function getErrorHtml(formID){
	var errorHtml = "";
	
	$("#"+formID).find("textarea[isv]").each(function(){
		var len = $(this).attr("len")*1;
		var vallen = $(this).val().length;
		var tit = $(this).attr("titles");
		if(vallen>len){
			errorHtml += tit+"过长("+vallen+"/"+len+"个字符)。";
		}
	});

	return errorHtml;
}

function dealForm(flag,objid){
	
	if(!$("#formflowDeal"+objid+" #addUsers_val").val()){
		$.ligerDialog.alert('请先在下拉框中选择接收人');
		return;
	}
	
	var dealurl = "";
	var mes = "删除";
	if(flag=='0'||flag=='1'){//串行添加
		dealurl = "../flowworkThread/addUsers";
		mes = "新增";
		$("#addFlag"+objid).val(flag);
		
		/* var nextoindex = $("#fn_thread_"+objid).attr("oindex")*1+1;
		if($("#fn_thread_"+objid).attr("dindex")=='0'){//发起人 
			var nextNodeName = $(".fn_mcon[oindex='"+nextoindex+"']").eq(0).attr("nodeName");
			$("#nodeName"+objid).val(nextNodeName);
		}  */
		
	}else if(flag=='2'){//删除
		dealurl = "../flowworkThread/del";
	}else if(flag=='3'){//替换
		mes = "选择该";
		dealurl = "../flowworkThread/replace";
	}
	 var options = {
		cache:false,
		type:'post',
		url:dealurl,
		beforeSubmit:function(){$("#pageloading").show();},
		dataType: 'json',
		success:function(tempData){
			//var tempData = data;//eval("(" + data + ")");
			//alert(data);
        	$("#pageloading").hide();
        	if(tempData.success){
        		$.ligerDialog.alert(tempData.message, '提示',function(){
        			//reloadtab($("#flowUid").val());
        			if(flag=='0'){//串行添加
        				afterAdd(objid,tempData.threads);
        			}else if(flag=='1'){//并行
        				afterAddBing(objid,tempData.threads);
        			}else if(flag=='2'){//删除
        				afterDel(objid,tempData.threads);
        			}else if(flag=='3'){//替换
        				afterReplace(objid,tempData.threads);
        			}
        			 showBut();
				});
        		 
        	}else{
        		$.ligerDialog.error(tempData.message, '提示',function(){
				});
        	}
			return false;
		},
		error: function (XMLHttpRequest, textStatus, errorThrown) {
        	$("#pageloading").hide();
        	$.ligerDialog.error("操作失败,请刷新后操作!", '提示',function(){
        		reloadtab($("#flowUid").val());
        	});
    	}
	};
	
	$.ligerDialog.confirm("您确定要"+mes+"联系人？",function (r){ 
		if(r){
			//alert(flag+"----"+$("#formflowDeal"+objid).html());
			$("#formflowDeal"+objid).ajaxSubmit(options); 
		}
	});
}


function loadImg(imgname,obj){
	var isrc = "../resource/sign/"+imgname;
	$.ajax({
        type: "get",
        url: isrc,
        contentType:"text/html;charset=UTF-8",
        //dataType: "text",//http://localhost/contract-manage/resource/sign/1.png
       // data:"threadId="+objectId+"&orderIndex="+orderIndex+"&flowUid="+flowUid+"&ts="+(new Date().getTime()),
        success: function (data) {
        	  $(obj).html("<img src='"+isrc+"' />");
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        	//alert(0);
        }
	});
}

function showBut(){
	if($("#showBut").val()=="true"){
		$(".fn_mcon").each(function(){
			var state = $(this).attr("state");
			if(state!='2'){
				$(this).find(".noprint").show();
				if($(this).find(".fn_deal").length>0){
					$(this).find(".fn_del").hide();
					$(this).find(".fn_addb").hide();
				}
			}
		});
	}
}
function openDetail(type, id){
	id = id*1;
	switch(type*1){
		case 0:
			parent.f_addTab('in_'+id,'招标需求表申报条件明细','../inviteNeeds/add?editable=false&id='+id+'&ts='+new Date().getTime());
			break;
		default:break;
	}
	
}
</script>

<style type="text/css">
.nc_sign{position: relative;width: 90px;height: 25px; float: right;}
.nc_sign img{position: absolute;top:-20px;right:0px;}
</style>
<style type="text/css" media=print>

</style>
</head>
<body style="height: 100%;">
<input type="hidden" id="flowUid" value="${map.flowProcess.flowUid}"/>
<center>
<div id="printdiv">
<table  cellpadding="0" cellspacing="0" border="0" class="fn_main">
	<tr>
		<td align="right" colspan="4"><div class="fn_no">流程编号：${map.flowProcess.flowNo}</div></td>
	</tr>
	<tr>
		<td align="center" colspan="4"><div class="fn_title">${map.flowProcess.flowTypeName}</div></td>
	</tr>
	<%-- <tr><td colspan="4" class="fn_line">基本信息</td></tr>
	<tr>
		<td class="fn_label">流程简述：</td>
		<td colspan="3">${map.flowProcess.flowDescription}</td>
	</tr>
 	<tr>
		<td class="fn_label">发&nbsp;起&nbsp;人：</td>
		<td width="300">${map.flowProcess.userName}</td>
		<td class="fn_label">流程状态：</td>
		<td width="300">${map.flowProcess.getStateStr()}</td>
	</tr>
	<tr>
		<td class="fn_label">发起时间：</td>
		<td>${map.flowProcess.startTime}</td>
		<td class="fn_label">结束时间：</td>
		<td>${map.flowProcess.endTime}</td>
	</tr>
	 --%>
	<c:if test="${map.flowProcess.linkStr!=''&&map.flowProcess.linkStr!=null&&map.flowProcess.objectId!=''&&map.flowProcess.objectId!=null}" >
<%-- 	<tr><td colspan="4" class="fn_line">内容摘要-${map.flowProcess.flowName}</td></tr>--%>	
	<tr class="">
		<td colspan="4" style="padding: 0px;">
			<iframe id="publicIframe" style="display:;border: 0px solid #A3C0E8;" width="100%" height="300" frameborder="0"  src="${map.flowProcess.linkStr}${map.flowProcess.objectId}"></iframe>
		</td>
	</tr>
	</c:if>
	
	<tr><td colspan="4" class="fn_line">审核处理意见
		<div style="display:none;">
			<div id="defAddUser" style="margin-top: 1px;margin-left: 0px;">
				<input id="addUserIds" name="userIds" value="" type="hidden" />
				<input id="addUsers" name="userNames" type="text" value="" />
			</div>
		</div>
	</td></tr>
	<tr><td colspan="4">
		<c:set var="showBut" value="false"/>
		<c:set var="nodename" value=""/>
		<c:forEach items="${map.flowThreads}" var="thread" begin='0' varStatus="status" >
		<c:if test="${nodename!=thread.nodeName}">
			<div class="fn_mcon" >${thread.nodeName}</div>
			<c:set var="nodename" value="${thread.nodeName}"/>
		</c:if>
		<div class="fn_mcon " id="fn_thread_${thread.id}" nodeName="${thread.nodeName}" oindex="${thread.orderIndex}" dindex="${thread.defOrderIndex}" state="${thread.state}">
			
			<form name="formflowDeal${thread.id}" id="formflowDeal${thread.id}" class="formflowDeal${thread.id}" >
			<ul class="fn_start fn_mod_${thread.id}" >
				<li class="fn_num fn_unlight" title="第${thread.orderIndex+1}位处理人">${thread.orderIndex+1}</li>
				<li class="fn_name">${thread.nodeName ne '标准化室一审' ? thread.userName : '标准化室'}</li>
				<c:if test="${thread.userId==sessionScope.SEC_CUR_ACCOUNT_ID&&thread.state==1}">  
					<c:set var="nextOneIndex" value="${thread.orderIndex + 1}"/>		
<%-- 				<input name="threadId" type="hidden" value="${thread.id}" />
				  	<input name="addFlag" type="hidden" id="addFlag${thread.id}" value="0" />
					<li  class="fn_inputbg noprint"></li>
					<li class="noprint">&nbsp;
						<a href="javascript:dealForm('0','${thread.id}');" >串行添加</a>&nbsp;&nbsp;
						<c:if test="${thread.userId!=map.flowProcess.userId}">
						<a href="javascript:dealForm('1','${thread.id}');" >并行添加</a>
						</c:if>
					</li> --%>
				</c:if>
				<c:if test="${thread.state==0 && nextOneIndex != null && thread.orderIndex eq nextOneIndex}">
					<script>
						nextOneId = '${thread.id}';
					</script>
	 				<c:if test="${not empty thread.altAuthCode}">
						<script>
							nextOneAltCode = '${thread.altAuthCode}';
						</script>	 				
						<input type="hidden" name="addNodeName" id="nodeName${thread.id}" value="${thread.nodeName}"/>
					  	<input name="threadId" type="hidden" value="${thread.id}" />
					  	<input name="addFlag" type="hidden" id="addFlag${thread.id}" value="0" />
						<%-- <li class="fn_del noprint fn_hid"><a href="javascript:dealForm('2','${thread.id}');">删除</a></li> --%>
						<li  class="fn_inputbg noprint fn_hid"></li>
						<li class="noprint fn_hid">&nbsp;
	<%-- 						<a class="fn_addc " href="javascript:dealForm('0','${thread.id}');" >串行添加</a>&nbsp;&nbsp;
							<a class="fn_addb " href="javascript:dealForm('1','${thread.id}');" >并行添加</a> --%>
							<a class="fn_addb " href="javascript:dealForm('3','${thread.id}');" >选择接收人</a>
						</li>
					</c:if>					
				</c:if>
			</ul>
			</form>
		    <c:choose> 
		      <c:when test="${thread.userId==sessionScope.SEC_CUR_ACCOUNT_ID&&thread.state==1}">   
		      	<c:set var="showBut" value="true"/>
		      	<form name="formflow" id="formflow_${thread.id}" >
		        <ul class="fn_deal noprint">
					<li>
						<div class="fn_hid"><input type="hidden" name="flowThread.operationContent" id="operationContent" value="同意"/></div>
						<div class="${thread.userId==map.flowProcess.userId&&thread.defOrderIndex=='0'?'fn_hid':''}"><input type="radio" value="0" checked="checked" name="flowThread.operationType" /><font>同意</font>
						<c:choose>
						<c:when test="${fn:contains(map.flowProcess.flowDescription,'back')}">
							<input type="radio" value="5" name="flowThread.operationType" /><font>退回发起人</font>
						</c:when>
						<c:otherwise>
							<input type="radio" value="3" name="flowThread.operationType" /><font>不同意</font>
						</c:otherwise>
						</c:choose>
						</div>
						
					</li>
					<c:if test="${fn:contains(map.flowProcess.flowDescription,'remark')}">
	 					<li>
							<textarea isv="0"
								cols="150" rows="${thread.orderIndex==0?8:4}" class="l-textarea"
								style="resize:none; width: 99%" 
								name="flowThread.contents" id="thread_contents"
								titles="内容" len="1000"></textarea>
						</li>
					</c:if>
					<%--
					<li>
						<div  id="fn_inputbg_${thread.userId}" class="fn_inputbg fn_fl" ></div>
						<div class="fn_fl">
							<input type="button" value="串行添加" onclick="adduserbyindex('${thread.orderIndex}','${thread.userId}','${thread.id}','${thread.loopIndex}','${thread.defOrderIndex}','0','${thread.nodeName}');" />
							<input type="button" value="并行添加" onclick="adduserbyindex('${thread.orderIndex}','${thread.userId}','${thread.id}','${thread.loopIndex}','${thread.defOrderIndex}','1','${thread.nodeName}');" />
						</div>
					--%>
					<li>
						<input type="hidden" name="flowThread.attachGroup" id="attachGroup${thread.id}" value="${thread.attachGroup}"/>
						<iframe frameborder="0" autoheight="true" style="height: 35px;" id="attachGroup${thread.id}Iframe" width="99%" scrolling="no" src="../attachment/findByGroup?group=${thread.attachGroup }&dest_code=attachGroup${thread.id}&operation=modify&ts=${nowtime}"  >
						</iframe>
					</li> 
					<li>
						<input type="hidden" name="flowThread.id" value="${thread.id}"/>
						<div style="text-align: center; margin: 10px;margin-top: 0px;padding-left: 335px;">
							<div style="text-align:left;margin-bottom:-25px; color: #999;padding-left: 80px;">提示：请先填写处理意见后，再提交。</div>
							<div id="save_add" onclick="mySubmit('${thread.id}')" ></div>
						</div>
						
					</li>
				</ul>
				</form>
		      </c:when> 
		      <c:otherwise>   
		      	<ul class="fn_con">${thread.contents}</ul>
				<c:if test="${thread.attachGroup!=''&&thread.attachGroup!=null}">
		      	<ul id="attachGroupDetail${thread.id}hid" class="fn_con attachGroupDetail">附件：
		      		<input type="hidden" name="attachGroupDetail" id="attachGroupDetail${thread.id}" value="${thread.attachGroup }"/>
					<iframe frameborder="0" autoheight="true" style="height: 15px;" id="attachGroupDetail${thread.id}Iframe" width="99%" scrolling="no" src="" srch="../attachment/findByGroup?group=${thread.attachGroup }&dest_code=attachGroupDetail${thread.id}&operation=&ishid=1&ts=${nowtime}"  >
						</iframe>
				</ul>
				</c:if>
				<ul class="fn_fin">
					<c:if test="${thread.state=='2'}">
							<%-- <li class="nc_sign" imgname="${thread.userId}.png" ></li> --%>
					</c:if>
					<li class="fn_state fn_unlight">${thread.stateStr}</li>
					<li class="fn_time fn_unlight">${thread.endTime}</li>
					<li class="fn_unlight">-</li>
					<li class="fn_time fn_unlight">${thread.startTime}</li>
					<li class="fn_info fn_unlight">${thread.operationContent}</li>
				</ul>
		      </c:otherwise> 
		    </c:choose> 
		</div>
		</c:forEach>
		<input type="hidden" name="showBut" value="${showBut}" id="showBut"/>
	</td></tr>
	
</table>
</div>
<!-- 
<div id="print" style="margin: 20px;" class="noprint" ><a href='javascript:$("#printdiv").jqprint();'>打印审核意见</a></div>
 -->
    <div id="print" style="margin: 20px;" class="noprint" ><a href='#' target="_blank">打印审核意见</a></div>
</center>

</body>
</html>




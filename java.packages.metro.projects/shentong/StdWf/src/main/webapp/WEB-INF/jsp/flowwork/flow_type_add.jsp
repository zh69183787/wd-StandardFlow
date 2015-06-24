<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<link href="../resource/lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" /> 
<link href="../resource/lib/ligerUI/skins/Gray/css/all.css" rel="stylesheet" type="text/css" /> 
<script src="../resource/lib/jquery/jquery-1.3.2.min.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/core/base.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerForm.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerDateEditor.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerComboBox.js" type="text/javascript"></script>
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
<script src="../resource/lib/ligerUI/js/plugins/ligerTree.js" type="text/javascript"></script>
<link href="../resource/flow/css/info.css" rel="stylesheet" type="text/css" /> 


  
<script type="text/javascript">
var accountManager = null;

$(function (){
	addusers();
     $.validator.addMethod(
            "notnull",
            function (value, element, regexp)
            {
                if (!value) return true;
                return !$(element).hasClass("l-text-field-null");
            },
            "不能为空"
    );

    $.metadata.setType("attr", "validate");
    var v = $("#form1").validate({
        //调试状态，不会提交数据的
        debug: true,
        errorPlacement: function (lable, element){
            if (element.hasClass("l-textarea")) {
                element.addClass("l-textarea-invalid");
            } else if (element.hasClass("l-text-field")) {
                element.parent().addClass("l-text-invalid");
            };
            $(element).removeAttr("title").ligerHideTip();
            $(element).attr("title", lable.html()).ligerTip();
        },
        success: function (lable) {
            var element = $("#" + lable.attr("for"));
            if (element.hasClass("l-textarea")) {
                element.removeClass("l-textarea-invalid");
            } else if (element.hasClass("l-text-field")) {
                element.parent().removeClass("l-text-invalid");
            };
            $(element).removeAttr("title").ligerHideTip();
        },
        submitHandler: function (){
            alert("Submitted!");
        }
    });
    $("#form1").ligerForm();
    
    $("input[type='text']").eq(0).focus(); // 解决ie8下  打开默认不能编辑问题
    
});
var waitDialog ;
// 保存简报
function saveForm(){
	 var options = {
		cache:false,
		type:'post',
		url:'saveData',
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
						//$("#searchbtn",window.parent.document).click();
						$("#searchbtn",window.parent.document).click();
						parent.winDialog.close();
					});
				}else{
					$.ligerMessageBox.alert('提示', tempData.message);
				}
				
				
			} else {
				$.ligerMessageBox.alert('提示', '操作失败！');
			}
			//waitDialog.close();
			return false;
		}
	};
	
	$.ligerDialog.confirm("您确定要保存数据？",function (r){ 
		if(r){
			setAddUser();
			$("#form1").ajaxSubmit(options);
		}
	});
};

//添加下一步处理人
function addusers(){

	accountManager =$("#addUsers").ligerComboBox({
	        width: 180,
	        selectBoxWidth: 180,
	        selectBoxHeight: 200, textField: 'name',
	        tree: { 
	        	url: '../index/findSubAllOfGroupByCode/ST_METRO',
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
	            	accountManager._toggleSelectBox(true);             // edit
	            }
	        }
	    });
		//accountManager = $("#addUsers").ligerGetComboBoxManager();
		$("#defAddUser").appendTo("#adduserstd");
		
		/* $("#addUsers").change(function(){
	    	 var notes = accountManager.treeManager.getSelected();
	         var text = "";
	         var ids = "";
	         for (var i = 0; i < notes.length; i++){
	        	 if(notes[i].data.group&&notes[i].data.group!=null){
	        		 text += notes[i].data.name + ",";
		             ids += notes[i].data.id + ",";
		             var str = "<div dataid='"+notes[i].data.id+"'>"+
		             	"<ul>"+i+"</ul><ul>"+notes[i].data.name+"</ul></div>";
		             $("#adduserslist").append(str);
	        	 }
	         }
	        // alert('选择的节点数：'+text+":" + ids);
	    }); */
}

// 提交
function mySubmit(){
	if($("#form1").valid()){
		
		saveForm();
	}
}
function setAddUser(){
	var ids ="";
	var names="";
	var inds ="";
	var nnames ="";
	var alts = "";
	$("#adduserslist .userlist").each(function(){
		ids += $(this).attr("dataid")+",";
		names +=$(this).find(".fu_name").eq(0).text()+",";
		inds +=$(this).find(".fu_index").eq(0).text()+",";
		nnames +=$(this).find(".fu_nname").eq(0).text()+",";
		alts +=$(this).find(".fu_alt").eq(0).text()+",";
	});
	$("#userIds").val(ids.substr(0, ids.length-1));
	$("#userNames").val(names.substr(0, names.length-1));
	$("#userIndexs").val(inds.substr(0, inds.length-1));
	$("#userNodeNames").val(nnames.substr(0, nnames.length-1));
	$("#userAlts").val(alts.substr(0, alts.length-1));
}

function addUserTo(obj,flag){
	
	var maxi = $("#maxindex").val()*1;
	if(maxi==0){
		maxi = maxi+1;
		$("#maxindex").val(maxi);
	}else{
		if(flag==0){
			maxi = maxi+1;
			$("#maxindex").val(maxi);
		}
	}
	
	var uid = $("#addUserIds").val();
	var uname = $("#addUsers").val();
	var unname = $("#addUserNodeNames").val();
	var altAuth = $("#addUserAltAuth").val();
	
	var str = "<div class='userlist' dataid='"+uid+"'>"+
	"<ul class='fu_index' >"+maxi+"</ul>"+
	"<ul class='fu_nname'>"+unname+"</ul>"+
	"<ul class='fu_name'>"+uname+"</ul>"+
	"<ul class='fu_alt'>"+altAuth+"</ul>"+
	"<ul class='fu_del' onclick='removeUser(this)'>删除</ul></div>";
	
	$("#adduserslist").append(str);
	return false;
}

function removeUser(obj){
	//console.log($("#adduserslist .fu_index_"+rownum).length);
	var rownum = $(obj).parent("div").find(".fu_index").eq(0).text()*1;
	$(obj).parent("div").remove();
	var flag = true;
	$("#adduserslist .userlist").each(function(){
		var ind = $(this).find(".fu_index").eq(0).text()*1;
		//console.log(ind);
		if(ind==rownum){
			flag=false;
		}
	});
	
	if(flag){
		
		$("#maxindex").val($("#maxindex").val()*1-1);
		$("#adduserslist .userlist").each(function(){
			var ind = $(this).find(".fu_index").eq(0).text()*1;
			//console.log(ind);
			if(ind>rownum){
				$(this).find(".fu_index").eq(0).text(ind-1);
			}
		});
	}
	return false;
	//var index = $(obj).parent("div").find(".fu_index").eq(0).text();
	
	
}

</script>

<style type="text/css">
#form1 table{margin-top: 20px;}
#form1 tr{height: 34px; line-height: 32px;}
</style>
</head>
<body style="height: 100%">
<div style="display:none;">
	<div id="defAddUser" style="margin-top: 2px;margin-left: 1px;">
		<input id="addUserIds" name="addUserIds" value="" type="hidden" />
		<input id="addUsers" name="addUsers" type="text" value="" />
	</div>
</div>
<form name="form1" id="form1" >
<input type="hidden"  name="flowType.id" id="id" value="${flowType.id}"/>
<input type="hidden"  name="oldFlowTypeName" id="oldFlowTypeName" value="${flowType.flowTypeName}"/>
<input type="hidden"  name="oldFlowTypeCode" id="oldFlowTypeCode" value="${flowType.flowTypeCode}"/>

<input id="nothing" name="nothing" type="hidden" />

<input id="userIds" name="userIds" type="hidden" value="" />
<input id="userNames" name="userNames" type="hidden" value="" />
<input id="userNodeNames" name="userNodeNames" type="hidden" value="" />
<input id="userIndexs" name="userIndexs" type="hidden" value="" />
<input id="userAlts" name="userAlts" type="hidden" value="" />

<table  cellpadding="0" cellspacing="0" border="0">
	<tr>
		<td align="right" width="135">流程类型名称：</td>
		<td colspan="3"><input type="text" name="flowType.flowTypeName" id="flowTypeName" value="${flowType.flowTypeName}" ltype="text" style="width: 150px;" validate="{required:true,notnull:true,maxlength:50,minlength:1}" /></td>
		
	</tr>
	<tr>
		<td align="right" >是否马上启用：</td>
		<td>
			<select name="flowType.state" ltype="select" id="state" style="width: 150px">
				<option value="0" ${flowType.state=='0'?'selected':''}>启用</option>
				<option value="1" ${flowType.state=='1'?'selected':''}>不启用</option>
            </select>
		</td>
		<td align="right" width="135">流程类型编码：</td>
		<td><input type="text" name="flowType.flowTypeCode" id="flowTypeCode" value="${flowType.flowTypeCode}" ltype="text" style="width: 150px;" validate="{required:true,notnull:true,maxlength:50,minlength:3}" /></td>
	</tr>
	<tr>
		<td align="right">关联类名：</td>
		<td width="135"><input type="text" name="flowType.objectClass" id="objectClass" value="${flowType.objectClass}" ltype="text" style="width: 150px;" validate="{required:true,notnull:true,maxlength:200,minlength:20}" /></td>
		<td align="right" width="135">执行类名：</td>
		<td><input type="text" name="flowType.objectMethodClass" id="objectMethodClass" value="${flowType.objectMethodClass}" ltype="text" style="width: 150px;" validate="{maxlength:200,minlength:20}" /></td>
	</tr>
	<tr>
		<td align="right">执行类方法名：</td>
		<td width="135"><input type="text" name="flowType.objectMethod" id="objectMethod" value="${flowType.objectMethod}" ltype="text" style="width: 150px;" validate="{maxlength:100,minlength:2}" /></td>
		<td align="right" width="135">执行类方法参数：</td>
		<td><input type="text" name="flowType.paramVals" id="paramVals" value="${flowType.paramVals}" ltype="text" style="width: 150px;" validate="{maxlength:500}" /></td>
	</tr>
	<tr>
		<td align="right" width="135">关联对象访问链接：</td>
		<td colspan="3"><input type="text" name="flowType.linkStr" id="linkStr" value="${flowType.linkStr}" ltype="text" style="width: 350px;" validate="{required:true,notnull:true,maxlength:500,minlength:1}" /></td>
	</tr>                                                                                                                                                                                                                                                         
	
	<tr>
		<td align="right">选择执行人：</td>
		<td ><input type="text" name="nodename" id="addUserNodeNames" value="" ltype="text" style="width: 120px;" validate="{maxlength:100,minlength:2}" /></td>
		<td id="adduserstd" >
			
		</td>
		<td>
			<input type="text" name="altAuth" id="addUserAltAuth" value="" ltype="text" style="width: 120px;" validate="{maxlength:100,minlength:2}" />
		</td>
	</tr>
	<tr>
		<td align="right">添加执行人：</td>
		<td colspan="3">
				<input type="button" value="串行添加"  onclick="addUserTo(this,0)" />
				<input type="button" value="并行添加"  onclick="addUserTo(this,1)" />
		</td>
	</tr>
	<tr>
		<td align="right">默认执行人：
		<!-- <input type="text" name="userIds" id="userIds" value="" />
		<input type="text" name="userNames" id="userNames" value="" />
		<input type="text" name="userIndexs" id="userIndexs" value="" /> -->
		</td>
		<td id="adduserslist" colspan="3 " class="fu_list">
			<div id="fu_defline" >
				<ul>执行顺序</ul>
				<ul>处理节点名称</ul>
				<ul>处理人</ul>
				<ul>候选人角色</ul>
				<ul>操作</ul></div>
				<c:set var="maxind" value="0"/>
			<c:forEach items="${flowWorkUsers}" var="user" begin='0' varStatus="status" >
				<div class='userlist' dataid='${user.userId}'>
				<ul class='fu_index' >${user.orderIndex}</ul>
				<ul class='fu_nname'>${user.nodeName}</ul>
				<ul class='fu_name'>${user.userName}</ul>
				<ul class='fu_alt'>${user.altAuthCode}</ul>
				<ul class='fu_del' onclick='removeUser(this)'>删除</ul></div>
				<c:if test="${maxind<user.orderIndex}" >
					<c:set var="maxind" value="${user.orderIndex}"/>
				</c:if>
			</c:forEach>
			<input type="hidden" id="maxindex" name="maxindex" value="${maxind}" />
		</td>
	</tr>
	<tr>
		<td align="right">流程结束后执行Hql：</td>
		<td colspan="3">
			<textarea
				cols="150" rows="2" class="l-textarea"
				style="resize:none; width: 90%" 
				name="flowType.endToDoHql" id="endToDoHql"
				validate="{maxlength:1500}">${flowType.endToDoHql}</textarea>
		</td>
	</tr>
	<tr>
		<td align="right">流程类型简述：</td>
		<td colspan="3">
			<textarea
				cols="150" rows="4" class="l-textarea"
				style="resize:none; width: 90%" 
				name="flowType.flowDescription" id="flowDescription"
				validate="{maxlength:1000}">${flowType.flowDescription}</textarea>
		</td>
	</tr>
	
	<tr>
		<td colspan="4" align="center"><input type="button" value="保存" class="l-button" name='save_add' onclick="mySubmit()"
									 			style=" width: 80px" /></td>
	</tr>
</table>

</form>
</body>
</html>




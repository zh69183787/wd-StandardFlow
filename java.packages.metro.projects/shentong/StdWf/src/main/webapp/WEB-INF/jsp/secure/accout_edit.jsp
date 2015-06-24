<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title></title>
   <%@ include file="../inc/inc_head_form.jsp"%>
       <script src="../resource/lib/xheditor/xheditor-1.1.14-zh-cn.min.js" type="text/javascript"></script>
   
<script type="text/javascript">
$(function (){
	
	$("textarea").blur(function(){
		var area=$(this);
		if(area.val().length>2000){ 
			area.val(area.val().substr(0,2000)); 
		}
	});  
	
	$('#workdetail').xheditor(
	{
		tools:'Blocktag,Fontface,FontSize,Bold,Italic,Underline,Strikethrough,FontColor,BackColor,Removeformat,Align,List,Outdent,Indent,Hr,Table,Source,Preview,Print',
		skin:'o2007blue',
		hoverExecDelay:-1,
		internalStyle:true,
		inlineStyle:true,
		html5Upload:false});
});

/*********       Form start         *********/

var waitDialog = null;
function saveF(obj){
	 var options = {
		cache:false,
		type:'post',
		url:'../accounts/saveAccount',
		beforeSerialize:function(){
			waitDialog = $.ligerDialog.waitting('正在操作中,请稍候...');
		},		
		success:function(result){
			waitDialog.close();
				$.ligerDialog.alert('操作成功!', '提示',function(){
					
				});
				return false;
		}
	};
	

		var wlen =  $("#workdetail").val().length;
		if(wlen>2000){ 
			alert("工作职责内容超长！请修改后在保存！");
		}else{
			$.ligerDialog.confirm("您确定要保存数据？",function (r){ 
				if(r){
					$("#form").ajaxSubmit(options);
				}
			});	
		}
	
			


}

function fillIntoContent(checked,text){
	if(checked){
		$('#content').val(text+';'+$('#content').val());
	}
}

/*********       Form end         *********/

</script>

<style type="text/css">
body {
	font-size: 12px;
}

.l-table-edit {
	
}

.l-table-edit-td {
	padding: 4px;
}

.l-button-submit,.l-button-test {
	width: 80px;
	float: left;
	margin-left: 10px;
	padding-bottom: 2px;
}

.l-verify-tip {
	left: 230px;
	top: 120px;
}
</style>
</head>

<body style="padding: 10px">
<center>
	<form name="form" method="post" id="form">
		<input type="hidden" id="id" name="id" value="${account.id}" />
		<table cellpadding="0" cellspacing="0" class="l-table-edit" width="800" border="0">
			<tr>
				<td align="right" class="l-table-edit-td" width="90">用户名称:</td>
				<td align="left" class="l-table-edit-td">
					<input type="text" readonly="readonly" style="width:90%;border:0px;" class="l-text" name="name" value="${account.name}" />
				</td>
				<td align="right" class="l-table-edit-td" colspan="2"></td>
			</tr>								
			<tr>
				<td align="right" class="l-table-edit-td" valign="top">工作职责:</td>
				<td align="left" class="l-table-edit-td" colspan="3">
					<textarea
						cols="100" rows="10" class="l-textarea"
						style="resize:none; width: 90%" 
						name="workdetail" id="workdetail">${account.workdetail}</textarea>
				</td>
			</tr>		
			<%-- <tr>
				<td align="right" class="l-table-edit-td" valign="top">用户头像:</td>
				<td align="left" class="l-table-edit-td" colspan="3">
					<input type="hidden" name="headimg" id="attachGroup" />
					<iframe frameborder="0" width="100%" scrolling="auto" id="attachGroup_needCheck"
          				src="../attachment/findByGroup?group=${account.headimg}&parentCheckId=checkAttach&dest_code=attachGroup&operation=modify&typeStr=jpg,jpeg,gif,png&limit1Img=1" >
          			</iframe>
				</td>
			</tr> --%>
			<tr>
				<td align="center" class="l-table-edit-td" colspan="4">
					<input type="button" value="保存" class="l-button" id="save_add" onclick="saveF()"/>
				</td>
			</tr>																					
		</table>
		
	</form>
</center>
</body>
</html>
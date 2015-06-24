<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>南昌地铁集团 合约管理部 工作管理系统</title>
    <script src="../resource/lib/jquery/jquery-1.7.1.min.js" type="text/javascript"></script>
    <link rel="stylesheet" href="../resource/css/formalize.css">
	<link rel="stylesheet" href="../resource/css/reset.css">
	<link href="../resource/css/pages.css" rel="stylesheet" type="text/css">
	<link href="../resource/css/imgs.css" rel="stylesheet" type="text/css">
    <script type="text/javascript">
    	$(document).ready(function(){
    		$('#sub').click(function(){
    			if($('#loginname').val() == ''){
    				$('#loginname').focus();
    				alert('登陆名不能为空');
    				return;
    			}
    			if($('#oldpassword').val() == ''){
    				$('#oldpassword').focus();
    				alert('旧密码不能为空');
    				return;
    			}
    			if($('#newpassword').val() == ''){
    				$('#newpassword').focus();
    				alert('新密码不能为空');
    				return;
    			}
    			if($('#repeatpassword').val() == ''){
    				$('#repeatpassword').focus();
    				alert('重复新密码不能为空');
    				return;
    			}
    			
    			if($('#repeatpassword').val() != $('#newpassword').val()){
    				$('#newpassword').focus();
    				alert('两次输入密码不一致');
    				return;
    			}
    			$('#form').submit();
    			return false;
    		});
    		
    		$("#subcan").click(function(){
    			parent.tab.removeTabItem("changepassword");
    		});
    		
    	});
    </script>
    <style>
    	table{
    		border-left: 1px solid #A3C0E8;
    		border-top: 1px solid #A3C0E8
    	}
    	 tr {
    		border-bottom:  1px solid #A3C0E8;
    	}
    	td {
    		border-right:  1px solid #A3C0E8;
    	}
    </style>
</head>
<body style="padding:0px;background:#EAEEF5;">  
<form action="changepassword" method="post" id="form">
<br><br><br><br><br>
	<c:if test="${success ne null && success ne '' }">
		<script type="text/javascript">alert('修改密码成功，请重新登陆');window.location.href = 'logout';</script>
	</c:if>
	<table align="center" style="padding:4px;border-left: 1px solid #A3C0E8" >
		<tr class="t_r">
			<td align="right"><label class="mr8">登陆名：</label></td>
			<td>${sessionScope.SEC_CUR_USER_LOGIN_NAME }<input type="hidden" name="loginname" id="loginname" value="${sessionScope.SEC_CUR_USER_LOGIN_NAME }"/></td>
		</tr>
		<tr class="t_r">
			<td align="right"><label class="mr8">旧密码：</label></td>
			<td><input type="password" name="oldpassword" id="oldpassword"/></td>
		</tr>
		<tr class="t_r">
			<td align="right"><label class="mr8">新密码：</label></td>
			<td><input type="password" name="newpassword" id="newpassword"/></td>
		</tr>
		<tr class="t_r">
			<td align="right"><label class="mr8">再输入一次新密码：</label></td>
			<td><input type="password" name="repeatpassword" id="repeatpassword"/></td>
		</tr>
		<tr class="t_r">
			<td>&nbsp;</td>
			<td align="center">
				<input type="button" value="提交" name="button" id="sub"/><input type="button" value="取消" name="button" id="subcan"/>
			</td>
		</tr>
	</table>
	<c:if test="${error ne null && error ne '' }">
		<center><font color="red" id="error">${error}</font></center>
	</c:if>
</form>
</body>
</html>
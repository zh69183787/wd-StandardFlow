<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script src="../resource/lib/jquery/jquery-1.5.2.min.js" type="text/javascript"></script>
<link rel="stylesheet" href="../css/formalize.css" />
<link rel="stylesheet" href="../css/page.css" />
<link rel="stylesheet" href="../css/default/imgs.css" />
<link rel="stylesheet" href="../css/reset.css" />
<script type="text/javascript">
	var waitDialog ;
	$(document).ready(function(){

	});

</script>
<style type="text/css">
.file-box{ position:relative;width:140px}
.txt{ height:22px; border:1px solid #cdcdcd; width:80px;}
.btn{ background-color:#FFF; border:1px solid #CDCDCD;height:24px; width:70px;}
.file{ position:absolute; top:0; left:0px; height:24px; filter:alpha(opacity:0);opacity: 0;width:10px }
</style>
</head>
<body style="padding:0px; "> 
<table width="100%" bordercolorlight="#eeeeee" bordercolordark="#eeeeee" border="1" cellpadding="2" cellspacing="0">
 	<tr>
 		<td colspan="5">历史版本</td>
 	</tr>
	<tr>
		<td nowrap="nowrap" ><font style="font-family: 黑体;"><b>文件名</b></font></td>
		<td nowrap="nowrap" ><font style="font-family: 黑体;"><b>大小(字节)</b></font></td>
		<td nowrap="nowrap" ><font style="font-family: 黑体;"><b>上传时间</b></font></td>
		<td nowrap="nowrap" ><font style="font-family: 黑体;"><b>上传者</b></font></td>
		<td nowrap="nowrap" ><font style="font-family: 黑体;"><b>版本</b></font></td>
	</tr>
	
	        	<c:if test="${requestScope.lists!=null&&fn:length(requestScope.lists)>0 }">
	        		<c:set value="${requestScope.lists}" var="lists" />
	        		<c:forEach items="${lists}" var="list">
		        		<tr>
			        		<td>
			        			<a href="download/${list.id}" target="_blank" title="${list.uploaderName }-${list.uploadTime } ${list.size }byte">
			        				${list.fileName}
			        			</a>
			        		</td>
			        		<td>${list.size}</td>
			        		<td>${list.uploadTime}</td>
			        		<td>${list.uploaderName}</td>
			        		<td>${list.version}</td>
						</tr>
	        		</c:forEach>
	        	</c:if>	
</table>

	</body>
</html>
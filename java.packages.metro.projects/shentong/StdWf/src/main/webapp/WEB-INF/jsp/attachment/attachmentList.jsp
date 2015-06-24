<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script src="../resource/lib/jquery/jquery-1.5.2.min.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/jquery.form.js" type="text/javascript"></script> 
<link href="../resource/lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<script src="../resource/lib/ligerUI/js/core/base.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerGrid.js" type="text/javascript"></script> 
<script src="../resource/lib/ligerUI/js/plugins/ligerTextBox.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerCheckBox.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerMenu.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerComboBox.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerMenuBar.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerToolBar.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerButton.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerResizable.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerDrag.js" type="text/javascript"></script>
<script type="text/javascript">
	function deleteAttach(id){
		if(window.confirm('确定删除?')){
            //alert("确定");
		        var form = document.getElementById('uploadform'); 
            		form.action = 'removeAttach';
            	/* $('#uploadform').attr('action','removeAttach');
        		$('#uploadform').attr('enctype',''); */
        		document.getElementById('attachId').value=id;
        		//$('#attachId').val(id);
        		form.submit();
		        return true;
		     }else{
		        //alert("取消");
		       // return false;
		    }
		
		
	}
	var waitDialog ;
	$(document).ready(function(){
		var destId = $('#dest_code').val();
		var checkId = $('#parentCheckId').val();
		var needCheck = $('#needCheck').val();
		var refresh = $('#refresh').val();
		//alert($("body").height());
		var ishid = $("#ishid").val();
		
		//是否在详细页面时  隐藏父页面相关附件  不提示附件信息
		if(ishid&&$("#listsize").val()==0&&ishid==1){
			var desthid = $('#'+destId+"hid",window.parent.document);
			if(desthid){
				$(desthid).hide();//($("body").height());
			}
		}
			
		
		if(refresh&& refresh == '1' && parent.deleteAttachRefresh && typeof(parent.deleteAttachRefresh) == 'function'){
			parent.deleteAttachRefresh();
		}
		if(destId){
			var destObj = $('#'+destId,window.parent.document);
			if(destObj&&$('#group').val()!=''){
				destObj.val($('#group').val());
			}
			
			var h = $("html").height();
			if($("body").height()>h){
				h=$("body").height();
			}
			if($("#maintable").height()>h){
				h=$("#maintable").height();
			}
			//alert($("#maintable").height());
			//alert($("html").height()+"----"+$("body").height());
			var destiframe = $('#'+destId+"Iframe",window.parent.document);
			//alert(h+"---"+$(destiframe).height()+"---"+$(destiframe).attr("id")+"---"+$(destiframe).attr("autoheight"));
			if(destiframe&&$(destiframe).attr("autoheight")!=null&&$(destiframe).attr("autoheight")=='true'){
				$(destiframe).height(h);
			}
		}
		if(needCheck == '1'&&checkId){
			var obj = $('#'+checkId,window.parent.document);
			if(obj&&$('#checked').val()!='') 
				obj.val($('#checked').val());
		}
		
		$('#sub').click(function(e){
			var form = $('#uploadform');
			//var curObj = $(this);
			var fileObj = $('#file');
			if(fileObj.val() == ''){
				alert('请选择附件进行上传。');
			}else{
				//waitDialog = $.ligerDialog.waitting('正在操作中,请稍候...');
				$("#loading").show();
				$("#loading div").css({"paddingTop":($("#loading").height()/2)-10,"paddingLeft":($("#loading").width()/2)+25});
				form.submit();
				//return false;
			}
			
		});
		$("form").submit(function(){
			var fileObj = $('#file');
			if(fileObj.val() == ''){
				alert('请选择附件进行上传。');
				return false;
			}else{
				return true;
			}
		});

		//$(parent.document,)
		//parent.document.getElementById("attach").style.height=document.body.scrollHeight; 
		//parent.document.getElementById("attach").style.width=document.body.scrollWidth; 

		fileType();
	});
	
	function fileType(){
		var typeStr = $("#typeStr").val();
		$("#file").change(function(){
			var isOk = false;
			var nval = $(this).val();
			if(nval!=""){
				var ext = /\.[^\.]+/.exec(nval);
				if(typeStr!=null&&typeStr!=''){
					isOk = true;
					var tss = typeStr.split(",");
					for(var i=0;i<tss.length;i++){
						var oext="."+tss[i];
						//oext= oext.toLowerCase();
						//ext = ext.toLowerCase();
						if(oext==ext){
							isOk = false;
						}
					}
				}
			}
			if(isOk){
				$(this).val("");
				alert("请上传["+typeStr+"]类型的文件！");
			}
		});
	}
	
	function loadVersionFiles(){
		var url = "../attachment/findHistoryByGroup?group=${requestScope.group}";
		//alert(url);
		window.open(url);
	}
</script>
<style type="text/css">
.file-box{ position:relative;width:140px}
.txt{ height:22px; border:1px solid #cdcdcd; width:80px;}
.btn{ background-color:#FFF; border:1px solid #CDCDCD;height:24px; width:70px;}
.file{ position:absolute; top:0; left:0px; height:24px; filter:alpha(opacity:0);opacity: 0;width:10px }
</style>
</head>
<body style="padding:0px; "> 
<div id="maintable">
<div class="l-tab-loading" id="loading" style="display: none;height:100%"><div>正在上传请稍后。。。</div></div>

		<c:if test="${errors != null && errors != '' }">
			<div style="display: block;font-size: 12px;width: 100%;height:23px;"><font color="red">${errors}</font></div>
		</c:if>
		<form action="uploadFiles" method="POST" id="uploadform" enctype="multipart/form-data">
		<input type='hidden' id="attachId" name = 'attachId' value="" />
		<input type='hidden' id="dest_code" name = 'dest_code' value="${requestScope.dest_code}" />
		<input type='hidden' id="group" name = 'group' value="${requestScope.group}" />
		<input type='hidden' id="typeStr" name = 'typeStr' value="${requestScope.typeStr}" />
		<input type='hidden' id="operation"  name = 'operation' value="${requestScope.operation}" />
		<input type='hidden' id="parentCode"  name = 'parentCode' value="${requestScope.parentCode}" />
		<input type='hidden' id="codeType"  name = 'codeType' value="${requestScope.codeType}" />
		<input type='hidden' id="parentCodeId"  name = 'parentCodeId' value="${requestScope.parentCodeId}" />
		<input type='hidden' id="parentCheckId"  name = 'parentCheckId' value="${requestScope.parentCheckId}">
		<input type='hidden' id="needCheck"  name = 'needCheck' value="${requestScope.needCheck}">
		<input type='hidden' id="canDel"  name = 'canDel' value="${requestScope.canDel}">
		<input type='hidden' id="localFileDir"  name = 'localFileDir' value="${param.localFileDir}">
		<input type='hidden' id="version"  name = 'version' value="${param.version}">
		<input type='hidden' id="showHistory"  name = 'showHistory' value="${param.showHistory}">
		<input type='hidden' id="origNameUsed"  name = 'origNameUsed' value="${param.origNameUsed eq true ? true : false}">
		
		<input type='hidden' id="checked"  name = 'checked' value="${requestScope.checked}">
		
		<input type='hidden' id="refresh"  name = 'refresh1' value="${requestScope.refresh}">
		
		<input type='hidden' id="ishid"  name = 'ishid' value="${requestScope.ishid}">
		
		<table  width="95%" style="font-size:12px;margin-left: 20px" >
			<c:if test="${requestScope.operation!=null&&requestScope.operation=='modify' }">
			<tr valign="top" style="min-height: 20px;height: 20px">
				<td nowrap="nowrap" align="left">
						
					        <c:choose>
					        	<c:when test="${requestScope.dicts!=null&&fn:length(requestScope.dicts)>0 }">
					        		<input type="file" name="files" id="file" size="7"/> 
					        		<select name="codeIds">
							        	<c:forEach items="${requestScope.dicts}" var="dict">
							        		<option value="${dict.id}">${dict.name}</option>
							        	</c:forEach>
							        	<option value="0">其他</option>						      
							        </select>&nbsp;&nbsp;
					        	</c:when>
					        	<c:otherwise>
					        		<input type="file" name="files" id="file" size="23"/> 
					        	</c:otherwise>
					        </c:choose>
					        <c:if test="${requestScope.dicts!=null&&fn:length(requestScope.dicts)>0 }">
					        </c:if>
					        <c:if test="${requestScope.dicts==null||fn:length(requestScope.dicts)==0 }">
						       <input type='hidden' name = 'codeIds' value="0" />
					        </c:if>
					        <input type="button" id="sub" value="上传" />&nbsp;&nbsp;
					   
				    
			    </td>
		    </tr>
		    </c:if>
		    <tr valign="top">
			    <td>
	        	<c:if test="${requestScope.lists==null||fn:length(requestScope.lists)==0 }">
	        		暂无附件
	        		<input type="hidden" value="0" id="listsize" name="listsize"/>
	        	</c:if>
	        	<c:if test="${param.showHistory eq '1'}">
	        	<input type="button" id="history" value="历" onclick="loadVersionFiles()"/>
	        	</c:if>
	        	<c:if test="${requestScope.lists!=null&&fn:length(requestScope.lists)>0 }">
	        		<c:set value="${requestScope.lists}" var="lists" />
	        			        		<input type="hidden" value="${fn:length(requestScope.lists)}" id="listsize" name="listsize"/>
	        		
	        		<c:forEach items="${lists}" var="list">
	        		<div>
	        			<c:if test="${ list.dict != null}">[<c:out value="${list.dict.name}"/>]</c:if>
	        			<a href="download/${list.id}" target="_blank" title="${list.uploaderName }-${list.uploadTime } ${list.size }byte">
	        				<c:out value="${list.fileName }"/>
	        			</a>&nbsp;
	        			<%-- <a href="preview/${list.id}" target="_blank">预览</a>&nbsp; --%>
	        			<c:if test="${requestScope.operation!=null&&requestScope.operation=='modify'&&requestScope.canDel!='uncan' }">
					       <a href="javascript:deleteAttach(${list.id})">删除</a>
				        </c:if>
						</div>
	        		</c:forEach>
	        	</c:if>
		        </td>
	        </tr>
	    </table>
	     </form>
	     </div>
	</body>
</html>
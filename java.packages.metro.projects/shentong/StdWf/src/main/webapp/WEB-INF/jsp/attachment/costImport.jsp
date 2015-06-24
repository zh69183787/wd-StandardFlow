<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<script src="../resource/lib/ligerUI/js/plugins/ligerDateEditor.js" type="text/javascript"></script>

<script type="text/javascript">
	function deleteAttach(id){
		$.ligerDialog.confirm('确定删除?', function(r){      
			
            if(r){
            	
            	var form = document.getElementById('uploadform'); 
            		form.action = 'removeAttach';
            	/* $('#uploadform').attr('action','removeAttach');
        		$('#uploadform').attr('enctype',''); */
        		document.getElementById('attachId').value=id;
        		//$('#attachId').val(id);
        		form.submit();
            }else{
            	// alert(10);
            }
        }); 
		
	}
	var waitDialog ;
	$(document).ready(function(){
		var destId = $('#dest_code').val();
		var checkId = $('#parentCheckId').val();
		var needCheck = $('#needCheck').val();
		var refresh = $('#refresh').val();
		
		if(refresh&& refresh == '1' && parent.deleteAttachRefresh && typeof(parent.deleteAttachRefresh) == 'function'){
			parent.deleteAttachRefresh();
		}
		if(destId){
			var destObj = $('#'+destId,window.parent.document);
			if(destObj&&$('#group').val()!='') 
				destObj.val($('#group').val());
			
		}
		if(needCheck == '1'&&checkId){
			var obj = $('#'+checkId,window.parent.document);
			if(obj&&$('#checked').val()!='') 
				obj.val($('#checked').val());
		}
		$("#receiveDate").ligerDateEditor({});
		$('#sub').click(function(){
			var form = $('#uploadform');
			//var curObj = $(this);
			var fileObj = $('#file');
			var receiveDate = $("#receiveDate").val();
			if(fileObj.val() == ''){
				$.ligerDialog.alert('请选择需要导入的文件。');
			}else if(receiveDate==""){
				$.ligerDialog.alert('请选择需要导入变更的日期期数。');
			}else if(!$("#fn_cb").is(":checked")&&!$("#sj_cb").is(":checked")&&!$("#qz_cb").is(":checked")){
				$.ligerDialog.alert('请选择需要导入变更类型及工作表位置。');
			}else{
				 $.ligerDialog.confirm('确定导入?', function(r){                        
			           if(r){
			        	   $("#setday").val($("#receiveDate").val());
			        	    waitDialog = $.ligerDialog.waitting('正在操作中,请稍候...');
							form.submit();
			           }
			       }); 
				
			}
			
		});
		
		//$(parent.document,)
		//parent.document.getElementById("attach").style.height=document.body.scrollHeight; 
		//parent.document.getElementById("attach").style.width=document.body.scrollWidth; 

		$("form").submit(function(){
			var fileObj = $('#file');
			if(fileObj.val() == ''){
				$.ligerDialog.alert('请选择附件进行上传。');
				return false;
			}else{
				return true;
			}
		});
	});
</script>
<style type="text/css">
.file-box{ position:relative;width:140px}
.txt{ height:22px; border:1px solid #cdcdcd; width:80px;}
.btn{ background-color:#FFF; border:1px solid #CDCDCD;height:24px; width:70px;}
.file{ position:absolute; top:0; left:0px; height:24px; filter:alpha(opacity:0);opacity: 0;width:10px }
.l-grid-row-cell{line-height:25px;text-align: left;padding-left:10px;}
.l-grid-row-cell input{vertical-align: middle;margin-left: 5px;margin-right: 5px;}
</style>
</head>
<body style="padding:0px; overflow:auto;"> 
<form action="../costVisaChange/uploadFilesImport" method="POST" id="uploadform" enctype="multipart/form-data">
							
		<table width="95%" style="min-height:120px;font-size:12px;margin: 50px" >
			<c:if test="${requestScope.operation!=null&&requestScope.operation=='modify' }">
			<tr valign="top" style="min-height: 20px;height: 20px">
				<td nowrap="nowrap" align="left">
					<input type='hidden' id="attachId" name = 'attachId' value="" />
							<input type='hidden' id="dest_code" name = 'dest_code' value="${requestScope.dest_code}" />
							<input type='hidden' id="group" name = 'group' value="${requestScope.group}" />
							<input type='hidden' id="operation"  name = 'operation' value="${requestScope.operation}" />
							<input type='hidden' id="parentCode"  name = 'parentCode' value="${requestScope.parentCode}" />
							<input type='hidden' id="codeType"  name = 'codeType' value="${requestScope.codeType}" />
							<input type='hidden' id="parentCodeId"  name = 'parentCodeId' value="${requestScope.parentCodeId}" />
							<input type='hidden' id="parentCheckId"  name = 'parentCheckId' value="${requestScope.parentCheckId}">
							<input type='hidden' id="needCheck"  name = 'needCheck' value="${requestScope.needCheck}">
							
							<input type='hidden' id="checked"  name = 'checked' value="${requestScope.checked}">
							
							<input type='hidden' id="refresh"  name = 'refresh1' value="${requestScope.refresh}">
							
					        <c:choose>
					        	<c:when test="${requestScope.dicts!=null&&requestScope.dicts.size()>0 }">
					        		<input type="file" name="files" id="file" size="7"/> 
					        		<select name="codeIds">
							        	<c:forEach items="${requestScope.dicts}" var="dict">
							        		<option value="${dict.id}">${dict.name}</option>
							        	</c:forEach>
							        	<option value="0">其他</option>						      
							        </select>&nbsp;&nbsp;
					        	</c:when>
					        	<c:otherwise>
					        		请上传变更需求数据文件：<input type="file" name="files" id="file" size="23"/> 
					        	</c:otherwise>
					        </c:choose>
					        <c:if test="${requestScope.dicts!=null&&requestScope.dicts.size()>0 }">
					        </c:if>
					        <c:if test="${requestScope.dicts==null||requestScope.dicts.size()==0 }">
						       <input type='hidden' name = 'codeIds' value="0" />
					        </c:if>
					        <input type="button" id="sub" value="导入" />&nbsp;&nbsp;
				</td>
		    </tr>	
		     <c:if test="${errors!=null&&errors != ''}">
		     <tr style="line-height:25px;color:red;">
			    <td>
	        	${errors}
		        </td>
	        </tr>
	        </c:if>	        
			<tr valign="top">
			    <td>
					        <table border="1" style="width: 80%;text-align:left; margin-top:10px; margin-bottom:10px; border-top: 1px solid #A3C0E8;border-left: 1px solid #A3C0E8;font-size: 12px;" class="l-grid-body-table">
								<tbody>
								<tr class="l-grid-row"> 
									<td class="l-grid-row-cell ">
									<div style="float:left;">上传第</div>
										<div style="float:left;"><input  name="SCRQ" class="l-text l-date" type="text" id="receiveDate" value="${today==null||today eq ''?params.SCRQ:today }" />
										</div>
										<div style="float:left;">期。</div>
									</td>
								</tr>
								<tr class="l-grid-row"> 
									<td class="l-grid-row-cell ">
									<c:set var="fnc" value="${params.FN==null||params.FN eq ''?'':'checked' }" />
									<input type="checkbox" id="fn_cb" ${fnc} name="FN" />需要导入<font color="red">方案变更</font>需求，数据来源为第 
									<select  name="FN_NUM" >
										<option>1</option>
										<option>2</option>
										<option>3</option>
										<option>4</option>
										<option>5</option>
										<option>6</option>
										<option>7</option>
										<option>8</option>
										<option>9</option>
										<option>10</option>
									</select>
									张工作表.
									</td>
								</tr>
								<tr class="l-grid-row-alt"> 
									<td class="l-grid-row-cell ">
									<c:set var="sjc" value="${params.SJ==null||params.SJ eq ''?'':'checked' }" />
									<input type="checkbox"  name="SJ" ${sjc}  id="sj_cb"/>需要导入<font color="red">设计变更</font>需求，数据来源为第 
									<select name="SJ_NUM">
										<option>1</option>
										<option selected>2</option>
										<option>3</option>
										<option>4</option>
										<option>5</option>
										<option>6</option>
										<option>7</option>
										<option>8</option>
										<option>9</option>
										<option>10</option>
									</select>
									张工作表.
									</td>
								</tr>
								<tr class="l-grid-row"> 
									<td class="l-grid-row-cell " >
									<c:set var="qzc" value="${params.QZ==null||params.QZ eq ''?'':'checked' }" />
									<input type="checkbox" ${qzc} name="QZ" id="qz_cb"/>需要导入<font color="red">签证变更</font>需求，数据来源为第
									<select name="QZ_NUM">
										<option>1</option>
										<option>2</option>
										<option selected>3</option>
										<option>4</option>
										<option>5</option>
										<option>6</option>
										<option>7</option>
										<option>8</option>
										<option>9</option>
										<option>10</option>
									</select>
									张工作表.
									</td>
								</tr>
							 </tbody></table>
					    
				    
			    </td>
		    </tr>
		    </c:if>
		   
		    <tr valign="top">
			    <td>
	        	<c:if test="${requestScope.lists==null||requestScope.lists.size()==0 }">
	        		暂无上传
	        	</c:if>
	        	<c:if test="${requestScope.lists!=null&&requestScope.lists.size()>0 }">
	        		<%-- <c:forEach items="${requestScope.lists}" var="list">
	        			<c:if test="${ list.dict != null}">[<c:out value="${list.dict.name}"/>]</c:if>
	        			<a href="download/${list.id}" target="_blank" title="${list.uploaderName }-${list.uploadTime } ${list.size }byte">
	        				<c:out value="${list.fileName }"/>
	        			</a>&nbsp;
	        			<c:if test="${requestScope.operation!=null&&requestScope.operation=='modify' }">
					       <a href="javascript:deleteAttach(${list.id})">删除</a>
				        </c:if><br>
	        		</c:forEach> --%>
	        		<table border="1" style="width: 80%;text-align:left; margin-top:10px; margin-bottom:10px; border-top: 1px solid #A3C0E8;border-left: 1px solid #A3C0E8;font-size: 12px;" class="l-grid-body-table">
						<tbody>
						<c:forEach items="${requestScope.lists}" var="list">
						<tr class="l-grid-row"> 
							<td class="l-grid-row-cell " colspan="2"title="${list.uploaderName }-${list.uploadTime } ${list.size }byte">
								<c:out value="${list.fileName }"/></td>
							<td class="l-grid-row-cell " ></td>
						</tr>
						</c:forEach>
						<tr class="l-grid-row"> 
							<td class="l-grid-row-cell " title="" colspan="2">
								<font style="font-weight: blod;">导入结果信息:</font>
								<font style="">新增:${requestScope.map.addCount}条，修改${requestScope.map.modifyCount}条。</font>
							</td>							
						</tr>
						<c:forEach items="${requestScope.map.acisError}" var="list">
						<c:choose>
							<c:when test="${list.numSheet!=null&&list.numSheet!=''}">
								<tr class="l-grid-row"> 
									<td class="l-grid-row-cell " width="130">第${list.numSheet+1 }个工作表第${list.rownum+1 }行</td>
									<td class="l-grid-row-cell " title="">
										${list.message }
									</td>
								</tr>
							</c:when>
							<c:otherwise>
								<tr class="l-grid-row"> 
									<td class="l-grid-row-cell " title="" colspan="2">
										${list.message }
									</td>
								</tr>
							</c:otherwise>
						</c:choose>
						
						</c:forEach>
					 </tbody></table>
	        	</c:if>
		        </td>
	        </tr>
	    </table>
	    </form>
	</body>
</html>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>个人备忘</title>

<link rel="stylesheet" href="../css/formalize.css" />
<link rel="stylesheet" href="../css/page.css" />
<link rel="stylesheet" href="../css/default/imgs.css" />
<link rel="stylesheet" href="../css/reset.css" />
<link href="../resource/lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" /> 
<script src="../resource/lib/jquery/jquery-1.5.2.min.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/core/base.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerDateEditor.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerTextBox.js" type="text/javascript"></script> 

<script src="../resource/lib/ligerUI/js/plugins/ligerTip.js" type="text/javascript"></script>
<script src="../resource/lib/jquery-validation/jquery.validate.min.js" type="text/javascript"></script> 
<script src="../resource/lib/jquery-validation/jquery.metadata.js" type="text/javascript"></script>
<script src="../resource/lib/jquery-validation/messages_cn.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerMessageBox.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/jquery.form.js" type="text/javascript"></script>
<script src="../resource/lib/jquery-validation/requiredValidator.js" type="text/javascript"></script>

<script src="../resource/flow/flowwork.js" type="text/javascript"></script> 

<script type="text/javascript">
	var html_renyuan = null, html_jindu = null;
	var l_persons = 0;
	var l_schs = 0;
    $(function (){
    	$(".l-date").ligerDateEditor({width: 160});
    	
    	//表单验证
    	$('#form').requiredValidator();
    	
		$('#add_renyuan').attr('href','javascript:void(0)').click(addRenyuan);
		$('.remove-renyuan').attr('href','javascript:void(0)').click(removeRenyuan);
		
		$('#add_jindu').attr('href','javascript:void(0)').click(addJindu);
		$('.remove-jindu').attr('href','javascript:void(0)').click(removeJindu);
		
		html_renyuan = $('#tr_renyuan').html();
		html_jindu = $('#tr_jindu').html();
    	
    	$('#stdFunId').change(onFunChange).val('${stdApply.stdFunId}').trigger('change');
    	$('#stdMajorTypeId').change(onMajorChange);
    	$('#stdRemarkItemId').val('${stdApply.stdRemarkItemId}');
    	
    	$('.s-stage').each(function(){
    		var val = $(this).prev().val();
    		$(this).val(val);
    	})
    	
    	$("#stdName").focus();
    });  

    function saveF(){
	   	 var options = {
	   		cache:false,
	   		type:'post',
	   		url:'save',
	   		beforeSerialize:function(){
	   			parent.waitDialog = $.ligerDialog.waitting('正在操作中,请稍候...');
	   			$('.tr-person').each(function(){
	   				var tr = $(this);
	   				$('input',tr).each(function(){
	   					var prefix = 'stdApply.spList['+l_persons+'].';
	   					$(this).attr('name', prefix + $(this).attr('name'));
	   				})	   				
	   				l_persons ++;
	   			});
	   			$('.tr-sch').each(function(){
	   				var tr = $(this);
		   			$('input',tr).each(function(){
		   				if($(this).attr('name')){
			   				var prefix = 'stdApply.ssList['+l_schs+'].';
			   				$(this).attr('name', prefix + $(this).attr('name'));		   					
		   				}
		   			});
		   			$('select',tr).each(function(){
		   				var prefix = 'stdApply.ssList['+l_schs+'].';
		   				$(this).attr('name', prefix + $(this).attr('name'));
		   			});   	
		   			l_schs ++;
	   			});
	   		},		
	   		success:function(obj){
  						if(!obj.flowGroup){
		   					parent.startNewFlow(obj.id,'FLOW_STD_APPLY_${sessionScope.SEC_CUR_DEPT.groupcode}',obj.stdName,false);
		   					if(parent.gridManager){
   	   							parent.gridManager.loadData(true);						
   	   						}
   	   						if(parent.waitDialog){
   	   							parent.waitDialog.close();
   	   						}						
   	   						if(parent.winDialog!=null){
   	   							parent.winDialog.close();
   	   						}		   					
	   					}else{
	   		   				$.ligerDialog.alert('保存成功!', '提示',function(){
	   	   						if(parent.gridManager){
	   	   							parent.gridManager.loadData(true);						
	   	   						}
	   	   						if(parent.waitDialog){
	   	   							parent.waitDialog.close();
	   	   						}						
	   	   						if(parent.winDialog!=null){
	   	   							parent.winDialog.close();
	   	   						}	        		 						
	   		   				});	   						
	   					}
	   				return false;
	   		}
	   	};
	   	 
	   	if($('#stdName').val() == ''){
	   		$.ligerDialog.alert('请填写标准名称');
	   		return false;
	   	}
	   	
	   	$.ligerDialog.confirm("您确定要保存数据？",function (r){ 
	   		if(r){
	   			$("#form").ajaxSubmit(options);
	   		}
	   	});		
   }
    
	function onFunChange(){
		var parentId = this.value;
		var majorSel = $('#stdMajorTypeId');
		
 		$.ajax({
			type:'post',
			url:'../dictionary/findByParentId/'+parentId,
			data:'typecode=STD_MAJOR',
			dataType:'json',
			success:function(data){
				if(data){
					majorSel.empty();
					var l = data.length;
					for(var i = 0;i < l;i++){
						var dic = data[i];
						var option = "<option value='"+dic.id+"'>"+dic.name+"</option>"
						majorSel.append(option);
					}
					majorSel.val('${stdApply.stdMajorTypeId}').trigger('change');
				}
			}
		});
	}
	
	function onMajorChange(){
		var parentId = this.value;
		var minorSel = $('#stdMinorTypeId');
		
 		$.ajax({
			type:'post',
			url:'../dictionary/findByParentId/'+parentId,
			data:'typecode=STD_MINOR',
			dataType:'json',
			success:function(data){
				if(data){
					minorSel.empty();
					var l = data.length;
					if(l > 0){
						minorSel.attr('disabled',false);
						for(var i = 0;i < l;i++){
							var dic = data[i];
							var option = "<option value='"+dic.id+"'>"+dic.name+"</option>"
							minorSel.append(option);
						}				
						minorSel.val('${stdApply.stdMinorTypeId}');
					}else{
						minorSel.attr('disabled',true);
					}
				}
			}
		});		
	}
	
	function addRenyuan(){
		var newTr = $('<tr class="tr-person"></tr>').append(html_renyuan);
		$('.remove-renyuan',newTr).click(removeRenyuan);
		$('#table_renyuan').append(newTr);
	}
	
	function removeRenyuan(){
		var tr = $(this).closest('tr');
		var id = $('#spId',tr);
		if(id.length > 0){
			delPerson($(id).val(),'delPersonById');
		}
		tr.remove();
	}
	
	function addJindu(){
		var newTr = $('<tr class="tr-sch"></tr>').append(html_jindu);
		$('.remove-jindu',newTr).click(removeJindu);
		$('input[type="date"]',newTr).each(function(){
			$(this).ligerDateEditor({width: 160});
		});		
		$('#table_jindu').append(newTr);
	}
	
	function removeJindu(){
		var tr = $(this).closest('tr');
 		var id = $('#ssId',tr);
		if(id.length > 0){
			delPerson($(id).val(),'delSchById');
		}		
		tr.remove();
	}	
	
	function delPerson(id,path){
		 $.ajax({
		        type: "post",
		        url: "../stdApply/"+path,
		        dataType: "json",
		        data:"id="+id+"&ts="+new Date().getTime(),
		        success: function (data) {
        	
		        },
		        error: function (XMLHttpRequest, textStatus, errorThrown) {
		                alert("删除失败");
		        }
		});	 
	}
</script>
<style type="text/css">
body{ font-size:12px;}
.l-table-edit-td {
	padding: 4px;
}     
.t_r{
	text-align:right;
}
.td-finished {
	${personalMemo.state== '0' ? 'display: none;':''}
}
.l-trigger-icon {
    background-image: url("../css/default/images/icon_1.png");
    background-position: -3px -720px;
    background-repeat: no-repeat;
}
</style>
</head>

<body class="Flow">
	<div class="f_bg">
      <!--Panel_6--><!--Panel_6 End-->	
  <div class="logo_1"></div>
        <div class="gray_bg">
        	<div class="gray_bg2">
            	<div class="w_bg">
                	<div class="Bottom">
                    	<div class="Top">
                        	<h1 class="t_c">上海申通地铁集团有限公司<br>
                       	  标准制修订立项表</h1>
                            <div class="mb10 Step"></div>
                            <div class="mb10">
                            <form id="form">
                            	<input type="hidden" name="stdApply.id" id="id" value="${stdApply.id}"/>
                           	  	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_2">
                                  <thead>
                                  <th colspan="4">
                                    	<h5 class="fl">基本信息</h5>
                                        <!-- <span class="fr pt5 mr5"><a href="#">收起</a></span> -->
                                    </th>
                                    </thead>
                                  <tr>
                                    <td class="lableTd t_r"><span class="tit">*</span>标准名称</td>
                                    <td><input type="text" class="input_large" name="stdApply.stdName" value="${stdApply.stdName}" id="stdName" maxlength="200"/></td>
                                    <td class="lableTd t_r">立项编号</td>
                                     <td>
                                     <input name="projectNo" type="text" class="input_large" id="projectNo" value="${empty stdApply.projectNo ? '自动生成' : stdApply.projectNo}" disabled/>
                                     </td>
                                  </tr>
                                  <tr>
                                    <td class="lableTd t_r">标准化员</td>
                                    <td><input name="cUserName" type="text" class="input_large" id="cUserName" value="${empty stdApply.cUser ? sessionScope.SEC_CUR_ACCOUNT.name : stdApply.cUser.name}" disabled/> 
                                     </td>
                                    <td class="lableTd t_r">申报部门</td>
                                    <td><input name="applyDeptName" type="text" class="input_large" id="applyDeptName" value="${empty stdApply.applyDept ? sessionScope.SEC_CUR_DEPT.name : stdApply.applyDept.name}" disabled/>
									</td>
                                  </tr>
                                  <tr>
                                    <td class="lableTd t_r">申报时间</td>
                                     <td><input name="stdApply.applyDate" type="date" class="input_large l-date" id="applyDate" value="${stdApply.applyDate}"/></td>
                                    <td class="lableTd t_r">申报状态</td>
                                    <td><input name="242" type="text" class="input_large" id="243" disabled value="${empty stdApply.flowWorkProcess ? '未开始' : stdApply.flowWorkProcess.state eq '2' ? '通过' : '审批中'}"/></td>
                                  </tr>
                                  <tr>
                                    <td class="lableTd t_r">标准功能</td>
                                    <td>
										<select id="stdFunId" name="stdApply.stdFunId" class="input_large">
											<c:forEach items="${stdFunDics}" var="iDic">
												<option value="${iDic.id}">${iDic.name}</option>
											</c:forEach>												
										</select> 
									</td>
                                    <td class="lableTd t_r">标准大类</td>
                                    <td>
                                    	<select id="stdMajorTypeId" name="stdApply.stdMajorTypeId" class="input_large" >
										</select>
									</td>
                                  </tr>         
                                  <tr>
                                    <td class="lableTd t_r">标准小类</td>
                                    <td>
										<select id="stdMinorTypeId" name="stdApply.stdMinorTypeId" class="input_large" disabled>
										</select> 
									</td>
                                    <td class="lableTd t_r">标准备注项</td>
                                    <td>
                                    	<select name="stdApply.stdRemarkItemId" id="stdRemarkItemId" class="input_large" >
											<c:forEach items="${stdRemarkDics}" var="iDic">
												<option value="${iDic.id}">${iDic.name}</option>
											</c:forEach>					
										</select>
									</td>
                                  </tr>                                                          
                                </table>
                           	  
                           	  <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_2">
                            	  <thead>
                                      <th><h5 class="fl">申报信息</h5>
                                      <!-- <span class="fr pt5 mr5"><a href="#" class="open">展开</a></span> -->
                                      </th>
                                  </thead>

                            	  <tr class="disable">
                            	    <td>
                            	      
                           	        </td>
                           	      </tr>
                          	        <tr>
                        	          <td class="lableTd">立项理由</td>
                       	            </tr>
                            	  <tr>
                            	    <td><textarea id="applyReason" rows="7" name="stdApply.applyReason">${stdApply.applyReason}</textarea></td>
                           	      </tr>
                          	        <tr>
                        	          <td class="lableTd">适用范围与主要内容</td>
                       	            </tr>
                            	  <tr>
                            	    <td><textarea id="scopeContent" rows="7" name="stdApply.scopeContent">${stdApply.scopeContent}</textarea></td>
                           	      </tr>
                          	        <tr>
                        	          <td class="lableTd"><span style="float:left">编制人员</span>
                                      <span class="fr pt5"><a href="#" id="add_renyuan"><img src="../css/default/+.png" width="15" height="15"/></a></span>
                                      </td>
                       	            </tr>
                            	  <tr>
                                  <td style="padding:0px; margin:0px">
                            	    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_2" id="table_renyuan">
                                  <tr >
                                    <td width="29%" class="lableTd t_c">部门</td>
                                    <td width="29%" class="lableTd t_c">姓名</td>
                                    <td width="28%" class="lableTd t_c">职务</td>
                                    <td width="14%" class="lableTd t_c">&nbsp;</td>
                                    </tr>
                                  <tr id="tr_renyuan" style="display:none;">
                                    <td><input name="dept" type="text" class="input_xmlarge" maxlength="200" /></td>
                                    <td><input name="name" type="text" class="input_xmlarge" maxlength="200" /></td>
                                    <td><input name="post" type="text" class="input_xmlarge" maxlength="200" /></td>
                                    <td><span class="fl pt5"><a href="#" class='remove-renyuan'><img src="../css/default/-.png" width="15" height="15"></a></span></td>
                                  </tr>
                                  <c:if test="${empty stdApply.spList}">
	                                  <tr class="tr-person">
	                                    <td><input name="dept" type="text" class="input_xmlarge" maxlength="200" /></td>
	                                    <td><input name="name" type="text" class="input_xmlarge" maxlength="200" /></td>
	                                    <td><input name="post" type="text" class="input_xmlarge" maxlength="200" /></td>
	                                    <td><span class="fl pt5"><a href="#" class='remove-renyuan'><img src="../css/default/-.png" width="15" height="15"></a></span></td>
	                                  </tr>                                  
                                  </c:if>
                                  <c:if test="${not empty stdApply.spList}">
    								<c:forEach items="${stdApply.spList}" var="sp" varStatus="status">
	                                  <tr class="tr-person">
	                                    <td><input name="id" type="hidden" id="spId" class="input_xmlarge" value="${stdApply.spList[status.index].id}"/>
	                                    <input name="dept" type="text" class="input_xmlarge" value="${stdApply.spList[status.index].dept}" maxlength="200"/></td>
	                                    <td><input name="name" type="text" class="input_xmlarge" value="${stdApply.spList[status.index].name}" maxlength="200"/></td>
	                                    <td><input name="post" type="text" class="input_xmlarge" value="${stdApply.spList[status.index].post}" maxlength="200"/></td>
	                                    <td><span class="fl pt5"><a href="#" class='remove-renyuan'><img src="../css/default/-.png" width="15" height="15"></a></span></td>
	                                  </tr>    									
    								</c:forEach>   
                                  </c:if>                                  
                                  </table>
                                  </td>
                           	      </tr>
                                                            	        <tr>
        <td class="lableTd"><span style="float:left">工作进度安排</span>  <span class="fr pt5"><a href="#" id="add_jindu"><img src="../css/default/+.png" width="15" height="15"></a></span></td>
                       	            </tr>
                               
                     <tr class="disable">
                          <td style="padding:0px; margin:0px">
                            	    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_2" id="table_jindu">

                                  <tr id="tr_jindu" style="display:none;">
                                    <td width="15%" class="lableTd">
                                    <select name="stdStageId" class="input_small"  style="font-size:12px" >
										<c:forEach items="${stdStageDics}" var="iDic">
											<option value="${iDic.id}">${iDic.name}</option>
										</c:forEach>			
                                    </select></td>
                                    <td width="23%">
										<input name="finishDate" type="date" class="input_large" />                                 
                                    </td>
                                    <td width="62%"><span class="fl pt5"><a href="#" class='remove-jindu'><img src="../css/default/-.png" width="15" height="15"/></a></span></td>
                                  </tr>
                                  <c:if test="${empty stdApply.ssList}">
	                                  <tr class="tr-sch">
	                                    <td width="15%" class="lableTd">
	                                    <select name="stdStageId" class="input_small"  style="font-size:12px" >
											<c:forEach items="${stdStageDics}" var="iDic">
												<option value="${iDic.id}">${iDic.name}</option>
											</c:forEach>			
	                                    </select></td>
	                                    <td width="23%">
											<input name="finishDate" type="date" class="input_large l-date" />                                 
	                                    </td>
	                                    <td width="62%"><span class="fl pt5"><a href="#" class='remove-jindu'><img src="../css/default/-.png" width="15" height="15"/></a></span></td>
	                                  </tr>                                  
                                  </c:if>
                                  <c:if test="${not empty stdApply.ssList}">
    								<c:forEach items="${stdApply.ssList}" var="ss" varStatus="status">
	                                  <tr class="tr-sch">
	                                    <td width="15%" class="lableTd"><input name="id" type="hidden" id="ssId" class="input_xmlarge" value="${stdApply.ssList[status.index].id}"/>
	                                    <input type="hidden" id="stdApply.ssList[${status.index}].stdStageId" class="input_xmlarge" value="${stdApply.ssList[status.index].stdStageId}"/>
	                                    <select name="stdStageId" class="input_small s-stage" style="font-size:12px" >
											<c:forEach items="${stdStageDics}" var="iDic">
												<option value="${iDic.id}">${iDic.name}</option>
											</c:forEach>			
	                                    </select></td>
	                                    <td width="23%">
											<input name="finishDate" value="${stdApply.ssList[status.index].finishDate}" type="date" class="input_large l-date" />                                 
	                                    </td>
	                                    <td width="62%"><span class="fl pt5"><a href="#" class='remove-jindu'><img src="../css/default/-.png" width="15" height="15"/></a></span></td>
	                                  </tr>    								
    								</c:forEach>   
                                  </c:if>                                   
                                  </table>
								</td>
                           	      </tr>
                          	      <tr>
                        	          <td class="lableTd">备注说明</td>
                       	            </tr>
                            	  <tr>
                            	    <td><textarea id="remark" rows="7" name="stdApply.remark">${stdApply.remark}</textarea></td>
                           	      </tr>                           	      
                       	      </table>
                       	      </form>
                          </div>
                          <div class="mb10 t_c">
                          <input type="button" value="提交" onclick="saveF()"/>
                                  &nbsp;
<input type="reset" value="取 消" onclick="parent.winDialog.close();"/>&nbsp;
                          </div>
                            <div class="footer"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
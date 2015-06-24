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
    $(function (){
    	
    	var destiframe = $("#publicIframe",window.parent.document);
        if(destiframe){
        	var h = $("body").height();
     		if($("#printdiv").height()>h){
     			h=$("#printdiv").height();
     		}
     		$(destiframe).height(h+36);
        }
        
    	$(".l-date").ligerDateEditor({width: 160});
    	
    	//表单验证
    	$('#form').requiredValidator();
    	
    	$('#stdFunId').change(onFunChange).val('${stdApprove.stdFunId}').trigger('change');
    	$('#stdMajorTypeId').change(onMajorChange);
    	$('#stdRemarkItemId').val('${stdApprove.stdRemarkItemId}');
    	
    	$("#stdName").focus();
    });  

    function saveF(){
	   	 var options = {
	   		cache:false,
	   		type:'post',
	   		url:'save',
	   		beforeSerialize:function(){
	   			parent.waitDialog = $.ligerDialog.waitting('正在操作中,请稍候...');
	   		},		
	   		success:function(obj){
  						if(!obj.flowGroup){
		   					parent.startNewFlow(obj.id,'FLOW_STD_APPROVE_${sessionScope.SEC_CUR_DEPT.groupcode}',obj.stdName,false);
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
	   	 
	   	if($('#stdName').val() == '' || $('#stdNo').val() == ''){
	   		$.ligerDialog.alert('请填写标准名称及标准编号');
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
					majorSel.val('${stdApprove.stdMajorTypeId}').trigger('change');
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
						minorSel.val('${stdApprove.stdMinorTypeId}');
					}else{
						minorSel.attr('disabled',true);
					}
				}
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
	<div class="f_bg" id="printdiv">
      <!--Panel_6--><!--Panel_6 End-->	
  <div class="logo_1"></div>
        <div class="gray_bg">
        	<div class="gray_bg2">
            	<div class="w_bg">
                	<div class="Bottom">
                    	<div class="Top">
                        	<h1 class="t_c">上海申通地铁集团有限公司<br>
                       	  标准制修订报批稿</h1>
                            <div class="mb10 Step"></div>
                            <div class="mb10">
                            <form id="form">
                            	<input type="hidden" name="stdApprove.id" id="id" value="${stdApprove.id}"/>
                            	<input type="hidden" name="srId" id="srId" value="${param.srId}"/>
                            	<input type="hidden" id="attachGroup" name="stdApprove.attachGroup" value="${stdApprove.attachGroup}"/>
                           	  	<table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_2">
                                  <thead>
                                  <th colspan="4">
                                    	<h5 class="fl">基本信息</h5>
                                        <!-- <span class="fr pt5 mr5"><a href="#">收起</a></span> -->
                                    </th>
                                    </thead>
                                  <tr>
                                    <td class="lableTd t_r"><span class="tit">*</span>标准名称</td>
                                    <td><input type="text" class="input_large" style="background: #eeeeee;" name="stdApprove.stdName" value="${stdApprove.stdName}" id="stdName" maxlength="200" readonly="readonly"/></td>
                                    <td class="lableTd t_r"><span class="tit">*</span>标准编号</td>
                                    <td><input name="stdApprove.stdNo" type="text" class="input_large" id="stdNo" value="${stdApprove.stdNo}" maxlength="200"/></td>                                    
                                  </tr>
                                  <tr>
                                    <td class="lableTd t_r">标准化员</td>
                                    <td><input name="cUserName" type="text" class="input_large" id="cUserName" value="${empty stdApprove.cUser ? sessionScope.SEC_CUR_ACCOUNT.name : stdApprove.cUser.name}" disabled/> 
                                     </td>
                                    <td class="lableTd t_r">申报部门</td>
                                    <td><input name="applyDeptName" type="text" class="input_large" id="applyDeptName" value="${empty stdApprove.applyDept ? sessionScope.SEC_CUR_DEPT.name : stdApprove.applyDept.name}" disabled/>
									</td>
                                  </tr>
                                  <tr>
                                    <td class="lableTd t_r">申报时间</td>
                                    <td><input name="stdApprove.applyDate" type="date" class="input_large l-date" id="applyDate" value="${stdApprove.applyDate}"/></td>
                                    <td class="lableTd t_r">立项编号</td>
                                    <td>
                                     	<input name="stdApprove.projectNo" type="text" class="input_large" style="background: #eeeeee;" id="projectNo" value="${stdApprove.projectNo}" readonly="readonly"/>
									</td>
                                  </tr>
                                  <tr>
                                    <td class="lableTd t_r">标准功能</td>
                                    <td>
										<select id="stdFunId" name="stdApprove.stdFunId" class="input_large">
											<c:forEach items="${stdFunDics}" var="iDic">
												<option value="${iDic.id}">${iDic.name}</option>
											</c:forEach>												
										</select> 
									</td>
                                    <td class="lableTd t_r">标准大类</td>
                                    <td>
                                    	<select id="stdMajorTypeId" name="stdApprove.stdMajorTypeId" class="input_large" >
										</select>
									</td>
                                  </tr>         
                                  <tr>
                                    <td class="lableTd t_r">标准小类</td>
                                    <td>
										<select id="stdMinorTypeId" name="stdApprove.stdMinorTypeId" class="input_large" disabled>
										</select> 
									</td>
                                    <td class="lableTd t_r">标准备注项</td>
                                    <td>
                                    	<select name="stdApprove.stdRemarkItemId" id="stdRemarkItemId" class="input_large" >
											<c:forEach items="${stdRemarkDics}" var="iDic">
												<option value="${iDic.id}">${iDic.name}</option>
											</c:forEach>					
										</select>
									</td>
                                  </tr>                                                          
                                </table>
                           	  
                           	  <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_2">
                            	  <thead>
                                      <th><h5 class="fl">报批信息</h5>
                                      <!-- <span class="fr pt5 mr5"><a href="#" class="open">展开</a></span> -->
                                      </th>
                                  </thead>
                          	      <tr>
                        	          <td class="lableTd">附件</td>
                       	          </tr>
                       	          <tr>
									<td>
										<iframe id="attachGroupIframe" height="35" frameborder="0" autoheight="true" width="80%" scrolling="no" src="../attachment/findByGroup?group=${stdApprove.attachGroup}&dest_code=attachGroup&operation=modify&refresh=1&showHistory=1&origNameUsed=true&version=${version}&localFileDir=${fn:replace(stdApprove.projectNo,'-','\\')}" ></iframe>
									</td>
								  </tr>                                  
                          	      <tr>
                        	          <td class="lableTd">备注说明</td>
                       	            </tr>
                            	  <tr>
                            	    <td><textarea id="remark" rows="7" name="stdApprove.remark">${stdApprove.remark}</textarea></td>
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
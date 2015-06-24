<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
<script src="../resource/lib/ligerUI/js/plugins/ligerComboBox.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerTextBox.js" type="text/javascript"></script> 

<script src="../resource/lib/ligerUI/js/plugins/ligerTip.js" type="text/javascript"></script>
<script src="../resource/lib/jquery-validation/jquery.validate.min.js" type="text/javascript"></script> 
<script src="../resource/lib/jquery-validation/jquery.metadata.js" type="text/javascript"></script>
<script src="../resource/lib/jquery-validation/messages_cn.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerMessageBox.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/jquery.form.js" type="text/javascript"></script>
<script src="../resource/lib/jquery-validation/requiredValidator.js" type="text/javascript"></script>
<script src="../resource/lib/datecompute.js" type="text/javascript"></script>

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
});
    function saveF(){
	   	 var options = {
	   		cache:false,
	   		type:'post',
	   		url:'save',
	   		beforeSerialize:function(){
	   			parent.waitDialog = $.ligerDialog.waitting('正在操作中,请稍候...');
	   		},		
	   		success:function(result){
	   				$.ligerDialog.alert('操作成功!', '提示',function(){
	   					if(parent.f_search){
	   						parent.f_search(null);	
	   					}
	   					if(parent.winDialog!=null){
	   						parent.winDialog.close();
	   					}else if(parent.tab){
	   						parent.tab.removeSelectedTabItem();
	   					}
	   				});
	   				return false;
	   		}
	   	};
	   	
	   	$.ligerDialog.confirm("您确定要保存数据？",function (r){ 
	   		if(r){
	   			$("#form").ajaxSubmit(options);
	   		}
	   	});		
   }
    
    function showFinishDate(obj){
    	if(obj.value == '1'){
    		$(".td-finished").show();    		
    	}
    	else{
    		$(".td-finished").hide();
    		$('.l-finished').val('');
    	}
    }    
    
	function dateCheck(beginId, endId, dateDifferId, inputFlag){
		var beginDate = $('#'+beginId).val();
		var endDate = $('#'+endId).val();
		if(endDate == null || endDate == ''){
			if(dateDifferId != null)$('#'+dateDifferId).val('');
			return;
		}
		if((beginDate == null || beginDate == '') && inputFlag != 'begin'){
			$('#'+beginId).val('');
			$('#'+endId).val('');
			if(dateDifferId != null)$('#'+dateDifferId).val('');
			$.ligerDialog.alert("请先选择提出时间！");
			return false;
		}
		
		if(beginDate instanceof Date){
			beginDate = xDateFormat(beginDate, 'yyyy-MM-dd');
		}
		if(endDate instanceof Date){
			endDate = xDateFormat(endDate, 'yyyy-MM-dd');
		}
		
		var dateLen = getWorkData(beginDate,endDate);
		var subdaynum = dateLen.split(",")[0]*1;
		var workdaynum = (dateLen.split(",")[1])*1;
		if(subdaynum>0){
			if(dateDifferId != null)$('#'+dateDifferId).val(workdaynum);
		}else{
			if(inputFlag == 'begin'){
				$('#'+beginId).val(endDate);
			}else{
				$('#'+endId).val(beginDate);
			}
			if(dateDifferId != null)$('#'+dateDifferId).val(1);
			$.ligerDialog.alert("提出时间必须小于等于预计完成时间！");
		}
		return true;
	}
	
	// date format "yyyy-MM-dd"
	function xDateFormat(date ,format){
		if (date == "NaN") return '';
		if(date instanceof Date){
			var o = {
				"M+" : date.getMonth()+1, //month
				"d+" : date.getDate(),    //day
				"h+" : date.getHours(),   //hour
				"m+" : date.getMinutes(), //minute
				"s+" : date.getSeconds(), //second
				"q+" : Math.floor((date.getMonth()+3)/3),  //quarter
				"S" : date.getMilliseconds() //millisecond
			};
			if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
			(date.getFullYear()+"").substr(4 - RegExp.$1.length));
			for(var k in o)if(new RegExp("("+ k +")").test(format))
			format = format.replace(RegExp.$1,
			RegExp.$1.length==1 ? o[k] :
			("00"+ o[k]).substr((""+ o[k]).length));
			return format;
		}else{
			return '';
		}
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
.l-table-edit{table-layout: fixed;word-break:break-all; word-wrap: break-word;}
.l-table-edit-td{word-break:break-all;word-wrap : break-word;overflow:hidden;vertical-align:text-top; padding-top: 4px;}
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
                       	  标准制修订立项表</h1>
                            <!-- <div class="mb10 Step">123</div> -->
                            <div class="mb10">
                            <form id="form">
                           	  <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_2">
                                  <thead>
                                  <th colspan="4">
                                    	<h5 class="fl">基本信息</h5>
                                        <!-- <span class="fr pt5 mr5"><a href="#">收起</a></span> -->
                                    </th>
                                    </thead>
                                  <tr>
                                    <td class="lableTd t_r">标准名称</td>
                                    <td>${stdApply.stdName}</td>
                                    <td class="lableTd t_r">立项编号</td>
                                     <td>${stdApply.projectNo}</td>
                                  </tr>
                                  <tr>
                                    <td class="lableTd t_r">标准化员</td>
                                    <td>${stdApply.cUser.name} 
                                     </td>
                                    <td class="lableTd t_r">申报部门</td>
                                    <td>${stdApply.applyDept.name}
									</td>
                                  </tr>
                                  <tr>
                                    <td class="lableTd t_r">申报时间</td>
                                     <td>${stdApply.applyDate}</td>
                                    <td class="lableTd t_r">申报状态</td>
                                    <td>${stdApply.stateStr}</td>
                                  </tr>
                                  <tr>
                                    <td class="lableTd t_r">标准功能</td>
                                    <td>
										${stdApply.stdFun.name}
									</td>
                                    <td class="lableTd t_r">标准大类</td>
                                    <td>
										${stdApply.stdMajorType.name}
									</td>
                                  </tr>         
                                  <tr>
                                    <td class="lableTd t_r">标准小类</td>
                                    <td>
										${stdApply.stdMinorType.name}
									</td>
                                    <td class="lableTd t_r">标准备注项</td>
                                    <td>
										${stdApply.stdRemarkItem.name}
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
                            	    <td>${stdApply.applyReason}</td>
                           	      </tr>
                          	        <tr>
                        	          <td class="lableTd">适用范围与主要内容</td>
                       	            </tr>
                            	  <tr>
                            	    <td>${stdApply.scopeContent}</td>
                           	      </tr>
                          	        <tr>
                        	          <td class="lableTd"><span style="float:left">编制人员</span>
                                      <!-- <span class="fr pt5"><a href="#" id="add_renyuan"><img src="css/default/+.png" width="15" height="15"></a></span> -->
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
   								<c:forEach items="${stdApply.spList}" var="sp" varStatus="status">
                                  <tr style="text-align:center;">
                                    <td>${stdApply.spList[status.index].dept}</td>
                                    <td>${stdApply.spList[status.index].name}</td>
                                    <td>${stdApply.spList[status.index].post}</td>
                                    <td></td>
                                  </tr>    									
   								</c:forEach>
                                  </table>
                                  </td>
                           	      </tr>
                                                            	        <tr>
        <td class="lableTd"><span style="float:left">工作进度安排</span>  <!-- <span class="fr pt5"><a href="#" id="add_jindu"><img src="css/default/+.png" width="15" height="15"></a></span> --></td>
                       	            </tr>
                               
                     <tr class="disable">
                          <td style="padding:0px; margin:0px">
                            	    <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_2" id="table_jindu">

    								<c:forEach items="${stdApply.ssList}" var="ss" varStatus="status">
	                                  <tr>
	                                    <td width="15%" class="lableTd">${stdApply.ssList[status.index].stdStage.name}</td>
	                                    <td width="23%">
											${stdApply.ssList[status.index].finishDate}
	                                    </td>
	                                    <td width="62%"></td>
	                                  </tr>    								
    								</c:forEach> 
                                  </table>
								</td>
                           	      </tr>
                          	      <tr>
                        	          <td class="lableTd">备注说明</td>
                       	            </tr>
                            	  <tr>
                            	    <td>${stdApply.remark}</td>
                           	      </tr>                           	      
                       	      </table>
                       	      </form>
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
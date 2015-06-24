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
                       	  标准制修订送审表</h1>
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
                                    <td>${stdReview.stdName}</td>
                                    <td class="lableTd t_r">标准编号</td>
                                     <td>${stdReview.stdNo}</td>
                                  </tr>
                                  <tr>
                                    <td class="lableTd t_r">标准化员</td>
                                    <td>${stdReview.cUser.name} 
                                     </td>
                                    <td class="lableTd t_r">申报部门</td>
                                    <td>${stdReview.applyDept.name}
									</td>
                                  </tr>
                                  <tr>
                                    <td class="lableTd t_r">申报时间</td>
                                     <td>${stdReview.applyDate}</td>
                                    <td class="lableTd t_r">立项编号</td>
                                    <td>${stdReview.projectNo} </td>                                     
                                  </tr>
                                  <tr>
                                    <td class="lableTd t_r">标准功能</td>
                                    <td>
										${stdReview.stdFun.name}
									</td>
                                    <td class="lableTd t_r">标准大类</td>
                                    <td>
										${stdReview.stdMajorType.name}
									</td>
                                  </tr>         
                                  <tr>
                                    <td class="lableTd t_r">标准小类</td>
                                    <td>
										${stdReview.stdMinorType.name}
									</td>
                                    <td class="lableTd t_r">标准备注项</td>
                                    <td>
										${stdReview.stdRemarkItem.name}
									</td>
                                  </tr>              
                                  <tr>
                                    <td class="lableTd t_r">申报状态</td>
                                    <td colspan="3">${stdReview.stateStr}</td>
                                  </tr>                                                                              
                                </table>
                           	  
                           	  <table width="100%" border="0" cellspacing="0" cellpadding="0" class="table_2">
                            	  <thead>
                                      <th><h5 class="fl">送审信息</h5>
                                      <!-- <span class="fr pt5 mr5"><a href="#" class="open">展开</a></span> -->
                                      </th>
                                  </thead>
                          	      <tr>
                        	          <td class="lableTd">附件</td>
                       	          </tr>
                       	          <tr>
									<td>
										<iframe id="attachGroupIframe" height="35" frameborder="0" autoheight="true" width="80%" scrolling="no" src="../attachment/findByGroup?group=${stdReview.attachGroup}&dest_code=attachGroup&refresh=1&showHistory=1&version=${version}" ></iframe>
									</td>
								  </tr>                                   
                          	      <tr>
                        	          <td class="lableTd">备注说明</td>
                       	            </tr>
                            	  <tr>
                            	    <td>${stdReview.remark}</td>
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
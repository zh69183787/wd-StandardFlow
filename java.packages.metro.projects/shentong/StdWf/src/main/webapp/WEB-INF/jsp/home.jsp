<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title></title>
<link href="../resource/lib/ligerUI/skins/Aqua/css/ligerui-dialog.css" rel="stylesheet" type="text/css" /> 
	<link href="../resource/css/M-all.css" rel="stylesheet" type="text/css" />
	<link href="../resource/lib/jquery.mCustomScrollbar/jquery.mCustomScrollbar.css" rel="stylesheet" type="text/css" />
	<!--[if IE]>
    <script type="text/javascript" src="../js/html5.js"></script>
	<![endif]--> 
	<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="../resource/lib/jquery.mCustomScrollbar/jquery.mCustomScrollbar.concat.min.js"></script>
	 <script src="../resource/lib/ligerUI/js/ligerui.all.js" type="text/javascript"></script> 
	 
	     <script src="../resource/lib/jquery-highchart/highcharts.js" type="text/javascript"></script>
    <script src="../resource/lib/jquery-highchart/modules/exporting.js" type="text/javascript"></script>
    <script type="text/javascript" src="../resource/lib/jquery-highchart/themes/grid.js"></script>
    <script src="../resource/lib/jquery-highchart/highcharts-more.js"></script>
<script type="text/javascript">
$(document).ready(function (){ 
  
	setPanelWidth();//初始化自适应模块宽度	
	setTabs();//初始化tab切换效果

});
//初始化自适应模块宽度
function setPanelWidth(){
	$("#tab_1").width($("body").width()-328-35);
	$("#tab_1 .list").width($("#tab_1").width()-300);
	$("#tab_2").width($("body").width()-328-25);
	$("#tab_2 .list").width($("#tab_2").width()-300);
	
	
	$("#noticeContent").width($(".notice").width()-160);
	$(window).resize(function(){
		if($(".main").width()>910){
		$("#tab_1").width($("body").width()-328-45);
		$("#tab_1 .list").width($("#tab_1").width()-300);
		$("#tab_2").width($("body").width()-328-25);
		$("#tab_2 .list").width($("#tab_2").width()-300);
		$("#noticeContent").width($(".notice").width()-160);
		}
		
	});
	
	
	$("#notice").click(function(){
		if($("#noticeContent").height()<150){
			$("#noticeContent").height(150);
			$("#noticeContent li").attr("style","line-height:25px;padding-top:10px;");
			$("#noticeContent").mCustomScrollbar({
				scrollButtons:{
					enable:true
				},
				theme:"dark"
			});
		}else{
			$("#noticeContent").mCustomScrollbar("destroy");
			$("#noticeContent").height(45);
			$("#noticeContent li").attr("style","line-height:45px;padding-top:0px;");
		}
		
	});
}
//初始化tab切换效果
function setTabs(){
	$("#tab_1 .tabs_1 li").mouseover(function(){
		$("#tab_1 .tabs_1 li").removeClass("selected");
		$(this).addClass("selected");
		$("#tab_1 .tabContent").hide();
		var con = $(this).attr('name');
		$("#tab_1 ."+con).show();
		$("#tab_1 ."+con+" .tabScrollbar").mCustomScrollbar("destroy");
		$("#tab_1 ."+con+" .tabScrollbar").mCustomScrollbar({
			scrollButtons:{
				enable:true
			},
			theme:"dark"
		});
	});
	$("#tab_1 .tabContent").hide();
	$("#tab_1 .tabs_1 li").eq(0).mouseover();
	
	$("#tab_2 .tabs_1 li").mouseover(function(){
		$("#tab_2 .tabs_1 li").removeClass("selected");
		$(this).addClass("selected");
		$("#tab_2 .tabContent").hide();
		var con = $(this).attr('name');
		$("#tab_2 ."+con).show();
		$("#tab_2 ."+con+" .tabScrollbar").mCustomScrollbar("destroy");
		$("#tab_2 ."+con+" .tabScrollbar").mCustomScrollbar({
			scrollButtons:{
				enable:true
			},
			theme:"dark"
		});
		
	});
	$("#tab_2 .tabContent").hide();
	$("#tab_2 .tabs_1 li").eq(0).mouseover();
	
	
}
//设置快捷入口方式
function setQlink(){
	//$("#q_link").css("top",500);
	var top = $("#q_link").offset().top; 
	$(window).scroll(function() {
		var scrolla=$(window).scrollTop(); 
		var cha=parseInt(top)+parseInt( scrolla); 
		$("#q_link").css("top",cha);
	});
	$("#q_link .tab").click(function(){
		if($("#q_link").hasClass("max")){
			$("#q_link").removeClass("max").addClass("mini");
		}else{
			$("#q_link").removeClass("mini").addClass("max");
		}
	});
}

//打开周报
function openReport(id,flag){
	if(flag == '1')
		parent.f_addTab('viewReport_'+id,'查看周简报明细','../report/reportView?id='+id);
	else
		parent.f_addTab('viewReport_'+id,'编缉周简报明细','../report/reportGenerate?id='+id);
  	return;
}
function openTabNode(nodeTypeCode){
	var title = "";
	if(nodeTypeCode=='Cost'){
		title = "造价";
	}else if(nodeTypeCode=='Invite'){
		title = "招标";
	}
	parent.f_addTab('nodeDelay'+nodeTypeCode,title+'阶段延期','../nodeProjectBaseInfo/nodeDelay?nodeTypeCode='+nodeTypeCode+'&ta='+new Date().getTime());
}

//打开新的tab页面
function nTab(tabid,titlename,url){
	if(url.indexOf("?")>=0){
		url+='&ta='+new Date().getTime();
	}
	//parent.
	parent.f_addTab(tabid,titlename,url);

}

//弹出相关页面
function openDialog(id,title,h,w){
	h==null?300:h;
	w==null?600:w;
	$.ligerDialog.open({ target: $("#"+id),title :title,height:300,width:600 });

}

</script>
<style type="text/css">
</style>
</head>
<body style="overflow-x:hidden; "> 
<!--main-->
<div class="clearfix">
	<!--right-->

	<div class="main">
			
		<div class="notice clearfix">
			<h5><i>工作职责</i>工作职责</h5>
			<div class="clearfix">
				<div id="noticeContent" class="fl overh h45">
					<ul class=" con ">
						<li></li>
					</ul>
				</div>
				<div id="notice" class="fr arrow"></div>
			</div>
		</div><!--notice宽度=窗口宽度-250像素-->
					
			
		<div class="clearfix">
			<div id="tab_1" class="panel_1 fl"><!--panel_1=notice宽度-348像素-->
				<div class="tabs_1">
					<ul class="clearfix">
					<c:if test="${sessionScope.SEC_LIMITS['HOME_TODO_MY']||sessionScope.SEC_ADMIN}"> <!-- 我的待处理工作权限 -->
						<li name="HOME_TODO_MY" class="selected"><a href="javascript:void(0);"><i class="d4">12</i>我的待处理工作
						<font class="numred">${countOfFlow*1}</font></a></li>
					</c:if>
					</ul>
				</div>
				<table border="0" width="100%" cellpadding="0" cellspacing="0">
						<tr  class="HOME_TODO_MY tabContent" >
							<td width="236" valign="top">
								<div class="m10 mb0 "><img src="../resource/css/default/images/daiban.jpg"/></div>
								<!--a href="javascript:void(0);" class="more_1 fr m10 mb0"></a-->
							</td>
							<td > 
								<div class="tabScrollbar clearfix">
									<ul class="list clearfix">
										<c:if test="${sessionScope.SEC_LIMITS['NC_METRO_SKGZGL_DWCL']||sessionScope.SEC_ADMIN}">
											<li class="clearfix" onclick="javascript:nTab('NC_MYWORK_Home','待我处理','../flowwork/flowThreads?state=1');">
												<a href="javascript:void(0);">有<font class="numred">${countOfFlow}</font>流程待处理，马上处理</a><span class="dianji">[点击查看]</span>
											</li>
										</c:if>
																		
									</ul>
								</div>
							
							</td>
						</tr> 
					</table> 
					
			</div>
			<div class="panel_2 fr w328">
				<h5><i class="time">12</i>工作统计<!-- <a href="javascript:void(0);" class="more_1 fr m10"></a> --></h5>
				<div class="" style="overflow: hidden;">
					<div id="piechar" style="width:300px; height:210px;margin-left: -10px;margin-top: -3px;"></div>

				</div>
			</div>
			<div class="clear"></div>
			<div class="fl w328">
				<div class="panel_1 mb20"  style="height: 123px;">
					<h5><i class="wether">12</i>今日天气</h5>
					<dl class="wether" >
 					<iframe  allowTransparency="true" src="../resource/lib/jquery.weather.v3/weather.html" width="100%" height="87" scrolling="no"frameborder="0"></iframe>
					</dl>
				</div>
				<div class="panel_1"  style="height: 121px;">
					<h5><i class="link">12</i>工作提醒</h5>
					<ul class="p10 clearfix link">
					<c:if test="${sessionScope.SEC_LIMITS['NC_INVITE_RZLR']||sessionScope.SEC_ADMIN}">
						<li class="l1 bor_r"><a title="招标工作日志录入 " href="javascript:nTab('inviteLogMainTwo','招标工作日志录入','../inviteLog/logEditMain'),parent.hideLeft()">123</a></li>
					</c:if>
					<c:if test="${sessionScope.SEC_LIMITS['NC_PUBLIC_TLQ']||sessionScope.SEC_ADMIN}">
						<li class="l2 bor_r"><a title="合约部讨论区 " href="javascript:nTab('publicMessage','合约部讨论区 ','../message/messageList?partCode=publicMessage');">123</a></li>
					</c:if>
					<c:if test="${sessionScope.SEC_LIMITS['NC_COST_CXZB']||sessionScope.SEC_ADMIN}">
						<li class="l3 bor_r"><a title="周报查询" href="javascript:nTab('reportListView','周报维护 ','../report/reportList');">123</a></li>
					</c:if>
						<li class="l4"><a title="个人设置中心" href="javascript:nTab('accoutmy','个人设置中心','../accounts/editAccout');">123</a></li>
					</ul>
				</div>
			</div>
			<div id="tab_2" class="panel_1 fr">
				<div class="tabs_1">
					<ul class="clearfix">
					<c:if test="${sessionScope.SEC_LIMITS['HOME_PUBLIC_INFO']||sessionScope.SEC_ADMIN}"> <!-- 最新信息发布权限 -->
						<li name="tab_news" class="selected"><a href="javascript:void(0);"><i class="d4">12</i>最新信息发布
							</a></li>
					</c:if>
					<c:if test="${sessionScope.SEC_LIMITS['HOME_NEAR_REPORT']||sessionScope.SEC_ADMIN}"> <!-- 近期周报权限 -->
						<li name="tab_reports"><a href="javascript:void(0);"><i class="d5">12</i>近期周报
							</a></li>
					</c:if>
					<c:if test="${sessionScope.SEC_LIMITS['HOME_MESSAGE']||sessionScope.SEC_ADMIN}"> <!--最新讨论话题权限 -->
						<li name="tab_questions"><a href="javascript:void(0);"><i class="d6">12</i>最新讨论话题
							</a></li>
					</c:if>
					</ul>
				</div>
				<table border="0" width="100%" cellpadding="0" cellspacing="0">
					<tr  class="tab_news tabContent" >
						<td width="236" valign="top">
							<div class="m10 mb0 "><img src="../resource/css/default/images/zuixin.jpg"/></div>
							<a href="javascript:nTab('lowandrules','法律法规','../publishinfo/publishinfoList?publishType=LOWANDRULES');" class="more_1 fr m10 mb0"></a>
						</td>
						<td > 
							<div class="tabScrollbar clearfix">
								<ul class="list clearfix">
									<c:forEach items="${publishInfos}" var="publishInfo" varStatus="status" >
										<li class="clearfix">
											<a href="javascript:nTab('publishInfo${publishInfo.id}','信息详情','../publishinfo/publishinfoAdd?editable=false&publishType=${publishInfo.publishType}&id=${publishInfo.id}');" >
												[${publishInfo.publishType.v}]${publishInfo.title}
											</a><span>${publishInfo.publishDate}</span>
										</li>
									</c:forEach>
								</ul>
							</div>
						</td>
					</tr> 
					<tr  class="tab_reports tabContent" >
						<td width="236" valign="top">
							<div class="m10 mb0 "><img src="../resource/css/default/images/zuixin.jpg"/></div>
							<a href="javascript:nTab('reportListView','周报维护 ','../report/reportList');" class="more_1 fr m10 mb0"></a>
						</td>
						<td > 
							<div class="tabScrollbar clearfix">
								<ul class="list clearfix">
									<c:forEach items="${reports}" var="report" varStatus="status" >
										<li class="clearfix"><a href="javascript:openReport('${report.id}','${report.status}')">
										${report.name}&nbsp;${report.beginDate}&nbsp;~&nbsp;${report.endDate}</a><span>${report.status eq '1' ? '已发布':'未发布' }</span></li>
									</c:forEach>
								</ul>
							</div>
						
						</td>
					</tr> 
					<tr  class="tab_questions tabContent" >
						<td width="236" valign="top">
							<div class="m10 mb0 "><img src="../resource/css/default/images/zuixin.jpg"/></div>
							<a href="javascript:nTab('publicMessage','合约部讨论区 ','../message/messageList?partCode=publicMessage');" class="more_1 fr m10 mb0"></a>
						</td>
						<td > 
							<div class="tabScrollbar clearfix">
								<ul class="list clearfix">
									<c:forEach items="${messages}" var="message" varStatus="status" >
										<li class="clearfix"><a href="javascript:nTab('publicMessage${message.id}','合约部讨论区[${message.sendTime}] ','../message/messageEdit?messageID=${message.id}&partCode=publicMessage');">
										${message.title}</a><span>RE:${message.updateTime}</span></li>
									</c:forEach>
								</ul>
							</div>
						
						</td>
					</tr> 
				</table> 
			</div>
		</div>
	</div>
	<!--right end-->
</div>
<!--main end-->
<!--弹出层 start-->
<div style="display: none;">
	<div id='costSelfs'><!--个人造价延期-->
   		<table border="1" style="padding:10px;cursor:pointer;width: 98%;border-top: 1px solid #A3C0E8;border-left: 1px solid #A3C0E8;font-size: 12px;" class="l-grid-body-table">
   			<tr class="l-grid-row" style="background-color:#EEF4F5;">
   				<td class="l-grid-row-cell " style="text-align: left; line-height:25px;">项目名称</td>
   				<!-- <td nowrap="nowrap" class="l-grid-row-cell ">计划开始时间</td> -->
   				<td nowrap="nowrap" class="l-grid-row-cell ">计划结束时间</td>
   				<td nowrap="nowrap" class="l-grid-row-cell ">经办人</td>
   				<td nowrap="nowrap" class="l-grid-row-cell ">操作</td>
   			</tr>
   			<c:forEach items="${costsSelf }" var="cost">
   				<tr class="l-grid-row" onclick="openCost(${cost.id})">
    				<td class="l-grid-row-cell " style="text-align: left; line-height:25px;">${cost.projectName}</td>
    				<%-- <td class="l-grid-row-cell ">${cost.planBeginDate}</td> --%>
    				<td class="l-grid-row-cell ">${cost.planFinishDate}</td>
    				<td nowrap="nowrap" class="l-grid-row-cell ">${cost.ownerAccount.name}</td>
    				<td nowrap="nowrap" class="l-grid-row-cell ">[<a href="#">点击查看明细</a>]</td>
    			</tr>
		</c:forEach>
   		</table>
   	</div>
   	
</div>
<!--弹出层  end-->
</body>
</html>
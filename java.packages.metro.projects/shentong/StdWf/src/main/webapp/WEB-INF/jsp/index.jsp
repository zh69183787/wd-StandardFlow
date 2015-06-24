<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>上海申通地铁集团 标准管理系统</title>
     <link href="../resource/lib/ligerUI/skins/Aqua/css/ligerui-dialog.css" rel="stylesheet" type="text/css" /> 
    <link href="../resource/css/M-all.css" rel="stylesheet" type="text/css" />
    <link rel="shortcut icon" type="image/x-icon" href="../images/favicon.ico" media="screen" />
	<!--[if IE]>
    <script type="text/javascript" src="../js/html5.js"></script>
	<![endif]--> 
	<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
	
	<script src="../resource/lib/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script src="../resource/lib/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script> 
    <script src="../resource/lib/ligerUI/js/plugins/ligerLayout.js" type="text/javascript"></script> 
    <script src="../resource/lib/ligerUI/js/plugins/ligerTab.js" type="text/javascript"></script> 
	<script src="../resource/lib/ligerUI/js/plugins/ligerTree.js" type="text/javascript"></script> 
	<script src="../resource/lib/ligerUI/js/plugins/ligerAccordion.js" type="text/javascript"></script> 
    	<script src="../resource/lib/ligerUI/js/plugins/ligerMenu.js" type="text/javascript"></script> 
    
        <script type="text/javascript">
            var tab = null;
            var tree = null;
            var homeLay = null;
            $(function ()
            {
            	setQlink();//设置快捷入口方式
                //布局
                homeLay = $("#layout1").ligerLayout({ leftWidth: 190,centerWidthDiff: 25, height: '100%',heightDiff:-34,space:4, onHeightChanged: f_heightChanged });

                var height = $(".l-layout-center").height();

                //Tab
                $("#framecenter").ligerTab({ height: height,contextmenu:true,onBeforeSelectTabItem:onBeforeSelectTabItem,onAfterSelectTabItem:onAfterSelectTabItem});

                
                $(".l-link").hover(function ()
                {
                    $(this).addClass("l-link-over");
                }, function ()
                {
                    $(this).removeClass("l-link-over");
                });

                tab = $("#framecenter").ligerGetTabManager();
                $(".l-tab-loading").show();
            	
                tree = $("#tree1").ligerGetTreeManager();
                $("#pageloading").hide();
                
				var content = "<p>1.招标讨论区已上线 </p><p>2.造价工作管理模块整体上线</p>";//发布信息内容
				var isShow = true; // 是否 开启、关闭
               // f_tip2(isShow,content);   // xzhk20130918
               
				changeNc(); //fk 20131026
				
				
				$(".con").click(function(event){
					event.stopPropagation();
					$(this).siblings(".selul").slideToggle(200).css("display","block");
				});
				$(".selul a").click(function(event){
					event.stopPropagation();
					var _this = $(this);
					_this.parents(".selul").siblings(".con").html(_this.html()).attr("_num",_this.parent().index())
					_this.addClass("cur").parent().siblings().find("a").removeClass("cur");
					$(".selul").slideUp(100);
				});
				$(document).click(function() { 
					$(".selul").slideUp(100); 
				});

            });
            function f_tip() {
            	$.ligerDialog.tip({ title: '提示信息',content:'记录已经删除！'+num++ });
            }
            
            var tip;
            function f_tip2(isShow,content) {
            	if(isShow){
            		if (!tip) {
    	           		tip = $.ligerDialog.tip({ title: '系统更新信息', content: content });
    	            } else {
    	            	var visabled = tip.get('visible');
    	           		if (!visabled) tip.show();
    	            		tip.set('content', content);
                	}
            	}
            } 
            
            function f_heightChanged(options)
            {
                if (tab)
                    tab.addHeight(options.diff);
                //fk 20131026
                if ($("#accordion1 .leftmenu").eq(0).ligerGetAccordionManager() && options.middleHeight - 24 > 0){
                	$("#accordion1 .leftmenu").each(function(){
                		$(this).ligerGetAccordionManager().setHeight(options.middleHeight - 24);
                	});
                }
                    
            }
            function f_addTab(tabid,text, url)
            { 
                tab.addTabItem({ tabid : tabid,text: text, url: url });
            	if(tab.getTabItemCount() > 6){
            		tab.removeTabItem($("li:eq(1)", tab.tab.links.ul).attr("tabid"));
            	}   
            	hideLeft();	//默认展开
            } 
            function reloadTabid(tabid){
            	tab.reload(tabid);
            }
            function onBeforeSelectTabItem(tabid){
            	if(tabid=='NC_HOME_Home'){
            		hideLeft();
        			$(".l-layout-collapse-left").hide();
            	}else{
            		if(homeLay.isLeftCollapse){
            			$(".l-layout-collapse-left").show();	            			
            		}
        			//homeLay.setLeftCollapse(false);  //不再自动收缩
            	}
            	if(tab.getSelectedTabItemID() != tabid){
            		if(tabid == 'NC_HOME_Home' || tabid == 'NC_FLOWWORK_DEAL')reloadTabid(tabid);
            	}
            	
            }
            function onAfterSelectTabItem(tabid){
            	//$(".l-tab-loading").hide();
            	//alert(0);
            }
            // 隐藏左边菜单栏
            function hideLeft(){
            	homeLay.setLeftCollapse(true);
            }
            
            //初始化 左侧菜单    fk 20131026
            function changeNc(){
            	var height = $(".l-layout-center").height();
            	$("#accordion1 .leftmenu").ligerAccordion({ height: height - 24, speed: null });
            	$("#accordion1 .leftmenu").hide();
            	$("#M_menu li").click(function(event){
            		event.stopPropagation();
            		//$(".l-tab-loading").show();
            		$("#M_menu li").removeClass("selected");
            		$(this).addClass("selected");
            		var code = $(this).attr("code");
            		var src = $(this).attr("src");
            		$("#accordion1 .leftmenu").hide();
            		$("#"+code).show();
            		
            		var tabid = code+"_Home";
            		
            		if(tab.isTabItemExist(tabid) ){//判断tab是否存在 
            			tab.selectTabItem(tabid);
            			//if(tabid == 'NC_HOME_Home')onloadFinal();
            			//if(tabid == 'NC_FLOWWORK_DEAL')onloadFinal();
            			//onloadFinal();
            		}else{
            			if(src.indexOf("?")>0){
                			src+="&a="+new Date().getTime();
                		}else{
                			src+="?a="+new Date().getTime();
                		}
            			var title=$(this).find("i").text()+"主页";
            			f_addTab(tabid,title,src);
            			
            		}
            		
            		if(code=='NC_HOME'){//  如果是主页  则隐藏左侧菜单
            			hideLeft();
            			/* $("#homeiframe").show();
            			$("#framecenter").find("li[tabid='NC_HOME_Home']").show(); */
            			$(".l-layout-collapse-left").hide();
            			
            		}else{
            			$(".l-layout-collapse-left").show();
            			homeLay.setLeftCollapse(false);
            		/* 	$("#homeiframe").hide();
            			$("#framecenter").find("li[tabid='NC_HOME_Home']").hide(); */
            		}
            		
            	});
            	
            	$("#M_menu li").eq(0).click(); // 默认打开第一个标签
            
            }
            
            function onloadFinal(){
            	$(".l-tab-loading").hide();
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
            	
            	var opencontenH = $("#q_link .open li").length;
            	if(opencontenH>5){
            		$("#q_link .open").height(opencontenH*35);
            	}
            }
          //关闭快捷入口
          function closeQ(){
        	  $("#q_link").removeClass("max").addClass("mini");
          }
          //打开新的tab页面
            function nTab(tabid,titlename,url){
            	if(url.indexOf("?")>=0){
            		url+='&ta='+new Date().getTime();
            	}
            	//parent.
            	f_addTab(tabid,titlename,url);

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
     </script> 
<style type="text/css"> 
    body,html{height:100%;}
    body{ padding:0px; margin:0;   overflow:hidden;}  
    .l-link2{text-decoration:underline; color:white; margin-left:2px;margin-right:2px;}
    .l-layout-top{background:#102A49; color:White;}
    .l-layout-bottom{ background:#E5EDEF; text-align:center;}
    #pageloading{position:absolute; left:0px; top:0px; background:white url('../resource/lib/images/loading.gif') no-repeat center; width:100%; height:100%;z-index:99999;}
    .l-winbar{ background:#2B5A76; height:30px; position:absolute; left:0px; bottom:0px; width:100%; z-index:99999;}
    .space{ color:#E7E7E7;}
    /* 顶部 */ 
    .l-topmenu{ margin:0; padding:0; height:31px; line-height:31px; background:url('../resource/lib/images/top.jpg') repeat-x bottom;  position:relative; border-top:1px solid #1D438B;  }
    .l-topmenu-logo{ font-family:微软雅黑;font-size:14px;font-weight:bold;color:#E7E7E7; padding-left:35px; line-height:26px;background:url('../resource/lib/images/topicon.gif') no-repeat 10px 5px;}
    .l-topmenu-menu{  font-family:微软雅黑;font-size:14px;font-weight:bold; position:absolute; height:24px; line-height:24px;  right:180px; top:2px;color:#070A0C;}
    .l-topmenu-menu a{  font-family:微软雅黑;font-size:14px;font-weight:bold;color:#E7E7E7; text-decoration:underline} 
	.l-topmenu-welcome{  font-family:微软雅黑;font-size:12px; position:absolute; height:24px; line-height:24px;  right:30px; top:2px;color:#070A0C;}

 </style>
</head>
<body>  
<c:out value=""></c:out>
<div id="pageloading"></div>  
<!--header-->
	<header>
		<div class="clearfix">
			<div class="welcome fr clearfix">
				<!-- <div onclick="f_addTab('accoutmy','个人设置中心','../accounts/editAccout')" class="fr m10">个人设置</div>
				 -->
				<c:if test="${sessionScope.SSO_FLAG ne 'true'}">
 					<div class="selbox">
				        <div class="con" id="n_Con" _num="0">个人中心</div>
				        <ul class="selul">
				          <!-- <li><a href="javascript:f_addTab('accoutmy','个人设置中心','../accounts/editAccout')" class="cur">个人设置</a></li> -->
				          <li><a href="javascript:f_addTab('changepassword','密码修改','changepassword')" >密码修改</a></li>
				        </ul>
			      	</div>
				</c:if>
				
				<div class="fr pl10">
				<c:set var='himg' value=''/>
				<c:if test="${sessionScope.SEC_CUR_ACCOUNT.headimg!=null&&sessionScope.SEC_CUR_ACCOUNT.headimg!=''}">
					<c:set var='himg' value='${sessionScope.SEC_CUR_ACCOUNT.id}'/>
				</c:if>
					<img src="../resource/headimg/headimg${himg}.png"/>
				</div>
				
				<span class="fr">欢迎您<h5>［${sessionScope.SEC_CUR_USER_NAME}］</h5>
					<c:if test="${sessionScope.SSO_FLAG ne 'true'}">
						<a href="../secure/logout" id="logout"  class="lo"  target="_self">退出</a>
					</c:if>
				</span>
			</div>
			<%-- <div class="tx fr">&nbsp;</div>
			<div class="fr tt">欢迎您<font color="#FFF">[${sessionScope.SEC_CUR_USER_NAME }]</font>登录管理系统</div> --%>
		</div>
		<menu>
			<ul id="M_menu">
				<c:if test="${sessionScope.SEC_LIMITS['INDEX_PAGE']||sessionScope.SEC_ADMIN}"> <!-- 拥有招标管理权限 -->
					<li code="NC_HOME" src="../index/homeIndex"><a href="#" ><i class="zh">首页</i>首页</a></li>
				</c:if>	
				<c:if test="${sessionScope.SEC_LIMITS['NC_METRO_GRSWGL']||sessionScope.SEC_ADMIN}"> 
					<li code="NC_MYWORK" src="../flowwork/flowThreads?state=1"><a href="#" ><i class="zh">个人事务</i>个人事务</a></li>
				</c:if>
			</ul>
		</menu>
</header>
<!--header end-->
<%-- <div id="topmenu" class="l-topmenu">
    <div class="l-topmenu-logo">
	    <ul style="float:left;"><img src="../resource/css/default/images/logo_2.png" alt="" /></ul>
	    <ul style="float:left;padding-left:15px;">南昌轨道交通集团 合约部管理系统</ul>
    </div>
	 <div></div> 
	 <div  class="l-topmenu-welcome">	 
        <span  class="l-link2">欢迎你，${sessionScope.SEC_CUR_USER_NAME }</span>&nbsp;&nbsp;&nbsp;
        <a href='changepassword'    class="l-link2"  target="_self">修改密码</a>&nbsp;&nbsp;&nbsp;
        <a href="logout" id="logout"  class="l-link2"  target="_self">退出</a>	 	
    </div> 
</div> --%>
	<div id="layout1" style="width:100%; margin:0 auto; margin-top:0px; "> 
		<div position="left"  title="主要菜单" id="accordion1"style="dispaly:none"> 
			
			<div id="NC_MYWORK" class="leftmenu"  style="dispaly:none">
			<!-- 拥有个人事务权限 -->
				<c:if test="${sessionScope.SEC_LIMITS['NC_METRO_GRSWGL']||sessionScope.SEC_ADMIN}"> 
					<!-- fk new-->
					<c:if test="${sessionScope.SEC_LIMITS['NC_METRO_GRGZ']||sessionScope.SEC_ADMIN}"> 
						<div title="个人工作" topcode="NC_INVITE" class="l-scroll">
							<c:if test="${sessionScope.SEC_LIMITS['NC_METRO_BZHGZ']||sessionScope.SEC_ADMIN}">
								<a class="l-link" href="javascript:f_addTab('NC_METRO_BZHGZ','立项申请','../stdApply/list')">立项申请</a>
							</c:if>
							<c:if test="${sessionScope.SEC_LIMITS['NC_METRO_SSG']||sessionScope.SEC_ADMIN}">
								<a class="l-link" href="javascript:f_addTab('NC_METRO_SSG','送审稿','../stdReview/list')">送审稿</a>
							</c:if>					
							<c:if test="${sessionScope.SEC_LIMITS['NC_METRO_BPG']||sessionScope.SEC_ADMIN}">
								<a class="l-link" href="javascript:f_addTab('NC_METRO_BPG','报批稿','../stdApprove/list')">报批稿</a>
							</c:if>										
						</div>
					</c:if>
					<c:if test="${sessionScope.SEC_LIMITS['NC_METRO_SKGZGL']||sessionScope.SEC_ADMIN}"> 
						<div title="审核工作管理" topcode="NC_INVITE" class="l-scroll">
							<c:if test="${sessionScope.SEC_LIMITS['NC_METRO_SKGZGL_WOFQDLC']||sessionScope.SEC_ADMIN}"> 
								<a class="l-link" href="javascript:f_addTab('NC_FLOWWORK_START','我发起的流程','../flowwork/flows')">我发起的流程</a>
							</c:if>
							<c:if test="${sessionScope.SEC_LIMITS['NC_METRO_SKGZGL_DWCL']||sessionScope.SEC_ADMIN}"> 
								<a class="l-link" href="javascript:f_addTab('NC_MYWORK_Home','待我处理','../flowwork/flowThreads?state=1')">待我处理</a> 
							</c:if>
							<c:if test="${sessionScope.SEC_LIMITS['NC_METRO_SKGZGL_QCLWCD']||sessionScope.SEC_ADMIN}">  
								<a class="l-link" href="javascript:f_addTab('NC_FLOWWORK_DEALED','我处理完成的','../flowwork/flowThreads?state=2')">我处理完成的流程</a>  
							</c:if>
							<c:if test="${sessionScope.SEC_LIMITS['NC_METRO_SKGZGL_LCGL']||sessionScope.SEC_ADMIN}">  
								<a class="l-link" href="javascript:f_addTab('NC_FLOWWORK_TYPE','流程管理','../flowworkType/list')">流程管理</a>  
							</c:if>
						</div>
					</c:if>
					<!-- fk new-->
				</c:if><!-- END个人事务权限 -->
			</div>
		</div>

        <div position="center" id="framecenter"> 
        <c:if test="${sessionScope.SEC_LIMITS['INDEX_PAGE']||sessionScope.SEC_ADMIN}">
            <div id="hometab" tabid="NC_HOME_Home" title="我的主页" style="height:300px;margin-left: 0px;" onclick="" >
            	<div style="" class="l-tab-loading"></div>
                <iframe frameborder="0" name="homeiframe" id="homeiframe" src="../index/homeIndex" onload="onloadFinal();"></iframe>
            </div> 
            </c:if>
        </div> 
        
    </div>
     <footer>Copyright © 2013 万达信息股份有限公司 , All Rights Reserved</footer>
    <div style="display:none"></div>
    
    
<!-- 快捷操作 -->
    <div id="q_link" class="q_link mini">

	</div>
</body>
</html>

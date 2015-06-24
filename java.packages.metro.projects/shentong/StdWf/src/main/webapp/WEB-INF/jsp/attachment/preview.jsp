<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">	
    <head> 
        <title></title>         
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
        <link href="../../resource/lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
        <style type="text/css" media="screen"> 
			html, body	{ height:100%; }
			body { margin:0; padding:0; overflow:auto; }   
			#flashContent { display:none; }
        </style>
		<script src="../../resource/lib/jquery/jquery-1.7.1.min.js" type="text/javascript"></script> 
		<script src="../../resource/lib/jquery-jsonp/jquery.jsonp.js" type="text/javascript"></script> 
		<script type="text/javascript" src="../../resource/preview/js/swfobject/swfobject.js"></script>
		<script type="text/javascript" src="../../resource/preview/js/flexpaper_flash.js"></script>
		
        <script type="text/javascript"> 
        	var isReady = ${isReady};
        	var isImage = ${isImage};
        	var filepath = '${filepath}';
        	
        	$(function(){
            	if(!isImage){
               		if(!isReady){
               			doConvert();        			
               		}else{
               			loadSwf();
               		}        		
            	}        		
        	})
        	
        	function doConvert() {
        		$("#loading").show();
        		$("#loading div").css({"paddingTop":($("#loading").height()/2)-30,"paddingLeft":($("#loading").width()/2)-65});
        		
        	 	var inData = {
        			'filepath' : filepath
        		};
        	 	
        		$.ajax({
        			 type: 'post',
        	         url: '../../print/doConvert?ts='+(new Date().getTime()),
        	         cache: false,
        	         dataType: 'json',
        	         data: inData,
        	         async:false,
        	         success: function (data){
        	        	 $("#loading").hide();
        	        	 data = eval(data);
        	        	 if(data && data.IsSuccess == 'true'){
        	        		 loadSwf();
        	        	 }else{
 				        	alert('文件编码有误，无法转化，请联系开发人员。您可以先点击下载按钮，直接下载源文件进行查看');
				        	window.close();        	        		 
        	        	 }
        	         },
        			 error: function (data){
        				 $("#loading").hide();
        				 alert('文件编码有误，无法转化，请联系开发人员！您可以先点击下载按钮，直接下载源文件进行查看');
        				 window.close();
        			 }
        		});
        	 	
/* 				$.jsonp({
				    url: '${converterUrl}',			//替换成要访问的跨域路径 !!!
				    data: inData,
				    callbackParameter: "callback",
				    success: function (json) {
				    	$("#loading").hide();
				        if(json.IsSuccess == 'true'){
				        	loadSwf();
				        }else{
				        	alert('文件编码有误，无法转化，请联系开发人员。您可以先点击下载按钮，直接下载源文件进行查看');
				        	window.close();
				        }
				    },
				    error: function (xOptions, textStatus) {
				    	$("#loading").hide();
				    	alert('文件编码有误，无法转化，请联系开发人员！您可以先点击下载按钮，直接下载源文件进行查看');
				    	window.close();
				    }
				}); */		

        				
        	/* 	var inData = {
        			};
        			//用JQUERY取跨域的数据 ,这里的路径可以换成是你要访问其它的路径，
        			//比如你要访问CSR中的某个action中的方法:
        			//http://128.160.96.15:9125/crm_essp/ocrmweb/com.ccb.cddev.csr.service.trasaction/sendTransactionForCCOB.action
        			//将路径改掉
        			jQuery.getJSON('http://127.0.0.1:8080/TestB/b1.jsp?jsoncallback=?',
        					inData, callback);		 */	
        	}

        	function loadSwf(){
                var swfVersionStr = "9.0.124";
                var xiSwfUrlStr = "${expressInstallSwf}";
                var flashvars = { 
                      SwfFile : escape("../../attachment/loadSwf/${attachId}?v="+new Date().getTime())  				 
    				  };
    			 var params = {
    				
    			    }
                params.quality = "high";
                params.bgcolor = "#ffffff";
                params.allowscriptaccess = "sameDomain";
                params.allowfullscreen = "true";
                var attributes = {};
                attributes.id = "FlexPaperViewer";
                attributes.name = "FlexPaperViewer";
                swfobject.embedSWF(
                    "../../resource/preview/FlexPaperViewer.swf", "flashContent", 
                    "955", "600",
                    swfVersionStr, xiSwfUrlStr, 
                    flashvars, params, attributes);
    			swfobject.createCSS("#flashContent", "display:block;text-align:left;");       
        	}
        </script> 
        
    </head> 
    <body>
    	<div class="l-tab-loading" id="loading" style="display: none;"><div>正在生成预览文件，请稍后。。。</div></div>
    	<c:if test="${isImage}">
			<img src="../../attachment/loadSwf/${attachId}?inSwf=false"></img>    	
    	</c:if>
    	<div style="position:absolute;left:10px;top:10px;">
	        <div id="flashContent"> 
	        	<p> 
		        	To view this page ensure that Adobe Flash Player version 
					9.0.124 or greater is installed. 
				</p> 
				<script type="text/javascript"> 
					var pageHost = ((document.location.protocol == "https:") ? "https://" :	"http://"); 
					document.write("<a href='http://www.adobe.com/go/getflashplayer'><img src='" 
									+ pageHost + "www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /></a>" ); 
				</script> 
	        </div>
        </div>
   </body> 
</html> 
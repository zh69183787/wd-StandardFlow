;(function($) {
	/**
	 *  var options={
        		formID:'form',  // form  id
        		formButID:'save_add', // form 提交按钮  id
        		formAction:'saveInvite', // form 执行action
        		formType:'post',
        		formHasParent:true,  // 是否有父页面  弹出窗口用
        		formParentSearchButID:'searchbtn'
        	};
	 */
	var callBackFunction = null;   // xzhk 添加加提交完成回调函数
	var beforSubmitFunction = null;        // xzhk 添加提交前调用函数
	$.fn.ajaxFormSub = function(options, callBack, beforSubmit) {
		
		if(arguments.length == 1){
			callBackFunction = null;  // xzhk 添加加回调函数
			beforSubmit = null;
		}else{
			callBackFunction = callBack;  // xzhk 添加加回调函数
			beforSubmitFunction = beforSubmit;
		}
		// alert(callBack);
		// var callBack = options.callBack;
		var form_id = options.formID;
		var form_button = options.formButID; 
		var form_url = options.formAction; 
		var form_type = options.formType; 
		var form_sbutton = null;
		var formHasParent = options.formHasParent;
		var AttaValidationID = options.AttaValidationID==null?'null':options.AttaValidationID;
		var AttaValidationFunction = (options.AttaValidationFunction==null||options.AttaValidationFunction==''||options.AttaValidationFunction==undefined)?null:options.AttaValidationFunction;   // xzhk
		var isSaveRefresh = options.isSaveRefresh==null?false:options.isSaveRefresh;
		if(formHasParent){
			form_sbutton = options.formParentSearchButID;
		}
		
		$.validator.addMethod("notnull", function(value, element, regexp) {
			if (!value)
				return true;
			return !$(element).hasClass("l-text-field-null");
		}, "");

		$.metadata.setType("attr", "validate");
		var v = $("#" + form_id).validate({
			// 调试状态，不会提交数据的
			debug : true,
			errorPlacement : function(lable, element) {

				if (element.hasClass("l-textarea")) {
					element.addClass("l-textarea-invalid");
				} else if (element.hasClass("l-text-field")) {
					element.parent().addClass("l-text-invalid");
				}
				$(element).removeAttr("title").ligerHideTip();
				$(element).attr("title", lable.html()).ligerTip();
			},
			success : function(lable) {
				var element = $("#" + lable.attr("for"));
				if (element.hasClass("l-textarea")) {
					element.removeClass("l-textarea-invalid");
				} else if (element.hasClass("l-text-field")) {
					element.parent().removeClass("l-text-invalid");
				}
				$(element).removeAttr("title").ligerHideTip();
			},
			submitHandler : function() {
				saveFormData(form_id, form_button, form_url, form_type,form_sbutton,formHasParent,isSaveRefresh);
			}
		});
		$("#" + form_id).ligerForm();

		$("#" + form_button).click(function() {
			var dateTime = new Date().getTime();
			if($("#timeParam").length<=0){
				$("#" + form_id).append("<input type='hidden' id='timeParam' name='timeParam' value='"+dateTime+"' />");
			}else{
				$("#timeParam").val(dateTime);
			}
			/////////////  xzhk  /////////////////
			if(AttaValidationFunction != null){
				if(AttaValidationFunction() == false){// 不通过
					//alert("请上传必选附件！");
					$.ligerDialog.alert('请上传必选附件！', '提示');
				}else{
					$("#" + form_id).submit();
				}
			}else{
				if($("#"+AttaValidationID).length>0){
					if($("#"+AttaValidationID).val()!='1'){// 不通过
						//alert("请上传必选附件！");
						$.ligerDialog.alert('请上传必选附件！', '提示');
					}else{
						$("#" + form_id).submit();
					}
				}else{
					$("#" + form_id).submit();
				}
			}
			////////////// end //////////////////
			
		});
	};

	// 保存信息
	function saveFormData(form_id, form_button, form_url, form_type,form_sbutton,formHasParent,isSaveRefresh) {
		
		var options = {
  				cache:false,
  				type:'post',
  				dataType:'text',
  				url:form_url,
  				beforeSubmit:function(){
  					if(beforSubmitFunction != null)beforSubmitFunction();
  					if(formHasParent){
  						parent.showWaitDialog();// = $.ligerDialog.waitting('正在操作中,请稍候...');
  					}
  				},
  				success:function(data){
  					//waitD.close();
  					if(parent.waitDialog!=null){parent.waitDialog.close();}
  					if(callBackFunction != null)callBackFunction(data);  // xzhk 添加加回调函数
  					
  					if(!isSaveRefresh){
  						if(data){
  	  						$.ligerDialog.alert('操作成功!', '提示',function(){
  								if(formHasParent){
  									if($("#"+form_sbutton,window.parent.document).length>0){
  										$("#"+form_sbutton,window.parent.document).click();
  									}
  									
  									if(parent.winDialog!=null){parent.winDialog.close();}
  								}
  							});
  	  					}else{
  							$.ligerDialog.alert('操作失败', '提示',function(){});
  	  					}
  					}
  					return false;
  				}
  			};
  			
			$.ligerDialog.confirm("您确定要保存数据？",function (r){ 
				if(r){
					$("#"+form_id).ajaxSubmit(options);
				}
			});


	}
	
})(jQuery);
/** @serializedParams looks like "prop1=value1&prop2=value2".   
Nested property like 'prop.subprop=value' is also supported **/ 
function formParamString2obj (serializedParams) {       
   var obj={}; 
   function evalThem (str) { 
	//var value = escape(decodeURIComponent(data.substr(li_pos+1)));
       var attributeName = str.split("=")[0]; 
//       var attributeValue = decodeURIComponent(str.split("=")[1]); 
//		attributeValue = encodeURI(encodeURI(attributeValue));
       var attributeValue = decodeURIComponent(str.split("=")[1],true);   


       if(!attributeValue){ 
           return ; 
       } 
         
       var array = attributeName.split("."); 
       for (var i = 1; i < array.length; i++) { 
           var tmpArray = Array(); 
           tmpArray.push("obj"); 
           for (var j = 0; j < i; j++) { 
               tmpArray.push(array[j]); 
           }; 
           var evalString = tmpArray.join("."); 
           // alert(evalString); 
           if(!eval(evalString)){ 
               eval(evalString+"={};");                 
           } 
       }; 
       eval("obj."+attributeName+"='"+attributeValue+"';"); 
         
   }; 
   var properties = serializedParams.split("&"); 
   for (var i = 0; i < properties.length; i++) { 
       evalThem(properties[i]); 
   }; 
   return obj; 
} 
//
//function ajaxFormSub(form_id, form_button, form_url, form_type,form_sbutton) {
//
//	
//
//}

(function($) {
	$.fn.requiredValidator = function() {
		
	    $.metadata.setType("attr", "validate");
	    var v = this.validate({
	        debug: true,
	        errorPlacement: function (lable, element)
	        {

	            if (element.hasClass("l-textarea"))
	            {
	            	if(element.hasClass("l-htmleditor")){
	            		var htmleditor = $('.xheLayout',element.parent());
	            		htmleditor.css("border-color","#FF7777");
	            		htmleditor.removeAttr("title").ligerHideTip();
	            		htmleditor.attr("title", lable.html()).ligerTip();
	            	}
	                element.addClass("l-textarea-invalid");
	            }
	            else if (element.hasClass("l-text-field"))
	            {
	                element.parent().addClass("l-text-invalid");
	            }
	            $(element).removeAttr("title").ligerHideTip();
	            $(element).attr("title", lable.html()).ligerTip();
	        },
	        success: function (lable)
	        {
	            var element = $("#" + lable.attr("for"));
	            if (element.hasClass("l-textarea"))
	            {
	            	if(element.hasClass("l-htmleditor")){
	            		var htmleditor = $('.xheLayout',element.parent());
	            		htmleditor.css("border-color","#ABC6DD");
	            		htmleditor.removeAttr("title").ligerHideTip();
	            	}	            	
	                element.removeClass("l-textarea-invalid");
	            }
	            else if (element.hasClass("l-text-field"))
	            {
	                element.parent().removeClass("l-text-invalid");
	            }
	            $(element).removeAttr("title").ligerHideTip();
	        },
	        submitHandler: function ()
	        {
	            alert("Submitted!");
	        }
	    });
	};
	 
})(jQuery);

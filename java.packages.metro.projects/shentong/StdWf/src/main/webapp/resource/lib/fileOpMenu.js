(function($) {
	$.fn.fileOpMenu = function(options) {
		
		options = options || {};
		
		var attachId;
		var relativePath = options.relativePath || '../attachment/';
		var allowPreview = (options.preview == false ? false : true);
		var allowDownload = (options.download == false ? false : true);
		
        function preview(){
        	window.open(relativePath+'preview/'+attachId);
        }
        
        function download(){
        	window.open(relativePath+'download/'+attachId);
        }
        
        var ops = [];
        if(allowPreview) ops.push({ text: '预览', click: preview });
        if(allowDownload) ops.push({ text: '下载', click: download });
		
        var menu = $.ligerMenu({ top: 100, left: 100, width: 90, items:ops});

        this.bind("click", function (e)
		{
        	attachId = $(this).attr('id');
        	menu.show({ top: e.pageY, left: e.pageX });
        	return false;
		});
		
	};

})(jQuery);

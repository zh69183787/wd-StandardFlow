(function($) {
	$.fn.searchBox = function(p) {
		
		var autocomplete = this.autocomplete(p['data'], {
	        minChars: 0,
	        width: 300,
	        scrollHeight: 300,
	        max: 15,
	        mustMatch: false,
	        multiple: false,
	        multipleSeparator: ',',
	        matchContains: true,
	        autoFill: false,
	        formatItem: p['formatItem'],
	        formatMatch: p['formatMatch'],
	        formatResult: p['formatResult']
		}).result(p['result']).blur(p['blur']);
		
		return autocomplete;
	};
	 
})(jQuery);

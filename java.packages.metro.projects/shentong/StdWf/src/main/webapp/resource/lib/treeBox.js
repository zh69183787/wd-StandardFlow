(function($) {
	$.fn.treeBox = function(options) {
		
	    function onBeforeExpand(note)
	    {
	    	if (note.data.children && note.data.children.length == 0)
	        {
	    		combo.treeManager.loadData(note.target,'../accounts/findSubAllOfGroup/'+note.data.id);	
	        }
	    }
	    function onBeforeAppend(parentNode, data)
	    {
	    	for(var i = 0; i < data.length; i++){
	    		var o = data[i];
	    		if(o.nodetype){   //部门属性
	        		o.children = [];
	        		o.isexpand = false;    			
	    		}
	    	}
	    	return true;
	    }    
	    function onBeforeSelect(node){
	    	if(node.data.children) return;
	    	changeOwner(node.data.id,node.data.group.id);
	    }
	    function onBeforeCancelSelect(node){
	    	changeOwner('','');
	    }
	    function onClick(node){
	    	if(node.data.children) return;
	    	combo._toggleSelectBox(true);
	    }
	    function changeOwner(accountId,groudId){
	    	$("#"+options.valueField).val(accountId);
	    	//$("#ownerDept").val(groudId);
	    }
		
		var combo =  this.ligerComboBox({
            width: 180,
            selectBoxWidth: 180,
            selectBoxHeight: 200, 
            textField: 'name',
            tree: { 
            	url: '../invite/findSubAllOfGroupByCode/HYGLB',
            	checkbox: false,
            	textFieldName: 'name',
                onBeforeSelect: onBeforeSelect,
                onBeforeCancelSelect: onBeforeCancelSelect,            	
            	onBeforeAppend: onBeforeAppend, 
            	onBeforeExpand: onBeforeExpand,
            	onClick: onClick
                }
		});
		
		return combo;
	};

	
})(jQuery);

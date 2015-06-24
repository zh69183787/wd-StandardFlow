Ext.define('security.view.dictionary.DictionaryManagePanel', {
	extend: 'Ext.panel.Panel',
	alias: 'widget.dictionarymgrpanel',
	requires: [
        'security.view.dictionary.DictionaryGrid',
        'security.view.dictionary.DictionaryTree'
    ],
	title: '维护字典',
    layout: {
        type: 'hbox',
        align: 'stretch'
    },
	bodyPadding: 1,
    initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			defaults: {
                border: true
            },
			items : [ {
				xtype : 'dictionarygrid',
				flex : 1,
    	        searchable: true,
    	        pagable: true,
    	        operable: true
			}, {
    	        xtype: 'splitter',
    	        defaultSplitMin: 100,
    	        collapsible: true
    	    }, {
				xtype : 'dictionarytree',
				flex : 1
			} ]
		});

		me.callParent(arguments);
	}
});
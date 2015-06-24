Ext.define('security.controller.GroupController', {
    extend: 'Ext.app.Controller',
    requires: [
	    'security.view.group.GroupWin', 
	    'security.view.group.RootGroupWin',
	    'security.view.account.AccountListWin'
    ],

    stores: ['Group', 'RootGroup'],
    
    models: ['Group', 'RootGroup'],

    views: [
        'group.GroupWin',
        'group.GroupTree',
        'group.RootGroupWin',
        'group.RootGroupGrid',
        'group.GroupManagePanel',
        'account.AccountGrid',
        'account.AccountListWin'
    ],
    
    refs: [{
        ref: 'groupTree',
        selector: 'grouptree'
    },{
        ref: 'accountGrid',
        selector: 'panel[title="部门管理"] > accountgrid'
    },{
        ref: 'rootGroupWin',
        selector: 'rootgroupwin'
    },{
        ref: 'rootGroupGrid',
        selector: 'rootgroupgrid'
    },{
    	ref: 'groupTree',
        selector: 'grouptree'
    },{
    	ref: 'accountGrid1',
        selector: 'accountlistwin > accountgrid'
    }],

    init: function() {
    	
    	this.control({
    		'rootgroupgrid button[tooltip="添加"]': {
    			click: this.showRootGroupWin
    		},
    		'rootgroupwin button[text="保存"]': {
    			click: this.saveRootGroup
    		},
    		'rootgroupgrid actioncolumn': {
    			click: this.doAction
    		},
    		'panel[title="部门管理"] > rootgroupgrid': {
    			selectionchange: this.onRootGroupGridSelectionChange
    		},
    		'groupmanagepanel > grouptree': {
    			itemcontextmenu: this.onGroupTreeItemcontextmenu
    		},
    		'groupwin button[text="保存"]': {
                click: this.saveGroup
            },
            'panel[title="部门管理"] > grouptree': {
                selectionchange: this.onGroupTreeSelectionChange
            }
    	});
    },
    
    showRootGroupWin: function(btn, e, eOpts, rec) {
        var win = Ext.getCmp('deptwin');
        if (!win) {
            win = Ext.create('security.view.group.RootGroupWin');
        }
    	win.show(btn, function() {
            var f = win.child('form').getForm();
            if (!rec) {
                rec = Ext.create('security.model.RootGroup');
            }
            f.loadRecord(rec);
        });
    },
    
    saveRootGroup: function() {
    	var win = this.getRootGroupWin(),
        f = win.child('form').getForm();
    
	    if (f.isValid()) {
	        f.updateRecord();
	        var groupTreeStore = this.getGroupTree().getStore(),
	        	rootGroupStore = this.getRootGroupGrid().getStore(),
	            rootGroup = f.getRecord();
	        
	        rootGroup.set('parent', {id: 1});
	        rootGroup.set('nodetype', 'root');
	        
	        rootGroup.save({
	            success: function(rootGroup) {
	                win.hide();
	                rootGroupStore.loadPage(1);
	                groupTreeStore.load();
	            }
	        });
	    }
    },
    
    doAction: function(grid, cell, row, col, e, eOpts) {
        var rec = grid.getStore().getAt(row),
        	action = e.target.getAttribute('class');
        
        if (action.indexOf("x-action-col-0") != -1) { // edit user
            this.showRootGroupWin(e.target, e, eOpts, rec);
        } else if (action.indexOf("x-action-col-1") != -1) { // delete user
            this.deleteRootGroup(rec);
        }
    },

    deleteRootGroup: function(rec) {
    	
    	if(!rec.get('leaf')) {
    		Ext.Msg.alert("提示","所选组织部门下面存在单位或部门，不能删除!");
			return;
    	}
    	
    	var id = rec.get('id');
        Ext.Msg.confirm('提示', '你确定要删除吗?', function(btn) {
            if (btn == 'yes') {
                var rootGroupStore = this.getRootGroupGrid().getStore();
                Ext.create('security.model.RootGroup', {
                    id: id
                }).destroy({
                    success: function() {
                    	rootGroupStore.loadPage(1);
                    }
                });
            }
        }, this);
    },
    
    onRootGroupGridSelectionChange: function(model, selected, eOpts) {

    	var treeStore = this.getGroupTree().getStore(),
	    accountGridStore = this.getAccountGrid().getStore();
    	
    	if(selected.length != 0) {
	
    		this.getGroupTree().rootVisible = true;
	    	treeStore.setRootNode({
	            text: selected[0].get('name'),
	            id: selected[0].get('id'),
	            nodetype: selected[0].get('nodetype'),
	            expanded: true
	        });
	    	accountGridStore.removeAll();
    	} else {

    		treeStore.setRootNode({
	            text: '系统组织机构',
	            id: '1',
	            expanded: true
	        });
    	}
    },
    
    onGroupTreeItemcontextmenu: function(view, record, item, index, e, eOpts ) {
    	if(!this.ctmenu) {
			this.ctmenu = Ext.widget('menu', {
				width: 80,
				items:[{
					text: '添加部门',
					icon: 'icons/group_add.png',
					scope: this,
					handler: function(menuItem) {
						this.showGroupWin(menuItem, 'add');
					}
		        },'-',{
		        	text: '编辑部门',
		        	icon: 'icons/group_edit.png',
		        	scope: this,
					handler: function(menuItem) {
						this.showGroupWin(menuItem, 'edit');
					}
		        },'-',{
		        	text: '查看帐户',
		        	icon: 'icons/application_view_list.png',
		        	scope: this,
					handler: function(menuItem) {
						this.showAccountListWin(menuItem);
					}
		        },'-',{
		        	text: '删除资源',
		        	scope: this,
		        	icon: 'icons/group_delete.png',
					handler: function(menuItem) {
						this.deleteGroup(menuItem);
					}
		        }]
		    });
		}
		e.preventDefault();
		this.ctmenu.showAt(e.getXY());
	},
	
	showAccountListWin: function(menuItem) {
				
		var node = this.getGroupTree().getSelectionModel().getLastSelected();

		if(node.get('nodetype') == 'D' || node.isRoot()) {
			Ext.Msg.alert("提示","所选不是部门，没有帐户可查看!");
			return;
		}
		
		var win = Ext.getCmp('accountlistwin');
		if(!win) {
			win = Ext.widget('accountlistwin');
		}
		
		accountGridStore = this.getAccountGrid1().getStore();
		accountGridStore.setProxy({
	        type: 'rest',
	        url: 'accounts/findByGroupId'
	    });
		accountGridStore.getProxy().setExtraParam('groupId', node.get('id'));
		
		win.show(menuItem);
		accountGridStore.load();
	},
	
	showGroupWin: function(menuItem, actionType) {
		
		var node = this.getGroupTree().getSelectionModel().getLastSelected();

//		if(node.isRoot()) { return; }
		
		if(node.get('nodetype') == 'B' && 'add' == actionType) {
			
			Ext.Msg.alert("提示","所选为部门，不能对其新增部门!");
			return;
		}
			
		if (!node.isExpanded()) {
			node.expand();
		}
		
		var win = Ext.getCmp('groupwin');
		if(!win) {
			win = Ext.widget('groupwin');
		}
		
		var f = win.child('form').getForm();
		
		if('add' == actionType) {

			var record = Ext.create('security.model.Group', {
				'parent': {id: node.get('id')}
			});
			
			f.loadRecord(record);			
			var parentText = node.get('text');
	        f.findField('parentText').setValue(parentText);
			win.show(menuItem);
		} else {
			
			node.set('parent', {id: node.parentNode.get('id')});
			 
			win.show(menuItem, function() {
	            f.loadRecord(node);	            
	            var parentText = node.parentNode.get('text');
	            f.findField('parentText').setValue(parentText);
	        });
		}
	},
	
    saveGroup: function(btn) {          
        
        var win = Ext.getCmp('groupwin'),
			f = win.child('form').getForm(),
			groupStore = this.getGroupStore();
        
        if (f.isValid()) {
            f.updateRecord();
            var group = f.getRecord();
            
            group.save({
                success: function(group) {
                    win.hide();
                }
            });
            
            if(group.get('id') == ''){
            	Ext.Msg.alert("提示","新增成功!");
            	groupStore.load();
            }else{
            	var node = this.getGroupTree().getSelectionModel().getLastSelected();
            	node.set('text', group.get('name'));
            	node.set('version', group.get('version')+1);
            	Ext.Msg.alert("提示","更新成功!");
            }
        }
    },
    
    onGroupTreeSelectionChange: function(model, selected, eOpts) {
    	
		if (selected && selected.length) {

			var record = selected[0], 
				groupId = record.get('id'), 
				accountStore = this.getAccountGrid().getStore();
			
			accountStore.setProxy({
		        type: 'rest',
		        url: 'accounts/findByGroupId'
		    });
			
			accountStore.getProxy().setExtraParam('groupId', groupId);
			accountStore.reload();
		}
    },
    
    deleteGroup: function(menuItem) {
		var selectedNode = this.getGroupTree().getSelectionModel().getLastSelected(),
			parentNode = selectedNode.parentNode,
			accountStore = this.getAccountGrid().getStore();
		if(selectedNode.isRoot()){
			Ext.Msg.alert('提示','根节点无法删除!');
			return;
		}
		if(accountStore.getCount() > 0){
			Ext.Msg.alert('提示','该节点有账号关联,无法删除!');
			return;
		}
		if(selectedNode != null){
	        var isLeaf = selectedNode.isLeaf();
			if(isLeaf){
				Ext.Msg.show({
					title:'提示',
					msg: '确定删除节点?',
					buttons: Ext.Msg.YESNO,
					icon: Ext.Msg.QUESTION,
					fn: function(btn){
                        if(btn=='yes'){
                        	selectedNode.destroy({
                        		success: function() {
                        			if(parentNode.childNodes.length == 0 ){
                                		parentNode.set('leaf',true);
                                	}
                                }
                            });
				    	}
				    }
				});
			}else{
				Ext.Msg.alert('提示','请选择子节点删除!');
			}
		}else{
			Ext.Msg.alert('提示','请先选择一个节点!');
		}
    }

});

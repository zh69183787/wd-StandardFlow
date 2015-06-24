<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" >
<head>
<title></title>
<link href="../resource/lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
<script src="../resource/lib/jquery/jquery-1.7.1.min.js" type="text/javascript"></script> 
    
<script src="../resource/lib/ligerUI/js/core/base.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerForm.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerGrid.js" type="text/javascript"></script> 
<script src="../resource/lib/ligerUI/js/plugins/ligerTextBox.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerCheckBox.js" type="text/javascript"></script>
 <script src="../resource/lib/ligerUI/js/plugins/ligerMenu.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerComboBox.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerMenuBar.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerToolBar.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerButton.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerResizable.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerDrag.js" type="text/javascript"></script>
<script src="../resource/lib/ligerUI/js/plugins/ligerDateEditor.js" type="text/javascript"></script>

<script src="../resource/lib/ligerUI/js/plugins/ligerResizable.js" type="text/javascript"></script> 

<script type="text/javascript">  
var alert = function (content) {
    $.ligerDialog.alert(content);
};

var gridManager = null;

$(function () {

    //工具条
    $("#toptoolbar").ligerToolBar({ 
    	items: [
          { text: '增加',
	          id: 'add', 
	       click: itemclick}
    	]
    });

    
    $("#searchbtn").ligerButton({ text : '查询',
    	click: function () {
	         if (!gridManager) return;
	         gridManager.setOptions(
	             { parms:getParms() }
	         );
	         gridManager.loadData(true);
	    }
	});
    
    $('form').ligerForm();
    loadGrid();
    gridManager.loadData(true);
});

function loadGrid() {
      //表格
	$("#maingrid").ligerGrid({pkName: 'id', 
		columns: [
		{ display: '流程定义名称',     name: 'flowTypeName' },
		{ display: '流程定义编码',     name: 'flowTypeCode' },
		{ display: '流程基本信息简述',     name: 'flowDescription' },
		{ display: '关联类名',   width:80,   name: 'objectClass' },
		{ display: '关联类链接地址', width:250,    name: 'linkStr' },
	/* 	{ display: '创建时间',     name: 'cDate' },
		{ display: '最后修改时间',     name: 'cDate' }, */
		{ display: '创建人',     name: 'cUser' },
		{ display: '流程定义状态',     name: 'state' ,render: function(v) {
			return v.state == '0' ? '启用':'未启用';
		}},
		{ display: '操作', name: 'view' , isSort: false, render: function(v){
			var str = '<a href="javascript:editType('+v.id+')">修改</a>';
				/* str += '<a href="javascript:editDef('+v.id+')">编辑流程定义</a>&nbsp;&nbsp;';
				str += '<a href="javascript:view('+v.id+')">查看流程定义</a>' */;
			return str;
		}}], 
		 parms:getParms(),
		dataAction: 'server', 
		//data: {Rows:[{"projectName":"aaa"}],Total:1},
		url : '../flowworkType', //默认的方法  不用单独写
		width : '100%',
		height : '100%',
		pageSize : 20,
		newPage : 1,
		sortName:'id',
		sortOrder:'desc',
		rownumbers : true,
		method : 'GET',
		checkbox : false,
		allowUnSelectRow:true
	});
	      
	gridManager = $("#maingrid").ligerGetGridManager();
	$("#pageloading").hide();
};

function getParms(){
	var parms = [];
	$("input[searchFlag='search']").each(function (){
		var name = $(this).attr('name');
		var value = $(this).val();
		parms.push({ name: name, value: value});
	});
	return parms;
}
var winDialog = null;
function itemclick(item) {
    if(item.id){
        switch (item.id) {
            case "add":
            		editType(null);
            	 return;		     
        }
    }
}

function editType(flowTypeId){
	var strUrl = "add?a="+new Date().getTime();
	//alert(flowTypeId);
	if(flowTypeId!=null&&flowTypeId!=''){
		strUrl += "&flowTypeId="+flowTypeId;
	}
	winDialog = $.ligerDialog.open({ title: '流程管理编辑', url: strUrl, 
   		showMin: false, showMax: true,isHidden:false }, function(){winDialog = null;});
        winDialog.max();
        

	//parent.f_addTab('flowWorkType','流程管理编辑',strUrl);
}

function editDef(flowTypeId){
	parent.f_addTab('flowDefinition','流程定义管理','../flowDefinition/edit?flowTypeId='+flowTypeId);
}

</script>
</head>
<body style="padding:0px; overflow:hidden;"> 
<div class="l-loading" style="display:none;" id="pageloading"></div> 
<form id="form1" runat="server"> 
<div id="toptoolbar"></div>
	
<div class="l-panel-search">
	<table cellpadding="0" cellspacing="0" class="l-table-edit" >
        <tr style="height: 30px;">
			<td width="100px" align="right" class="l-table-edit-td" style="font-size:12px;">名称：</td>
			<td width="100px" align="center" class="l-table-edit-td"  style="font-size:12px;">
				<input id="name" name="search_flowTypeName_like" type="text" searchFlag='search' ltype="text"/> 
			</td>
			<td width="40px" align="center" class="l-table-edit-td"  style="font-size:12px;">编码</td>
			<td width="100px" align="center" class="l-table-edit-td"  style="font-size:12px;">
				<input id="code" name="search_flowTypeCode_like" type="text" searchFlag='search' ltype="text"/>
			</td>
			<td width="200px" align="center" class="l-table-edit-td"  style="font-size:12px;">
				<div id="searchbtn" style="width:80px;"></div>
			</td>
		</tr>
    </table>
</div>
<div id="maingrid" style="margin:0; padding:0"></div>
</form>
<div style="display:none;"></div>
</body>
</html>

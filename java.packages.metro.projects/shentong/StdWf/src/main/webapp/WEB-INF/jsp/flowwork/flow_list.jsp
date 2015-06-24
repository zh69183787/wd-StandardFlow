<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>

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

$(function (){

 /*    //工具条
    $("#toptoolbar").ligerToolBar({ 
    	items: [
          { text: '增加',
	          id: 'add', 
	       click: itemclick}
    	]
    }); */

    
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
   // gridManager.loadData(true);
});

function loadGrid() {
      //表格
	$("#maingrid").ligerGrid({pkName: 'id', 
		columns: [
		{ display: '流程类型名称',width:200, name: 'flowTypeName' },
		{ display: '标准名称', width:200, name: 'flowName' },
		/* { display: '流程基本信息简述',width:160, name: 'flowDescription' }, */
	/* 	{ display: '创建时间',     name: 'cDate' },
		{ display: '最后修改时间',     name: 'cDate' }, */
		{ display: '发起人', width:50, name: 'userName' },
		{ display: '发起时间',width:125, name: 'startTime' },
		{ display: '当前进度',width:225, name: 'flowHandler' },
		{ display: '流程状态',width:60, name: 'state' ,render: function(v) {
			return v.state == '0' ? '暂存':'进行中';
		}},
		{ display: '操作',width:60, name: 'view' , isSort: false, render: function(v){
			var str ="";
			
				str += "<a href=javascript:editBo('"+v.flowUid+"')>查看流程</a>";
			
			return str;
		}}], 
		parms:getParms(),
		dataAction: 'server', 
		//data: {Rows:[{"projectName":"aaa"}],Total:1},
		url : '../flowworkProcess', //
		width : '100%',
		height : '100%',
		pageSize : 20,
		newPage : 1,
		sortName:'updateTime',
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

function editBo(flowUid){
	parent.f_addTab("flowInfo"+flowUid,"流程信息","../flowwork/flowInfo?flowUid="+flowUid+"&editFlag=false");
}

function viewBo(flowTypeId){
	parent.f_addTab('flowDefinition','流程定义管理','../flowDefinition/edit?flowTypeId='+flowTypeId);
}

</script>
</head>
<body style="padding:0px; overflow:hidden;"> 
<div class="l-loading" style="display:none;" id="pageloading"></div> 
<form id="form1" runat="server"> 
	<input name="contentsType" type="hidden" value="0" searchFlag='search' ltype="text"/>
<div id="toptoolbar"></div>
<div class="l-panel-search" style="height: 32px;">
	<table cellpadding="0" cellspacing="0" class="l-table-edit" >
        <tr style="height: 30px;">
			<td width="100px" align="right" class="l-table-edit-td" style="font-size:12px;"> 流程类型名称：</td>
			<td width="100px" align="center" class="l-table-edit-td"  style="font-size:12px;">
				<input  name="search_flowTypeName_like" type="text" searchFlag='search' ltype="text"/>
				<input  name="search_userId_eq" type="hidden" searchFlag='search' value="${sessionScope.SEC_CUR_ACCOUNT_ID}"/> 
				 
			</td>
			<td width="100px" align="right" class="l-table-edit-td" style="font-size:12px;">标准名称：</td>
			<td width="100px" align="center" class="l-table-edit-td"  style="font-size:12px;">
				<input  name="search_flowName_like" type="text" searchFlag='search' ltype="text"/>
			</td>
			<td width="20px" align="center" class="l-table-edit-td"  style="font-size:12px;"></td>
			<td width="100px" align="center" class="l-table-edit-td"  style="font-size:12px;">
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

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
   /*  //工具条
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
		{ display: '流程类型名称',width:200, name: 'flowWorkProcess.flowTypeName' },
		{ display: '标准名称', width:200, name: 'flowWorkProcess.flowName' },
		{ display: '发起人',width:50, name: 'flowWorkProcess.userName' },
		/* { display: '流程发起时间',width:125,  name: 'flowWorkProcess.startTime' },
		{ display: '流程结束时间',width:125, name: 'flowWorkProcess.endTime' }, */
		{ display: '开始处理时间',width:125, name: 'startTime' },
		{ display: '处理完成时间',width:125, name: 'endTime' },
		{ display: '流程当前进度',width:225,     name: 'flowWorkProcess.flowHandler' },
		{ display: '流程状态',width:60, name: 'flowWorkProcess.state' ,render: function(v) {
			return v.flowWorkProcess.state == '2' ? '已通过' : v.flowWorkProcess.state == '3' ? '未通过' : '进行中';
		}},
		{ display: '操作',width:60, name: 'view' , isSort: false, render: function(v){
			var str ="";
			var state = $("#state").val();
			var name = "查看流程";
			if(state == '1'){//暂存
				name="处理";
			}
				str += "<a href=javascript:editflow('"+v.flowUid+"') >"+name+"</a>";
			
			return str;
		}}], 
		parms:getParms(),
		dataAction: 'server', 
		//data: {Rows:[{"projectName":"aaa"}],Total:1},
		url : '../flowworkThread', //
		width : '100%',
		height : '100%',
		pageSize : 20,
		newPage : 1,
		sortName:'flowWorkProcess.updateTime',
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
	var parms = [
	             {name:'search_flowWorkProcess_NNULL', value:'null'},
	             {name:'search_flowWorkProcess.state_NNULL', value:'null'},
	             {name:'a', value:new Date().getTime()}
	             ];
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

function editflow(flowUid){
	parent.f_addTab('flowInfo'+flowUid,'流程信息','../flowwork/flowInfo?flowUid='+flowUid+'&editFlag=true');
}

function editDef(flowTypeId){
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
				<input  name="search_flowWorkProcess.flowTypeName_like" type="text" searchFlag='search' ltype="text"/> 
				<input  name="search_userId_eq" type="hidden" searchFlag='search' value="${sessionScope.SEC_CUR_ACCOUNT_ID}"/> 
				<input id="state"  name="search_state_eq" type="hidden" searchFlag='search' value="${state}"/> 
				<%-- <if test="${state==1}">
					<input  name="search_flowWorkProcess.state_ne" type="hidden" searchFlag='search' value="2"/> 
				</if> --%>
			</td>
			<td width="100px" align="right" class="l-table-edit-td" style="font-size:12px;">标准名称：</td>
			<td width="100px" align="center" class="l-table-edit-td"  style="font-size:12px;">
				<input  name="search_flowWorkProcess.flowName_like" type="text" searchFlag='search' ltype="text"/>
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

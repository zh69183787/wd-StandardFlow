<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title></title>
    <link href="../resource/lib/ligerUI/skins/Aqua/css/ligerui-all.css" rel="stylesheet" type="text/css" />
    <script src="../resource/lib/jquery/jquery-1.5.2.min.js" type="text/javascript"></script> 
    <script src="../resource/lib/ligerUI/js/core/base.js" type="text/javascript"></script>
    <script src="../resource/lib/ligerUI/js/plugins/ligerGrid.js" type="text/javascript"></script> 
    <script src="../resource/lib/ligerUI/js/plugins/ligerToolBar.js" type="text/javascript"></script>
    <script src="../resource/lib/ligerUI/js/plugins/ligerDialog.js" type="text/javascript"></script>
    <script src="../resource/lib/ligerUI/js/plugins/ligerDrag.js" type="text/javascript"></script>
    <script src="../resource/lib/ligerUI/js/plugins/ligerDateEditor.js" type="text/javascript"></script>
    
    <script src="../resource/lib/ligerUI/js/plugins/ligerResizable.js" type="text/javascript"></script> 
    <script src="../resource/flow/flowwork.js" type="text/javascript"></script> 
    <script type="text/javascript">
        var grid = null;
        var gridManager = null;
        $(function () {
            $(".l-date").ligerDateEditor({});
            var params = getParms();
            gridManager = $("#maingrid").ligerGrid({
    			columns: [
    		    			{ display: '标准名称', width: 300, name: 'stdName'},
    		    			{ display: '标准编号', width: 140, name: 'stdNo' },
    		    			{ display: '申报时间', width: 100, name: 'applyDate' },
    		    			{ display: '申报进度', width: 140, name: 'state', render: function(row){
    		    				if(row.flowWorkProcess){
    		    					if(row.flowWorkProcess.state == '0'){
    		    						return '未提交';
    		    					}else if(row.flowWorkProcess.state == '2'){
    		    						return '通过';
    		    					}else if(row.flowWorkProcess.state == '3'){
    		    						if(row.flowWorkProcess.flowStage){
    		    							return row.flowWorkProcess.flowStage + ' 未通过';   
    		    						}	
    		    						return '未通过';
    		    					}else{
    		    						if(row.flowWorkProcess.flowStage){
    		    							return row.flowWorkProcess.flowStage + ' 进行中';    		    							
    		    						}
    		    						return '审批中';
    		    					}
    		    				}
    		    				return '未提交';
    		    			}},
    		    			{ display: '操作', name: 'ops',isSort: false, width: 200, render: function(row){
    		    				var flowgroup = row.flowGroup;
    		    				var state = (row.flowWorkProcess ? row.flowWorkProcess.state : '');
    		    				var str ="";
    		    				var id = row.id;

								var name = "";
								if(flowgroup != null && flowgroup != ""){
									name = "查看审批单";
    		    				}else{
    		    					name = "发起审核";
    		    				}
								var tit = row.stdName;
								
								var reg=new RegExp(",","g");
								tit=tit.replace(reg,"");
								reg = new RegExp("'","g");
								tit=tit.replace(reg,"");
								tit=tit.replace(/\s/ig,'');
								
								str += "<a href=javascript:openFlow('"+flowgroup+"','FLOW_STD_REVIEW_${sessionScope.SEC_CUR_DEPT.groupcode}','"+id+"','"+tit+"','searchbtn') >"+name+"</a>&nbsp;&nbsp;";
								
								if(state == '2'){
									if(row.stdApprove && row.stdApprove.flowGroup){
										str += "<a href=javascript:openFlow('"+row.stdApprove.flowGroup+"','FLOW_STD_APPROVE_${sessionScope.SEC_CUR_DEPT.groupcode}','"+row.stdApprove.id+"','"+tit+"','searchbtn') >查看报批稿</a>&nbsp;&nbsp;";;	
									}else{
										str += "<a href='javascript:addStdApprove("+id+")'>填写报批稿</a>&nbsp;&nbsp;";										
									}
								}
								
    		    				return str;
    		    			}
    		    			}], 
    			rownumbers : true,
    			dataAction: 'server', 
    			url : '../stdReview',
    			checkbox : true,
                width: '100%',
                height:'99%',
    			pageSize : 20,
    			parms: params,
    			newPage : 1,
    			sortName:'flowWorkProcess.updateTime',
    			sortOrder:'desc',
    			method : 'GET'
            });

            $('form :text').keyup(function(e) {
				if (e.which == 13) {
               		$('form :button').trigger('click');
                   	return false;
				}
           });
            $('#search_workName_like').focus();
        });
        function viewStd(id){
           	parent.f_addTab('std'+id,'标准制修订立项表详情','../stdReview/add?editable=false&id='+id);
        }
        function startNewFlow(id,flowCode,title,skipStartThread){
        	openFlow('',flowCode,id,title,'searchbtn',skipStartThread);
        }        
        var winDialog;
        function itemclick(item)
        { 
            if(item.id)
            {
                switch (item.id)
                {
                    case "add":
                    	winDialog = $.ligerDialog.open({ title: '新增送审表', height: 420, url: '../stdReview/add?editable=true', 
                   		width: 720, showMax: true, isResize: true, slide: false }); 
                   		winDialog.max();
                    	return;
                    case "modify":
                    	 var rowsdata = gridManager.getCheckedRows();
                         if(rowsdata.length != 1){
                         	alert('请选择1行');
                         	return;
                         }
                    	winDialog = $.ligerDialog.open({ title: '编辑送审表', height: 420, url: '../stdReview/add?editable=true&id='+rowsdata[0].id+'&ts='+new Date().getTime(), 
                     	width: 720, showMax: true, isResize: true, slide: false }); 
                     	winDialog.max();
                        return;
                    case "view":
                        return;                       
                    case "delete":
                    	var data = gridManager.getCheckedRows();
                        var ids = '';
                        if (data.length == 0){
                        	alert('请选择行');
                        }else{
                            $(data).each(function (){    
                                //checkedIds.push(this.id);
                                ids +='ids='+this.id+'&';
                            });
                            $.ligerDialog.confirm('确定删除?', function(r){
                                
                                if(r){
                                	$.ajax({
                                    	url:'deleteByIds',
                                    	dataType:'text',
                                    	type: 'post',
                                    	data: ids+'ts='+new Date().getTime(),
                                    	success:function(json){
                                    		f_search();
                                    	}
                                    }); 
                                }else{
                                	// alert(10);
                                }
                            }); 
                        }
                        return;
                }   
            }
        }
        function f_search(){
			if (!gridManager) return;
			var parms = getParms();
			gridManager.setOptions({ parms: parms});			
			gridManager.loadData(true);
			//$('#search_workName_like').focus();
        }
        function rst(){
			$('form').find("input[type='text']").each(function(){
				$(this).val('');
			});
		 	
        }
        function getParms(){
    		var parms = [
    			{ name: 'search_removed_eq', value: 0},{name: 'search_cUserId_EQ',value: '${sessionScope.SEC_CUR_ACCOUNT_ID}'}
    		];        	
     		$("input[name^='search'], select[name^='search']").each(function (){
    			var name = $(this).attr('name');
    			var value = $(this).val();
    			parms.push({ name: name, value: value});
    		});
			var state = $('#state').val();
			if(state != ''){
				parms.push({name:'search_flowWorkProcess.state_eq',value:state});
			}     		
    		return parms;
        }
        function showWaitDialog(){
        	waitDialog = $.ligerDialog.waitting('正在操作中,请稍候...');
        }
        
        function flow(objid){
        	winDialog = $.ligerDialog.open({ title: '流程', height: 420, url: '../publishinfo/publishinfoAdd?editable=true&publishType=${publishType}&id='+rowsdata[0].id+'&ts='+new Date().getTime(), 
           	width: 720, showMax: true, isResize: true, slide: false }); 
           	winDialog.max();
        }
        
        function addStdApprove(srId){
        	winDialog = $.ligerDialog.open({ title: '新增报批稿', height: 420, url: '../stdApprove/add?editable=true&srId='+srId, 
           		width: 720, showMax: true, isResize: true, slide: false }); 
           	winDialog.max();
            return;
        }        
    </script>
    <style type="text/css">
	input{ border: 1px solid #AECAF0; height:19px; line-height:19px;vertical-align: middle; padding-left: 2px; padding-top: 1px;}
	select{ border: 1px solid #AECAF0; height:22px; }
	
</style>
</head>
<body  style="padding:0px; overflow:hidden;">
<div id="toptoolbar"></div> 
<div class="l-panel-search" style="height: 54px">
<form>
	<table cellpadding="0" cellspacing="0" border="0" class="l-table-edit" width="100%">
		<tr style="height:25px;">
			<td width="10%" align="right" class="l-table-edit-td">标准名称：</td>
			<td align="left" class="l-table-edit-td">
				<input type="text" style="width:90%" name="search_stdName_like"/>
			</td>			
			<td align="right" class="l-table-edit-td">申报状态：</td>
			<td align="left" class="l-table-edit-td">
				<select id="state">
					<option value="">全部</option>
					<option value="0">未提交</option>
					<option value="1">审批中</option>
					<option value="2">通过</option>
					<option value="3">未通过</option>
				</select>
			</td>			
			<td align="right" class="l-table-edit-td">标准编号：</td>
			<td align="left" class="l-table-edit-td">
				<input type="text" style="width:90%" name="search_stdNo_like"/>
			</td>									
			<td align="right" class="l-table-edit-td">申报时间：</td>
			<td align="left" class="l-table-edit-td">
				<div class="l-panel-search-item"><input type="text" name="search_applyDate_ge" class="l-date"/></div>
				<div class="l-panel-search-item"> ~ </div>	
				<div class="l-panel-search-item"><input type="text" name="search_applyDate_le" class="l-date"/></div>	
			</td>
		</tr>
		<tr style="height: 25px;">
			<td align="center" class="l-table-edit-td" colspan="8">
				<table>
					<tr>
						<td width="50%" align="right" style="padding-right: 9px;">
							<input type="button" id="searchbtn" onclick="f_search()" class="l-button" style="height:22px;line-height:22px;" value="查询"></input>
						</td>
						<td>
							<input type="reset" value="重置" class="l-button l-button-reset" style="height:22px;line-height:22px;" ></input>
						</td>
					</tr>
				</table>
			</td>
		</tr>	
    </table>
</form>
</div>
    <div id="maingrid" style="margin:0; padding:0"></div>
 
</body>
</html>

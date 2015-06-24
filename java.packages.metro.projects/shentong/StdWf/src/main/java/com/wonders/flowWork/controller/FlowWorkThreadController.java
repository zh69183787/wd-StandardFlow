package com.wonders.flowWork.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wonders.flowWork.entity.FlowWorkThread;
import com.wonders.flowWork.entity.FlowWorkType;
import com.wonders.flowWork.repository.FlowWorkThreadRepository;
import com.wonders.flowWork.service.FlowWorkThreadService;
import com.wonders.framework.attachment.repository.AttachmentRepository;
import com.wonders.framework.controller.AbstractCrudController;
import com.wonders.framework.repository.MyRepository;
import com.wonders.framework.utils.DateUtil;
import com.wonders.util.StringUtil;

@Controller
@RequestMapping("flowworkThread")
public class FlowWorkThreadController extends AbstractCrudController<FlowWorkThread,Long>{
	@Inject
	FlowWorkThreadRepository flowWorkThreadRepository;
	@Inject
	FlowWorkThreadService flowWorkThreadService;
	@Inject
	AttachmentRepository attachmentRepository;
	@Override
	protected MyRepository<FlowWorkThread, Long> getRepository() {
		// TODO Auto-generated method stub
		return flowWorkThreadRepository;
	}
	
	/**
	 * 保存新增数据
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "saveData")
	public @ResponseBody
	String saveData(@RequestParam Map<String, ?> params,
			HttpSession session){
		FlowWorkThread flowThread = new FlowWorkThread();
		Object id = params.get("flowThread.id");
		if(id==null||"".equals(id)){
			//isadd = true;
			/*flowType.setcUser(session.getAttribute("SEC_CUR_USER_NAME").toString());
			flowType.setcDate(DateUtil.getToday());*/
		}else{
			flowThread = flowWorkThreadRepository.findOne(Long.parseLong(id.toString()));//findOne(flowType.getId());
			if(flowThread == null || flowThread.getState()!=1){
				return "{success: false,message:'保存失败，您可能与其他操作人操作冲突，请刷新页面后在进行操作！'}";
			}
			StringUtil.setValueNc(params, flowThread, "flowThread");
			String now = DateUtil.getNowTime();
			flowThread.setEndTime(now);
			flowThread.setState(2);
			
			String attrgroup = flowThread.getAttachGroup();
			Long alen = attachmentRepository.countAttachment(attrgroup);
			
			if(alen<=0){
				flowThread.setAttachGroup(null);
			}
			
			String message = flowWorkThreadService.saveThreadAndDoNext(flowThread);
			if(message==null){
				return "{success: false,message:'请刷新后重试！'}";
			}
		}
		return "{success: true,message:''}";
	}
	/**
	 * 删除
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "del")
	public @ResponseBody
	Map<String,Object> del(@RequestParam Map<String, ?> params,
			HttpSession session){
		Map<String,Object> map = null;
		String threadId = (String) params.get("threadId");
		if(threadId==null||"".equals(threadId)){
			map = new HashMap<String,Object>();
			map.put("success", false);
			map.put("message", "删除失败，请刷新后重试！");
		}else{
			map = flowWorkThreadService.del(Long.parseLong(threadId));
		}
		return map;
	}
	/**
	 * 添加下一步操作人
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "addUsers")
	public @ResponseBody
	Map<String,Object> addUsers(@RequestParam Map<String, ?> params,HttpSession session){
		Map<String,Object> map = null;
		String userIds = (String) params.get("userIds");
		String userNames = (String) params.get("userNames");
		String threadId = (String) params.get("threadId");
		String addFlag = (String) params.get("addFlag");
		String nodeName = (String) params.get("addNodeName");
		
		
		if(userIds!=null&&userNames!=null&&threadId!=null&&!"".equals(userIds)&&!"".equals(userNames)&&!"".equals(threadId)){
			map = flowWorkThreadService.addUsers(Long.parseLong(threadId), userIds, userNames,addFlag,nodeName);
		}else{
			map = new HashMap<String,Object>();
			map.put("success", false);
			map.put("message", "操作失败，请选择处理人！");
		}
		return map;
	}
	
	/**
	 * 替换下一步操作人
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "replace")
	public @ResponseBody
	Map<String,Object> replace(@RequestParam Map<String, ?> params,
			HttpSession session){
		Map<String,Object> map = null;
		String threadId = (String) params.get("threadId");
		String userIds = (String) params.get("userIds");
		String userNames = (String) params.get("userNames");
		if(threadId==null||"".equals(threadId)){
			map = new HashMap<String,Object>();
			map.put("success", false);
			map.put("message", "选择失败，请刷新后重试！");
		}else{
			map = flowWorkThreadService.replace(Long.parseLong(threadId),Long.parseLong(userIds),userNames);
		}
		return map;
	}
	
}

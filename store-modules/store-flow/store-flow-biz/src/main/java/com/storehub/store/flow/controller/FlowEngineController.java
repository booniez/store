package com.storehub.store.flow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.alibaba.nacos.shaded.com.google.common.collect.Maps;
import com.storehub.store.common.core.exception.CheckedException;
import com.storehub.store.common.core.util.R;
import com.storehub.store.common.security.annotation.Inner;
import com.storehub.store.flow.api.constants.FlowConstants;
import com.storehub.store.flow.api.dto.json.ChildNode;
import com.storehub.store.flow.api.entity.BizProcessTemplateEntity;
import com.storehub.store.flow.service.BizProcessTemplateService;
import com.storehub.store.flow.utils.BpmnModelUtils;
import com.storehub.store.flow.utils.IdWorker;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.*;
import org.flowable.bpmn.model.Process;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.delegate.ExecutionListener;
import org.springframework.http.HttpHeaders;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
//import org.flowable.bpmn.

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import static com.storehub.store.flow.utils.BpmnModelUtils.*;

/**
 * 流程引擎
 *
 * @author store
 * @date 2024-03-22 10:28:56
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/engine" )
@Tag(description = "engine" , name = "流程引擎" )
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class FlowEngineController {
	private final IdWorker idWorker;
	private final RepositoryService repositoryService;
	private final BizProcessTemplateService templateService;

	/**
	 * 转化JSON
	 * @param jsonObject
	 * @return
	 */
	@Operation(summary = "转化JSON" , description = "利用引擎实现 JSON 数据到模型的转化" )
	@PostMapping("/convert/{templateId}" )
	@Inner(value = false)
//	@PreAuthorize("@pms.hasPermission('engine_convert_edit')" )
	public R getBizBrandPage(@RequestBody JSONObject jsonObject, @PathVariable("templateId") String templateId) {
		try {
			ChildNode childNode = JSONObject.parseObject(JSON.toJSONString(jsonObject), new TypeReference<ChildNode>(){});

			BizProcessTemplateEntity template = templateService.getById(Long.valueOf(templateId));
			template.setTemplateJson(JSON.toJSONString(jsonObject));
			templateService.updateById(template);

			jsonObject.put("processJson", jsonObject);
			BpmnModel bpmnModel = assemBpmnModel(jsonObject, childNode, template.getName(), template.getName(), 1,
					template.getId().toString());
			repositoryService.createDeployment()
					.addBpmnModel(template.getName() + ".bpmn", bpmnModel)
					.name(template.getName())
//					.category("1" + "")
					.deploy();
		} catch (Exception e) {
			System.err.println(e.fillInStackTrace());
		}
		return R.ok();
	}

	private BpmnModel assemBpmnModel(JSONObject jsonObject, ChildNode childNode, String remark,
									 String formName, Integer groupId, String templateId)
	{
		BpmnModel bpmnModel =new BpmnModel();
		List<SequenceFlow> sequenceFlows = Lists.newArrayList();
		Map<String,ChildNode> childNodeMap=new HashMap<>();
		bpmnModel.setTargetNamespace(groupId+"");
		ExtensionAttribute extensionAttribute=new ExtensionAttribute();
		extensionAttribute.setName("DingDing");
		extensionAttribute.setNamespace("http://flowable.org/bpmn");
		extensionAttribute.setValue(jsonObject.toJSONString());
		Process process =new Process();
		process.setId(FlowConstants.PROCESS_PREFIX+templateId);
		process.setName(formName);
		process.setDocumentation(remark);
		process.addAttribute(extensionAttribute);
		bpmnModel.addProcess(process);

		StartEvent startEvent = createStartEvent();
		process.addFlowElement(startEvent);
		String lastNode = null;
		try {
			lastNode = create(startEvent.getId(), childNode,process,bpmnModel,sequenceFlows,childNodeMap);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			throw new CheckedException("操作失败");
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			throw new CheckedException("操作失败");
		}
		EndEvent endEvent = createEndEvent();
		process.addFlowElement(endEvent);
		process.addFlowElement(connect(lastNode, endEvent.getId(),sequenceFlows,childNodeMap,process));
		List<FlowableListener> executionListeners =new ArrayList<>();
		FlowableListener flowableListener=new FlowableListener();
		flowableListener.setEvent(ExecutionListener.EVENTNAME_END);
		flowableListener.setImplementationType(ImplementationType.IMPLEMENTATION_TYPE_DELEGATEEXPRESSION);
		flowableListener.setImplementation("${processListener}");
		executionListeners.add(flowableListener);
		process.setExecutionListeners(executionListeners);
//		new BpmnAutoLayout(bpmnModel).execute();
		return bpmnModel;
	}
}

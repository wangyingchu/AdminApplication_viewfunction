package com.viewfunction.activitySpaceEventListeners;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.viewfunction.activityEngine.activityBureau.ActivitySpace;
import com.viewfunction.activityEngine.activityView.common.ActivityStep;
import com.viewfunction.activityEngine.activityView.common.ParticipantTask;
import com.viewfunction.activityEngine.exception.ActivityEngineActivityException;
import com.viewfunction.activityEngine.exception.ActivityEngineDataException;
import com.viewfunction.activityEngine.exception.ActivityEngineProcessException;
import com.viewfunction.activityEngine.exception.ActivityEngineRuntimeException;
import com.viewfunction.activityEngine.extension.ActivitySpaceEventContext;
import com.viewfunction.activityEngine.extension.ActivitySpaceEventListener;
import com.viewfunction.messageEngine.exchange.MessageServiceConstant;
import com.viewfunction.messageEngine.exchange.restful.ActivityTaskNotificationVO;
import com.viewfunction.messageEngine.exchange.restful.CommonNotificationVO;
import com.viewfunction.messageEngine.exchange.restful.MessageReceiverVO;
import com.viewfunction.messageEngine.exchange.restful.SendActivityTaskNotificationVO;
import com.viewfunction.messageEngine.exchange.restful.SendMessageResultVO;
import com.viewfunction.messageEngine.exchange.restfulClient.NotificationOperationServiceRESTClient;
import com.viewfunction.participantManagement.operation.restful.ParticipantDetailInfoVO;
import com.viewfunction.participantManagement.operation.restful.ParticipantDetailInfoVOsList;
import com.viewfunction.participantManagement.operation.restful.ParticipantDetailInfosQueryVO;
import com.viewfunction.participantManagement.operation.restfulClient.ParticipantOperationServiceRESTClient;


public class AssigneeNotificationActivitySpaceEventListener extends ActivitySpaceEventListener{

	@Override
	public void executeActivitySpaceEventHandleLogic(ActivitySpaceEventContext activitySpaceEventContext) {


	}
	
	public boolean isNewAssignedStep(ActivitySpaceEventContext activitySpaceEventContext,ActivityStep targetActivityStep){
		if(targetActivityStep.getFinishTime()!=null){
			return false;
		}
		String stepAssignee=targetActivityStep.getStepAssignee();
		String activityId=targetActivityStep.getActivityId();
		String activityStepId=targetActivityStep.getActivityStepId();
		String activityStepDefinitionKey=targetActivityStep.getActivityStepDefinitionKey();
		if(stepAssignee==null){
			return false;
		}else{
			ActivitySpace attachedActivitySpace=activitySpaceEventContext.getActivitySpace();
			boolean isNewAssignedStep=true;
			try {
				List<ParticipantTask>  participantTaskList=attachedActivitySpace.getParticipant(stepAssignee).fetchParticipantTasks();
				for(ParticipantTask participantTask:participantTaskList){
					String taskStepId=participantTask.getActivityStep().getActivityStepId();
					String taskStepDefinitionKey=participantTask.getActivityStep().getActivityStepDefinitionKey();
					String taskActivityId=participantTask.getActivityStep().getActivityId();
					if(taskStepId.equals(activityStepId)&&taskStepDefinitionKey.equals(activityStepDefinitionKey)&&taskActivityId.equals(activityId)){
						isNewAssignedStep=false;
						break;
					}
				}	
				return isNewAssignedStep;
			} catch (ActivityEngineRuntimeException e2) {
				e2.printStackTrace();
			} catch (ActivityEngineProcessException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
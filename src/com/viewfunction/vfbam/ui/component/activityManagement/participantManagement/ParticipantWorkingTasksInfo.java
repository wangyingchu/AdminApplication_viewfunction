package com.viewfunction.vfbam.ui.component.activityManagement.participantManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityStepsTable;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class ParticipantWorkingTasksInfo  extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private String participantName;

    private VerticalLayout containerLayout;
    public ParticipantWorkingTasksInfo(UserClientInfo currentUserClientInfo,String participantName){
        this.currentUserClientInfo=currentUserClientInfo;
        this.participantName=participantName;
        setSpacing(true);
        setMargin(true);
        // Participant's Working Tasks Info Section
        SecondarySectionTitle belongsToRolesSectionTitle=new SecondarySectionTitle("Participant's Working Tasks");
        addComponent(belongsToRolesSectionTitle);

        containerLayout=new VerticalLayout();
        addComponent(containerLayout);
    }

    @Override
    public void attach() {
        super.attach();
        containerLayout.removeAllComponents();
        String activitySpaceName="";
        if(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo()!=null){
            activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        }

        SectionActionsBar workingTasksActionsBar=new SectionActionsBar(
                new Label("Participant : <b>"+this.participantName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML));
        containerLayout.addComponent(workingTasksActionsBar);

        ActivityStepsTable activityStepsTable =new ActivityStepsTable(this.currentUserClientInfo,"300px");
        activityStepsTable.setActivityStepQueryId(this.participantName);
        activityStepsTable.setActivityStepType(ActivityStepsTable.ACTIVITYSTEPS_TYPE_PARTICIPANT);
        containerLayout.addComponent(activityStepsTable);
    }
}

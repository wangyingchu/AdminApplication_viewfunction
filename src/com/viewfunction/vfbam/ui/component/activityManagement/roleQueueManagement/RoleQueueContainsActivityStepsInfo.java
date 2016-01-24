package com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement;


import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityStepsTable;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class RoleQueueContainsActivityStepsInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private String roleQueueName;
    public RoleQueueContainsActivityStepsInfo(UserClientInfo currentUserClientInfo,String roleQueueName){
        this.currentUserClientInfo=currentUserClientInfo;
        this.roleQueueName=roleQueueName;
        setSpacing(true);
        setMargin(true);

        SecondarySectionTitle roleQueueContainsActivityStepsSectionTitle=new SecondarySectionTitle("Role Queue Contains Activity Steps");
        addComponent(roleQueueContainsActivityStepsSectionTitle);
    }

    @Override
    public void attach() {
        super.attach();
        String activitySpaceName="";
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        if(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo()!=null){
            activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        }

        SectionActionsBar workingTasksActionsBar=new SectionActionsBar(
                new Label("Role Queue : <b>"+this.roleQueueName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML));
        addComponent(workingTasksActionsBar);

        ActivityStepsTable activityStepsTable =new ActivityStepsTable(this.currentUserClientInfo,"300px");
        addComponent(activityStepsTable);
    }
}

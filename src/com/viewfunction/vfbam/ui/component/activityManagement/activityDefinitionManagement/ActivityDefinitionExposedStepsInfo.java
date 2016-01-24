package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class ActivityDefinitionExposedStepsInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar activityTypeStepsSectionActionsBar;
    private String activityType;

    public ActivityDefinitionExposedStepsInfo(UserClientInfo currentUserClientInfo,String activityType){
        this.currentUserClientInfo=currentUserClientInfo;
        this.activityType=activityType;
        setSpacing(true);
        setMargin(true);
        SecondarySectionTitle activityStepsSectionTitle=new SecondarySectionTitle("Activity Step Properties");
        addComponent(activityStepsSectionTitle);
        activityTypeStepsSectionActionsBar=new SectionActionsBar(
                new Label("Activity Type : <b>"+this.activityType+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(activityTypeStepsSectionActionsBar);
        ActivityStepItemsActionList activityStepItemsActionList=new ActivityStepItemsActionList(this.currentUserClientInfo,"340px",false);
        addComponent(activityStepItemsActionList);

    }

    @Override
    public void attach() {
        super.attach();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        if(currentActivitySpaceComponentInfo!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            Label sectionActionBarLabel=new Label("Activity Type : <b>"+this.activityType+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            activityTypeStepsSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
    }
}

package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityDataFieldsActionTable;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class ActivityDefinitionContainedDataFieldsInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar activityTypeDataFieldsSectionActionsBar;
    private String activityType;

    public ActivityDefinitionContainedDataFieldsInfo(UserClientInfo currentUserClientInfo,String activityType){
        this.currentUserClientInfo=currentUserClientInfo;
        this.activityType=activityType;
        setSpacing(true);
        setMargin(true);
        SecondarySectionTitle belongsToRolesSectionTitle=new SecondarySectionTitle("Data Field Definitions");
        addComponent(belongsToRolesSectionTitle);

        activityTypeDataFieldsSectionActionsBar=new SectionActionsBar(
                new Label("Activity Type : <b>"+this.activityType+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(activityTypeDataFieldsSectionActionsBar);
        ActivityDataFieldsActionTable activityDataFieldsActionTable =new ActivityDataFieldsActionTable(this.currentUserClientInfo,"300px",false,false);
        activityDataFieldsActionTable.setDataFieldQueryType(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ACTIVITYTYPE);
        activityDataFieldsActionTable.setDataFieldsQueryId(this.activityType);
        addComponent(activityDataFieldsActionTable);
    }

    @Override
    public void attach() {
        super.attach();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        if(currentActivitySpaceComponentInfo!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            Label sectionActionBarLabel=new Label("Activity Type : <b>"+this.activityType+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            activityTypeDataFieldsSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
    }
}

package com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityDataFieldsActionTable;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class RoleQueueExposedDataFieldsInfo  extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar exposedDataFieldsSectionActionsBar;
    private String roleQueueName;
    public RoleQueueExposedDataFieldsInfo(UserClientInfo currentUserClientInfo,String roleQueueName){
        this.currentUserClientInfo=currentUserClientInfo;
        this.roleQueueName=roleQueueName;
        setSpacing(true);
        setMargin(true);
        SecondarySectionTitle belongsToRolesSectionTitle=new SecondarySectionTitle("Role Queue Displayed Data Fields Filter");
        addComponent(belongsToRolesSectionTitle);

        exposedDataFieldsSectionActionsBar=new SectionActionsBar(
                new Label("Role Queue : <b>"+this.roleQueueName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(exposedDataFieldsSectionActionsBar);

        ActivityDataFieldsActionTable activityDataFieldsActionTable =new ActivityDataFieldsActionTable(this.currentUserClientInfo,"300px",false,true);
        activityDataFieldsActionTable.setDataFieldQueryType(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ROLEQUEUE);
        activityDataFieldsActionTable.setDataFieldsQueryId(this.roleQueueName);
        addComponent(activityDataFieldsActionTable);
    }

    @Override
    public void attach() {
        super.attach();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        if(currentActivitySpaceComponentInfo!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            Label sectionActionBarLabel=new Label("Role Queue : <b>"+this.roleQueueName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            exposedDataFieldsSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
    }
}

package com.viewfunction.vfbam.ui.component.activityManagement.roleManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement.RoleQueuesActionTable;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class RoleRelatedRoleQueuesInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar relatedRoleQueueSectionActionsBar;
    private String roleName;

    public RoleRelatedRoleQueuesInfo(UserClientInfo currentUserClientInfo,String roleName){
        this.currentUserClientInfo=currentUserClientInfo;
        this.roleName=roleName;
        setSpacing(true);
        setMargin(true);
        SecondarySectionTitle participantsBelongsToRoleSectionTitle=new SecondarySectionTitle("Role Queues Role Related To");
        addComponent(participantsBelongsToRoleSectionTitle);

        relatedRoleQueueSectionActionsBar=new SectionActionsBar(
                new Label("Role : <b>"+this.roleName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(relatedRoleQueueSectionActionsBar);
        RoleQueuesActionTable roleQueuesActionTable=new RoleQueuesActionTable(this.currentUserClientInfo, "300px",false,false);
        roleQueuesActionTable.setRoleQueuesType(RoleQueuesActionTable.ROLEQUEUES_TYPE_ROLE);
        roleQueuesActionTable.setRoleQueuesQueryId(this.roleName);
        addComponent(roleQueuesActionTable);
    }

    @Override
    public void attach() {
        super.attach();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        if(currentActivitySpaceComponentInfo!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            Label sectionActionBarLabel=new Label("Role : <b>"+this.roleName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            relatedRoleQueueSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
    }
}

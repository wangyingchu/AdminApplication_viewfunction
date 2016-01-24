package com.viewfunction.vfbam.ui.component.activityManagement.roleManagement;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.viewfunction.vfbam.ui.component.activityManagement.participantManagement.ParticipantEditor;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class AddNewRolePanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private RoleEditor roleEditor;
    private SectionActionsBar addNewRoleActionsBar;
    private RolesActionTable relatedRolesTable;
    public AddNewRolePanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        setSpacing(true);
        setMargin(true);
        // Add New Role Section
        MainSectionTitle addNewParticipantSectionTitle=new MainSectionTitle("Add New Role");
        addComponent(addNewParticipantSectionTitle);
        addNewRoleActionsBar=new SectionActionsBar(
                new Label("Activity Space : <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(addNewRoleActionsBar);
        roleEditor=new RoleEditor(this.currentUserClientInfo,ParticipantEditor.EDITMODE_NEW);
        roleEditor.setContainerAddNewRolePanel(this);
        addComponent(roleEditor);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void addNewRoleFinishCallBack(String roleName,String roleDisplayName,String roleDescription){
        if(this.getRelatedRolesTable() !=null){
            this.getRelatedRolesTable().addRole(roleName, roleDisplayName, roleDescription);
        }
        //close dialog window
        if(this.containerDialog!=null){
            this.containerDialog.close();
        }
    }

    @Override
    public void attach() {
        super.attach();
        if(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo()!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            Label activitySpaceNameLabel=new Label("Activity Space : <b>"+activitySpaceName+"</b>" , ContentMode.HTML);
            addNewRoleActionsBar.resetSectionActionsBarContent(activitySpaceNameLabel);
        }
    }

    public void setRelatedRolesTable(RolesActionTable relatedRolesTable) {
        this.relatedRolesTable = relatedRolesTable;
    }

    public RolesActionTable getRelatedRolesTable() {
        return relatedRolesTable;
    }
}

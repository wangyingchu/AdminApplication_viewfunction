package com.viewfunction.vfbam.ui.component.activityManagement.participantManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityStepsTable;
import com.viewfunction.vfbam.ui.component.activityManagement.roleManagement.RolesActionTable;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class ParticipantDetailInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    public ParticipantDetailInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        setSpacing(false);
        setMargin(true);

        String activitySpaceName=currentActivitySpaceComponentInfo.getActivitySpaceName();
        String participantName=currentActivitySpaceComponentInfo.getComponentId();
        Participant currentParticipant=ActivitySpaceOperationUtil.getParticipantByName(activitySpaceName, participantName);

        TabSheet tabs=new TabSheet();
        addComponent(tabs);
        VerticalLayout participantInfoLayout=new VerticalLayout();
        TabSheet.Tab participantInfoTab =tabs.addTab(participantInfoLayout, "Participant Info");
        participantInfoTab.setIcon(FontAwesome.INFO);

        MainSectionTitle mainSectionTitle=new MainSectionTitle("Participant Info");
        participantInfoLayout.addComponent(mainSectionTitle);
        //Participant info editor
        ParticipantEditor participantEditor =new ParticipantEditor(this.currentUserClientInfo,ParticipantEditor.EDITMODE_UPDATE);
        participantEditor.setParticipant(currentParticipant);
        participantInfoLayout.addComponent(participantEditor);

        int browserWindowHeight=UI.getCurrent().getPage().getBrowserWindowHeight();
        String tableHeightString=""+(browserWindowHeight-330)+"px";

        VerticalLayout belongsToRolesLayout=new VerticalLayout();
        TabSheet.Tab belongsToRolesTab = tabs.addTab(belongsToRolesLayout, "Belongs To Roles");
        belongsToRolesTab.setIcon(FontAwesome.USERS);
        // Participant's Roles Info Section
        MainSectionTitle belongsToRolesSectionTitle=new MainSectionTitle("Belongs To Roles");
        belongsToRolesLayout.addComponent(belongsToRolesSectionTitle);
        SectionActionsBar belongsToRolesSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.USERS.getHtml() + " "+"Roles Participant Belongs To", ContentMode.HTML));
        belongsToRolesLayout.addComponent(belongsToRolesSectionActionsBar);
        SectionActionButton modifyBelongedRolesActionButton = new SectionActionButton();
        modifyBelongedRolesActionButton.setCaption("Modify Belonged Roles");
        modifyBelongedRolesActionButton.setIcon(FontAwesome.COG);
        belongsToRolesSectionActionsBar.addActionComponent(modifyBelongedRolesActionButton);

        RolesActionTable rolesActionTable=new RolesActionTable(this.currentUserClientInfo,tableHeightString,true);
        rolesActionTable.setRolesQueryId(currentActivitySpaceComponentInfo.getComponentId());
        rolesActionTable.setRolesType(RolesActionTable.ROLES_TYPE_PARTICIPANT);
        belongsToRolesLayout.addComponent(rolesActionTable);

        final ParticipantBelongedRolesSelector participantBelongedRolesSelector=new ParticipantBelongedRolesSelector(this.currentUserClientInfo);
        participantBelongedRolesSelector.setParticipant(currentParticipant);
        participantBelongedRolesSelector.setRelatedRolesActionTable(rolesActionTable);
        modifyBelongedRolesActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(700.0f, Unit.PIXELS);
                window.setHeight(490.0f, Unit.PIXELS);
                window.center();
                window.setModal(true);
                window.setContent(participantBelongedRolesSelector);
                participantBelongedRolesSelector.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });

        VerticalLayout participantTasksLayout=new VerticalLayout();
        TabSheet.Tab participantTasksTab = tabs.addTab(participantTasksLayout, "Participant Tasks");
        participantTasksTab.setIcon(FontAwesome.TASKS);
        // Participant's Working Tasks Section
        MainSectionTitle workingTasksSectionTitle=new MainSectionTitle("Participant Tasks");
        participantTasksLayout.addComponent(workingTasksSectionTitle);
        SectionActionsBar  workingTasksSectionActionsBar=new SectionActionsBar(new Label(FontAwesome.TASKS.getHtml() + " "+"Participant's Working Tasks", ContentMode.HTML));
        participantTasksLayout.addComponent(workingTasksSectionActionsBar);
        SectionActionButton fetchParticipantTasksActionButton = new SectionActionButton();
        fetchParticipantTasksActionButton.setCaption("Fetch Participant's Tasks Info");
        fetchParticipantTasksActionButton.setIcon(FontAwesome.DOWNLOAD);
        workingTasksSectionActionsBar.addActionComponent(fetchParticipantTasksActionButton);

        final ActivityStepsTable activityStepsTable =new ActivityStepsTable(this.currentUserClientInfo,tableHeightString);
        activityStepsTable.setActivityStepQueryId(currentActivitySpaceComponentInfo.getComponentId());
        activityStepsTable.setActivityStepType(ActivityStepsTable.ACTIVITYSTEPS_TYPE_PARTICIPANT);
        participantTasksLayout.addComponent(activityStepsTable);

        fetchParticipantTasksActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                activityStepsTable.loadActivityStepsData();
            }
        });
    }
}
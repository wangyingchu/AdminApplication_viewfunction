package com.viewfunction.vfbam.ui.component.activityManagement.roleManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.participantManagement.ParticipantsActionTable;
import com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement.RoleQueuesActionTable;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class RoleDetailInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    public RoleDetailInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        setSpacing(false);
        setMargin(true);

        String activitySpaceName=currentActivitySpaceComponentInfo.getActivitySpaceName();
        String roleName=currentActivitySpaceComponentInfo.getComponentId();
        Role currentRole= ActivitySpaceOperationUtil.getRoleByName(activitySpaceName,roleName);

        TabSheet tabs=new TabSheet();
        addComponent(tabs);
        VerticalLayout roleInfoLayout=new VerticalLayout();
        TabSheet.Tab roleInfoTab =tabs.addTab(roleInfoLayout, "Role Info");
        roleInfoTab.setIcon(FontAwesome.INFO);

        MainSectionTitle mainSectionTitle=new MainSectionTitle("Role Info");
        roleInfoLayout.addComponent(mainSectionTitle);
        //Role info editor
        RoleEditor roleEditor =new RoleEditor(this.currentUserClientInfo,RoleEditor.EDITMODE_UPDATE);
        roleEditor.setRole(currentRole);
        roleInfoLayout.addComponent(roleEditor);

        int browserWindowHeight=UI.getCurrent().getPage().getBrowserWindowHeight();
        String tableHeightString=""+(browserWindowHeight-330)+"px";

        VerticalLayout relatedRoleQueuesLayout=new VerticalLayout();
        TabSheet.Tab relatedRoleQueuesLayoutTab =tabs.addTab(relatedRoleQueuesLayout, "Related Role Queues");
        relatedRoleQueuesLayoutTab.setIcon(FontAwesome.ALIGN_JUSTIFY);
        // Related Role Queues Info Section
        MainSectionTitle relatedRoleQueuesSectionTitle=new MainSectionTitle("Related Role Queues");
        relatedRoleQueuesLayout.addComponent(relatedRoleQueuesSectionTitle);
        SectionActionsBar relatedRoleQueuesSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.ALIGN_JUSTIFY.getHtml() + " "+"Role Queues Role Related To", ContentMode.HTML));
        relatedRoleQueuesLayout.addComponent(relatedRoleQueuesSectionActionsBar);
        SectionActionButton modifyRelatedRoleQueuesActionButton = new SectionActionButton();
        modifyRelatedRoleQueuesActionButton.setCaption("Modify Related Role Queues");
        modifyRelatedRoleQueuesActionButton.setIcon(FontAwesome.COG);
        final RoleRelatedRoleQueuesSelector roleRelatedRoleQueuesSelector=new RoleRelatedRoleQueuesSelector(this.currentUserClientInfo);
        roleRelatedRoleQueuesSelector.setRole(currentRole);
        modifyRelatedRoleQueuesActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(700.0f, Unit.PIXELS);
                window.setHeight(490.0f, Unit.PIXELS);
                window.center();
                window.setModal(true);
                window.setContent(roleRelatedRoleQueuesSelector);
                roleRelatedRoleQueuesSelector.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });
        relatedRoleQueuesSectionActionsBar.addActionComponent(modifyRelatedRoleQueuesActionButton);

        RoleQueuesActionTable roleQueuesActionTable=new RoleQueuesActionTable(this.currentUserClientInfo, tableHeightString,true,false);
        roleQueuesActionTable.setRoleQueuesType(RoleQueuesActionTable.ROLEQUEUES_TYPE_ROLE);
        roleQueuesActionTable.setRoleQueuesQueryId(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId());
        relatedRoleQueuesLayout.addComponent(roleQueuesActionTable);
        roleRelatedRoleQueuesSelector.setRelatedRoleQueuesActionTable(roleQueuesActionTable);

        VerticalLayout containsParticipantsLayout=new VerticalLayout();
        TabSheet.Tab containsParticipantsLayoutTab =tabs.addTab(containsParticipantsLayout, "Contains Participants");
        containsParticipantsLayoutTab.setIcon(FontAwesome.USER);
        // Contains Participants Info Section
        MainSectionTitle containsParticipantsSectionTitle=new MainSectionTitle("Contains Participants");
        containsParticipantsLayout.addComponent(containsParticipantsSectionTitle);
        SectionActionsBar  containsParticipantsSectionActionsBar=new SectionActionsBar(new Label(FontAwesome.USER.getHtml() + " "+"Participants Belonged to Role", ContentMode.HTML));
        containsParticipantsLayout.addComponent(containsParticipantsSectionActionsBar);
        SectionActionButton modifyContainsParticipantsActionButton = new SectionActionButton();
        modifyContainsParticipantsActionButton.setCaption("Modify Contains Participants");
        modifyContainsParticipantsActionButton.setIcon(FontAwesome.COG);
        final RoleContainsParticipantsSelector roleContainsParticipantsSelector=new RoleContainsParticipantsSelector(this.currentUserClientInfo);
        roleContainsParticipantsSelector.setRole(currentRole);
        modifyContainsParticipantsActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(700.0f, Unit.PIXELS);
                window.setHeight(490.0f, Unit.PIXELS);
                window.center();
                window.setModal(true);
                window.setContent(roleContainsParticipantsSelector);
                roleContainsParticipantsSelector.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });
        containsParticipantsSectionActionsBar.addActionComponent(modifyContainsParticipantsActionButton);
        ParticipantsActionTable participantsActionTable=new ParticipantsActionTable(this.currentUserClientInfo,tableHeightString,true,false);
        participantsActionTable.setParticipantsQueryId(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId());
        participantsActionTable.setParticipantsType(ParticipantsActionTable.PARTICIPANTS_TYPE_ROLE);
        containsParticipantsLayout.addComponent(participantsActionTable);
        roleContainsParticipantsSelector.setRelatedParticipantsActionTable(participantsActionTable);
    }
}

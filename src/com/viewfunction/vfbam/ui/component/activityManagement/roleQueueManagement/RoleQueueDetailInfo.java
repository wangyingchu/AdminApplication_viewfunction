package com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityDataFieldsEditor;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityManagementConst;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityStepsTable;
import com.viewfunction.vfbam.ui.component.activityManagement.roleManagement.RoleContainsParticipantsSelector;
import com.viewfunction.vfbam.ui.component.activityManagement.roleManagement.RolesActionTable;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class RoleQueueDetailInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    public RoleQueueDetailInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        setSpacing(false);
        setMargin(true);

        String activitySpaceName=currentActivitySpaceComponentInfo.getActivitySpaceName();
        String roleQueueName=currentActivitySpaceComponentInfo.getComponentId();
        RoleQueue currentRoleQueue= ActivitySpaceOperationUtil.getRoleQueueByName(activitySpaceName,roleQueueName);

        TabSheet tabs=new TabSheet();
        addComponent(tabs);
        VerticalLayout roleQueueInfoLayout=new VerticalLayout();
        TabSheet.Tab roleQueueInfoTab =tabs.addTab(roleQueueInfoLayout, "Role Queue Info");
        roleQueueInfoTab.setIcon(FontAwesome.INFO);
        MainSectionTitle mainSectionTitle=new MainSectionTitle("Role Queue Info");
        roleQueueInfoLayout.addComponent(mainSectionTitle);
        RoleQueueEditor roleQueueEditor=new RoleQueueEditor(this.currentUserClientInfo,RoleQueueEditor.EDITMODE_UPDATE);
        roleQueueEditor.setRoleQueue(currentRoleQueue);
        roleQueueInfoLayout.addComponent(roleQueueEditor);

        int browserWindowHeight=UI.getCurrent().getPage().getBrowserWindowHeight();
        String tableHeightString=""+(browserWindowHeight-330)+"px";

        VerticalLayout relatedRolesLayout=new VerticalLayout();
        TabSheet.Tab relatedRolesTab =tabs.addTab(relatedRolesLayout, "Related Roles");
        relatedRolesTab.setIcon(FontAwesome.USERS);
        MainSectionTitle relatedRolesSectionTitle=new MainSectionTitle("Related Roles");
        relatedRolesLayout.addComponent(relatedRolesSectionTitle);
        SectionActionsBar relatedRolesSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.USERS.getHtml() + " "+"Roles Related To Role Queue", ContentMode.HTML));
        relatedRolesLayout.addComponent(relatedRolesSectionActionsBar);
        SectionActionButton modifyRelatedRolesActionButton = new SectionActionButton();
        modifyRelatedRolesActionButton.setCaption("Modify Related Roles");
        modifyRelatedRolesActionButton.setIcon(FontAwesome.COG);
        final RoleQueueRelatedRolesSelector roleQueueRelatedRolesSelector=new RoleQueueRelatedRolesSelector(this.currentUserClientInfo);
        roleQueueRelatedRolesSelector.setRoleQueue(currentRoleQueue);
        modifyRelatedRolesActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(700.0f, Unit.PIXELS);
                window.setHeight(490.0f, Unit.PIXELS);
                window.center();
                window.setContent(roleQueueRelatedRolesSelector);
                roleQueueRelatedRolesSelector.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });
        relatedRolesSectionActionsBar.addActionComponent(modifyRelatedRolesActionButton);

        RolesActionTable rolesActionTable=new RolesActionTable(this.currentUserClientInfo,tableHeightString,true);
        rolesActionTable.setRolesQueryId(currentActivitySpaceComponentInfo.getComponentId());
        rolesActionTable.setRolesType(RolesActionTable.ROLES_TYPE_ROLEQUEUE);
        relatedRolesLayout.addComponent(rolesActionTable);
        roleQueueRelatedRolesSelector.setRelatedRolesActionTable(rolesActionTable);

        VerticalLayout exposedDataFieldsLayout=new VerticalLayout();
        TabSheet.Tab exposedDataFieldsTab =tabs.addTab(exposedDataFieldsLayout, "Displayed Data Fields Filter");
        exposedDataFieldsTab.setIcon(FontAwesome.TH_LIST);
        MainSectionTitle exposedDataFieldsSectionTitle=new MainSectionTitle("Displayed Data Fields Filter");
        exposedDataFieldsLayout.addComponent(exposedDataFieldsSectionTitle);
        ActivityDataFieldsEditor activityDataFieldsEditor=new ActivityDataFieldsEditor(this.currentUserClientInfo,
                ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE,currentActivitySpaceComponentInfo.getComponentId(),tableHeightString);
        exposedDataFieldsLayout.addComponent(activityDataFieldsEditor);

        VerticalLayout activityStepsLayout=new VerticalLayout();
        TabSheet.Tab activityStepsTab =tabs.addTab(activityStepsLayout, "Contains Activity Steps");
        activityStepsTab.setIcon(FontAwesome.SLIDERS);
        MainSectionTitle activityStepsSectionTitle=new MainSectionTitle("Contains Activity Steps");
        activityStepsLayout.addComponent(activityStepsSectionTitle);

        SectionActionsBar workingActivityStepsSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.SLIDERS.getHtml() + " "+"Working Activity Steps of Role", ContentMode.HTML));
        activityStepsLayout.addComponent(workingActivityStepsSectionActionsBar);
        SectionActionButton fetchActivityStepsActionButton = new SectionActionButton();
        fetchActivityStepsActionButton.setCaption("Fetch Working Activity Steps");
        fetchActivityStepsActionButton.setIcon(FontAwesome.DOWNLOAD);
        workingActivityStepsSectionActionsBar.addActionComponent(fetchActivityStepsActionButton);
        final ActivityStepsTable activityStepsTable =new ActivityStepsTable(this.currentUserClientInfo,tableHeightString);
        activityStepsTable.setActivityStepType(ActivityStepsTable.ACTIVITYSTEPS_TYPE_ROLEQUEUE);
        activityStepsTable.setActivityStepQueryId(roleQueueName);
        activityStepsLayout.addComponent(activityStepsTable);

        fetchActivityStepsActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                activityStepsTable.loadActivityStepsData();
            }
        });
    }
}

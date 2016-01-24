package com.viewfunction.vfbam.ui.component.activityManagement.rosterManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.activityView.Roster;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityDataFieldsEditor;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityInstancesActionTable;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityManagementConst;
import com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement.ActivityDefinitionsActionTable;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class RosterDetailInfo  extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    public RosterDetailInfo(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        setSpacing(false);
        setMargin(true);

        String activitySpaceName=currentActivitySpaceComponentInfo.getActivitySpaceName();
        String rosterName=currentActivitySpaceComponentInfo.getComponentId();
        Roster currentRoster= ActivitySpaceOperationUtil.getRosterByName(activitySpaceName,rosterName);

        TabSheet tabs=new TabSheet();
        addComponent(tabs);

        VerticalLayout rosterInfoLayout=new VerticalLayout();
        TabSheet.Tab roleInfoTab =tabs.addTab(rosterInfoLayout, "Roster Info");
        roleInfoTab.setIcon(FontAwesome.INFO);
        MainSectionTitle mainSectionTitle=new MainSectionTitle("Roster Info");
        rosterInfoLayout.addComponent(mainSectionTitle);
        RosterEditor rosterEditor=new RosterEditor(this.currentUserClientInfo,RosterEditor.EDITMODE_UPDATE);
        rosterEditor.setRoster(currentRoster);
        rosterInfoLayout.addComponent(rosterEditor);

        VerticalLayout containedActivityTypeLayout=new VerticalLayout();
        TabSheet.Tab containedActivityTypeTab =tabs.addTab(containedActivityTypeLayout, "Contained Activity Definitions");
        containedActivityTypeTab.setIcon(FontAwesome.SHARE_ALT_SQUARE);
        MainSectionTitle containedActivityDefinitionSectionTitle=new MainSectionTitle("Contained Activity Definitions");
        containedActivityTypeLayout.addComponent(containedActivityDefinitionSectionTitle);

        ActivityDefinitionsActionTable activityDefinitionsActionTable=new ActivityDefinitionsActionTable(this.currentUserClientInfo,null,true);
        activityDefinitionsActionTable.setActivityDefinitionsQueryId(currentActivitySpaceComponentInfo.getComponentId());
        activityDefinitionsActionTable.setActivityDefinitionsType(ActivityDefinitionsActionTable.ACTIVITYDEFINITIONS_TYPE_ROSTER);
        SectionActionsBar containsActivityDefinitionsSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.SHARE_ALT_SQUARE.getHtml() + " "+" Activity Definitions List", ContentMode.HTML));
        containedActivityTypeLayout.addComponent(containsActivityDefinitionsSectionActionsBar);
        SectionActionButton modifyContainedActivityDefinitionActionButton = new SectionActionButton();
        modifyContainedActivityDefinitionActionButton.setCaption("Modify Related Activity Definitions");
        modifyContainedActivityDefinitionActionButton.setIcon(FontAwesome.COG);
        final RosterRelatedActivityDefinitionsSelector rosterRelatedActivityDefinitionsSelector=new RosterRelatedActivityDefinitionsSelector(this.currentUserClientInfo);
        rosterRelatedActivityDefinitionsSelector.setRoster(currentRoster);
        rosterRelatedActivityDefinitionsSelector.setRelatedActivityDefinitionsActionTable(activityDefinitionsActionTable);
        modifyContainedActivityDefinitionActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(700.0f, Unit.PIXELS);
                window.setHeight(490.0f, Unit.PIXELS);
                window.center();
                window.setContent(rosterRelatedActivityDefinitionsSelector);
                rosterRelatedActivityDefinitionsSelector.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });
        containsActivityDefinitionsSectionActionsBar.addActionComponent(modifyContainedActivityDefinitionActionButton);
        containedActivityTypeLayout.addComponent(activityDefinitionsActionTable);

        VerticalLayout exposedDataFieldsLayout=new VerticalLayout();
        TabSheet.Tab exposedDataFieldsTab =tabs.addTab(exposedDataFieldsLayout, "Displayed Data Fields Filter");
        exposedDataFieldsTab.setIcon(FontAwesome.TH_LIST);
        MainSectionTitle exposedDataFieldsSectionTitle=new MainSectionTitle("Displayed Data Fields Filter");
        exposedDataFieldsLayout.addComponent(exposedDataFieldsSectionTitle);
        ActivityDataFieldsEditor activityDataFieldsEditor=new ActivityDataFieldsEditor(this.currentUserClientInfo,
                ActivityManagementConst.COMPONENT_TYPE_ROSTER,currentActivitySpaceComponentInfo.getComponentId());
        exposedDataFieldsLayout.addComponent(activityDataFieldsEditor);

        VerticalLayout businessActivitiesLayout=new VerticalLayout();
        TabSheet.Tab businessActivitiesTab =tabs.addTab(businessActivitiesLayout, "Running Business Activities");
        businessActivitiesTab.setIcon(FontAwesome.INDENT);
        MainSectionTitle runningBusinessActivitiesSectionTitle=new MainSectionTitle("Running Business Activities");
        businessActivitiesLayout.addComponent(runningBusinessActivitiesSectionTitle);
        SectionActionsBar activitiesContainedInRosterSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.INDENT.getHtml() + " "+"Business Activities contained In Roster", ContentMode.HTML));
        businessActivitiesLayout.addComponent(activitiesContainedInRosterSectionActionsBar);
        SectionActionButton fetchActivitiesActionButton = new SectionActionButton();
        fetchActivitiesActionButton.setCaption("Fetch Business Activities");
        fetchActivitiesActionButton.setIcon(FontAwesome.DOWNLOAD);
        activitiesContainedInRosterSectionActionsBar.addActionComponent(fetchActivitiesActionButton);

        final ActivityInstancesActionTable activityInstancesActionTable =new ActivityInstancesActionTable(this.currentUserClientInfo,null,true);
        activityInstancesActionTable.setActivityInstancesQueryId(currentActivitySpaceComponentInfo.getComponentId());
        activityInstancesActionTable.setActivityInstancesQueryType(ActivityInstancesActionTable.ACTIVITYINSTANCES_TYPE_ROSTER);
        activityInstancesActionTable.setRelatedRoster(currentRoster);
        businessActivitiesLayout.addComponent(activityInstancesActionTable);

        fetchActivitiesActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                activityInstancesActionTable.loadActivityInstancesData();
            }
        });
    }
}

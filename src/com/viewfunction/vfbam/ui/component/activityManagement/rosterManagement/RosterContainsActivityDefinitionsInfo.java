package com.viewfunction.vfbam.ui.component.activityManagement.rosterManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement.ActivityDefinitionsActionTable;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class RosterContainsActivityDefinitionsInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar containedActivityDefinitionsSectionActionsBar;
    private String rosterName;
    public RosterContainsActivityDefinitionsInfo(UserClientInfo currentUserClientInfo,String rosterName){
        this.currentUserClientInfo=currentUserClientInfo;
        this.rosterName=rosterName;
        setSpacing(true);
        setMargin(true);
        // Participant's Roles Info Section
        SecondarySectionTitle belongsToRolesSectionTitle=new SecondarySectionTitle("Roster Contains Activity Definitions");
        addComponent(belongsToRolesSectionTitle);

        containedActivityDefinitionsSectionActionsBar=new SectionActionsBar(
                new Label("Roster : <b>"+this.rosterName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(containedActivityDefinitionsSectionActionsBar);

        ActivityDefinitionsActionTable activityDefinitionsActionTable=new ActivityDefinitionsActionTable(this.currentUserClientInfo,"300px",false);
        activityDefinitionsActionTable.setActivityDefinitionsQueryId(this.rosterName);
        activityDefinitionsActionTable.setActivityDefinitionsType(ActivityDefinitionsActionTable.ACTIVITYDEFINITIONS_TYPE_ROSTER);
        addComponent(activityDefinitionsActionTable);
    }

    @Override
    public void attach() {
        super.attach();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        if(currentActivitySpaceComponentInfo!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            Label sectionActionBarLabel=new Label("Roster : <b>"+this.rosterName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            containedActivityDefinitionsSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
    }
}

package com.viewfunction.vfbam.ui.component.activityManagement.rosterManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityDataFieldsActionTable;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class RosterExposedDataFieldsInfo extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar exposedDataFieldsSectionActionsBar;
    private String rosterName;
    public RosterExposedDataFieldsInfo(UserClientInfo currentUserClientInfo,String rosterName){
        this.currentUserClientInfo=currentUserClientInfo;
        this.rosterName=rosterName;
        setSpacing(true);
        setMargin(true);
        SecondarySectionTitle belongsToRolesSectionTitle=new SecondarySectionTitle("Roster Displayed Data Fields Filter");
        addComponent(belongsToRolesSectionTitle);

        exposedDataFieldsSectionActionsBar=new SectionActionsBar(
                new Label("Roster : <b>"+this.rosterName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(exposedDataFieldsSectionActionsBar);

        ActivityDataFieldsActionTable activityDataFieldsActionTable =new ActivityDataFieldsActionTable(this.currentUserClientInfo,"300px",false,true);
        activityDataFieldsActionTable.setDataFieldQueryType(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ROSTER);
        activityDataFieldsActionTable.setDataFieldsQueryId(this.rosterName);
        addComponent(activityDataFieldsActionTable);
    }

    @Override
    public void attach() {
        super.attach();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        if(currentActivitySpaceComponentInfo!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            Label sectionActionBarLabel=new Label("Roster : <b>"+this.rosterName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            exposedDataFieldsSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
    }
}

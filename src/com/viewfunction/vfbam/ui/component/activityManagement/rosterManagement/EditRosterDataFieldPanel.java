package com.viewfunction.vfbam.ui.component.activityManagement.rosterManagement;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityDataFieldsActionTable;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class EditRosterDataFieldPanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private RosterEditor rosterEditor;
    private SectionActionsBar addNewDataFieldActionsBar;
    private ActivityDataFieldsActionTable relatedActivityDataFieldsActionTable;
    public EditRosterDataFieldPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        setSpacing(true);
        setMargin(true);
        // Add New Role Section
        MainSectionTitle addNewParticipantSectionTitle=new MainSectionTitle("Add New Data Field");
        addComponent(addNewParticipantSectionTitle);
        addNewDataFieldActionsBar=new SectionActionsBar(new Label("Activity Space : <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(addNewDataFieldActionsBar);
        RosterEditor rosterEditor=new RosterEditor(this.currentUserClientInfo, RosterEditor.EDITMODE_NEW);
        //rosterEditor.setContainerAddNewRosterPanel(this);
        addComponent(rosterEditor);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    /*
    public void addNewRosterFinishCallBack(String rosterName,String rosterDisplayName,String rosterDescription){
        if(this.relatedRostersActionTable !=null){
            this.relatedRostersActionTable.addRoster(rosterName, rosterDisplayName, rosterDescription);
        }
        //close dialog window
        if(this.containerDialog!=null){
            this.containerDialog.close();
        }
    }
*/
    @Override
    public void attach() {
        super.attach();
        if(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo()!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            Label activitySpaceNameLabel=new Label("Activity Space : <b>"+activitySpaceName+"</b>" , ContentMode.HTML);
            addNewDataFieldActionsBar.resetSectionActionsBarContent(activitySpaceNameLabel);
        }
    }
/*
    public void setRelatedRostersTable(RostersActionTable relatedRostersActionTable) {
        this.relatedRostersActionTable = relatedRostersActionTable;
    }
 */
}

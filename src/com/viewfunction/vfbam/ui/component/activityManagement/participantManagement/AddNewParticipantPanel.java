package com.viewfunction.vfbam.ui.component.activityManagement.participantManagement;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class AddNewParticipantPanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private ParticipantEditor participantEditor;
    private SectionActionsBar addNewParticipantActionsBar;
    private ParticipantsActionTable relatedParticipantsTable;

    public AddNewParticipantPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        setSpacing(true);
        setMargin(true);
        // Add New Participant Section
        MainSectionTitle addNewParticipantSectionTitle=new MainSectionTitle("Add New Participant");
        addComponent(addNewParticipantSectionTitle);
        addNewParticipantActionsBar=new SectionActionsBar(
                new Label("Activity Space : <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(addNewParticipantActionsBar);
        participantEditor=new ParticipantEditor(this.currentUserClientInfo,ParticipantEditor.EDITMODE_NEW);
        participantEditor.setContainerAddNewParticipantPanel(this);
        addComponent(participantEditor);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void setRelatedParticipantsTable(ParticipantsActionTable relatedParticipantsTable) {
        this.relatedParticipantsTable = relatedParticipantsTable;
    }

    public ParticipantsActionTable getRelatedParticipantsTable() {
        return this.relatedParticipantsTable;
    }

    public void addNewParticipantFinishCallBack(String participantName,String participantDisplayName,String participantType){
        if(this.relatedParticipantsTable!=null){
            this.relatedParticipantsTable.addParticipant(participantName,participantDisplayName,participantType);
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
            addNewParticipantActionsBar.resetSectionActionsBarContent(activitySpaceNameLabel);
        }
    }
}

package com.viewfunction.vfbam.ui.component.activityManagement.participantManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.security.Participant;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.business.activitySpace.dao.ActivitySpaceMetaInfoDAO;
import com.viewfunction.vfbam.ui.component.activityManagement.roleManagement.RolesActionTable;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ParticipantBelongedRolesSelector extends VerticalLayout {
    private Window containerDialog;
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar belongsToRolesSectionActionsBar;
    private TwinColSelect participantRolesSelect;
    private Map<String,Role> rolesInfoMap;
    private Participant participant;
    private RolesActionTable relatedRolesActionTable;
    public ParticipantBelongedRolesSelector(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        setSpacing(true);
        setMargin(true);
        SecondarySectionTitle belongsToRolesSectionTitle=new SecondarySectionTitle("Roles Participant Belongs To");
        addComponent(belongsToRolesSectionTitle);
        rolesInfoMap=new HashMap<String,Role>();

        belongsToRolesSectionActionsBar=new SectionActionsBar(
                new Label("Participant : <b>"+""+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(belongsToRolesSectionActionsBar);

        participantRolesSelect = new TwinColSelect();
        participantRolesSelect.setLeftColumnCaption(" Available Roles");
        participantRolesSelect.setRightColumnCaption(" Belonged To Roles");
        participantRolesSelect.setNewItemsAllowed(false);
        participantRolesSelect.setWidth("100%");
        participantRolesSelect.setHeight("270px");
        addComponent(participantRolesSelect);

        HorizontalLayout actionButtonsContainer=new HorizontalLayout();
        actionButtonsContainer.setSpacing(true);
        actionButtonsContainer.setMargin(true);
        actionButtonsContainer.setWidth("100%");
        addComponent(actionButtonsContainer);

        Button confirmUpdateButton = new Button("Update");
        confirmUpdateButton.setIcon(FontAwesome.SAVE);
        confirmUpdateButton.addStyleName("small");
        confirmUpdateButton.addStyleName("primary");
        actionButtonsContainer.addComponent(confirmUpdateButton);
        actionButtonsContainer.setComponentAlignment(confirmUpdateButton, Alignment.MIDDLE_RIGHT);
        actionButtonsContainer.setExpandRatio(confirmUpdateButton, 1L);

        final ParticipantBelongedRolesSelector self=this;
        confirmUpdateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                String participantName=self.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
                Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+
                        " Please confirm to update the belonged to roles information of participant <b>"+participantName +"</b>.", ContentMode.HTML);
                final ConfirmDialog modifyParticipantRolesConfirmDialog = new ConfirmDialog();
                modifyParticipantRolesConfirmDialog.setConfirmMessage(confirmMessage);
                Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        //update participant's role info
                        self.doUpdateParticipantBelongedRolesInfo();
                        //close confirm dialog
                        modifyParticipantRolesConfirmDialog.close();
                        //close self dialog window
                        if(self.containerDialog!=null){
                            self.containerDialog.close();
                        }
                    }
                };
                modifyParticipantRolesConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
                UI.getCurrent().addWindow(modifyParticipantRolesConfirmDialog);
            }
        });

        Button cancelUpdateButton = new Button("Cancel Update");
        cancelUpdateButton.setIcon(FontAwesome.TIMES);
        cancelUpdateButton.addStyleName("small");
        actionButtonsContainer.addComponent(cancelUpdateButton);
        actionButtonsContainer.setComponentAlignment(cancelUpdateButton, Alignment.MIDDLE_RIGHT);
        cancelUpdateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close self dialog window
                if(self.containerDialog!=null){
                    self.containerDialog.close();
                }
            }
        });
    }

    public void doUpdateParticipantBelongedRolesInfo(){
        Set participantRolesSet=(Set)participantRolesSelect.getValue();
        String[] strRoleArray = new String[participantRolesSet.size()];
        String[] roleCombinationStrArray=(String[]) participantRolesSet.toArray(strRoleArray);
        String[] newParticipantRoleArray=new String[roleCombinationStrArray.length];
        for(int i=0;i<roleCombinationStrArray.length;i++){
            String roleName=rolesInfoMap.get(roleCombinationStrArray[i]).getRoleName();
            newParticipantRoleArray[i]=roleName;
        }
        boolean updateRolesResult=ActivitySpaceOperationUtil.updateParticipantRoles(participant,newParticipantRoleArray);
        if(updateRolesResult){
            if(relatedRolesActionTable!=null){
               relatedRolesActionTable.loadRolesData();
            }
            Notification resultNotification = new Notification("Update Data Operation Success",
                    "Update participant information success", Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }else{
            Notification errorNotification = new Notification("Update Participant Information Error",
                    "Server side error occurred", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
        }
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    @Override
    public void attach() {
        super.attach();
        participantRolesSelect.clear();
        participantRolesSelect.removeAllItems();
        if(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo()!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            String participantName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
            Label sectionActionBarLabel=new Label("Participant : <b>"+participantName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            belongsToRolesSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        ActivitySpaceMetaInfoDAO activitySpaceMetaInfoDAO=
                ActivitySpaceOperationUtil.getActivitySpaceMetaInfo(activitySpaceName, new String[]{ActivitySpaceOperationUtil.ACTIVITYSPACE_METAINFOTYPE_ROLE});
        Role[] roles=activitySpaceMetaInfoDAO.getRoles();
        if(roles!=null){
            for(Role currentRole:roles){
                String roleCombinationStr=currentRole.getRoleName()+" ("+currentRole.getDisplayName()+")";
                participantRolesSelect.addItem(roleCombinationStr);
                rolesInfoMap.put(roleCombinationStr,currentRole);
            }
        }
        if(participant!=null){
            Role[] participantRoles=ActivitySpaceOperationUtil.getRolesByParticipant(participant);
            if(participantRoles!=null){
                for(Role currentRole:participantRoles){
                    String roleCombinationStr=currentRole.getRoleName()+" ("+currentRole.getDisplayName()+")";
                    participantRolesSelect.select(roleCombinationStr);
                }
            }
        }
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public void setRelatedRolesActionTable(RolesActionTable relatedRolesActionTable) {
        this.relatedRolesActionTable = relatedRolesActionTable;
    }
}

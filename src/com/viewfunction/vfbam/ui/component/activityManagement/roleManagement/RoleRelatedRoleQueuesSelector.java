package com.viewfunction.vfbam.ui.component.activityManagement.roleManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.activityView.RoleQueue;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.business.activitySpace.dao.ActivitySpaceMetaInfoDAO;
import com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement.RoleQueuesActionTable;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.SecondarySectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RoleRelatedRoleQueuesSelector extends VerticalLayout {
    private Window containerDialog;
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar relatedToRoleQueuesSectionActionsBar;
    private TwinColSelect roleRoleQueuesSelect;
    private Map<String,RoleQueue> roleQueuesInfoMap;
    private Role role;
    private RoleQueuesActionTable relatedRoleQueuesActionTable;
    public RoleRelatedRoleQueuesSelector(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        setSpacing(true);
        setMargin(true);
        SecondarySectionTitle relatedToRoleQueuesSectionTitle=new SecondarySectionTitle("Role Queues Role Related To");
        addComponent(relatedToRoleQueuesSectionTitle);
        roleQueuesInfoMap=new HashMap<String,RoleQueue>();
        relatedToRoleQueuesSectionActionsBar=new SectionActionsBar(
                new Label("Role : <b>"+""+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(relatedToRoleQueuesSectionActionsBar);

        roleRoleQueuesSelect = new TwinColSelect();
        roleRoleQueuesSelect.setLeftColumnCaption(" Available Role Queues");
        roleRoleQueuesSelect.setRightColumnCaption(" Related Role Queues");
        roleRoleQueuesSelect.setNewItemsAllowed(false);
        roleRoleQueuesSelect.setWidth("100%");
        roleRoleQueuesSelect.setHeight("270px");
        addComponent(roleRoleQueuesSelect);

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

        String roleName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        final Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+
                " Please confirm to update the related to role queues information of role <b>"+roleName +"</b>.", ContentMode.HTML);
        final RoleRelatedRoleQueuesSelector self=this;
        confirmUpdateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final ConfirmDialog modifyRoleRelatedRoleQueuesConfirmDialog = new ConfirmDialog();
                modifyRoleRelatedRoleQueuesConfirmDialog.setConfirmMessage(confirmMessage);

                Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        self.doUpdateRoleRelatedRoleQueuesInfo();
                        //close confirm dialog
                        modifyRoleRelatedRoleQueuesConfirmDialog.close();
                        //close self dialog window
                        if(self.containerDialog!=null){
                            self.containerDialog.close();
                        }
                    }
                };
                modifyRoleRelatedRoleQueuesConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
                UI.getCurrent().addWindow(modifyRoleRelatedRoleQueuesConfirmDialog);
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

    public void doUpdateRoleRelatedRoleQueuesInfo(){
        Set roleRoleQueuesSet=(Set)roleRoleQueuesSelect.getValue();
        String[] strRoleQueueArray = new String[roleRoleQueuesSet.size()];
        String[] roleQueueCombinationStrArray=(String[]) roleRoleQueuesSet.toArray(strRoleQueueArray);
        String[] newRoleRoleQueuesArray=new String[roleQueueCombinationStrArray.length];
        for(int i=0;i<roleQueueCombinationStrArray.length;i++){
            String roleName=roleQueuesInfoMap.get(roleQueueCombinationStrArray[i]).getQueueName();
            newRoleRoleQueuesArray[i]=roleName;
        }
        boolean updateRoleQueuesResult=ActivitySpaceOperationUtil.updateRoleRoleQueues(this.role, newRoleRoleQueuesArray);
        if(updateRoleQueuesResult){
            if(this.relatedRoleQueuesActionTable!=null){
                this.relatedRoleQueuesActionTable.loadRoleQueuesData();
            }
            Notification resultNotification = new Notification("Update Data Operation Success",
                    "Update roleQueue information success", Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }else{
            Notification errorNotification = new Notification("Update RoleQueue Information Error",
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
        roleRoleQueuesSelect.clear();
        roleRoleQueuesSelect.removeAllItems();
        if(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo()!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            String participantName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
            Label sectionActionBarLabel=new Label("Role : <b>"+participantName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            relatedToRoleQueuesSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        ActivitySpaceMetaInfoDAO activitySpaceMetaInfoDAO=
                ActivitySpaceOperationUtil.getActivitySpaceMetaInfo(activitySpaceName, new String[]{ActivitySpaceOperationUtil.ACTIVITYSPACE_METAINFOTYPE_ROLEQUEUE});
        RoleQueue[] roleQueues=activitySpaceMetaInfoDAO.getRoleQueues();
        if(roleQueues!=null){
            for(RoleQueue currentRoleQueue:roleQueues){
                String roleQueueCombinationStr=currentRoleQueue.getQueueName()+" ("+currentRoleQueue.getDisplayName()+")";
                roleRoleQueuesSelect.addItem(roleQueueCombinationStr);
                roleQueuesInfoMap.put(roleQueueCombinationStr,currentRoleQueue);
            }
        }
        if(role!=null){
            RoleQueue[] roleRelatedRoleQueues=ActivitySpaceOperationUtil.getRoleQueuesByRole(role);
            if(roleRelatedRoleQueues!=null){
                for(RoleQueue currentRoleQueue:roleRelatedRoleQueues) {
                    String roleQueueCombinationStr = currentRoleQueue.getQueueName() + " (" + currentRoleQueue.getDisplayName() + ")";
                    roleRoleQueuesSelect.select(roleQueueCombinationStr);
                }
            }
        }
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRelatedRoleQueuesActionTable(RoleQueuesActionTable relatedRoleQueuesActionTable) {
        this.relatedRoleQueuesActionTable = relatedRoleQueuesActionTable;
    }
}

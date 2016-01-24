package com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.activityView.RoleQueue;
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

public class RoleQueueRelatedRolesSelector  extends VerticalLayout {
    private Window containerDialog;
    private UserClientInfo currentUserClientInfo;
    private SectionActionsBar relatedToRolesSectionActionsBar;
    private Map<String,Role> rolesInfoMap;
    private RoleQueue roleQueue;
    private TwinColSelect roleQueueRolesSelect;
    private RolesActionTable relatedRolesActionTable;
    public RoleQueueRelatedRolesSelector(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        setSpacing(true);
        setMargin(true);
        SecondarySectionTitle relatedToRoleQueuesSectionTitle=new SecondarySectionTitle("Roles Role Queue Related To");
        addComponent(relatedToRoleQueuesSectionTitle);
        rolesInfoMap=new HashMap<String,Role>();
        relatedToRolesSectionActionsBar=new SectionActionsBar(
                new Label("Role Queue: <b>"+""+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" ]" , ContentMode.HTML));
        addComponent(relatedToRolesSectionActionsBar);

        roleQueueRolesSelect = new TwinColSelect();
        roleQueueRolesSelect.setLeftColumnCaption(" Available Roles");
        roleQueueRolesSelect.setRightColumnCaption(" Related Roles");
        roleQueueRolesSelect.setNewItemsAllowed(false);
        roleQueueRolesSelect.setWidth("100%");
        roleQueueRolesSelect.setHeight("270px");
        addComponent(roleQueueRolesSelect);

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

        String roleQueueName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        final Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+
                " Please confirm to update the related to roles information of role queue <b>"+roleQueueName +"</b>.", ContentMode.HTML);
        final RoleQueueRelatedRolesSelector self=this;
        confirmUpdateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final ConfirmDialog modifyRoleQueueRelatedRolesConfirmDialog = new ConfirmDialog();
                modifyRoleQueueRelatedRolesConfirmDialog.setConfirmMessage(confirmMessage);

                Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        self.doUpdateRoleQueueRelatedRolesInfo();
                        //close confirm dialog
                        modifyRoleQueueRelatedRolesConfirmDialog.close();
                        //close self dialog window
                        if(self.containerDialog!=null){
                            self.containerDialog.close();
                        }
                    }
                };
                modifyRoleQueueRelatedRolesConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
                UI.getCurrent().addWindow(modifyRoleQueueRelatedRolesConfirmDialog);
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

    public void doUpdateRoleQueueRelatedRolesInfo(){
        Set roleQueueRolesSet=(Set)roleQueueRolesSelect.getValue();
        String[] strRoleArray = new String[roleQueueRolesSet.size()];
        String[] roleCombinationStrArray=(String[]) roleQueueRolesSet.toArray(strRoleArray);
        String[] newRoleQueueRolesArray=new String[roleCombinationStrArray.length];
        for(int i=0;i<roleCombinationStrArray.length;i++){
            String roleName=rolesInfoMap.get(roleCombinationStrArray[i]).getRoleName();
            newRoleQueueRolesArray[i]=roleName;
        }
        boolean updateRolesResult=ActivitySpaceOperationUtil.updateRoleQueueRoles(this.roleQueue, newRoleQueueRolesArray);
        if(updateRolesResult){
            if(this.relatedRolesActionTable!=null){
                this.relatedRolesActionTable.loadRolesData();
            }
            Notification resultNotification = new Notification("Update Data Operation Success",
                    "Update role information success", Notification.Type.HUMANIZED_MESSAGE);
            resultNotification.setPosition(Position.MIDDLE_CENTER);
            resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
            resultNotification.show(Page.getCurrent());
        }else{
            Notification errorNotification = new Notification("Update Role Information Error",
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
        roleQueueRolesSelect.clear();
        roleQueueRolesSelect.removeAllItems();
        if(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo()!=null){
            String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            String participantName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
            Label sectionActionBarLabel=new Label("Role Queue: <b>"+participantName+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            relatedToRolesSectionActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        ActivitySpaceMetaInfoDAO activitySpaceMetaInfoDAO=
                ActivitySpaceOperationUtil.getActivitySpaceMetaInfo(activitySpaceName, new String[]{ActivitySpaceOperationUtil.ACTIVITYSPACE_METAINFOTYPE_ROLE});
        Role[] roles=activitySpaceMetaInfoDAO.getRoles();
        if(roles!=null){
            for(Role currentRole:roles){
                String roleCombinationStr=currentRole.getRoleName()+" ("+currentRole.getDisplayName()+")";
                roleQueueRolesSelect.addItem(roleCombinationStr);
                rolesInfoMap.put(roleCombinationStr,currentRole);
            }
        }
        if(this.roleQueue!=null){
            Role[] roleQueueRelatedRoles=ActivitySpaceOperationUtil.getRolesByRoleQueue(this.roleQueue);
            if(roleQueueRelatedRoles!=null){
                for(Role currentRole:roleQueueRelatedRoles){
                    String roleCombinationStr=currentRole.getRoleName()+" ("+currentRole.getDisplayName()+")";
                    roleQueueRolesSelect.select(roleCombinationStr);
                }
            }
        }
    }

    public void setRoleQueue(RoleQueue roleQueue) {
        this.roleQueue = roleQueue;
    }

    public void setRelatedRolesActionTable(RolesActionTable relatedRolesActionTable) {
        this.relatedRolesActionTable = relatedRolesActionTable;
    }
}

package com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityManagementConst;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivitySpaceComponentModifyEvent;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class RoleQueuesTableRowActions extends HorizontalLayout {
    private UserClientInfo currentUserClientInfo;
    private String roleQueueName;
    private boolean allowRemoveOperation;
    private RoleQueuesActionTable containerRoleQueuesActionTable;
    public RoleQueuesTableRowActions(UserClientInfo currentUserClientInfo,String roleQueueName,boolean allowRemoveOperation){
        this.currentUserClientInfo=currentUserClientInfo;
        this.roleQueueName=roleQueueName;
        this.allowRemoveOperation=allowRemoveOperation;
        Button showContainsDataFieldsButton = new Button();
        showContainsDataFieldsButton.setIcon(FontAwesome.USERS);
        showContainsDataFieldsButton.setDescription("Related Roles");
        showContainsDataFieldsButton.addStyleName("small");
        showContainsDataFieldsButton.addStyleName("borderless");
        addComponent(showContainsDataFieldsButton);
        final RoleQueueRelatedRolesInfo roleQueueRelatedRolesInfo=new RoleQueueRelatedRolesInfo(this.currentUserClientInfo,this.roleQueueName);
        showContainsDataFieldsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(700.0f, Sizeable.Unit.PIXELS);
                window.setHeight(490.0f, Sizeable.Unit.PIXELS);
                window.center();
                window.setContent(roleQueueRelatedRolesInfo);
                UI.getCurrent().addWindow(window);
            }
        });

        Button showContainsActivityStepsButton = new Button();
        showContainsActivityStepsButton.setIcon(FontAwesome.TH_LIST);
        showContainsActivityStepsButton.setDescription("Displayed Data Fields Filter");
        showContainsActivityStepsButton.addStyleName("small");
        showContainsActivityStepsButton.addStyleName("borderless");
        addComponent(showContainsActivityStepsButton);
        final RoleQueueExposedDataFieldsInfo roleQueueExposedDataFieldsInfo=new RoleQueueExposedDataFieldsInfo(this.currentUserClientInfo,this.roleQueueName);
        showContainsActivityStepsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(1000.0f, Sizeable.Unit.PIXELS);
                window.setHeight(490.0f, Sizeable.Unit.PIXELS);
                window.center();
                window.setContent(roleQueueExposedDataFieldsInfo);
                UI.getCurrent().addWindow(window);
            }
        });

        Button startActivityButton = new Button();
        startActivityButton.setIcon(FontAwesome.SLIDERS);
        startActivityButton.setDescription("Contains Activity Steps");
        startActivityButton.addStyleName("small");
        startActivityButton.addStyleName("borderless");
        addComponent(startActivityButton);
        final RoleQueueContainsActivityStepsInfo roleQueueContainsActivityStepsInfo=new RoleQueueContainsActivityStepsInfo(this.currentUserClientInfo,this.roleQueueName);
        startActivityButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(1200.0f, Sizeable.Unit.PIXELS);
                window.setHeight(490.0f, Sizeable.Unit.PIXELS);
                window.center();
                window.setContent(roleQueueContainsActivityStepsInfo);
                UI.getCurrent().addWindow(window);
            }
        });

        Label spaceDivLabel=new Label(" ");
        spaceDivLabel.setWidth("10px");
        addComponent(spaceDivLabel);

        Button removeCurrentRoleQueueButton = new Button();
        removeCurrentRoleQueueButton.setIcon(FontAwesome.TRASH_O);
        removeCurrentRoleQueueButton.setDescription("Remove This Role Queue");
        removeCurrentRoleQueueButton.addStyleName("small");
        removeCurrentRoleQueueButton.addStyleName("borderless");
        addComponent(removeCurrentRoleQueueButton);
        final String roleQueueNameToRemove=this.roleQueueName;
        removeCurrentRoleQueueButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {

                Label confirmMessage = new Label(FontAwesome.INFO.getHtml() +
                        " Please confirm to remove role queue  <b>" + roleQueueNameToRemove + "</b>.", ContentMode.HTML);
                final ConfirmDialog removeRosterConfirmDialog = new ConfirmDialog();
                removeRosterConfirmDialog.setConfirmMessage(confirmMessage);
                Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                    @Override
                    public void buttonClick(final Button.ClickEvent event) {
                        //close confirm dialog
                        removeRosterConfirmDialog.close();
                        //remove roster in roster table
                        if (getContainerRoleQueuesActionTable() != null) {
                            getContainerRoleQueuesActionTable().removeRoleQueue(roleQueueNameToRemove);
                        }
                    }
                };
                removeRosterConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
                UI.getCurrent().addWindow(removeRosterConfirmDialog);
            }
        });
        if(!this.allowRemoveOperation){
            removeCurrentRoleQueueButton.setEnabled(false);
        }
    }

    public RoleQueuesActionTable getContainerRoleQueuesActionTable() {
        return containerRoleQueuesActionTable;
    }

    public void setContainerRoleQueuesActionTable(RoleQueuesActionTable containerRoleQueuesActionTable) {
        this.containerRoleQueuesActionTable = containerRoleQueuesActionTable;
    }
}

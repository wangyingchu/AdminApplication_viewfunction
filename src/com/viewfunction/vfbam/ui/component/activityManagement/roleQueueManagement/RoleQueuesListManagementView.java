package com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;

import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class RoleQueuesListManagementView extends VerticalLayout implements View {
    private UserClientInfo currentUserClientInfo;
    public RoleQueuesListManagementView(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setMargin(true);
        this.setSpacing(true);
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        VerticalLayout viewContentContainer = new VerticalLayout();
        viewContentContainer.setMargin(false);
        viewContentContainer.setSpacing(false);
        viewContentContainer.addStyleName("ui_appSubViewContainer");
        this.addComponent(viewContentContainer);
        // View Title
        MainSectionTitle mainSectionTitle=new MainSectionTitle("Role Queues Info");
        viewContentContainer.addComponent(mainSectionTitle);
        // Rosters List Section
        SectionActionsBar roleQueuesListSectionActionsBar=new SectionActionsBar(new Label(FontAwesome.ALIGN_JUSTIFY.getHtml() + " "+"Role Queues List", ContentMode.HTML));
        viewContentContainer.addComponent(roleQueuesListSectionActionsBar);
        SectionActionButton addNewRoleQueueActionButton = new SectionActionButton();
        addNewRoleQueueActionButton.setCaption("Add New Role Queue");
        addNewRoleQueueActionButton.setIcon(FontAwesome.PLUS_SQUARE);
        roleQueuesListSectionActionsBar.addActionComponent(addNewRoleQueueActionButton);

        RoleQueuesActionTable roleQueuesActionTable=new RoleQueuesActionTable(this.currentUserClientInfo, null,true,true);
        roleQueuesActionTable.setRoleQueuesType(RoleQueuesActionTable.ROLEQUEUES_TYPE_ROLEQUEUE);
        roleQueuesActionTable.setRoleQueuesQueryId(null);
        viewContentContainer.addComponent(roleQueuesActionTable);

        final AddNewRoleQueuePanel addNewRoleQueuePanel=new AddNewRoleQueuePanel(this.currentUserClientInfo);
        addNewRoleQueuePanel.setRelatedRoleQueuesTable(roleQueuesActionTable);
        addNewRoleQueueActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(700.0f, Unit.PIXELS);
                window.setHeight(390.0f, Unit.PIXELS);
                window.center();
                window.setModal(true);
                window.setContent(addNewRoleQueuePanel);
                addNewRoleQueuePanel.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }
}

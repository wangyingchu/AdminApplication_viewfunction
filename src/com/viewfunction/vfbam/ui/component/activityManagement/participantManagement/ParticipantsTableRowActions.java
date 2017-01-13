package com.viewfunction.vfbam.ui.component.activityManagement.participantManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class ParticipantsTableRowActions  extends HorizontalLayout {
    private UserClientInfo currentUserClientInfo;
    private String participantName;
    private boolean allowRemoveOperation;
    public ParticipantsTableRowActions(UserClientInfo currentUserClientInfo,String participantName,boolean allowRemoveOperation){
        this.currentUserClientInfo=currentUserClientInfo;
        this.participantName=participantName;
        this.allowRemoveOperation=allowRemoveOperation;

        Button showBelongedRolesButton = new Button();
        showBelongedRolesButton.setIcon(FontAwesome.USERS);
        showBelongedRolesButton.setDescription("Belongs To Roles");
        showBelongedRolesButton.addStyleName("small");
        showBelongedRolesButton.addStyleName("borderless");
        addComponent(showBelongedRolesButton);
        final ParticipantBelongedRolesInfo participantBelongedRolesInfo=new ParticipantBelongedRolesInfo(this.currentUserClientInfo,this.participantName);
        showBelongedRolesButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(800.0f, Sizeable.Unit.PIXELS);
                window.setHeight(490.0f, Sizeable.Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setContent(participantBelongedRolesInfo);
                showBelongedRolesButton.setEnabled(false);
                window.addCloseListener(new Window.CloseListener() {
                    @Override
                    public void windowClose(Window.CloseEvent e) {
                        showBelongedRolesButton.setEnabled(true);
                    }
                });
                UI.getCurrent().addWindow(window);
            }
        });

        Button showWorkingTasksButton = new Button();
        showWorkingTasksButton.setIcon(FontAwesome.TASKS);
        showWorkingTasksButton.setDescription("Participant Tasks");
        showWorkingTasksButton.addStyleName("small");
        showWorkingTasksButton.addStyleName("borderless");
        addComponent(showWorkingTasksButton);
        final ParticipantWorkingTasksInfo participantWorkingTasksInfo=new ParticipantWorkingTasksInfo(this.currentUserClientInfo,this.participantName);
        showWorkingTasksButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(1200.0f, Unit.PIXELS);
                window.setHeight(490.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setContent(participantWorkingTasksInfo);
                showWorkingTasksButton.setEnabled(false);
                window.addCloseListener(new Window.CloseListener() {
                    @Override
                    public void windowClose(Window.CloseEvent e) {
                        showWorkingTasksButton.setEnabled(true);
                    }
                });
                UI.getCurrent().addWindow(window);
            }
        });

        Label spaceDivLabel=new Label(" ");
        spaceDivLabel.setWidth("10px");
        addComponent(spaceDivLabel);

        Button disableParticipantButton = new Button();
        disableParticipantButton.setIcon(FontAwesome.BAN);
        disableParticipantButton.setDescription("Disable Participant");
        disableParticipantButton.addStyleName("small");
        disableParticipantButton.addStyleName("borderless");
        addComponent(disableParticipantButton);
        if(!this.allowRemoveOperation){
            disableParticipantButton.setEnabled(false);
        }
    }
}

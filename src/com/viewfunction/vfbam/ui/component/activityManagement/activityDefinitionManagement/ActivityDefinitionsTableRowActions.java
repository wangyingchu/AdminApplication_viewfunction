package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import com.viewfunction.vfbam.ui.component.activityManagement.participantManagement.ParticipantBelongedRolesInfo;
import com.viewfunction.vfbam.ui.component.activityManagement.participantManagement.ParticipantWorkingTasksInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class ActivityDefinitionsTableRowActions extends HorizontalLayout {
    private UserClientInfo currentUserClientInfo;
    private String activityType;
    public ActivityDefinitionsTableRowActions(UserClientInfo currentUserClientInfo,String activityType){
        this.currentUserClientInfo=currentUserClientInfo;
        this.activityType=activityType;
        Button showContainsDataFieldsButton = new Button();
        showContainsDataFieldsButton.setIcon(FontAwesome.TH_LIST);
        showContainsDataFieldsButton.setDescription("Contains Data Fields");
        showContainsDataFieldsButton.addStyleName("small");
        showContainsDataFieldsButton.addStyleName("borderless");
        addComponent(showContainsDataFieldsButton);
        final ActivityDefinitionContainedDataFieldsInfo activityDefinitionContainedDataFieldsInfo=new ActivityDefinitionContainedDataFieldsInfo(this.currentUserClientInfo,this.activityType);
        showContainsDataFieldsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(1000.0f, Sizeable.Unit.PIXELS);
                window.setHeight(490.0f, Sizeable.Unit.PIXELS);
                window.center();
                window.setContent(activityDefinitionContainedDataFieldsInfo);
                UI.getCurrent().addWindow(window);
            }
        });

        Button showContainsActivityStepsButton = new Button();
        showContainsActivityStepsButton.setIcon(FontAwesome.SLIDERS);
        showContainsActivityStepsButton.setDescription("Contains Activity Steps");
        showContainsActivityStepsButton.addStyleName("small");
        showContainsActivityStepsButton.addStyleName("borderless");
        addComponent(showContainsActivityStepsButton);
        final ActivityDefinitionExposedStepsInfo activityDefinitionExposedStepsInfo=new ActivityDefinitionExposedStepsInfo(this.currentUserClientInfo,this.activityType);
        showContainsActivityStepsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(1000.0f, Unit.PIXELS);
                window.setHeight(530.0f, Unit.PIXELS);
                window.center();
                window.setContent(activityDefinitionExposedStepsInfo);
                UI.getCurrent().addWindow(window);
            }
        });

        Button startActivityButton = new Button();
        startActivityButton.setIcon(FontAwesome.ARROW_CIRCLE_O_RIGHT);
        startActivityButton.setDescription("Start New Business Activity");
        startActivityButton.addStyleName("small");
        startActivityButton.addStyleName("borderless");
        addComponent(startActivityButton);
        final ParticipantWorkingTasksInfo participantWorkingTasksInfo2=new ParticipantWorkingTasksInfo(this.currentUserClientInfo,null);
        startActivityButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(1200.0f, Unit.PIXELS);
                window.setHeight(490.0f, Unit.PIXELS);
                window.center();
                window.setContent(participantWorkingTasksInfo2);
                UI.getCurrent().addWindow(window);
            }
        });
    }
}

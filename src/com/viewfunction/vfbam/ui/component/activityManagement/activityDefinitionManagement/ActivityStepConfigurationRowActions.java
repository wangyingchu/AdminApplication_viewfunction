package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.activityManagement.participantManagement.ParticipantWorkingTasksInfo;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityDataFieldVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityStepVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.RoleVO;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.List;

public class ActivityStepConfigurationRowActions extends HorizontalLayout {
    private UserClientInfo currentUserClientInfo;
    private ActivityStepVO currentActivityStep;
    private List<ActivityDataFieldVO> activityDataFieldsList;
    private ActivityStepExposedDataFieldsEditor activityStepExposedDataFieldsEditor;
    private ActivityStepItem relatedActivityStepItem;
    public ActivityStepConfigurationRowActions(UserClientInfo currentUserClientInfo,ActivityStepVO currentActivityStep,List<ActivityDataFieldVO> activityDataFieldsList,List<RoleVO> rolesList){
        this.currentUserClientInfo=currentUserClientInfo;
        this.currentActivityStep= currentActivityStep;
        this.activityDataFieldsList = activityDataFieldsList;
        Button showContainsDataFieldsButton = new Button();
        showContainsDataFieldsButton.setIcon(FontAwesome.TH_LIST);
        showContainsDataFieldsButton.setDescription("Activity Step Exposed Data Fields");
        showContainsDataFieldsButton.addStyleName("small");
        showContainsDataFieldsButton.addStyleName("borderless");
        addComponent(showContainsDataFieldsButton);
        activityStepExposedDataFieldsEditor=
                new ActivityStepExposedDataFieldsEditor(this.currentUserClientInfo,this.currentActivityStep,this.activityDataFieldsList);
        showContainsDataFieldsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(900.0f, Sizeable.Unit.PIXELS);
                window.setHeight(490.0f, Sizeable.Unit.PIXELS);
                window.center();
                window.setModal(true);
                window.setContent(activityStepExposedDataFieldsEditor);
                activityStepExposedDataFieldsEditor.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });

        Label spaceDivLabel=new Label("|");
        addComponent(spaceDivLabel);

        Button showActivityStepPropertiesButton = new Button();
        showActivityStepPropertiesButton.setIcon(FontAwesome.BARS);
        showActivityStepPropertiesButton.setDescription("Activity Step Process Properties");
        showActivityStepPropertiesButton.addStyleName("small");
        showActivityStepPropertiesButton.addStyleName("borderless");
        addComponent(showActivityStepPropertiesButton);
        final ActivityStepProcessVariablesEditor activityStepProcessVariablesEditor=
                new ActivityStepProcessVariablesEditor(this.currentUserClientInfo,this.currentActivityStep,rolesList);
        showActivityStepPropertiesButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(900.0f, Sizeable.Unit.PIXELS);
                window.setHeight(490.0f, Sizeable.Unit.PIXELS);
                window.center();
                window.setModal(true);
                window.setContent(activityStepProcessVariablesEditor);
                activityStepProcessVariablesEditor.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });

        Button showStepDecisionPointsPropertiesButton = new Button();
        showStepDecisionPointsPropertiesButton.setIcon(FontAwesome.COG);
        showStepDecisionPointsPropertiesButton.setDescription("Activity Step Decision Points Properties");
        showStepDecisionPointsPropertiesButton.addStyleName("small");
        showStepDecisionPointsPropertiesButton.addStyleName("borderless");
        addComponent(showStepDecisionPointsPropertiesButton);
        final ActivityStepDecisionPointEditor activityStepDecisionPointEditor=new ActivityStepDecisionPointEditor(this.currentUserClientInfo,this.currentActivityStep);
        showStepDecisionPointsPropertiesButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(900.0f, Sizeable.Unit.PIXELS);
                window.setHeight(510.0f, Sizeable.Unit.PIXELS);
                window.center();
                window.setModal(true);
                window.setContent(activityStepDecisionPointEditor);
                activityStepDecisionPointEditor.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });

        Button removeCurrentStepButton = new Button();
        removeCurrentStepButton.setIcon(FontAwesome.TRASH_O);
        removeCurrentStepButton.setDescription("Don't Expose This Activity Step Anymore");
        removeCurrentStepButton.addStyleName("small");
        removeCurrentStepButton.addStyleName("borderless");
        addComponent(removeCurrentStepButton);
        removeCurrentStepButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                removeCurrentStepFromExposedStepList();
            }
        });
    }

    public ActivityStepExposedDataFieldsEditor getActivityStepExposedDataFieldsEditor() {
        return activityStepExposedDataFieldsEditor;
    }

    public void removeCurrentStepFromExposedStepList(){
        final ActivityStepItem activityStepItem=this.relatedActivityStepItem;
        Label confirmMessage = new Label(FontAwesome.INFO.getHtml() +
                " Please confirm don't exposed activity step <b>" + currentActivityStep.getActivityStepName() + "</b>.", ContentMode.HTML);
        final ConfirmDialog deleteStepExposeConfirmDialog = new ConfirmDialog();
        deleteStepExposeConfirmDialog.setConfirmMessage(confirmMessage);

        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                activityStepItem.removeCurrentActivityStepFromExposedStepList();
                deleteStepExposeConfirmDialog.close();
            }
        };
        deleteStepExposeConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(deleteStepExposeConfirmDialog);
    }

    public void setRelatedActivityStepItem(ActivityStepItem relatedActivityStepItem) {
        this.relatedActivityStepItem = relatedActivityStepItem;
    }
}

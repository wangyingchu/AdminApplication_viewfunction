package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;

import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityStepVO;
import com.viewfunction.vfbam.ui.component.common.*;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class ActivityStepDecisionPointEditor  extends VerticalLayout {
    private SectionActionsBar updateStepDecisionPointActionsBar;

    private UserClientInfo currentUserClientInfo;
    private ActivityStepVO currentActivityStep;
    private Window containerDialog;
    private FormLayout form;
    private HorizontalLayout footer;
    private TextField decisionPointAttribute;
    private SectionActionButton addDecisionOptionActionButton;
    private PropertyValuesActionTable propertyValuesActionTable;
    public ActivityStepDecisionPointEditor(UserClientInfo currentUserClientInfo,ActivityStepVO currentActivityStep) {
        this.currentUserClientInfo=currentUserClientInfo;
        setSpacing(true);
        setMargin(true);
        this.currentActivityStep=currentActivityStep;

        MainSectionTitle updateStepDecisionPointSectionTitle = new MainSectionTitle("Update Activity Step Decision Point Properties");
        addComponent(updateStepDecisionPointSectionTitle);
        updateStepDecisionPointActionsBar=new SectionActionsBar(new Label("Activity Definition : <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(updateStepDecisionPointActionsBar);

        form = new FormLayout();
        form.setMargin(false);
        form.setSpacing(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);
        decisionPointAttribute = new TextField("Activity Step Decision Point Attribute Name");
        decisionPointAttribute.setRequired(false);
        decisionPointAttribute.setWidth("100%");
        decisionPointAttribute.setInputPrompt("Please input decision point attribute name");
        form.addComponent(decisionPointAttribute);

        SectionActionsBar launchDecisionPointSectionActionsBar=new SectionActionsBar(new Label(FontAwesome.LIST.getHtml() + " "+"Activity Step Decision Point Choose Options", ContentMode.HTML));
        addComponent(launchDecisionPointSectionActionsBar);
        addDecisionOptionActionButton = new SectionActionButton();
        addDecisionOptionActionButton.setCaption("Add New Option");
        addDecisionOptionActionButton.setIcon(FontAwesome.PLUS_SQUARE);
        launchDecisionPointSectionActionsBar.addActionComponent(addDecisionOptionActionButton);
        propertyValuesActionTable=new PropertyValuesActionTable(this.currentUserClientInfo,"180","Decision Point Choose List Option","Activity Step Decision Point Choose Options",true,true);
        addComponent(propertyValuesActionTable);

        addDecisionOptionActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                propertyValuesActionTable.addPropertyValue();
            }
        });

        footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        addComponent(footer);

        Button confirmButton=new Button("Confirm Change", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updateDecisionPointProperties();
            }
        });
        confirmButton.setIcon(FontAwesome.CHECK);
        confirmButton.addStyleName("primary");
        footer.addComponent(confirmButton);

        Button cancelButton=new Button("Reset", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                setDecisionPointValue();
            }
        });
        cancelButton.setIcon(FontAwesome.TIMES);
        footer.addComponent(cancelButton);
        this.setComponentAlignment(footer, Alignment.MIDDLE_CENTER);
    }

    @Override
    public void attach() {
        super.attach();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        String componentType=currentActivitySpaceComponentInfo.getComponentType();
        String componentId=currentActivitySpaceComponentInfo.getComponentId();
        if(componentType==null){
            return;
        }else{
            String activitySpaceName="";
            Label sectionActionBarLabel=null;
            if(currentActivitySpaceComponentInfo!=null){
                activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            }
            String activityStepCombinationStr=currentActivityStep.getActivityStepName()+" ("+currentActivityStep.getActivityStepDisplayName() + ")";
            sectionActionBarLabel=new Label("Activity Step : <b>"+activityStepCombinationStr+"</b> &nbsp;&nbsp;Activity Type : "+componentId+" &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            updateStepDecisionPointActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
        setDecisionPointValue();
    }

    private void setDecisionPointValue(){
        decisionPointAttribute.setValue("");
        propertyValuesActionTable.setPropertyValues(null);
        String attributeName=this.currentActivityStep.getDecisionPointAttribute();
        String[] chooseOption=this.currentActivityStep.getDecisionPointChooseOption();
        if(attributeName!=null){
            decisionPointAttribute.setValue(attributeName);
        }
        if(chooseOption!=null){
            propertyValuesActionTable.setPropertyValues(chooseOption);
        }
    }

    private void updateDecisionPointProperties(){
        final String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        final String activityType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        final String activityStepName=currentActivityStep.getActivityStepName();
        final String newDecisionPointAttribute=decisionPointAttribute.getValue();
        final String[] newChooseOptions=propertyValuesActionTable.getPropertyValues();
        if(newDecisionPointAttribute==null||newDecisionPointAttribute.equals("")) {
            if(newChooseOptions==null||newChooseOptions.length==0){}else{
                Notification errorNotification = new Notification("Data Validation Error",
                        "Please input decision point attribute name", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
        }
        if(newChooseOptions==null||newChooseOptions.length==0) {
            if(newDecisionPointAttribute==null||newDecisionPointAttribute.equals("")) {}else{
                Notification errorNotification = new Notification("Data Validation Error",
                        "Please input at lease one choose option", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
        }

        final ActivityStepDecisionPointEditor self=this;
        Label confirmMessage = new Label(FontAwesome.INFO.getHtml() +
                " Please confirm to update the decision point properties of <b>" + currentActivityStep.getActivityStepName() + "</b>.", ContentMode.HTML);
        final ConfirmDialog updateDecisionPointPropertiesConfirmDialog = new ConfirmDialog();
        updateDecisionPointPropertiesConfirmDialog.setConfirmMessage(confirmMessage);
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                updateDecisionPointPropertiesConfirmDialog.close();
                if(self.containerDialog!=null){
                    self.containerDialog.close();
                }
                boolean setDecisionPointPropResult=ActivitySpaceOperationUtil.setActivityTypeStepDecisionPointProperties(activitySpaceName, activityType, activityStepName, newDecisionPointAttribute, newChooseOptions);
                if(setDecisionPointPropResult){
                    self.currentActivityStep.setDecisionPointAttribute(newDecisionPointAttribute);
                    self.currentActivityStep.setDecisionPointChooseOption(newChooseOptions);
                    Notification resultNotification = new Notification("Update Data Operation Success",
                            "Update exposed activity type step decision point properties success", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("Update Exposed Activity Type Step Decision Point Properties Error",
                            "Server side error occurred", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        updateDecisionPointPropertiesConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(updateDecisionPointPropertiesConfirmDialog);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }
}

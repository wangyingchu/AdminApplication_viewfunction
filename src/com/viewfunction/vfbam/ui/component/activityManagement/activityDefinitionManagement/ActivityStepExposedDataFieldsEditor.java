package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityExposedDataFieldsEditor;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityDataFieldVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityStepVO;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.ArrayList;
import java.util.List;

public class ActivityStepExposedDataFieldsEditor  extends VerticalLayout {

    private SectionActionsBar editDataFieldActionsBar;
    private ActivityExposedDataFieldsEditor activityExposedDataFieldsEditor;
    private UserClientInfo currentUserClientInfo;
    private ActivityStepVO currentActivityStep;
    private List<ActivityDataFieldVO> activityDataFieldsList;
    private ActivityStepItem relatedActivityStepItem;
    private Window containerDialog;

    public ActivityStepExposedDataFieldsEditor(UserClientInfo currentUserClientInfo,ActivityStepVO currentActivityStep,List<ActivityDataFieldVO> activityDataFieldsList) {
        this.currentUserClientInfo=currentUserClientInfo;
        this.activityDataFieldsList = activityDataFieldsList;
        setSpacing(true);
        setMargin(true);
        this.currentActivityStep=currentActivityStep;

        MainSectionTitle updateStepExposedDataFieldsSectionTitle = new MainSectionTitle("Update Activity Step Exposed Data Fields");
        addComponent(updateStepExposedDataFieldsSectionTitle);
        editDataFieldActionsBar=new SectionActionsBar(new Label("Activity Definition : <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(editDataFieldActionsBar);

        activityExposedDataFieldsEditor=new ActivityExposedDataFieldsEditor(this.currentUserClientInfo,
                ActivityExposedDataFieldsEditor.DATAFIELDS_TYPE_ACTIYITYSTEP,this.currentActivityStep.getActivityStepName(),this.currentActivityStep);
        activityExposedDataFieldsEditor.setActivityDataFieldsList(this.activityDataFieldsList);
        addComponent(activityExposedDataFieldsEditor);

        HorizontalLayout buttonsBarLayout=new HorizontalLayout();
        addComponent(buttonsBarLayout);
        this.setComponentAlignment(buttonsBarLayout, Alignment.MIDDLE_CENTER);

        Button confirmButton=new Button("Confirm Change", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updateExposedActivityDataFields();
            }
        });
        confirmButton.setIcon(FontAwesome.CHECK);
        confirmButton.addStyleName("primary");
        buttonsBarLayout.addComponent(confirmButton);

        HorizontalLayout divLayout=new HorizontalLayout();
        divLayout.setWidth("15px");
        buttonsBarLayout.addComponent(divLayout);

        Button cancelButton=new Button("Reset", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                resetExposedActivityDataFields();
            }
        });
        cancelButton.setIcon(FontAwesome.TIMES);
        buttonsBarLayout.addComponent(cancelButton);
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
            editDataFieldActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
        activityExposedDataFieldsEditor.setExposedDataFields(this.currentActivityStep.getExposedActivityDataFields());
    }

    private void resetExposedActivityDataFields(){
        activityExposedDataFieldsEditor.setExposedDataFields(this.currentActivityStep.getExposedActivityDataFields());
    }

    private void updateExposedActivityDataFields(){
        final String currentActivitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        final String activityType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        final String activityStep=this.currentActivityStep.getActivityStepName();

        final ActivityStepExposedDataFieldsEditor self=this;
        Label confirmMessage = new Label(FontAwesome.INFO.getHtml() +
                " Please confirm to update the exposed data fields of <b>" + activityType + "</b>.", ContentMode.HTML);
        final ConfirmDialog updateDataFieldConfirmDialog = new ConfirmDialog();
        updateDataFieldConfirmDialog.setConfirmMessage(confirmMessage);

        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                updateDataFieldConfirmDialog.close();
                if(self.containerDialog!=null){
                    self.containerDialog.close();
                }
                List<ActivityDataFieldVO> exposedActivityDataFieldsList=activityExposedDataFieldsEditor.getExposedDataFields();
                boolean setStepDataFieldDefinitionsResult=ActivitySpaceOperationUtil.
                        setActivityTypeExposedStepDataFieldDefinitions(currentActivitySpaceName, activityType, activityStep, exposedActivityDataFieldsList);
                if(setStepDataFieldDefinitionsResult){
                    Notification resultNotification = new Notification("Update Data Operation Success",
                            "Update activity step exposed data fields success", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                    self.currentActivityStep.setExposedActivityDataFields(exposedActivityDataFieldsList);
                    self.relatedActivityStepItem.loadExposedActivityDataFields();
                }else{
                    Notification errorNotification = new Notification("Update Activity Step Exposed Data Fields Error",
                            "Server side error occurred", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        updateDataFieldConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(updateDataFieldConfirmDialog);
    }

    public void setRelatedActivityStepItem(ActivityStepItem relatedActivityStepItem) {
        this.relatedActivityStepItem = relatedActivityStepItem;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    private boolean isExistDatafield(String fieldName){
        List<ActivityDataFieldVO> exposedDataFieldsList=this.currentActivityStep.getExposedActivityDataFields();
        if(exposedDataFieldsList==null){
            return false;
        }else{
            for(ActivityDataFieldVO currentDataField:exposedDataFieldsList){
                if(fieldName.equals(currentDataField.getDataFieldName())){
                    return true;
                }
            }
            return false;
        }
    }
}

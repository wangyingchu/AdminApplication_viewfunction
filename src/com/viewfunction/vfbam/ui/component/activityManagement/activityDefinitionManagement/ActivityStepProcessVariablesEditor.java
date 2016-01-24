package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityDataFieldVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.ActivityStepVO;
import com.viewfunction.vfbam.ui.component.activityManagement.util.RoleVO;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ActivityStepProcessVariablesEditor extends VerticalLayout {
    private SectionActionsBar updateStepVariablesActionsBar;

    private UserClientInfo currentUserClientInfo;
    private ActivityStepVO currentActivityStep;
    private List<RoleVO> rolesList;
    private Window containerDialog;
    private TwinColSelect stepActivityVariablesSelect;
    private FormLayout form;
    private HorizontalLayout footer;
    private ComboBox relatedRoles;
    private TextField stepUserIdentityAttribute;
    private Map<String,RoleVO> rolesInfoMap;
    private Map<String,ActivityDataFieldVO> activityDataFieldsInfoMap;
    private String currentStepRole;
    private String currentStepUserIdentityAttribute;
    private String[] currentStepProcessVariablesList;

    public ActivityStepProcessVariablesEditor(UserClientInfo currentUserClientInfo,ActivityStepVO currentActivityStep,
                                              List<RoleVO> rolesList) {
        this.currentUserClientInfo=currentUserClientInfo;
        setSpacing(true);
        setMargin(true);
        this.currentActivityStep=currentActivityStep;
        this.rolesList = rolesList;

        rolesInfoMap=new HashMap<String, RoleVO>();
        activityDataFieldsInfoMap=new HashMap<String,ActivityDataFieldVO>();

        MainSectionTitle updateStepProcessVariablesSectionTitle = new MainSectionTitle("Update Activity Step Process Properties");
        addComponent(updateStepProcessVariablesSectionTitle);
        updateStepVariablesActionsBar=new SectionActionsBar(new Label("Activity Definition : <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(updateStepVariablesActionsBar);

        form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        relatedRoles = new ComboBox("Activity Step Related Role");
        relatedRoles.setRequired(false);
        relatedRoles.setWidth("100%");
        relatedRoles.setTextInputAllowed(false);
        relatedRoles.setNullSelectionAllowed(true);
        relatedRoles.setInputPrompt("Please Select Related Role");
        form.addComponent(relatedRoles);

        stepUserIdentityAttribute = new TextField("User Identity Attribute Name");
        stepUserIdentityAttribute.setRequired(false);
        stepUserIdentityAttribute.setWidth("100%");
        form.addComponent(stepUserIdentityAttribute);

        stepActivityVariablesSelect = new TwinColSelect("Activity Step Process Variable List");
        stepActivityVariablesSelect.setLeftColumnCaption(" Available Process Variables");
        stepActivityVariablesSelect.setRightColumnCaption(" Selected Step Process Variables");
        stepActivityVariablesSelect.setNewItemsAllowed(false);
        stepActivityVariablesSelect.setWidth("600px");
        stepActivityVariablesSelect.setHeight("170px");
        addComponent(stepActivityVariablesSelect);
        stepActivityVariablesSelect.addStyleName("ui_appElementMiddleMargin");
        form.addComponent(stepActivityVariablesSelect);

        footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        Button confirmButton=new Button("Confirm Change", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                updateActivityStepProcessVariables();
            }
        });
        confirmButton.setIcon(FontAwesome.CHECK);
        confirmButton.addStyleName("primary");
        footer.addComponent(confirmButton);

        Button cancelButton=new Button("Reset", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                setProcessVariablesValue();
            }
        });
        cancelButton.setIcon(FontAwesome.TIMES);
        footer.addComponent(cancelButton);
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
            updateStepVariablesActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
        relatedRoles.clear();
        relatedRoles.removeAllItems();
        if(rolesList!=null){
            for(RoleVO currentRoleVO:rolesList){
                String roleCombinationStr=currentRoleVO.getRoleName()+" ("+currentRoleVO.getRoleDisplayName()+")";
                relatedRoles.addItem(roleCombinationStr);
                rolesInfoMap.put(roleCombinationStr, currentRoleVO);
            }
        }
        stepActivityVariablesSelect.clear();
        List<ActivityDataFieldVO> exposedActivityDataFieldsList=this.currentActivityStep.getExposedActivityDataFields();

        stepActivityVariablesSelect.clear();
        stepActivityVariablesSelect.removeAllItems();
        if(exposedActivityDataFieldsList!=null){
            for(ActivityDataFieldVO currentActivityDataFieldVO:exposedActivityDataFieldsList){
                String dataFieldCombinationStr=currentActivityDataFieldVO.getDataFieldName()+" ("+currentActivityDataFieldVO.getDataFieldDisplayName()+")";
                stepActivityVariablesSelect.addItem(dataFieldCombinationStr);
                activityDataFieldsInfoMap.put(dataFieldCombinationStr,currentActivityDataFieldVO);
            }
        }
        setProcessVariablesValue();
    }

    private void setProcessVariablesValue(){
        currentStepRole=currentActivityStep.getRelatedRole();
        if(currentStepRole!=null){
            String currentRelatedRoleCombinationStr=getRoleCombinationStrByName(currentStepRole);
            relatedRoles.select(currentRelatedRoleCombinationStr);
        }else{
            relatedRoles.select(null);
        }
        currentStepUserIdentityAttribute=currentActivityStep.getUserIdentityAttribute();
        if(currentStepUserIdentityAttribute!=null){
            stepUserIdentityAttribute.setValue(currentStepUserIdentityAttribute);
        }else{
            stepUserIdentityAttribute.setValue("");
        }
        currentStepProcessVariablesList=currentActivityStep.getStepProcessVariables();
        if(currentStepProcessVariablesList!=null){
            stepActivityVariablesSelect.clear();
            for(String currentValue:currentStepProcessVariablesList){
                String activityDataFieldCombinationStr=getActivityDataFieldCombinationStrByName(currentValue);
                if(activityDataFieldCombinationStr!=null){
                    stepActivityVariablesSelect.select(activityDataFieldCombinationStr);
                }
            }
        }else{
            stepActivityVariablesSelect.clear();
        }
    }

    private String getRoleCombinationStrByName(String roleName){
        if(rolesList!=null){
            for(RoleVO currentRoleVO:rolesList){
                if(currentRoleVO.getRoleName().equals(roleName)){
                    return currentRoleVO.getRoleName()+" ("+currentRoleVO.getRoleDisplayName()+")";
                }
            }
        }
        return null;
    }

    private String getActivityDataFieldCombinationStrByName(String dataFieldName){
        if(this.currentActivityStep.getExposedActivityDataFields()!=null){
            for(ActivityDataFieldVO currentActivityDataFieldVO:this.currentActivityStep.getExposedActivityDataFields()){
                if(currentActivityDataFieldVO.getDataFieldName().equals(dataFieldName)){
                    return currentActivityDataFieldVO.getDataFieldName()+" ("+currentActivityDataFieldVO.getDataFieldDisplayName()+")";
                }
            }
        }
        return null;
    }

    public void updateActivityStepProcessVariables(){
        final String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        final String activityDefinitionType=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getComponentId();
        final String activityStep=currentActivityStep.getActivityStepName();
        if(relatedRoles.getValue()!=null){
            String roleName=rolesInfoMap.get(relatedRoles.getValue().toString()).getRoleName();
            currentStepRole=roleName;
        }else{
            currentStepRole=null;
        }
        if(!stepUserIdentityAttribute.getValue().equals("")){
            currentStepUserIdentityAttribute=stepUserIdentityAttribute.getValue();
        }else{
            currentStepUserIdentityAttribute=null;
        }
        Set startActivityVariablesSet=(Set)stepActivityVariablesSelect.getValue();
        String[] strActivityDataArray = new String[startActivityVariablesSet.size()];
        String[] startActivityVariableCombinationStrArray=(String[]) startActivityVariablesSet.toArray(strActivityDataArray);
        currentStepProcessVariablesList=new String[startActivityVariableCombinationStrArray.length];
        for(int i=0;i<startActivityVariableCombinationStrArray.length;i++){
            String dataFieldName=activityDataFieldsInfoMap.get(startActivityVariableCombinationStrArray[i]).getDataFieldName();
            currentStepProcessVariablesList[i]=dataFieldName;
        }

        final ActivityStepProcessVariablesEditor self=this;
        Label confirmMessage = new Label(FontAwesome.INFO.getHtml() +
                " Please confirm to update the process variables of <b>" + currentActivityStep.getActivityStepName() + "</b>.", ContentMode.HTML);
        final ConfirmDialog updateProcessVariablesConfirmDialog = new ConfirmDialog();
        updateProcessVariablesConfirmDialog.setConfirmMessage(confirmMessage);
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                updateProcessVariablesConfirmDialog.close();
                if(self.containerDialog!=null){
                    self.containerDialog.close();
                }
                boolean setStepInfoResult=ActivitySpaceOperationUtil.
                        setActivityTypeExposedStepInfo(activitySpaceName, activityDefinitionType, activityStep,
                                currentStepRole, currentStepUserIdentityAttribute, currentStepProcessVariablesList);
                if(setStepInfoResult){
                    Notification resultNotification = new Notification("Update Data Operation Success",
                            "Update exposed activity type step process properties success", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                    self.currentActivityStep.setRelatedRole(currentStepRole);
                    self.currentActivityStep.setUserIdentityAttribute(currentStepUserIdentityAttribute);
                    self.currentActivityStep.setStepProcessVariables(currentStepProcessVariablesList);
                }else{
                    Notification errorNotification = new Notification("Update Exposed ActivityType Step Process Properties Error",
                            "Server side error occurred", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        updateProcessVariablesConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(updateProcessVariablesConfirmDialog);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }
}

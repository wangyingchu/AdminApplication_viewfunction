package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

public class ActivityDataFieldsEditor extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private  ActivityDataFieldsActionTable activityDataFieldsActionTable;

    private ActivityDataFieldEditor activityDataFieldEditor;

    private String componentType;
    private String componentId;
    public ActivityDataFieldsEditor(UserClientInfo currentUserClientInfo,String componentType,String componentId,String tableHeight){
        this.currentUserClientInfo=currentUserClientInfo;
        this.componentType=componentType;
        this.componentId=componentId;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        SectionActionsBar dataFieldsSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.TH_LIST.getHtml() + " "+userI18NProperties.
                getProperty("ActivityManagement_ActivityTypeManagement_DataFieldsDefineText"), ContentMode.HTML));
        addComponent(dataFieldsSectionActionsBar);
        SectionActionButton addNewDataFieldActionButton = new SectionActionButton();
        addNewDataFieldActionButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_Common_AddDataFieldText"));
        addNewDataFieldActionButton.setIcon(FontAwesome.PLUS_SQUARE);

        if(this.componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ACTIVITYDEFINITION)){
            activityDataFieldEditor=new ActivityDataFieldEditor(this.currentUserClientInfo,ActivityDataFieldEditor.EDITMODE_NEW,false);
        }
        if(this.componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROSTER)){
            activityDataFieldEditor=new ActivityDataFieldEditor(this.currentUserClientInfo,ActivityDataFieldEditor.EDITMODE_NEW,true);
        }
        if(this.componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE)){
            activityDataFieldEditor=new ActivityDataFieldEditor(this.currentUserClientInfo,ActivityDataFieldEditor.EDITMODE_NEW,true);
        }

        if(activityDataFieldEditor!=null){
            activityDataFieldEditor.setRelatedActivityDataFieldsEditor(this);
            activityDataFieldEditor.setComponentType(this.componentType);
            activityDataFieldEditor.setComponentID(this.componentId);
        }

        addNewDataFieldActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                final Window window = new Window();
                window.setWidth(670.0f, Unit.PIXELS);
                window.setHeight(450.0f, Unit.PIXELS);
                window.setResizable(false);
                window.center();
                window.setModal(true);
                window.setContent(activityDataFieldEditor);
                activityDataFieldEditor.setContainerDialog(window);
                UI.getCurrent().addWindow(window);
            }
        });

        dataFieldsSectionActionsBar.addActionComponent(addNewDataFieldActionButton);
        if(this.componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ACTIVITYDEFINITION)){
            setActivityDataFieldsActionTable(new ActivityDataFieldsActionTable(this.currentUserClientInfo,tableHeight,true,false));
            getActivityDataFieldsActionTable().setDataFieldQueryType(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ACTIVITYTYPE);
        }
        if(this.componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROSTER)){
            setActivityDataFieldsActionTable(new ActivityDataFieldsActionTable(this.currentUserClientInfo,tableHeight,true,true));
            getActivityDataFieldsActionTable().setDataFieldQueryType(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ROSTER);
        }
        if(this.componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE)){
            setActivityDataFieldsActionTable(new ActivityDataFieldsActionTable(this.currentUserClientInfo,tableHeight,true,true));
            getActivityDataFieldsActionTable().setDataFieldQueryType(ActivityDataFieldsActionTable.DATAFIELDS_TYPE_ROLEQUEUE);
        }
        if(getActivityDataFieldsActionTable() !=null){
            getActivityDataFieldsActionTable().setDataFieldsQueryId(this.componentId);
        }
        addComponent(getActivityDataFieldsActionTable());
    }

    public void addNewDataFieldFinishCallBack(String dataFieldName,String dataFieldDisplayName,String dataFieldType,
                                              String dataFieldDesc,boolean isArray,boolean isMandatory,boolean isSystem){
        getActivityDataFieldsActionTable().addNewDataField(dataFieldName, dataFieldDisplayName, dataFieldType, dataFieldDesc
                , isArray, isMandatory, isSystem);
    }

    public ActivityDataFieldsActionTable getActivityDataFieldsActionTable() {
        return activityDataFieldsActionTable;
    }

    private void setActivityDataFieldsActionTable(ActivityDataFieldsActionTable activityDataFieldsActionTable) {
        this.activityDataFieldsActionTable = activityDataFieldsActionTable;
    }

    public boolean dataFieldNotExistCheck(String dataFieldName){
        return  this.activityDataFieldsActionTable.dataFieldNotExistCheck(dataFieldName);
    }
}


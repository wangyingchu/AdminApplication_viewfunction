package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;

import com.vaadin.ui.*;

import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.ApplicationConstant;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class ActivityDataFieldEditor  extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    public static final String EDITMODE_UPDATE="EDITMODE_UPDATE";
    public static final String EDITMODE_NEW="EDITMODE_NEW";
    private String currentEditMode;
    private SectionActionsBar editDataFieldActionsBar;
    private String componentType;
    private String componentID;

    private FormLayout form;
    private HorizontalLayout footer;
    private TextField dataFieldNameName;
    private TextField dataFieldDisplayName;
    private ComboBox dataFieldType;
    private TextArea dataFieldDescription;
    private CheckBox arrayFieldCheck;
    private CheckBox mandatoryFieldCheck;
    private CheckBox systemFieldCheck;

    private Button updateButton;
    private Button resetButton;
    private Button addButton;

    private Window containerDialog;

    private ActivityDataFieldsEditor relatedActivityDataFieldsEditor;
    private ActivityDataFieldsActionTable relatedActivityDataFieldsActionTable;

    private String currentDataFieldName;
    private String currentDataFieldDisplayName;
    private String currentDataFieldType;
    private String currentDataFieldIsArray;
    private String currentDataFieldIsMandatory;
    private String currentDataFieldIsSystem;
    private String currentDataFieldDesc;

    private boolean isFilterMode;

    public ActivityDataFieldEditor(UserClientInfo currentUserClientInfo,String editMode,boolean isFilterMode){
        this.currentUserClientInfo=currentUserClientInfo;
        setSpacing(true);
        setMargin(true);
        this.currentEditMode=editMode;
        this.isFilterMode=isFilterMode;
        if(EDITMODE_NEW.equals(this.currentEditMode)){
            MainSectionTitle addNewParticipantSectionTitle=new MainSectionTitle("Add New Data Field");
            addComponent(addNewParticipantSectionTitle);
        }else if(EDITMODE_UPDATE.equals(this.currentEditMode)){
            MainSectionTitle addNewParticipantSectionTitle=new MainSectionTitle("Update Exist Data Field");
            addComponent(addNewParticipantSectionTitle);
        }

        editDataFieldActionsBar=new SectionActionsBar(new Label("Activity Space : <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(editDataFieldActionsBar);

        form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        dataFieldNameName = new TextField("Data Field Name");
        dataFieldNameName.setRequired(true);
        dataFieldNameName.setWidth("100%");
        form.addComponent(dataFieldNameName);

        dataFieldDisplayName = new TextField("Data Field Display Name");
        dataFieldDisplayName.setWidth("100%");
        dataFieldDisplayName.setRequired(true);
        form.addComponent(dataFieldDisplayName);

        dataFieldType = new ComboBox("Data Field Type");
        dataFieldType.setRequired(true);
        dataFieldType.setWidth("100%");
        dataFieldType.setTextInputAllowed(false);
        dataFieldType.setNullSelectionAllowed(false);
        dataFieldType.setInputPrompt("Please Select Data Field Type");
        dataFieldType.addItem(ApplicationConstant.DataFieldType_STRING);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_BINARY);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_BOOLEAN);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_DATE);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_DECIMAL);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_DOUBLE);
        dataFieldType.addItem(ApplicationConstant.DataFieldType_LONG);
        form.addComponent(dataFieldType);

        dataFieldDescription = new TextArea("Data Field Description");
        dataFieldDescription.setRequired(true);
        dataFieldDescription.setWidth("100%");
        dataFieldDescription.setRows(2);
        form.addComponent(dataFieldDescription);

        HorizontalLayout checkboxRow = new HorizontalLayout();
        checkboxRow.setMargin(true);
        checkboxRow.setSpacing(true);
        form.addComponent(checkboxRow);

        arrayFieldCheck = new CheckBox("Array Field", false);
        checkboxRow.addComponent(arrayFieldCheck);

        mandatoryFieldCheck = new CheckBox("Mandatory Field", false);
        checkboxRow.addComponent(mandatoryFieldCheck);

        systemFieldCheck = new CheckBox("System Field", false);
        checkboxRow.addComponent(systemFieldCheck);
        form.setReadOnly(true);

        footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        updateButton = new Button("Update", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do update current data field logic */
                updateCurrentDataField();
            }
        });
        updateButton.setIcon(FontAwesome.SAVE);
        updateButton.addStyleName("primary");

        resetButton = new Button("Reset Change", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                resetFieldData();
            }
        });
        resetButton.setIcon(FontAwesome.TIMES);

        addButton=new Button("Add Data Field", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new data field logic */
                addNewDataField();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");

        if(this.isFilterMode){
            mandatoryFieldCheck.setVisible(false);
            systemFieldCheck.setVisible(false);
        }
    }

    @Override
    public void attach() {
        super.attach();
        ActivitySpaceManagementMeteInfo currentActivitySpaceComponentInfo=
                this.currentUserClientInfo.getActivitySpaceManagementMeteInfo();
        if(getComponentType()==null){
            return;
        }else{
            String activitySpaceName="";
            Label sectionActionBarLabel=null;
            if(currentActivitySpaceComponentInfo!=null){
                activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            }
            if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE)){
                sectionActionBarLabel=new Label("Role Queue : <b>"+getComponentID()+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            }
            if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROSTER)){
                sectionActionBarLabel=new Label("Roster : <b>"+getComponentID()+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            }
            if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ACTIVITYDEFINITION)){
                sectionActionBarLabel=new Label("Activity Type : <b>"+getComponentID()+"</b> &nbsp;&nbsp;["+ FontAwesome.TERMINAL.getHtml()+" "+activitySpaceName+"]" , ContentMode.HTML);
            }
            editDataFieldActionsBar.resetSectionActionsBarContent(sectionActionBarLabel);
        }
        footer.removeAllComponents();

        if(this.currentEditMode.equals(EDITMODE_NEW)){
            //For add new data field
            footer.addComponent(addButton);
            dataFieldNameName.setValue("");
            dataFieldDisplayName.setValue("");
            dataFieldType.select(null);
            dataFieldDescription.setValue("");
            arrayFieldCheck.clear();
            mandatoryFieldCheck.clear();
            systemFieldCheck.clear();
        }else{
            //For edit existing data field
            footer.addComponent(updateButton);
            footer.addComponent(resetButton);
            dataFieldNameName.setReadOnly(false);
            if(currentDataFieldName!=null){
                dataFieldNameName.setValue(currentDataFieldName);
            }
            dataFieldNameName.setReadOnly(true);
            resetFieldData();
        }
    }

    private boolean updateCurrentDataField(){
        final String dataFieldNameStr=dataFieldNameName.getValue();
        final String dataFieldDisplayNameStr=dataFieldDisplayName.getValue();
        final Object dataFieldTypeObj=dataFieldType.getValue();
        final String dataFieldDescStr=dataFieldDescription.getValue();
        final boolean isArray=arrayFieldCheck.getValue();
        final boolean isMandatory=mandatoryFieldCheck.getValue();
        final boolean isSystem=systemFieldCheck.getValue();
        if(dataFieldNameStr.equals("")||dataFieldDisplayNameStr.equals("")||
                dataFieldTypeObj==null||dataFieldDescStr.equals("")){
            Notification errorNotification = new Notification("Data Validation Error",
                    "Please input all required fields", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return false;
        }else{
            Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+
                    " Please confirm to update data field <b>"+dataFieldNameStr +"</b>'s information.", ContentMode.HTML);
            final ConfirmDialog updateDataFieldConfirmDialog = new ConfirmDialog();
            updateDataFieldConfirmDialog.setConfirmMessage(confirmMessage);
            final ActivityDataFieldEditor self=this;
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    Notification resultNotification = new Notification("Update Data Operation Success",
                            "Update data field success", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                    //close confirm dialog
                    updateDataFieldConfirmDialog.close();
                    if(self.containerDialog!=null){
                        self.containerDialog.close();
                    }
                    //execute callback logic
                    if(self.relatedActivityDataFieldsActionTable!=null){
                        self.relatedActivityDataFieldsActionTable.updateExistDataField(dataFieldNameStr, dataFieldDisplayNameStr,
                                         dataFieldTypeObj.toString(),dataFieldDescStr,isArray,isMandatory,isSystem);
                    }
                }
            };
            updateDataFieldConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(updateDataFieldConfirmDialog);
        }
        return true;
    }

    private void resetFieldData(){
        if(currentDataFieldName!=null){
            dataFieldNameName.setValue(currentDataFieldName);
        }
        dataFieldNameName.setReadOnly(true);
        if(currentDataFieldDisplayName!=null){
            dataFieldDisplayName.setValue(currentDataFieldDisplayName);
        }
        if(currentDataFieldDesc!=null){
            dataFieldDescription.setValue(currentDataFieldDesc);
        }
        if(currentDataFieldType!=null){
            dataFieldType.select(currentDataFieldType);
        }
        if(currentDataFieldIsArray!=null){
            if(currentDataFieldIsArray.equals("true")){
                arrayFieldCheck.setValue(true);
            }else{
                arrayFieldCheck.setValue(false);
            }
        }
        if(currentDataFieldIsMandatory!=null){
            if(currentDataFieldIsMandatory.equals("true")){
                mandatoryFieldCheck.setValue(true);
            }else{
                mandatoryFieldCheck.setValue(false);
            }
        }
        if(currentDataFieldIsSystem!=null){
            if(currentDataFieldIsSystem.equals("true")){
                systemFieldCheck.setValue(true);
            }else{
                systemFieldCheck.setValue(false);
            }
        }
    }

    private boolean addNewDataField(){
        final String dataFieldNameStr=dataFieldNameName.getValue();
        final String dataFieldDisplayNameStr=dataFieldDisplayName.getValue();
        final Object dataFieldTypeObj=dataFieldType.getValue();
        final String dataFieldDescStr=dataFieldDescription.getValue();
        final boolean isArray=arrayFieldCheck.getValue();
        final boolean isMandatory=mandatoryFieldCheck.getValue();
        final boolean isSystem=systemFieldCheck.getValue();
        if(dataFieldNameStr.equals("")||dataFieldDisplayNameStr.equals("")||
                dataFieldTypeObj==null||dataFieldDescStr.equals("")){
            Notification errorNotification = new Notification("Data Validation Error",
                    "Please input all required fields", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return false;
        }else{
            boolean dataFieldNotExistCheck=this.relatedActivityDataFieldsEditor.dataFieldNotExistCheck(dataFieldNameStr);
            if(!dataFieldNotExistCheck){
                Notification errorNotification = new Notification("Data Validation Error",
                        "Data field already exist", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return false;

            }
            //do add new logic
            Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+
                    " Please confirm to add new data field <b>"+dataFieldNameStr +"</b>.", ContentMode.HTML);
            final ConfirmDialog addDataFieldConfirmDialog = new ConfirmDialog();
            addDataFieldConfirmDialog.setConfirmMessage(confirmMessage);
            final ActivityDataFieldEditor self=this;
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    Notification resultNotification = new Notification("Add Data Operation Success",
                            "Add new data field success", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                    //close confirm dialog
                    addDataFieldConfirmDialog.close();
                    if(self.containerDialog!=null){
                        self.containerDialog.close();
                    }
                    //execute callback logic
                    if(self.relatedActivityDataFieldsEditor!=null){
                        self.relatedActivityDataFieldsEditor.addNewDataFieldFinishCallBack(dataFieldNameStr,dataFieldDisplayNameStr,
                                dataFieldTypeObj.toString(),dataFieldDescStr,isArray,isMandatory,isSystem);
                    }
                }
            };
            addDataFieldConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(addDataFieldConfirmDialog);
        }
        return true;
    }

    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getComponentID() {
        return componentID;
    }

    public void setComponentID(String componentID) {
        this.componentID = componentID;
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    public void setRelatedActivityDataFieldsEditor(ActivityDataFieldsEditor relatedActivityDataFieldsEditor) {
        this.relatedActivityDataFieldsEditor = relatedActivityDataFieldsEditor;
    }

    public void setRelatedActivityDataFieldsActionTable(ActivityDataFieldsActionTable relatedActivityDataFieldsActionTable) {
        this.relatedActivityDataFieldsActionTable = relatedActivityDataFieldsActionTable;
    }

    public void setCurrentDataFieldName(String currentDataFieldName) {
        this.currentDataFieldName = currentDataFieldName;
    }

    public void setCurrentDataFieldDisplayName(String currentDataFieldDisplayName) {
        this.currentDataFieldDisplayName = currentDataFieldDisplayName;
    }

    public void setCurrentDataFieldType(String currentDataFieldType) {
        this.currentDataFieldType = currentDataFieldType;
    }

    public void setCurrentDataFieldIsArray(String currentDataFieldIsArray) {
        this.currentDataFieldIsArray = currentDataFieldIsArray;
    }

    public void setCurrentDataFieldIsMandatory(String currentDataFieldIsMandatory) {
        this.currentDataFieldIsMandatory = currentDataFieldIsMandatory;
    }

    public void setCurrentDataFieldIsSystem(String currentDataFieldIsSystem) {
        this.currentDataFieldIsSystem = currentDataFieldIsSystem;
    }

    public void setCurrentDataFieldDesc(String currentDataFieldDesc) {
        this.currentDataFieldDesc = currentDataFieldDesc;
    }
}

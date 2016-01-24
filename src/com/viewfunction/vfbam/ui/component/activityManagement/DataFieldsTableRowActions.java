package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class DataFieldsTableRowActions  extends HorizontalLayout {
    private UserClientInfo currentUserClientInfo;
    private ActivityDataFieldsActionTable containerActivityDataFieldsActionTable;
    private ActivityExposedDataFieldsActionTable containerActivityExposedDataFieldsActionTable;
    private String componentId;
    private Button editDataFieldsButton;
    private Button deleteDataFieldButton;
    public DataFieldsTableRowActions(UserClientInfo currentUserClientInfo,String componentId){
        this.currentUserClientInfo=currentUserClientInfo;
        this.componentId=componentId;
        editDataFieldsButton = new Button();
        editDataFieldsButton.setIcon(FontAwesome.EDIT);
        editDataFieldsButton.setDescription("Edit Data Field");
        editDataFieldsButton.addStyleName("small");
        editDataFieldsButton.addStyleName("borderless");
        addComponent(editDataFieldsButton);
        final String dataFieldName=this.componentId;
        editDataFieldsButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                if(containerActivityDataFieldsActionTable!=null){
                    containerActivityDataFieldsActionTable.updateDataField(dataFieldName);
                }
                if(containerActivityExposedDataFieldsActionTable!=null){
                    containerActivityExposedDataFieldsActionTable.updateDataField(dataFieldName);
                }
            }
        });
        deleteDataFieldButton = new Button();
        deleteDataFieldButton.setIcon(FontAwesome.TRASH_O);
        deleteDataFieldButton.setDescription("Delete Data Field");
        deleteDataFieldButton.addStyleName("small");
        deleteDataFieldButton.addStyleName("borderless");
        addComponent(deleteDataFieldButton);
        deleteDataFieldButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                if(containerActivityDataFieldsActionTable!=null){
                    containerActivityDataFieldsActionTable.deleteExistDataField(dataFieldName);
                }
                if(containerActivityExposedDataFieldsActionTable!=null){
                    containerActivityExposedDataFieldsActionTable.deleteExistDataField(dataFieldName);
                }
            }
        });
    }

    public void setContainerActivityDataFieldsActionTable(ActivityDataFieldsActionTable containerActivityDataFieldsActionTable) {
        this.containerActivityDataFieldsActionTable = containerActivityDataFieldsActionTable;
    }

    public void enableEditActions(boolean allowEditOperation){
        editDataFieldsButton.setEnabled(allowEditOperation);
        deleteDataFieldButton.setEnabled(allowEditOperation);
    }

    public void setContainerActivityExposedDataFieldsActionTable(ActivityExposedDataFieldsActionTable containerActivityExposedDataFieldsActionTable) {
        this.containerActivityExposedDataFieldsActionTable = containerActivityExposedDataFieldsActionTable;
    }
}

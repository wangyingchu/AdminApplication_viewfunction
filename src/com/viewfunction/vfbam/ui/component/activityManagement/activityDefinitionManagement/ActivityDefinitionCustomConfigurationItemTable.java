package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.ui.Table;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.List;
import java.util.Properties;

/**
 * Created by wangychu on 1/24/17.
 */
public class ActivityDefinitionCustomConfigurationItemTable extends Table {
    private UserClientInfo currentUserClientInfo;
    private String columnName_CustomConfigurationName ="columnName_CustomConfigurationName";
    private String columnName_CustomConfigurationOperations ="columnName_CustomConfigurationOperations";
    private IndexedContainer containerDataSource;
    private String configurationItemType;
    private boolean canDeleteItem;
    public ActivityDefinitionCustomConfigurationItemTable(UserClientInfo currentUserClientInfo,String tableHeight,String configurationItemDisplayName,boolean canDeleteItem){
        this.currentUserClientInfo=currentUserClientInfo;
        this.canDeleteItem=canDeleteItem;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();
        setWidth("100%");
        if(tableHeight!=null){
            setHeight(tableHeight);
        }else{
            setHeight("100%");
        }
        setPageLength(10);
        setColumnReorderingAllowed(false);
        setSelectable(true);
        setMultiSelect(false);
        setSortEnabled(true);
        addStyleName("no-vertical-lines");
        addStyleName("no-horizontal-lines");
        addStyleName("borderless");

        this.containerDataSource = new IndexedContainer();
        this.containerDataSource.addContainerProperty(columnName_CustomConfigurationName, String.class, null);
        this.containerDataSource.addContainerProperty(columnName_CustomConfigurationOperations, ActivityDefinitionCustomConfigurationItemTableRowActions.class, null);
        setRowHeaderMode(Table.RowHeaderMode.INDEX);
        setContainerDataSource(this.containerDataSource);
        setColumnAlignment(columnName_CustomConfigurationName, Table.Align.LEFT);
        setColumnAlignment(columnName_CustomConfigurationOperations, Table.Align.CENTER);
        setColumnWidth(columnName_CustomConfigurationOperations, 160);
        if(configurationItemDisplayName!=null){
            setColumnHeaders(new String[]{configurationItemDisplayName,
                    userI18NProperties.
                            getProperty("ActivityManagement_Table_ListActionPropertyText")});
        }else{
            setColumnHeaders(new String[]{userI18NProperties.
                    getProperty("ActivityManagement_ActivityTypeManagement_ConfigItemNameText"),
                    userI18NProperties.
                            getProperty("ActivityManagement_Table_ListActionPropertyText")});
        }

    }

    @Override
    public void attach() {
        super.attach();
    }

    public void loadConfigurationItemsData(List<String> configurationItems){
        this.clear();
        this.containerDataSource.removeAllItems();
        for(String currentConfigurationItem:configurationItems){
            Item item = this.containerDataSource.addItem(currentConfigurationItem);
            item.getItemProperty(columnName_CustomConfigurationName).setValue(currentConfigurationItem);
            ActivityDefinitionCustomConfigurationItemTableRowActions currentRowActions=
                    new ActivityDefinitionCustomConfigurationItemTableRowActions(this.currentUserClientInfo,this.canDeleteItem);
            currentRowActions.setConfigurationItemType(getConfigurationItemType());
            currentRowActions.setConfigurationItemName(currentConfigurationItem);
            item.getItemProperty(columnName_CustomConfigurationOperations).setValue(currentRowActions);
        }
    }

    public String getConfigurationItemType() {
        return configurationItemType;
    }

    public void setConfigurationItemType(String configurationItemType) {
        this.configurationItemType = configurationItemType;
    }
}

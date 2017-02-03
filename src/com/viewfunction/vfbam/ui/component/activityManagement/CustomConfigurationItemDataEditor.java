package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import com.vaadin.ui.themes.ValoTheme;
import com.viewfunction.activityEngine.activityView.common.CustomStructure;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

/**
 * Created by wangychu on 2/1/17.
 */
public class CustomConfigurationItemDataEditor extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;
    private VerticalLayout dataEditorContainerLayout;

    public CustomConfigurationItemDataEditor(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        this.setMargin(true);
        this.setMargin(new MarginInfo(false,true,true,true));
        Properties userI18NProperties = this.currentUserClientInfo.getUserI18NProperties();

        Label sectionLabel=new Label(userI18NProperties.
                getProperty("ActivityManagement_Common_ConfigurationItemDataText"));
        //sectionLabel.addStyleName("h4");
        sectionLabel.addStyleName(ValoTheme.LABEL_BOLD);
        sectionLabel.addStyleName("colored");
        sectionLabel.addStyleName("ui_appSectionDiv");
        sectionLabel.addStyleName("ui_appFadeMargin");
        addComponent(sectionLabel);

        dataEditorContainerLayout=new VerticalLayout();
        SectionActionsBar itemDataPropertiesSectionActionsBar=new SectionActionsBar(new Label( FontAwesome.LIST.getHtml() + " "+
                userI18NProperties.
                        getProperty("ActivityManagement_Common_ConfigurationItemPropertiesText")+":", ContentMode.HTML));
        dataEditorContainerLayout.addComponent(itemDataPropertiesSectionActionsBar);
        SectionActionButton addNewPropertiesButton = new SectionActionButton();
        addNewPropertiesButton.setCaption(userI18NProperties.
                getProperty("ActivityManagement_Common_AddConfigurationItemPropertyButtonLabel"));
        addNewPropertiesButton.setIcon(FontAwesome.PLUS_SQUARE);
        itemDataPropertiesSectionActionsBar.addActionComponent(addNewPropertiesButton);

        CustomAttributeItemsList customAttributeItemsList=new CustomAttributeItemsList(this.currentUserClientInfo);
        dataEditorContainerLayout.addComponent(customAttributeItemsList);
    }

    public void renderConfigurationItemData(CustomStructure targetCustomStructure){
        this.addComponent(dataEditorContainerLayout);






    }

    public void clearConfigurationItemData(){
        this.removeComponent(dataEditorContainerLayout);
    }
}

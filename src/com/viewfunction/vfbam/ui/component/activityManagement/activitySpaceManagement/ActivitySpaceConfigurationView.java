package com.viewfunction.vfbam.ui.component.activityManagement.activitySpaceManagement;

import com.vaadin.server.FontAwesome;

import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class ActivitySpaceConfigurationView extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;

    private String activitySpaceName;
    public ActivitySpaceConfigurationView(UserClientInfo currentUserClientInfo,String activitySpaceName){
        this.currentUserClientInfo=currentUserClientInfo;
        this.activitySpaceName=activitySpaceName;
        setSpacing(true);
        setMargin(true);
        setWidth("100%");
        setHeight("430px");

        TabSheet tabs=new TabSheet();
        addComponent(tabs);
        ActivitySpaceBusinessCategoryEditor activitySpaceBusinessCategoryEditor=new ActivitySpaceBusinessCategoryEditor(this.currentUserClientInfo,this.activitySpaceName);
        TabSheet.Tab businessCategoriesInfoLayoutTab =tabs.addTab(activitySpaceBusinessCategoryEditor, "Activity Business Categories");
        businessCategoriesInfoLayoutTab.setIcon(FontAwesome.CHECK_CIRCLE_O);

        ActivitySpaceExtendFeatureCategoryEditor activitySpaceExtendFeatureCategoryEditor=new ActivitySpaceExtendFeatureCategoryEditor(this.currentUserClientInfo,this.activitySpaceName);
        TabSheet.Tab extendFeatureCategoriesInfoLayoutTab =tabs.addTab(activitySpaceExtendFeatureCategoryEditor, "Extend Feature Categories");
        extendFeatureCategoriesInfoLayoutTab.setIcon(FontAwesome.LAPTOP);

        ActivitySpaceBusinessCategoryEditor knowledgeBaseIntegrationEditor=new ActivitySpaceBusinessCategoryEditor(this.currentUserClientInfo,this.activitySpaceName);
        TabSheet.Tab knowledgeBaseEntegationInfoLayoutTab =tabs.addTab(knowledgeBaseIntegrationEditor, "KnowledgeBase Integration");
        knowledgeBaseEntegationInfoLayoutTab.setIcon(FontAwesome.BOOK);
    }
}

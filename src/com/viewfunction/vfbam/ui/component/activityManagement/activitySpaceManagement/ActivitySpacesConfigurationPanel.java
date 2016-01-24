package com.viewfunction.vfbam.ui.component.activityManagement.activitySpaceManagement;

import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class ActivitySpacesConfigurationPanel extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;

    public ActivitySpacesConfigurationPanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        setSpacing(true);
        setMargin(true);
        setWidth("100%");
        setHeight("100%");
        setSizeFull();
        MainSectionTitle addNewActivitySpaceSectionTitle=new MainSectionTitle("Activity Spaces Configuration");
        addComponent(addNewActivitySpaceSectionTitle);

        TabSheet activitySpacesConfigurationTabSheet = new TabSheet();
        activitySpacesConfigurationTabSheet.addStyleName("framed padded-tabbar");
        addComponent(activitySpacesConfigurationTabSheet);

        String[] activitySpaceNamesArray= ActivitySpaceOperationUtil.listActivitySpaces();
        for(String currentActivitySpaceName:activitySpaceNamesArray){
            ActivitySpaceConfigurationView currentConfigurationView=new ActivitySpaceConfigurationView(this.currentUserClientInfo,currentActivitySpaceName);
            TabSheet.Tab configurationTab = activitySpacesConfigurationTabSheet.addTab(currentConfigurationView,currentActivitySpaceName);
        }

        setExpandRatio(activitySpacesConfigurationTabSheet, 1.0F);
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }
}

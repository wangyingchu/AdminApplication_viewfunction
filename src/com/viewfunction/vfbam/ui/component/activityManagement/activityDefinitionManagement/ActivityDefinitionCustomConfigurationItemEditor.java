package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.util.Properties;

/**
 * Created by wangychu on 1/25/17.
 */
public class ActivityDefinitionCustomConfigurationItemEditor extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;

    public ActivityDefinitionCustomConfigurationItemEditor(UserClientInfo currentUserClientInfo){
        setSpacing(true);
        setMargin(true);
        this.currentUserClientInfo=currentUserClientInfo;
        Properties userI18NProperties=this.currentUserClientInfo.getUserI18NProperties();

        MainSectionTitle addNewActivityTypeSectionTitle=new MainSectionTitle("Activity Definition Configuration Item");
        addComponent(addNewActivityTypeSectionTitle);


    }
}

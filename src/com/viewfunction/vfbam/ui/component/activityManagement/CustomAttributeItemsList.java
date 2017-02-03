package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.ui.VerticalLayout;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

/**
 * Created by wangychu on 2/1/17.
 */
public class CustomAttributeItemsList extends VerticalLayout {

    private UserClientInfo currentUserClientInfo;

    public CustomAttributeItemsList(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
    }
}

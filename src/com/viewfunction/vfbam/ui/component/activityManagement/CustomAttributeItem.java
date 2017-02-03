package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.ui.HorizontalLayout;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

/**
 * Created by wangychu on 2/1/17.
 */
public class CustomAttributeItem extends HorizontalLayout{

    private UserClientInfo currentUserClientInfo;

    public CustomAttributeItem(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
    }
}

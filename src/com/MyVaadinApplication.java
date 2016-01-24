package com;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

/**
 * Created by viewfunction on 2015/7/7.
 */
public class MyVaadinApplication extends UI {
    @Override
    public void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout();
        setContent(layout);
        layout.addComponent(new Label("Hello, world2!"));
    }
}

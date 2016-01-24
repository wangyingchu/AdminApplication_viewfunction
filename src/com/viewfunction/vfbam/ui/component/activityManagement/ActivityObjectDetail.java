package com.viewfunction.vfbam.ui.component.activityManagement;

import com.vaadin.navigator.Navigator;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement.ActivityDefinitionManagementView;
import com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement.ActivityDefinitionsListManagementView;
import com.viewfunction.vfbam.ui.component.activityManagement.participantManagement.ParticipantManagementView;
import com.viewfunction.vfbam.ui.component.activityManagement.participantManagement.ParticipantsListManagementView;
import com.viewfunction.vfbam.ui.component.activityManagement.roleManagement.RoleManagementView;
import com.viewfunction.vfbam.ui.component.activityManagement.roleManagement.RolesListManagementView;
import com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement.RoleQueueManagementView;
import com.viewfunction.vfbam.ui.component.activityManagement.roleQueueManagement.RoleQueuesListManagementView;
import com.viewfunction.vfbam.ui.component.activityManagement.rosterManagement.RosterManagementView;
import com.viewfunction.vfbam.ui.component.activityManagement.rosterManagement.RostersListManagementView;
import com.viewfunction.vfbam.ui.component.common.ElementStatusBar;
import com.viewfunction.vfbam.ui.util.ActivitySpaceManagementMeteInfo;
import com.viewfunction.vfbam.ui.util.UserClientInfo;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivitySpaceComponentSelectedEvent.ActivitySpaceComponentSelectedListener;

public class ActivityObjectDetail extends VerticalLayout implements ActivitySpaceComponentSelectedListener{
    private UserClientInfo currentUserClientInfo;
    private ElementStatusBar elementStatusBar;
    private Navigator contentNavigator;
    private static final String NAV_GENERAL="general_am";
    private static final String NAV_PARTICIPANTLIST="participants";
    private static final String NAV_PARTICIPANT="participant";
    private static final String NAV_ROLELIST="roles";
    private static final String NAV_ROLE="role";
    private static final String NAV_ACTIVITYDEFINITIONLIST="activityDefinitions";
    private static final String NAV_ACTIVITYDEFINITION="activityDefinition";
    private static final String NAV_ROSTERLIST="rosters";
    private static final String NAV_ROSTER="roster";
    private static final String NAV_ROLEQUEUELIST="roleQueues";
    private static final String NAV_ROLEQUEUE="roleQueue";

    public ActivityObjectDetail(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo = currentUserClientInfo;
        this.currentUserClientInfo.getEventBlackBoard().addListener(this);
        elementStatusBar=new ElementStatusBar();
        this.addComponent(elementStatusBar);

        HorizontalLayout contentContainer = new HorizontalLayout();
        contentContainer.setMargin(false);
        contentContainer.setSpacing(false);
        contentContainer.setSizeFull();
        this.addComponent(contentContainer);

        ComponentContainer componentContainer=(ComponentContainer)contentContainer;
        contentNavigator = new Navigator(UI.getCurrent(),componentContainer);
        /* Config Components View */
        GeneralInfoView generalInfoView=new GeneralInfoView(currentUserClientInfo);
        contentNavigator.addView(NAV_GENERAL, generalInfoView);
        ParticipantsListManagementView participantsListManagementView=new ParticipantsListManagementView(currentUserClientInfo);
        contentNavigator.addView(NAV_PARTICIPANTLIST, participantsListManagementView);
        ParticipantManagementView participantManagementView=new ParticipantManagementView(currentUserClientInfo);
        contentNavigator.addView(NAV_PARTICIPANT, participantManagementView);
        RolesListManagementView rolesListManagementView=new RolesListManagementView(currentUserClientInfo);
        contentNavigator.addView(NAV_ROLELIST, rolesListManagementView);
        RoleManagementView roleManagementView=new RoleManagementView(currentUserClientInfo);
        contentNavigator.addView(NAV_ROLE, roleManagementView);
        ActivityDefinitionsListManagementView activityDefinitionsListManagementView=new ActivityDefinitionsListManagementView(currentUserClientInfo);
        contentNavigator.addView(NAV_ACTIVITYDEFINITIONLIST, activityDefinitionsListManagementView);
        ActivityDefinitionManagementView activityDefinitionManagementView=new ActivityDefinitionManagementView(currentUserClientInfo);
        contentNavigator.addView(NAV_ACTIVITYDEFINITION, activityDefinitionManagementView);
        RostersListManagementView rostersListManagementView=new RostersListManagementView(currentUserClientInfo);
        contentNavigator.addView(NAV_ROSTERLIST, rostersListManagementView);
        RosterManagementView rosterManagementView=new RosterManagementView(currentUserClientInfo);
        contentNavigator.addView(NAV_ROSTER, rosterManagementView);
        RoleQueuesListManagementView roleQueuesListManagementView=new RoleQueuesListManagementView(currentUserClientInfo);
        contentNavigator.addView(NAV_ROLEQUEUELIST, roleQueuesListManagementView);
        RoleQueueManagementView roleQueueManagementView=new RoleQueueManagementView(currentUserClientInfo);
        contentNavigator.addView(NAV_ROLEQUEUE, roleQueueManagementView);

        contentNavigator.navigateTo(NAV_GENERAL);
    }

    @Override
    public void receivedActivitySpaceComponentSelectedEvent(ActivitySpaceComponentSelectedEvent event) {
        String componentType=event.getComponentType();
        String componentId=event.getComponentId();
        String activitySpaceName=event.getActivitySpaceName();

        if(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo()==null){
            this.currentUserClientInfo.setActivitySpaceManagementMeteInfo(new ActivitySpaceManagementMeteInfo());
        }
        this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().setActivitySpaceName(activitySpaceName);
        this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().setComponentType(componentType);
        this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().setComponentId(componentId);

        this.elementStatusBar.setActivitySpaceName(activitySpaceName);
        this.elementStatusBar.clearStatusElements();

        if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_PARTICIPANT)){
            if(componentId==null){
                contentNavigator.navigateTo(NAV_PARTICIPANTLIST);
                this.elementStatusBar.addStatusElement(generateStatusElementLabel("Participants",false));
                Notification.show("PARTICIPANTS INFO" + "-" + "Participants List", activitySpaceName, Notification.Type.TRAY_NOTIFICATION);
            }else{
                this.elementStatusBar.addStatusElement(generateStatusNavigateButton("Participants", activitySpaceName,componentType));
                this.elementStatusBar.addStatusElement(generateStatusElementLabel(componentId,true));
                contentNavigator.navigateTo(NAV_PARTICIPANT);
                Notification.show("PARTICIPANT INFO"+"-"+componentId,activitySpaceName,Notification.Type.TRAY_NOTIFICATION);
            }
        }else if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROLE)){
            if(componentId==null){
                this.elementStatusBar.addStatusElement(generateStatusElementLabel("Roles",false));
                contentNavigator.navigateTo(NAV_ROLELIST);
                Notification.show("ROLES INFO"+"-"+"Roles List",activitySpaceName,Notification.Type.TRAY_NOTIFICATION);
            }else{
                this.elementStatusBar.addStatusElement(generateStatusNavigateButton("Roles", activitySpaceName,componentType));
                this.elementStatusBar.addStatusElement(generateStatusElementLabel(componentId,true));
                contentNavigator.navigateTo(NAV_ROLE);
                Notification.show("ROLE INFO"+"-"+componentId,activitySpaceName,Notification.Type.TRAY_NOTIFICATION);
            }
        }else if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ACTIVITYDEFINITION)){
            if(componentId==null){
                this.elementStatusBar.addStatusElement(generateStatusElementLabel("Activity Definitions",false));
                contentNavigator.navigateTo(NAV_ACTIVITYDEFINITIONLIST);
                Notification.show("ACTIVITY DEFINITIONS INFO"+"-"+"Activity Definitions List",activitySpaceName,Notification.Type.TRAY_NOTIFICATION);
            }else{
                this.elementStatusBar.addStatusElement(generateStatusNavigateButton("Activity Definitions", activitySpaceName,componentType));
                this.elementStatusBar.addStatusElement(generateStatusElementLabel(componentId,true));
                contentNavigator.navigateTo(NAV_ACTIVITYDEFINITION);
                Notification.show("ACTIVITY DEFINITION INFO"+"-"+componentId,activitySpaceName,Notification.Type.TRAY_NOTIFICATION);
            }
        }else if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROSTER)){
            if(componentId==null){
                this.elementStatusBar.addStatusElement(generateStatusElementLabel("Rosters",false));
                contentNavigator.navigateTo(NAV_ROSTERLIST);
                Notification.show("ROSTERS INFO"+"-"+"Rosters List",activitySpaceName,Notification.Type.TRAY_NOTIFICATION);
            }else{
                this.elementStatusBar.addStatusElement(generateStatusNavigateButton("Rosters", activitySpaceName,componentType));
                this.elementStatusBar.addStatusElement(generateStatusElementLabel(componentId,true));
                contentNavigator.navigateTo(NAV_ROSTER);
                Notification.show("ROSTER INFO"+"-"+componentId,activitySpaceName,Notification.Type.TRAY_NOTIFICATION);
            }
        }else if(componentType.equals(ActivityManagementConst.COMPONENT_TYPE_ROLEQUEUE)){
            if(componentId==null){
                this.elementStatusBar.addStatusElement(generateStatusElementLabel("Role Queues",false));
                contentNavigator.navigateTo(NAV_ROLEQUEUELIST);
                Notification.show("ROLE QUEUES INFO"+"-"+"Role Queues List",activitySpaceName,Notification.Type.TRAY_NOTIFICATION);
            }else{
                this.elementStatusBar.addStatusElement(generateStatusNavigateButton("Role Queues", activitySpaceName,componentType));
                this.elementStatusBar.addStatusElement(generateStatusElementLabel(componentId,true));
                contentNavigator.navigateTo(NAV_ROLEQUEUE);
                Notification.show("ROLE QUEUE INFO"+"-"+componentId,activitySpaceName,Notification.Type.TRAY_NOTIFICATION);
            }
        }else{
            //do nothing for not exist element type
            Notification.show("Received not registered Component Type:",componentType,Notification.Type.TRAY_NOTIFICATION);
        }
    }

    private Label generateStatusElementLabel(String elementName,boolean showDivChar){
        Label elementLabel=null;
        if(showDivChar){
            elementLabel=new Label(FontAwesome.ANGLE_RIGHT.getHtml()+"&nbsp;&nbsp;&nbsp;"+elementName, ContentMode.HTML);
        }else{
            elementLabel=new Label("&nbsp;&nbsp;&nbsp;"+elementName, ContentMode.HTML);
        }
        return elementLabel;
    }

    private Button generateStatusNavigateButton(String buttonName,final String activitySpaceName,final String componentType){
        Button navigateButton = new Button(buttonName);
        navigateButton.addStyleName("link");
        navigateButton.addStyleName("ui_compElementStatusBarLink");
        navigateButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                switchToComponentDetailView(activitySpaceName,componentType);
            }
        });
        return navigateButton;
    }

    private void switchToComponentDetailView(String activitySpaceName,String componentType){
        ActivitySpaceComponentSelectedEvent activitySpaceComponentSelectedEvent=
                new ActivitySpaceComponentSelectedEvent(activitySpaceName,componentType,null);
        this.currentUserClientInfo.getEventBlackBoard().fire(activitySpaceComponentSelectedEvent);
    }
}

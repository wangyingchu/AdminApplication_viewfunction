package com.viewfunction.vfbam.ui.component.activityManagement.roleManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.security.Role;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class RoleEditor  extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    public static final String EDITMODE_UPDATE="EDITMODE_UPDATE";
    public static final String EDITMODE_NEW="EDITMODE_NEW";
    private String currentEditMode;

    private final FormLayout form;
    private HorizontalLayout footer;
    private TextField roleName;
    private TextField roleDisplayName;
    private TextArea roleDescription;

    private Button updateButton;
    private Button cancelButton;
    private Button addButton;

    private String currentRoleDisplayName;
    private String currentRoleDescription;

    private AddNewRolePanel containerAddNewRolePanel;
    private Role role;

    public RoleEditor(UserClientInfo currentUserClientInfo,String editorType){
        this.currentUserClientInfo=currentUserClientInfo;

        this.currentEditMode = editorType;
        form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        roleName = new TextField("Role Name");
        roleName.setRequired(true);
        form.addComponent(roleName);

        roleDisplayName = new TextField("Role Display Name");
        roleDisplayName.setRequired(true);
        roleDisplayName.setWidth("50%");
        form.addComponent(roleDisplayName);

        roleDescription = new TextArea("Role Description");
        roleDescription.setWidth("50%");
        roleDescription.setRows(3);
        form.addComponent(roleDescription);

        form.setReadOnly(true);

        footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        updateButton = new Button("Update", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                boolean readOnly = form.isReadOnly();
                if (readOnly) {
                    form.setReadOnly(false);
                    roleDisplayName.setReadOnly(false);
                    roleDescription.setReadOnly(false);
                    form.removeStyleName("light");
                    event.getButton().setCaption("Save");
                    event.getButton().setIcon(FontAwesome.SAVE);
                    event.getButton().addStyleName("primary");
                    footer.addComponent(cancelButton);
                } else {
                    /* Do update role logic */
                    updateCurrentRole();
                }
            }
        });
        updateButton.setIcon(FontAwesome.HAND_O_RIGHT);

        cancelButton = new Button("Cancel", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                roleDisplayName.setValue(currentRoleDisplayName);
                roleDescription.setValue(currentRoleDescription);
                form.setReadOnly(true);
                roleDisplayName.setReadOnly(true);
                roleDescription.setReadOnly(true);
                form.addStyleName("light");
                updateButton.setCaption("Update");
                updateButton.removeStyleName("primary");
                updateButton.setIcon(FontAwesome.HAND_O_RIGHT);
                footer.removeComponent(cancelButton);
            }
        });
        cancelButton.setIcon(FontAwesome.TIMES);

        addButton=new Button("Add Role", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                /* Do add new role logic */
                addNewRole();
            }
        });
        addButton.setIcon(FontAwesome.PLUS_SQUARE);
        addButton.addStyleName("primary");
    }

    @Override
    public void attach() {
        super.attach();
        //reset form element value
        footer.removeAllComponents();
        roleName.setReadOnly(false);
        roleDisplayName.setReadOnly(false);
        roleDescription.setReadOnly(false);
        if(!this.currentEditMode.equals(EDITMODE_NEW)){
            //For edit existing role
            footer.addComponent(updateButton);
            if(this.role!=null){
                roleName.setValue(this.role.getRoleName());
                //need get role's info from server
                roleDisplayName.setValue(this.role.getDisplayName());
                roleDescription.setValue(this.role.getDescription());
                currentRoleDisplayName=roleDisplayName.getValue();
                currentRoleDescription=roleDescription.getValue();
            }
            roleName.setReadOnly(true);
            roleDisplayName.setReadOnly(true);
            roleDescription.setReadOnly(true);
        }else{
            //For add new participant
            footer.addComponent(addButton);
            roleName.setValue("");
            roleDisplayName.setValue("");
            roleDescription.setValue("");
        }
    }

    private boolean addNewRole(){
        final String roleNameStr=roleName.getValue();
        final String roleDisplayNameStr=roleDisplayName.getValue();
        final String roleDescriptionStr=roleDescription.getValue();
        if(roleNameStr.equals("")||roleDisplayNameStr.equals("")){
            Notification errorNotification = new Notification("Data Validation Error",
                    "Please input all required fields", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return false;
        }else{
            boolean alreadyExist=containerAddNewRolePanel.getRelatedRolesTable().checkRoleExistence(roleNameStr);
            if(alreadyExist){
                Notification errorNotification = new Notification("Data Validation Error",
                        "Role already exist", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return false;
            }
            //do add new logic
            Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+
                    " Please confirm to add new role  <b>"+roleNameStr +"</b>.", ContentMode.HTML);
            final ConfirmDialog addRoleConfirmDialog = new ConfirmDialog();
            addRoleConfirmDialog.setConfirmMessage(confirmMessage);
            final RoleEditor self=this;
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    Notification resultNotification = new Notification("Add Data Operation Success",
                            "Add new role success", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                    //close confirm dialog
                    addRoleConfirmDialog.close();
                    //execute callback logic
                    if(self.containerAddNewRolePanel!=null) {
                        self.containerAddNewRolePanel.addNewRoleFinishCallBack(
                                roleNameStr, roleDisplayNameStr, roleDescriptionStr
                        );
                    }
                }
            };
            addRoleConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(addRoleConfirmDialog);
        }
        return true;
    }

    private boolean updateCurrentRole(){
        final String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        final String roleNameStr=roleName.getValue();
        final String roleDisplayNameStr=roleDisplayName.getValue();
        final String roleDescriptionStr=roleDescription.getValue();
        if(roleDisplayNameStr.equals("")){
            Notification errorNotification = new Notification("Data Validation Error",
                    "Please input all required fields", Notification.Type.ERROR_MESSAGE);
            errorNotification.setPosition(Position.MIDDLE_CENTER);
            errorNotification.show(Page.getCurrent());
            errorNotification.setIcon(FontAwesome.WARNING);
            return false;
        }else{
            //do update logic
            Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+
                    " Please confirm to update role <b>"+roleNameStr +"</b>'s Information.", ContentMode.HTML);
            final ConfirmDialog updateRoleConfirmDialog = new ConfirmDialog();
            updateRoleConfirmDialog.setConfirmMessage(confirmMessage);
            final RoleEditor self=this;
            Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
                @Override
                public void buttonClick(final Button.ClickEvent event) {
                    //close confirm dialog
                    updateRoleConfirmDialog.close();
                    boolean updateRoleResult=
                            ActivitySpaceOperationUtil.updateRole(activitySpaceName,roleNameStr,roleDisplayNameStr,roleDescriptionStr);
                    if(updateRoleResult){
                        Notification resultNotification = new Notification("Update Data Operation Success",
                                "Update role information success", Notification.Type.HUMANIZED_MESSAGE);
                        resultNotification.setPosition(Position.MIDDLE_CENTER);
                        resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                        resultNotification.show(Page.getCurrent());

                        self.role.setDisplayName(roleDisplayName.getValue());
                        self.role.setDescription(roleDescription.getValue());
                        currentRoleDisplayName=roleDisplayName.getValue();
                        currentRoleDescription=roleDescription.getValue();

                        form.setReadOnly(true);
                        roleDisplayName.setReadOnly(true);
                        roleDescription.setReadOnly(true);
                        form.addStyleName("light");
                        updateButton.setCaption("Update");
                        updateButton.removeStyleName("primary");
                        updateButton.setIcon(FontAwesome.HAND_O_RIGHT);
                        footer.removeComponent(cancelButton);
                    }else{
                        Notification errorNotification = new Notification("Update Role Information Error",
                                "Server side error occurred", Notification.Type.ERROR_MESSAGE);
                        errorNotification.setPosition(Position.MIDDLE_CENTER);
                        errorNotification.show(Page.getCurrent());
                        errorNotification.setIcon(FontAwesome.WARNING);
                    }
                }
            };
            updateRoleConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
            UI.getCurrent().addWindow(updateRoleConfirmDialog);
        }
        return true;
    }

    public void setContainerAddNewRolePanel(AddNewRolePanel containerAddNewRolePanel) {
        this.containerAddNewRolePanel = containerAddNewRolePanel;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

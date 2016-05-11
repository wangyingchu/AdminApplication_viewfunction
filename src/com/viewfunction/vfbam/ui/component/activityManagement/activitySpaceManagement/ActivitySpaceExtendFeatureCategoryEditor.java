package com.viewfunction.vfbam.ui.component.activityManagement.activitySpaceManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.business.activitySpace.dao.ActivitySpaceMetaInfoDAO;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.PropertyValuesActionTable;
import com.viewfunction.vfbam.ui.component.common.SectionActionButton;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

public class ActivitySpaceExtendFeatureCategoryEditor extends VerticalLayout {
    private UserClientInfo currentUserClientInfo;
    private String activitySpaceName;
    private PropertyValuesActionTable propertyValuesActionTable;
    private Button updateButton;
    private Button cancelButton;
    private String[] currentExtendFeatureCategories;
    private SectionActionButton addCategoryActionButton;
    final HorizontalLayout operationButtonsLayout = new HorizontalLayout();
    public ActivitySpaceExtendFeatureCategoryEditor(UserClientInfo currentUserClientInfo,String activitySpaceName){
        this.currentUserClientInfo=currentUserClientInfo;
        this.activitySpaceName=activitySpaceName;

        SectionActionsBar extendFeatureCategoriesSectionActionsBar=new SectionActionsBar(new Label(FontAwesome.LIST.getHtml() + " "+"Extend Feature Categories Options", ContentMode.HTML));
        addComponent(extendFeatureCategoriesSectionActionsBar);
        addCategoryActionButton = new SectionActionButton();
        addCategoryActionButton.setCaption("Add New Category");
        addCategoryActionButton.setIcon(FontAwesome.PLUS_SQUARE);
        addCategoryActionButton.setEnabled(false);
        extendFeatureCategoriesSectionActionsBar.addActionComponent(addCategoryActionButton);
        propertyValuesActionTable=new PropertyValuesActionTable(this.currentUserClientInfo,"270","Categories Option","Extend Feature Categories Option",false,false);
        addComponent(propertyValuesActionTable);
        addCategoryActionButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                propertyValuesActionTable.addPropertyValue();
            }
        });

        operationButtonsLayout.setMargin(new MarginInfo(true, false, true, false));
        operationButtonsLayout.setSpacing(true);
        operationButtonsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        addComponent(operationButtonsLayout);

        updateButton = new Button("Update", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if(updateButton.getCaption().equals("Update")){
                    updateButton.setCaption("Save");
                    updateButton.setIcon(FontAwesome.SAVE);
                    updateButton.addStyleName("primary");
                    operationButtonsLayout.addComponent(cancelButton);
                    addCategoryActionButton.setEnabled(true);
                    propertyValuesActionTable.enableTableFullEdit();
                }else{
                    saveCategoriesData();
                }
            }
        });
        updateButton.setIcon(FontAwesome.HAND_O_RIGHT);
        operationButtonsLayout.addComponent(updateButton);

        cancelButton = new Button("Cancel", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                disableEdit();
            }
        });
        cancelButton.setIcon(FontAwesome.TIMES);
    }

    @Override
    public void attach() {
        super.attach();
        ActivitySpaceMetaInfoDAO metaInfoDAO= ActivitySpaceOperationUtil.getActivitySpaceMetaInfo(this.activitySpaceName, new String[]{ActivitySpaceOperationUtil.ACTIVITYSPACE_METAINFOTYPE_EXTENDFEATURECATEGORY});
        currentExtendFeatureCategories=metaInfoDAO.getExtendFeatureCategories();
        setExtendFeatureCategoriesValue();
    }

    private void setExtendFeatureCategoriesValue(){
        propertyValuesActionTable.setPropertyValues(currentExtendFeatureCategories);
    }

    private void disableEdit(){
        updateButton.setCaption("Update");
        updateButton.removeStyleName("primary");
        updateButton.setIcon(FontAwesome.HAND_O_RIGHT);
        operationButtonsLayout.removeComponent(cancelButton);
        addCategoryActionButton.setEnabled(false);
        propertyValuesActionTable.disableTableFullEdit();
        setExtendFeatureCategoriesValue();
    }

    private void saveCategoriesData(){
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+
                " Please confirm to update extend feature categories for activity space  <b>"+this.activitySpaceName +"</b>", ContentMode.HTML);
        final ActivitySpaceExtendFeatureCategoryEditor self=this;
        final String[] newCategories=propertyValuesActionTable.getPropertyValues();
        final ConfirmDialog updatePropertyValueConfirmDialog = new ConfirmDialog();
        updatePropertyValueConfirmDialog.setConfirmMessage(confirmMessage);
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                updatePropertyValueConfirmDialog.close();

                //do update change
                boolean updateCategoryResult=ActivitySpaceOperationUtil.setActivitySpaceExtendFeatureCategories(self.activitySpaceName, newCategories);
                if(updateCategoryResult){
                    self.currentExtendFeatureCategories=newCategories;
                    disableEdit();
                    Notification resultNotification = new Notification("Update Data Operation Success",
                            "Update activity space extend feature categories success", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                }else{
                    Notification errorNotification = new Notification("Update Activity Space extend feature Categories Error",
                            "Server side error occurred", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        updatePropertyValueConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(updatePropertyValueConfirmDialog);
    }
}

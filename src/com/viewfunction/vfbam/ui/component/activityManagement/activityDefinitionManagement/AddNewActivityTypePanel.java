package com.viewfunction.vfbam.ui.component.activityManagement.activityDefinitionManagement;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.viewfunction.activityEngine.activityBureau.BusinessActivityDefinition;
import com.viewfunction.vfbam.business.activitySpace.ActivitySpaceOperationUtil;
import com.viewfunction.vfbam.business.activitySpace.dao.ActivitySpaceMetaInfoDAO;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivityManagementConst;
import com.viewfunction.vfbam.ui.component.activityManagement.ActivitySpaceComponentModifyEvent;
import com.viewfunction.vfbam.ui.component.common.ConfirmDialog;
import com.viewfunction.vfbam.ui.component.common.MainSectionTitle;
import com.viewfunction.vfbam.ui.component.common.SectionActionsBar;
import com.viewfunction.vfbam.ui.util.RuntimeEnvironmentUtil;
import com.viewfunction.vfbam.ui.util.UserClientInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class AddNewActivityTypePanel extends VerticalLayout implements Upload.Receiver {
    private UserClientInfo currentUserClientInfo;
    private Window containerDialog;
    private SectionActionsBar addNewActivityTypeActionsBar;
    private Upload upload;
    private final static String tempFileDir = RuntimeEnvironmentUtil.getBinaryTempFileDirLocation();
    private final static String definitionFileMIMEType="text/xml";

    private String templateDefinitionFileName;
    private String activitySpaceName;

    private TextField activityTypeName;
    private TextField activityTypeBPMNFile;
    private String activityType;

    private HorizontalLayout footer;
    private Button addButton;
    private Button cancelButton;

    private ActivityDefinitionsActionTable relatedActivityDefinitionsActionTable;

    public AddNewActivityTypePanel(UserClientInfo currentUserClientInfo){
        this.currentUserClientInfo=currentUserClientInfo;
        setSpacing(true);
        setMargin(true);
        // Add New Activity Definition Section
        MainSectionTitle addNewActivityTypeSectionTitle=new MainSectionTitle("Add New Activity Definition");
        addComponent(addNewActivityTypeSectionTitle);
        addNewActivityTypeActionsBar=new SectionActionsBar(
                new Label("Activity Space : <b>"+""+"</b>" , ContentMode.HTML));
        addComponent(addNewActivityTypeActionsBar);

        FormLayout form = new FormLayout();
        form.setMargin(false);
        form.setWidth("100%");
        form.addStyleName("light");
        addComponent(form);

        activityTypeName = new TextField("Activity Type");
        activityTypeName.setRequired(true);
        form.addComponent(activityTypeName);

        activityTypeBPMNFile = new TextField("BPMN2 Definition File");
        activityTypeBPMNFile.setRequired(true);
        form.addComponent(activityTypeBPMNFile);

        activityTypeName.setEnabled(false);
        activityTypeBPMNFile.setEnabled(false);

        form.setReadOnly(true);

        footer = new HorizontalLayout();
        footer.setMargin(new MarginInfo(true, false, true, false));
        footer.setSpacing(true);
        footer.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        form.addComponent(footer);

        addButton=new Button("Add New Activity Definition", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                addNewActivityType();
            }
        });
        addButton.setIcon(FontAwesome.UPLOAD);
        addButton.addStyleName("primary");

        cancelButton = new Button("Cancel", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                activityTypeName.setEnabled(false);
                activityTypeName.setValue("");
                activityTypeBPMNFile.setValue("");
                footer.addComponent(upload);
                footer.removeComponent(addButton);
                footer.removeComponent(cancelButton);
                File uploadedFile = new File(templateDefinitionFileName);
                if(uploadedFile!=null){
                    uploadedFile.delete();
                }
            }
        });
        cancelButton.setIcon(FontAwesome.TIMES);

        upload = new Upload(null, this);
        // make analyzing start immediatedly when file is selected
        upload.setImmediate(true);
        upload.setButtonCaption("Upload Activity Definition BPMN2 File");
        /*
        upload.addStartedListener(new Upload.StartedListener() {
            @Override
            public void uploadStarted(Upload.StartedEvent startedEvent) {
                System.out.println("---Start---------------------------");
                System.out.println(startedEvent.getFilename());
                System.out.println(startedEvent.getContentLength());
                System.out.println(startedEvent.getMIMEType());
            }
        });
        upload.addFinishedListener(new Upload.FinishedListener() {
            @Override
            public void uploadFinished(Upload.FinishedEvent finishedEvent) {
                System.out.println("---Finish---------------------------");
                System.out.println(finishedEvent.getFilename());
                System.out.println(finishedEvent.getLength());
                System.out.println(finishedEvent.getMIMEType());
            }
        });
        */
        upload.addFailedListener(new Upload.FailedListener() {
            @Override
            public void uploadFailed(Upload.FailedEvent failedEvent) {
                Notification errorNotification = new Notification("Upload Activity Definition File Error",
                        "Server side error occurred", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
            }
        });

        upload.addSucceededListener(new Upload.SucceededListener() {
            @Override
            public void uploadSucceeded(Upload.SucceededEvent succeededEvent) {
                if(!succeededEvent.getMIMEType().equals(definitionFileMIMEType)){
                    Notification errorNotification = new Notification("Activity Definition File Format Error",
                            "Activity definition file should be a BPMN2 XML file", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                    File wrongFile = new File(templateDefinitionFileName);
                    if(wrongFile!=null){
                        wrongFile.delete();
                    }
                }else{
                    renderAddNewActivityTypeUI(succeededEvent.getFilename());
                }
            }
        });
        footer.addComponent(upload);
    }

    private void renderAddNewActivityTypeUI(String fileName){
        int idx=fileName.indexOf(".");
        String activityType=fileName.substring(0,idx);
        String[] existingSpacesArray=ActivitySpaceOperationUtil.listActivitySpaces();
        for(String currentSpaceName:existingSpacesArray){
            String activitySpaceNamePrefix=currentSpaceName+"_";
            if(activityType.startsWith(activitySpaceNamePrefix)){
                if(!currentSpaceName.equals(activitySpaceName)){
                    Notification errorNotification = new Notification("Activity Definition File Error",
                            "Activity definition file's activity space prefix doesn't match current activity space", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                    File wrongFile = new File(templateDefinitionFileName);
                    if(wrongFile!=null){
                        wrongFile.delete();
                    }
                    return;
                }else{
                    activityType=activityType.replaceFirst(activitySpaceNamePrefix,"");
                }
            }
        }
        this.activityType=activityType;
        activityTypeName.setValue(activityType);
        activityTypeBPMNFile.setValue(fileName);
        activityTypeName.setEnabled(true);
        footer.removeComponent(upload);
        footer.addComponent(addButton);
        footer.addComponent(cancelButton);
    }

    private void addNewActivityType(){
        //do add new logic
        ActivitySpaceMetaInfoDAO metaInfoData=ActivitySpaceOperationUtil.
                getActivitySpaceMetaInfo(activitySpaceName, new String[]{ActivitySpaceOperationUtil.ACTIVITYSPACE_METAINFOTYPE_ACTIVITYTYPE});
        BusinessActivityDefinition[] existingActivityTypes=metaInfoData.getBusinessActivityDefinitions();
        for(BusinessActivityDefinition currentBusinessActivityDefinition:existingActivityTypes){
            if(currentBusinessActivityDefinition.getActivityType().equals(this.activityType)){
                Notification errorNotification = new Notification("Data Validation Error",
                        "Activity Definition "+this.activityType+" already exists", Notification.Type.ERROR_MESSAGE);
                errorNotification.setPosition(Position.MIDDLE_CENTER);
                errorNotification.show(Page.getCurrent());
                errorNotification.setIcon(FontAwesome.WARNING);
                return;
            }
        }
        Label confirmMessage=new Label(FontAwesome.INFO.getHtml()+
                " Please confirm to add new Activity Definition  <b>"+this.activityType +"</b>", ContentMode.HTML);
        final ConfirmDialog addActivityTypeConfirmDialog = new ConfirmDialog();
        addActivityTypeConfirmDialog.setConfirmMessage(confirmMessage);
        final AddNewActivityTypePanel self=this;
        Button.ClickListener confirmButtonClickListener = new Button.ClickListener() {
            @Override
            public void buttonClick(final Button.ClickEvent event) {
                //close confirm dialog
                addActivityTypeConfirmDialog.close();
                //execute business logic
                boolean addNewActivityTypeResult=
                        ActivitySpaceOperationUtil.addNewActivityType(self.activitySpaceName,self.activityType,self.templateDefinitionFileName);
                if(addNewActivityTypeResult){
                    Notification resultNotification = new Notification("Add New Activity Definition Success",
                            "Add new activity definition success", Notification.Type.HUMANIZED_MESSAGE);
                    resultNotification.setPosition(Position.MIDDLE_CENTER);
                    resultNotification.setIcon(FontAwesome.INFO_CIRCLE);
                    resultNotification.show(Page.getCurrent());
                    self.containerDialog.close();
                    self.relatedActivityDefinitionsActionTable.loadDefinitionsData();
                    broadcastAddedActivityDefinitionEvent(self.activityType);
                }else{
                    Notification errorNotification = new Notification("Add New Activity Definition Error",
                            "Server side error occurred", Notification.Type.ERROR_MESSAGE);
                    errorNotification.setPosition(Position.MIDDLE_CENTER);
                    errorNotification.show(Page.getCurrent());
                    errorNotification.setIcon(FontAwesome.WARNING);
                }
            }
        };
        addActivityTypeConfirmDialog.setConfirmButtonClickListener(confirmButtonClickListener);
        UI.getCurrent().addWindow(addActivityTypeConfirmDialog);
    }

    @Override
    public void attach() {
        super.attach();
        if(this.currentUserClientInfo.getActivitySpaceManagementMeteInfo()!=null){
            activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
            Label activitySpaceNameLabel=new Label("Activity Space : <b>"+activitySpaceName+"</b>" , ContentMode.HTML);
            addNewActivityTypeActionsBar.resetSectionActionsBarContent(activitySpaceNameLabel);
        }
        activityTypeName.setEnabled(false);
        activityTypeName.setValue("");
        activityTypeBPMNFile.setValue("");
        footer.addComponent(upload);
        footer.removeComponent(addButton);
        footer.removeComponent(cancelButton);
        if(templateDefinitionFileName!=null){
            File uploadedFile = new File(templateDefinitionFileName);
            if(uploadedFile!=null){
                uploadedFile.delete();
            }
        }
    }

    public void setContainerDialog(Window containerDialog) {
        this.containerDialog = containerDialog;
    }

    @Override
    public OutputStream receiveUpload(String filename, String MIMEType) {
        String templateDefinationFilePerfix=this.activitySpaceName+"_";
        templateDefinitionFileName=tempFileDir+templateDefinationFilePerfix+filename;
        FileOutputStream fos = null;
        // Output stream to write to
        File file = new File(templateDefinitionFileName);
        try {
            // Open the file for writing.
            fos = new FileOutputStream(file);
        } catch (final java.io.FileNotFoundException e) {
            // Error while opening the file. Not reported here.
            e.printStackTrace();
            return null;
        }
        return fos;
    }

    public void setRelatedActivityDefinitionsActionTable(ActivityDefinitionsActionTable relatedActivityDefinitionsActionTable) {
        this.relatedActivityDefinitionsActionTable = relatedActivityDefinitionsActionTable;
    }

    private void broadcastAddedActivityDefinitionEvent(String activityType){
        String activitySpaceName=this.currentUserClientInfo.getActivitySpaceManagementMeteInfo().getActivitySpaceName();
        String componentType= ActivityManagementConst.COMPONENT_TYPE_ACTIVITYDEFINITION;
        ActivitySpaceComponentModifyEvent activitySpaceComponentModifyEvent=
                new ActivitySpaceComponentModifyEvent(activitySpaceName,componentType,activityType,
                        ActivitySpaceComponentModifyEvent.MODIFYTYPE_ADD);
        this.currentUserClientInfo.getEventBlackBoard().fire(activitySpaceComponentModifyEvent);
    }
}

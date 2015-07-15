package com.stacksync.desktop.gui.tray;

import com.ast.cloudABE.GUI.UIUtils;
import com.ast.cloudABE.accessTree.AccessTree;
import com.ast.cloudABE.cloudABEClient.CloudABEClientAdapter;
import com.ast.cloudABE.kpabe.KPABESecretKey;
import com.ast.cloudABE.userManagement.User;
import com.google.gson.Gson;
import com.stacksync.commons.exceptions.ShareProposalNotCreatedException;
import com.stacksync.commons.exceptions.UserNotFoundException;
import com.stacksync.desktop.ApplicationController;
import com.stacksync.desktop.Constants;
import com.stacksync.desktop.Environment;
import com.stacksync.desktop.config.Config;
import com.stacksync.desktop.config.profile.Profile;
import com.stacksync.desktop.db.DatabaseHelper;
import com.stacksync.desktop.db.models.CloneFile;
import com.stacksync.desktop.db.models.CloneWorkspace;
import com.stacksync.desktop.gui.error.ErrorMessage;
import com.stacksync.desktop.gui.sharing.SharePanel;
import com.stacksync.desktop.syncserver.Server;
import com.stacksync.desktop.util.FileUtil;
import java.io.File;
import java.util.HashMap;
import java.util.ResourceBundle;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import org.apache.log4j.Logger;

/**
 *
 * @author cotes
 */
public class TrayEventListenerImpl implements TrayEventListener {

    private ApplicationController controller;
    private final Logger logger = Logger.getLogger(TrayEventListenerImpl.class.getName());

    public TrayEventListenerImpl(ApplicationController controller) {
        this.controller = controller;
    }

    @Override
    public void trayEventOccurred(TrayEvent event) {
        switch (event.getType()) {
            case OPEN_FOLDER:
                File folder = new File((String) event.getArgs().get(0));
                FileUtil.openFile(folder);
                break;

            /*case PREFERENCES:
             settingsDialog.setVisible(true);
             break;*/
            case WEBSITE:
                FileUtil.browsePage(Constants.APPLICATION_URL);
                break;

            case WEBSITE2:
                FileUtil.browsePage(Constants.APPLICATION_URL2);
                break;

            case PAUSE_SYNC:
                this.controller.pauseSync();
                break;

            case RESUME_SYNC:
                this.controller.resumeSync();
                break;
            case SHARE:
                //Show share panel
                
                JFrame frame = createSharingFrame();

                SharePanel panel = new SharePanel(frame);

                frame.setContentPane(panel);
                frame.pack();
                frame.setVisible(true);

                synchronized (panel.lock) {
                    try {
                        panel.lock.wait();
                    } catch (InterruptedException ex) {
                        logger.error(ex);
                    }
                }
                
                HashMap<String,HashMap<String,byte[]>> emailsKeys = null;
                
                Config config = Config.getInstance();
                Profile profile = config.getProfile();
                Server server = profile.getServer();

                DatabaseHelper db = DatabaseHelper.getInstance();
                CloneFile sharedFolder = db.getFileOrFolder(panel.getFolderSelected());
                
                String publicKey = null;
                        
                if (panel.isAbeEncrypted()) {

                    String RESOURCES_PATH = Environment.getInstance().getDefaultUserConfigDir().getAbsolutePath()+"/conf/abe/";
                    
                    CloudABEClientAdapter abeClient = null;
                             
                    try {
                        abeClient = new CloudABEClientAdapter(RESOURCES_PATH);
                        abeClient.setupABESystem(0);
                    } catch (Exception ex) {
                        logger.error(ex);
                        break;
                    }
                    
                    CloneWorkspace newWorkspace = new CloneWorkspace();
                    newWorkspace.setId(sharedFolder.getId().toString());
                    newWorkspace.setName(sharedFolder.getName());
                    newWorkspace.setLocalLastUpdate(0);
                    newWorkspace.setRemoteRevision(0);
                    newWorkspace.setEncrypted(false);
                    newWorkspace.setAbeEncrypted(true);
                    newWorkspace.setDefaultWorkspace(false);
                    newWorkspace.setOwner(profile.getAccountId());
                    newWorkspace.getPathWorkspace();
                    newWorkspace.setIsApproved(false);
                    newWorkspace.setGroupGenerator(abeClient.getGroupGenerator());
                    
                    Gson gson = new Gson();
                    publicKey = gson.toJson(abeClient.getPublicKey());
                    String masterKey = gson.toJson(abeClient.getMasterKey());
                    
                    newWorkspace.setMasterKey(masterKey.getBytes());
                    newWorkspace.setPublicKey(publicKey.getBytes());
                    
                    newWorkspace.merge();
                    
                    emailsKeys = new HashMap<String,HashMap<String,byte[]>>();
                          
                    for (String email : panel.getEmails()) {
                        System.out.println("Setting permissions for: " + email);
                        try {
                            
                            String attSet = UIUtils.getAccessStructure(RESOURCES_PATH, null, email);
                            
                            User invitedUser = abeClient.newABEUserInvited(attSet);
                            AccessTree accessTree= invitedUser.getSecretKey().getAccess_tree();

                            KPABESecretKey secretKeyLight = new KPABESecretKey(invitedUser.getSecretKey().getLeaf_keys(),null);
            
                            String secretKeyjson = gson.toJson(secretKeyLight);
                            
                            HashMap<String,byte[]> secretKeyStruct = new HashMap<String,byte[]>();
                            
                            secretKeyStruct.put("secret_key", secretKeyjson.getBytes());
                            secretKeyStruct.put("access_struct", accessTree.toString().getBytes());
                            
                            emailsKeys.put(email,secretKeyStruct);
                            
                            System.out.println("[" + email + "] Setting up access logical expression to: " + attSet);
                            
                        }catch (Exception ex) {
                            logger.error(ex);
                        }
                         
                        
                    }
                }
                
                if(panel.isAbeEncrypted()){
                        try {
                            /*FIXME! Be careful, emails and keys are sent in plain text without encryption, 
                                    key distribution problem should be solved in order to guarantee security and privacy */
                            server.createShareProposal(profile.getAccountId(), publicKey.getBytes(), emailsKeys, sharedFolder.getId(), false, panel.isAbeEncrypted());
                        } catch (ShareProposalNotCreatedException ex) {
                            ErrorMessage.showMessage(panel, "Error", "An error ocurred, please try again later.\nVerify email accounts.");
                        } catch (UserNotFoundException ex) {
                            ErrorMessage.showMessage(panel, "Error", "An error ocurred, please try again later.");
                        }
                }else {
                    {
                        try {
                            server.createShareProposal(profile.getAccountId(), panel.getEmails(), sharedFolder.getId(), false, panel.isAbeEncrypted());
                        } catch (ShareProposalNotCreatedException ex) {
                            ErrorMessage.showMessage(panel, "Error", "An error ocurred, please try again later.\nVerify email accounts.");
                        } catch (UserNotFoundException ex) {
                            ErrorMessage.showMessage(panel, "Error", "An error ocurred, please try again later.");
                        }
                    }
                }

                break;
            case QUIT:
                this.controller.doShutdownTray();
                break;

            default:
                //checkthis
                logger.warn("Unknown tray event type: " + event);
            // Fressen.
        }
    }

    private JFrame createSharingFrame() {

        Config config = Config.getInstance();
        ResourceBundle resourceBundle = config.getResourceBundle();

        String title = resourceBundle.getString("share_panel_title");
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setIconImage(new ImageIcon(config.getResDir() + File.separator + "logo48.png").getImage());

        return frame;
    }
}

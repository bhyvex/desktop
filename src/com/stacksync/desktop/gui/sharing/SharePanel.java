package com.stacksync.desktop.gui.sharing;

import com.stacksync.commons.exceptions.ShareProposalNotCreatedException;
import com.stacksync.commons.exceptions.UserNotFoundException;
import com.stacksync.desktop.config.Config;
import com.stacksync.desktop.config.Encryption;
import com.stacksync.desktop.config.Folder;
import com.stacksync.desktop.config.profile.Profile;
import com.stacksync.desktop.db.DatabaseHelper;
import com.stacksync.desktop.db.models.CloneItem;
import com.stacksync.desktop.exceptions.ConfigException;
import com.stacksync.desktop.gui.error.ErrorMessage;
import com.stacksync.desktop.syncserver.Server;
import com.stacksync.desktop.util.FileUtil;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.JFrame;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SharePanel extends javax.swing.JPanel implements DocumentListener, ItemListener {

    private static final Config config = Config.getInstance();
    private ResourceBundle resourceBundle;
    
    private JFrame frame;
    private File folderSelected;
    
    /*
     * Creates new form SharePanel
     */
    public SharePanel(JFrame frame) {
        
        super();
        this.resourceBundle = config.getResourceBundle();
        this.frame = frame;
        initComponents();
        
        this.lblMail.setText(resourceBundle.getString("share_panel_email"));
        this.lblFolder.setText(resourceBundle.getString("share_panel_folder"));
        this.cancelButton.setText(resourceBundle.getString("wizard_cancel"));
        this.shareButton.setText(resourceBundle.getString("share_button_share"));
        this.shareButton.setEnabled(false);
        this.browseButton.setText(resourceBundle.getString("fpw_simple_browse")); 
        
        this.lblPassword.setText(resourceBundle.getString("encryp_password"));
        this.lblPasswordConfirm.setText(resourceBundle.getString("encryp_password_confirm"));
        this.txtPassword.setEnabled(false);
        this.txtPassword1.setEnabled(false);
        // Hide encryption
        this.lblPassword.setVisible(false);
        this.lblPasswordConfirm.setVisible(false);
        this.txtPassword.setVisible(false);
        this.txtPassword1.setVisible(false);
        
        this.emailField.getDocument().addDocumentListener(this);
        this.folderNameField.getDocument().addDocumentListener(this);
        
        this.encryptCheckBox.addItemListener(this);
        this.encryptCheckBox.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        emailField = new javax.swing.JTextField();
        lblMail = new javax.swing.JLabel();
        lblFolder = new javax.swing.JLabel();
        folderNameField = new javax.swing.JTextField();
        cancelButton = new javax.swing.JButton();
        shareButton = new javax.swing.JButton();
        lblPassword = new javax.swing.JLabel();
        lblPasswordConfirm = new javax.swing.JLabel();
        encryptCheckBox = new javax.swing.JCheckBox();
        txtPassword = new javax.swing.JPasswordField();
        txtPassword1 = new javax.swing.JPasswordField();
        browseButton = new javax.swing.JButton();

        lblMail.setText("__E-mail:");

        lblFolder.setText("__Folder name:");

        folderNameField.setEditable(false);

        cancelButton.setText("__Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        shareButton.setText("__Share");
        shareButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                shareButtonActionPerformed(evt);
            }
        });

        lblPassword.setText("__Password:");

        lblPasswordConfirm.setText("__Confirm:");

        encryptCheckBox.setText("__Encrypt");

        browseButton.setText("__Browse");
        browseButton.setName("browseButton"); // NOI18N
        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(encryptCheckBox)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblFolder)
                            .addComponent(lblMail)
                            .addComponent(lblPassword)
                            .addComponent(lblPasswordConfirm))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(cancelButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(shareButton, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26))
                            .addComponent(folderNameField)
                            .addComponent(emailField, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                            .addComponent(txtPassword)
                            .addComponent(txtPassword1))))
                .addGap(26, 26, 26)
                .addComponent(browseButton)
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblMail))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(folderNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblFolder)
                    .addComponent(browseButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(encryptCheckBox)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(lblPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(8, 8, 8)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtPassword1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(lblPasswordConfirm, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(shareButton)
                    .addComponent(cancelButton))
                .addGap(14, 14, 14))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void shareButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_shareButtonActionPerformed

        if (this.emailField.getText().isEmpty() || this.folderNameField.getText().isEmpty()) {
            return;
        }
        
        Profile profile = config.getProfile();
        Server server = profile.getServer();
        
        List<String> emails;
        try {
            emails = getEmails();
        } catch (IOException ex) {
            ErrorMessage.showMessage(this, "Error", "Verify email accounts.");
            return;
        }
        
        boolean correct = check();
        if (!correct) {
            return;
        }
        
        try {
            boolean encrypted = false;
            if (this.encryptCheckBox.isSelected()) {
                encrypted = true;
            }
            
            DatabaseHelper db = DatabaseHelper.getInstance();
            
            if (this.folderSelected == null) {
                ErrorMessage.showMessage(this, "Error", "No folder selected!");
                return;
            }
            
            CloneItem folder = db.getFileOrFolder(this.folderSelected);
            server.createShareProposal(profile.getAccountId(), emails, folder.getId(), encrypted);
            this.frame.setVisible(false);
        } catch (ShareProposalNotCreatedException ex) {
            ErrorMessage.showMessage(this, "Error", "An error ocurred, please try again later.\nVerify email accounts.");
        } catch (UserNotFoundException ex) {
            ErrorMessage.showMessage(this, "Error", "An error ocurred, please try again later.");
        }
    }//GEN-LAST:event_shareButtonActionPerformed

    private List<String> getEmails() throws IOException {
        
        String emailsStr = this.emailField.getText();
        emailsStr = emailsStr.replaceAll("\\s", "");    // Remove whitespaces
        List<String> emails = Arrays.asList(emailsStr.split(","));   // Split it
        
        for (String email : emails) {
            // Check if email is correct
            if (!isCorrect(email)) {
                throw new IOException("Incorrect mail.");
            }
        }
        
        // All mails are correct! =)
        return emails;
    }
    
    private boolean isCorrect(String mail) {
        
        if (!mail.contains("@") || !mail.contains(".")) {
            return false;
        }
        
        // Add here other filters (special characters...)
        
        return true;
    }
    
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        this.frame.setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void browseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browseButtonActionPerformed

        File file = FileUtil.showBrowseDirectoryDialog();
        if (file != null && !file.getPath().equals("")) {
            
            Profile profile = config.getProfile();
            Folder folder = profile.getFolder();
            String rootFolderPath = folder.getLocalFile().getAbsolutePath();
            String sharedFolderPath = file.getAbsolutePath();
            
            if (!sharedFolderPath.contains(rootFolderPath) || sharedFolderPath.equals(rootFolderPath)) {
                ErrorMessage.showMessage(this, "Error", "Incorrect folder.");
                return;
            }
            
            this.folderNameField.setText(file.getAbsolutePath());
            this.folderSelected = file;
        }
    }//GEN-LAST:event_browseButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browseButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField emailField;
    private javax.swing.JCheckBox encryptCheckBox;
    private javax.swing.JTextField folderNameField;
    private javax.swing.JLabel lblFolder;
    private javax.swing.JLabel lblMail;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPasswordConfirm;
    private javax.swing.JButton shareButton;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JPasswordField txtPassword1;
    // End of variables declaration//GEN-END:variables

    /*
     * Functions to enable/disable dynamically the share button
     */
    @Override
    public void insertUpdate(DocumentEvent e) {
        setEnableShareButton();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        setEnableShareButton();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        setEnableShareButton();
    }
    
    private void setEnableShareButton() {
        if (this.emailField.getText().equals("") ||
                this.folderNameField.getText().equals("")) {
            this.shareButton.setEnabled(false);
        } else {
            this.shareButton.setEnabled(true);
            getRootPane().setDefaultButton(this.shareButton);
        }
    }
    
    @Override
    public void itemStateChanged(ItemEvent e) {
        
        if (this.encryptCheckBox.isSelected()) {
            
            this.txtPassword.setEnabled(true);
            this.txtPassword1.setEnabled(true);
            this.lblPassword.setVisible(true);
            this.lblPasswordConfirm.setVisible(true);
            this.txtPassword.setVisible(true);
            this.txtPassword1.setVisible(true);
        } else {
            this.lblPassword.setVisible(false);
            this.lblPasswordConfirm.setVisible(false);
            this.txtPassword.setVisible(false);
            this.txtPassword1.setVisible(false);
            this.txtPassword.setEnabled(false);
            this.txtPassword1.setEnabled(false);
        }
    }
    
    private boolean check() {
        boolean check = true;

        if (this.encryptCheckBox.isSelected()) {

            String password = new String(txtPassword.getPassword());
            String password1 = new String(txtPassword1.getPassword());

            if (password.isEmpty() || password1.isEmpty()) {
                ErrorMessage.showMessage(this, "Error", "The password is empty.");
                check = false;
            }

            if (password.compareTo(password1) != 0) {
                ErrorMessage.showMessage(this, "Error", "The passwords do not match.");
                check = false;
            }

            // TODO check the password length
            try {
                Encryption encryption = new Encryption(new String(txtPassword.getPassword()));
            } catch (ConfigException ex) {
                ErrorMessage.showMessage(this, "Error", ex.getMessage());
                check = false;
            }
        }

        return check;
    }
}

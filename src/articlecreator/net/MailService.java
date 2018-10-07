package articlecreator.net;

import articlecreator.gui.components.ui.ProjectsUI;
import articlecreator.gui.components.ui.PropertiesUI;
import articlecreator.gui.run.ArticleManagmentMain;
import java.awt.Dimension;
import java.security.Security;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;

public class MailService {

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    public MailService() {
        super();
        this.setMailServerProperties();
    }

    public static void main(String args[]) throws AddressException,
            MessagingException {

        MailService javaEmail = new MailService();

        javaEmail.setMailServerProperties();
        //javaEmail.createEmailMessage();
        //javaEmail.sendEmail();
    }

    public void constractMessade(String[] sendToEmails, String subject, String body) {

        try {
            createEmailMessage(sendToEmails, subject, body);
            sendEmail();
        } catch (MessagingException ex) {
            // Logger.getLogger(MailService.class.getName()).log(Level.SEVERE, null, ex);
            /*
        You need to set the Allow less secure apps: ON, 
        login to google using the account desired, 
        and go to the security page https://myaccount.google.com/lesssecureapps
        and set the Allow less secure apps to be ON.
             */
            displayErrorMessage();
        }
    }

    private void displayErrorMessage() {
        JEditorPane txt = new JTextPane();
        //HTMLEditorKit kit = HTMLEditorKit.
        String type = ("text/html");
        final EditorKit kit = txt.getEditorKitForContentType(type);

        //  SwingUtilities.invokeLater(new Runnable() {
        //    public void run() {
        txt.setEditorKit(kit);
        javax.swing.text.Document d = kit.createDefaultDocument();
        txt.setDocument(d);
        String s = "You need to set the Allow less secure apps: ON,<br>"
                + "login to google using the account desired,<br>"
                + "and go to the security page <br><a href='https://myaccount.google.com/lesssecureapps'>https://myaccount.google.com/lesssecureapps</a><br>"
                + "and set the Allow less secure apps to be ON.<br>"
                + "If you gmail has 2-step authentication,this may not work<br>"
                 + "This setting is not available for accounts with 2-Step <br>"
                + "Verification enabled. Such accounts require an application-specific <br>"
                + "password for less secure apps access"
                +"<a href='https://support.google.com/accounts/answer/185833'>https://support.google.com/accounts/answer/185833</a>";
        txt.setText(s);
        txt.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txt);
        scrollPane.setPreferredSize(new Dimension(350, 200));
        int result = JOptionPane.showConfirmDialog(null, scrollPane, "Sending Emails Error", JOptionPane.PLAIN_MESSAGE);

    }

    public void setMailServerProperties() {
        emailProperties = System.getProperties();

        /*
               String emailPort = "587";//gmail's smtp port

		emailProperties = System.getProperties();
		emailProperties.put("mail.smtp.port", emailPort);
		emailProperties.put("mail.smtp.auth", "true");
		emailProperties.put("mail.smtp.starttls.enable", "true");
         */
 /*
        You need to set the Allow less secure apps: ON, 
        login to google using the account desired, 
        and go to the security page https://myaccount.google.com/lesssecureapps
        and set the Allow less secure apps to be ON.
         */
        String email = (String) PropertiesUI.getInstance().getDefaultProps().get("USER_GMAIL");
        email = email.split("@")[0];
        emailProperties.put("mail.smtp.user", /*"jamesdone0507"*/ email);
        emailProperties.put("mail.smtp.host", "smtp.gmail.com");
        emailProperties.put("mail.smtp.port", "25");
        emailProperties.put("mail.debug", "true");
        emailProperties.put("mail.smtp.auth", "true");
        emailProperties.put("mail.smtp.starttls.enable", "true");
        emailProperties.put("mail.smtp.EnableSSL.enable", "true");

        emailProperties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        emailProperties.setProperty("mail.smtp.socketFactory.fallback", "false");
        emailProperties.setProperty("mail.smtp.port", "465");
        emailProperties.setProperty("mail.smtp.socketFactory.port", "465");
    }

    public void createEmailMessage(String[] emailTo, String subject, String body) throws AddressException,
            MessagingException {
        String[] toEmails = emailTo;// {/*"fx2go4u@gmail.com"*/emailTo};
        String emailSubject = subject;//"Java Email";
        String emailBody = body;//"This is an email sent by JavaMail api.";

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        for (int i = 0; i < toEmails.length; i++) {
            emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
        }

        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");//for a html email
        //emailMessage.setText(emailBody);// for a text email

    }

    public void sendEmail() throws AddressException, MessagingException {

        String email = (String) PropertiesUI.getInstance().getDefaultProps().get("USER_GMAIL");
        String pswd = (String) PropertiesUI.getInstance().getDefaultProps().get("USER_GMAIL_PSWD");
        if (pswd != null) {
            pswd = ArticleManagmentMain.decrypt(pswd);
        }
        email = email.split("@")[0];
        String emailHost = "smtp.gmail.com";
        String fromUser = email;//"jamesdone0507";//just the id alone without @gmail.com
        String fromUserEmailPassword = pswd;// "alida001";

        Transport transport = mailSession.getTransport("smtp");

        transport.connect(emailHost, fromUser, fromUserEmailPassword);
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
       // System.out.println("Email sent successfully.");
        ProjectsUI.console.append("\r\nEmail sent successfully.\r\n");
        ProjectsUI.console.setCaretPosition(ProjectsUI.console.getText().length()-1);
        ProjectsUI.console.getCaret().setVisible(true);
    }
}

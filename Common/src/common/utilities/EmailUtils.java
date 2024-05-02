package common.utilities;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SubjectTerm;

import org.apache.log4j.*;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Properties;

public class EmailUtils {
	private static final Logger LOGGER = LogManager.getLogger(EmailUtils.class);

	@SuppressWarnings("static-access")

	public static void main(String args[]) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.stmp.user", "sarath.kavati@gmail.com");
		// If you want you use TLS
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.password", "password");
		// If you want to use SSL
		props.put("mail.smtp.socketFactory.port", "25");
		props.put("mail.smtp.ssl.checkserveridentity", true);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.port", "25");
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				String username = "sarath.kavati@gmail.com";

				return new PasswordAuthentication(username, "pwd");
			}
		});
		String[] to = { "sarath.kavati@stgeorge.com.au" };
		String from = "sarath.kavati@gmail.com";
		String subject = "IBank Test Report";
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(from));
			InternetAddress[] addressTo = new InternetAddress[to.length];
			for (int i = 0; i < to.length; i++) {
				addressTo[i] = new InternetAddress(to[i]);
			}
			msg.setRecipients(RecipientType.TO, addressTo);
			// msg.setRecipient(MimeMessage.RecipientType.TO, new
			// InternetAddress(to));
			msg.setSubject(subject);
			// msg.setText("JAVA is the BEST");

			// Create the message part
			BodyPart messageBodyPart = new MimeBodyPart();

			// Fill the message
			messageBodyPart.setText("Testing email from java");

			// Create a multipar message
			Multipart multipart = new MimeMultipart();

			// Set text message part
			multipart.addBodyPart(messageBodyPart);

			// Part two is attachment
			messageBodyPart = new MimeBodyPart();
			String filePath = "C:\\Users\\C70032\\TestReport\\Extent\\IBank";
			String filename = "TestResultsReport.html";
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName(filename);

			multipart.addBodyPart(messageBodyPart);

			// Send the complete message parts
			msg.setContent(multipart);

			Transport transport = session.getTransport("smtp");
			transport.send(msg);
			LOGGER.info("E-mail sent !");
		} catch (Exception exc) {
			LOGGER.info(exc);
		}
	}

	// *********************************** Check gamil messages using subject as
	// criteria
	// ***************************************************************************
	public static void main1(String[] args) throws Exception {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");

		Session session = Session.getDefaultInstance(props, null);
		Store store = session.getStore("imaps");
		store.connect("imap.gmail.com", "sarath.kavati@gmail.com", "xxxxxx");

		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_WRITE);

		LOGGER.info("Total Message:" + folder.getMessageCount());
		LOGGER.info("Unread Message:" + folder.getUnreadMessageCount());

		Message[] messages = null;
		boolean isMailFound = false;
		Message mailFromGod = null;

		// Search for mail from God
		for (int i = 0; i < 5; i++) {
			messages = folder.search(new SubjectTerm("sql"), // subject of
																// required
																// email
					folder.getMessages());
			// Wait for 10 seconds
			if (messages.length == 0) {
				Thread.sleep(10000);
			}
		}

		// Search for unread mail from God
		// This is to avoid using the mail for which
		// Registration is already done
		for (Message mail : messages) {
			if (!mail.isSet(Flags.Flag.SEEN)) {
				mailFromGod = mail;
				LOGGER.info("Message Count is: " + mailFromGod.getMessageNumber());
				isMailFound = true;
			}
		}

		// Test fails if no unread mail was found from God
		if (!isMailFound) {
			throw new Exception("Could not find new mail from God :-(");

			// Read the content of mail and launch registration URL
		} else {
			String line;
			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(mailFromGod.getInputStream()));
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			LOGGER.info("Email body message: " + buffer);

		}
	}

	// ******************* Example 2 for sending mail *********************

	// reportFileName = TestExecutionResultFileName

	public static void execute(String reportFileName) throws Exception

	{
		String path = "C:\\Users\\C70032\\TestReport\\Extent\\IBank";

		String[] to = { "sarath.kavati@stgeorge.com.au" };
		String[] cc = {};
		String[] bcc = { "sarath.kavati@stgeorge.com.au" };
		Properties login = new Properties();
		try (FileReader in = new FileReader(System.getProperty("user.home") + "\\gmail\\login_properties.txt")) {
			login.load(in);
		}
		String username = login.getProperty("username");
		String password = login.getProperty("password");
		sendMail(username, password, "smtp.gmail.com", "465", "true", "true", true, "javax.net.ssl.SSLSocketFactory",
				"false", to, cc, bcc, "CompassAutomation_TestReport", "Please find IBank test report attached.", path,
				reportFileName);
	}

	public static boolean sendMail(String userName, String passWord, String host, String port, String starttls,
			String auth, boolean debug, String socketFactoryClass, String fallback, String[] to, String[] cc,
			String[] bcc, String subject, String text, String attachmentPath, String attachmentName) {

		// Object Instantiation of a properties file.
		Properties props = new Properties();

		props.put("mail.smtp.user", userName);

		props.put("mail.smtp.host", host);

		if (!"".equals(port)) {
			props.put("mail.smtp.port", port);
		}

		if (!"".equals(starttls)) {
			props.put("mail.smtp.starttls.enable", starttls);
			props.put("mail.smtp.auth", auth);
		}

		if (debug) {

			props.put("mail.smtp.debug", "true");

		} else {

			props.put("mail.smtp.debug", "false");

		}

		if (!"".equals(port)) {
			props.put("mail.smtp.socketFactory.port", port);
		}
		if (!"".equals(socketFactoryClass)) {
			props.put("mail.smtp.socketFactory.class", socketFactoryClass);
		}
		if (!"".equals(fallback)) {
			props.put("mail.smtp.socketFactory.fallback", fallback);
		}

		try {

			Session session = Session.getDefaultInstance(props, null);

			session.setDebug(debug);

			MimeMessage msg = new MimeMessage(session);

			msg.setText(text);

			msg.setSubject(subject);

			Multipart multipart = new MimeMultipart();
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			File att = new File(new File(attachmentPath), attachmentName);
			messageBodyPart.attachFile(att);
			multipart.addBodyPart(messageBodyPart);

			msg.setContent(multipart);
			msg.setFrom(new InternetAddress(userName));

			for (int i = 0; i < to.length; i++) {
				msg.addRecipient(Message.RecipientType.TO, new InternetAddress(to[i]));
			}

			for (int i = 0; i < cc.length; i++) {
				msg.addRecipient(Message.RecipientType.CC, new InternetAddress(cc[i]));
			}

			for (int i = 0; i < bcc.length; i++) {
				msg.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc[i]));
			}

			msg.saveChanges();

			Transport transport = session.getTransport("smtp");

			transport.connect(host, userName, passWord);

			transport.sendMessage(msg, msg.getAllRecipients());

			transport.close();

			return true;

		} catch (Exception mex) {
			return false;
		}
	}

	/**
	 * Connects to email server with credentials provided to read from a given
	 * folder of the email application
	 * 
	 * @param username Email username (e.g. janedoe@email.com)
	 * @param password Email password
	 * @param server   Email server (e.g. smtp.email.com)
	 * @param INBOX    Folder in email application to interact with
	 * @return
	 * @throws Exception
	 */
	public void outlookEmail() throws Exception {
		Properties props = System.getProperties();
		props.setProperty("mail.store.protocol", "imap");
		props.setProperty("mail.imap.ssl.enable", "true");
		props.setProperty("mail.imaps.partialfetch", "false");
		props.put("mail.mime.base64.ignoreerrors", "true");
		props.put("mail.imap.starttls.enable", "true");
		props.put("mail.smtp.auth.plain.disable", true);
		
		
		Session mailSession = Session.getInstance(props);
		mailSession.setDebug(true);
		Store store = mailSession.getStore("imap");
		store.connect("outlook.office365.com", "sarath.kavati@stgeorge.com.au", "xxxxxx");

		Folder folder = store.getFolder("INBOX");
		folder.open(Folder.READ_ONLY);

		LOGGER.info("Total Message:" + folder.getMessageCount());
		LOGGER.info("Unread Message:" + folder.getUnreadMessageCount());

		Message[] messages = folder.getMessages();
		for (Message mail : messages) {
			LOGGER.info("*********************************");
			LOGGER.info("MESSAGE : \n");
			LOGGER.info("Subject: " + mail.getSubject());
			LOGGER.info("From: " + mail.getFrom()[0]);
			LOGGER.info("To: " + mail.getAllRecipients()[0]);
			LOGGER.info("Date: " + mail.getReceivedDate());
			LOGGER.info("Size: " + mail.getSize());
			LOGGER.info("Flags: " + mail.getFlags());
			LOGGER.info("ContentType: " + mail.getContentType());
			// LOGGER.info("Body: \n" + getEmailBody(mail));
			LOGGER.info("*******************************");

		}
	}
	
	

	// *************** Below methods are for testing purpose only **********

	@Test
	public void test_email() throws Exception {
		// execute("TestResultsReport.html");
		outlookEmail();

	}

}
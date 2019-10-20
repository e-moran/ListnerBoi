package ie.eointm.listnerboi;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class PostRecordingEmail {
    public PostRecordingEmail(Config c, Show s) {
        Properties p = new Properties();
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.host", c.getSmtpServer());
        p.put("mail.smtp.port", Integer.toString(c.getSmtpPort()));
        p.put("mail.smtp.connectiontimeout", "5000");
        p.put("mail.smtp.timeout", "5000");
        p.put("mail.smtp.ssl.enable", "true");
        p.put("mail.debug", "true");

        System.out.println(c.getEmailPass());

        Session session = Session.getInstance(p,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(c.getFromEmail(), c.getEmailPass());
                    }
                }
        );

        try {
            System.out.println("Opening new message");
            MimeMessage m = new MimeMessage(session);

            m.setFrom(new InternetAddress(c.getFromEmail()));
            m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(s.getEmailAddress()));
            m.setRecipients(Message.RecipientType.CC, InternetAddress.parse(c.getCcEmail()));

            m.setSubject("TEST");
            m.setText("TEST");

            Transport.send(m);

            System.out.println("Confirmation email sent");
        } catch(Exception e) {
            System.out.println("Failed to send confirmation email. \n" + e.getMessage());
        }

    }
}

package ie.eointm.listnerboi;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class PostRecordingEmail {
    private Config c;

    public PostRecordingEmail(Config c) {
        this.c = c;
    }

    public void sendPostRecordingEmail(Show s, String filename) {
        try {
            MimeMessage m = new MimeMessage(generateSession());

            m.setFrom(new InternetAddress(c.getFromEmail()));
            for (int i=0; i<s.getEmailAddress().length; i++)
                m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(s.getEmailAddress()[i]));
            m.setRecipients(Message.RecipientType.CC, InternetAddress.parse(c.getCcEmail()));

            m.setSubject("Your Internet Radio Recording");
            m.setText("Your recording is available at "+ c.getBaseDlUrl() + URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()).replace("+", "%20") +" for the next little while.\n" +
                    "10/10 would recommend downloading it before it disappears forever.\n\n" +
                    "Your friendly neighbourhood robot,\n" +
                    "ListnerBoi"
            );

            Transport.send(m);

            System.out.println("Confirmation email sent");
        } catch(Exception e) {
            System.out.println("Failed to send confirmation email. \n" + e.getMessage());
        }
    }

    public void sendFailureEmail(Show s, Exception e) {
        try {
            MimeMessage m = new MimeMessage(generateSession());

            m.setFrom(new InternetAddress(c.getFromEmail()));
            m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(c.getCcEmail()));

            m.setSubject("Show Recording Failed");
            m.setText("Yo, shit happened and " + s.getShowName() + " wasn't recorded.\n Please use a backup or something like that.\n\n" +
                    "This is the exception to send to some IT person: " + getFullExceptionString(e) +
                    "\n\n\nYour least favourite failure of a robot,\n" +
                    "ListnerBoi"
            );

            Transport.send(m);

            System.out.println("Error email sent");
        } catch(Exception ex) {
            System.out.println("Failed to send error email. \n" + ex.getMessage());
        }
    }

    private Session generateSession() {
        Properties p = new Properties();
        p.put("mail.smtp.auth", "true");
        p.put("mail.smtp.starttls.enable", "true");
        p.put("mail.smtp.host", c.getSmtpServer());
        p.put("mail.smtp.port", Integer.toString(c.getSmtpPort()));
        p.put("mail.smtp.connectiontimeout", "10000");
        p.put("mail.smtp.timeout", "10000");
        p.put("mail.smtp.ssl.enable", "true");
        p.put("mail.debug", "true");

        return Session.getInstance(p,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(c.getFromEmail(), c.getEmailPass());
                    }
                }
        );
    }

    private String getFullExceptionString(Exception e) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);
        ps.close();
        return baos.toString();
    }
}

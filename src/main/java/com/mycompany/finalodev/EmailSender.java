package com.mycompany.finalodev;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    public static void sendEmail(String toEmail, String subject, String messageText) {
        // SMTP sunucu ayarları (Gmail örneği)
        String host = "smtp.gmail.com";
        final String username = "your_email@gmail.com"; // Gönderen e-posta
        final String password = "your_app_password";     // Gmail için uygulama şifresi

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Oturum oluştur
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // E-posta oluştur
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(messageText);

            // Gönder
            Transport.send(message);

            System.out.println("✅ E-posta başarıyla gönderildi.");
        } catch (MessagingException e) {
            System.err.println("❌ E-posta gönderilemedi: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
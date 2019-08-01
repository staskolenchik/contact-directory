package by.kolenchik.core.service.email;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

public class TestEmail {
    private static final String HOST = "smtp.gmail.com";
    private static final int PORT = 465;
    private static final boolean SSL_FLAG = true;

    public static void main(String[] args) {
        TestEmail sender = new TestEmail();
        sender.sendSimpleEmail();
    }

    private void sendSimpleEmail() {

        //String userName = "application.massaging.email@gmail.com";
        String userName = "mnopqwaszx@gmail.com";
        String password = "qwerty12345A";

        String fromAddress="mnopqwaszx@gmail.com";
        String toAddress =  "application.messaging.email@gmail.com";
        String subject = "Test Mailzz";
        String message = "Hello from Apache Mail";

        try {
            Email email = new SimpleEmail();
            email.setHostName(HOST);
            email.setSmtpPort(PORT);
            email.setAuthenticator(new DefaultAuthenticator(userName, password));
            email.setSSLOnConnect(SSL_FLAG);
            email.setFrom(fromAddress);
            email.setSubject(subject);
            email.setMsg(message);
            email.addTo(toAddress);
            email.send();
        }catch(Exception ex){
            System.out.println("Unable to send email");
            ex.printStackTrace();
        }
    }
}

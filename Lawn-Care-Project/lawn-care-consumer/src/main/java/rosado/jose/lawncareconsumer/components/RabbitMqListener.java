package rosado.jose.lawncareconsumer.components;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Component
public class RabbitMqListener {
    @RabbitListener(queues = "${RABBITMQ_QUEUE}")
    public void receiveMessage(String incoming) throws Exception {
        Map<String, String> payload = new ObjectMapper().readValue(incoming, HashMap.class);
        switch (payload.get("action")) {
            case "createUser":
                System.out.println("Creating user!");
                sendEmail(payload.get("userEmail"), "Password Changed", "Hello " + payload.get("userName") + ", Your password has been changed");
            case "changePassword":
                System.out.println("Recieved: Changing password!");
                sendEmail(payload.get("userEmail"), "Password Changed", "Hello " + payload.get("userName") + ", Your password has been changed");
                break;
            case "resetPassword":
                System.out.println("Recieved: Resetting password!");
                sendEmail(payload.get("userEmail"), "Password Reset", "Hello " + payload.get("userName") + ", Password has been set to " + payload.get("rawPassword"));
                break;
            case "createRequest":
                System.out.println("Recieved: Creating a request!");
                sendEmail(payload.get("userEmail"), "Request has been created", "Hello " + payload.get("userName") + ", The request you made has been created");
                break;
            case "claimRequest":
                System.out.println("Recieved: Claiming a request!");
                sendEmailClaimRequest(payload.get("userEmail"), payload.get("contractorEmail"), payload.get("userName"));
                break;
            case "closeRequest":
                System.out.println("Recieved: Closing a request!");
                sendEmailCloseRequest(payload.get("userEmail"), payload.get("contractorEmail"), payload.get("userName"));
                break;
            default:
                System.out.println(payload.get("action") + " is not supported");
        }
    }

    private void sendEmailClaimRequest(String userEmail, String contractorEmail, String username) throws Exception {
        sendEmail(userEmail, "USER: Your Request was accepted", "Hello " + username + ", Your request has been accepted by a service provider");
        sendEmail(contractorEmail, "CONTRACTOR: Request Accepted", "Hello " + username + ", You have accepted a request");
    }

    private void sendEmailCloseRequest(String userEmail, String contractorEmail, String username) throws Exception {
        sendEmail(userEmail, "USER: Your Request has been closed", "Your request has been completed and closed by a service provider");
        sendEmail(contractorEmail, "CONTRACTOR: Request Closed", "You have successfully closed a request");
    }

    private void sendEmail(String userEmail, String subject, String message) throws Exception {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", System.getenv("MAILHOG_HOST"));
        props.put("mail.smtp.port", "1025");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("", "");
            }
        });
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("support@lawn-care-rabbitmq", false));

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(userEmail));
        msg.setSubject(subject);
        msg.setContent(message, "text/html");
        msg.setSentDate(new Date());

        Transport.send(msg);
    }
}

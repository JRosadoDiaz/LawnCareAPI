package rosado.jose.lawncareproducer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rosado.jose.lawncareproducer.errorhandling.RequestAlreadyClaimedException;
import rosado.jose.lawncareproducer.errorhandling.RequestNotAvailableException;
import rosado.jose.lawncareproducer.errorhandling.UserNotAContractorException;
import rosado.jose.lawncareproducer.model.Request;
import rosado.jose.lawncareproducer.model.User;
import rosado.jose.lawncareproducer.repositories.RequestJpaRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class RequestService {

    private static String REQUEST_OPEN = "OPEN";
    private static String REQUEST_CLAIMED = "CLAIMED";
    private static String REQUEST_CLOSED = "CLOSED";

    @Autowired
    private RabbitMqService rabbitMqService;

    @Autowired
    RequestJpaRepository repo;

    public void createRequest(User user) {
        if(!user.isContractor()) {
            Request newRequest = new Request();
            newRequest.setTimeScheduled(LocalDate.now());
            newRequest.setUsersWithRequest(user);
            newRequest.setRequestStatus(REQUEST_OPEN);
            repo.save(newRequest);
        }
        else {
            throw new UserNotAContractorException();
        }

        Map<String, String> payload = new HashMap<>();
        payload.put("action", "createRequest");
        payload.put("userEmail",user.getEmail());

        rabbitMqService.send(payload);
    }

    public void claimRequest(Request req, String contractorEmail) {
        if(req.getRequestStatus().equals(REQUEST_OPEN)) {
            req.setRequestStatus(REQUEST_CLAIMED);
            repo.save(req);
        }
        else {
            throw new RequestAlreadyClaimedException();
        }

        Map<String, String> payload = new HashMap<>();
        payload.put("action", "claimRequest");
        payload.put("userEmail",req.getUsersWithRequest().getEmail());
        payload.put("contractorEmail", contractorEmail);

        rabbitMqService.send(payload);
    }

    public void closeRequest(Request req, String contractorEmail) {
        if(req.getRequestStatus().equals(REQUEST_CLAIMED)) {
            req.setRequestStatus(REQUEST_CLOSED);
            repo.save(req);
        }
        else {
            throw new RequestNotAvailableException();
        }

        Map<String, String> payload = new HashMap<>();
        payload.put("action", "closeRequest");
        payload.put("userEmail",req.getUsersWithRequest().getEmail());
        payload.put("contractorEmail", contractorEmail);

        rabbitMqService.send(payload);
    }
}

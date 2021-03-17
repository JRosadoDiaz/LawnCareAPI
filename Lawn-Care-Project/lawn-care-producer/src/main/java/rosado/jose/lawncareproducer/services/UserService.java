package rosado.jose.lawncareproducer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rosado.jose.lawncareproducer.configs.SecurityConfiguration;
import rosado.jose.lawncareproducer.model.User;
import rosado.jose.lawncareproducer.repositories.UserJpaRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder pE;

    @Autowired
    private RabbitMqService rabbitMqService;

    @Autowired
    UserJpaRepository repo;

    @Autowired
    private SecurityConfiguration scg;

    public User findById(int id) {
        return repo.findById(id).get();
    }

    public void createUser(User newUser) {
        newUser.setPassword(pE.encode(newUser.getPassword()));

        if(newUser.isContractor()) {
            newUser.setAuthorityString(SecurityConfiguration.AUTH_CONTRACTOR);
        }
        else {
            newUser.setAuthorityString(SecurityConfiguration.AUTH_USER);
        }

        repo.save(newUser);
        scg.addNewUser(newUser);

        Map<String, String> payload = new HashMap<>();
        payload.put("action","createUser");
        payload.put("userName", newUser.getUsername());
        payload.put("userEmail", newUser.getEmail());

        rabbitMqService.send(payload);
    }

    public void changePassword(User user, String password) throws Exception {
        user.setPassword(pE.encode(password));
        repo.save(user);

        Map<String, String> payload = new HashMap<>();
        payload.put("action", "changePassword");
        payload.put("userName", user.getUsername());
        payload.put("userEmail",user.getEmail());

        rabbitMqService.send(payload);
    }

    public void resetPassword(User user) throws Exception {
        String tempPassword = UUID.randomUUID().toString();
        System.out.println(tempPassword);

        user.setPassword(pE.encode(tempPassword));
        repo.save(user);

        Map<String, String> payload = new HashMap<>();
        payload.put("action","resetPassword");
        payload.put("userName", user.getUsername());
        payload.put("userEmail", user.getEmail());
        payload.put("rawPassword", tempPassword);

        rabbitMqService.send(payload);
    }
}

package rosado.jose.lawncareproducer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import rosado.jose.lawncareproducer.model.PasswordChangeModel;
import rosado.jose.lawncareproducer.model.User;
import rosado.jose.lawncareproducer.repositories.UserJpaRepository;
import rosado.jose.lawncareproducer.services.UserService;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/user")
public class UserRestController {
    private final static Logger logger = Logger.getLogger(UserRestController.class.getName());

    @Autowired
    private UserJpaRepository repo;

    @Autowired
    private UserService userService;

    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(code = HttpStatus.OK)
    public List<User> getAllUsers() {
        logger.info("Grabbing all users...");
        List<User> users = repo.findAll();
        logger.info("Grabbed" + users.size() + " users...");
        return users;
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.FOUND)
    @PreAuthorize("hasAuthority('ADMIN')")
    public User getUserById(@PathVariable int id) {
        return repo.findById(id).get();
    }

    @PostMapping("")
    public ResponseEntity<Object> createUser(@RequestBody User newUser) {
        System.out.println("Creating user...");
        userService.createUser(newUser);

        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/changePassword")
    @PreAuthorize("hasAnyAuthority('USER', 'CONTRACTOR', 'ADMIN')")
    public ResponseEntity<Object> changePassword(@PathVariable int id, @RequestBody PasswordChangeModel model) throws Exception {
        User user = userService.findById(id);
        userService.changePassword(user, model.newPassword);

        System.out.println("Change password set");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/{id}/resetPassword")
    @PreAuthorize("hasAnyAuthority('USER', 'CONTRACTOR', 'ADMIN')")
    public ResponseEntity<Object> resetPassword(@PathVariable int id) throws Exception {
        User user = userService.findById(id);
        if(user == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource does not exist");

        userService.resetPassword(user);
        System.out.println("Email sent!");
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}

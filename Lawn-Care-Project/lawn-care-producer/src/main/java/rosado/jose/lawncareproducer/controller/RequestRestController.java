package rosado.jose.lawncareproducer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rosado.jose.lawncareproducer.model.Request;
import rosado.jose.lawncareproducer.model.User;
import rosado.jose.lawncareproducer.repositories.RequestJpaRepository;
import rosado.jose.lawncareproducer.repositories.UserJpaRepository;
import rosado.jose.lawncareproducer.services.RequestService;

import java.util.List;

@RestController
@RequestMapping("/request")
public class RequestRestController {


    @Autowired
    private RequestJpaRepository repo;

    @Autowired
    private UserJpaRepository userRepo;

    @Autowired
    private RequestService requestService;

    @GetMapping("")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CONTRACTOR')")
    @ResponseStatus(code = HttpStatus.FOUND)
    public List<Request> getAllRequests() { return repo.findAll(); }

    @PostMapping("/create/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")//("hasAuthority('ADMIN') OR hasAuthority('USER')")
    public ResponseEntity<Object> createRequest(@PathVariable int userId) {
        User user = userRepo.findById(userId).get();
        requestService.createRequest(user);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{contractorId}/claim/{requestId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CONTRACTOR')")
    public ResponseEntity<Object> claimRequest(@PathVariable int contractorId, @PathVariable int requestId) {
        User contractor = userRepo.findById(contractorId).get();
        Request req = repo.findById(requestId).get();

        if(contractor.isContractor()) {
            requestService.claimRequest(req, contractor.getEmail());
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{contractorId}/close/{requestId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CONTRACTOR')")
    public ResponseEntity<Object> closeRequest(@PathVariable int contractorId, @PathVariable int requestId) {
        Request req = repo.findById(requestId).get();
        User user = userRepo.findById(contractorId).get();

        requestService.closeRequest(req, user.getEmail());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}

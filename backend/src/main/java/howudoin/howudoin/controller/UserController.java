package howudoin.howudoin.controller;


import howudoin.howudoin.business.abstracts.UserService;
import howudoin.howudoin.business.request.LoginRequest;
import howudoin.howudoin.business.request.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @CrossOrigin(origins = "http://localhost:19006")  // Mobil frontend URL'si
    @PostMapping("/add")
    public ResponseEntity<String> add(@RequestBody RegisterRequest registerRequest) {
        return userService.add(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }


}

package howudoin.howudoin.business.abstracts;

import howudoin.howudoin.business.request.LoginRequest;
import howudoin.howudoin.business.request.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<String> add(RegisterRequest registerRequest);

    ResponseEntity<String> login(LoginRequest loginRequest);
}

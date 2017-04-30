package rest;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    @Autowired
    private UserRepository repository;

    @RequestMapping("/user/{userId}")
    public ResponseEntity<UserResponse> user(@PathVariable Long userId) {


        if (repository.exists(userId)) {
            UserModel user = repository.findOne(userId);
            return ResponseEntity.ok(new UserResponse(user.getFirstName(), user.getLastName()));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @RequestMapping(value = "/auth", method = RequestMethod.POST)
    public ResponseEntity<String> auth(@RequestBody AuthBody body) {
        UserModel user = repository.findByLogin(body.login);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        if (user.getPassword().equals(body.pass)) {
            return ResponseEntity.ok().body("{\"token\":\"token goes here...\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"messege\":\"failed to login. check your password.\"}");
        }
    }
}

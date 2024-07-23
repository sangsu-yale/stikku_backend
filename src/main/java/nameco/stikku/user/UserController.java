package nameco.stikku.user;

import nameco.stikku.game.GameService;
import nameco.stikku.game.dto.GameResponseDto;
import nameco.stikku.responseDto.MessageResponse;
import nameco.stikku.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;
    private final GameService gameService;

    @Autowired
    public UserController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<User> getUserById(@PathVariable("user_id") Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @GetMapping("/{user_id}/game")
    public ResponseEntity<List<GameResponseDto>> getAllGameByUser(@PathVariable("user_id") Long userId) {
        return new ResponseEntity<>(gameService.getAllGameByUserId(userId), HttpStatus.OK);
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<User> updateUser(@PathVariable("user_id") Long userId, @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(userService.updateUser(userId, userDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<MessageResponse> deleteUser(@PathVariable("user_id") Long userId) {
        String deletedUserId = userService.deleteUser(userId);
        return new ResponseEntity<>(new MessageResponse("User " + deletedUserId + " deleted successfully"), HttpStatus.OK);
    }
}

package nameco.stikku.user;

import nameco.stikku.responseDto.MessageResponse;
import nameco.stikku.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{user_id}")
    public User getUserById(@PathVariable("user_id") Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping("/{user_id}")
    public User updateUser(@PathVariable("user_id") Long userId, @RequestBody UserDTO userDTO) {
        return userService.updateUser(userId, userDTO);
    }

    @DeleteMapping("/{user_id}")
    public MessageResponse deleteUser(@PathVariable("user_id") Long userId) {
        String deletedUserId = userService.deleteUser(userId);
        return new MessageResponse("User " + deletedUserId + " deleted successfully");
    }
}

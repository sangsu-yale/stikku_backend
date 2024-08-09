package nameco.stikku.user;

import jakarta.servlet.http.HttpServletRequest;
import nameco.stikku.advice.exception.AccesDeniedException;
import nameco.stikku.game.GameService;
import nameco.stikku.game.dto.GameRequestDto;
import nameco.stikku.game.dto.GameResponseDto;
import nameco.stikku.resolver.Auth;
import nameco.stikku.responseDto.MessageResponse;
import nameco.stikku.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

    @GetMapping
    public ResponseEntity<User> getUserByAccessToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if(authorization != null && authorization.startsWith("Bearer")){
            String token = authorization.substring(7);
            User user = userService.getUserByAccessToken(token);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            throw new AccesDeniedException("액세스 토큰이 없거나 유효하지 않습니다.");
        }
    }

    @GetMapping("/{user_id}/game")
    public ResponseEntity<List<GameResponseDto>> getAllGameByUser(@PathVariable("user_id") Long userId) {
        return new ResponseEntity<>(gameService.getAllGameByUserId(userId), HttpStatus.OK);
    }

    @PutMapping("/{user_id}")
    public ResponseEntity<User> updateUser(@Auth String tokenUserId, @PathVariable("user_id") Long userId, @RequestBody UserDTO userDTO) {
        if(!tokenUserId.equals(userId.toString())){
            throw new AccesDeniedException("권한이 없습니다.");
        }
        return new ResponseEntity<>(userService.updateUser(userId, userDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{user_id}")
    public ResponseEntity<MessageResponse> deleteUser(@Auth String tokenUserId, @PathVariable("user_id") Long userId) {
        if(!tokenUserId.equals(userId.toString())){
            throw new AccesDeniedException("권한이 없습니다.");
        }
        String deletedUserId = userService.deleteUser(userId);
        return new ResponseEntity<>(new MessageResponse("User " + deletedUserId + " deleted successfully"), HttpStatus.OK);
    }

    @PostMapping("/{user_id}/game/sync")
    public ResponseEntity<?> syncGame(@Auth String tokenUserId, @PathVariable("user_id") Long userId, @RequestBody List<GameRequestDto> games) {
        if (!tokenUserId.equals(userId.toString())) {
            throw new AccesDeniedException("권한이 없습니다.");
        }
        List<GameResponseDto> gameResponseDtos = gameService.syncGame(userId, games);
        return new ResponseEntity<>(gameResponseDtos, HttpStatus.CREATED);
    }
}

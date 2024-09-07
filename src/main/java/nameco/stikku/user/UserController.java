package nameco.stikku.user;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import nameco.stikku.advice.exception.AccesDeniedException;
import nameco.stikku.annotation.users.*;
import nameco.stikku.game.GameService;
import nameco.stikku.game.dto.GameResponseDto;
import nameco.stikku.game.dto.GameSyncRequestDto;
import nameco.stikku.annotation.Auth;
import nameco.stikku.responseDto.MessageResponse;
import nameco.stikku.user.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@Tag(name = "User API", description = "사용자 계정 정보")
public class UserController {

    private final UserService userService;
    private final GameService gameService;

    @Autowired
    public UserController(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @GetMapping("/{user_id}")
    @GetUserByIdOperation
    public ResponseEntity<User> getUserById(@PathVariable("user_id") Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @GetMapping
    @GetUserByAccessTokenOperation
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
    @GetAllGameByUserOperation
    public ResponseEntity<List<GameResponseDto>> getAllGameByUser(@PathVariable("user_id") Long userId) {
        return new ResponseEntity<>(gameService.getAllGameByUserId(userId), HttpStatus.OK);
    }

    @DeleteMapping("/{user_id}/game")
    @DeleteAllGameByUserOperation
    /* TODO : 토큰 확인 필요 */
    public ResponseEntity<MessageResponse> deleteAllGameByUser(@PathVariable("user_id") Long userId) {
        return new ResponseEntity<>(new MessageResponse(gameService.deleteAllGameByUser(userId)), HttpStatus.OK);
    }

    @PutMapping("/{user_id}")
    @UpdateUserOperation
    public ResponseEntity<User> updateUser(@Parameter(hidden = true) @Auth String tokenUserId, @PathVariable("user_id") Long userId, @RequestBody UserDTO userDTO) {
        if(!tokenUserId.equals(userId.toString())){
            throw new AccesDeniedException("권한이 없습니다.");
        }
        return new ResponseEntity<>(userService.updateUser(userId, userDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{user_id}")
    @DeleteUserOperation
    public ResponseEntity<MessageResponse> deleteUser(@Parameter(hidden = true) @Auth String tokenUserId, @PathVariable("user_id") Long userId) {
        if(!tokenUserId.equals(userId.toString())){
            throw new AccesDeniedException("권한이 없습니다.");
        }
        String deletedUserId = userService.deleteUser(userId);
        return new ResponseEntity<>(new MessageResponse("User " + deletedUserId + " deleted successfully"), HttpStatus.OK);
    }

    @PostMapping("/{user_id}/game/sync")
    @SyncGameOperation
    public ResponseEntity<?> syncGame(@Parameter(hidden = true) @Auth String tokenUserId, @PathVariable("user_id") Long userId,
                                    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "newTickets: 로컬에만 저장되어 있고, 아직 서버에 동기화되지 않은 티켓(id가 존재하지 않음) <br> existedTickets: 서버에 동기화된 티켓(GameResult와 GameReview가 id를 가짐) ") @RequestBody GameSyncRequestDto tickets) {
        if (!tokenUserId.equals(userId.toString())) {
            throw new AccesDeniedException("권한이 없습니다.");
        }
        List<GameResponseDto> gameResponseDtos = gameService.syncGame(userId, tickets);
        return new ResponseEntity<>(gameResponseDtos, HttpStatus.CREATED);
    }
}

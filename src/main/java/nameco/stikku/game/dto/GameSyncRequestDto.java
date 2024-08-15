package nameco.stikku.game.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GameSyncRequestDto {
    private List<GameRequestDto> newTickets;
    private List<GameResponseDto> existedTickets;
}

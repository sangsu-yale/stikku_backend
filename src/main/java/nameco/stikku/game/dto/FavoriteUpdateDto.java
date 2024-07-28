package nameco.stikku.game.dto;

import lombok.Getter;
import lombok.Setter;

public class FavoriteUpdateDto {
    private boolean isFavorite;

    public boolean getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}

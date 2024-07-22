package nameco.stikku.setting.dto;

import lombok.Getter;
import lombok.Setter;

public class SettingUpdateDto {
    private boolean isDarkMode;

    public boolean getIsDarkMode() {
        return isDarkMode;
    }

    public void setIsDarkMode(boolean darkMode) {
        isDarkMode = darkMode;
    }
}

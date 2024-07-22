package nameco.stikku.setting;

import nameco.stikku.setting.dto.SettingUpdateDto;
import nameco.stikku.user.User;
import nameco.stikku.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SettingService {

    private final UserService userService;
    private final SettingRepository settingRepository;

    @Autowired
    public SettingService(UserService userService, SettingRepository settingRepository) {
        this.userService = userService;
        this.settingRepository = settingRepository;
    }

    public Setting getSettingByUserId(Long userId) {
        User user = userService.getUserById(userId);

        Optional<Setting> settingOptional = settingRepository.getSettingByUserId(user.getId());
        if(settingOptional.isEmpty()) {
            Setting newSetting = new Setting();
            newSetting.setUserId(userId);
            newSetting.setIsDarkMode(false);
            Setting savedNewSetting = settingRepository.save(newSetting);
            return savedNewSetting;
        }
        return settingOptional.get();
    }

    public Setting updateSetting(Long userId, SettingUpdateDto settingUpdateDto) {
        User user = userService.getUserById(userId);

        Optional<Setting> settingOptional = settingRepository.getSettingByUserId(user.getId());
        Setting setting;
        if(settingOptional.isEmpty()) {
            Setting newSetting= new Setting();
            newSetting.setUserId(userId);
            newSetting.setIsDarkMode(false);
            setting = settingRepository.save(newSetting);
        } else {
            setting = settingOptional.get();
        }

        if (settingUpdateDto.getIsDarkMode() != setting.getIsDarkMode()) {
            setting.setIsDarkMode(settingUpdateDto.getIsDarkMode());
        }
        Setting savedSetting = settingRepository.save(setting);

        return savedSetting;

    }
}

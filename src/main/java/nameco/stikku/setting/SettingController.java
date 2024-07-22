package nameco.stikku.setting;

import nameco.stikku.setting.dto.SettingUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/settings")
public class SettingController {

    private final SettingService settingService;

    @Autowired
    public SettingController(SettingService settingService) {
        this.settingService = settingService;
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<Setting> getSettingByUserId(@PathVariable("user_id") Long userId) {
        return new ResponseEntity<>(settingService.getSettingByUserId(userId), HttpStatus.OK);
    }

    @PatchMapping("/{user_id}")
    public ResponseEntity<Setting> updateSetting(@PathVariable("user_id") Long userId, @RequestBody SettingUpdateDto settingUpdateDto){
        return new ResponseEntity<>(settingService.updateSetting(userId, settingUpdateDto), HttpStatus.OK);
    }

}

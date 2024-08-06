package nameco.stikku.setting;

import nameco.stikku.advice.exception.AccesDeniedException;
import nameco.stikku.resolver.Auth;
import nameco.stikku.setting.dto.SettingUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.parameters.P;
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

    @PutMapping("/{user_id}")
    public ResponseEntity<Setting> updateSetting(@Auth String tokenUserId, @PathVariable("user_id") Long userId, @RequestBody SettingUpdateDto settingUpdateDto){
        if (!tokenUserId.equals(userId.toString())) {
            throw new AccesDeniedException("권한이 없습니다.");
        }
        Setting setting = settingService.updateSetting(userId, settingUpdateDto);
        return new ResponseEntity<>(setting, HttpStatus.OK);
    }

}

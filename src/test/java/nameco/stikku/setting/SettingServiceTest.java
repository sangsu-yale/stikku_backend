package nameco.stikku.setting;

import nameco.stikku.advice.exception.UserNotFoundException;
import nameco.stikku.setting.dto.SettingUpdateDto;
import nameco.stikku.user.User;
import nameco.stikku.user.UserRepository;
import nameco.stikku.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SettingServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private SettingRepository settingRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private SettingService settingService;

    @BeforeEach
    public void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("[세팅 조회] 성공")
    void getSettingByUserId_success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        Setting setting = new Setting();
        setting.setUserId(1L);
        setting.setIsDarkMode(false);

        when(userService.getUserById(1L)).thenReturn(user);
        when(settingRepository.getSettingByUserId(user.getId())).thenReturn(Optional.of(setting));

        Setting settingResult = settingService.getSettingByUserId(user.getId());

        assertThat(settingResult).isNotNull();
        assertThat(settingResult.getUserId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("[세팅 조회] 기존에 세팅이 없는 경우")
    void getSettingByUserId_createNewSetting() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        Setting setting = new Setting();
        setting.setUserId(1L);
        setting.setIsDarkMode(false);

        when(userService.getUserById(1L)).thenReturn(user);
        when(settingRepository.getSettingByUserId(user.getId())).thenReturn(Optional.empty());
        when(settingRepository.save(any(Setting.class))).thenReturn(setting);

        Setting settingResult = settingService.getSettingByUserId(user.getId());

        assertThat(settingResult).isNotNull();
        assertThat(settingResult.getUserId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("[세팅 조회] 사용자가 존재하지 않는 경우")
    void getSettingByUserId_UserNotFound() {
        when(userService.getUserById(1L)).thenThrow(new UserNotFoundException("1"));
        assertThatThrownBy(() -> settingService.getSettingByUserId(1L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageMatching(".*id 1.*");
    }

    @Test
    @DisplayName("[세팅 수정] 성공")
    void updateSetting_success() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        Setting setting = new Setting();
        setting.setUserId(1L);
        setting.setIsDarkMode(false);

        when(userService.getUserById(1L)).thenReturn(user);
        when(settingRepository.getSettingByUserId(user.getId())).thenReturn(Optional.of(setting));
        when(settingRepository.save(any(Setting.class))).thenReturn(setting);

        SettingUpdateDto settingUpdateDto = new SettingUpdateDto();
        settingUpdateDto.setIsDarkMode(true);

        Setting settingResult = settingService.updateSetting(1L, settingUpdateDto);

        assertThat(settingResult.getUserId()).isEqualTo(user.getId());
        assertThat(settingResult.getIsDarkMode()).isTrue();

    }

    @Test
    @DisplayName("[세팅 수정] 기존에 세팅이 없는 경우")
    void updateSetting_createNewSetting() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        Setting setting = new Setting();
        setting.setUserId(1L);
        setting.setIsDarkMode(false);

        when(userService.getUserById(1L)).thenReturn(user);
        when(settingRepository.getSettingByUserId(user.getId())).thenReturn(Optional.empty());
        when(settingRepository.save(any(Setting.class))).thenReturn(setting);

        SettingUpdateDto settingUpdateDto = new SettingUpdateDto();
        settingUpdateDto.setIsDarkMode(true);

        Setting settingResult = settingService.updateSetting(1L, settingUpdateDto);

        assertThat(settingResult.getUserId()).isEqualTo(user.getId());
        assertThat(settingResult.getIsDarkMode()).isTrue();

    }

    @Test
    @DisplayName("[세팅 수정] 다크모드 해제")
    void updateSetting_darkModeOff() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testUser");
        user.setEmail("test@example.com");

        Setting setting = new Setting();
        setting.setUserId(1L);
        setting.setIsDarkMode(true);

        when(userService.getUserById(1L)).thenReturn(user);
        when(settingRepository.getSettingByUserId(user.getId())).thenReturn(Optional.empty());
        when(settingRepository.save(any(Setting.class))).thenReturn(setting);

        SettingUpdateDto settingUpdateDto = new SettingUpdateDto();
        settingUpdateDto.setIsDarkMode(false);

        Setting settingResult = settingService.updateSetting(1L, settingUpdateDto);

        assertThat(settingResult.getUserId()).isEqualTo(user.getId());
        assertThat(settingResult.getIsDarkMode()).isFalse();

    }

}
package nameco.stikku.setting;

import com.fasterxml.jackson.databind.ObjectMapper;
import nameco.stikku.setting.dto.SettingUpdateDto;
import nameco.stikku.user.User;
import nameco.stikku.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class SettingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SettingRepository settingRepository;

    @Autowired
    private SettingService settingService;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        settingRepository.deleteAll();
    }

    @Test
    @DisplayName("[설정 조회] 성공")
    void getSettingById_success() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        User savedUser = userRepository.save(user);

        Setting setting = new Setting();
        setting.setUserId(savedUser.getId());
        setting.setIsDarkMode(false);
        Setting savedSetting = settingRepository.save(setting);

        mockMvc.perform(get("/settings/" + savedUser.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(savedUser.getId().toString()))
                .andExpect(jsonPath("$.isDarkMode").value(savedSetting.getIsDarkMode()));
    }

    @Test
    @DisplayName("[설정 조회] 사용자가 없는 경우")
    void getSettingById_UserNotFound() throws Exception {

        Setting setting = new Setting();
        setting.setUserId(1L);
        setting.setIsDarkMode(false);
        Setting savedSetting = settingRepository.save(setting);

        mockMvc.perform(get("/settings/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(containsString("id 1")));
    }

    @Test
    @DisplayName("[설정 수정] 성공")
    void updateSetting_success() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        User savedUser = userRepository.save(user);

        Setting setting = new Setting();
        setting.setUserId(savedUser.getId());
        setting.setIsDarkMode(false);
        Setting savedSetting = settingRepository.save(setting);

        SettingUpdateDto settingUpdateDto = new SettingUpdateDto();
        settingUpdateDto.setIsDarkMode(true);
        String settingUpdateDtoJson = objectMapper.writeValueAsString(settingUpdateDto);

        mockMvc.perform(patch("/settings/" + savedUser.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(settingUpdateDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDarkMode").value(settingUpdateDto.getIsDarkMode()))
                .andExpect(jsonPath("$.userId").value(savedUser.getId().toString()));
    }

    @Test
    @DisplayName("[설정 수정] 성공 (다크모드 해제)")
    void updateSetting_success_darkmode_off() throws Exception {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        User savedUser = userRepository.save(user);

        Setting setting = new Setting();
        setting.setUserId(savedUser.getId());
        setting.setIsDarkMode(true);
        Setting savedSetting = settingRepository.save(setting);

        SettingUpdateDto settingUpdateDto = new SettingUpdateDto();
        settingUpdateDto.setIsDarkMode(false);
        String settingUpdateDtoJson = objectMapper.writeValueAsString(settingUpdateDto);

        mockMvc.perform(patch("/settings/" + savedUser.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(settingUpdateDtoJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isDarkMode").value(settingUpdateDto.getIsDarkMode()))
                .andExpect(jsonPath("$.userId").value(savedUser.getId().toString()));
    }
}
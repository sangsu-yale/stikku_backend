package nameco.stikku.setting;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting> getSettingByUserId(Long userId);
}

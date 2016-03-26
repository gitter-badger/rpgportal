package ru.deathman.rpgportal.common.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.deathman.rpgportal.common.domain.User;

/**
 * @author Виктор
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("update User u set u.failedAttempts = 0 where u.username = ?1")
    Integer resetFailAttempts(String username);

    User findByUsername(String username);
}

package ru.deathman.rpgportal.common.repository.api;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.deathman.rpgportal.common.domain.Role;

/**
 * @author Виктор
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}

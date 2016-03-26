package ru.deathman.rpgportal.common.domain;

import javax.persistence.*;
import java.util.Objects;

/**
 * @author Виктор
 */
@Entity
@Table(name = "rpg_role")
public class Role {

    @Transient
    public static final String ADMIN = "ADMIN";
    @Transient
    public static final String USER = "USER";

    @Id
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;

    public Role() {}

    public Role(String name) {
        this.name = name;
    }

    //region Getters/Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    //endregion

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role userRole = (Role) o;
        return Objects.equals(name, userRole.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Role{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
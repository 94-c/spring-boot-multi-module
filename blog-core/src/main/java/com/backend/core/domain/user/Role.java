package com.backend.core.domain.user;

import com.backend.core.domain.user.type.RoleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "roles")
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType type;

    public Role(Long id, RoleType type) {
        this.id = id;
        this.type = type;
    }

}

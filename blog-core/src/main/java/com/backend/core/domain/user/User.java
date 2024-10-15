package com.backend.core.domain.user;

import com.backend.core.domain.user.data.UserCreateData;
import com.backend.core.domain.user.type.UserGenderType;
import com.backend.core.domain.user.type.UserSocialType;
import com.backend.core.support.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.usertype.UserType;

import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "nick_name", nullable = false)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserGenderType gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "social", nullable = false)
    private UserSocialType social;

    private Double weight;

    private Double height;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();

    public User(String email, String password, String nickname, UserGenderType gender, UserSocialType social, Double weight, Double height, Set<Role> roles) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.gender = gender;
        this.social = social;
        this.weight = weight;
        this.height = height;
        this.roles = roles;
    }

    public static User fromCreateData(UserCreateData data, String encodedPassword) {
       return new User(data.getEmail(), encodedPassword, data.getNickname(), data.getGender(), data.getSocial(), data.getWeight(), data.getHeight(), data.getRoles());
    }

}

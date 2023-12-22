package com.example.matcha.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
@Table(name = "users")
@Entity
@Getter
@Setter
public class User implements UserDetails {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String lastname;

    @Column(unique = true)
    private String email;

    @Column(columnDefinition = "text")
    private String bio;

    @Column(length = 3000)
    private String password;

    @Column(name = "last_time_visit")
    @JsonFormat(pattern = "yyyy-mm-dd HH:mm:ss")
    private LocalDateTime lastTimeVisit;

    @Column
    @JsonFormat(pattern = "yyyy-mm-dd")
    private Date birth;

    @Column
    private String city;

    @Column
    private String photo;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", bio='" + bio + '\'' +
                ", password='" + password + '\'' +
                ", lastTimeVisit=" + lastTimeVisit +
                ", birth=" + birth +
                ", city='" + city + '\'' +
                ", photo='" + photo + '\'' +
                '}';
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    @Transient
    private Collection<? extends GrantedAuthority> authorities;


    public User(Long id, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public User() {

    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

package com.security.domain.user.entry;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name="user_credential")
public class UserCredential implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String password; // 비밀번호

    @Column(nullable = false)
    private int failedLoginAttempts = 0; // 비밀번호 오류 횟수

    @OneToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean accountLocked = false; // 계정 잠금 여부

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        //TODO 구현
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.user.isEnabled();
    }

    public void increaseFailedLoginAttempts(){
        this.failedLoginAttempts++;
        if(this.failedLoginAttempts >=5) this.accountLocked = true;
    }

    public void unlockAccount(){
        this.failedLoginAttempts = 0;
        this.accountLocked = false;
    }
}

package com.ust.onlineBookStore.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private long userId;
    private String username;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}

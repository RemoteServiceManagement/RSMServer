package com.rsm.user;

import com.rsm.role.Role;
import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@Table(name = "USER_T")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="USER_ID")
    private Long id;

    @Column(name="USERNAME")
    private String username;

    @Column(name="PASSWORD")
    private String password;

    @Column(name="ENABLED")
    private boolean enabled;

    @Column(name="CREDENTIALS_EXPIRED")
    private boolean credentialsExpired;

    @Column(name="EXPIRED")
    private boolean expired;

    @Column(name="LOCKED")
    private boolean locked;

    @ManyToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinTable(name="user_role",joinColumns = @JoinColumn(name="USER_ID",
            referencedColumnName = "USER_ID"),inverseJoinColumns = @JoinColumn(name="ROLE_ID",
            referencedColumnName = "ROLE_ID"))
    private Set<Role> roles;

}

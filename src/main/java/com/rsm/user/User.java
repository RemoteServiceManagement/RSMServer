package com.rsm.user;

import com.rsm.common.BaseEntity;
import com.rsm.role.Role;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "USER_T")
public abstract class User extends BaseEntity{

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

    @OneToOne(cascade = CascadeType.ALL)
    private UserDetails details;

    @ManyToMany(fetch = FetchType.EAGER,cascade=CascadeType.ALL)
    @JoinTable(name="user_role",joinColumns = @JoinColumn(name="USER_ID",
            referencedColumnName = "ID"),inverseJoinColumns = @JoinColumn(name="ID",
            referencedColumnName = "ID"))
    private Set<Role> roles;

}

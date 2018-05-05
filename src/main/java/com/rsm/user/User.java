package com.rsm.user;

import com.rsm.common.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "user_t")
public class User extends BaseEntity {
    private String username;
    private String password;

    @ElementCollection
    private Set<USER_ROLE> roles;

}

package com.rsm.user.service;

import com.rsm.user.User;

public interface UserService extends BaseUserService<User> {
    public User getCurrentUser();
}

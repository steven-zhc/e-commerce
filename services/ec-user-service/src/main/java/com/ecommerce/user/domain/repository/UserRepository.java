package com.ecommerce.user.domain.repository;

import com.ecommerce.user.domain.model.User;

public interface UserRepository {
    void save(User user);
}

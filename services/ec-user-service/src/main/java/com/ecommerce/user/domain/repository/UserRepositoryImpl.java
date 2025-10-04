package com.ecommerce.user.domain.repository;

import com.ecommerce.user.domain.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryImpl(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public void save(User user) {
        UserEntity entity = new UserEntity(
            user.getEmail().getValue(),
            user.getPassword(),
            user.isValidated(),
            user.getCreatedDate()
        );
        entity.setValidateDate(user.getValidateDate());
        jpaUserRepository.save(entity);
    }
}

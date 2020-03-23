package mops.portfolios.domain.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String>{
    User findOneByName(String username);
}
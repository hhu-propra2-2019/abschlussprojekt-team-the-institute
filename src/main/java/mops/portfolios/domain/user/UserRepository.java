package mops.portfolios.domain.user;

import org.springframework.data.repository.CrudRepository;

interface UserRepository extends CrudRepository<User, String>{
    User findOneByName(String username);
}
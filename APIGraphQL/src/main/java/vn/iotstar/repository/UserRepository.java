package vn.iotstar.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.iotstar.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {}

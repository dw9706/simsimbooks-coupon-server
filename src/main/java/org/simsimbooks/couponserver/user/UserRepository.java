package org.simsimbooks.couponserver.user;

import org.simsimbooks.couponserver.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}

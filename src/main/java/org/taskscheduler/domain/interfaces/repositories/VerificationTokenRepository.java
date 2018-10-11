package org.taskscheduler.domain.interfaces.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.taskscheduler.domain.entities.User;
import org.taskscheduler.domain.entities.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken getByUser(User user);
    VerificationToken getByToken(String token);
}

package hello.jwt.repository;

import hello.jwt.domain.APIUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface APIUserRepository extends JpaRepository<APIUser, String> {
}

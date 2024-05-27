package hello.jwt.repository;

import hello.jwt.domain.APIUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class APIUserRepositoryTest {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private APIUserRepository apiUserRepository;

    @Test
    void testInserts() {

        IntStream.rangeClosed(1, 100).forEach(i -> {
            APIUser apiUser = APIUser.builder()
                    .mid("apiuser"+i)
                    .mpw(passwordEncoder.encode("1111"))
                    .build();

            apiUserRepository.save(apiUser);
        });
    }

}
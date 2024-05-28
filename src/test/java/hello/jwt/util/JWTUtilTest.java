package hello.jwt.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class JWTUtilTest {

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    void testGenerate() {

        //given
        Map<String, Object> claimMap = Map.of("mid", "ABCDE");

        //when
        String jwtStr = jwtUtil.generateToken(claimMap, 1);

        //then
        log.info(jwtStr);
    }

    /**
     * 토큰 유효시간이 지나면 ExpiredJwtException 발생
     */
    @Test
    void testValidation() {

        String jwtStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTY4NTcxNzMsIm1pZCI6IkFCQ0RFIiwiaWF0IjoxNzE2ODU3MTEzfQ.xwugjs5UDR9lWhbQJZ6ZV8HJcTB8p5KZ9Q0QJ5u_SDI";

        assertThatThrownBy(() -> jwtUtil.validateToken(jwtStr))
                .isInstanceOf(ExpiredJwtException.class);

    }

    /**
     * 토큰을 고의로 수정했을 때 SignatureException 발생
     */
    @Test
    void modifyToken() {

        String jwtStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MTY4NTcxNzMsIm1pZCI6IkFCQ0RFIiwiaWF0IjoxNzE2ODU3MTEzfQ.xwugjs5UDR9lWhbQJZ6ZV8HJcTB8p5KZ9Q0QJ5u_SDIzz";

        assertThatThrownBy(() -> jwtUtil.validateToken(jwtStr))
                .isInstanceOf(SignatureException.class);
    }

    @Test
    void testAll() {

        //given
        String jwtStr = jwtUtil.generateToken(Map.of("mid", "AAAA", "email", "aaa@bbb.com"), 1);
        log.info("jwtStr: {}", jwtStr);

        //when
        Map<String, Object> claim = jwtUtil.validateToken(jwtStr);

        //then
        log.info("MID: {}", claim.get("mid"));
        log.info("email: {}", claim.get("email"));
    }
}
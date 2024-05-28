package hello.jwt.util;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class JWTUtil {

    @Value("${org.zerock.jwt.secret}")
    private String key;

    //토큰 생성
    public String generateToken(Map<String, Object> valueMap, int days) {
        log.info("generateKey..." + key);

        //header
        HashMap<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        //payload
        HashMap<String, Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);

        //test시에는 짧은 유효 기간
        int time = (60 * 24) * days; //test 분 단위로 나중에 60*24로 변경

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant()))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();

        return jwtStr;
    }

    //토큰 검증
    public Map<String, Object> validateToken(String token) throws JwtException {
        Map<String, Object> claim = null; //payload

        claim = (Map<String, Object>) Jwts.parser()
                .setSigningKey(key.getBytes())
                .parseClaimsJws(token)
                .getBody();

        return claim;
    }
}

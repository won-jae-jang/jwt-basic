package hello.jwt.security.filter;

import hello.jwt.security.exception.AccessTokenException;
import hello.jwt.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import static hello.jwt.security.exception.AccessTokenException.TOKEN_ERROR.*;

@Slf4j
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if (!path.startsWith("/api")) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Token Check Filter......................");
        log.info("JWTUtil: {}", jwtUtil);

        try {
            validateAccessToken(request);
            filterChain.doFilter(request, response);
        } catch (AccessTokenException accessTokenException) {
            accessTokenException.sendResponseError(response);
        }

    }

    private Map<String, Object> validateAccessToken(HttpServletRequest request) {
        String headerStr = request.getHeader("Authorization");

        if (headerStr == null || headerStr.length() < 8) {
            throw new AccessTokenException(UNACCEPT);
        }

        //Bearer 생략
        String tokenType = headerStr.substring(0, 6);
        String tokenStr = headerStr.substring(7);

        if (tokenType.equals("Bearer") == false) {
            throw new AccessTokenException(BADTYPE);
        }

        try {
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);
            return values;
        } catch (MalformedJwtException malformedJwtException) {
            log.error("MalformedJwtException-----------------------------");
            throw new AccessTokenException(MALFORM);
        } catch (SignatureException signatureException) {
            log.error("SignatureException-----------------------------");
            throw new AccessTokenException(BADSIGN);
        } catch (ExpiredJwtException expiredJwtException) {
            log.error("ExpiredJwtException-----------------------------");
            throw new AccessTokenException(EXPIRED);
        }
    }
}

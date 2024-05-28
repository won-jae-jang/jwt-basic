package hello.jwt.security.handler;

import com.google.gson.Gson;
import hello.jwt.util.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * 로그인 성공시 토큰 문자열 생성
 * APILoginFilter 와 연동되어야 함 (CustomSecurityConfig 에 설정)
 */
@Slf4j
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("Login Success Handler...........................");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info("{}", authentication);
        log.info("{}", authentication.getName());

        Map<String, Object> claim = Map.of("mid", authentication.getName());

        //access token 유효 기간 1일
        String accessToken = jwtUtil.generateToken(claim, 1);
        //refresh token 유효 기간 30일
        String refreshToken = jwtUtil.generateToken(claim, 30);

        Gson gson = new Gson();
        Map<String, String> keyMap = Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        String jsonStr = gson.toJson(keyMap);

        response.getWriter().println(jsonStr);
    }
}

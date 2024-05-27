package hello.jwt.security;

import hello.jwt.domain.APIUser;
import hello.jwt.dto.APIUserDTO;
import hello.jwt.repository.APIUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class APIUserDetailsService implements UserDetailsService {

    private final APIUserRepository apiUserRepository;

    /**
     * 사용자가 존재하면 dto 반환
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<APIUser> result = apiUserRepository.findById(username);

        APIUser apiUser = result.orElseThrow(() -> new UsernameNotFoundException("cannot find mid"));

        log.info("ApiUserDetailsService apiUser----------------------------------");

        APIUserDTO dto = new APIUserDTO(
                apiUser.getMid(),
                apiUser.getMpw(),
                List.of(new SimpleGrantedAuthority("ROLE_USER")));

        log.info("{}", dto);

        return dto;
    }
}

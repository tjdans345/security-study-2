package dev.study.springscurity2.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// 빈으로 관리하기 위해 @Component Annotation 적용
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader =  request.getHeader("Authorization");
        // 방법 2
//        final String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwtToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);

        // TODO extract the userEmail from JWT token ;
        userEmail = jwtService.extractUsername(jwtToken);

        // user 객체와 시큐리티 세션안에 인증 정보 객체 여부 검증
        // 시큐리티 세션 인증 객체 정보가 없으면 시큐리티 세션 객체 생성
        // -> 즉 ( 사용자가 인증 되지 않은 경우 ) 데이터베이스에서 사용자 세부정보를 가져오고 가져온 정보를 시큐리티 컨텍스트 홀더에 업데이트 시켜준다.
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // 토큰 유효성 검증
            if(jwtService.isTokenValid(jwtToken, userDetails)) {
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                authenticationToken.setDetails(
                        // 추가 할 세부 정보가 있으면 WebAuthenticationDetailsSource 을 이용하여 커스터 마이징 하면됨.
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 시큐리티 컨텍스트 홀더 ( 세션 ) 업데이트
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        }
    }
}

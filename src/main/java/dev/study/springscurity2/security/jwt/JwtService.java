package dev.study.springscurity2.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoder;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // 512 bit
    private static final String SECRET_KEY = "6E3272357538782F413F4428472D4B6150645367566B5970337336763979244226452948404D6251655468576D5A7134743777217A25432A462D4A614E645266";
    // 256 bit
//    private static final String SECRET_KEY = "2A472D4B6150645367566B59703373357638792F423F4528482B4D6251655468";

    /**
     * JwtToken -> subject 추출
     * @param jwtToken JwtToken
     * @return subject
     */
    public String extractUsername(String jwtToken) {
        return extractClaim(jwtToken, Claims::getSubject);
    }

    /**
     * 토큰에서 subject 추출
     * @param jwtToken JwtToken
     * @param claimsResolver 사용하고자 하는 Function
     * @return subject
     * @param <T> Class Generic
     */
    public <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    /**
     * 토큰 생성 함수
     * 추가 클레임 필요 없이 토큰을 생성하고 싶을 때
     * @param userDetails 유저 세부정보 ( 유저 객체 )
     * @return
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * 토큰 생성 함수
     * 추가 클레임을 필요로 하는 토큰을 생성하고 싶을 때
     * @param extractClaims 추가 클레임
     * @param userDetails 유저 세부정보 ( 유저 객체 )
     * @return Token
     */
    public String generateToken(
            Map<String, Object> extractClaims,
            UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigneInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // TODO : 여기서 부터 해야함 ( 강의 1:11:20초 부터 )
    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        final String username = extractUsername(jwtToken);

        // 토큰에 담긴 subject 유저 정보와 UserDetails 객체의 유저 정보가 같은지 검증 , 토큰 만료 여부 검증
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(jwtToken);

    }

    /**
     * 토큰 만료시간 검증 메서드
     * @param jwtToken JwtToken
     * @return 토큰 만료시간 여부
     */
    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    /**
     * 토큰 만료시간 추출 메서드
     * @param jwtToken JwtToken
     * @return 토큰 만료 시간
     */
    private Date extractExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }


    /**
     * 토큰에서 클레임 추출
     * @param jwtToken JwtToken
     * @return 클레임 정보
     */
    private Claims extractAllClaims(String jwtToken) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigneInKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    /**
     * signKey 반환 메서드
     * @return signKey
     */
    private Key getSigneInKey() {
        byte [] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }


}

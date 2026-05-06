package org.makson.tasktrackerbackend.services;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import org.makson.tasktrackerbackend.exceptions.InvalidJwtException;
import org.makson.tasktrackerbackend.exceptions.JwtClaimsException;
import org.makson.tasktrackerbackend.exceptions.JwtGenerationException;
import org.makson.tasktrackerbackend.exceptions.SecretLengthException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration-sec}")
    private Long jwtExpiration;
    private final UserService userService;

    public String generateJwt(UserDetails user) {
        try {
            String jws = generateJws(user);
            return generateJwe(jws);
        } catch (JOSEException e) {
            throw new JwtGenerationException("Error generating token", e);
        }
    }

    public UserDetails verifyToken(String jwt) throws JOSEException {
        SignedJWT jws = extractJWSFromJWE(jwt);

        if (!isValidateSignature(jws)) {
            throw new InvalidJwtException("Token is not valid");
        }

        String username = getUsernameFromToken(jws).orElseThrow(() -> new InvalidJwtException("Subject is missing in token"));
        UserDetails user = userService.loadUserByUsername(username);

        if (isTokenExpired(jws)) {
            throw new InvalidJwtException("Token has expired");
        }

        return user;
    }

    public Optional<String> getUsernameFromToken(SignedJWT jws) {
        try {
            String subject = jws.getJWTClaimsSet().getSubject();
            return Optional.ofNullable(subject);
        } catch (ParseException e) {
            throw new JwtClaimsException("Error getting subject", e);
        }
    }

    public boolean isTokenExpired(SignedJWT jws) {
        try {
            Date expirationTime = jws.getJWTClaimsSet().getExpirationTime();
            return expirationTime.before(Date.from(Instant.now()));
        } catch (ParseException e) {
            throw new JwtClaimsException("Error getting expiration date", e);
        }
    }

    private SignedJWT extractJWSFromJWE(String jwe) {
        try {
            JWEObject jweObject = JWEObject.parse(jwe);
            SecretKey secretKey = createSecretKey(secret, "AES");
            jweObject.decrypt(new DirectDecrypter(secretKey));
            return jweObject.getPayload().toSignedJWT();
        } catch (ParseException | JOSEException e) {
            throw new InvalidJwtException("Failed to decrypt JWE", e);
        }
    }

    private String generateJws(UserDetails user) throws JOSEException {
        JWSHeader jwsHeader = new JWSHeader.Builder(JWSAlgorithm.HS256).build();
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issueTime(Date.from(Instant.now()))
                .expirationTime(Date.from(Instant.now().plusSeconds(jwtExpiration)))
                .build();

        SecretKey secretKey = createSecretKey(secret, "HMACSHA256");

        SignedJWT jws = new SignedJWT(jwsHeader, claims);
        jws.sign(new MACSigner(secretKey));
        return jws.serialize();
    }

    private String generateJwe(String payload) throws JOSEException {
        JWEHeader jweHeader = new JWEHeader.Builder(JWEAlgorithm.DIR, EncryptionMethod.A256GCM).build();
        JWEObject jwe = new JWEObject(jweHeader, new Payload(payload));
        SecretKey secretKey = createSecretKey(secret, "AES");

        jwe.encrypt(new DirectEncrypter(secretKey));
        return jwe.serialize();
    }

    private SecretKey createSecretKey(String secret, String algorithm) {
        byte[] bytes = Base64.getDecoder().decode(secret);
        return new SecretKeySpec(bytes, algorithm);
    }

    private boolean isValidateSignature(SignedJWT jws) throws JOSEException {
        SecretKey secretKey = createSecretKey(secret, "HMACSHA256");
        return jws.verify(new MACVerifier(secretKey));
    }
}

package com.destroflyer.battlebuds.server;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.destroflyer.battlebuds.shared.Account;
import org.bouncycastle.openssl.PEMParser;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;

public class AuthTokenUtil {

    private static final JWTVerifier JWT_VERIFIER = createJwtVerifier();

    private static JWTVerifier createJwtVerifier() {
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) readPublicKey("/public_auth_key.pem"), null);
        return JWT.require(algorithm).build();
    }

    private static PublicKey readPublicKey(String resourcePath) {
        try {
            PEMParser pemParser = new PEMParser(new InputStreamReader(AuthTokenUtil.class.getResourceAsStream(resourcePath)));
            byte[] publicKeyBytes = pemParser.readPemObject().getContent();
            return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Account getAccount(String jwt) {
        DecodedJWT decodedJWT = JWT_VERIFIER.verify(jwt);
        Map<String, Object> user = decodedJWT.getClaim("user").asMap();
        int id = (int) user.get("id");
        String login = (String) user.get("login");
        return new Account(id, login);
    }
}

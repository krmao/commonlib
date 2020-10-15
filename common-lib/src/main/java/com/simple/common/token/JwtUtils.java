package com.simple.common.token;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.simple.common.auth.AuthConstant;
import com.simple.common.auth.Authorize;
import org.jose4j.jwk.PublicJsonWebKey;
import org.jose4j.jwk.RsaJsonWebKey;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.*;

public class JwtUtils {
    static RsaJsonWebKey jwk = null;
    static String secretString = "simple";
    static String keyId = "simple_id";
    static String issuer = "simple_issuer";
    static String jwkUse = "simple_use";
    static String publicKeyFile = "public_key.pem";
    static String privateKeyFile = "private_key.pem";


    private static String decodeBase64(String src) {
        return new String(Base64.getDecoder().decode(src));
    }

    private static byte[] decodeBase64ToBytes(String src) {
        return Base64.getDecoder().decode(src);
    }

    private static String encodeBase64(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /*
    public static RsaJsonWebKey refreshJWKCreator(String keyId) throws Exception {
        RsaJsonWebKey jwk = RsaJwkGenerator.generateJwk(2048);
        jwk.setKeyId(keyId);
        jwk.setAlgorithm(AlgorithmIdentifiers.RSA_USING_SHA256);
        return jwk;
    }

    public static RsaJsonWebKey generateJWKCreator() {
        String keyId = JwtUtils.keyId;
        try {
            if (null == JwtUtils.jwk) {
                JwtUtils.jwk = JwtUtils.refreshJWKCreator(JwtUtils.keyId);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return JwtUtils.jwk;

    }

    public static String generateJWK() throws Exception {
        RsaJsonWebKey jwk = JwtUtils.generateJWKCreator();
        String publicKey = jwk.toJson(RsaJsonWebKey.OutputControlLevel.PUBLIC_ONLY);
        String other = jwk.toJson(RsaJsonWebKey.OutputControlLevel.INCLUDE_SYMMETRIC);
        return publicKey;
    }

    public static VerificationKeys JWKs() {
        VerificationKeys keys = new VerificationKeys();
        try {
            String pubKeyJson = JwtUtils.generateJWK();
            List<VerificationKey> keyList = new ArrayList<VerificationKey>();
            VerificationKey key = JSON.parseObject(pubKeyJson, VerificationKey.class);
            keyList.add(key);
            keys.setKeys(keyList);
            return keys;
        } catch (Exception e) {
            return keys;
        }
    }

public static String createNewToken(String openId) {

        try {
            RsaJsonWebKey rsaJsonWebKey = JwtUtils.generateJWKCreator();
            RSAPublicKey publicKey = rsaJsonWebKey.getRsaPublicKey();
            RSAPrivateKey privateKey = rsaJsonWebKey.getRsaPrivateKey();
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            JWTCreator.Builder builder = JWT.create()
                    .withKeyId(JwtUtils.keyId)
                    .withIssuer("auth0")
                    .withClaim("email", "test@qq.com")
                    .withClaim("userId", openId);
            String token = builder.sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            return "none-token";
        }
    }
    public static String createToken2(String userOpenId) {

        try {
            RsaJsonWebKey rsaJsonWebKey = JwtUtils.generateJWKCreator();
            RSAPublicKey publicKey = rsaJsonWebKey.getRsaPublicKey();
            RSAPrivateKey privateKey = rsaJsonWebKey.getRsaPrivateKey();
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            String token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("email", "mail@example.com")
                    .withClaim("uid", userOpenId)
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            //Invalid Signing configuration / Couldn't convert Claims.
            return "none-token";
        }
    }

    public static String verifyTokenValues(String token) {
        try {

            RsaJsonWebKey rsaJsonWebKey = JwtUtils.generateJWKCreator();
            RSAPublicKey publicKey = rsaJsonWebKey.getRsaPublicKey();
            RSAPrivateKey privateKey = rsaJsonWebKey.getRsaPrivateKey();
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            DecodedJWT jwtor = verifier.verify(token);
            String algorithmName = jwtor.getAlgorithm();
            String keyId = jwtor.getKeyId();
            Map<String, Claim> token_claims = jwtor.getClaims();
            Claim claim = token_claims.get("email");
            System.out.println("Email: " + claim.asString());
        } catch (Exception e) {
            System.out.println("failed to decode");
            e.printStackTrace();
        }
        return token;
    }


    public static String createNewToken() throws Exception {

        JwtClaims claims = new JwtClaims();
        claims.setIssuer("auth0");  // who creates the token and signs it
        claims.setAudience("Audience"); // to whom the token is intended to be sent
        //claims.setExpirationTimeMinutesInTheFuture(10); // time when the token will expire (10 minutes from now)
        //claims.setGeneratedJwtId(); // a unique identifier for the token
        claims.setIssuedAtToNow();  // when the token was issued/created (now)
        //claims.setNotBeforeMinutesInThePast(1); // time before which the token is not yet valid (2 minutes ago)
        //claims.setSubject("subject"); // the subject/principal is whom the token is about
        claims.setClaim("email", "mailx@example.com"); // additional claims/attributes about the subject can be added
        //List<String> groups = Arrays.asList("group-one", "other-group", "group-three");
        //claims.setStringListClaim("groups", groups); // multi-valued claims work too and will end up as a JSON array
        JsonWebSignature jws = new JsonWebSignature();

        // The payload of the JWS is JSON content of the JWT Claims
        jws.setPayload(claims.toJson());


        PrivateKey privateKey = RsaUtils.getPrivateKey(JwtUtils.privateKeyFile);
        jws.setKey(privateKey);
        jws.setKeyIdHeaderValue(JwtUtils.keyId);
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);
        String jwtToken = jws.getCompactSerialization();
        System.out.println("JWT: " + jwtToken);

        return jwtToken;

    }
    */
    public static String createToken(Map<String, Object> payload) {

        try {
            JwtClaims claims = new JwtClaims();
            claims.setIssuer(JwtUtils.issuer);  // who creates the token and signs it
            //claims.setAudience("Audience"); // to whom the token is intended to be sent
            //claims.setExpirationTimeMinutesInTheFuture(10); // time when the token will expire (10 minutes from now)
            claims.setIssuedAtToNow();  // when the token was issued/created (now)
            claims.setClaim("email", "master@example.com"); // additional claims/attributes about the subject can be added
            if (null != payload) {
                Iterator iter = payload.entrySet().iterator();
                while (iter.hasNext()) {
                    Map.Entry entry = (Map.Entry) iter.next();
                    String key = (String) entry.getKey();
                    Object val = entry.getValue();
                    claims.setClaim(key, val);
                }
            }

            JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(claims.toJson());
            PrivateKey privateKey = RsaUtils.getPrivateKey(JwtUtils.privateKeyFile);
            jws.setKey(privateKey);
            jws.setKeyIdHeaderValue(JwtUtils.keyId);
            jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

            String jwt = jws.getCompactSerialization();
            System.out.println("JWT: " + jwt);
            return jwt;
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }


    }




    public static void resetRsaKeyPair() {

        try {
            RsaUtils.generateKey(JwtUtils.publicKeyFile, JwtUtils.privateKeyFile, JwtUtils.secretString, 2048);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




    public static String verifyNewToken(String token) {
        try {
            RSAPrivateKey privateKey = (RSAPrivateKey) RsaUtils.getPrivateKey(JwtUtils.privateKeyFile);
            RSAPublicKey publicKey = (RSAPublicKey) RsaUtils.getPublicKey(JwtUtils.publicKeyFile);
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(JwtUtils.issuer)
                    .build(); //Reusable verifier instance
            DecodedJWT jwtor = verifier.verify(token);
            String algorithmName = jwtor.getAlgorithm();
            String keyId = jwtor.getKeyId();
            Map<String, Claim> token_claims = jwtor.getClaims();
            Claim claim = token_claims.get("email");
            System.out.println("Email: " + claim.asString());
        } catch (Exception e) {
            System.out.println("failed to decode");
            e.printStackTrace();
        }
        return token;
    }

    public static boolean verifyToken(String token) {
        try {
            RSAPrivateKey privateKey = (RSAPrivateKey) RsaUtils.getPrivateKey(JwtUtils.privateKeyFile);
            RSAPublicKey publicKey = (RSAPublicKey) RsaUtils.getPublicKey(JwtUtils.publicKeyFile);
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(JwtUtils.issuer)
                    .build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            System.out.println("failed to verify token");
            e.printStackTrace();
            return false;
        }

    }

    public static String decodeToken(String token) {
        try {
            RSAPrivateKey privateKey = (RSAPrivateKey) RsaUtils.getPrivateKey(JwtUtils.privateKeyFile);
            RSAPublicKey publicKey = (RSAPublicKey) RsaUtils.getPublicKey(JwtUtils.publicKeyFile);
            Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(JwtUtils.issuer)
                    .build();
            verifier.verify(token);
            DecodedJWT decode = JWT.decode(token);
            return decode.getClaim(AuthConstant.AUTHORIZATION_HEADER).asString();

        } catch (Exception e) {
            System.out.println("failed to verify token");
            e.printStackTrace();
            return null;
        }

    }

    public static VerificationKeys createJWKs() {
        VerificationKeys keys = new VerificationKeys();
        try {
            PublicKey publicKey = RsaUtils.getPublicKey(JwtUtils.publicKeyFile);
            PublicJsonWebKey jwk = PublicJsonWebKey.Factory.newPublicJwk(publicKey);
            jwk.setKeyId(JwtUtils.keyId);
            jwk.setAlgorithm(AlgorithmIdentifiers.RSA_USING_SHA256);
            jwk.setUse(JwtUtils.jwkUse);
            String jwkJson = jwk.toJson();
            List<VerificationKey> keyList = new ArrayList<VerificationKey>();
            VerificationKey key = JSON.parseObject(jwkJson, VerificationKey.class);
            keyList.add(key);
            keys.setKeys(keyList);
            return keys;
        } catch (Exception e) {
            return keys;
        }
    }

}


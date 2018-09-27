package com.xuecheng.auth;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: szz
 * @Date: 2018/9/24 下午7:02
 * @Version 1.0
 */
public class TestJwt {
    @Test
    public void testCreateJwt() {
        //秘钥库地址,基于classpath
        String keystore_location = "xc.keystore2";
        //秘钥库密码
        String keystore_password = "xuechengkeystore";
        //生成jwt令牌
        //CharSequence content 令牌内容,Signer singner签名

        //加密秘钥库
        //Resource resource,char[] password
        ClassPathResource classPathResource = new ClassPathResource(keystore_location);
        //秘钥库
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(classPathResource, keystore_password.toCharArray());


        //取出秘钥,指定秘钥的密码
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("xckey", "xuecheng".toCharArray());
        //取出私钥
        PrivateKey aPrivate = keyPair.getPrivate();

        //创建签名对象,采用RSA算法
        RsaSigner rsaSigner = new RsaSigner((RSAPrivateKey) aPrivate);

        //定义payload信息
        Map<String, Object> tokenMap = new HashMap<>();
        tokenMap.put("id", "123");
        tokenMap.put("name", "itcast");
        tokenMap.put("roles", "r01,r02");
        tokenMap.put("ext", "1");
        String toJSONString = JSON.toJSONString(tokenMap);

        Jwt jwt = JwtHelper.encode(toJSONString, rsaSigner);
        //取出jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);

    }

    @Test
    public void testVerifyJwt(){
        //jwt令牌
        //String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHQiOiIxIiwicm9sZXMiOiJyMDEscjAyIiwibmFtZSI6Iml0Y2FzdCIsImlkIjoiMTIzIn0.HnjWjEX4Wk2NL1zcAMsxbvuwiPpyM4UOIPLBk6N3l1VTtOxLcpdV3ht4I668ljmGyQDezb6MVEVx4LqjME7YhNjsm1yrry-TWsn3IO274e5YBkRAPn02NQ9A-oy0CTe37IymuVSILpIcD85FC0adM9kg_cSICM9uLcfYJdXd4pvUM3W-ifRrRP-AE-kBl-f2oAdyJDWqGyXEMC1tM-S8YLdb383kCgFrX5XxU2bJGl2nSMOXs7KpRCPycfYgDj3UOu3UjEwqJOnIDJYCL9VmcG-lL5L_6XjXl5MsnoJ2Dtog3muOfj4RuzXGtLTViDOJElCGpkkNuQxKwHHPXZaFAA";
        //String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOiIxIiwidXNlcnBpYyI6bnVsbCwidXNlcl9uYW1lIjoiaXRjYXN0Iiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiJ0ZXN0MDIiLCJ1dHlwZSI6IjEwMTAwMiIsImlkIjoiNDkiLCJleHAiOjE1Mzc5ODIzNDcsImF1dGhvcml0aWVzIjpbImNvdXJzZV9maW5kX2xpc3QiLCJjb3Vyc2VwaWNfZmluZF9saXN0Il0sImp0aSI6IjcyOWVhMDZiLWRkYmYtNGY0ZS1iZGUwLWYwZTk5OGNkMzVjNyIsImNsaWVudF9pZCI6IlhjV2ViQXBwIn0.V2x1KfuDPhsEAGEE8w9iRjB1NkYCYiFYTg_3-vZ-KwB5ncWQ73LFWKzlRuBUHS33wqZRbWnIa6bynPRAI7RuNgLIe-Bw9t8Sn-pUNzZynHe8vpEd0TRGrYGknECKkclKXCV6RtiWSs-SOYaI5ym4r-gOJcBbw1XhhSjoWxA8YjWjcYK9aj0yGoOE5EEee_S_mrbxo580etQx_7tiTLKK51ArRr4p0VzZ3Q_wDo9nS7ZBlKvEg2IY4g0_gqEiACVaqeLHNLBPvdysv8fSjWOJ4cH2_LMegVksUrbi2he8h0Z6RLIDM_ZEInGniafJtkQzxF66I9luyHvGkkDMsKjZeQ";
        String token = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJjb21wYW55SWQiOiIxIiwidXNlcnBpYyI6bnVsbCwidXNlcl9uYW1lIjoiaXRjYXN0Iiwic2NvcGUiOlsiYXBwIl0sIm5hbWUiOiJ0ZXN0MDIiLCJ1dHlwZSI6IjEwMTAwMiIsImlkIjoiNDkiLCJleHAiOjE1MzgwMDk4NTMsImF1dGhvcml0aWVzIjpbInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYmFzZSIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfZGVsIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9saXN0IiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZV9wbGFuIiwieGNfdGVhY2htYW5hZ2VyX2NvdXJzZSIsImNvdXJzZV9maW5kX2xpc3QiLCJ4Y190ZWFjaG1hbmFnZXIiLCJ4Y190ZWFjaG1hbmFnZXJfY291cnNlX21hcmtldCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfcHVibGlzaCIsInhjX3RlYWNobWFuYWdlcl9jb3Vyc2VfYWRkIl0sImp0aSI6IjA1Y2EzZTczLWU3OTYtNGZkMi04YjYzLTJlNTlmNTBlZDRkMSIsImNsaWVudF9pZCI6IlhjV2ViQXBwIn0.bSTmDcrIn84EDikX5VHGINYAn3XfNa8ACADHmChb6QjikzdGu4nYtMDq-SgUV1jxO5rxfBWfwzMkgrdb6yyNo0cMwSNmA5wyDsQlWZYLodyt7IWxZXX3stLt_6XxHJHJIbVhPcvSflSy_rgNhLQyoVm9QRmeaRd5z4EoVUhv72yz-JgGYDtGXL1WQZI-oZKXenPLWycKciOtpn_7lY0I1qHtHqk0aB0mPcNE9LOHbd1GXlSrsStsb0DAtiEO2Denn2b_o6WD-9eYr6odpm_seMOkjETFR5TZ0cdkcRn4wv4sYRi1WuU_UR2pHK9p85d3vWmKa6gE1JnXDfQPpfg8eg";

        //公钥
        //String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAl4Hli20sLJwSaj9Mo5dwSBWph6x6QtCXZVc+i7bq5wpqqPObqfc5nSoZ19JAw2LmfxOdgXbpXtE0AVi7vhMNnz21S9pUdADJ4FTrTlm63UOPLssnDpvwuSLzJBvzwBoRgBL/g9rH4PoCbnOeEW6Uo0AvoSriIBubPq9MwUdP04iXq/1e6KZebQMPmyiHR4uRn40kKdC44NcIZjBCc9wh69gd0tTAmTqCW4YZv60ktv9PSMyx1m9kCPhdbleCA0SPxiZ/VEh6U3C7imn6b0kNiBoLH2RDNuO1oo4YthC9qxZWeueZyUWksqBR+2qcybxYrdfK3jwc8vqcsenL1yDxzQIDAQAB-----END PUBLIC KEY-----";
        String publicKey = "-----BEGIN PUBLIC KEY-----MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnASXh9oSvLRLxk901HANYM6KcYMzX8vFPnH/To2R+SrUVw1O9rEX6m1+rIaMzrEKPm12qPjVq3HMXDbRdUaJEXsB7NgGrAhepYAdJnYMizdltLdGsbfyjITUCOvzZ/QgM1M4INPMD+Ce859xse06jnOkCUzinZmasxrmgNV3Db1GtpyHIiGVUY0lSO1Frr9m5dpemylaT0BV3UwTQWVW9ljm6yR3dBncOdDENumT5tGbaDVyClV0FEB1XdSKd7VjiDCDbUAUbDTG1fm3K9sx7kO1uMGElbXLgMfboJ963HEJcU01km7BmFntqI5liyKheX+HBUCD4zbYNPw236U+7QIDAQAB-----END PUBLIC KEY-----";

        //创建rsa签名校验对象
        RsaVerifier rsaVerifier = new RsaVerifier(publicKey);

        //校验jwt令牌合法
        Jwt jwt = JwtHelper.decodeAndVerify(token, rsaVerifier);
        String claims = jwt.getClaims();
        System.out.println(claims);

        //jwt令牌
        String encoded = jwt.getEncoded();
        System.out.println(encoded);
    }

}


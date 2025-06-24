package org.example.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.example.annotation.Decrypt;
import org.example.annotation.Encrypt;
import org.example.util.AESUtil;
import org.example.util.RSAUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

@Aspect
@Component
public class EncryptAspect {

    @Around("@annotation(decrypt)")
    public Object doDecrypt(ProceedingJoinPoint pjp, Decrypt decrypt) throws Throwable {
        Object[] args = pjp.getArgs();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        Parameter[] parameters = method.getParameters();

        for (int i = 0; i < parameters.length; i++) {
            if (args[i] instanceof String) {
                args[i] = decryptValue((String) args[i], decrypt.algorithm());
            } else if (parameters[i].isAnnotationPresent(RequestBody.class)) {
                String json = new ObjectMapper().writeValueAsString(args[i]);
                String decryptedJson = decryptValue(json, decrypt.algorithm());
                args[i] = new ObjectMapper().readValue(decryptedJson, args[i].getClass());
            }
        }
        return pjp.proceed(args);
    }

    @Around("@annotation(encrypt)")
    public Object doEncrypt(ProceedingJoinPoint pjp, Encrypt encrypt) throws Throwable {
        Object result = pjp.proceed();
        String json = new ObjectMapper().writeValueAsString(result);
        return encryptValue(json, encrypt.algorithm());
    }

    private String encryptValue(String value, String algorithm) throws Exception {
        return "RSA".equalsIgnoreCase(algorithm) ? RSAUtil.encrypt(value) : AESUtil.encrypt(value);
    }

    private String decryptValue(String value, String algorithm) throws Exception {
        return "RSA".equalsIgnoreCase(algorithm) ? RSAUtil.decrypt(value) : AESUtil.decrypt(value);
    }
}

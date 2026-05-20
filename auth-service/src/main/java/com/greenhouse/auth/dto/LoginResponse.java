package com.greenhouse.auth.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "success",
    "message",
    "token"
})
@XmlRootElement(name = "LoginResponse", namespace = "http://auth-service.dev/soap/auth")
public class LoginResponse {

    private static final String NAMESPACE = "http://auth-service.dev/soap/auth";

    @XmlElement(required = true, namespace = NAMESPACE)
    private boolean success;

    @XmlElement(namespace = NAMESPACE)
    private String message;

    @XmlElement(namespace = NAMESPACE)
    private String token;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

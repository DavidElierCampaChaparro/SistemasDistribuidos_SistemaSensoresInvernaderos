package com.greenhouse.auth.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RegisterResponse", namespace = "http://auth-service.dev/soap/auth")
public class RegisterResponse {
    private static final String NAMESPACE = "http://auth-service.dev/soap/auth";

    @XmlElement(namespace = NAMESPACE)
    private boolean success;
    @XmlElement(namespace = NAMESPACE)
    private String message;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
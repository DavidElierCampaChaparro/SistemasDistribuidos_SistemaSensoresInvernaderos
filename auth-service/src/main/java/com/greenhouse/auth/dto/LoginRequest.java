package com.greenhouse.auth.dto;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "email",
        "password"
})
@XmlRootElement(name = "LoginRequest", namespace = "http://auth-service.dev/soap/auth")
public class LoginRequest {

    private static final String NAMESPACE = "http://auth-service.dev/soap/auth";

    @XmlElement(required = true, namespace = NAMESPACE)
    private String email;

    @XmlElement(required = true, namespace = NAMESPACE)
    private String password;

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
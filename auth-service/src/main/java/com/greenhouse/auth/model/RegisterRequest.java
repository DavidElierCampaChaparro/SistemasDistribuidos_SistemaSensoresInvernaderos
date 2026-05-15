package com.greenhouse.auth.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "RegisterRequest", namespace = "http://auth-service.dev/soap/auth")
public class RegisterRequest {
    private static final String NAMESPACE = "http://auth-service.dev/soap/auth";

    @XmlElement(required = true, namespace = NAMESPACE)
    private String name;
    @XmlElement(required = true, namespace = NAMESPACE)
    private String lastname;
    @XmlElement(required = true, namespace = NAMESPACE)
    private String email;
    @XmlElement(required = true, namespace = NAMESPACE)
    private String password;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
package com.greenhouse.auth.dto;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"id", "name", "lastname", "email"})
@XmlRootElement(name = "UpdateOwnerRequest", namespace = "http://auth-service.dev/soap/auth")
public class UpdateOwnerRequest {

    private static final String NAMESPACE = "http://auth-service.dev/soap/auth";

    @XmlElement(required = true, namespace = NAMESPACE)
    private Long id;
    @XmlElement(required = true, namespace = NAMESPACE)
    private String name;
    @XmlElement(required = true, namespace = NAMESPACE)
    private String lastname;
    @XmlElement(required = true, namespace = NAMESPACE)
    private String email;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLastname() { return lastname; }
    public void setLastname(String lastname) { this.lastname = lastname; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}


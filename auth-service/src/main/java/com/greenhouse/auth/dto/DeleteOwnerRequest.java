package com.greenhouse.auth.dto;


import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"id"})
@XmlRootElement(name = "DeleteOwnerRequest", namespace = "http://auth-service.dev/soap/auth")
public class DeleteOwnerRequest {

    private static final String NAMESPACE = "http://auth-service.dev/soap/auth";

    @XmlElement(required = true, namespace = NAMESPACE)
    private Long id;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
}
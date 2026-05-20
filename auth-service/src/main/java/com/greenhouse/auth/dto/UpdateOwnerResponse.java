package com.greenhouse.auth.dto;

import jakarta.xml.bind.annotation.*;
import lombok.Data;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"success", "message"})
@XmlRootElement(name = "UpdateOwnerResponse", namespace = "http://auth-service.dev/soap/auth")
public class UpdateOwnerResponse {

    private static final String NAMESPACE = "http://auth-service.dev/soap/auth";

    @XmlElement(required = true, namespace = NAMESPACE)
    private boolean success;
    @XmlElement(namespace = NAMESPACE)
    private String message;

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
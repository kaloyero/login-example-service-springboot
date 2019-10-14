package com.example.springsocial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {

    private int id;
    private String name;
    private String mail;
    private String avatar;
    private String metadata;
    private List<String> role;
    private EnabledType enabled;
    private String userAction;
}


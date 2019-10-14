package com.example.springsocial.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"id","createdBy","enabledDate","lastUpdateBy","lastUpdateDate"})
public class User {

    private int id;
    private String name;
    private String mail;
    private String avatar;
    private String metadata;
    private String enabled;
    private String createdBy;
    private Date enabledDate;
    private String lastUpdateBy;
    private List<String> role;
    private Date lastUpdateDate;

}

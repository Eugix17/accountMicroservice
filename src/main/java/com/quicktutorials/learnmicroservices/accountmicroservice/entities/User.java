package com.quicktutorials.learnmicroservices.accountmicroservice.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@AllArgsConstructor @NoArgsConstructor
public class User {

    @Id
    @Column(name="ID")
    @NotEmpty @NotBlank @NotNull
    @Getter @Setter
    private String id;

    @Column(name="USERNAME")
    @NotEmpty @NotBlank @NotNull
    @Getter @Setter
    private String username;

    @Column(name="PASSWORD")
    @NotEmpty @NotBlank @NotNull
    @Getter @Setter
    private String password;

    @Column(name="PERMISSION")
    @NotEmpty @NotBlank @NotNull
    @Getter @Setter
    private String permission;
    
}
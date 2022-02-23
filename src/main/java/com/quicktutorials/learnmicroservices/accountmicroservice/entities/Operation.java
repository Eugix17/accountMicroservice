package com.quicktutorials.learnmicroservices.accountmicroservice.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="operations")
@AllArgsConstructor @NoArgsConstructor
public class Operation {

    @Id
    @Column(name="ID")
    @NotEmpty @NotBlank @NotNull
    @Getter @Setter
    private String id;

    @Column(name="DATE")
    @Getter @Setter
    private Date date;

    @Column(name="DESCRIPTION")
    @Getter @Setter
    private String description;

    @Column(name="VALUE")
    @NotNull
    @Getter @Setter
    private Double value;

    @Column(name="FK_ACCOUNT1")
    @NotEmpty @NotBlank @NotNull
    @Getter @Setter
    private String fkAccount1;

    @Column(name="FK_ACCOUNT2")
    @Getter @Setter
    private String fkAccount2;

    @PrePersist
    void getTimeOperation() {
        this.date = new Date();
    }

}
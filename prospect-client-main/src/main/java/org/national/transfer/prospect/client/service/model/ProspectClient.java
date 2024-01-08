package org.national.transfer.prospect.client.service.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "prospect_client")
public class ProspectClient extends AuditModel {

    @Id
    private String identityNumber;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(nullable = false, length = 50)
    private String lastname;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(nullable = false, length = 50)
    private String firstname;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Column(nullable = false, length = 100, unique = true)
    private String email;
    @Column(nullable = false)
    private String gender;
    @Column(nullable = false)
    private String identityType;
    @Column(nullable = false)
    private String birthdayDate;
    @Column(nullable = false)
    private String occupation;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    @Column(nullable = false)
    private String nationality;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private String emissionCountry;
}

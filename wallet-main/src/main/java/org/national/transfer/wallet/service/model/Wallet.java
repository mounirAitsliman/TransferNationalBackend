package org.national.transfer.wallet.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Wallet {

    @Id
    private String identityNumber;
    private String lastname;
    private String firstname;
    private String gender;
    private String identityType;
    private String birthdayDate;
    private String creationDate;
    private String lastUpdate;
    private String occupation;
    private String address;
    private String city;
    private String phoneNumber;
    private String status;
    private String role;
    private Double amount;
    private String nationality;
    private String country;
    private String email;
}

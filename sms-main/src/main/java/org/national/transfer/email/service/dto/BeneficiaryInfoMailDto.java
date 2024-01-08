package org.national.transfer.email.service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class BeneficiaryInfoMailDto {

    private String reference;
    private String beneficiaryEmail;
    private String beneficiaryLastName;
    private String beneficiaryFirstName;
}

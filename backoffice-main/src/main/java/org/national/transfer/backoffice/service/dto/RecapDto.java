package org.national.transfer.backoffice.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RecapDto {

    private String agentId;
    private String clientIdentityNumber;
    private String clientFullName;
    private String creationDate;
    private Double amount;
    private String beneficiaryFirstName;
    private String beneficiaryLastName;
}

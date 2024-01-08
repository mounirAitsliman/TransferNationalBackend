package org.national.transfer.emission.service.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Transfers {

    @Id
    private String reference;
    private String pinCode;
    private String agentId;
    private String canalId;
    private String clientIdentityNumber;
    private String clientFullName;
    private String clientEmail;
    private String beneficiaryLastName;
    private String status;
    private String beneficiaryFirstName;
    private String beneficiaryEmail;
    private Boolean withNotification;
    private Double amount;
    private Double totalAmount;
    private Double feeAmount;
    private Double totalTransactionAmountForOneYear;
    private Boolean firstTransaction;
    private String validityDate;
    private String dateForOneYear;
    private String emissionType;
    private String creationDate;
}

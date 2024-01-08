package org.national.transfer.emission.service.collection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TransferHistoric {

    private String clientIdentityNumber;
    private String clientFullName;
    private String beneficiaryLastName;
    private String status;
    private String beneficiaryFirstName;
    private String beneficiaryEmail;
    private Double amount;
    private Double totalAmount;
    private Double feeAmount;
}

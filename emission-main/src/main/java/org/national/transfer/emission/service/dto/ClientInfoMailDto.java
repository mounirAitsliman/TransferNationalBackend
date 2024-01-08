package org.national.transfer.emission.service.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ClientInfoMailDto {

    private String reference;
    private String clientEmail;
    private String pinCode;
    private String clientFullName;
}

package org.national.transfer.prospect.client.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.prospect.client.service.exception.NotFoundException;
import org.national.transfer.prospect.client.service.model.Beneficiaries;
import org.national.transfer.prospect.client.service.model.ProspectClient;
import org.national.transfer.prospect.client.service.rep.BeneficiariesRepository;
import org.national.transfer.prospect.client.service.rep.ProspectClientRepository;
import org.national.transfer.prospect.client.service.utils.LoggingUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional
@Service
@CommonsLog
@RequiredArgsConstructor
@SuppressWarnings({"unchecked", "rawtypes"})
public class ProspectClientService {

    private final ProspectClientRepository prospectClientRepository;
    private final BeneficiariesRepository beneficiariesRepository;

    public ProspectClient addProspectClient(ProspectClient prospectClient) {

        log.info(LoggingUtils.getStartMessage(prospectClient));

        ProspectClient prospect = new ProspectClient(prospectClient.getIdentityNumber(), prospectClient.getLastname(),
                prospectClient.getFirstname(),
                prospectClient.getEmail(), prospectClient.getGender(), prospectClient.getIdentityType(),
                prospectClient.getBirthdayDate(), prospectClient.getOccupation(), prospectClient.getAddress(),
                prospectClient.getCity(), prospectClient.getPhoneNumber(),
                prospectClient.getNationality(), prospectClient.getCountry(), prospectClient.getEmissionCountry());

        log.debug(LoggingUtils.getEndMessage(prospect));
        return prospectClientRepository.save(prospect);
    }

    public Beneficiaries addBeneficiary(String identityNumber, Beneficiaries beneficiary) {
        log.info(LoggingUtils.getMessage(identityNumber, beneficiary));
        return prospectClientRepository.findById(identityNumber).map(prospect -> {
            beneficiary.setProspectClient(prospect);
            return beneficiariesRepository.save(beneficiary);
        }).orElseThrow(() -> new NotFoundException("PROSPECT_NOT_FOUND"));
    }

    public ProspectClient getProspect(String identityNumber) {
        log.info(LoggingUtils.getMessage(identityNumber));
        return prospectClientRepository.findById(identityNumber).orElseThrow(() -> new NotFoundException("PROSPECT_NOT_FOUND"));
    }

    public Set<Beneficiaries> getAllBeneficiariesByProspectClientId(String identityNumber) {
        log.info(LoggingUtils.getStartMessage(identityNumber));
        List<Beneficiaries> list = beneficiariesRepository.findAll();
        return list.stream().filter(beneficiary -> beneficiary.getProspectClient().getIdentityNumber().equals(identityNumber)
        ).collect(Collectors.toSet());
    }


    public void deleteBeneficiary(String beneficiaryEmail) {
        Beneficiaries beneficiaries = beneficiariesRepository.findById(beneficiaryEmail).get();
        if (ObjectUtils.isEmpty(beneficiaries)) {
            throw new NotFoundException("BENEFICIARY_NOT_FOUND");
        }

        beneficiariesRepository.deleteById(beneficiaryEmail);
    }
}

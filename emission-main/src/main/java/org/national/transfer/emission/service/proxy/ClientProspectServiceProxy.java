package org.national.transfer.emission.service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.Set;

@FeignClient(name = "prospect-client-service",url = "prospect-client.national-transfer.svc.cluster.local:8083")
public interface ClientProspectServiceProxy {

    @GetMapping("/ws-prospect/beneficiary/{identityNumber}")
    Set<Map<String, Object>> getAllBeneficiariesByProspectClientId(@PathVariable String identityNumber);

    @PostMapping("/ws-prospect/beneficiary/{identityNumber}")
    void addBeneficiary(@PathVariable String identityNumber, @RequestBody Map beneficiary);

    @PostMapping("/ws-prospect/prospect")
    Map<String, Object> addProspectClient(@RequestBody Map<String, Object> prospectClient);

    @GetMapping("/ws-prospect/prospect/{identityNumber}")
    Map<String, Object> getProspect(@PathVariable String identityNumber);
}

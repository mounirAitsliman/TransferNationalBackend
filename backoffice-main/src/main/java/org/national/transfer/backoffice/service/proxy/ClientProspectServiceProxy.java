package org.national.transfer.backoffice.service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(name = "prospect-client-service",url = "prospect-client.national-transfer.svc.cluster.local:8083")
public interface ClientProspectServiceProxy {

    @PostMapping("/ws-prospect/prospect")
    Map<String, Object> addProspectClient(@RequestBody Map<String, Object> prospectClient);
}

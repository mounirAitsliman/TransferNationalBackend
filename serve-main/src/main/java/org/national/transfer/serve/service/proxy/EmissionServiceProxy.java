package org.national.transfer.serve.service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Map;

@FeignClient(name = "emission-service",url="emission.national-transfer.svc.cluster.local:8082")
public interface EmissionServiceProxy {

    @GetMapping("/ws-emission/cash/getTransfer/{reference}")
    Map<String, Object> getTransfer(@PathVariable String reference);

    @PutMapping("/ws-emission/updateTransferStatus/{status}/{reference}")
    Object updateTransferStatus(@PathVariable String status, @PathVariable String reference);
}

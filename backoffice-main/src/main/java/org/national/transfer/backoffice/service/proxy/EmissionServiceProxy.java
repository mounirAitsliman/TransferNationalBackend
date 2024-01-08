package org.national.transfer.backoffice.service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Map;

@FeignClient(name = "emission-service",url="emission.national-transfer.svc.cluster.local:8082")
public interface EmissionServiceProxy {

    @GetMapping("/ws-emission/cash/getTransfer/{reference}")
    Map<String, Object> getTransfer(@PathVariable String reference);

    @PutMapping("/ws-emission/cash/updateTransferStatus/{status}/{reference}")
    void updateTransferStatus(@PathVariable String status, @PathVariable String reference);

    @GetMapping("/ws-emission/cash/getAllToServeTransfers")
    List<Map> getAllToServeTransfers();

    @GetMapping("/ws-emission/cash/getAllLockedTransfers")
    List<Map> getAllLockedTransfers();

    @GetMapping("/ws-emission/getAllTransfers")
    List<Map<String, Object>> getAllTransfers();
}

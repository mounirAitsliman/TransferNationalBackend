package org.national.transfer.emission.service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@SuppressWarnings({"unchecked", "rawtypes"})
@FeignClient(name = "wallet-service",url = "wallet.national-transfer.svc.cluster.local:8081")
public interface WalletServiceProxy {

    @GetMapping("/ws-wallet/amount/{identityNumber}")
    Double getWalletAmount(@PathVariable String identityNumber);

    @PutMapping("/ws-wallet/{identityNumber}")
    Object updateWalletAmount(@RequestParam Double newAmount, @PathVariable String identityNumber);
}

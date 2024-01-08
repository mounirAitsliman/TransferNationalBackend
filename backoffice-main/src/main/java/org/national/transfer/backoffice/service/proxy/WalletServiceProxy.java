package org.national.transfer.backoffice.service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "wallet-service",url = "wallet.national-transfer.svc.cluster.local:8081")
public interface WalletServiceProxy {

    @PutMapping("/ws-wallet/{identityNumber}")
    Object updateWalletAmount(@RequestParam Double newAmount, @PathVariable String identityNumber);

    @PutMapping("/ws-wallet/addAmount/{identityNumber}")
    Object addAmountToWallet(@RequestParam Double newAmount, @PathVariable String identityNumber);
}

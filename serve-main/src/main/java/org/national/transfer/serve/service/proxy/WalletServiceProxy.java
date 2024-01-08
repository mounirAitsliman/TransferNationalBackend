package org.national.transfer.serve.service.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(name = "wallet-service",url = "wallet.national-transfer.svc.cluster.local:8081")
public interface WalletServiceProxy {

    @PutMapping("/ws-wallet/{identityNumber}")
    Object updateWalletAmount(@RequestParam Double newAmount, @PathVariable String identityNumber);

    @PostMapping("/ws-wallet/addWallet")
    Map<String, Object> addWallet(@RequestBody Map<String, Object> wallet);

    @GetMapping("/ws-wallet/{identityNumber}")
    Map<String, Object> getWallet(@PathVariable String identityNumber);

    @PutMapping("/ws-wallet/addAmount/{identityNumber}")
    Object addAmountToWallet(@RequestParam Double newAmount, @PathVariable String identityNumber);
}

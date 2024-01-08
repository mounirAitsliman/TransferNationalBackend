package org.national.transfer.wallet.service.restcontroller;

import lombok.AllArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.national.transfer.wallet.service.model.Wallet;
import org.national.transfer.wallet.service.service.WalletService;
import org.national.transfer.wallet.service.utils.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
@CommonsLog
public class WalletRestController {

    private WalletService walletService;

    @GetMapping("/{identityNumber}")
    public ResponseEntity<Wallet> getWallet(@PathVariable String identityNumber){
        log.info(LoggingUtils.getStartMessage(identityNumber));
        Wallet wallet = walletService.getWallet(identityNumber);
        log.debug(LoggingUtils.getEndMessage(wallet));
        return ResponseEntity.status(HttpStatus.OK).body(wallet);
    }

    @GetMapping("/amount/{identityNumber}")
    public ResponseEntity<Double> getWalletAmount(@PathVariable String identityNumber){
        log.info(LoggingUtils.getStartMessage(identityNumber));
        Double walletAmount = walletService.getWalletAmount(identityNumber);
        log.debug(LoggingUtils.getEndMessage(walletAmount));
        return ResponseEntity.status(HttpStatus.OK).body(walletAmount);
    }

    @PutMapping("/{identityNumber}")
    public ResponseEntity<Object> updateWalletAmount(@RequestParam Double newAmount,@PathVariable String identityNumber){
        log.info(LoggingUtils.getStartMessage(identityNumber,newAmount));
        String response = walletService.updateWalletAmount(newAmount,identityNumber);
        log.debug(LoggingUtils.getEndMessage(response+"for"+identityNumber));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/addAmount/{identityNumber}")
    public ResponseEntity<Object> addAmountToWallet(@RequestParam Double newAmount,@PathVariable String identityNumber){
        log.info(LoggingUtils.getStartMessage(identityNumber,newAmount));
        String response = walletService.addAmountToWallet(newAmount,identityNumber);
        log.debug(LoggingUtils.getEndMessage(response+"for"+identityNumber));
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PutMapping("/{identityNumber}/status")
    public ResponseEntity<Wallet> updateWalletStatus(@PathVariable String identityNumber){
        log.info(LoggingUtils.getStartMessage(identityNumber));
        Wallet wallet = walletService.updateWalletStatus(identityNumber);
        log.debug(LoggingUtils.getEndMessage(wallet));
        return ResponseEntity.status(HttpStatus.OK).body(wallet);
    }

    @PostMapping("/addWallet")
    public ResponseEntity<Wallet> addWallet(@RequestBody Wallet wallet){
        log.info(LoggingUtils.getStartMessage(wallet));
        Wallet result = walletService.addWallet(wallet);
        log.debug(LoggingUtils.getEndMessage(result));
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}

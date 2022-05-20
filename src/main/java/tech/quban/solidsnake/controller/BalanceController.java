package tech.quban.solidsnake.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.quban.solidsnake.entity.Balance;
import tech.quban.solidsnake.repository.BalanceRepository;

@RestController
@RequiredArgsConstructor
@RequestMapping("/balances")
public class BalanceController {
    private final BalanceRepository balanceRepository;

    @PostMapping("/deposit/{balanceUuid}/{amount}")
    @Operation(summary = "Deposits money to specified balance.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    ResponseEntity<?> depositBalance(@PathVariable String balanceUuid, @PathVariable long amount) {
        Balance balance = balanceRepository
                .findById(balanceUuid)
                .orElse(new Balance(balanceUuid, 0));
        balance.setValue(balance.getValue() + amount);
        return ResponseEntity.ok(balanceRepository.save(balance));
    }

    @GetMapping("/{balanceUuid}")
    @Operation(summary = "Returns user balance.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    ResponseEntity<?> getBalance(@PathVariable String balanceUuid) {
        Balance balance = balanceRepository
                .findById(balanceUuid)
                .orElse(new Balance(balanceUuid, 0));
        return ResponseEntity.ok(balance);
    }
}

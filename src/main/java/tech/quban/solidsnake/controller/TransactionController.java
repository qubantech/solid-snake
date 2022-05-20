package tech.quban.solidsnake.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.quban.solidsnake.entity.Balance;
import tech.quban.solidsnake.entity.Transaction;
import tech.quban.solidsnake.exception.AlreadyExistsException;
import tech.quban.solidsnake.exception.NotEnoughException;
import tech.quban.solidsnake.exception.NotFoundException;
import tech.quban.solidsnake.mapper.TransactionMapper;
import tech.quban.solidsnake.repository.BalanceRepository;
import tech.quban.solidsnake.repository.TransactionRepository;

import javax.transaction.Transactional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionRepository transactionRepository;
    private final BalanceRepository balanceRepository;
    private final TransactionMapper transactionMapper;

    @GetMapping("/status/{transactionHash}")
    @Operation(summary = "Returns transaction by its hash.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    @ApiResponse(responseCode = "404", description = "Transaction not found.", content = @Content)
    ResponseEntity<?> getTransactionStatusByHash(@PathVariable String transactionHash) {
        Transaction transaction = transactionRepository
                .findByHash(transactionHash)
                .orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(transactionMapper.transactionToTransactionDto(transaction));
    }

    @PostMapping("/transfer/{senderUuid}/{receiverUuid}/{hash}/{amount}")
    @Transactional
    @Operation(summary = "Performs transaction.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    @ApiResponse(responseCode = "404", description = "Receiver or sender not found.", content = @Content)
    @ApiResponse(responseCode = "406", description = "Transaction already exists.", content = @Content)
    @ApiResponse(responseCode = "409", description = "Not enough money.", content = @Content)
    ResponseEntity<?> transfer(
            @PathVariable String senderUuid,
            @PathVariable String receiverUuid,
            @PathVariable String hash,
            @PathVariable long amount
    ) {
        Balance senderBalance = balanceRepository.findById(senderUuid)
                .orElseThrow(NotFoundException::new);
        Balance receiverBalance = balanceRepository.findById(receiverUuid)
                .orElseThrow(NotFoundException::new);
        ;

        if (transactionRepository.findByHash(hash).isPresent()) {
            throw new AlreadyExistsException();
        }

        if (senderBalance.getValue() < amount) {
            throw new NotEnoughException();
        }

        senderBalance.setValue(senderBalance.getValue() - amount);
        receiverBalance.setValue(receiverBalance.getValue() + amount);

        Transaction transaction = new Transaction();
        transaction.setSender(senderBalance);
        transaction.setReceiver(receiverBalance);
        transaction.setHash(hash);
        transaction.setAmount(amount);
        balanceRepository.saveAndFlush(senderBalance);
        balanceRepository.saveAndFlush(receiverBalance);
        transactionRepository.saveAndFlush(transaction);
        return ResponseEntity.ok(transactionMapper.transactionToTransactionDto(transaction));
    }

    @GetMapping("/user/{userUuid}")
    @Operation(summary = "Returns user transactions.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    ResponseEntity<?> getUserTransactions(@PathVariable String userUuid) {
        Balance senderBalance = balanceRepository.findById(userUuid)
                .orElse(new Balance(userUuid, 0));

        return ResponseEntity.ok(
                transactionMapper.transactionListToTransactionDtoList(
                        transactionRepository.findAllBySender(senderBalance)
                )
        );
    }

    @GetMapping("/seller/{sellerUuid}")
    @Operation(summary = "Returns seller transactions.")
    @ApiResponse(responseCode = "200", description = "OK", content = @Content)
    ResponseEntity<?> getSellerTransactions(@PathVariable String sellerUuid) {
        Balance receiverBalance = balanceRepository.findById(sellerUuid)
                .orElse(new Balance(sellerUuid, 0));
        return ResponseEntity.ok(
                transactionMapper.transactionListToTransactionDtoList(
                        transactionRepository.findAllByReceiver(receiverBalance)
                )
        );
    }
}

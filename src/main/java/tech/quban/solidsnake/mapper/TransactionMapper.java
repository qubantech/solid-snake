package tech.quban.solidsnake.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tech.quban.solidsnake.dto.TransactionDto;
import tech.quban.solidsnake.entity.Transaction;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "sender.uuid", target = "senderUuid")
    @Mapping(source = "receiver.uuid", target = "receiverUuid")
    @Mapping(source = "hash", target = "hash")
    @Mapping(source = "amount", target = "amount")
    @Mapping(source = "timestamp", target = "timestamp")
    TransactionDto transactionToTransactionDto(Transaction transaction);
    List<TransactionDto> transactionListToTransactionDtoList(List<Transaction> transactionList);
}

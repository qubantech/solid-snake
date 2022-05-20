package tech.quban.solidsnake.dto;

import lombok.Data;

@Data
public class TransactionDto {
    private long id;
    private String senderUuid;
    private String receiverUuid;
    private String hash;
    private String timestamp;
    private long amount;
}

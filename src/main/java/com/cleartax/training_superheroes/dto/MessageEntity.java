package com.cleartax.training_superheroes.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "messages")
public class MessageEntity {
    @Id
    private String id;
    private String message;
}

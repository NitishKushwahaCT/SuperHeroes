package com.cleartax.training_superheroes.services;

//import com.amazonaws.services.sqs.AmazonSQS;
//import com.amazonaws.services.sqs.model.DeleteMessageRequest;
import com.cleartax.training_superheroes.dto.MessageEntity;
import com.cleartax.training_superheroes.repos.MessageRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
//import com.amazonaws.services.sqs.model.Message;
import com.cleartax.training_superheroes.config.SqsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

@Service
public class SQSConsumerService {

//    private final AmazonSQS amazonSQS;
    private final SqsConfig sqsConfig;
    private final SqsClient sqsClient;
//    private final MongoDatabase mongoDatabase;
    private final MessageRepository messageEntityRepository;
    private final String queueUrl = "http://localhost:4566/000000000000/superhero-queue";

    public SQSConsumerService(MessageRepository messageEntityRepositoy, SqsClient sqsClient, SqsConfig sqsConfig) {
        this.sqsClient = sqsClient;
        this.sqsConfig = sqsConfig;
//        this.amazonSQS = amazonSQS;
        this.messageEntityRepository = messageEntityRepositoy;
    }


    public void startListening() {

//        MongoCollection<Document> collection = mongoDatabase.getCollection("superheroes");

        while (true) {
            // Receive messages from the queue

            ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(10) // Max messages per batch
                    .waitTimeSeconds(5)
                    .build();
//                    .withMaxNumberOfMessages(10) // Max messages per batch
//                    .withWaitTimeSeconds(5);    // Long polling duration

            ReceiveMessageResponse receiveMessageResponse = sqsClient.receiveMessage(receiveMessageRequest);

            for (Message message : receiveMessageResponse.messages()) {
                try {
                    // Log the received message
                    String messageBody = message.body();
                    if (messageBody == null || messageBody.isEmpty()) {
                        System.err.println("Received empty message body.");
                        continue;  // Skip processing this message
                    }
                    // Log the received message
                    System.out.println("Received message: " + message.body());

                    // Insert the message into MongoDB
                    MessageEntity ok = new MessageEntity();
                    ok.setMessage(message.body());
                    messageEntityRepository.save(ok);

                    // Delete the message from the queue after processing
                    sqsClient.deleteMessage(DeleteMessageRequest.builder().queueUrl(queueUrl).receiptHandle(message.receiptHandle()).build());
                    System.out.println("Message processed and deleted from SQS.");
                } catch (Exception e) {
                    System.err.println("Error processing message: " + e.getMessage());
                }
            }

            // Sleep for a short period to avoid overwhelming the queue
            try {
//                System.out.println("jihs");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Listener interrupted: " + e.getMessage());
            }
        }
    }
}


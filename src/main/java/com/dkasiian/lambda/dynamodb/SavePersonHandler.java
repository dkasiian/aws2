package com.dkasiian.lambda.dynamodb;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.PutItemOutcome;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.dkasiian.lambda.dynamodb.bean.ProductRequest;
import com.dkasiian.lambda.dynamodb.bean.PersonResponse;

public class SavePersonHandler implements RequestHandler<ProductRequest, PersonResponse> {
    private DynamoDB dynamoDb;

    private String DYNAMODB_TABLE_NAME = "Product";
    private Regions REGION = Regions.US_WEST_2;

    public PersonResponse handleRequest(ProductRequest productRequest, Context context) {
        this.initDynamoDbClient();

        persistData(productRequest);

        final PersonResponse personResponse = new PersonResponse();
        personResponse.setMessage("Saved Successfully");

        return personResponse;
    }

    private PutItemOutcome persistData(ProductRequest productRequest) throws ConditionalCheckFailedException {
        return this.dynamoDb.getTable(DYNAMODB_TABLE_NAME).putItem(
            new PutItemSpec().withItem(new Item()
              .withNumber("id", productRequest.getId())
              .withString("name", productRequest.getName())
              .withString("pictureURL", productRequest.getPictureURL())
              .withNumber("price", productRequest.getPrice())));
    }

    private void initDynamoDbClient() {
        final AmazonDynamoDBClient client = new AmazonDynamoDBClient();
        client.setRegion(Region.getRegion(REGION));
        this.dynamoDb = new DynamoDB(client);
    }
}

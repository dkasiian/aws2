package com.dkasiian.lambda.apigateway;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.dkasiian.lambda.apigateway.model.Product;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class APIDemoHandler implements RequestStreamHandler {
    private final JSONParser parser = new JSONParser();
    private static final String DYNAMODB_TABLE_NAME = "Product";

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final JSONObject responseJson = new JSONObject();

        final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        final DynamoDB dynamoDb = new DynamoDB(client);

        try {
            final JSONObject event = (JSONObject) parser.parse(reader);

            if (event.get("body") != null) {
                final Product product = new Product((String) event.get("body"));

                dynamoDb.getTable(DYNAMODB_TABLE_NAME)
                        .putItem(new PutItemSpec()
                                .withItem(new Item()
                                        .withNumber("id", product.getId())
                                        .withString("name", product.getName())
                                        .withString("pictureURL", product.getPictureURL())
                                        .withNumber("price", product.getPrice())
                                ));
            }

            final JSONObject responseBody = new JSONObject();
            responseBody.put("message", "New item created");

            final JSONObject headerJson = new JSONObject();
            headerJson.put("x-custom-header", "my custom header value");

            responseJson.put("statusCode", 200);
            responseJson.put("headers", headerJson);
            responseJson.put("body", responseBody.toString());
        } catch (ParseException pex) {
            responseJson.put("statusCode", 400);
            responseJson.put("exception", pex);
        }

        final OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
    }

    public void handleGetByParam(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final JSONObject responseJson = new JSONObject();

        final AmazonDynamoDB client = AmazonDynamoDBClientBuilder.defaultClient();
        final DynamoDB dynamoDb = new DynamoDB(client);

        Item result = null;
        try {
            final JSONObject event = (JSONObject) parser.parse(reader);
            final JSONObject responseBody = new JSONObject();

            if (event.get("pathParameters") != null) {
                final JSONObject pps = (JSONObject) event.get("pathParameters");
                if (pps.get("id") != null) {
                    int id = Integer.parseInt((String) pps.get("id"));
                    result = dynamoDb.getTable(DYNAMODB_TABLE_NAME).getItem("id", id);
                }
            } else if (event.get("queryStringParameters") != null) {
                final JSONObject qps = (JSONObject) event.get("queryStringParameters");
                if (qps.get("id") != null) {
                    int id = Integer.parseInt((String) qps.get("id"));
                    result = dynamoDb.getTable(DYNAMODB_TABLE_NAME).getItem("id", id);
                }
            }

            if (result != null) {
                final Product product = new Product(result.toJSON());
                responseBody.put("Product", product);
                responseJson.put("statusCode", 200);
            } else {

                responseBody.put("message", "No item found");
                responseJson.put("statusCode", 404);
            }

            final JSONObject headerJson = new JSONObject();
            headerJson.put("x-custom-header", "my custom header value");

            responseJson.put("headers", headerJson);
            responseJson.put("body", responseBody.toString());

        } catch (final ParseException pex) {
            responseJson.put("statusCode", 400);
            responseJson.put("exception", pex);
        }

        final OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
    }
}

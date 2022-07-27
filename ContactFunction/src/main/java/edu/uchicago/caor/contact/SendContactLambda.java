package edu.uchicago.caor.contact;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

public class SendContactLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    // save memory by avoiding create this every time
    private final ObjectMapper objectMapper = new ObjectMapper();

    static final String TO = "caor@uchicago.edu";
    static final String HTMLBODY = "<h1>Amazon SES test (AWS SDK for Java)</h1>"
            + "<p>This email was sent with <a href='https://aws.amazon.com/ses/'>"
            + "Amazon SES</a> using the <a href='https://aws.amazon.com/sdk-for-java/'>"
            + "AWS SDK for Java</a>";

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {

        MyMessage message = null;
        try {
            message = objectMapper.readValue(input.getBody(), MyMessage.class);
        } catch (JsonProcessingException e) {
            return new APIGatewayProxyResponseEvent()
                    .withBody(String.format("{%$}", e.getMessage()))
                    .withStatusCode(400);
        }

        try {
            AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
                    .standard()
                    .withRegion(Regions.US_WEST_2)
                    .build();
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(new Destination().withToAddresses(TO))
                    .withMessage(new Message()
                            .withBody(new Body()
                                    .withHtml(new Content()
                                            .withCharset("UTF-8").withData(HTMLBODY))
                                    .withText(new Content()
                                            .withCharset("UTF-8").withData(message.getBody())))
                            .withSubject(new Content()
                                    .withCharset("UTF-8").withData(message.getSubject())))
                    .withSource(message.getEmailFrom());
            client.sendEmail(request);
        } catch (Exception ex) {
            return new APIGatewayProxyResponseEvent()
                    .withBody(String.format("{%$}", ex.getMessage()))
                    .withStatusCode(400);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("emailFrom", message.getEmailFrom());
        jsonObject.put("subject", message.getSubject());
        jsonObject.put("body", message.getBody());

        return new APIGatewayProxyResponseEvent().withStatusCode(200).withBody(jsonObject.toString());
    }
}


package awslextest;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lexruntimev2.LexRuntimeV2AsyncClient;
import software.amazon.awssdk.services.lexruntimev2.model.ConversationMode;
import software.amazon.awssdk.services.lexruntimev2.model.StartConversationRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Lex {

    private LexRuntimeV2AsyncClient lexRuntimeServiceClient;
    private BotResponseHandler botResponseHandler;
    private StartConversationRequest startConversationRequest;
    private EventsPublisher eventsPublisher;

    public Lex() {
        String botId = System.getenv("AWS_LEX_BOT_ID");
        String botAliasId = System.getenv("AWS_LEX_BOT_ALIAS_ID");
        String localeId = "en_US";
        String accessKey = System.getenv("AWS_ACCESS_KEY_ID");
        String secretKey = System.getenv("AWS_SECRET_KEY");
        String sessionId = UUID.randomUUID().toString();
        Region region = Region.US_EAST_1; // Choose an AWS Region where the Amazon Lex Streaming API is available.

        AwsCredentialsProvider awsCredentialsProvider = StaticCredentialsProvider
                .create(AwsBasicCredentials.create(accessKey, secretKey));

        // Create a new SDK client. You need to use an asynchronous client.
        System.out.println("step 1: creating a new Lex SDK client");
        lexRuntimeServiceClient = LexRuntimeV2AsyncClient.builder()
                .region(region)
                .credentialsProvider(awsCredentialsProvider)
                .build();


        // Configure the bot, alias and locale that you'll use to have a conversation.
        System.out.println("step 2: configuring bot details");
        StartConversationRequest.Builder startConversationRequestBuilder = StartConversationRequest.builder()
                .botId(botId)
                .botAliasId(botAliasId)
                .localeId(localeId);

        // Configure the conversation mode of the bot. By default, the
        // conversation mode is audio.
        System.out.println("step 3: choosing conversation mode");
        startConversationRequestBuilder = startConversationRequestBuilder.conversationMode(ConversationMode.AUDIO);

        // Assign a unique identifier for the conversation.
        System.out.println("step 4: choosing a unique conversation identifier");
        startConversationRequestBuilder = startConversationRequestBuilder.sessionId(sessionId);

        // Start the initial request.
        startConversationRequest = startConversationRequestBuilder.build();

        // Create a stream of audio data to the Amazon Lex bot. The stream will start after the connection is established with the bot.
        eventsPublisher = new EventsPublisher();

        // Create a class to handle responses from bot. After the server processes the user data you've streamed, the server responds
        // on another stream.
        botResponseHandler = new BotResponseHandler(eventsPublisher, sessionId);
    }

    public void begin() throws InterruptedException{
        System.out.println("step 5: starting the conversation ...");
        CompletableFuture<Void> conversation = lexRuntimeServiceClient.startConversation(
                startConversationRequest,
                eventsPublisher,
                botResponseHandler
        );

        // Wait until the conversation finishes. The conversation finishes if the dialog state reaches the "Closed" state.
        // The client stops the connection. If an exception occurs during the conversation, the
        // client sends a disconnection event.
        conversation.whenComplete((result, exception) -> {
            if (exception != null) {
                eventsPublisher.disconnect();
            }
        });

        // The conversation finishes when the dialog state is closed and last prompt has been played.
        while (!botResponseHandler.isConversationComplete()) {
            Thread.sleep(100);
        }

        // Randomly sleep for 100 milliseconds to prevent JVM from exiting.
        // You won't need this in your production code because your JVM is
        // likely to always run.
        // When the conversation finishes, the following code block stops publishing more data and informs the Amazon Lex bot that there is no more data to send.
        if (botResponseHandler.isConversationComplete()) {
            System.out.println("conversation is complete.");
            eventsPublisher.stop();
        }
    }
}

package edu.java.scrapper.clients.stackoverflow;

import edu.java.scrapper.clients.Client;

public class StackOverflowClient extends Client<StackOverFlowResponse> {
    private static final String BASE_URL = "https://api.stackexchange.com/";
    private static final String URI_STRING =
        "/2.3/questions/{userInfo}";

    private static final Class<StackOverFlowResponse> CLASS_REF = StackOverFlowResponse.class;

    public StackOverflowClient(String baseUrl) {
        super(CLASS_REF, baseUrl);
    }

    public StackOverflowClient() {
        super(CLASS_REF, BASE_URL);
    }

    public StackOverFlowResponse fetchResponse(String userId) {
        return super.fetchResponse(userId);
    }

    @Override
    protected String getUriString() {
        return URI_STRING;
    }
}

package edu.java.clients.stackoverflow;

import edu.java.clients.Client;

public class StackOverflowClient extends Client<StackOverFlowResponse> {
    private static final String BASE_URL = "https://api.stackexchange.com/";
    private static final String URI_STRING =
        "/2.3/questions/{userInfo}?order=desc&sort=activity&site=stackoverflow&filter=!-n0mNLma5xZn(k-WDcJ*(pSqnnWWeNJ7KAELyvIpT1vQ0).WiQ5TYS";

    private static final Class<StackOverFlowResponse> CLASS_REF = StackOverFlowResponse.class;

    public StackOverflowClient(String baseUrl) {
        super(CLASS_REF, baseUrl);
    }

    public StackOverflowClient() {
        super(CLASS_REF, BASE_URL);
    }

    @Override
    protected String getUriString() {
        return URI_STRING;
    }
}

package idv.haojun.graphqlsample;


import com.apollographql.apollo.ApolloClient;

public class ApolloHelper {
    private static ApolloHelper instance = new ApolloHelper();
    private ApolloClient apolloClient;

    public static ApolloHelper getInstance() {
        return instance;
    }

    private ApolloHelper() {
        apolloClient = ApolloClient.builder()
                .serverUrl(URLHelper.URL)
                .okHttpClient(OkHttpHelper.getInstance().getOkHttpClient())
                .build();
    }

    public ApolloClient getApolloClient() {
        return apolloClient;
    }
}

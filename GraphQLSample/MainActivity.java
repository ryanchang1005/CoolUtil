package idv.haojun.graphqlsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;

import java.util.List;

import javax.annotation.Nonnull;

import idv.haojun.graphqlsample.type.FeedType;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        feedQuery();
    }

    private void feedQuery() {
        FeedQuery feedQuery = FeedQuery.builder()
                .limit(1)
                .type(FeedType.HOT)
                .build();

        ApolloHelper.getInstance().getApolloClient().query(feedQuery).enqueue(new ApolloCall.Callback<FeedQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<FeedQuery.Data> response) {
                FeedQuery.Data data = response.data();
                if (data != null) {

                    List<FeedQuery.Feed> feeds = data.feed();
                    if (feeds != null) {
                        for (FeedQuery.Feed feed : feeds) {
                            L.d(feed.toString());
                        }
                    }
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                L.d("e:" + e.toString());
            }
        });
    }
}

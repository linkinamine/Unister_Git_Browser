package com.unister.gitquery;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.unister.gitquery.Data.Constants;
import com.unister.gitquery.Network.ConnectionCheck;
import com.unister.gitquery.Network.NetworkManager;
import com.unister.gitquery.Pojo.Adapters.SubscribersListAdapter;
import com.unister.gitquery.Pojo.Subscriber;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed El Amine on 28/05/2016.
 */


public class SubscribersActivity extends AppCompatActivity {

    private List<Subscriber> subscribers;
    private ArrayList<Subscriber> subscriberArrayList;
    private ListView subscribersListView;
    private SubscribersListAdapter subscriberListAdapter;
    private ProgressBar progressBar;
    private String repoName;
    private String subscriberUrl;
    private TextView repositoryName;
    private TextView subscribersCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository_details);

        initElements();
        initLayout();

        // loadSubscribers only if connection is available and store the subscriberUrl to search again
        if (networkConnectedCheck(subscriberUrl))
            loadSubscribers(subscriberUrl);
    }

    private void initElements() {
        subscribers = new ArrayList<Subscriber>();
        subscriberArrayList = new ArrayList<Subscriber>();
        subscribersListView = (ListView) findViewById(R.id.subscribersListView);

        repoName = getIntent().getExtras().getString(Constants.REPO_NAME);
        subscriberUrl = getIntent().getExtras().getString(Constants.SUBSCRIBERS_URL);
    }

    private void initLayout() {

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.material_deep_teal_200),
                android.graphics.PorterDuff.Mode.MULTIPLY);


        repositoryName = (TextView) findViewById(R.id.repository_name);
        repositoryName.setText(repoName);

        subscribersCount = (TextView) findViewById(R.id.subscribersCount);
        subscribersCount.setVisibility(View.GONE);

    }

    //Check the status of the connection and display the dialog accordingly
    private boolean networkConnectedCheck(final String retryQuery) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {
            Log.d(Constants.TAG, retryQuery);
            ConnectionCheck.showNotConnected(
                    "Couldn't load search results. Are you connected to the internet?",
                    new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);
                            loadSubscribers(retryQuery);
                        }
                    },
                    SubscribersActivity.this);
        }
        return cm.getActiveNetworkInfo() != null;
    }

    /**
     * Git API Call Starts here
     *
     * @param subscriberUrl
     */

    private void loadSubscribers(final String subscriberUrl) {
        if (!progressBar.isShown())
            progressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest subscribersRequest = new JsonArrayRequest(subscriberUrl,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(Constants.TAG, response.toString());

                        fillsubscribersList(response);
                    }
                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(Constants.TAG, "Error: " + error.getMessage());
                progressBar.setVisibility(View.GONE);

                ConnectionCheck.showNotConnected(
                        "Couldn't load search results. Are you connected to the internet?",
                        new Runnable() {
                            @Override
                            public void run() {
                                loadSubscribers(subscriberUrl);
                            }
                        },
                        SubscribersActivity.this);
            }
        }

        );

        // Adding request to request queue
        NetworkManager.getInstance(getApplicationContext()).addToRequestQueue(subscribersRequest);

    }

    @Override
    public void onBackPressed() {

        goBack(null);

        // super.onBackPressed();
    }

    public void goBack(View v) {


        this.finish();
        this.overridePendingTransition(
                R.anim.slide_from_left, R.anim.slide_to_right);


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    //fill the list with the jsonArray response
    private void fillsubscribersList(JSONArray jsonArray) {


        for (int i = 0; i < jsonArray.length(); i++) {
            try {

                JSONObject subscriberJson = (JSONObject) jsonArray
                        .get(i);
                Subscriber subscriber = new Subscriber(subscriberJson.getString("login"), subscriberJson.getString("avatar_url"));
                subscribers.add(subscriber);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        subscribersCount.setVisibility(View.VISIBLE);
        String subscribersText = getResources().getString(R.string.subscriber);
        if (subscribers.size() > 1) {
            subscribersText += "s";
        }
        subscribersCount.setText(subscribers.size() + " " + subscribersText);

        progressBar.setVisibility(View.GONE);


        subscriberArrayList.clear();
        subscriberArrayList.addAll(subscribers);

        Log.i(Constants.TAG, "fillsubscribersList, subscribers: " + subscriberArrayList.get(0).getLogin());

        subscribersListView.smoothScrollToPosition(0);
        subscriberListAdapter = new SubscribersListAdapter(SubscribersActivity.this, subscriberArrayList);
        subscribersListView.setAdapter(subscriberListAdapter);
        subscriberListAdapter.notifyDataSetChanged();
    }


}

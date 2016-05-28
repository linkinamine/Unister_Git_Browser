package com.unister.gitquery;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.unister.gitquery.Data.Constants;
import com.unister.gitquery.Network.ConnectionCheck;
import com.unister.gitquery.Network.NetworkManager;
import com.unister.gitquery.Network.Request.SearchRequest;
import com.unister.gitquery.Network.Response.GitResponse;
import com.unister.gitquery.Pojo.Adapters.RepositoryListAdapter;
import com.unister.gitquery.Pojo.Repository;

import java.util.ArrayList;
import java.util.List;

import br.com.mauker.materialsearchview.MaterialSearchView;

/**
 * Created by Mohamed El Amine on 28/05/2016.
 */


public class MainActivity extends AppCompatActivity {

    private MaterialSearchView searchView;
    private List<Repository> repositories;
    private ArrayList<Repository> repositoriesList;
    private ListView repositoriesListView;
    private RepositoryListAdapter repositoryListAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        initElements();
        initLayout();


    }

    private void initElements() {
        repositoriesList = new ArrayList<Repository>();
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        repositoriesListView = (ListView) findViewById(R.id.repositoriesListView);

    }

    private void initLayout() {

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.material_deep_teal_200),
                android.graphics.PorterDuff.Mode.MULTIPLY);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(Constants.TAG, "onQueryTextSubmit");
                progressBar.setVisibility(View.VISIBLE);

                if (repositoriesList != null) {
                    clearListView();
                }
                // loadSearchResults only if connection is available and store the query to search again
                if (networkConnectedCheck(query))
                    loadSearchResults(query);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(Constants.TAG, "onQueryTextChange");
                clearListView();
                return false;
            }
        });

        searchView.setSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewOpened() {
                Log.d(Constants.TAG, "onSearchViewOpened");

            }

            @Override
            public void onSearchViewClosed() {
                Log.d(Constants.TAG, "onSearchViewOpened");

            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView tv = (TextView) view.findViewById(R.id.tv_str);

                if (tv != null) {
                    searchView.setQuery(tv.getText().toString(), false);
                    Log.d(Constants.TAG, "searchView.setQuery");

                }
            }
        });


        searchView.setTintAlpha(200);
        searchView.adjustTintAlpha(0.8f);


    }

    //Check the status of the connection and display the dialog accordingly
    private boolean networkConnectedCheck(final String retryQuery) {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null) {
            ConnectionCheck.showNotConnected(
                    "Couldn't load search results. Are you connected to the internet?",
                    new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);
                            loadSearchResults(retryQuery);
                        }
                    },
                    MainActivity.this);
        }
        return cm.getActiveNetworkInfo() != null;
    }

    private void clearListView() {
        repositoriesList.clear();
        repositoryListAdapter = new RepositoryListAdapter(MainActivity.this, repositoriesList);
        repositoriesListView.setAdapter(repositoryListAdapter);
        repositoryListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle toolbar item clicks here. It'll
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_search:
                // Open the search view on the menu item click.

                searchView.openSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isOpen()) {
            // Close the search on the back button press.
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * Git API Call Starts here
     *
     * @param searchTerm
     */

    private void loadSearchResults(final String searchTerm) {
        Request searchRequest = new SearchRequest(searchTerm, new Response.Listener<GitResponse>() {
            @Override
            public void onResponse(GitResponse response) {
                progressBar.setVisibility(View.GONE);
                Log.d(Constants.TAG, " size : " + response.getRepoData().get(0).getOwner().getAvatar_url());
                repositories = response.getRepoData();
                fillRepositoriesList(repositories);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(Constants.TAG, "Unhandled error! " + error);
                progressBar.setVisibility(View.GONE);

                ConnectionCheck.showNotConnected(
                        "Couldn't load search results. Are you connected to the internet?",
                        new Runnable() {
                            @Override
                            public void run() {
                                loadSearchResults(searchTerm);
                            }
                        },
                        MainActivity.this);
            }
        });

        NetworkManager.getInstance(getApplicationContext()).addToRequestQueue(searchRequest);
    }

    @Override
    protected void onPause() {
        super.onPause();
        searchView.clearSuggestions();
    }

    @Override
    protected void onResume() {
        super.onResume();
        searchView.activityResumed();

    }

    private void fillRepositoriesList(List<Repository> repositories) {


        repositoriesList.clear();
        repositoriesList.addAll(repositories);

        Log.i(Constants.TAG, "fillRepositoriesList, repositories first: " + repositoriesList.get(0).getName());

        repositoriesListView.smoothScrollToPosition(0);

        repositoryListAdapter = new RepositoryListAdapter(MainActivity.this, repositoriesList);
        repositoriesListView.setAdapter(repositoryListAdapter);
        repositoryListAdapter.notifyDataSetChanged();
    }


}

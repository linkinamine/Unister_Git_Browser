package com.unister.gitquery.Network.Request;

import com.android.volley.Response;
import com.unister.gitquery.Data.Constants;
import com.unister.gitquery.Network.Response.GitResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mohamed El Amine on 28/05/2016.
 */



/**
 * The class calls GsonRequest via GitRequest and buils the search query
 */

public class SearchRequest extends GitRequest {
    /**
     *
     * @param searchTerm
     * @param listener
     * @param errorListener
     */
    public SearchRequest(String searchTerm, Response.Listener<GitResponse> listener, Response.ErrorListener errorListener) {
        super(Constants.GIT_SEARCH_URL, buildQuery(searchTerm), listener, errorListener);
    }

    /**
     *
     * @param searchTerm
     * @return  HashMap<String, String>
     */
    private static Map<String, String> buildQuery(String searchTerm) {
        HashMap<String, String> query = new HashMap<>();
        query.put("q", searchTerm);
        return query;
    }
}

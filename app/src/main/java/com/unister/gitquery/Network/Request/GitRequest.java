package com.unister.gitquery.Network.Request;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.android.volley.Response;
import com.unister.gitquery.Data.Constants;
import com.unister.gitquery.Network.Response.GitResponse;

import java.util.Map;

/**
 * Created by Mohamed El Amine on 28/05/2016.
 */

/**
 * An abstract class that defines the request and add parameters, as well as building the final URL
 */
abstract class GitRequest extends GsonRequest<GitResponse> {

    /**
     * Make a GET request to the Git API.
     *
     * @param path          The request path, to be appended to the Git API stub.
     * @param listener      The object to call when the request is successful
     * @param errorListener The object to call when the request fails
     */
    public GitRequest(String path, @Nullable Map<String, String> params, Response.Listener<GitResponse> listener, Response.ErrorListener errorListener) {
        super(buildUrl(path, params),
                GitResponse.class,
                null,
                listener,
                errorListener);
    }


    /**
     * Given a Git API path and a dictionary of parameters, assemble the full API URL.
     */
    private static String buildUrl(String path, @Nullable Map<String, String> params) {
        Uri.Builder builder = Constants.GIT_API_URL.buildUpon();
        builder.appendEncodedPath(path);

        // Append each dictionary parameter to the URI in turn
        if (params != null) {
            for (Map.Entry<String, String> param : params.entrySet()) {
                builder.appendQueryParameter(param.getKey(), param.getValue());
            }
        }


        return builder.build().toString();
    }
}

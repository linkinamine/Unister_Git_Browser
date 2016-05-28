package com.unister.gitquery.Network.Response;

import com.unister.gitquery.Pojo.Repository;

import java.util.List;

/**
 * Created by Mohamed El Amine on 28/05/2016.
 */


/**
 * returning the response data from the Json respnse
 */
public class GitResponse {

    List<Repository> items;

    /**
     *
     * @return List<Repository>
     */
    public List<Repository> getRepoData() {
        return items;
    }
}
package com.unister.gitquery.Pojo.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.unister.gitquery.Data.Constants;
import com.unister.gitquery.Pojo.Repository;
import com.unister.gitquery.R;
import com.unister.gitquery.SubscribersActivity;

import java.util.List;

/**
 * Created by Mohamed El Amine on 28/05/2016.
 */


/**
 * The class sets the repository list adapter
 */

public class RepositoryListAdapter extends BaseAdapter {

    private Context context;
    private List<Repository> repositories;
    private ViewHolder viewHolder;


    public static class ViewHolder {
        public ImageView avatarOwner;
        public TextView repoName;
        public TextView description;
        public TextView forksNumber;

    }


    public RepositoryListAdapter(Context context, List<Repository> repositories) {

        Log.d(Constants.TAG, "RepositoryListAdapter,size :" + repositories.size());

        this.context = context;
        this.repositories = repositories;

    }

    @Override
    public int getCount() {

        if (repositories == null)
            return 0;
        Log.d(Constants.TAG, "" + repositories.size());

        return repositories.size();
    }

    @Override
    public Object getItem(int i) {
        return repositories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Log.d(Constants.TAG, "RepositoryListAdapter, getView position: " + position);

        View view = convertView;
        Log.d(Constants.TAG, "convertView, not null ");


        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.repository_item, parent, false);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.avatarOwner = (ImageView) view.findViewById(R.id.avatarOwner);
            viewHolder.repoName = (TextView) view.findViewById(R.id.repo_name);
            viewHolder.description = (TextView) view.findViewById(R.id.description);
            viewHolder.forksNumber = (TextView) view.findViewById(R.id.forks_count);
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Log.i(Constants.TAG, "repositories.get " + position);

        Repository repository = repositories.get(position);

        viewHolder.repoName.setText(repository.getName());
        viewHolder.description.setText(repository.getDescription());
        viewHolder.forksNumber.setText(repository.getForks_count());
        //Glide lib is used for better image loading and caching (notice the DiskCacheStrategy)
        Glide.with(context.getApplicationContext()).load(repository.getOwner().getAvatar_url()).thumbnail(Glide.with(context.getApplicationContext()).load(R.mipmap.ic_github))
                .error(R.mipmap.ic_default_avatar).diskCacheStrategy(DiskCacheStrategy.RESULT).into(viewHolder.avatarOwner);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Log.i(Constants.TAG, "onClick position: " + position);
                //Go to repo details
                openRepositoryDetails(position);


            }
        });

        return view;
    }

    private void openRepositoryDetails(int position) {

        Log.i(Constants.TAG, "openRepositoryDetails position: " + position);

        Intent intent = new Intent((Activity) context, SubscribersActivity.class);
        intent.putExtra(Constants.REPO_NAME, repositories.get(position).getName());
        intent.putExtra(Constants.SUBSCRIBERS_URL, repositories.get(position).getSubscribers_url());
        ((Activity) context).startActivity(intent);
        ((Activity) context).overridePendingTransition(
                R.anim.slide_from_right, R.anim.slide_to_left);
    }


}

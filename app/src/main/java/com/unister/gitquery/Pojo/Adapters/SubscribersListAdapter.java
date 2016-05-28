package com.unister.gitquery.Pojo.Adapters;

import android.content.Context;
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
import com.unister.gitquery.Pojo.Subscriber;
import com.unister.gitquery.R;

import java.util.List;


/**
 * Created by Mohamed El Amine on 28/05/2016.
 */


/**
 * The class sets the subscribers list adapter
 */


public class SubscribersListAdapter extends BaseAdapter {

    private Context context;
    private List<Subscriber> subscribers;
    private ViewHolder viewHolder;


    public static class ViewHolder {
        public ImageView avatarSubscriber;
        public TextView login;


    }


    public SubscribersListAdapter(Context context, List<Subscriber> subscribers) {

        Log.d(Constants.TAG, "SubscriberListAdapter,size :" + subscribers.size());

        this.context = context;
        this.subscribers = subscribers;

    }

    @Override
    public int getCount() {

        if (subscribers == null)
            return 0;
        Log.d(Constants.TAG, "" + subscribers.size());

        return subscribers.size();
    }

    @Override
    public Object getItem(int i) {
        return subscribers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Log.d(Constants.TAG, "SubscriberListAdapter, getView position: " + position);

        View view = convertView;
        Log.d(Constants.TAG, "convertView, not null ");


        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.subscriber_item, parent, false);
            // configure view holder
            viewHolder = new ViewHolder();
            viewHolder.avatarSubscriber = (ImageView) view.findViewById(R.id.avatarSubscriber);
            viewHolder.login = (TextView) view.findViewById(R.id.login);

            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }


        Log.i(Constants.TAG, "subscribers.get " + position);

        Subscriber subscriber = subscribers.get(position);

        viewHolder.login.setText(subscriber.getLogin());

        Glide.with(context.getApplicationContext()).load(subscriber.getAvatar_url()).error(R.mipmap.ic_default_avatar).thumbnail(Glide.with(context.getApplicationContext()).load(R.mipmap.ic_github)).diskCacheStrategy(DiskCacheStrategy.RESULT).into(viewHolder.avatarSubscriber);

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Log.i(Constants.TAG, "onClick position: " + position);


            }
        });

        return view;
    }


}

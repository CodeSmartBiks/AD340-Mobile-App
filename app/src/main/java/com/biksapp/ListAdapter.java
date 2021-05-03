package com.biksapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;


public class ListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Cameras> cameras;

    public ListAdapter(Context context, ArrayList<Cameras> cameras) {

        this.context = context;
        this.cameras = cameras;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return cameras.size();
    }

    @Override
    public Object getItem(int position) {
        return cameras.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_traffic_camera_cam, null, true);
            holder.ImageUrl = (ImageView) convertView.findViewById(R.id.ImageUrl);
            holder.Description = (TextView) convertView.findViewById(R.id.Description);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        Picasso.get().load(cameras.get(position).getImageUrl()).into(holder.ImageUrl);
        holder.Description.setText("Description: "+cameras.get(position).description);
        return convertView;
    }

    private class ViewHolder {
        protected TextView Description;
        protected ImageView ImageUrl;
    }

}
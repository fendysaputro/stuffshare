package com.stuff.stuffshare.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.model.Campaigner;
import com.stuff.stuffshare.model.CategoryBarang;
import com.stuff.stuffshare.model.Message;
import com.stuff.stuffshare.model.MessageUser;

import org.json.JSONException;

import java.util.List;

/**
 * Created by Fendy Saputro on 21/11/2020.
 * vidis194@gmail.com
 */
public class InboxMessageAdapter extends ArrayAdapter<MessageUser> {
    private Context context;
    StuffShareApp stuffShareApp;
    private List<MessageUser> values;


    public InboxMessageAdapter(Context context, int resourceId, List<MessageUser> values) {
        super(context, resourceId, values);
        this.context = context;
        this.values = values;
        stuffShareApp = (StuffShareApp) this.context.getApplicationContext();
    }

    private class ViewHolder {
        TextView tvDescription;
        TextView tvDate;
        ImageView ivDelete;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
        MessageUser rowItem = (MessageUser) values.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_list_inbox_message, parent, false);
            holder = new ViewHolder();
            holder.tvDescription = (TextView) convertView.findViewById(R.id.tv_description);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            holder.ivDelete = (ImageView) convertView.findViewById(R.id.iv_message_action);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position % 2 == 1) {
            convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            convertView.setBackgroundColor(Color.parseColor("#e1f0ee"));
        }

        holder.tvDescription.setText(rowItem.getText());
        holder.tvDate.setText(rowItem.getDate());

        holder.ivDelete.setImageResource(R.drawable.ic_delete_black_24dp);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "this is message", Toast.LENGTH_SHORT).show();
            }
        });


        return convertView;
    }
}

package com.stuff.stuffshare.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.model.Message;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fendy Saputro on 21/11/2020.
 * vidis194@gmail.com
 */
public class InboxMessageAdapter extends ArrayAdapter<Message> {
    private Context context;


    public InboxMessageAdapter(Context context, int resourceId, List<Message> itemsMessage) {
        super(context, resourceId, itemsMessage);
        this.context = context;
    }

    private class ViewHolder {

    }
}

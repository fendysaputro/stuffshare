package com.stuff.stuffshare.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.model.CategoryBarang;
import com.stuff.stuffshare.model.DetailDonation;

import java.util.ArrayList;

public class DetailSubmissionAdapter extends ArrayAdapter<CategoryBarang> {

    private ArrayList<CategoryBarang> categoryBarangs = null;
    Context context;
    StuffShareApp stuffShareApp;

    public DetailSubmissionAdapter(Context context, int textViewResourceId, ArrayList<CategoryBarang> categoryBarangs){
        super(context, textViewResourceId, categoryBarangs);
        this.context = context;
        this.categoryBarangs = categoryBarangs;
        stuffShareApp = (StuffShareApp) this.context.getApplicationContext();
    }

    private class ViewHolder {
        TextView txtTitleBarang;
        EditText edJmlBarang;
        int position;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @Override
    public CategoryBarang getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder = null;
//        CategoryBarang categoryBarang = getItem(position);
        CategoryBarang categoryBarang = (CategoryBarang) categoryBarangs.get(position);
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_view_detail_submission, null);
            holder = new ViewHolder();
            holder.txtTitleBarang = (TextView) convertView.findViewById(R.id.txtDetailJenisBarang);
            holder.edJmlBarang = (EditText) convertView.findViewById(R.id.edDetailJmlBarang);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtTitleBarang.setText(categoryBarang.getProductName());
        holder.edJmlBarang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                categoryBarang.setCount(charSequence.toString());
                categoryBarang.setId(categoryBarang.getId());
                Log.i(stuffShareApp.TAG, categoryBarang.getProductName() + " -> " + categoryBarang.getCount());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return convertView;
    }
}

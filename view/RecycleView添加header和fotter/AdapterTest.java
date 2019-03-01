package com.xal.testmd.vp.page;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xal.testmd.R;
import com.xal.testmd.vp.OnItemClickListener;
import com.xal.testmd.vp.OnItemLongClickListener;

import java.util.ArrayList;


/**
 * Created by Administrator on 2018/1/16 0016.
 */

public class AdapterTest extends RecyclerView.Adapter<AdapterTest.MyViewHolder> {
    private Context context;
    private ArrayList<String> list;
    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    public AdapterTest(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickLitener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setOnItemLongCLickListener(OnItemLongClickListener longListener) {
        this.longListener = longListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home, null);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.tv.setText(list.get(position));

        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(holder.itemView, position);
                }
            });
        }

        if (longListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    longListener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = view.findViewById(R.id.tv);
        }
    }
}

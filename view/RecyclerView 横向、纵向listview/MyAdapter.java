package shixiu.testxscrollview;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ShiShow_xk on 2016/10/24.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyHolder> {

    private ArrayList<String> list;
    private OnItemClickListener listener;

    public MyAdapter(ArrayList<String> list) {
        this.list = list;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickListener mOnItemClickLitener) {
        this.listener = mOnItemClickLitener;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, null);
        MyHolder holder = new MyHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final int position) {

        holder.tv.setText(list.get(position));
        if (listener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = holder.getLayoutPosition();
                    Log.e("Tag", "pos=" + pos);
                    Log.e("Tag", "position=" + position);
                    listener.onItemClick(holder.itemView, position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {

        private TextView tv;

        public MyHolder(View itemView) {
            super(itemView);
            tv = (TextView) itemView.findViewById(R.id.tv);
        }
    }
}

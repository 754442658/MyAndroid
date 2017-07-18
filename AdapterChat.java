package com.tencentim.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencentim.R;
import com.tencentim.custom.CircleImageView;
import com.tencentim.im.Message;

import java.util.ArrayList;

public class AdapterChat extends ArrayAdapter<Message> {

    // 布局id
    private int layout;
    private View view;
    private ViewHolder holder;

    public AdapterChat(Context context, ArrayList<Message> list) {
        super(context, R.layout.item_chat, list);
        this.layout = R.layout.item_chat;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup arg2) {
        if (convertView == null) {
            holder = new ViewHolder();
            view = LayoutInflater.from(getContext()).inflate(layout, null);
            holder.img_left_photo = (CircleImageView) view.findViewById(R.id.img_left);
            holder.img_right_photo = (CircleImageView) view.findViewById(R.id.img_right);

            holder.leftMessage = (RelativeLayout) view.findViewById(R.id.leftMessage);
            holder.rightMessage = (RelativeLayout) view.findViewById(R.id.rightMessage);

            holder.leftPanel = (RelativeLayout) view.findViewById(R.id.leftPanel);
            holder.rightPanel = (RelativeLayout) view.findViewById(R.id.rightPanel);

            holder.sending = (ProgressBar) view.findViewById(R.id.sending);
            holder.error = (ImageView) view.findViewById(R.id.sendError);
            holder.sender = (TextView) view.findViewById(R.id.sender);
            holder.rightDesc = (TextView) view.findViewById(R.id.rightDesc);
            holder.tv_date = (TextView) view.findViewById(R.id.tv_date);
            view.setTag(holder);

        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }

        if (i < getCount()) {
            Message data = getItem(i);
            data.showMessage(holder, getContext());
        }
        return view;
    }

    public class ViewHolder {
        // 左右头像
        public CircleImageView img_left_photo, img_right_photo;
//        // 左右布局
//        public RelativeLayout rl_left, rl_right;
//
//        // 左右文本消息布局
//        public RelativeLayout rl_left_text, rl_right_text;
//        // 消息发送日期
//        public TextView tv_date, rightDesc;
//        // 消息发送中pro
//        public ProgressBar sending;
//        // 消息发送失败
//        public ImageView error;


        // 左侧消息文本布局
        public RelativeLayout leftMessage;
        // 右侧消息文本布局
        public RelativeLayout rightMessage;
        // 左侧消息布局
        public RelativeLayout leftPanel;
        // 右侧消息布局
        public RelativeLayout rightPanel;
        // 正在发送消息时的pro
        public ProgressBar sending;
        // 发送失败Img
        public ImageView error;
        // 发送人
        public TextView sender;
        // 消息发送日期
        public TextView tv_date;
        // 消息提示信息
        public TextView rightDesc;
    }

}

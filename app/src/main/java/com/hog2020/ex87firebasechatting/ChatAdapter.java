package com.hog2020.ex87firebasechatting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends BaseAdapter {
    Context context;
    ArrayList<MessageItem> messageItems;

    public ChatAdapter(Context context, ArrayList<MessageItem> messageItems) {
        this.context = context;
        this.messageItems = messageItems;
    }

    @Override
    public int getCount() {
        return messageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //현재 보여줄 번째(position)의 데이터를 얻어오기
        MessageItem item = messageItems.get(position);

        //재활용할 뷰 (converter view) 사용하지 않을 것임
        View itemview= null;

        LayoutInflater inflater = LayoutInflater.from(context);
        //메세지가 내 메세지 인지..
        if (item.name.equals(G.nickname)){
            itemview=inflater.inflate(R.layout.mymessagebox,parent,false);
        }else{
            itemview=inflater.inflate(R.layout.othermessagebox,parent,false);
        }

        //bind view : 값 연결
        CircleImageView civ= itemview.findViewById(R.id.iv);
        TextView tvName=itemview.findViewById(R.id.tv_name);
        TextView tvMsg= itemview.findViewById(R.id.tv_msg);
        TextView tvTime=itemview.findViewById(R.id.tv_time);

        tvName.setText(item.name);
        tvMsg.setText(item.message);
        tvTime.setText(item.time);

        Glide.with(context).load(item.profileUrl).into(civ);

        return itemview;
    }
}

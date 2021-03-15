package com.hog2020.ex87firebasechatting;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class ChattingActivity extends AppCompatActivity {

    ArrayList<MessageItem> messageItems= new ArrayList<>();
    ListView listView;
    EditText et;

    ChatAdapter chatAdapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference chatRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        listView= findViewById(R.id.listview);
        et = findViewById(R.id.et);
        chatAdapter= new ChatAdapter(this,messageItems);
        listView.setAdapter(chatAdapter);

        //Firebase Database 에 저장되어 있는 메세지 읽어오기
        firebaseDatabase= FirebaseDatabase.getInstance();
        //'chat' 이라는 노드에 MessageItem 들을 저장 ['chat' 이라는 이름만 별도로 지정하면 여러 채팅방개설도 가능함]
        chatRef=firebaseDatabase.getReference("chat");

        //먼저 send 버튼으로 저장하는 코드부터 작성

        //'chat' 노드에값이 변경되는것을 듣는 리스너
        //addValueEventListener() 는 노드 아래 자식 1개 추가 되어도
        //전체데이터를 모두 읽어들임 그래서 이전 데이터들이 중복됨
        //addChildEventListener() : chat 노드의 자식이 변경되었을 때 그 하나만 읽어들임
       chatRef.addChildEventListener(new ChildEventListener() {
           @Override
           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

               //새로 추가된 데이터값 (DataSnapshot 이 촬영한 값)
               MessageItem item =snapshot.getValue(MessageItem.class);

               //읽어 들인 메세지를 리스트 뷰가 보여주는 대량의데이터에 추가
               messageItems.add(item);

               //리스트뷰 갱신- 리스트뷰가 보여줄 뷰를 만들어내는 아답터에게 요청
               chatAdapter.notifyDataSetChanged();
               listView.setSelection(messageItems.size()-1); //리스트뷰 마지막 위치로 스크롤 이동

           }

           @Override
           public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onChildRemoved(@NonNull DataSnapshot snapshot) {

           }

           @Override
           public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }

    public void clicksend(View view) {
        //firebase DB 에 저장할 데이터들 (닉네임, 메세지, 프로필이미지URL, 작성시간)
        String nickName= G.nickname;
        String messsage= et.getText().toString();
        String profileUrl=G.profileUrl;

        //메세지 작성 시간을 문자열 (시,분)
        Calendar calendar = Calendar.getInstance(); //현재 시간객체
        String time=calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE);

        //firebase DB에 MessageItem 객체를통으로 저장하기
        MessageItem item = new MessageItem(nickName,messsage,time,profileUrl);

        //'chat' 노드에 MessageItem 통째로 값 추가(push)
        chatRef.push().setValue(item);

        //다음 메세지 입력이 수월하도록
        et.setText("");

        //소프트 키패드 안보이도록
        InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

    }
}
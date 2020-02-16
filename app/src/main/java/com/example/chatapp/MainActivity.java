package com.example.chatapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.chatapp.Adapters.MessageAdapter;
import com.example.chatapp.Config.ServerConfig;
import com.example.chatapp.Models.ChatContent;
import com.example.chatapp.Util.GlobalApplication;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Socket mSocket;
    {
        try{
            mSocket = IO.socket(ServerConfig.SERVER_URI);
        }catch (URISyntaxException e){
            e.printStackTrace();
        }
    }
    GlobalApplication application;
    ListView messages_view;
    ImageButton btn_send;
    EditText editText;
    private Emitter.Listener getUserInfo = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];
                    Log.e("Message: ", data.toString());
                    Toast.makeText(MainActivity.this, data.toString(), Toast.LENGTH_LONG).show();

                    List<String> lstFriend = new ArrayList<>();
                    lstFriend.add(application.getLstFriend().get(0));

                    //mSocket.emit("test",lstFriend);


                    String[] lst = new String[lstFriend.size()];
                    lstFriend.toArray(lst);
                    String jsonString = "{lstFriend: "+arrayToString(lst)+", user: "+application.getUsername()+"}";
                    Log.d("json", jsonString);
                    JSONObject jsonData = null;
                    try {
                        jsonData = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mSocket.emit("get-user-online-friend-1",jsonData);
                }
            });
        }
    };
    private Emitter.Listener getUserMess = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONArray data = (JSONArray) args[0];
                    Log.d("JSON",data.toString());

                    for(int i =0; i<data.length(); i++) {
                        try {
                            JSONObject jsonObject = data.getJSONObject(i);
                            JSONArray mess = jsonObject.getJSONArray("newest-mess");

                            for(int j = 0; j < mess.length(); j++) {
                                String chat = mess.getJSONObject(i).getString("chat");
                                String send_user = mess.getJSONObject(i).getString("send_user");
                                String timestamp = mess.getJSONObject(i).getString("timestamp");

                                ChatContent content = new ChatContent(timestamp,send_user,chat);
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messages_view = (ListView) findViewById(R.id.messages_view);
        btn_send = (ImageButton) findViewById(R.id.btn_send);
        editText = (EditText) findViewById(R.id.editText);

        MessageAdapter adapter = new MessageAdapter(this);
        messages_view.setAdapter(adapter);


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mess = editText.getText().toString();

            }
        });


        application  = (GlobalApplication) getApplicationContext();
        mSocket.connect();
        getUserFriendAndMess();
        mSocket.on("get-my-information", getUserInfo);
        mSocket.on("get-user-online-friend-1", getUserMess);

    }
    private void getUserFriendAndMess(){
        try{
            mSocket.emit("user", application.getUserId());

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String arrayToString(String[] arr){
        String toString = "[";
        for(int i = 0; i<arr.length; i++){
            toString += arr[i];
        }
        toString+="]";
        return toString;
    }
}

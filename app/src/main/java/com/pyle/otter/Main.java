package com.pyle.otter;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;


public class Main extends AppCompatActivity {

    private Emitter.Listener onMessage = new Emitter.Listener() {
        @Override
        public void call (final Object... args) {
            runOnUiThread( new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    try {
                        Log.d("NETWORK", "MESSAGE RECEIVED");
                        String message = data.getString("message");
                        Log.d("NETWORK", message);
                        chatArrayAdapter.add(new ChatMessage(false, message));
                    } catch (JSONException e) {
                        Log.d("NETWORK", "ERROR PARSING JSON MESSAGE");
                    }
                }
            });
        }
    };

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://sethpyle.com:3000");
            Log.d("NETWORK", "CONNECTION ESTABLISHED");
        } catch (URISyntaxException e)
        {
            Log.d("NETWORK", "COULDNT CONNECT SOCKET TO GIVEN URL");
        }
    }

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSocket.on("message", onMessage);
        mSocket.connect();

        buttonSend = (Button) findViewById(R.id.send);
        listView = (ListView) findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });
    }

    private boolean sendChatMessage() {

        chatArrayAdapter.add(new ChatMessage(true, chatText.getText().toString()));

        mSocket.emit("message", chatText.getText().toString());

        chatText.setText("");
        return true;
    }
}






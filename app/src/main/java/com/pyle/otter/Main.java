package com.pyle.otter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
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
                        TextView view = (TextView)findViewById(R.id.message);
                        view.setText(message);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSocket.on("message", onMessage);
        mSocket.connect();

    }
}

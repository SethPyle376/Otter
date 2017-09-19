package com.pyle.otter;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

import com.github.nkzawa.socketio.client.Socket;

/**
 * Created by Owner on 9/18/2017.
 */

public class LoginActivity extends AppCompatActivity {

    private EditText usernameView;

    private String username;
    private Socket mSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

}

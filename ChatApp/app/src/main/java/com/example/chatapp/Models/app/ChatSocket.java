package com.example.chatapp.Models.app;

import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class ChatSocket {

    private Socket chatSocket;
    {
        try {
            chatSocket = IO.socket("http://10.0.0.185:8900/");
            Log.i("Testing Socket", "Connects");
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
    }

    public Socket getSocket() {
        return chatSocket;
    }

}

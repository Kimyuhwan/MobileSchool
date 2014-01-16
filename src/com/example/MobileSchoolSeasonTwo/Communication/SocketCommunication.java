package com.example.MobileSchoolSeasonTwo.Communication;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.example.MobileSchoolSeasonTwo.BaseMethod;
import com.example.MobileSchoolSeasonTwo.Utils.Constants;
import com.example.MobileSchoolSeasonTwo.Utils.GlobalApplication;
import com.example.MobileSchoolSeasonTwo.Utils.ServerMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * User: yuhwan
 * Date: 13. 11. 19
 * Time: 오후 9:51
 */
public class SocketCommunication {

    private String TAG = Constants.TAG;

    private Context context;
    private Fragment dialogueFragment;
    private GlobalApplication globalApplication;

    // Socket
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader reader;
    private boolean running = true;

    private String matchId;

    public SocketCommunication(Context context, Fragment dialogueFragment) {
        this.context = context;
        this.dialogueFragment = dialogueFragment;
        this.globalApplication = (GlobalApplication) context.getApplicationContext();
    }

    public void socketInit() {
        if(globalApplication.getAccountManager().getMyInfo() == null || globalApplication.getPartnerInfo() == null)
            Log.d(TAG, "Error! socketInit : Information is NULL");
        else {
            Log.d(TAG, "Socket Init");
            this.matchId = globalApplication.getSession_id();
            connectThread.start();
            readThread.start();
        }
    }

    public boolean _checkReady() {
        if(socket != null && writer != null && reader != null)
            return true;
        else
            return false;
    }

    public void socketFinish(){
        if(socket != null) {
            if(!socket.isClosed()){
                Log.d(TAG, "Socket Finish");
                try {
                    socket.shutdownInput();
                    socket.shutdownOutput();
                    reader.close();
                    writer.close();
                    socket.close();
                } catch (IOException e) { e.printStackTrace(); }
            }
        }
    }

    public void sendMsg(String text){
        JSONObject object = new JSONObject();
        try {
            object.put("type", "data");
            object.put("match_id", matchId);
            object.put("sender_id", globalApplication.getAccountManager().getMyInfo().getUnique_id());
            object.put("message", text);

            writer.write(object.toString());
            writer.flush();
            Log.d(TAG, "Socket Send Message object : " + object);
        }catch (IOException e) { e.printStackTrace(); }
         catch (JSONException e) { e.printStackTrace(); }
    }

    private Thread connectThread = new Thread(){
        public void run(){
            try {
                socket = new Socket(ServerMessage.SERVER_IP, ServerMessage.SOCKET_PORT);
                writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                running = true;

                JSONObject object = new JSONObject();
                object.put("type", "init");
                object.put("match_id", matchId);
                object.put("sender_id", globalApplication.getAccountManager().getMyInfo().getUnique_id());
                object.put("message", "");

                writer.write(object.toString());
                writer.flush();
                Log.d(TAG, "ConnectedThread send : " + object);
            } catch (UnknownHostException e) {
                Log.d(TAG, "ConnectThread run " + this.toString() + " UnknownHostException : " + e.toString());
            } catch (IOException e) {
                Log.d(TAG, "ConnectThread run " + this.toString() + " IOException : " + e.toString());
            } catch (JSONException e) {
                Log.d(TAG, "ConnectThread run " + this.toString() + " JSONException : " +  e.toString());
            }
        }
    };

    private Thread readThread = new Thread(){
        public void run() {
            String line;
            while(running) {
                try {
                    if(reader != null) {
                        if((line = reader.readLine()) != null){
                            if(line.contains("close"))  running = false;
                            Log.d(TAG, "Message" + line);
                            mHandler.obtainMessage(10, line).sendToTarget();
                        }
                        else {
                            running = false;
                        }
                    }
                } catch (IOException e) {
                    Log.d(TAG, "StudentActivity read thread error " + e.toString());
                    break;
                }
            }
        }
    };

    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:{
                    String message = _extractMessage(msg.obj.toString());
                    ((BaseMethod) dialogueFragment).handleSocketMessage(message);
                }
            }
        }
    };

    private String _extractMessage(String json) {
        if (json.contains(":")) {
            String result = json.split("message")[1];
            result = result.substring(3, result.length() - 2);
            return result;
        } else {
            return json;
        }
    }
}

package srk.syracuse.gameofcards.Connections;


import android.os.Bundle;
import android.os.Message;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import srk.syracuse.gameofcards.Fragments.MainFragment;
import srk.syracuse.gameofcards.Model.Game;
import srk.syracuse.gameofcards.Utils.Constants;

public class ClientListenerThread extends Thread {

    Socket socket;

    ClientListenerThread(Socket soc) {
        socket = soc;
    }

    @Override
    public void run() {
        try {
            while (true) {
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                Bundle data = new Bundle();
                Object serverObject = objectInputStream.readObject();
                if (serverObject != null) {
                    if (serverObject instanceof String) {
                        data.putSerializable(Constants.DATA_KEY, (String) serverObject);
                        data.putInt(Constants.ACTION_KEY, Constants.UPDATE_GAME_NAME);
                    }
                    if (serverObject instanceof Game) {
                        data.putSerializable(Constants.DATA_KEY, (Game) serverObject);
                    }
                    Message msg = new Message();
                    msg.setData(data);
                    MainFragment.clientHandler.sendMessage(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

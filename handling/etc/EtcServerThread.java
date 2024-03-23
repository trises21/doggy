package handling.etc;

import client.MapleClient;
import handling.ServerType;
import handling.SessionOpen;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.GeneralThreadPool;

import tools.SystemUtils;

public class EtcServerThread extends Thread {

    protected ServerSocket _serverSocket;
    private static Logger _log = Logger.getLogger(EtcServerThread.class
            .getName());

    @Override
    public void run() {

        System.out.println("[VaconMaple] Etc Server Thread Started. Memory used : "
                + SystemUtils.getUsedMemoryMB()+"MB");

        while (true) {
            try {
                Socket socket = _serverSocket.accept();
//                System.out.println("New Connection from "
//                        + socket.getInetAddress());
                String host = socket.getInetAddress().getHostAddress();
                if (SessionOpen.sessionOpen(host, ServerType.ETC, -5)) {
                    // Session OK!
                    MapleClient client = new MapleClient(socket, -5, true);
                    GeneralThreadPool.getInstance().execute(client);
                } else {
                    // Session Failed or Banned
                    _log.log(Level.INFO, "Session Opening Failed on ({0})", host);
                }
            } catch (IOException ioe) {
            }
        }
    }
}

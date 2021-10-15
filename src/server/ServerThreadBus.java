/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Admin
 */
public class ServerThreadBus {
    private List<ServerThread> listServerThreads;

    public List<ServerThread> getListServerThreads() {
        return listServerThreads;
    }

    public ServerThreadBus() {
        listServerThreads = new ArrayList<>();
    }

    public void add(ServerThread serverThread){
        listServerThreads.add(serverThread);
    }
    
    public void mutilCastSend(String message){ //like sockets.emit in socket.io
        for(ServerThread serverThread : Server.serverThreadBus.getListServerThreads()){
            try {
                serverThread.write(message);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void boardCast(int id, String message){
        for(ServerThread serverThread : Server.serverThreadBus.getListServerThreads()){
            if (serverThread.getClientNumber() == id) {
                continue;
            } else {
                try {
                    serverThread.write(message);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    public int getLength(){
        return listServerThreads.size();
    }
    
    public void sendOnlineList(){
        String res = "";
        List<ServerThread> threadbus = Server.serverThreadBus.getListServerThreads();
        for(ServerThread serverThread : threadbus){
            res+=serverThread.getClientNumber()+"-";
        }
        Server.serverThreadBus.mutilCastSend("update-online-list"+","+res);
    }
    public void sendMessageToPersion(int id, String message){
        for(ServerThread serverThread : Server.serverThreadBus.getListServerThreads()){
            if(serverThread.getClientNumber()==id){
                try {
                    serverThread.write("global-message"+","+message);
                    break;
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    public void remove(int id){
        for(int i=0; i<Server.serverThreadBus.getLength(); i++){
            if(Server.serverThreadBus.getListServerThreads().get(i).getClientNumber()==id){
                Server.serverThreadBus.listServerThreads.remove(i);
            }
        }
    }
}

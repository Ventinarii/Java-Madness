package Comms;
import Game.Player;
import Stages.App;
import Stages.MyErrorPopup;
import javafx.scene.control.TextArea;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Gate{
    public Player player;
    private Socket socket;
    public ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Thread thread;
    public long lastUsed = System.currentTimeMillis();

    public static Gate getNewGate(Player player){
        if(App.gates.stream().anyMatch(gate -> gate.player.equalsNET(player))){
            TextArea textArea = new TextArea(player.toString());
            textArea.setPrefSize(512, 512);
            MyErrorPopup myError = new MyErrorPopup("Comms Error => this gate already exists", textArea, false);
        }else {
            Socket socket = player.getSocket();
            if (socket != null)
                try {
                    Gate gate = new Gate(player, socket);
                    System.out.println(App.gates);
                    return gate;
                } catch (Exception ex) {
                    TextArea textArea = new TextArea(ex.toString());
                    textArea.setPrefSize(512, 512);
                    MyErrorPopup myError = new MyErrorPopup("Comms Error => fail to complete", textArea, false);
                    App.players.remove(player);
                    App.toPlayPlayers.remove(player);
                    Box.inThisGame.remove(player);
                }
        }
        return null;
    }//DONE
    public static void gotNewGate(Socket socket){
        Thread tmp = new Thread(()->{
            Gate gate = null;
            try{
                gate = new Gate(socket);
            }catch (Exception ex) {
                TextArea textArea = new TextArea(ex.toString());
                textArea.setPrefSize(512, 512);
                MyErrorPopup myError = new MyErrorPopup("Comms Error => fail to complete", textArea, false);
            }
            if(gate!=null) {
                App.gates.add(gate);
                System.out.println(App.gates);
            }
        });
        tmp.setDaemon(true);
        tmp.start();
    }//DONE
    private Gate(Socket socket) throws  IOException{
        socket.setKeepAlive(true);
        player = Player.getNewPlayer(null,socket.getRemoteSocketAddress().toString(),socket.getPort());
        this.socket=socket;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());

        try {
            Box box;
            box = ((Box) (objectInputStream.readObject()));
            this.player=box.getSender();
            App.inBox.add(box);
            lastUsed = System.currentTimeMillis();
        } catch (Exception ex) {
            throw new IOException();
        }

        thread = new Thread(() -> {
            try {
                boolean run = true;
                do{
                    Box box;
                    box = ((Box) (objectInputStream.readObject()));
                    App.inBox.add(box);
                    lastUsed = System.currentTimeMillis();
                    if(box.getWhatDoIcarry()==-666)
                        run=false;
                }while (run);
            } catch (Exception ex) {
                System.out.println(ex.toString());
                App.gates.remove(this);
                new Thread(this::dispose).start();
            }
            App.gates.remove(this);
            new Thread(this::dispose).start();
            });
        thread.setDaemon(true);
        thread.start();
    }//DONE
    private Gate(Player player, Socket socket) throws IOException{
        socket.setKeepAlive(true);
        this.player=player;
        this.socket=socket;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        thread = new Thread(() -> {
            try {
                boolean run = true;
                do{
                    Box box;
                    box = ((Box) (objectInputStream.readObject()));
                    App.inBox.add(box);
                    lastUsed = System.currentTimeMillis();
                    if(box.getWhatDoIcarry()==-666)
                        run=false;
                }while (run);
            } catch (Exception ex) {
                System.out.println(ex.toString());
                App.gates.remove(this);
                new Thread(this::dispose).start();
            }
            App.gates.remove(this);
            new Thread(this::dispose).start();
            });
        thread.setDaemon(true);
        thread.start();
    }//DONE
    public void dispose(){
        System.out.println("gate "+this+" is beign disposed");
        App.gates.remove(this);
        thread=null;
        player=null;
        try {
            objectOutputStream.close();
            objectOutputStream = null;
            objectInputStream.close();
            objectInputStream = null;
            socket.close();//exeption prone; may cause mess in comms
        }catch (Exception ex){
            ex.printStackTrace();
        }
        socket=null;
    }//DONE

    @Override
    public String toString() {
        return super.toString()+" "+player;
    }
}

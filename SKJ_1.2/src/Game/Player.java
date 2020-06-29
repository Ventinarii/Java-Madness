package Game;
import Comms.Box;
import Stages.App;

import java.io.Serializable;
import java.net.Socket;
import java.util.List;
import java.util.stream.Collectors;

public class Player implements Serializable, Comparable<Player>{
    private final String nick;
    private final String ip;
    private final int port;
    public boolean inTheGame = false;
    public static Player getNewPlayer(String nick, String ip, int port){
        if(nick==null)
            nick="defalut_nick";
        return new Player(nick,ip,port);
    }//DONE
    private Player(String nick, String ip, int port){
        this.nick=nick;
        this.ip=ip;
        this.port=port;
    }//DONE
    public Socket getSocket(){
        try {
            return new Socket(ip, port);
        }catch (Exception ex){
            //TextArea textArea = new TextArea(ex.toString());
            //textArea.setPrefSize(512, 512);
            //MyErrorPopup myError = new MyErrorPopup("Comms Error => fail to complete to get sock", textArea, false);
            System.out.println("player method: get socket failure");
            Box.disconnect(this);
            App.players.remove(this);
            App.toPlayPlayers.remove(this);
            Box.inThisGame.remove(this);
            return null;
        }
    }//DONE
    @Override
    public String toString() {
        return ip+" "+port+" "+nick+" "+((inTheGame)?("G"):("F"));
    }//DONE
    public boolean equalsNET(Player player){
        return (player.ip.equals(this.ip)&&player.port==this.port);
    }//DONE
    public static Player getPlayer(String n, String i, String p){
        if(p==null)
            p="playerSelectedNumber";
        if(n!=null){
            List<Player> list = App.players.stream().filter(player -> player.nick.equals(n)).collect(Collectors.toList());
            if(list.size()==0)
                return null;
            return list.get(0);
        }else if(i!=null&&p.matches("\\d+")) {
            Player player = new Player("defalut_nick",i,Integer.parseInt(i));
            List<Player> list = App.players.stream().filter(pl -> pl.equalsNET(player)).collect(Collectors.toList());
            if(list.size()==0)
                return player;
            return list.get(0);
        }else{
            System.out.println("could't find player witch params: "+i+" # "+p);
            return null;
        }
    }//DONE

    @Override
    public int compareTo(Player o) {
        int x;
        x = this.ip.compareTo(o.ip);
        if(x==0) x = this.port-o.port;
        return x;
    }
}

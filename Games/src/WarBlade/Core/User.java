package WarBlade.Core;

import javafx.scene.Group;
import javafx.scene.text.Text;

public class User extends Group implements Comparable<User>{
    private String Nick;
    private long pkt;
    public User(String Nick, long pkt){
        this.Nick = new String(Nick);
        this.pkt = pkt;
    }

    @Override
    public String toString() {
        return Nick+" with moneyz: "+pkt+"$";
    }

    public long getPkt() {
        return pkt;
    }
    @Override
    public int compareTo(User o) {
        if(o.getPkt()>this.pkt)return 1;
        if(o.getPkt()<this.pkt)return -1;
        return 0;
    }
}

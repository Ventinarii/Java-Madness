package Game;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Result implements Serializable {
    public Player winner;
    public final ArrayList<Player> loosers = new ArrayList<>();
    public long uniqeID; //of a given GAME
    @Override
    public String toString() {
        return uniqeID+" winner "+winner+" & loosers:"+loosers.stream().map(Player::toString).collect(Collectors.joining(System.lineSeparator()))+System.lineSeparator();
    }
}//send by both sides, each client needs to receive both to account it;

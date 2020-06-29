package Stages;
import Comms.Box;
import Comms.Gate;
import Game.Player;
import Game.Result;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Chat extends Stage{
    public static final Chat chat = new Chat();
    public static void getChat(){
        chat.show();
    }//DONE
    private final Group root = new Group();
    public final TextArea feed;
    private final TextArea writing;
    private static final double w=1000,h=1000,sP0=9,sP1=10;
    private Chat(){
        setWidth(w);
        setHeight(h);
        setScene(new Scene(root,w,h));
        setTitle(App.me.toString());
        setOnCloseRequest((event)-> hide());
        feed = new TextArea();
        writing = new TextArea();
        chatRoomL();
        }//DONE
    private void chatRoomL(){
        feed.setPrefSize(w,(h*sP0/sP1));
        feed.setEditable(false);
        feed.setWrapText(true);
        feed.setFont(new Font("Lucida Console",15));
        root.getChildren().add(feed);

        writing.setPrefSize(w,h*(1-(sP0/sP1)));
        writing.setEditable(true);
        writing.setWrapText(true);
        writing.setLayoutY(h*sP0/sP1);
        writing.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.ENTER)){
                newMessage(writing.getText());
                writing.setText("");
            }
        });
        writing.setFont(new Font("Lucida Console",15));
        root.getChildren().add(writing);

        feed.appendText(displayManual());
    }//DONE
    private void newMessage(String string){
        feed.appendText(App.me.toString()+"    "+string);
        if(string.getBytes()[0]=='/')
            newCommand(string);
        else
            Box.sendMessage(App.me.toString()+"    "+string);
    }//DONE
    private void  newCommand(String string){
        String[] strings = string.replace("\n","").split(" ");
        if(strings[0].equals("/Man"))
            feed.appendText(displayManual());
        else if(strings[0].matches("/StN")&& strings.length==2){
            boolean g = App.me.inTheGame;
            App.me = Player.getNewPlayer(strings[1],App.myIP,App.port);
            App.me.inTheGame=g;
            App.app.stage.setTitle(App.app.title+" "+App.me.toString());
            setTitle(App.me.toString());
            Box.statusChange(0);
        }else if(strings[0].matches("/Con")&&strings.length == 3){
            connect(strings[1],strings[2]);
        }
        else if(strings[0].matches("/LAP")) {
            listPlayers();
        }else if(strings[0].matches("/kill")){
            System.exit(-1);
        } else feed.appendText("-System: Unknown command. "+string);
    }//DONE
    private String displayManual(){
        return
                "-System: This is chat. you can do \"a lot\" of things here. currnetly implemented commands:"+System.lineSeparator()+
                "_________/Man______________________shows this"+System.lineSeparator()+
                "_________/StN_@nick________________sets you new nick."+System.lineSeparator()+
                "_________/Con_@ip_@port____________connects to a given client to get list of players"+System.lineSeparator()+
                "_________/LAP______________________lists all players"+System.lineSeparator();
    }//DONE
    private void connect(String ip,String port){
        if(port.matches("[\\d]+")) {
            int intPort = Integer.parseInt(port);
            Box.fetchPlayers(ip,intPort);
        }else {
            feed.appendText("-System: Syntax Error =>port invalid (Parse)");
        }
    }//DONE
    private void listPlayers(){
        feed.appendText("System: players");
        App.players.stream().map(Player::toString).forEach(t->feed.appendText("System: "+t+System.lineSeparator()));
        feed.appendText(System.lineSeparator());
        feed.appendText("System: gates");
        App.gates.stream().map(Gate::toString).forEach(t->feed.appendText("System: "+t+System.lineSeparator()));
        feed.appendText(System.lineSeparator());
        feed.appendText("System: broken Boxes");
        App.shelfBox.stream().map(Box::toString).forEach(t->feed.appendText("System: "+t+System.lineSeparator()));
        feed.appendText(System.lineSeparator());
        feed.appendText("System: results");
        App.results.stream().map(Result::toString).forEach(t->feed.appendText("System: "+t+System.lineSeparator()));
        feed.appendText(System.lineSeparator());
    }//DONE
    public static void kill(){
        if (chat!=null)
            chat.hide();
    }//DONE
}
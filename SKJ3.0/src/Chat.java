import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Chat extends Stage{
    public  static final Chat chat = new Chat();
    public  static       void getChat(){
        chat.show();
    }//DONE
    public               TextArea feed = new TextArea();
    private              TextArea writing = new TextArea();
    private static final double w=1000,h=1000,sP0=9,sP1=10, rand = 16;
    private              Chat(){
        setWidth(w);
        setHeight(h);
        Group root = new Group();
        setScene(new Scene(root,w,h));
        setTitle(((App.server)?("Server "):("Client "))+App.stage.getTitle());
        setOnCloseRequest((event)-> App.exitFun());

        feed.setPrefSize(w-rand,(h*sP0/sP1)-rand);
        feed.setEditable(false);
        feed.setWrapText(true);
        feed.setFont(new Font("Lucida Console",15));
        root.getChildren().add(feed);

        writing.setPrefSize(w-rand,h*(1-(sP0/sP1))-rand);
        writing.setEditable(true);
        writing.setWrapText(true);
        writing.setLayoutY(h*sP0/sP1-rand);
        writing.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.TAB)){
                if(writing.getText().startsWith("/"))
                    newCommand(writing.getText());
                else
                    newMessage(writing.getText());
                writing.setText("");
            }
        });
        writing.setFont(new Font("Lucida Console",15));
        root.getChildren().add(writing);

        feed.appendText(displayManual());
    }//DONE
    private              void newCommand(String string){
        if(string.startsWith("/Man ")) {
            feed.appendText(displayManual());
        }else if(string.startsWith("/Con")&&!App.server){
            auth(string);
        }else if(string.startsWith("/LAP")) {
            lAP();
        }else if(string.startsWith("/UDP")) {
            try{
                String[] mess=string.split(" ");
                mess[3]=App.me+" PMvUDP "+mess[1]+" "+mess[2]+" >> "+string.split(">>")[1]+System.lineSeparator();
                feed.appendText(mess[3]);
                Packet.sendMessage(mess[3],mess[1],Integer.parseInt(mess[2]));
            }catch (Exception ex){feed.appendText(ex.toString());}
        }else
            feed.appendText("-System: Unknown command. "+string);
    }//DONE
    private              String displayManual(){
        return
                "-System: This is chat. you can do \"a lot\" of things here. currnetly implemented commands:"+System.lineSeparator()+
                "_________/Man______________________shows this"+System.lineSeparator()+
                "_________/Con_sequnece_@i_{@p,@k}__connects to a given client to get list of CONNECTIONS"+System.lineSeparator()+
                "_________/LAP______________________lists all CONNECTIONS"+System.lineSeparator()+
                "_________/UDP_@ip_@port >>@message_send message vai udp to specified receiver"+System.lineSeparator()+
                " <TAB>                             used to enter input"+System.lineSeparator();
    }//DONE
    private              void newMessage(String string){
        feed.appendText(App.me+string+System.lineSeparator());
        Packet.sendMessage(App.me+string+System.lineSeparator());
    }//DONE
    private              void auth(String string){
        try {
            String[] tokens = string.replace("\t","").split("[ ]|[,]");
            String ip = tokens[1];
            int[][] keys = new int[(tokens.length - 2) / 2][2];
            for (int x = 0; x < keys.length; x++) {
                keys[x][0] = Integer.parseInt(tokens[(2 + 2 * x)]);
                keys[x][1] = Integer.parseInt(tokens[(2 + 2 * x + 1)]);
            }
            Packet.auth(ip, keys);
        }catch (Exception ex){feed.appendText("System: "+ex.toString());}
    }//DONE
    private              void lAP(){
        feed.appendText(
                "System: "+Packet.lAP()+System.lineSeparator()+
                "System: "+Gate.lAP()+System.lineSeparator()
        );
    }//DONE
    public  static       void clientCFG(){

    }//==========================================================================>
    public  static       void serverCFG(){

    }//==========================================================================>
}
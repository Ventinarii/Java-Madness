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
        setTitle(App.title+" "+App.me+" "+"Unpaired");
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
        string=string.replace("\t","");
              if(string.startsWith("/Man ")) {feed.appendText(displayManual());
        }else if(string.startsWith("/LAP")){lAP();
        }else if(string.startsWith("/Rig")){
            try{
                String[] mess=string.split(" ");
                String ip0=mess[1],ip1=mess[3];
                int    po0=Integer.parseInt(mess[2]),po1=Integer.parseInt(mess[4]);
                Packet.rig(ip0,po0,ip1,po1);
            }catch (Exception ex){feed.appendText(ex.toString());}
        }else if(string.startsWith("/Lew")) {
            try{
                String[] mess=string.split(" ");
                String ip0=mess[1],ip1=mess[3];
                int    po0=Integer.parseInt(mess[2]),po1=Integer.parseInt(mess[4]);
                Packet.lew(ip0,po0,ip1,po1);
            }catch (Exception ex){feed.appendText(ex.toString());}
        }else
            feed.appendText("-System: Unknown command. "+string);
    }//DONE
    private              String displayManual(){
        return
                "-System: This is chat. you can do \"a lot\" of things here. currently implemented commands:"+System.lineSeparator()+
                "_________/Man______________________shows this"+System.lineSeparator()+
                "_________/Rig_@ip_@port_@ip_@port__K-R=K"+System.lineSeparator()+
                "_________/Lew_@ip_@port_@ip_@port__K=R-K"+System.lineSeparator()+
                "_________/LAP______________________lists all CONNECTIONS"+System.lineSeparator()+
                "<TAB>                              used to enter input"+System.lineSeparator();
    }//DONE
    private              void newMessage(String string){
        feed.appendText(App.me+string+System.lineSeparator());
        Packet.sendMessage(App.me+string+System.lineSeparator());
    }//DONE
    private              void lAP(){
        feed.appendText(
                "System: "+Packet.lAP()+System.lineSeparator()+
                "System: "+Gate.lAP()+System.lineSeparator()
        );
    }//DONE
}
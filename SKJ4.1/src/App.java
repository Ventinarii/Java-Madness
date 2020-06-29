import javafx.application.*;
import javafx.scene.text.*;
import javafx.stage.*;

import java.net.*;

public class App extends Application {
    //================================================================================================================== INIT
        public  static       void main(String args[]){launch(args);}//DONE
        @Override
        public               void start(Stage primaryStage){cfg(primaryStage);}//DONE
        public  static       NetPath me;
        public  static final String title = "SKJ_4.1+";
        private              void cfg(Stage stage) {
            try{
                me = new NetPath(InetAddress.getLocalHost().getHostAddress().trim(),(int) (Math.random() * 8000 + 1024));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            Gate.gateCFG();
            //========================================================================================================== Fast Boot
            Chat.getChat();
        }//DONE
        public  static       void exitFun() {
            Packet.terminalOperations();
            Platform.exit();
            System.exit(0);
        }//DONE
}

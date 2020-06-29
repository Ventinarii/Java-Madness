import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.stage.*;

import java.net.*;

public class App extends Application {
    //================================================================================================================== INIT
        public  static       void main(String args[]) throws Exception{
            if(args.length!=0)
                App.args=args;
            launch(args);
        }//DONE
        @Override
        public               void start(Stage primaryStage) {
            cfg(primaryStage);
            menu();
        }//DONE
    //================================================================================================================== GUI
        public  static       App app;
        public  static       NetPath me;
        private static final int width = 1000;
        private static final int height = 1000;
        private static final int sizeX = 200;
        private static final int sizeY = 40;
        private static final int gap = 50;
        private static       Group root;
        public  static       Stage stage;
        public  static final String title = "SKJ_3.1+";
        public  static final Font menuFont = new Font("Comic Sans MS", 20);
        private static       String[] args;
        public  static       boolean server;
    //================================================================================================================== CODE
    //CONFIG functions
        private              void cfg(Stage stage) {
            app = this;
            root = new Group();
            this.stage = stage;
            stage.setOnCloseRequest(event -> exitFun());
            stage.setScene(new Scene(root, width, height));
            stage.show();
            try{
                me = new NetPath(InetAddress.getLocalHost().getHostAddress().trim(),(int) (Math.random() * 8000 + 1024));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            stage.setTitle(title + " " + me);
            Gate.gateCFG();
        }//DONE
    //GUI Functions
        public               void menu() {
                Button node = new Button("Client");
                node.setFont(menuFont);
                node.setPrefSize(sizeX, sizeY);
                node.setLayoutX(width / 2 - sizeX / 2);
                node.setLayoutY(512 - gap);
                node.setOnAction(event -> {
                    root.getChildren().removeAll(root.getChildren());
                    stage.hide();
                    client();
                });
                root.getChildren().add(node);
                Button hub = new Button("Server");
                hub.setFont(menuFont);
                hub.setPrefSize(sizeX, sizeY);
                hub.setLayoutX(width / 2 - sizeX / 2);
                hub.setLayoutY(512);
                hub.setOnAction(event -> {
                    root.getChildren().removeAll(root.getChildren());
                    stage.hide();
                    server();
                });
                root.getChildren().add(hub);
                Button exit = new Button("Exit");
                exit.setFont(menuFont);
                exit.setPrefSize(sizeX, sizeY);
                exit.setLayoutX(width / 2 - sizeX / 2);
                exit.setLayoutY(512 + gap);
                exit.setOnAction(event -> {
                    root.getChildren().removeAll(root.getChildren());
                    exitFun();
                });
                root.getChildren().add(exit);
        }//DONE
        private              void client(){
            Chat.clientCFG();
            Chat.getChat();
            Gate.clientCFG();
            server=false;
        }//DONE
        private              void server(){
            server=true;
            Chat.serverCFG();
            Packet.serverCFG();
            Chat.getChat();
            Gate.serverCFG();

        }//DONE
        public  static       void exitFun() {
            Packet.terminalOperations();
            Platform.exit();
            System.exit(0);
                        }//DONE
}

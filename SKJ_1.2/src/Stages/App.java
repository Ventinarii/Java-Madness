package Stages;

import Comms.*;
import Game.Player;
import Game.Result;
import javafx.animation.*;
import javafx.application.*;
import javafx.collections.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.*;
import javafx.stage.*;
import org.controlsfx.control.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.stream.*;

public class App extends Application {
    //================================================================================================================== INIT
        public static void main(String args[]) {
            if(args.length!=0)
                App.args=args;
            launch(args);
        }
        @Override
        public void start(Stage primaryStage) {
            cfg(primaryStage);
            menu();
        }
    //================================================================================================================== GUI
        public static App app;
        private final int width = 1000;
        private final int height = 1000;
        private final int sizeX = 200;
        private final int sizeY = 40;
        private final int gap = 50;
        public Group root;
        public Stage stage;
        public final String title = "Marynaż";
        public static final Font menuFont = new Font("Comic Sans MS", 20);
    //================================================================================================================== GAME
        private static String[] args;
        public static Player me = Player.getNewPlayer("null","null",0000);
        public static final ArrayList<Player> toPlayPlayers = new ArrayList<>();
        public static final ArrayList<Player> players = new ArrayList<>();
        public static final List<Result> results = new ArrayList<>();
        public static final ArrayList<Gate> gates = new ArrayList<>();
        public static final ArrayList<Box> inBox = new ArrayList<>();
        public static final ArrayList<Box> outBox = new ArrayList<>();
        public static final ArrayList<Box> shelfBox = new ArrayList<>();//if box want to stay in client and wait for something that's the place.
        public static AnimationTimer postman;//A one very busy postman to keep all of that mess running >> instead of sync
        private static Thread serverSocketHandler;
        public static String myIP;
        public static int port;
    //================================================================================================================== CODE
    //CONFIG functions
        private void cfg(Stage stage) {
            app = this;
            root = new Group();
            Scene scene = new Scene(root, width, height);
            this.stage = stage;
            stage.setOnCloseRequest(event -> exitFun());
            stage.setScene(scene);
            stage.show();
            try {
                myIP = InetAddress.getLocalHost().getHostAddress().trim();
                if(args!=null) {
                    port = Integer.parseInt(args[1].replace(",",""));
                }else
                    port = (int) (Math.random() * 9000 + 1000);
            } catch (Exception exeption) {
                TextArea textArea = new TextArea(exeption.toString());
                textArea.setPrefSize(512, 512);
                MyErrorPopup myErrorPopup = new MyErrorPopup("Faliure to get IP", textArea, true);
                myIP = "127.0.0.1";
                port = 0;
            }
            if (myIP.equals("127.0.0.1")) {
                TextArea textArea = new TextArea(
                        "It seems that you are offline. your 'IP' have value:" + System.lineSeparator() +
                                "127.0.0.1" + System.lineSeparator() +
                                "what you can do:" + System.lineSeparator() +
                                "-check your internet connection" + System.lineSeparator() +
                                "-reconnect and get different IP" + System.lineSeparator() +
                                "-edit source code to ignore the issue (not recommended):" + System.lineSeparator() +
                                "   -go to Stages.App class; cfg method;" + System.lineSeparator() +
                                "   -comment out entire if(!myIP.equals(\"127.0.0.1\")){...}else{...}");
                textArea.setPrefSize(512, 512);
                new MyErrorPopup("Faliure to get proper IP", textArea, true);
            }else {
                postman = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        while (inBox.size() != 0) App.inBox.remove(0).unpack();//odbiera paczki
                        while (outBox.size() != 0) App.outBox.remove(0).pack();//wysyla paczki
                        //if (App.gates.size() != 0) {Gate gate = App.gates.remove(0); if(cleanGatesOfIdles(gate))App.gates.add(gate);}//pozbywa sie nieuzywanych bramek
                    }
                };
                postman.start();
                serverSocketHandler = new Thread(() -> {
                    try {
                        ServerSocket serverSocket = new ServerSocket(port);
                        while (true)
                            Gate.gotNewGate(serverSocket.accept());
                    } catch (Exception ex) {
                        TextArea textArea = new TextArea(ex.toString());
                        textArea.setPrefSize(512, 512);
                        new MyErrorPopup("ServerSocket Failure", textArea, true);
                    }
                });
                serverSocketHandler.setDaemon(true);
                serverSocketHandler.start();
                me = Player.getNewPlayer(((args == null) ? (null) : (null)), myIP, port);//=======================================
                if (args != null) {
                    Box.fetchPlayers(args[2], Integer.parseInt(args[3].replace(",", "")));
                    Box.statusChange(1);
                }
                stage.setTitle(title + " " + me);
            }
        }//DONE
        private void youHaventPlayedEnough() {
            Label komm = new Label("you haven't played with everyone, please finish game");
            komm.setFont(menuFont);
            komm.setPrefHeight(sizeY);
            komm.setLayoutX(width / 2 - sizeX);
            komm.setLayoutY(512 - gap);
            root.getChildren().add(komm);

            Button menu = new Button("return to game");
            menu.setFont(menuFont);
            menu.setPrefSize(sizeX, sizeY);
            menu.setLayoutX(width / 2 - sizeX / 2);
            menu.setLayoutY(512);
            menu.setOnAction(event -> {
                root.getChildren().removeAll(root.getChildren());
                menu();
            });
            root.getChildren().add(menu);

            Button exit = new Button("exit enyway");
            exit.setFont(menuFont);
            exit.setPrefSize(sizeX, sizeY);
            exit.setLayoutX(width / 2 - sizeX / 2);
            exit.setLayoutY(512 + gap);
            exit.setOnAction(event -> {
                Box.terminalOperations();
                root.getChildren().removeAll(root.getChildren());
                Platform.exit();
                System.exit(0);
            });
            root.getChildren().add(exit);
        }//DONE
    //GUI Functions
        public void menu() {
            {//buttons
                Button play = new Button("Play");
                play.setFont(menuFont);
                play.setPrefSize(sizeX, sizeY);
                play.setLayoutX(width / 2 - sizeX / 2);
                play.setLayoutY(512 - gap);
                play.setOnAction(event -> {
                    if (!me.inTheGame) {
                        me.inTheGame=true;
                        Box.statusChange(1);
                        stage.setTitle(title + " " + me);
                    }
                    if(!Box.gameInProgress)
                        if(players.size()==0){
                        Chat.getChat();
                        TextArea textArea = new TextArea("Use /Con command to connect to network");
                        textArea.setPrefSize(512, 512);
                        MyErrorPopup myErrorPopup = new MyErrorPopup("Waring", textArea, false);
                    }else if(players.stream().noneMatch(player -> player.inTheGame)){
                        TextArea textArea = new TextArea("No one in network to play with!");
                        textArea.setPrefSize(512, 512);
                        MyErrorPopup myErrorPopup = new MyErrorPopup("Waring", textArea, false);
                    }else {
                        root.getChildren().removeAll(root.getChildren());
                        play();
                    }
                });
                root.getChildren().add(play);

                Button settings = new Button("Monitor");
                settings.setFont(menuFont);
                settings.setPrefSize(sizeX, sizeY);
                settings.setLayoutX(width / 2 - sizeX / 2);
                settings.setLayoutY(512);
                settings.setOnAction(event -> {
                    root.getChildren().removeAll(root.getChildren());
                    stage.hide();
                    Chat.getChat();
                    Chat.chat.setOnCloseRequest(event1->{
                        Box.terminalOperations();
                        Platform.exit();
                        System.exit(0);
                    });
                    Box.statusChange(2);
                    Box.gameInProgress=true;
                });
                root.getChildren().add(settings);

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

                Button chat = new Button("Chat");
                chat.setFont(menuFont);
                chat.setPrefSize(sizeX, sizeY);
                chat.setLayoutX(width / 2 - sizeX / 2);
                chat.setLayoutY(512 + 4 * gap);
                chat.setOnAction(event -> Chat.getChat());
                root.getChildren().add(chat);
            }
        }//DONE
        //Menu functions
            private void play() {
                    final CheckListView<Player> checkListView = new CheckListView<>(FXCollections.observableArrayList(players.stream().filter(player -> player.inTheGame).collect(Collectors.toList())));
                    checkListView.setPrefSize(width,height-sizeY);
                    root.getChildren().add(checkListView);
                    Button sendTo = new Button("Send");
                    sendTo.setFont(menuFont);
                    sendTo.setPrefSize(sizeX, sizeY);
                    sendTo.setLayoutY(height-sizeY);
                    sendTo.setOnAction(event -> {
                        List<Player> list = checkListView.getCheckModel().getCheckedItems();
                        if(list.size()>0){
                            root.getChildren().removeAll(root.getChildren());
                            menu();
                            Thread thread = new Thread(()->Box.inviteToGame(list));
                            thread.setDaemon(true);
                            thread.start();
                            System.out.println("prosze czekac! inicjalizacja rozgrywki: "+Box.uniqueID);
                        }
                    });
                    root.getChildren().add(sendTo);
                    Button exit = new Button("BACK");
                    exit.setFont(menuFont);
                    exit.setPrefSize(sizeX, sizeY);
                    exit.setLayoutX(width-sizeX);
                    exit.setLayoutY(height-sizeY);
                    exit.setOnAction(event -> {
                        root.getChildren().removeAll(root.getChildren());
                        menu();
                    });
                    root.getChildren().add(exit);
                }//DONE
                public void play2(){
                    TextArea description = new TextArea("put val in window below and press enter. after 5 sec system sends val 0");
                    description.setFont(menuFont);
                    description.setEditable(false);
                    description.setPrefSize(width,sizeY);
                    description.setLayoutY(width/2-sizeY);
                    root.getChildren().add(description);

                    TextArea val = new TextArea();
                    val.setFont(menuFont);
                    val.setEditable(true);
                    val.setPrefSize(width,sizeY);
                    val.setLayoutY(width/2);
                    val.setOnKeyPressed(event -> {
                        String str = val.getText().trim();
                        if(event.getCode().equals(KeyCode.ENTER)&&(str.matches("[\\d]+"))) {
                            Box.playerSelectedNumber =Byte.parseByte(str);
                            root.getChildren().removeAll(root.getChildren());
                            menu();
                        }
                    });
                    root.getChildren().add(val);
                }//DONE
            private void exitFun() {
                            if (toPlayPlayers.size() != 0)
                                youHaventPlayedEnough();
                            else {
                                Box.terminalOperations();
                                Platform.exit();
                                System.exit(0);
                            }
                        }//DONE
    //util functions
        //NET functions
            private boolean cleanGatesOfIdles(Gate gate) {
                try {
                    long idleTaimeMax = 10000;
                    if ((gate.lastUsed+ idleTaimeMax)<System.currentTimeMillis()) {
                        List<Player> list = new ArrayList<>();
                        list.add(gate.player);
                        Box.terminateConnections(list);
                        return false;
                    }
                }catch (Exception ex){
                    System.out.println(ex.toString());
                    return false;
                }
                return true;
            }//DONE
        //OTHER functions
            public static void kill() {
                app.stage.hide();
                //gates.forEach(Gate::dispose);
                if(postman!=null)
                    postman.stop();
                if(serverSocketHandler!=null)
                    if(serverSocketHandler.isAlive())
                        serverSocketHandler.interrupt();
            }//DONE
}

package Comms;
import Game.Player;
import Game.Result;
import Stages.App;
import Stages.Chat;
import Stages.MyErrorPopup;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Box implements Serializable{
    //UDP
        public static Thread thread;
    //CHAT functions
        public static void sendMessage(String massage){
            App.players.forEach(e -> {
                Box box = new Box();
                box.whatDoIcarry=1;
                box.massage.add(0,App.me);
                box.massage.add(1,e);
                box.massage.add(2,massage);
                App.outBox.add(box);
            });
        }//DONE
    //GAME functions
        public static void statusChange(int status){
                if(status!=0)
                    App.me.inTheGame=((status==1));
                App.players.forEach(e -> {
                    Box box = new Box();
                    box.whatDoIcarry=2;
                    box.massage.add(0,App.me);
                    box.massage.add(1,e);
                    box.massage.add(2,status);
                    App.outBox.add(box);
                });
        }//DONE
        public static void inviteToGame(List<Player> players){
                gameInProgress=true;
                flagAcceptInvites=true;
                playersToInv.removeAll(playersToInv);
                playersToInv.addAll(players);
                uniqueID=System.currentTimeMillis();
                sum=0;
                lockedBoxes.removeAll(Box.lockedBoxes);
                inThisGame.removeAll(Box.inThisGame);
                players.forEach(player -> {
                    Box box = new Box();
                    box.whatDoIcarry = 3;
                    box.massage.add(0,App.me);
                    box.massage.add(1,player);
                    box.massage.add(2,uniqueID);
                    App.outBox.add(box);
                });
                try { Thread.sleep(10000); }catch (Exception ex){ ex.printStackTrace(); }
                flagAcceptInvites=false;
                if(inThisGame.size()==0)
                    gameInProgress = false;
                else
                    sendBlackBox();
        }//DONE
            private static final List<Player> playersToInv = new ArrayList<>();
            private static boolean flagAcceptInvites;
            public static void sendBlackBox(){
            sum=0;
            playerSelectedNumber=0;
            System.out.println("game on with"+System.lineSeparator()+inThisGame);
            App.toPlayPlayers.removeAll(inThisGame.stream().filter(inthisgameplayer->App.toPlayPlayers.stream().anyMatch(toplaypalyersplayer->inthisgameplayer.equalsNET(toplaypalyersplayer))).collect(Collectors.toList()));
            inThisGame.forEach(player -> {
                    Box box = new Box();
                    box.whatDoIcarry = 23;
                    box.massage.add(0, App.me);
                    box.massage.add(1, player);
                    box.massage.add(2, inThisGame);
                    App.outBox.add(box);
                });//send all who are they playing with
            //======================================================================================================//others are offset in time by 1-3 sec but go the following code:
            geerateKEY();
            {
                Box box = new Box();
                box.whatDoIcarry = 900;
                box.massage.add(0, null);
                box.massage.add(1, null);
                box.massage.add(2, new Consumer<>() {
                    @Override
                    public void accept(Object o) {
                        App.app.root.getChildren().removeAll(App.app.root.getChildren());
                        App.app.play2();
                    }
                });
                box.massage.add(3, null);
                App.inBox.add(box);
                try { Thread.sleep(10000); } catch (Exception ex) { System.out.println(ex); }//waiting for player
                box.massage.add(2, new Consumer<>() {
                    @Override
                    public void accept(Object o) {
                        App.app.root.getChildren().removeAll(App.app.root.getChildren());
                        App.app.menu();
                    }
                });
                box.massage.remove(3);
                App.inBox.add(box);
            }
            inThisGame.forEach(player -> {
                    Box box = new Box();
                    box.whatDoIcarry = 5;
                    box.massage.add(0, App.me);
                    box.massage.add(1, player);
                    box.massage.add(2, playerSelectedNumber);
                    box.massage.add(3, keyRepository);
                    box.pack();
                });//got key now send closed box.
            //======================================================================================================
            try { Thread.sleep(5000); }catch (Exception ex){System.out.println(ex);}//waiting for Box-layer to commute& send their own closed boxes
            inThisGame.forEach(player -> {
                    Box box = new Box();
                    box.whatDoIcarry = 4;
                    box.massage.add(0, App.me);
                    box.massage.add(1, player);
                    box.massage.add(2, keyRepository);
                    box.pack();
                });//sending keys
            //======================================================================================================
            try { Thread.sleep(5000); }catch (Exception ex){System.out.println(ex);}//waiting for Box-layer
            gameInProgress=false;
            sendResult();//result possessing
        }//DONE
            private static int sum;
            public static byte playerSelectedNumber = 0;
            private static Byte[][] keyRepository = {{1,1},{1,1}};
            private static void sendResult(){
                gameInProgress=false;
                inThisGame.add(App.me);
                Collections.sort(inThisGame);
                sum+=playerSelectedNumber;
                sum++;
                while(inThisGame.size()<=sum)
                    sum-=inThisGame.size();
                Result result = new Result();
                result.uniqeID=Box.uniqueID;
                result.winner=inThisGame.get(sum);
                result.loosers.addAll(inThisGame);
                result.loosers.remove(sum);
                App.results.add(result);
                {
                    Box box = new Box();
                    box.whatDoIcarry = 900;
                    box.massage.add(0, null);
                    box.massage.add(1, null);
                    box.massage.add(2, new Consumer<>() {
                        @Override
                        public void accept(Object o) {
                            TextArea textArea = new TextArea(((Result)o).toString());
                            textArea.setPrefSize(512,512);
                            textArea.setWrapText(true);
                            textArea.setFont(App.menuFont);
                            MyErrorPopup myErrorPopup = new MyErrorPopup("result",textArea,false);
                        }
                    });
                    box.massage.add(3, result);
                    App.inBox.add(box);
                }
                if(App.me.equalsNET(inThisGame.get(0))||App.me.equalsNET(inThisGame.get(inThisGame.size()-1)))
                App.players.forEach(player -> {
                    Box box = new Box();
                    box.whatDoIcarry = 6;
                    box.massage.add(0,App.me);
                    box.massage.add(1,player);
                    box.massage.add(2,result);
                    App.outBox.add(box);
                });
        }//DONE
    //SYSTEM
        public static void fetchPlayers(String ip, int port){
            Box box = new Box();
            box.whatDoIcarry = 7;
            box.massage.add(0,App.me);
            box.massage.add(1,Player.getNewPlayer(null,ip,port));
            box.massage.add(2,App.players);
            App.outBox.add(box);
        }//DONE
        public static void disconnect(Player player){
            App.players.forEach(e -> {
                Box box = new Box();
                box.whatDoIcarry=9;
                box.massage.add(0,player);
                box.massage.add(1,e);
                App.outBox.add(box);
            });
        }//DONE
        public static void terminateConnections(List<Player> players){
            ArrayList<Gate> gates = new ArrayList<>();
            players.forEach(player -> App.gates.stream().filter(gate -> gate.player.equalsNET(player)).forEach(gates::add));
            while (gates.size()!=0) {
                Gate gate = gates.remove(0);
                Box box = new Box();
                box.whatDoIcarry=-666;
                box.massage.add(0,App.me);
                box.massage.add(1,gate.player);
                App.outBox.add(box);
            }
        }//DONE
        public static void terminalOperations(){
            App.postman.stop();
            System.out.println(App.results.stream().map(Result::toString).collect(Collectors.joining((System.lineSeparator()+System.lineSeparator()))));
            App.players.forEach(player -> disconnect(player));
            while (App.outBox.size() != 0) App.outBox.remove(0).pack();//wysyla paczki
            terminateConnections(App.players);
            App.results.forEach(result -> System.out.println(result));
            App.postman.stop();
        }//DONE
        private static void generateError(Box box){
            System.out.println("Box "+box+" returned error");
            if(!App.shelfBox.contains(box)) {
                App.shelfBox.add(box);
                App.outBox.add(box);
            }else {
                App.shelfBox.remove(box);
                System.out.println("Box "+box+" returned error again is deleted");
                if(box.whatDoIcarry==1)
                    Chat.chat.feed.appendText("Player "+box.massage.get(1).toString()+" died"+System.lineSeparator());
                disconnect(((Player)box.massage.get(1)));
                inThisGame.removeAll(    inThisGame.stream().filter(in->in.equalsNET((Player)box.massage.get(1))).collect(Collectors.toList()));
                App.toPlayPlayers.removeAll(App.toPlayPlayers.stream().filter(in->in.equalsNET((Player)box.massage.get(1))).collect(Collectors.toList()));
                App.players      .removeAll(App.players      .stream().filter(in->in.equalsNET((Player)box.massage.get(1))).collect(Collectors.toList()));
                App.results      .removeAll(App.results      .stream().filter(result -> result.winner                                    .equalsNET((Player)box.massage.get(1))) .collect(Collectors.toList()));
                App.results      .removeAll(App.results      .stream().filter(result -> result.loosers.stream().anyMatch(player -> player.equalsNET((Player)box.massage.get(1)))).collect(Collectors.toList()));

            }
        }//DONE
    //BOX
        private Box(){}//DONE
        public void pack(){
            System.out.println("Departure: "+this);
            List<Gate> gates = App.gates.stream().filter(gate -> gate.player.equalsNET((Player) massage.get(1))).collect(Collectors.toList());
            if(gates.size()>1){
                TextArea textArea = new TextArea("WAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAT?"+System.lineSeparator()+
                "Holy Moly you BROKE the network layer. bwhut ;-;."+System.lineSeparator()+
                "System uncertainty: more than one gate detected. You would  need to reboot game enyway so..."+System.lineSeparator()+
                "instead you got crash cus... less work for me HF. here goes gates that caused mess."+System.lineSeparator()+
                gates.stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator())));
                textArea.setPrefSize(512, 512);
                new MyErrorPopup("WAAAAAAAAT??????????", textArea, true);
            } else if(gates.size()==1){
                try {
                    gates.get(0).objectOutputStream.writeObject(this);
                    if(whatDoIcarry==-666) {
                        App.gates.remove(gates.get(0));
                        gates.get(0).dispose();
                    }
                }catch (Exception ex){
                    TextArea textArea = new TextArea(ex.toString());
                    textArea.setPrefSize(512, 512);
                    new MyErrorPopup("New Error", textArea, false);
                    generateError(this);
                }
            }else if(whatDoIcarry!=-666){
                Gate gate = Gate.getNewGate((Player) massage.get(1));
                if (gate != null) {
                    App.gates.add(gate);
                    try {
                        gate.objectOutputStream.writeObject(this);
                    } catch (Exception ex) {
                        TextArea textArea = new TextArea(ex.toString());
                        textArea.setPrefSize(512, 512);
                        new MyErrorPopup("New Error", textArea, false);
                        generateError(this);
                    }
                }else {
                    generateError(this);
                }
            }
        }///DONE
        public void unpack(){
            System.out.println("Arrival: "+this);
            if(200<=whatDoIcarry&&whatDoIcarry<=400)
                frel();
            else switch (whatDoIcarry){
                case  1:f001();break;
                case 11:f011();break;
                case  2:f002();break;
                case  3:f003();break;
                case 13:f013();break;
                case 23:f023();break;
                case  4:f004();break;
                case  5:f005();break;
                case  6:f006();break;
                case  7:f007();break;
                case  8:f008();break;
                case  9:f009();break;
               case 900:f900();break;
                default:System.out.println("I ("+this+") don't know what to do, i'll go ;-;");break;
            }
        }//DONE
        public Player getSender(){return (Player)massage.get(0);}

        public int getWhatDoIcarry() {
        return whatDoIcarry;
    }//DONE
        @Override
        public String toString() {
            //if(whatDoIcarry==5)
            //    return "Status "+whatDoIcarry+System.lineSeparator()+massage.get(0)+" "+massage.get(1)+" "+(((int)massage.get(2))*(Math.random()*100))+massage.get(3).getClass();
            return "Status "+whatDoIcarry+System.lineSeparator()+massage+System.lineSeparator();
        }//DONE
        private boolean sender(Player player){
            return player.equalsNET((Player)massage.get(0));
        }//DONE
        private byte open(Byte[][] ints){
            if(gameInProgress&&keyCmp(ints,(Byte[][])massage.get(3))) {
                System.out.println("good password "+massage.get(0)+" byte: "+massage.get(3));
                return (byte) massage.get(2);
            }
            System.out.println("bad password!");
            return 0;
        }//DONE
        private boolean keyCmp(Byte[][] k0, Byte[][] k1){
            if(k0.length!=k1.length)
                return false;
            for(int x = 0; x<k0.length; x++){
                if(k0[x].length!=k1[x].length)
                    return false;
                for(int y = 0; y<k0[x].length; y++)
                    if(k0[x][y]!=k1[x][y])
                        return false;
            }
            return true;
        }//DONE
        private static void geerateKEY(){
            int tmp = 64;//tmp const to sav pressing pwr
            keyRepository = new Byte[tmp][tmp];
            for(int x = 0; x<keyRepository.length; x++)
                for (int y = 0; y<keyRepository.length; y++)
                    keyRepository[x][y]=(byte)(Math.random()*255);
        }//DONE
    //LOCAL VAR
        public static boolean gameInProgress = false;
        private static final ArrayList<Box> lockedBoxes = new ArrayList<>();
        public static final ArrayList<Player> inThisGame = new ArrayList<>();
        public static long uniqueID;
    //EXEC
        private void f001(){
            Chat.chat.feed.appendText((String) massage.get(2));
        }//DONE
        private void f011(){

        }//todo
        private void f002(){
                if((int)massage.get(2)==1)
                    ((Player)massage.get(0)).inTheGame=true;
                if((int)massage.get(2)==2)
                    ((Player)massage.get(0)).inTheGame=false;
                App.players.removeAll(App.players.stream().filter(player -> player.equalsNET(((Player) massage.get(0)))).collect(Collectors.toList()));
                App.players.add((Player) massage.get(0));
            switch ((int) massage.get(2)) {
                case 0:
                    if (((Player) massage.get(0)).inTheGame) {
                        List<Player> list;
                        list = App.toPlayPlayers.stream().filter(player -> player.equalsNET(((Player) massage.get(0)))).collect(Collectors.toList());
                        if (list.size() > 0) {
                            App.toPlayPlayers.remove(list.get(0));
                            App.toPlayPlayers.add((Player) massage.get(0));
                        }
                        list = inThisGame.stream().filter(player -> player.equalsNET(((Player) massage.get(0)))).collect(Collectors.toList());
                        if (list.size() > 0) {
                            inThisGame.remove(list.get(0));
                            inThisGame.add((Player) massage.get(0));
                        }
                    }
                    break;
                case 1:
                    if (App.me.inTheGame)
                        App.toPlayPlayers.add((Player) massage.get(0));
                    break;
                default:
                    if (App.me.inTheGame) {
                        inThisGame.removeAll(inThisGame.stream().filter(in -> in.equalsNET((Player) massage.get(0))).collect(Collectors.toList()));
                        App.toPlayPlayers.removeAll(App.toPlayPlayers.stream().filter(in -> in.equalsNET((Player) massage.get(0))).collect(Collectors.toList()));
                        App.results.removeAll(App.results.stream().filter(result -> result.winner.equalsNET((Player) massage.get(0))).collect(Collectors.toList()));
                        App.results.removeAll(App.results.stream().filter(result -> result.loosers.stream().anyMatch(player -> player.equalsNET((Player) massage.get(0)))).collect(Collectors.toList()));
                    }
                    break;
            }
        }//DONE
        private void f003(){
            if(!gameInProgress) {
                Button confirm = new Button("You have been invited to game by " + massage.get(0) + System.lineSeparator() + "join? after 5 sec invitation will be turned down");
                confirm.setWrapText(true);
                confirm.setPrefSize(512, 512);
                confirm.setFont(App.menuFont);
                Player player = (Player) massage.get(0);
                uniqueID = (long) massage.get(2);
                MyErrorPopup myErrorPopup = new MyErrorPopup("invitation", confirm, false);
                confirm.setOnAction(event -> {
                    if (!gameInProgress) {
                        Box box = new Box();
                        box.whatDoIcarry = 13;
                        box.massage.add(0, App.me);
                        box.massage.add(1, player);
                        App.outBox.add(box);
                        myErrorPopup.close();
                        Box.gameInProgress = true;
                        Box.sum = 0;
                        Box.lockedBoxes.removeAll(Box.lockedBoxes);
                        Box.inThisGame.removeAll(Box.inThisGame);
                    }
                });
            }
        }//DONE
        private void f013(){
                List<Player> tmp = playersToInv.stream().filter(player -> player.equalsNET((Player) massage.get(0))).collect(Collectors.toList());
                if(flagAcceptInvites&&tmp.size()==1){
                    playersToInv.remove(tmp.get(0));
                    inThisGame.add((Player) massage.get(0));
                }
        }//DONE
        private void f023(){
            ((ArrayList<Player>)massage.get(2)).remove(((ArrayList<Player>)massage.get(2)).stream().filter(player -> player.equalsNET(App.me)).findFirst().get());
            ((ArrayList<Player>)massage.get(2)).add((Player)massage.get(0));
            inThisGame.addAll(((ArrayList<Player>)massage.get(2)));
            System.out.println("game on with"+System.lineSeparator()+inThisGame);
            App.toPlayPlayers.removeAll(inThisGame.stream().filter(inthisgameplayer->App.toPlayPlayers.stream().anyMatch(toplaypalyersplayer->inthisgameplayer.equalsNET(toplaypalyersplayer))).collect(Collectors.toList()));
            Thread thread = new Thread(()-> {
                //======================================================================================================//others are offset in time by 1-3 sec but go the following code:
                sum = 0;
                playerSelectedNumber = 0;
                {
                    Box box = new Box();
                    box.whatDoIcarry = 900;
                    box.massage.add(0, null);
                    box.massage.add(1, null);
                    box.massage.add(2, new Consumer<>() {
                        @Override
                        public void accept(Object o) {
                            App.app.root.getChildren().removeAll(App.app.root.getChildren());
                            App.app.play2();
                        }
                    });
                    box.massage.add(3, null);
                    App.inBox.add(box);
                }
                //reset val& ask for new number
                try { Thread.sleep(10000); } catch (Exception ex) { System.out.println(ex); }//waiting for player
                geerateKEY();
                {
                    Box box = new Box();
                    box.whatDoIcarry = 900;
                    box.massage.add(0, null);
                    box.massage.add(1, null);
                    box.massage.add(2, new Consumer<>() {
                        @Override
                        public void accept(Object o) {
                            App.app.root.getChildren().removeAll(App.app.root.getChildren());
                            App.app.menu();
                        }
                    });
                    box.massage.add(3, null);
                    App.inBox.add(box);
                }
                inThisGame.forEach(player -> {
                    Box box = new Box();
                    box.whatDoIcarry = 5;
                    box.massage.add(0, App.me);
                    box.massage.add(1, player);
                    box.massage.add(2, playerSelectedNumber);
                    box.massage.add(3, keyRepository);
                    App.outBox.add(box);
                });//got key now send closed box.
                //======================================================================================================
                try { Thread.sleep(5000); } catch (Exception ex) { System.out.println(ex); }//waiting for Box-layer to commute& send their own closed boxes
                inThisGame.forEach(player -> {
                    Box box = new Box();
                    box.whatDoIcarry = 4;
                    box.massage.add(0, App.me);
                    box.massage.add(1, player);
                    box.massage.add(2, keyRepository);
                    App.outBox.add(box);
                });//sending keys
                //======================================================================================================
                try { Thread.sleep(5000); } catch (Exception ex) { System.out.println(ex); }//waiting for Box-layer
                sendResult();//result possessing
            });
            thread.setDaemon(true);
            thread.start();
        }//DONE
        private void f004(){
                Thread thread = new Thread(()->{
                    Player me = (Player)massage.get(0);
                    Box disbox = lockedBoxes.stream().filter(box->box.sender(me)).findFirst().orElse(null);
                    if(disbox!=null) {
                        lockedBoxes.remove(disbox);
                        sum += disbox.open((Byte[][]) massage.get(2));
                    }
                });
                thread.setDaemon(true);
                thread.start();
        }//DONE
        private void f005(){ lockedBoxes.add(this); }//DONE
        private void f006(){
            Result newResult = (Result) massage.get(2);
            List<Result> results = App.results.stream().filter(result1 -> result1.uniqeID==newResult.uniqeID).collect(Collectors.toList());
            if(results.size()==0)
                results.add(newResult);
            else if(results.size()>1){
                TextArea textArea = new TextArea("Error! => multiple unique ID detected");
                textArea.setPrefSize(512,512);
                textArea.setWrapText(true);
                textArea.setFont(App.menuFont);
                MyErrorPopup myErrorPopup = new MyErrorPopup("result",textArea,false);
            }else {
                Result oldResult = results.get(0);
                if(!(oldResult.winner.equalsNET(newResult.winner))){
                    results.remove(oldResult);
                    TextArea textArea = new TextArea("Error! => cheater detected");
                    textArea.setPrefSize(512,512);
                    textArea.setWrapText(true);
                    textArea.setFont(App.menuFont);
                    MyErrorPopup myErrorPopup = new MyErrorPopup("result",textArea,false);
                }
            }
        }//DONE
        private void f007(){
                ((ArrayList<Player>) massage.get(2)).add((Player) massage.get(0));// dodanie mnie do wszytkich o kim wiem
                ArrayList<Player> tmp1 = new ArrayList<>((ArrayList<Player>) massage.get(2));//lista wszytkich o ktorych wiem ze mna wlacznie

                List<Player> list1 =        tmp1.stream().filter(nP->App.players.stream().noneMatch(sP->sP.equalsNET(nP))).collect(Collectors.toList());//lista graczy o ktorych wie nadawca ale nie host
                List<Player> list2 = App.players.stream().filter(sP->       tmp1.stream().noneMatch(nP->nP.equalsNET(sP))).collect(Collectors.toList());//lista graczy o ktorych wie host ale nie wie nadawca

                App.players.addAll(list1);//juz wie
                App.toPlayPlayers.addAll(list1.stream().filter(player -> player.inTheGame).collect(Collectors.toList()));

                list2.forEach(player -> {
                    Box box = new Box();
                    box.whatDoIcarry=8;
                    box.massage.add(0,App.me);
                    box.massage.add(1,player);
                    box.massage.add(2,list1);
                    App.outBox.add(box);
                });
                list2.add(App.me);
                list1.forEach(player -> {
                    Box box = new Box();
                    box.whatDoIcarry=8;
                    box.massage.add(0,App.me);
                    box.massage.add(1,player);
                    box.massage.add(2,list2);
                    App.outBox.add(box);
                });

        }//DONE
        private void f008(){
                App.players.addAll((List<Player>)massage.get(2));
                App.toPlayPlayers.addAll(((List<Player>)massage.get(2)).stream().filter(player -> player.inTheGame).collect(Collectors.toList()));
                System.out.println(App.players);
        }//DONE
        private void f009(){
                    inThisGame.removeAll(    inThisGame.stream().filter(in->in.equalsNET((Player)massage.get(0))).collect(Collectors.toList()));
                App.toPlayPlayers.removeAll(App.toPlayPlayers.stream().filter(in->in.equalsNET((Player)massage.get(0))).collect(Collectors.toList()));
                App.players      .removeAll(App.players      .stream().filter(in->in.equalsNET((Player)massage.get(0))).collect(Collectors.toList()));
                App.results      .removeAll(App.results      .stream().filter(result -> result.winner                                    .equalsNET((Player)massage.get(0))) .collect(Collectors.toList()));
                App.results      .removeAll(App.results      .stream().filter(result -> result.loosers.stream().anyMatch(player -> player.equalsNET((Player)massage.get(0)))).collect(Collectors.toList()));
        }//DONE
        private void f900(){
            ((Consumer)massage.get(2)).accept(massage.get(3));
        }
        private void frel(){}//todo
    //================================================================================================================== CONTENT
    private int whatDoIcarry;
    /*
    -666 = kill gate on spot
    01 = message                                  =done                                                                 => payload: massage
    11 = UDP message
    02 = statusChange nickChange& GAME(JOIN/QUIT) functionality extended                                                => payload: new Status -> Integer stat=0/1/2 N/J/Q
                                                  From now on: 10 send blackBox to all on list; 11 send key to all on list;
    03 = game invite                              =                                                                     => payload: ArrayList<Player> invited, long uniqueId
    13 = invitation confirmation => null          =done
    23 = invitation confirmation => arrayList<PLayer>          =done
    04 = coder  /sendKey                          =done                                                                 => payload: int[][] lock
    05 = matrix /sendBlackBox                     =done                                                                 => payload: int, int[][] 0--nn
    06 = result                                   =                                                                     => payload: Box.Result
    07 = get players                              =done                                                                 => payload: ArrayList<Players>
    08 = got players                              =done                                                                 => payload: ArrayList<Players>
    09 = disconnect                               =done                                                                 => payload: null
    900= command sender = receiver  = consumer = argument
    prefix 2xx                                    =                                                                     => instead of one receiver, ArrayList<Player> receivers -> allow for bot to be relayed
     */
    private final ArrayList massage = new ArrayList();//array list of things
    /*
    first 2 fields are ALWAYS: sender receiver
     */
}

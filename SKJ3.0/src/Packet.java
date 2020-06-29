import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Packet implements Serializable {
    //CFG
  //public  static       void clientCFG(){}//==========================================================================>
    public  static       void serverCFG(){
        try{
            Stream<String> stringStream = Files.lines(Paths.get("ServerCFG"));
            acceptAll = false;
            Object[] arr = stringStream.toArray();
            acceptAll = ((String)arr[0]).endsWith("true");
            for(int x = 1; x<arr.length; x++)
                prossesCFG((String) arr[x]);
            stringStream.close();
        }catch (Exception ex){ex.printStackTrace();System.exit(-1);}
    }//DONE
    private static       void prossesCFG(String s){
            String[] key = s.split(",");
            keyBook[keyAmount]=new int[key.length][2];
            for(int x = 0; x<key.length; x++) {
                keyBook[keyAmount][x][0]=Integer.parseInt(key[x].split(" ")[0]);
                keyBook[keyAmount][x][1]=Integer.parseInt(key[x].split(" ")[1]);
            }
            keyAmount++;
    }//DONE
  //private static       void internCFG(){}//==========================================================================>
  //private static final Thread execMgr = new Thread(()->{});//========================================================>
    @Override
    public               String toString() {
        return System.lineSeparator()+
               " PL:  "+whatDoIcarry+" S: "+sender+" R: "+reciver+System.lineSeparator()+
               " KG?: "+killGateOnExitEnter+" ATEQ?: "+addToExecQuene+" UDP?: "+udp+System.lineSeparator()+
               " val: "+val+" MSG: "+message+System.lineSeparator();
    }//DONE
    public  static       String lAP(){
        return System.lineSeparator()+"authQuene:             "+System.lineSeparator()+authQuene            .values().stream().map(Qune::toString).collect(Collectors.joining(System.lineSeparator()));
    }//DONE
    //UDP
    public  static       void auth(String ip, int[][] keys){
        for (int[] key : keys) {
            Gate.addToSend(new Packet(Packet.AUTH, App.me, new NetPath(ip, key[0]), false, false, true, key[1], null));
        }
    }//DONE
    public  static       void connect(NetPath reciver,int val){
        Gate.addToSend(new Packet(CON,App.me,reciver,false,false,true,val,""));
    }
    //CHAT functions
    public  static       void sendMessage(String massage) {
        Gate.sendAll(new Packet(SNDMSG,App.me,null,false,false,false,0,massage));
    }//DONE
    public  static       void sendMessage(String massage,String ip, int port) {
        Gate.addToSend(new Packet(SNDMSG,App.me,new NetPath(ip,port),false,false,true,0,massage));
    }//DONE
    public  static       void patchThrou(Packet packet){
        Thread thread = new Thread((()->Gate.sendAll(packet)));
        thread.setDaemon(true);
        thread.start();
    }//DONE
    public  static       void sndCurrFedS(NetPath reciver){
        Gate.addToSend(new Packet(SNDFED,App.me,reciver,false,false,false,0,Chat.chat.feed.getText()));
    }//DONE
    //SYSTEM
  //public  static       void disconnect(NetPath netPath) {Gate.addToSend(new Packet(DIS,App.me,netPath,true,false,false,0,null));}//DONE
    public  static       void terminalOperations() {
        Gate.sendAll(new Packet(DIS,App.me,null,true,false,false,0,null));
        Gate.diposeALL();
    }//DONE
    //BOX
    public               Packet(int whatDoIcarry, NetPath sender, NetPath reciver,boolean killGateOnExitEnter,boolean addToExecQuene, boolean udp, int val, String message) {
        this.whatDoIcarry=whatDoIcarry;
        this.sender=sender;
        this.reciver=reciver;
        this.killGateOnExitEnter=killGateOnExitEnter;
        this.addToExecQuene=addToExecQuene;
        this.udp=udp;
        this.val=val;
        this.message=message;
    }//DONE
    public               void open() {
        System.out.println("attempt to open "+toString());
        if(addToExecQuene)
            toExec.add(this);
        else
            switch (whatDoIcarry){
                case AUTH  :fAUTH()  ;break;
                case SNDMSG:fSNDMSG();break;
                case SNDFED:fSNDFED();break;
                case DIS   :fDIS()   ;break;
                case CON   :fCON()   ;break;
                default    :fUKN()   ;break;
            }
    }//DONE
  //private              void exec(){}//===============================================================================>
    private              void fAUTH(){
        authQuene.putIfAbsent(sender.ip,new Qune());
        Qune qune = authQuene.get(sender.ip);
        qune.packets.add(this);
        qune.timeout=System.currentTimeMillis();
        //if(val==0)
            if(!acceptAll){
            int[][] arr = new int[qune.packets.size()][2];
            for(int x = 0; x<qune.packets.size(); x++){
                arr[x][0]=qune.packets.get(x).reciver.port;
                arr[x][1]=qune.packets.get(x).val;
            }
            for(int x = 0; x<keyBook.length; x++)
                if(Arrays.deepEquals(keyBook[x], arr)){
                    x=keyBook.length;
                    Gate.authSucess(sender);
                }
            }else
                Gate.authSucess(sender);
    }//DONE
    private              void fSNDMSG(){
        if(App.server&&!udp)
            patchThrou(this);
        Chat.chat.feed.appendText(message);
    }//DONE
    private              void fSNDFED(){
        Chat.chat.feed.setText(message);
    }//DONE
    private              void fDIS(){
        Chat.chat.feed.appendText("System: "+sender+" disconnected");
    }//DONE
    private              void fCON() {
        Gate.connectToServer(new NetPath(sender.ip,val));
    }//DONE
    private              void fUKN(){
        Chat.chat.feed.appendText("System: broken box: "+this.toString());
    }//DONE
    //LOCAL VAR
    public  static final int AUTH=0,SNDMSG=1,SNDFED=2,DIS=3,CON=4;
    private static final HashMap<String,Qune> authQuene = new HashMap<>();
    private              class Qune{
        public long timeout = System.currentTimeMillis();
        public ArrayList<Packet> packets = new ArrayList<>();
        @Override
        public String toString() {
            return "timeout: "+timeout+" "+packets.toString();
        }
    }//DONE
    private static       boolean acceptAll;
    private static       int keyAmount = 0;
    private static final int[][][] keyBook = new int [100][][];//up to 100 correct key sequences
    public  static       ArrayList<Integer> getPorts(){
        ArrayList<Integer> integers = new ArrayList<>();
        Arrays.stream(keyBook)
                .filter(ints -> ints!=null)
                .forEach(
                seq-> Arrays.stream(seq).map(vars->vars[0]).forEach(
                        integers::add
                )
        );
        return integers;
    }//DONE
    private static final LinkedList<Packet> toExec = new LinkedList<>();
    //================================================================================================================== CONTENT NEW
    public         final int whatDoIcarry;
    public         final NetPath sender, reciver;
    //============================================================================================================= NAV
    public         final boolean killGateOnExitEnter,addToExecQuene,udp;
    public         final int val;// Key/Port
    public         final String message;
}

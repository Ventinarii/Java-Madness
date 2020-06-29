import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

public class Gate{
//====================================================================================================================== basic comms static part
    private static final Map<NetPath,Gate>        connectionGateHashMap = new HashMap<>();
    private static final HashMap<Integer,UDPGate> udpGate               = new HashMap<>();
    private static final LinkedList<Gate>         toErase               = new LinkedList<>();
    private static final LinkedList<Packet>       toSend                = new LinkedList<>();
    private static final long                     timeOut               = 10000;
    private static       boolean                  work                  = true;
    private static final Thread                   operator              = new Thread(()->{
        try {
            while (work) {
                connectionGateHashMap.forEach(Gate::recivePacket);
                while (toSend.size()!=0)
                    sendPacket(toSend.remove(0));
                while (toErase.size()!=0)
                    toErase.get(0).dispose();
                try {
                    Thread.sleep(100);
                }catch (Exception ex){ex.printStackTrace();}
                //System.out.print("X");
            }
        }catch (Exception ex){ex.printStackTrace();App.exitFun();}
    });//DONE
    //================================================================================================================== all static variables
    private static       void    recivePacket(NetPath key,Gate gate){
        try {
            if (gate.socket.getInputStream().available()>1){
                Packet packet = (Packet) gate.objectInputStream.readObject();
                if(packet.killGateOnExitEnter)
                    toErase.add(gate);
                packet.open();
            }
        }catch (Exception ex){ex.printStackTrace();work=false;App.exitFun();}
    }//DONE
    private static       void    sendPacket  (Packet packet)   throws Exception{
        System.out.println("attempt to send "+packet);
        if(packet.udp) {
            sendUDP(packet);
        }else if(connectionGateHashMap.containsKey(packet.reciver)){
            Gate gate = connectionGateHashMap.get(packet.reciver);
            gate.objectOutputStream.writeObject(packet);
            if (packet.killGateOnExitEnter)
                toErase.add(gate);
        }else System.out.println("Exception: failed to send: "+packet+System.lineSeparator()+"no receiver found");
    }//DONE
    public  static       boolean addToSend   (Packet packet){return toSend.add(packet);}//DONE
    public  static       void    gateCFG     (){
        operator.setDaemon(true);
        operator.start();
    }//DONE
    public  static       String  lAP         (){
        return System.lineSeparator()+
                "App.me:                "+App.me+System.lineSeparator()+
                "connectionGateHashMap: "+System.lineSeparator()+connectionGateHashMap.values().stream().map(Gate::toString).collect(Collectors.joining(System.lineSeparator()))+
                "udpSocket:             "+System.lineSeparator()+udpGate              .values().stream().map(Object::toString).collect(Collectors.joining(System.lineSeparator()));
    }//DONE
    public  static       void    diposeALL   (){
        try {
            Thread.sleep(1000);
            work=false;
            operator.interrupt();
            connectionGateHashMap.forEach((K,V)->V.dispose());
            udpGate.forEach((K,V)->V.dispose());
        }catch (Exception ex){ex.printStackTrace();}
    }//DONE
//====================================================================================================================== basic comms mobile part
    public         final NetPath               netPath;
    private        final Socket                socket;
    private        final ObjectOutputStream    objectOutputStream;
    private        final ObjectInputStream     objectInputStream;
    private                      Gate          (NetPath netPath) throws IOException{
        this.netPath = netPath;
        socket = new Socket(netPath.ip, netPath.port);
        socket.setKeepAlive(true);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
    }//DONE
    private                      Gate          (Socket  socket)  throws   Exception{
        socket.setKeepAlive(true);
        this.socket=socket;
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        Packet packet = (Packet)objectInputStream.readObject();
        netPath = packet.sender;
        packet.open();
    }//DONE
    public               void    dispose       ()                                  {
        try {
            connectionGateHashMap.remove(netPath);
            System.out.println("gate " + this + " is beign disposed");
            objectOutputStream.close();
            objectInputStream.close();
            socket.close();
        }catch (Exception ex){ex.printStackTrace();}
    }//DONE
 @Override
    public               String  toString      (){return super.toString()+" "+ netPath;}//DONE
//====================================================================================================================== adv comms => Server
    public  static       void    connectToServer(NetPath netPath){
        Gate newOne = null;
        try {
            newOne = new Gate(netPath);
        }catch (Exception ex){Chat.chat.feed.appendText(ex.toString());newOne=null;}
        if(newOne!=null) {
            connectionGateHashMap.put(netPath, newOne);
            try {
                newOne.objectOutputStream.writeObject(new Packet(Packet.SNDMSG,App.me,netPath,false,false,false,0,("System: "+App.me+" joined")));
            }catch (Exception ex){Chat.chat.feed.appendText(ex.toString());}
        }
    }//DONE
    public  static       void    allowIntoServer(NetPath netPath, int port){
        Thread thread = new Thread(()->{
            try {
                ServerSocket serverSocket = new ServerSocket(port);
                serverSocket.setSoTimeout(1000);
                Gate newOne = null;
                try {
                    newOne = new Gate(serverSocket.accept());
                }catch (Exception ex){Chat.chat.feed.appendText(ex.toString());newOne=null;}
                if(newOne!=null) {
                    connectionGateHashMap.put(netPath, newOne);
                    Packet.sndCurrFedS(netPath);
                }
                serverSocket.close();
            }catch (Exception ex){Chat.chat.feed.appendText(ex.toString());}
        });
        thread.setDaemon(true);
        thread.start();
    }//DONE
    public  static       void    sendAll        (Packet packet){
        if(packet.reciver==null)
            connectionGateHashMap.values().stream().forEach(gate ->
                    addToSend(new Packet(packet.whatDoIcarry,packet.sender,gate.netPath,packet.killGateOnExitEnter,packet.addToExecQuene,packet.udp,packet.val,packet.message)));
        else
            connectionGateHashMap.values().stream().filter(gate -> !gate.netPath.equals(packet.reciver)).forEach(gate ->
                    addToSend(new Packet(packet.whatDoIcarry,packet.sender,gate.netPath,packet.killGateOnExitEnter,packet.addToExecQuene,packet.udp,packet.val,packet.message)));
    }//DONE
    public  static       void    authSucess     (NetPath netPath){
        int randomPortAddress=(int)(Math.random()*8000+1024);
        Packet.connect (netPath,randomPortAddress);//sends request to connect
        allowIntoServer(netPath,randomPortAddress);//opens server socket
    }//DONE
//====================================================================================================================== adv comms => UDP
    private static       class   UDPGate{
        public UDPGate(int port)throws Exception{
            this.port=port;
            datagramSocket = new DatagramSocket(port);
            thread.setDaemon(true);
            thread.start();
        }//DONE
        private            int                port;
        private            DatagramSocket     datagramSocket;
        private final      Thread             thread = new Thread(()->{
            DatagramPacket datagramPacket;
            byte[] data;
            ByteArrayInputStream byteArrayInputStream;
            ObjectInputStream objectInputStream;
            while (work)
                try {
                    data = new byte[8192];
                    datagramPacket = new DatagramPacket(data,data.length);
                    datagramSocket.receive(datagramPacket);
                    byteArrayInputStream = new ByteArrayInputStream(datagramPacket.getData());
                    objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    Packet packet = (Packet)objectInputStream.readObject();
                    objectInputStream.close();
                    byteArrayInputStream.close();
                    if(packet.reciver.port!=port)
                        Chat.chat.feed.appendText(packet.toString()+" suspicious");
                    packet.open();
                }catch (Exception ex){Chat.chat.feed.appendText(ex.toString());}
        });//DONE
        private       void send   (Packet packet){
            try {
                byte[] data=new byte[1],netAdress={0,0,0,0};
                {
                    String[] strings = packet.sender.ip.split("[.]");
                    netAdress[0]=(byte)Integer.parseInt(strings[0]);
                    netAdress[1]=(byte)Integer.parseInt(strings[1]);
                    netAdress[2]=(byte)Integer.parseInt(strings[2]);
                    netAdress[3]=(byte)Integer.parseInt(strings[3]);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                    objectOutputStream.writeObject(packet);
                    data = byteArrayOutputStream.toByteArray();
                    byteArrayOutputStream.close();
                    objectOutputStream.close();
                }
                datagramSocket.send(new DatagramPacket(data, data.length, InetAddress.getByAddress(netAdress),packet.reciver.port));
            }catch (Exception ex){Chat.chat.feed.appendText(ex.toString());}
        }//DONE
        public        void dispose(){
            try {
                thread.interrupt();
                datagramSocket.close();
            }catch (Exception ex){ex.printStackTrace();}
        }//DONE
    }//DONE
    private static       void    sendUDP  (Packet packet){udpGate.get(App.me.port).send(packet);}//DONE
    public  static       void    clientCFG(){try{udpGate.put(App.me.port,new UDPGate(App.me.port));}catch (Exception ex){ex.printStackTrace();System.exit(-1);}}//DONE
    public  static       void    serverCFG(){
        clientCFG();
            Packet.getPorts().forEach(port -> {
                if (!udpGate.containsKey(port))
                    try {
                    udpGate.put(port, new UDPGate(port));
                }catch (Exception ex){ex.printStackTrace();System.exit(-1);}
            });
    }//DONE
}


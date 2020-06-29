import javafx.application.Platform;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Packet implements Serializable {
    //CFG
        @Override
        public               String toString() {
            return System.lineSeparator()+
                   " PL:  "+whatDoIcarry+" S: "+sender+" R: "+reciver+System.lineSeparator()+
                   " KG?: "+killGateOnExitEnter+" UDP?: "+udp+System.lineSeparator()+
                   " MSG: "+message+System.lineSeparator()+
                   " PQ:  "+payload+System.lineSeparator();
        }//DONE
        public  static       String lAP(){
            return "Packet vars";
        }//DONE
        public  static       void   lew(String ip0,int po0,String ip1,int po1){
            Gate.addToSend(new Packet(CON,App.me,new NetPath(ip0,po0),false,false,null,true ,0,new NetPath(ip1,po1)));
        }//DONE K=R-K
        public  static       void   rig(String ip0,int po0,String ip1,int po1){
            Gate.addToSend(new Packet(CON,App.me,new NetPath(ip0,po0),false,false,null,false,0,new NetPath(ip1,po1)));
        }//DONE K-R=K
    //CHAT functions
        public  static       void   sendMessage(String massage) {
            if(defRelay!=null&&defReciver!=null) {
                if (tcpSideOfLink)
                    Gate.addToSend(new Packet(REL   , App.me, defRelay  , false, false, null,
                                   new Packet(SNDMSG, App.me, defReciver, false, true , massage)));
                else
                    Gate.addToSend(new Packet(REL   , App.me, defRelay  , false, true ,  null,
                                   new Packet(SNDMSG, App.me, defReciver, false, false, massage)));
            }else {
                Chat.chat.feed.appendText("System: connection not configured");
            }
        }//DONE
        public  static       void   sndCurrFedS(NetPath reciver){
            if(defRelay!=null&&defReciver!=null) {
                if(tcpSideOfLink)
                    Gate.addToSend(new Packet(REL   , App.me, defRelay  ,false,false,null,
                                   new Packet(SNDFED, App.me, defReciver,false,true ,Chat.chat.feed.getText())));
                else
                    Gate.addToSend(new Packet(REL   , App.me, defRelay  ,false,true ,null,
                                   new Packet(SNDFED, App.me, defReciver,false,false,Chat.chat.feed.getText())));
            }else {
                Chat.chat.feed.appendText("System: connection not configured");
            }
    }//DONE
    //SYSTEM
        public  static       void   terminalOperations() {
            if(defRelay!=null&&defReciver!=null)
                if (tcpSideOfLink)
                    Gate.addToSend(new Packet(DIS, App.me, defRelay, true, false, null,false,defReciver));
                else
                    Gate.addToSend(new Packet(DIS, App.me, defRelay, true, true , null,false,defReciver));
            Gate.diposeALL();
        }//DONE
    //BOX
        public               Packet(int whatDoIcarry, NetPath sender, NetPath reciver,boolean killGateOnExitEnter, boolean udp, String message, Object... payload) {
            this.whatDoIcarry=whatDoIcarry;
            this.sender=sender;
            this.reciver=reciver;
            this.killGateOnExitEnter=killGateOnExitEnter;
            this.udp=udp;
            this.message=message;
            this.payload.addAll(Arrays.asList(payload));
        }//DONE
        public               void   open   () {
            System.out.println("attempt to open "+toString());
            switch (whatDoIcarry){
                case SNDMSG:fSNDMSG();break;
                case SNDFED:fSNDFED();break;
                case DIS   :fDIS()   ;break;
                case CON   :fCON()   ;break;
                case REL   :fREL()   ;break;
                default    :fUKN()   ;break;
            }
        }//DONE
        private              void   fSNDMSG(){Chat.chat.feed.appendText(message);}//DONE
        private              void   fSNDFED(){Chat.chat.feed.   setText(message);}//DONE
        private              void   fDIS   (){
            boolean lew  =(boolean)payload.get(0);//true{K=R-K}<=>false{K-R=K}
            if(payload.size()==2) {
                NetPath adres = (NetPath) payload.get(1);//tmp
                Gate.addToSend(    new Packet(DIS,sender,adres,true,lew,null,false));
            }else {
                defRelay=null;
                defReciver=null;
                tcpSideOfLink=false;
                Platform.runLater(() -> Chat.chat.setTitle(App.title+" "+App.me+" "+"Unpaired"));
            }
        }//DONE
        private              void   fCON   (){
            boolean lew  =(boolean)payload.get(0);//true{K=R-K}<=>false{K-R=K}
            int     stage=(int)    payload.get(1);//K0<=>R<=>K1
            NetPath adres=(NetPath)payload.get(2);//tmp
            switch (stage) {
                case 0: //R
                    Gate.addToSend(new Packet(CON, sender, adres, false, lew, null, lew, 1, reciver));
                    break;
                case 1: //K1
                    if (defRelay == null && defReciver == null) {
                        defRelay = adres;
                        defReciver = sender;
                        tcpSideOfLink = !lew;
                        Platform.runLater(() -> Chat.chat.setTitle(App.title + " " + App.me + " " + "paired with: Rel<" + defRelay + ">,Rec<" + defReciver + ">"));
                        Gate.addToSend(new Packet(CON, reciver, defRelay, false, lew, null, lew, 2, defReciver));
                    } else {
                        Chat.chat.feed.appendText("System: failed connection attempt by: " + System.lineSeparator() + "System: " + toString());
                    }
                    break;
                case 2: //R
                    Gate.addToSend(new Packet(CON, sender, adres, false, !lew, null, lew, 3, reciver));
                    break;
                default: //L0
                    defRelay = adres;
                    defReciver = sender;
                    tcpSideOfLink = lew;
                    Platform.runLater(() -> Chat.chat.setTitle(App.title + " " + App.me + " " + "paired with: Rel<" + defRelay + ">,Rec<" + defReciver + ">"));
                    break;
            }
        }//DONE
        private              void   fREL   (){payload.forEach(o -> Gate.addToSend((Packet)o));}//DONE
        private              void   fUKN   (){Chat.chat.feed.appendText("System: broken box: "+this.toString());}//DONE
    //LOCAL VAR
        public  static final int SNDMSG=1,SNDFED=2,DIS=3,CON=4,REL=5;
        public  static       NetPath defRelay=null,defReciver=null;
        public  static       boolean tcpSideOfLink=false;
    //================================================================================================================== CONTENT NEW
        public         final int whatDoIcarry;
        public         final NetPath sender, reciver;
    //================================================================================================================== NAV
        public         final boolean killGateOnExitEnter,udp;
        public         final String message;
        public         final ArrayList payload = new ArrayList();
}

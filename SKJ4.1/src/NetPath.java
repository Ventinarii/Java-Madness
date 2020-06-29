import java.io.Serializable;
public class NetPath implements Serializable, Comparable<NetPath>{
    public final String ip;
    public final int port;
    public NetPath(String ip, int port){
        this.ip=ip;
        this.port=port;
    }//DONE
    @Override
    public String toString() {
        return ip+" "+port;
    }//DONE
    @Override
    public boolean equals(Object o) {
        return o.getClass() == NetPath.class && (((NetPath) o).ip.equals(this.ip) && ((NetPath) o).port == this.port);
    }//DONE
    @Override
    public int hashCode() {
        return 7*ip.hashCode()+11*port;
    }
    @Override
    public int compareTo(NetPath o) {
        int x;
        x = this.ip.compareTo(o.ip);
        if(x==0) x = this.port-o.port;
        return x;
    }//DONE
}

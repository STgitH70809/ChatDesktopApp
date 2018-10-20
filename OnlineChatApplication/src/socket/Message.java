package socket;

import java.io.Serializable;

public class Message implements Serializable{
    
    private static final long serialVersionUID = 1L;
    public String type, sender, content, recipient , ipaddress;
    public int port;
    
    public Message(String type, String sender, String content, String recipient , String ipaddress , int port){
        this.type = type; this.sender = sender; this.content = content; this.recipient = recipient;
        this.ipaddress = ipaddress;
        this.port = port;
    }
    
    @Override
    public String toString(){
        return "{type='"+type+"', sender='"+sender+"', content='"+content+"', recipient='"+recipient+"ip = "+ipaddress + "port =" + port+"'}";
    }
}

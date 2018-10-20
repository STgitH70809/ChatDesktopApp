package socket;

import ui.ChatFrame;
import java.io.*;
import java.net.*;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Download implements Runnable{
    
    public ServerSocket server;//server de hong nguoi gui
    public Socket socket;
    public int port;
    public String saveTo = "";//duong dan toi file save...
    public InputStream In;// doc tu mang
    public FileOutputStream Out;//ghi ra file
    public ChatFrame ui;//giao dien
    
    public Download(String saveTo, ChatFrame ui){//cai ham khoi tao chu ko phai ham down load
        try {
            server = new ServerSocket(0);//serversocket(0) thi no thay cong nao con trong thi lay
            port = server.getLocalPort();//gio moi thay co nghia :v
            this.saveTo = saveTo;
            this.ui = ui;
        } 
        catch (IOException ex) {
        	final JPanel panel = new JPanel();
        	JOptionPane.showMessageDialog(panel, "Exception [Download : Download(...)]!", "Download Failed!", JOptionPane.WARNING_MESSAGE);
        }
    }

    @Override
    public void run() {
        try {
            socket = server.accept();
            System.out.println("Download : "+socket.getRemoteSocketAddress());
            
            In = socket.getInputStream();
            Out = new FileOutputStream(saveTo);
            
            byte[] buffer = new byte[1024];
            int count;
            
            while((count = In.read(buffer)) >= 0){//doc toi khi cai buffer nhan duoc con full
                Out.write(buffer, 0, count);//ghi ra file
            }
            
            Out.flush();
            final JPanel panel = new JPanel();
        	JOptionPane.showMessageDialog(panel, "You have already received the file!", "Download Complete!", JOptionPane.INFORMATION_MESSAGE);
        	
            if(Out != null){ Out.close(); }
            if(In != null){ In.close(); }
            if(socket != null){ socket.close(); }
        } 
        catch (Exception ex) {
        	final JPanel panel = new JPanel();
        	JOptionPane.showMessageDialog(panel, "Exception [Download : run(...)]!", "Download Failed!", JOptionPane.WARNING_MESSAGE);
        }
    }
}
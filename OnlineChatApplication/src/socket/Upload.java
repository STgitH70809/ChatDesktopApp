package socket;

import java.io.*;
import java.net.*;
import ui.ChatFrame;

public class Upload implements Runnable{//class up load file

    public String addr;
    public int port;//thong tin noi nhan
    public Socket socket;
    public FileInputStream In;//doc file
    public OutputStream Out;//gui file
    public File file;
    public ChatFrame ui;//giao dien
    
    public Upload(String addr, int port, File filepath, ChatFrame frame){
        super();
        try {
            file = filepath; ui = frame;
            socket = new Socket(InetAddress.getByName(addr), port);
            Out = socket.getOutputStream();
            In = new FileInputStream(filepath);
        } 
        catch (Exception ex) {
            System.out.println("Exception [Upload : Upload(...)]");
        }
    }
    
    @Override
    public void run() {
        try {       
            byte[] buffer = new byte[1024];//moi lan doc 1k roi gui
            int count;
            
            while((count = In.read(buffer)) >= 0){//chung nao con thi doc roi gui
                Out.write(buffer, 0, count);
            }
            Out.flush();
            
            //ui.jTextArea1.append("[Applcation > Me] : File upload complete\n");//thong bao gui file thanh cong ra giao dien
            //ui.jButton5.setEnabled(true); ui.jButton6.setEnabled(true);
            //ui.jTextField5.setVisible(true);//cho su dung lai vung gui file(vung phia duoi giao dien)
            
            if(In != null){ In.close(); }//giai phong vung nho
            if(Out != null){ Out.close(); }
            if(socket != null){ socket.close(); }
        }
        catch (Exception ex) {
            System.out.println("Exception [Upload : run()]");
            ex.printStackTrace();
        }
    }

}
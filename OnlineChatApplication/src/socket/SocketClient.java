package socket;

import ui.LoginFrame;
import ui.ChatFrame;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import ui.HistoryFrame;

public class SocketClient implements Runnable{//class chay client
    
	public int port;
    public String serverAddr;
    public Socket socket;// thong tin soc ket
    public Thread clientThread;//====================>
    public String username, password;//================>
    public Client_Thread clients[];//===================>
    public int clinetcounts = 0 , port_S;//==>
    public ServerSocket server = null;//=============>
    public boolean runForver = true;//========>
    public ArrayList<Integer> ports;
    public ArrayList<String> usernames;
    public ArrayList<String> ipaddresses;
    
    public ChatFrame ui_Chat;
    public LoginFrame ui_Login;
    public HistoryFrame historyFrame; 
    
    public ObjectInputStream In;
    public ObjectOutputStream Out;//cac luong in out
    public History hist;// lich su chat chit
    
    public SocketClient(LoginFrame frame) throws IOException{
    	clients = new Client_Thread[50];
        ui_Login = frame; 
        this.serverAddr = ui_Login.serverAddr; this.port = ui_Login.port;
        socket = new Socket(InetAddress.getByName(serverAddr), port);
            
        Out = new ObjectOutputStream(socket.getOutputStream());
        Out.flush();
        In = new ObjectInputStream(socket.getInputStream());
        
        ports = new ArrayList<Integer>();
        usernames = new ArrayList<String>();
        ipaddresses = new ArrayList<String>();
        Random rand = new Random();
		for(int i = 1; i<10;i++) {//thu 10 lan la gh
			try {
				port_S = 5000 + rand.nextInt(1000);
				server = new ServerSocket(port_S);
				break;
			} catch (IOException e) {
				port_S = 0;
				continue;
			}
		}
		if (port_S == 0) {
			System.out.println("fail to create server YOU BETTER RESTART APP");
			return;
		}
        new miniServerRunning().start();
        
    }

    @Override
    public void run() {//lap vo han de hong tin nhan
        boolean keepRunning = true;
        while(keepRunning){
            try {
            	ui_Login.jButton1.setEnabled(false); 
                ui_Login.jTextField1.setEditable(false); 
                ui_Login.jTextField2.setEditable(false);
                Message msg = (Message) In.readObject();
                System.out.println("Incoming : "+msg.toString());
                
                if(msg.type.equals("message")) {
                	if(msg.recipient.equals("All"))
                 		ui_Chat.chatArea[0].append(">>"+ String.format("%-20s", msg.sender+":") +"\n " + msg.content + "\n");
                }
                else if(msg.type.equals("login")){//ket qua login tu server
                    if(msg.content.equals("TRUE")){//chap nhan
                        ui_Login.jButton2.setEnabled(false); 
                        ui_Login.jButton3.setEnabled(false);
                        username = ui_Login.jTextField3.getText();//==============>
                        password = msg.content;//===============>
                        //ui_Chat.setVisible(false);
                        ui_Chat = new ChatFrame();//=====================>
                        ui_Chat.setVisible(true);//===================>
                        ui_Login.setVisible(false);//===================>
                        ui_Chat.client = this;
                
                        final JPanel panel = new JPanel();
                    	JOptionPane.showMessageDialog(panel, "You have already logged into the application!", "Login Successful!", JOptionPane.INFORMATION_MESSAGE);
                     
                    	//ui_Login.jTextField3.setEnabled(false); 
                        hist = ui_Chat.hist;
                        sendToServer(new Message("port_rep", username, "", "SERVER","nani",port_S));
                    }	
                    else{
                    	final JPanel panel = new JPanel();
                    	JOptionPane.showMessageDialog(panel, "Username or password is incorrect or account has already logined!", "Login Failed!", JOptionPane.WARNING_MESSAGE);
                    }
                }
                else if(msg.type.equals("test")){
                    ui_Login.jButton1.setEnabled(false);
                    ui_Login.jButton2.setEnabled(true); 
                    ui_Login.jButton3.setEnabled(true);
                    ui_Login.jTextField3.setEnabled(true);
                    ui_Login.jTextField3.setEditable(true);
                    ui_Login.jPasswordField1.setEnabled(true);
                    ui_Login.jPasswordField1.setEditable(true);
                }
                else if(msg.type.equals("newuser")){//neu co nguoi moi dang nhap
                    if(!msg.content.equals(username)){//neu khong phai minh thi moi add vo
                        boolean exists = false;//kiem tra xem da co hay chua
                        for(int i = 0; i < ui_Chat.model.getSize(); i++){
                            if(ui_Chat.model.getElementAt(i).equals(msg.content)){
                                exists = true; break;
                            }
                        }
                        if(!exists){ 
                        	ui_Chat.model.addElement(msg.content);
                        	int index = 0;
                         	for (int i = 1; i < ui_Chat.model.getSize(); i++) {
                         		String find = ui_Chat.model.get(i).toString();
                         		if(msg.content.equals(find)) {
                         			index = i;
                         			break;
                         		}
                         	}
                         	ui_Chat.chatArea[index].setText(hist.VoltalicChain(username, msg.content));
                        	usernames.add(msg.content);
                        	ports.add(msg.port);
                        	ipaddresses.add(msg.ipaddress);
                        }
                    }
                }
                else if(msg.type.equals("signup")){//ket qua dang ki tu server
                    if(msg.content.equals("TRUE")){//thanh cong
                    	//ui_Login.jButton2.setEnabled(false); 
                        //ui_Login.jButton3.setEnabled(false);
                        //username = ui_Login.jTextField3.getText();//==============>
                        //password = msg.content;//===============>
                        //ui_Chat = new ChatFrame();//=====================>
                        //ui_Chat.setVisible(true);//===================>
                        //ui_Login.setVisible(false);//===================>
                        //ui_Chat.client = this;
                        
                        
                        final JPanel panel = new JPanel();
                    	JOptionPane.showMessageDialog(panel, "You have already logged into the application!", "Signup Successful!", JOptionPane.INFORMATION_MESSAGE);
                    	
                    	//ui_Login.jButton2.setEnabled(false); ui_Login.jButton3.setEnabled(false);
                    }
                    else{
                    	final JPanel panel = new JPanel();
                    	JOptionPane.showMessageDialog(panel, "Username already exist!", "Signup Failed!", JOptionPane.WARNING_MESSAGE);
                    }
                }
                else if(msg.type.equals("signout")){
                    if(msg.content.equals(username)){
                    	//TODO: Hien thuc lai logout
                        ui_Chat.chatArea[0].append("["+ msg.sender +"] has already logged out!\n");
                        ui_Login.jButton1.setEnabled(true); 
                        //ui_Chat.jButton4.setEnabled(false); 
                        ui_Login.jTextField1.setEditable(true); 
                        ui_Login.jTextField2.setEditable(true);
                        
                        for(int i = 1; i < ui_Chat.model.size(); i++){
                            ui_Chat.model.removeElementAt(i);
                            ports.remove(i);
                            usernames.remove(i);
                            ipaddresses.remove(i);
                        }
                        
                        clientThread.stop();
                    }
                    else{
                        ui_Chat.model.removeElement(msg.content);
                        for(int i = 0 ; i < ports.size(); i++) {
                        	if(msg.content.equalsIgnoreCase(usernames.get(i))) {
                        		 ports.remove(i);
                                 usernames.remove(i);
                                 ipaddresses.remove(i);
                                 break;
                        	}
                        }
                        //ui_Chat.jTextArea1.append("["+ msg.content +"] has already logged out!\n");
                    }
                }
                else{
                	final JPanel panel = new JPanel();
                	JOptionPane.showMessageDialog(panel, "Unknown message type!", "Warning!", JOptionPane.WARNING_MESSAGE);
                }
            }
            catch(Exception ex) {
                keepRunning = false;
                final JPanel panel = new JPanel();
            	JOptionPane.showMessageDialog(panel, "Connection Failure!", "Warning!", JOptionPane.WARNING_MESSAGE);
                ui_Login.jButton1.setEnabled(true); 
                ui_Login.jTextField1.setEditable(true); 
                ui_Login.jTextField2.setEditable(true);
                //ui_Chat.jButton4.setEnabled(false); ui_Chat.jButton5.setEnabled(false); ui_Chat.jButton5.setEnabled(false);
                
               // for(int i = 1; i < ui_Chat.model.size(); i++){
                //    ui_Chat.model.removeElementAt(i);
               // }
                
               // clientThread.stop();
                JOptionPane.showMessageDialog(panel, "Exception SocketClient run()!", "Warning!", JOptionPane.WARNING_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
    public synchronized void handle(int ID, Message msg) {
    	 if(msg.type.equals("message")) {//tin nhan chat
             if(msg.recipient.equals(username)){//Neu goi ca nhan thi in ra nay neu recipient la all thi chay cai duoi
             	int index = 0;
             	for (int i = 1; i < ui_Chat.model.getSize(); i++) {
             		String find = ui_Chat.model.get(i).toString();
             		if(msg.sender.equals(find)) {
             			index = i;
             			break;
             		}
             	}
             	if(index != 0)
             		ui_Chat.chatArea[index].append(">>"+ String.format("%-20s", msg.sender+":") +"\n " + msg.content + "\n");
             }
             else {
             	
             	
             		int index = 0;
                 	for (int i = 1; i < ui_Chat.model.getSize(); i++) {
                 		String find = ui_Chat.model.get(i).toString();
                 		if(msg.recipient.equals(find)) {
                 			index = i;
                 			break;
                 		}
                 	}
                 	if(index != 0)
                 		ui_Chat.chatArea[index].append(">>"+String.format("%-20s", msg.sender+":") +"\n " + msg.content + "\n");
             	
             }
                                     
             if(!msg.content.equals(".bye") && !msg.sender.equals(username)){//neu khong phai la tin dang xuat
                 String msgTime = (new Date()).toString();						// thi ghi ra history
                 
                 try{
                     hist.addMessage(msg, msgTime);
                     //TODO: Hien thuc logout dang nhap lai van con thay tin nhan cu
                     //DefaultTableModel table = (DefaultTableModel) ui_Chat.historyFrame.jTable1.getModel();
                     //table.addRow(new Object[]{msg.sender, msg.content, "Me", msgTime});
                 }
                 catch(Exception ex){}  
             }
         }else if(msg.type.equals("iwanttotellyouwhatmynameis")) {
         	clients[findClient(ID)].usernami = msg.sender;
         	//System.out.println("IWTTYMN: "+ username +" " +msg.sender);
         }else if(msg.type.equals("upload_req")){// yeu cau gui_Chat file
             //hoi thoai cho lua chon xuat hien
             if(JOptionPane.showConfirmDialog(ui_Chat, ("Accept '"+msg.content+"' from "+msg.sender+" ?")) == 0){
                 
                 JFileChooser jf = new JFileChooser();//neu accept thi mo hop thoai cho noi de luu file
                 jf.setSelectedFile(new File(msg.content));
                 int returnVal = jf.showSaveDialog(ui_Chat);
                
                 String saveTo = jf.getSelectedFile().getPath();
                 if(saveTo != null && returnVal == JFileChooser.APPROVE_OPTION){
                     Download dwn = new Download(saveTo, ui_Chat);
                     Thread t = new Thread(dwn);//chay thread download
                     t.start();
                     //send(new Message("upload_res", (""+InetAddress.getLocalHost().getHostAddress()), (""+dwn.port), msg.sender));
                     try {
						sendToClient(new Message("upload_res", ""+InetAddress.getLocalHost().getHostAddress(), (""+dwn.port), msg.sender,"nani",-1));
					} catch (UnknownHostException e) {
						e.printStackTrace();
					}
                 }
                 else{
                     sendToClient(new Message("upload_res", username, "NO", msg.sender,"nani",-1));
                 }
             }
             else{
                 sendToClient(new Message("upload_res", username, "NO", msg.sender,"nani",-1));
             }
         }
         else if(msg.type.equals("upload_res")){//giu y/c gui_Chat file
             if(!msg.content.equals("NO")){//duoc chap thuan
                 int port  = Integer.parseInt(msg.content);
                 String addr = msg.sender;//lay dia chi nguoi giu
             	final JPanel panel = new JPanel();
             	JOptionPane.showMessageDialog(panel, "["+msg.sender+"] accepted file request!", "Transfer Successful!", JOptionPane.INFORMATION_MESSAGE);
                 //ui_Chat.jButton5.setEnabled(false); ui_Chat.jButton6.setEnabled(false);
                 Upload upl = new Upload(addr, port, ui_Chat.file, ui_Chat);
                 Thread t = new Thread(upl);
                 t.start();//chay thread upload
             }
             else{
             	final JPanel panel = new JPanel();
             	JOptionPane.showMessageDialog(panel, "["+msg.sender+"] rejected file request!", "Transfer Failed!", JOptionPane.INFORMATION_MESSAGE);
                 //ui_Chat.jTextArea1.append("["+msg.sender+"] rejected file request\n");
             }
         }else{
         	final JPanel panel = new JPanel();
         	JOptionPane.showMessageDialog(panel, "Unknown message type!", "Warning!", JOptionPane.WARNING_MESSAGE);
         }
    }
    public void sendToServer(Message msg){//ham viet tin nhan
        try {
            Out.writeObject(msg);
            Out.flush();
            System.out.println("Outgoing : "+msg.toString());
            
            if(msg.type.equals("message") && !msg.content.equals(".bye")){
                String msgTime = (new Date()).toString();
                try{//viet tin nao thi luu lai
                    hist.addMessage(msg, msgTime);               
                    //DefaultTableModel table = (DefaultTableModel) ui_Chat.historyFrame.jTable1.getModel();
                    //table.addRow(new Object[]{"Me", msg.content, msg.recipient, msgTime});
                }
                catch(Exception ex){}
            }
        } 
        catch (IOException ex) {
        	final JPanel panel = new JPanel();
        	JOptionPane.showMessageDialog(panel, "Exception SocketClient send()!", "Warning!", JOptionPane.WARNING_MESSAGE);
        }
    }
    public void sendToClient(Message msg) {
    	if (msg.recipient.equals("All")) {
    		if(msg.type.equals("message")) {
    			sendToServer(msg);
    		}else {
    			final JPanel panel = new JPanel();
             	JOptionPane.showMessageDialog(panel, "No send file to ALL", "Warning!", JOptionPane.WARNING_MESSAGE);
    		}
    		return;
    	}
    	Client_Thread t = null;
        System.out.println("Outgoing : "+msg.toString());
		for(int i = 0 ; i < clinetcounts ; i++) {
			if (msg.recipient.equals(clients[i].usernami)){
				t = clients[i];
				break;
			};
		}
		
		if (t == null) {
			System.out.println("t == null = true");
			t = makenewconnection(msg);
		}
		
		if(t == null) {
			System.out.println("NO HOPE NOW, BETTER RESTART YOUR APP :V");
			return;
		}
		t.sendcl(msg);
		
		int index = 0;
     	for (int i = 1; i < ui_Chat.model.getSize(); i++) {
     		String find = ui_Chat.model.get(i).toString();
     		if(msg.recipient.equals(find)) {
     			index = i;
     			break;
     		}
     	}
     	if(index != 0)
     		ui_Chat.chatArea[index].append(">>"+ String.format("%-20s", msg.sender+":") +"\n " + msg.content + "\n");
		
		if(msg.type.equals("message") && !msg.content.equals(".bye")){
            String msgTime = (new Date()).toString();
            try{//viet tin nao thi luu lai
                hist.addMessage(msg, msgTime);               
                //DefaultTableModel table = (DefaultTableModel) ui_Chat.historyFrame.jTable1.getModel();
                //table.addRow(new Object[]{"Me", msg.content, msg.recipient, msgTime});
            }
            catch(Exception ex){}
        }
    }
    
    private Client_Thread makenewconnection(Message msg) {
    	Client_Thread cl = null;
    	for(int i = 0; i<ports.size();i++) {
			if(usernames.get(i).equalsIgnoreCase(msg.recipient)) {
				try {
					
					Socket soc = new Socket(ipaddresses.get(i),ports.get(i));
					
					cl = new Client_Thread(soc);
					System.out.println("I AM FROM MKNCNT"+ipaddresses.get(i) + ports.get(i) + " " + cl.ID);
					clients[clinetcounts] = cl;
					clinetcounts++;
					cl.start();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return cl;
	}

	public void closeThread(Thread t){
        t = null;
    }
    
    private int findClient(int ID){  //tim client thei ID de xu li
    	for (int i = 0; i < clinetcounts; i++){//thi duyet 1 luot toi khi nao thay thi tra ve
        	if (clients[i].ID == ID){
                    return i;
                }
    	}
    	return -1;//ko tim thay tra ve -1
    }
    
    @SuppressWarnings("deprecation")
	public synchronized void remove(int ID){  //xoa client co ID==ID
        int pos = findClient(ID);//tim ban can xoa
            if (pos >= 0){  //niu khong phai -1 (tim duoc)
                Client_Thread toTerminate = clients[pos];//luu lai
                
    	    if (pos < clinetcounts-1){//xoa di
                    for (int i = pos+1; i < clinetcounts; i++){
                        clients[i-1] = clients[i];
    	        }
    	    }
    	    clinetcounts--;//giam so luong client xuong
    	    try{  
    	      	toTerminate.close(); //roi dong no lai ko nang may
    	    }
    	    catch(IOException ioe){  
    	      	//khong biet phai lm j het 
    	    }
    	    toTerminate.stop(); 
    	}
   }
    
    class Client_Thread extends Thread{
    	public Socket socket = null;//cai socket luu client ket noi toi
        public int ID = -1;//moi cai co ID rieng de tien xu li(them xoa chinh sua do)
        public String usernami = "";//ten nguoi dung
        public ObjectInputStream streamIn  =  null;//cai de doc Message
        public ObjectOutputStream streamOut = null;//cai de gui Message

        public Client_Thread( Socket _socket){//chi la cai ham khoi tao gian nay no  
        	super();
        	
            socket = _socket;
            ID     = socket.getPort();
            
            try {
				streamOut = new ObjectOutputStream(socket.getOutputStream());
				streamOut.flush();
	            streamIn = new ObjectInputStream(socket.getInputStream());
	            
	            this.sendcl(new Message("iwanttotellyouwhatmynameis", username, "", "","nani",-1));
	           
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}//khoi tao doc viet
            
        }
        
        public void sendcl(Message msg){//dung de gui tin nhan cho client nay
            try {
            	System.out.println("Outgoing : "+msg.toString());
                streamOut.writeObject(msg);//xai cai nay ko cung gui duoc nhung co ham no ngan hon :v
                streamOut.flush();
            } 
            catch (IOException ex) {
    //IP Address
                System.out.println("Exception [SocketClient : send(...)]");
            }
        }
        
        @SuppressWarnings("deprecation")
    	public void run(){  //cai nay se chay khi bat dau thread
        	//ui_Chat.jTextArea1.append("\nServer Thread " + ID + " running.");//ghi ra man hinh de biet no dang chay
            while (true){  
            	Message msg;
            	try{  
        	    	
                    msg = (Message) streamIn.readObject();//dang hong yeu cau tu client
                    System.out.println("Incoming : "+msg.toString() + " " +ID);
                   // for(int i = 0 ; i < clinetcounts ; i++) {
                   // 	System.out.println(clients[i].ID);
                   // }
                    
                }
                catch(Exception ioe){  //truc trac
                	System.out.println(ID + " ERROR reading: " + ioe.getMessage());
                    remove(ID);
                    stop();
                    return;
                }
        	    handle(ID, msg);//cai ham su li yeu cau ben server
            }
        }
        
        
        public void close() throws IOException {  //cai ham xu li khi dong thread ko
        	if (socket != null)    socket.close();//ko close bon nay no chay miet ton bo nho
            if (streamIn != null)  streamIn.close();
            if (streamOut != null) streamOut.close();
        }
    }
    
    class miniServerRunning extends Thread {
		public void run() {
			
			while(runForver) {
				
				Socket soc;
				try {
					soc = server.accept();
					
					Client_Thread t = new Client_Thread(soc); 
					
					if (!runForver) break;
					
					clients[clinetcounts] = t;
					clinetcounts +=1;
					t.start();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
    
}

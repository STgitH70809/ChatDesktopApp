package socket;

import java.io.*;
import java.net.*;

class ServerThread extends Thread { //cai thread de xu li client , moi thread xu 1 thang client
	
    public SocketServer server = null;//thi no tham khao toi thang socketServer nao
    public Socket socket = null;//cai socket luu client
    public int ID = -1;//moi cai co ID rieng de tien xu li(them xoa chinh sua do)
    public String username = "";//ten nguoi dung
    public ObjectInputStream streamIn  =  null;//cai de doc Message
    public ObjectOutputStream streamOut = null;//cai de gui Message
    public ServerFrame ui;//giao dien do hoa do
    public int thePORT;

    public ServerThread(SocketServer _server, Socket _socket){//chi la cai ham khoi tao gian nay no  
    	super();
        server = _server;
        socket = _socket;
        ID     = socket.getPort();
        ui = _server.ui;
    }
    
    public void send(Message msg){//dung de gui tin nhan cho client nay
        try {
            streamOut.writeObject(msg);//xai cai nay ko cung gui duoc nhung co ham no ngan hon :v
            streamOut.flush();
        } 
        catch (IOException ex) {
//IP Address
            System.out.println("Exception [SocketClient : send(...)]");
        }
    }
    
    public int getID(){  //t ko hieu sao ID la public ma cai ham nay lai ton tai :v
	    return ID;
    }

    @SuppressWarnings("deprecation")
	public void run(){  //cai nay se chay khi bat dau thread
    	ui.jTextArea1.append("\nServer Thread " + ID + " running.");//ghi ra man hinh de biet no dang chay
        while (true){  
    	    try{  
                Message msg = (Message) streamIn.readObject();//dang hong yeu cau tu client
    	    	server.handle(ID, msg);//cai ham su li yeu cau ben server
            }
            catch(Exception ioe){  //truc trac
            	System.out.println(ID + " ERROR reading: " + ioe.getMessage());
                server.remove(ID);
                stop();
            }
        }
    }
    
    public void open() throws IOException {  //cai ham chay truoc cai ham run o tren (chac vay)
        streamOut = new ObjectOutputStream(socket.getOutputStream());//khoi tao doc viet
        streamOut.flush();
        streamIn = new ObjectInputStream(socket.getInputStream());
    }
    
    public void close() throws IOException {  //cai ham xu li khi dong thread ko
    	if (socket != null)    socket.close();//ko close bon nay no chay miet ton bo nho
        if (streamIn != null)  streamIn.close();
        if (streamOut != null) streamOut.close();
    }
}





public class SocketServer implements Runnable {//cai nay de quan li 1 list cac serverThread o tren
    
    public ServerThread clients[];//cac client
    public ServerSocket server = null;//cai nay la serverSocket khac SocketServer
    public Thread       thread = null;
    public int clientCount = 0, port = 37011; // so luong client dang ket noi va cong ket noi MD:37011
    public ServerFrame ui;//giao dien do
    public Database db;//ds user duoc luu

    public SocketServer(ServerFrame frame){//ham khoi tao bang cai giao dien
       
        clients = new ServerThread[50];//50(>>3) client la het dat
        ui = frame;
        db = new Database(ui.filePath);//filePath la cai file chua thong tin user va no duoc nhap tu giao dien
        
	try{  // ko co try catch no ko chiu
	    server = new ServerSocket(port);//tao cai de hong client ket noi
            port = server.getLocalPort();//???
	    ui.jTextArea1.append("[Server IP] : " + InetAddress.getLocalHost() + "\n [Port] : " + server.getLocalPort());//cai dong thong bao
	    start(); 
        }
	catch(IOException ioe){  
            ui.jTextArea1.append("Fail to bind to port : " + port + "\nRetrying..."); 
            ui.RetryStart(0);//tao ko duoc thi lam lai tu dau
	}
    }
    
    public SocketServer(ServerFrame frame, int Port){//thi cung la cai ham khoi tao ma port no ko xai 13000 nua
       
        clients = new ServerThread[50];
        ui = frame;
        port = Port;//khac moi cho nay :v
        db = new Database(ui.filePath);
        
	try{  
	    server = new ServerSocket(port);
            port = server.getLocalPort();
	    ui.jTextArea1.append("[Server IP] : " + InetAddress.getLocalHost() + "\n [Port] : " + server.getLocalPort());
	    start(); 
        }
	catch(IOException ioe){  
            ui.jTextArea1.append("\nFail to bind to port " + port + ": " + ioe.getMessage()); 
	}
    }
	
    public void run(){  //thread.start la no goi cai nay chu dau
	while (thread != null){  
            try{  
		ui.jTextArea1.append("\nWaiting for a client..."); 
	        addThread(server.accept()); //hong duoc client nao thi cho no vo list luon va tiep tuc hong tiep
	    }
	    catch(Exception ioe){ 
                ui.jTextArea1.append("\nServer accept error: \n");
                ui.RetryStart(0);
	    }
        }
    }
	
    public void start(){  //khoi dong thread
    	if (thread == null){  
            thread = new Thread(this); 
	    thread.start();
	}
    }
    
    @SuppressWarnings("deprecation")
    public void stop(){  
        if (thread != null){  
            thread.stop(); 
	    thread = null;
	}
    }
    
    private int findClient(int ID){  //tim client thei ID de xu li
    	for (int i = 0; i < clientCount; i++){//thi duyet 1 luot toi khi nao thay thi tra ve
        	if (clients[i].getID() == ID){
                    return i;
                }
    	}
    	return -1;//ko tim thay tra ve -1
    }
	
    public synchronized void handle(int ID, Message msg){//cai ham xu li Message chu j  
	if (msg.content.equals(".bye")){//nhan duoc tin hieu logout day ma
            Announce("signout", "SERVER", msg.sender,"nani",-1);//in ra ban sender da signout
            remove(ID); //roi cho ra khoi danh sach
	}
	else{
            if(msg.type.equals("login")){//type == login thi message thuoc he login
                if(findUserThread(msg.sender) == null){//niu ban client gui thong diep ko thuoc clients[] moi xu li de tranh spam
                    if(db.checkLogin(msg.sender, msg.content)){//tim trong database xem co ten ban do ko
                    	int te = findClient(ID);
                        clients[te].username = msg.sender;//niu co thi luu lai
                        clients[te].send(new Message("login", "SERVER", "TRUE", msg.sender,"nani",-1));//roi gui thong bao da login thanh cong
                        
                    }
                    else{
                        clients[findClient(ID)].send(new Message("login", "SERVER", "FALSE", msg.sender,"nani",-1));//ko thanh cong thi FALSE
                    } 
                }
                else{
                    clients[findClient(ID)].send(new Message("login", "SERVER", "FALSE", msg.sender,"nani",-1));//...
                }
            }
            else if(msg.type.equals("message")){//neu msg thuoc loai tin nhan
                if(msg.recipient.equals("All")){//nguoi nhan bang All(lo co thang dat ten user = All thi kham)
                    Announce("message", msg.sender, msg.content,"nani",-1);//giu cho tat ca
                }
                else{//con khong thi phai tim roi tu gui lai cho no de in ra
                    findUserThread(msg.recipient).send(new Message(msg.type, msg.sender, msg.content, msg.recipient,"nani",-1));
                    clients[findClient(ID)].send(new Message(msg.type, msg.sender, msg.content, msg.recipient,"nani",-1));
                }
            }
            else if(msg.type.equals("test")){//yeu cau kiem tra ket noi voi server
                clients[findClient(ID)].send(new Message("test", "SERVER", "OK", msg.sender,"nani",-1));
            }
            else if(msg.type.equals("signup")){//y/c dang ki
                if(findUserThread(msg.sender) == null){//chua dang nhap vo moi cho dang ki
                    if(!db.userExists(msg.sender)){//chua co trong ds moi cho dang ki
                        db.addUser(msg.sender, msg.content);//gio add vo database
                        int te = findClient(ID);
                        clients[te].username = msg.sender;//cac buoc sau nhu hoi dang nhap
                        clients[te].send(new Message("signup", "SERVER", "TRUE", msg.sender,"nani",-1));
                        clients[te].send(new Message("login", "SERVER", "TRUE", msg.sender,"nani",-1));
                        //Announce("newuser", "SERVER", msg.sender,clients[te].socket.getInetAddress().getHostAddress(),clients[te].thePORT);
                        //SendUserList(msg.sender);
                    }
                    else{
                        clients[findClient(ID)].send(new Message("signup", "SERVER", "FALSE", msg.sender,"nani",-1));
                    }
                }
                else{
                    clients[findClient(ID)].send(new Message("signup", "SERVER", "FALSE", msg.sender,"nani",-1));
                }
            }
            else if(msg.type.equals("upload_req")){//yeu cau hoi co gui file cho ban khac duoc khong
                if(msg.recipient.equals("All")){//khong gui tran lan
                    clients[findClient(ID)].send(new Message("message", "SERVER", "Uploading to 'All' forbidden", msg.sender,"nani",-1));
                }
                else{
                    findUserThread(msg.recipient).send(new Message("upload_req", msg.sender, msg.content, msg.recipient,"nani",-1));
                }
            }
            else if(msg.type.equals("upload_res")){//phan hoi co cho ban hoi nay gui file khong
                if(!msg.content.equals("NO")){//niu cho thi gui dia chi lai de giu
                    String IP = findUserThread(msg.sender).socket.getInetAddress().getHostAddress();
                    findUserThread(msg.recipient).send(new Message("upload_res", IP, msg.content, msg.recipient,"nani",-1));
                }
                else{//niu ko thi ...
                    findUserThread(msg.recipient).send(new Message("upload_res", msg.sender, msg.content, msg.recipient,"nani",-1));
                }
            } 
            else if(msg.type.equals("port_rep")) {
            	int te = findClient(ID);
            	clients[te].thePORT = msg.port;
            	System.out.println("this is port_rep : " + msg.port +" "+ findUserThread(msg.sender).thePORT);
            	
            	Announce("newuser", "SERVER", msg.sender,clients[te].socket.getInetAddress().getHostAddress(),clients[te].thePORT);
                //TODO there must be something I can do
                SendUserList(msg.sender);//gui cho may ban client khac de biet duong lien lac
            }
	}
    }
    
    public void Announce(String type, String sender, String content, String nani , int tru_1){//gui thong bao cho all user
        Message msg = new Message(type, sender, content, "All",nani,tru_1);
        for(int i = 0; i < clientCount; i++){
            clients[i].send(msg);
        }
    }
    
    public void SendUserList(String toWhom){//gui ngoui moi dang nhap qua cho moi client de cap nhat ds
        ServerThread t = findUserThread(toWhom);
    	for(int i = 0; i < clientCount; i++){
            t.send(new Message("newuser", "SERVER", clients[i].username, toWhom , clients[i].socket.getInetAddress().getHostAddress(),clients[i].thePORT));
        }
    }
    
    public ServerThread findUserThread(String usr){//tim thread theo user name
        for(int i = 0; i < clientCount; i++){
            if(clients[i].username.equals(usr)){
                return clients[i];
            }
        }
        return null;
    }
	
    @SuppressWarnings("deprecation")
    public synchronized void remove(int ID){  //xoa client co ID==ID
    int pos = findClient(ID);//tim ban can xoa
        if (pos >= 0){  //niu khong phai -1 (tim duoc)
            ServerThread toTerminate = clients[pos];//luu lai
            ui.jTextArea1.append("\nRemoving client thread " + ID + " at " + pos);
	    if (pos < clientCount-1){//xoa di
                for (int i = pos+1; i < clientCount; i++){
                    clients[i-1] = clients[i];
	        }
	    }
	    clientCount--;//giam so luong client xuong
	    try{  
	      	toTerminate.close(); //roi dong no lai ko nang may
	    }
	    catch(IOException ioe){  
	      	ui.jTextArea1.append("\nError closing thread: " + ioe); 
	    }
	    toTerminate.stop(); 
	}
    }
    
    private void addThread(Socket socket){  //them 1 cai thread moi vao khi co 1 client dang nhap
	if (clientCount < clients.length){  
            ui.jTextArea1.append("\nClient accepted: " + socket);
	    clients[clientCount] = new ServerThread(this, socket);
	    try{  
	      	clients[clientCount].open(); 
	        clients[clientCount].start();  
	        clientCount++; 
	    }
	    catch(IOException ioe){  
	      	ui.jTextArea1.append("\nError opening thread: " + ioe); 
	    } 
	}
	else{
            ui.jTextArea1.append("\nClient refused: maximum " + clients.length + " reached.");
	}
    }
}

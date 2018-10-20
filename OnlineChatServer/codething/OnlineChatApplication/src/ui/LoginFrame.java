package ui;

import socket.History;
import socket.Message;
import socket.SocketClient;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.*;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import net.miginfocom.swing.MigLayout;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

//import oracle.jrockit.jfr.JFR;

public class LoginFrame extends javax.swing.JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SocketClient client;
    public int port;
    public String serverAddr, username, password;
    //public Thread clientThread;
    public DefaultListModel model;
    public File file;
    File location =  new File(new File("").getAbsolutePath());
    /*public String historyFile = location + "/History.xml"; //file history mac dinh
    public HistoryFrame historyFrame;
    public History hist;*/
    //public ChatFrame chat_man;//=======================>
    
    public LoginFrame() {
    	setAlwaysOnTop(true);
    	try {
    	    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
    	        if ("Nimbus".equals(info.getName())) {
    	            UIManager.setLookAndFeel(info.getClassName());
    	            break;
    	        }
    	    }
    	} catch (Exception e) {
    	    // If Nimbus is not available, you can set the GUI to another look and feel.
    	}
        initComponents();
        this.setTitle("Online Chat Application");
        this.setSize(500, 150);
        this.setResizable(false); //====================================>
        model = new DefaultListModel();//=====================================>
        model.addElement("All");
        getContentPane().setLayout(new MigLayout("", "[59px][157px][81px][73px][13px][84px]", "[19px][27px][25px]"));
        getContentPane().add(jLabel1, "cell 0 0,alignx left,aligny top");
        getContentPane().add(jTextField1, "cell 1 0,growx,aligny top");
        getContentPane().add(jLabel4, "cell 2 0,alignx left,aligny center");
        getContentPane().add(jTextField3, "cell 3 0 3 1,growx,aligny top");
        getContentPane().add(jLabel2, "cell 0 1,alignx right,aligny top");
        getContentPane().add(jTextField2, "cell 1 1,growx,aligny bottom");
        getContentPane().add(jLabel3, "cell 2 1,alignx right,aligny center");
        getContentPane().add(jPasswordField1, "cell 3 1 3 1,growx,aligny center");
        getContentPane().add(jButton1, "cell 1 2,growx,aligny top");
        getContentPane().add(jButton2, "cell 3 2,alignx left,aligny top");
        getContentPane().add(jButton3, "cell 5 2,growx,aligny top");
        
        this.addWindowListener(new WindowListener() {

            @Override public void windowOpened(WindowEvent e) {}
            @Override public void windowClosing(WindowEvent e) { try{ client.send(new Message("message", username, ".bye", "SERVER","nani", -1)); client.clientThread.stop();  }catch(Exception ex){} }
            @Override public void windowClosed(WindowEvent e) {}
            @Override public void windowIconified(WindowEvent e) {}
            @Override public void windowDeiconified(WindowEvent e) {}
            @Override public void windowActivated(WindowEvent e) {}
            @Override public void windowDeactivated(WindowEvent e) {}
        });

    }
    
    public boolean isWin32(){
        return System.getProperty("os.name").startsWith("Windows");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton1.setForeground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
        jButton1.setFont(new Font("Ubuntu", Font.BOLD, 16));
        jTextField3 = new javax.swing.JTextField();
        jTextField3.setText("Viet");
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton3.setForeground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
        jButton3.setFont(new Font("Ubuntu", Font.BOLD, 16));
        jPasswordField1 = new javax.swing.JPasswordField();
        jPasswordField1.setToolTipText("");
        jButton2 = new javax.swing.JButton();
        jButton2.setForeground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
        jButton2.setFont(new Font("Ubuntu", Font.BOLD, 16));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("Server : ");

        jTextField1.setText("localhost");

        jLabel2.setText("Port : ");

        jTextField2.setText("37011");

        jButton1.setText("Connect");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jTextField3.setEnabled(false);

        jLabel3.setText("Password :");

        jLabel4.setText("Username :");

        jButton3.setText("SignUp");
        jButton3.setEnabled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPasswordField1.setEnabled(false);

        jButton2.setText("Login");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        serverAddr = jTextField1.getText(); 
        port = Integer.parseInt(jTextField2.getText());
        
        if(!serverAddr.isEmpty() && !jTextField2.getText().isEmpty()){
            try{
                client = new SocketClient(this);
                client.clientThread = new Thread(client);
                client.clientThread.start();
                client.send(new Message("test", "testUser", "testContent", "SERVER","nani",-1));
            }
            catch(Exception ex){
                //jTextArea1.append("[Application > Me] : Server not found\n");
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        username = jTextField3.getText();
        password = jPasswordField1.getText();
        
        if(!username.isEmpty() && !password.isEmpty()){
            client.send(new Message("login", username, password, "SERVER","nani",-1));
        }
        else {
        	final JPanel panel = new JPanel();
        	JOptionPane.showMessageDialog(panel, "Username and password must be provided!", "Login Failed!", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        username = jTextField3.getText();
        password = jPasswordField1.getText();
        
        if(!username.isEmpty() && !password.isEmpty()){
            client.send(new Message("signup", username, password, "SERVER","nani",-1));
        }
        else {
        	final JPanel panel = new JPanel();
        	JOptionPane.showMessageDialog(panel, "Username and password must be provided!", "Signup Failed!", JOptionPane.WARNING_MESSAGE);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch(Exception ex){
            System.out.println("Look & Feel exception");
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton jButton1;
    public javax.swing.JButton jButton2;
    public javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    public javax.swing.JPasswordField jPasswordField1;
    public javax.swing.JTextField jTextField1;
    public javax.swing.JTextField jTextField2;
    public javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}

package ui;

import socket.History;
import socket.Message;
import socket.SocketClient;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.net.URL;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UIManager.*;

import ui.HistoryFrame;

import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.DropMode;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
//import oracle.jrockit.jfr.JFR;

public class ChatFrame extends javax.swing.JFrame {

    public SocketClient client;
    public int port;
    
    public Thread clientThread;
    public DefaultListModel model;
    public File file;
    File location =  new File(new File("").getAbsolutePath());
    public String historyFile = location + "/History.xml"; //file history mac dinh
    public HistoryFrame historyFrame;
    public History hist;
    
    public ChatFrame() {
    	getContentPane().setFont(new Font("Dialog", Font.PLAIN, 12));
    	setResizable(false);
    	hist = new History(historyFile);
    	historyFrame = new HistoryFrame(hist);
    	historyFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);//===============>
        historyFrame.setVisible(false);
    	setBackground(Color.GRAY);
    	getContentPane().setBackground(Color.DARK_GRAY);
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
        this.setSize(700,600);
        this.setTitle("Online Chat Application");
        model.addElement("All");
        jList1.setSelectedIndex(0);
        
        JScrollPane scrollPane = new JScrollPane();
        
        usernameField = new JTextField();
        usernameField.setFont(new Font("Courier 10 Pitch", Font.BOLD, 18));
        usernameField.setText("username");
        usernameField.setEnabled(false);
        usernameField.setEditable(false);
        usernameField.setHorizontalAlignment(SwingConstants.CENTER);
        usernameField.setForeground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
        usernameField.setBackground(Color.DARK_GRAY);
        usernameField.setColumns(10);
        GroupLayout groupLayout = new GroupLayout(getContentPane());
        groupLayout.setHorizontalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGap(12)
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
        						.addComponent(jLabel6, GroupLayout.PREFERRED_SIZE, 43, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jLabel5))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        						.addComponent(jTextField5, GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)
        						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE)))
        				.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 499, GroupLayout.PREFERRED_SIZE))
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addComponent(jButton5, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
        					.addComponent(jButton6, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(jButton4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
        					.addComponent(jButton8, GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
        					.addComponent(jScrollPane2, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)))
        			.addGap(20))
        		.addGroup(groupLayout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(586, Short.MAX_VALUE))
        );
        groupLayout.setVerticalGroup(
        	groupLayout.createParallelGroup(Alignment.LEADING)
        		.addGroup(groupLayout.createSequentialGroup()
        			.addGap(12)
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 371, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jScrollPane2, GroupLayout.PREFERRED_SIZE, 371, GroupLayout.PREFERRED_SIZE))
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(12)
        					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        						.addGroup(groupLayout.createSequentialGroup()
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(jButton4))
        						.addGroup(groupLayout.createSequentialGroup()
        							.addGap(10)
        							.addComponent(jLabel5)))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(jButton8, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(18)
        					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        			.addGap(5)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jTextField5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel6)
        				.addComponent(jButton5)
        				.addComponent(jButton6, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        			.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap())
        );
        
        jTextField4 = new JTextArea();
        scrollPane.setViewportView(jTextField4);
        jTextField4.setWrapStyleWord(true);
        jTextField4.setLineWrap(true);
        jTextField4.setRows(5);
        getContentPane().setLayout(groupLayout);
        
        this.addWindowListener(new WindowListener() {

            @Override public void windowOpened(WindowEvent e) {}
            @Override public void windowClosing(WindowEvent e) { client.runForver = false;try{ client.send(new Message("message", client.username, ".bye", "SERVER","nani",-1)); clientThread.stop();  }catch(Exception ex){} }
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
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextArea1.setEditable(false);
        jTextArea1.setLineWrap(true);
        jScrollPane2 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jLabel5 = new javax.swing.JLabel();
        jLabel5.setFont(new Font("Ubuntu", Font.BOLD, 16));
        jLabel5.setForeground(Color.LIGHT_GRAY);
        jButton4 = new javax.swing.JButton();
        jButton4.setFont(new Font("Ubuntu", Font.BOLD, 16));
        jSeparator2 = new javax.swing.JSeparator();
        jTextField5 = new javax.swing.JTextField();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton6.setFont(new Font("Ubuntu", Font.BOLD, 16));
        jLabel6 = new javax.swing.JLabel();
        jLabel6.setForeground(Color.LIGHT_GRAY);
        jLabel6.setFont(new Font("Ubuntu", Font.BOLD, 16));
        jButton8 = new javax.swing.JButton();
        jButton8.setFont(new Font("Ubuntu", Font.BOLD, 16));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        jList1.setModel((model = new DefaultListModel()));
        jScrollPane2.setViewportView(jList1);

        jLabel5.setText("Message : ");

        jButton4.setText("Send");
        jButton4.setEnabled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("...");
        jButton5.setEnabled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Transfer");
        jButton6.setEnabled(false);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel6.setText("File :");

        jButton8.setText("History");
        jButton8.setEnabled(false);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String msg = jTextField4.getText();
        String target = jList1.getSelectedValue().toString();
        
        if(!msg.isEmpty() && !target.isEmpty()){
            jTextField4.setText("");
            client.send2(new Message("message", client.username, msg, target,"nani",-1));
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showDialog(this, "Select File");
        file = fileChooser.getSelectedFile();
        
        if(file != null){
            if(!file.getName().isEmpty()){
                jButton6.setEnabled(true); String str;
                
                if(jTextField5.getText().length() > 30){
                    String t = file.getPath();
                    str = t.substring(0, 20) + " [...] " + t.substring(t.length() - 20, t.length());
                }
                else{
                    str = file.getPath();
                }
                jTextField5.setText(str);
            }
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
            long size = file.length();
            if(size < 124 * 1024 * 1024){
                client.send(new Message("upload_req", client.username, file.getName(), jList1.getSelectedValue().toString(),"nani",-1));
            }
            else{
            	final JPanel panel = new JPanel();
            	JOptionPane.showMessageDialog(panel, "File is size too large!", "Transfer Failed!", JOptionPane.WARNING_MESSAGE);
            }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        historyFrame.setLocation(this.getLocation());
        historyFrame.setVisible(true);
    }//GEN-LAST:event_jButton8ActionPerformed

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } 
        catch(Exception ex){
            System.out.println("Look & Feel exception");
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatFrame().setVisible(true);
            }
        });
        
    }
    public javax.swing.JButton jButton4;
    public javax.swing.JButton jButton5;
    public javax.swing.JButton jButton6;
    public javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    public javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator2;
    public javax.swing.JTextArea jTextArea1;
    public javax.swing.JTextField jTextField5;
    private JTextArea jTextField4;
    public JTextField usernameField;
}

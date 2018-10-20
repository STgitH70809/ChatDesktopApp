package socket;

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

public class ChatFrameUI extends javax.swing.JFrame {

    public SocketClient client;
    public int port;
    
    public Thread clientThread;
    public DefaultListModel model;
    public File file;
    File location =  new File(new File("").getAbsolutePath());
    public String historyFile = location + "/History.xml"; //file history mac dinh
    public HistoryFrame historyFrame;
    public History hist;
    
    public ChatFrameUI() {
    	getContentPane().setFont(new Font("Dialog", Font.PLAIN, 12));
    	setResizable(false);
    	hist = new History(historyFile);
    	historyFrame = new HistoryFrame(hist);
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
        nameList.setSelectedIndex(0);
        
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
        			.addComponent(filePathBtn, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.RELATED)
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
        					.addComponent(transferBtn, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        					.addComponent(sendBtn, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
        					.addComponent(HisBtn, GroupLayout.DEFAULT_SIZE, 152, Short.MAX_VALUE)
        					.addComponent(jScrollPaneNameList, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 152, GroupLayout.PREFERRED_SIZE)))
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
        				.addComponent(jScrollPaneNameList, GroupLayout.PREFERRED_SIZE, 371, GroupLayout.PREFERRED_SIZE))
        			.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(12)
        					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
        						.addGroup(groupLayout.createSequentialGroup()
        							.addPreferredGap(ComponentPlacement.RELATED)
        							.addComponent(sendBtn))
        						.addGroup(groupLayout.createSequentialGroup()
        							.addGap(10)
        							.addComponent(jLabel5)))
        					.addPreferredGap(ComponentPlacement.RELATED)
        					.addComponent(HisBtn, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
        				.addGroup(groupLayout.createSequentialGroup()
        					.addGap(18)
        					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
        			.addGap(5)
        			.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(jTextField5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        				.addComponent(jLabel6)
        				.addComponent(filePathBtn)
        				.addComponent(transferBtn, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        			.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        			.addComponent(usernameField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap())
        );
        
        inputTextField = new JTextArea();
        scrollPane.setViewportView(inputTextField);
        inputTextField.setWrapStyleWord(true);
        inputTextField.setLineWrap(true);
        inputTextField.setRows(5);
        getContentPane().setLayout(groupLayout);
        
        this.addWindowListener(new WindowListener() {

            @Override public void windowOpened(WindowEvent e) {}
            @Override public void windowClosing(WindowEvent e) { try{ client.send(new Message("message", client.username, ".bye", "SERVER")); clientThread.stop();  }catch(Exception ex){} }
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
        TextArea = new javax.swing.JTextArea();
        TextArea.setEditable(false);
        TextArea.setLineWrap(true);
        jScrollPaneNameList = new javax.swing.JScrollPane();
        nameList = new javax.swing.JList();
        jLabel5 = new javax.swing.JLabel();
        jLabel5.setFont(new Font("Ubuntu", Font.BOLD, 16));
        jLabel5.setForeground(Color.LIGHT_GRAY);
        sendBtn = new javax.swing.JButton();
        sendBtn.setFont(new Font("Ubuntu", Font.BOLD, 16));
        jSeparator2 = new javax.swing.JSeparator();
        jTextField5 = new javax.swing.JTextField();
        filePathBtn = new javax.swing.JButton();
        transferBtn = new javax.swing.JButton();
        transferBtn.setFont(new Font("Ubuntu", Font.BOLD, 16));
        jLabel6 = new javax.swing.JLabel();
        jLabel6.setForeground(Color.LIGHT_GRAY);
        jLabel6.setFont(new Font("Ubuntu", Font.BOLD, 16));
        HisBtn = new javax.swing.JButton();
        HisBtn.setFont(new Font("Ubuntu", Font.BOLD, 16));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TextArea.setColumns(20);
        TextArea.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        TextArea.setRows(5);
        jScrollPane1.setViewportView(TextArea);

        nameList.setModel((model = new DefaultListModel()));
        jScrollPaneNameList.setViewportView(nameList);

        jLabel5.setText("Message : ");

        sendBtn.setText("Send");
        sendBtn.setEnabled(false);
        sendBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        filePathBtn.setText("...");
        filePathBtn.setEnabled(false);
        filePathBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        transferBtn.setText("Transfer");
        transferBtn.setEnabled(false);
        transferBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jLabel6.setText("File :");

        HisBtn.setText("History");
        HisBtn.setEnabled(false);
        HisBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String msg = inputTextField.getText();
        String target = nameList.getSelectedValue().toString();
        
        if(!msg.isEmpty() && !target.isEmpty()){
            inputTextField.setText("");
            client.send(new Message("message", client.username, msg, target));
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showDialog(this, "Select File");
        file = fileChooser.getSelectedFile();
        
        if(file != null){
            if(!file.getName().isEmpty()){
                transferBtn.setEnabled(true); String str;
                
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
                client.send(new Message("upload_req", client.username, file.getName(), nameList.getSelectedValue().toString()));
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
    public javax.swing.JButton sendBtn;
    public javax.swing.JButton filePathBtn;
    public javax.swing.JButton transferBtn;
    public javax.swing.JButton HisBtn;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    public javax.swing.JList nameList;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPaneNameList;
    private javax.swing.JSeparator jSeparator2;
    public javax.swing.JTextArea TextArea;
    public javax.swing.JTextField jTextField5;
    private JTextArea inputTextField;
    public JTextField usernameField;
}

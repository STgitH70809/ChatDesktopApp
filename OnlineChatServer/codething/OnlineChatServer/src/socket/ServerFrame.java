package socket;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.URL;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ImageIcon;

import javax.swing.UIManager.*;
import java.awt.Font;



public class ServerFrame extends javax.swing.JFrame {//class giao dien cua server

    public SocketServer server;//cai socketServer de chay ui nay(khac serversocket)
    public Thread serverThread;
    File location =  new File(new File("").getAbsolutePath());
    public String filePath = location + "/Data.xml";//file database mac dinh
    public JFileChooser fileChooser;
    
    public ServerFrame() {//khoi tao...
    	
    	System.out.println(filePath);
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
    	getContentPane().setBackground(Color.GRAY);
    	setBackground(Color.GRAY);
        initComponents();
        
        fileChooser = new JFileChooser();
        jTextArea1.setEditable(false);
    }
    
    public boolean isWin32(){//he diu hanh la co phai win32 ko
        return System.getProperty("os.name").startsWith("Windows");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() { // khoi tao button, jtextfield,area...

        jButton1 = new javax.swing.JButton();
        jButton1.setFont(new Font("Courier 10 Pitch", Font.BOLD, 16));
        jButton1.setForeground(Color.LIGHT_GRAY);
        jButton1.setBackground(Color.BLACK);
        jButton1.setText("Start");
        jButton1.setToolTipText("Start");
        jButton1.setSelectedIcon(new ImageIcon("/img/press-play-button.png"));
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextArea1.setBackground(Color.DARK_GRAY);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Online Chat Server");
        jButton1.setEnabled(true);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 634, Short.MAX_VALUE)
        			.addContainerGap())
        		.addGroup(layout.createSequentialGroup()
        			.addGap(266)
        			.addComponent(jButton1, GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
        			.addGap(272))
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.TRAILING)
        		.addGroup(Alignment.LEADING, layout.createSequentialGroup()
        			.addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 310, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(jButton1)
        			.addContainerGap(22, Short.MAX_VALUE))
        );
        getContentPane().setLayout(layout);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        server = new SocketServer(this);//nhan nut start thi tao 1 server moi
        jButton1.setEnabled(false);
    }//GEN-LAST:event_jButton1ActionPerformed

    public void RetryStart(int port){//ham ket noi lai
        if(server != null){ server.stop(); }
        server = new SocketServer(this, port);
    }

    public static void main(String args[]) {

        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception ex){
            System.out.println("Look & Feel Exception");
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ServerFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JTextArea jTextArea1;
    // End of variables declaration//GEN-END:variables
}

package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import javax.swing.JLayeredPane;
import java.awt.CardLayout;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;

import net.miginfocom.swing.MigLayout;
import socket.History;
import socket.Message;
import socket.SocketClient;

import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

public class ChatFrame extends JFrame {
	/**
	 * Variable Declaration.
	 */
	private JPanel MainPanel;
	public SocketClient client;
	public File file;
    File location =  new File(new File("").getAbsolutePath());
    public String historyFile = location + "/History.xml";
    public History hist;
    public DefaultListModel model;
    private int indexChatArea;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatFrame frame = new ChatFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ChatFrame() {
		hist = new History(historyFile);//=========>
		setTitle("JatQ");
		setResizable(false);
		setBackground(Color.DARK_GRAY);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 500);
		MainPanel = new JPanel();
		MainPanel.setBackground(Color.LIGHT_GRAY);
		MainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(MainPanel);
		
		scrollNamePanel = new JScrollPane();
		
		JLayeredPane layeredChatPanel = new JLayeredPane();
		
		JPanel functionPanel = new JPanel();
		functionPanel.setBackground(UIManager.getColor("Button.darkShadow"));
		
		
		usernameList = new JList();
		
		
		usernameList.setModel((model = new DefaultListModel()));
		model.addElement("All");
		usernameList.setSelectedIndex(0);
		usernameList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if(indexChatArea != usernameList.getSelectedIndex()) {
					scrollChatPanel[indexChatArea].setVisible(false);
					indexChatArea = usernameList.getSelectedIndex();
					scrollChatPanel[indexChatArea].setVisible(true);
				}
			}
		});
		scrollNamePanel.setViewportView(usernameList);
		MainPanel.setLayout(new MigLayout("", "[610px][112px]", "[390.00px][72.00px,center][-32.00]"));
		MainPanel.add(functionPanel, "cell 0 1,grow");
		
		scrollTextingPanel = new JScrollPane();
		scrollTextingPanel.setBounds(0, 5, 473, 46);
		
		dirBtn = new JButton("...");
		dirBtn.setEnabled(true);
		dirBtn.setForeground(new Color(128, 128, 128));
		dirBtn.setBounds(474, 5, 49, 46);
		dirBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	dirBtnActionPerformed(evt);
            }
        });
		
		sendBtn = new JButton("Send");
		sendBtn.setBackground(UIManager.getColor("Button.focus"));
		sendBtn.setEnabled(false);
		sendBtn.setForeground(new Color(30, 144, 255));
		sendBtn.setFont(new Font("Ubuntu Mono", Font.PLAIN, 22));
		sendBtn.setBounds(524, 5, 86, 46);
		sendBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
            	sendBtnActionPerformed(evt);
            }
        });
		functionPanel.setLayout(null);
		functionPanel.add(dirBtn);
		functionPanel.add(sendBtn);
		functionPanel.add(scrollTextingPanel);
		
		inputTextArea = new JTextArea();
		inputTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String msg = inputTextArea.getText();
				if(!msg.isEmpty())
					sendBtn.setEnabled(true);
				else
					sendBtn.setEnabled(false);
			}
		});
		
		inputTextArea.setWrapStyleWord(true);
		inputTextArea.setRows(5);
		inputTextArea.setLineWrap(true);
		scrollTextingPanel.setViewportView(inputTextArea);
		MainPanel.add(layeredChatPanel, "cell 0 0,grow");
		layeredChatPanel.setLayout(new CardLayout(0, 0));
		
		for(int i = 0; i < 50; i++) {
			scrollChatPanel[i] = new JScrollPane();
			layeredChatPanel.add(scrollChatPanel[i]);
			if(i == usernameList.getSelectedIndex()) {
				scrollChatPanel[i].setVisible(true);
				indexChatArea = i;
			}
			else {
				scrollChatPanel[i].setVisible(false);
			}
			chatArea[i] = new JTextArea();
			chatArea[i].setRows(5);
			chatArea[i].setLineWrap(true);
			chatArea[i].setFont(new Font("Dialog", Font.PLAIN, 12));
			chatArea[i].setEditable(false);
			chatArea[i].setColumns(20);
			scrollChatPanel[i].setViewportView(chatArea[i]);
		}
		MainPanel.add(scrollNamePanel, "cell 1 0 1 2,grow");
		
		
	}
		
	/**
	 * Check is Window 32.
	 */
	public boolean isWin32(){
        return System.getProperty("os.name").startsWith("Windows");
    }
	/**
	 * Execute when Send Button is clicked
	 */
	private void sendBtnActionPerformed(java.awt.event.ActionEvent evt) {
        String msg = inputTextArea.getText();
        String target = usernameList.getSelectedValue().toString();
        
        if(!msg.isEmpty() && !target.isEmpty()){
        	inputTextArea.setText("");
            client.sendToClient(new Message("message", client.username, msg, target,"nani", -1));
        }
        if(file != null){
        	long size = file.length();
            if(size < 124 * 1024 * 1024){
                client.sendToClient(new Message("upload_req", client.username, file.getName(), usernameList.getSelectedValue().toString(),"nani", -1));
            }
            else{
            	final JPanel panel = new JPanel();
            	JOptionPane.showMessageDialog(panel, "File is size too large!", "Transfer Failed!", JOptionPane.WARNING_MESSAGE);
            }
            file = null;
        }
    }
	/**
	 * Execute when Dir Button is clicked
	 */
	private void dirBtnActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showDialog(this, "Select File");
        file = fileChooser.getSelectedFile();
        
        if(file != null){
        	sendBtn.setEnabled(true);
        }
    }
	
	public  JButton sendBtn;
	public  JButton dirBtn;
	private JTextArea inputTextArea;
	public  JTextArea []chatArea = new JTextArea[50];
	public  String username;
	public  JList usernameList;
	private JScrollPane scrollNamePanel;
	private JScrollPane []scrollChatPanel = new JScrollPane[50];
	private JScrollPane scrollTextingPanel;
}

package socket;

import java.io.*;
import java.util.ArrayList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;
import ui.HistoryFrame;
import javax.swing.table.DefaultTableModel;

public class History {//class doc lich su chat chit
    
    public String filePath;
    
    public History(String filePath){
        this.filePath = filePath;
    }
    
    public void addMessage(Message msg, String time){
        //them tin nhan moi vao history
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(filePath);

            Node data = doc.getFirstChild();
            
            Element message = doc.createElement("message");
            Element _sender = doc.createElement("sender"); _sender.setTextContent(msg.sender);
            Element _content = doc.createElement("content"); _content.setTextContent(msg.content);
            Element _recipient = doc.createElement("recipient"); _recipient.setTextContent(msg.recipient);
            Element _time = doc.createElement("time"); _time.setTextContent(time);
            
            message.appendChild(_sender); message.appendChild(_content); message.appendChild(_recipient); message.appendChild(_time);
            data.appendChild(message);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
	    } 
            catch(Exception ex){
		System.out.println("Exceptionmodify xml");
	    }
	}

    public void FillTable(HistoryFrame frame){//in du lieu trong file ra bang

        DefaultTableModel model = (DefaultTableModel) frame.jTable1.getModel();
    
        try{
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            
            NodeList nList = doc.getElementsByTagName("message");
            
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    model.addRow(new Object[]{getTagValue("sender", eElement), getTagValue("content", eElement), getTagValue("recipient", eElement), getTagValue("time", eElement)});
                }
            }
        }
        catch(Exception ex){
            System.out.println("Filling Exception");
        }
    }
    
    public String VoltalicChain(String nameS,String nameR){//lay du lieu theo ten
    	String THE_STRING="";
    
        try{
            File fXmlFile = new File(filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();
            
            NodeList nList = doc.getElementsByTagName("message");
            
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String _sender = getTagValue("sender", eElement);
                    String _content = getTagValue("content", eElement);
                    String _recipient = getTagValue("recipient", eElement);
                    String _time = getTagValue("time", eElement);
                    if(nameS.equals(_sender) && nameR.equals(_recipient)) {
                    	THE_STRING += ">>" + _sender + "\n" + _content + "\n";
                    }else {
                    	if(nameR.equals(_sender) && nameS.equals(_recipient)) {
                    		THE_STRING += ">>" + _sender + "\n" + _content + "\n";
                    	}
                    }
                }
            }
        }
        catch(Exception ex){
            System.out.println("NO THING Exception");
            return THE_STRING;
        }
        return THE_STRING;
    }
    
    public static String getTagValue(String sTag, Element eElement) {
	NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
	return nValue.getNodeValue();
  }
}

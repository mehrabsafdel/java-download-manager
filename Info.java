import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.sql.Time;

/**
 * this class is for saving details of each download
 * like the name and the size and the url address
 * and saving address or some more details
 */
public class Info implements Serializable{
    //proper fields
    String name;
    float size;
    String urlAddress;
    String savingAddress;
    int time;
    boolean isFinished = false;
    /**
     * this constructor initializing the fields
     * @param name is the name
     * @param size is the size
     * @param urlAddress is the url addres
     * @param savingAddress is the saving address
     */
    public Info(String name,float size,String urlAddress,String savingAddress){
        this.name = name;
        this.size = size;
        this.urlAddress = urlAddress;
        this.savingAddress = savingAddress;
    }

    /**
     * anothe constructor
     * @param name is the name
     * @param urlAddress is the saving address
     */
    public Info(String name,String urlAddress){
        this.name = name;
        this.urlAddress = urlAddress;
    }

    /**
     * another constructro
     */
    public Info(){
        name = "";
        size = 0;
        urlAddress = "";
        savingAddress = "";
        time = 0;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void set(String name,float size,String urlAddress,String savingAddress){
        this.name = name;
        this.size = size;
        this.urlAddress = urlAddress;
        this.savingAddress = savingAddress;
    }
    /**
     * this method show this details of downloading file
     * in a new frame and a new window
     */
    public void show(){
        JFrame info = new JFrame();
        JPanel panel = new JPanel(new GridLayout(5,1));
        JTextField textFieldName = new JTextField("name of file is: " + name);
        JTextField textFieldUrl = new JTextField("url address is: " + urlAddress);
        JTextField textFieldSave = new JTextField("saving address is: " + savingAddress);
        JTextField textFieldSize = new JTextField("size of file is: " + size);

        panel.add(textFieldName);
        panel.add(textFieldUrl);
        panel.add(textFieldSave);
        panel.add(textFieldSize);

        textFieldName.setEditable(false);
        textFieldSave.setEditable(false);
        textFieldSize.setEditable(false);
        textFieldUrl.setEditable(false);

        info.add(panel);
        info.setBackground(Color.gray);
        info.setSize(500, 300);

        info.setTitle("downloading info");
        info.setLocation(75,50);
        info.setVisible(true);
    }
}

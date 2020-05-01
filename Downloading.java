import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

//import static jdk.jfr.internal.instrument.JDKEvents.remove;

/**
 * this class is a downloading file that has a panel to show the download process
 * and has a object of Info class to show more
 * details of downloading file
 */
public class Downloading implements Serializable{
    float speed;
    Download download;
    //panel fields

    JPanel downloadProcess;
    JPanel buttonPanel;
    JProgressBar progressBar;
    //an object from Info class
    Info info;
    //boolean fields
    boolean selected = false;
    boolean isFinished = false;
    //value field
    int value = 50;
    //an object from Action class
    Action action = new Action();
    //button fields
    JButton buttonResume = new JButton();
    JButton buttonPause = new JButton();
    JButton buttonRemove = new JButton();
    JButton buttonSelect;
    /**
     * this object is for get the right mouse click
     * and do the proper operation when
     * right mouse clicked
     */
    MouseListener mouseListener = new MouseListener() {
        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getModifiers() == MouseEvent.BUTTON3_MASK )
                info.show();
            else if (e.getClickCount() == 2)
                open();
        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }
    };

    /**
     * this constructor initial the fields
     * and make the panel of download with details like progress bar
     * or buttons panel
     * @param downloadName is the name of file
     * @param downloadUrl is the web size address
     * @param ssavingAddress is the saving address
     */
    public Downloading(String downloadName,float size,String downloadUrl,String ssavingAddress){
        info = new Info();
        info.size = size;
        if (info.size == 0) {
            try {
                info.size = getFileSize(new URL(downloadUrl));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        //info = new Info(downloadName,downloadUrl,ssavingAddress);
        downloadProcess = new JPanel(new BorderLayout());
        downloadProcess.addMouseListener(mouseListener);
        buttonPanel = new JPanel(new GridLayout(1,4));
        progressBar = new JProgressBar();
        //progressBar.setValue(value);
        buttonSelect = new JButton();
        buttonResume.setBackground(Color.white);
        buttonPause.setBackground(Color.white);
        buttonRemove.setBackground(Color.white);

        buttonSelect.addActionListener(action);
        buttonPause.addActionListener(action);
        buttonRemove.addActionListener(action);
        buttonResume.addActionListener(action);

        progressBar.setForeground(Color.GREEN);
        progressBar.setStringPainted(true);

        try {
            Image image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\download.png"));
            buttonSelect.setIcon(new ImageIcon(image));
            buttonSelect.setBackground(Color.white);
            downloadProcess.add(buttonSelect,BorderLayout.WEST);
            downloadProcess.add(progressBar,BorderLayout.CENTER);
            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\pause1.png"));
            buttonPause.setIcon(new ImageIcon(image));
            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\resume.png"));
            buttonResume.setIcon(new ImageIcon(image));
            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\remove1.png"));
            buttonRemove.setIcon(new ImageIcon(image));
        }
        catch (Exception e){
            System.out.println(e);
        }

        buttonPanel.add(buttonPause);
        buttonPanel.add(buttonResume);
        buttonPanel.add(buttonRemove);
        JTextArea textArea = new JTextArea(downloadName + " speed is " + "downloaded" + (progressBar.getValue()/100)*info.size + "from" + info.size);
        textArea.setEditable(false);
        downloadProcess.add(textArea,BorderLayout.SOUTH);
        downloadProcess.add(buttonPanel,BorderLayout.EAST);
        downloadProcess.setBackground(Color.white);
        downloadProcess.setBorder(new EmptyBorder(2,2,2,2));
    }

    /**
     * this method return the main panel of this class
     * @return the downloadProcess field
     */
    public JPanel getDownloadProcess() {
        return downloadProcess;
    }

    /**
     * set the value field
     * @param percent is the value of downloading
     */
    public void setValue(int percent) {
        value = percent;
        progressBar.setValue(percent);
    }

    /**
     * @return value field
     */
    public int getValue() {
        return value;
    }

    /**
     * pause downloading
     */
    public void pause(){
        download.pause();
        System.out.println("pause");
    }

    /**
     * resume downloading
     */
    public void resume(){
        download.resume();
        //System.out.println("start");
    }

    /**
     * set the selected field true or false
     * this method is for select the download or un select
     */
    public void select(){
        if (selected){
            selected = false;
            buttonSelect.setBackground(Color.white);
        }
        else{
            selected = true;
            buttonSelect.setBackground(Color.cyan);
        }
    }

    /**
     * this INNER class is for set the Action of each button
     * and do the proper operation when a button clicked
     */

    public void open(){
        System.out.println(info.savingAddress);
        File file = new File(info.savingAddress);

        //first check if Desktop is supported by Platform or not
        if(!Desktop.isDesktopSupported()){
            System.out.println("Desktop is not supported");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        if(file.exists()) {
            try {
                desktop.open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * starting a downloading
     * @param downloadName is the download name
     * @param downloadUrl is the url adderess
     * @param ssavingAddress is the saving address
     */
    public void startDownload(String downloadName,String downloadUrl,String ssavingAddress){
        try {
            download = new Download(new URL(downloadUrl),ssavingAddress,downloadName,progressBar,info.size,isFinished);
            System.out.println(getFileSize(new URL(downloadUrl)) + " mamad");
            info.set(downloadName, getFileSize(new URL(downloadUrl)), downloadUrl, ssavingAddress);
            info.savingAddress = download.Address;
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    /**
     * this method get the size of downloading file
     * @param url is hte url address of a downliading
     * @return the size of downloading
     */
    public static float getFileSize(URL url) {
        URLConnection conn = null;
        try {
            conn = url.openConnection();
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).setRequestMethod("HEAD");
            }
            conn.getInputStream();
            return (float) conn.getContentLength() / (1024*1024);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(conn instanceof HttpURLConnection) {
                ((HttpURLConnection)conn).disconnect();
            }
        }
    }

    /**
     * cancel a downloading
     */
    public void cancel() {
        download.cancel();
        progressBar.setValue(0);
    }

    /**
     * this calss is a inner calss for action listener
     */
    class Action implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buttonSelect){
                select();
            }
            else if (e.getSource() == buttonPause)
                pause();
            else if (e.getSource() == buttonResume)
                resume();
            else if (e.getSource() == buttonRemove)
                select();

        }
    }

}

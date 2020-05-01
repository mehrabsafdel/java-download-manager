import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * this class is a GUI for show the main window of our
 * download manager
 * has many button fields and panels and frames
 * update the proper frame in the proper chapter
 * during the download manager is running
 */

public class Gui{
    static int counter = 0;
    DownloadingList downloadingList;
    removedDownload removedDownload;
    DownloadingList queueDownloadList;
    DownloadingList completedList;

    SettingFile settingFile;
    Filter filter = new Filter();
    JTextField filterInput;
    //this fields is for number of downloads
    int numberOfDownloads = 0;
    //some panel and frame and action listener fields and string
    Action action = new Action();
    SystemTray systemTray = SystemTray.getSystemTray();
    TrayIcon trayIcon = new TrayIcon(new ImageIcon("\\EagleGetIcons\\icon.png").getImage());
    JFrame frame;
    JPanel centerPanel;
    JPanel leftPanel;
    JPanel downloadPanel;
    JPanel completePanel;
    JPanel QueuePanel;
    BorderLayout borderLayout = new BorderLayout();
    static ArrayList<Downloading> downloads;
    static ArrayList<Downloading> Queue;
    static  ArrayList<Downloading> completed;
    JFrame settingFrame;

    //j menu bar button
    JMenuBar jMenuBar = new JMenuBar();
    JMenu menu1 = new JMenu("Download");
    JMenu menu2 = new JMenu("Help");
    JMenuItem jMenuItemNewDownload = new JMenuItem("New Download");
    JMenuItem jMenuItemPause = new JMenuItem("Pause");
    JMenuItem jMenuItemResume = new JMenuItem("Resume");
    JMenuItem jMenuItemCancel = new JMenuItem("Cancel");
    JMenuItem jMenuItemRemove = new JMenuItem("Remove");
    JMenuItem jMenuItemSetting = new JMenuItem("Setting");
    JMenuItem jMenuItemExit = new JMenuItem("Exit");
    JMenuItem jMenuItemAbout = new JMenuItem("About");

    //tool bar button
    JButton buttonNewDownload = new JButton();
    JButton buttonPause = new JButton();
    JButton buttonResume = new JButton();
    JButton buttonCancel = new JButton();
    JButton buttonRemove = new JButton();
    JButton buttonSetting = new JButton();
    JButton buttonSort = new JButton();


    //left panel button
    JButton buttonProcess = new JButton("Processing");
    JButton buttonComplete = new JButton("Completed");
    JButton buttonQueues = new JButton("Queues");
    JButton recycle_bin = new JButton("recycle bin");

    //setting button
    JButton setingButtonAct;
    JButton LAF_1;
    JButton LAF_2;
    JButton LAF_3;
    JSpinner spinner;

    //new download tab text fields
    JTextField urlAddress;
    JTextField fileName;
    JTextField savingAddress;
    //setting text fields
    JTextField settingText;
    //new download frame button
    JRadioButton radioButtonQueue = new JRadioButton("Queue");
    JRadioButton radioButtonAutomatic = new JRadioButton("automatically");

    JFrame sortFrame;
    JRadioButton nameSort = new JRadioButton("Name");
    JRadioButton sizeSort = new JRadioButton("Size");
    JRadioButton timeSort = new JRadioButton("Time");
    JButton upwardSort = new JButton("upward");
    JButton downwardSort = new JButton("downward");

    JButton newDownloadOk = new JButton("ok");
    JButton newDownloadCancel = new JButton("cancel");
    JFrame newDownloadFrame ;

    JFrame searchframe;
    JTextField searchText;
    JButton searchButton;
    JButton srch;
    JPanel searchResultPanel = new JPanel();

    static int i = 0;
    /**
     * this constructor build all of the fields objects and show the first window
     * of download manager
     * the main part of this is the frame fields that handle all of the other panels
     */
    public Gui() {
        downloadingList = new DownloadingList();
        removedDownload = new removedDownload();
        queueDownloadList = new DownloadingList();
        completedList = new DownloadingList();
        completed = new ArrayList<>();
        settingFile = new SettingFile();
        try {
            systemTray.add(trayIcon);
            trayIcon.addActionListener(action);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        downloads = new ArrayList<>();
        Queue = new ArrayList<>();
        centerPanel = new JPanel(new BorderLayout());
        leftPanel = new JPanel(new GridLayout(3, 1));
        leftPanel.setBackground(Color.DARK_GRAY);
        downloadPanel = new JPanel(new GridLayout(6, 1));
        completePanel = new JPanel(new GridLayout(6,1));
        QueuePanel = new JPanel(new GridLayout(6,1));
        QueuePanel.setBackground(Color.white);
        completePanel.setBackground(Color.white);
        downloadPanel.setBackground(Color.white);
        centerPanel.add(downloadPanel,BorderLayout.CENTER);

        downloadRead();
        //completeRead();
        queueRead();
        removeRead();
        settingRead();
        filterRead();

        for (Info info:downloadingList.infos) {
            Downloading dn = new Downloading(info.name,info.size,info.urlAddress,info.savingAddress);
            dn.info.isFinished = true;
            //dn.download.downloadFinished = true;
            completed.add(dn);
            System.out.println();
        }
        for (Info info:queueDownloadList.infos) {
            Downloading dn = new Downloading(info.name,info.size,info.urlAddress,info.savingAddress);
            dn.info.isFinished = true;
            //dn.download.downloadFinished = true;
            completed.add(dn);
            System.out.println();
        }
       /* for (Info info:completedList.infos) {
            Downloading dn = new Downloading(info.name,info.size,info.urlAddress,info.savingAddress);
            dn.info.isFinished = true;
            //dn.download.downloadFinished = true;
            completed.add(dn);
            System.out.println();
        }*/
        frame = new JFrame();
        frame.setSize(1000, 500);
        frame.setResizable(true);
        frame.setTitle("java download manager");
        frame.setLayout(borderLayout);

        JMenuBar();
        JToolBar();
        LeftBar();

        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(leftPanel, BorderLayout.WEST);

        lookAndFeel(frame,settingFile.LAF);
        frame.setVisible(true);
        updateProcessingFrame();
    }

    /**
     * this method read the download details from file
     */
    private void downloadRead(){
        ObjectInputStream ois = null;
        try {
            FileInputStream fin = new FileInputStream("list.jdm");
            ois = new ObjectInputStream(fin);
            downloadingList = (DownloadingList) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * this method read the complete download from file
     */
    private void completeRead(){/*
        ObjectInputStream ois = null;
        try {
            FileInputStream fin = new FileInputStream("complete.jdm");
            ois = new ObjectInputStream(fin);
            completedList = (DownloadingList) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * this method read the queue download from file
     */
    private void queueRead(){
        ObjectInputStream ois = null;
        try {
            FileInputStream fin = new FileInputStream("queue.jdm");
            ois = new ObjectInputStream(fin);
            queueDownloadList = (DownloadingList) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     *      * this method read the removed download from file
     */
    private void removeRead(){
        ObjectInputStream ois = null;
        try {
            FileInputStream fin = new FileInputStream("removed.jdm");
            ois = new ObjectInputStream(fin);
            removedDownload = (removedDownload) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     *      * this method read the setting download from file
     */
    private void settingRead(){
        ObjectInputStream ois = null;
        try {
            FileInputStream fin = new FileInputStream("setting.jdm");
            ois = new ObjectInputStream(fin);
            settingFile = (SettingFile) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     *      * this method read the filter download from file
     */
    private void filterRead(){
        ObjectInputStream ois = null;
        try {
            FileInputStream fin = new FileInputStream("filter.jdm");
            ois = new ObjectInputStream(fin);
            filter = (Filter) ois.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * this method make a menu bar and add this menu to the main window
     */
    private void JMenuBar() {
        jMenuItemNewDownload.addActionListener(action);
        menu1.add(jMenuItemNewDownload);
        jMenuItemPause.addActionListener(action);
        menu1.add(jMenuItemPause);
        jMenuItemResume.addActionListener(action);
        menu1.add(jMenuItemResume);
        jMenuItemCancel.addActionListener(action);
        menu1.add(jMenuItemCancel);
        jMenuItemRemove.addActionListener(action);
        menu1.add(jMenuItemRemove);
        jMenuItemSetting.addActionListener(action);
        menu1.add(jMenuItemSetting);
        jMenuItemExit.addActionListener(action);
        menu1.add(jMenuItemExit);

        jMenuItemAbout.addActionListener(action);
        menu2.add(jMenuItemAbout);

        jMenuBar.add(menu1);
        jMenuBar.add(menu2);

        frame.setJMenuBar(jMenuBar);
    }

    /**
     * this method make a tool bar and add this menu to the main window
     */
    private void JToolBar() {
        JPanel jToolBar = new JPanel(new GridLayout(1, 50));
        jToolBar.setBorder(new EmptyBorder(3, 3, 3, 320));
        srch = new JButton();
        srch.addActionListener(action);
        srch.setBackground(Color.lightGray);
        buttonNewDownload.addActionListener(action);
        buttonNewDownload.setBackground(Color.lightGray);
        buttonPause.setBackground(Color.lightGray);
        buttonPause.addActionListener(action);
        buttonResume.setBackground(Color.lightGray);
        buttonResume.addActionListener(action);
        buttonCancel.setBackground(Color.lightGray);
        buttonCancel.addActionListener(action);
        buttonRemove.setBackground(Color.lightGray);
        buttonRemove.addActionListener(action);
        buttonSetting.setBackground(Color.lightGray);
        buttonSetting.addActionListener(action);
        buttonSort.setBackground(Color.lightGray);
        buttonSort.addActionListener(action);
        try {
            Image image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\add.png"));
            buttonNewDownload.setIcon(new ImageIcon(image));
            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\pause.png"));
            buttonPause.setIcon(new ImageIcon(image));
            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\play.png"));
            buttonResume.setIcon(new ImageIcon(image));
            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\cancel.png"));
            buttonCancel.setIcon(new ImageIcon(image));
            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\remove.png"));
            buttonRemove.setIcon(new ImageIcon(image));
            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\setting.png"));
            buttonSetting.setIcon(new ImageIcon(image));
            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\sort.png"));
            buttonSort.setIcon(new ImageIcon(image));
            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\search.png"));
            srch.setIcon(new ImageIcon(image));

        } catch (Exception e) {
            System.out.println(e);
        }
        jToolBar.add(buttonNewDownload);
        jToolBar.add(buttonPause);
        jToolBar.add(buttonCancel);
        jToolBar.add(buttonResume);
        jToolBar.add(buttonRemove);
        jToolBar.add(buttonSetting);
        jToolBar.add(buttonSort);
        jToolBar.add(srch);

        jToolBar.setBackground(Color.lightGray);
        centerPanel.add(jToolBar, BorderLayout.NORTH);
    }

    /**
     * this method make the left bar of download manager and add this to the main window
     */
    private void LeftBar() {

        buttonProcess.addActionListener(action);
        buttonComplete.addActionListener(action);
        buttonQueues.addActionListener(action);
        recycle_bin.addActionListener(action);

        try {
            Image image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\logo.png"));
            JLabel jLabel = new JLabel();
            jLabel.setIcon(new ImageIcon(image));
            leftPanel.add(jLabel, 0, 0);
        } catch (Exception e) {
            System.out.println(e);
        }
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        buttonProcess.setForeground(Color.lightGray);
        buttonComplete.setForeground(Color.lightGray);
        buttonQueues.setForeground(Color.lightGray);
        recycle_bin.setForeground(Color.lightGray);

        try {
            Image image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\process.png"));
            buttonProcess.setIcon(new ImageIcon(image));

            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\complete.png"));
            buttonComplete.setIcon(new ImageIcon(image));

            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\queue.png"));
            buttonQueues.setIcon(new ImageIcon(image));

            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\recycleBin.png"));
            recycle_bin.setIcon(new ImageIcon(image));

        }
        catch (Exception e) {
            System.out.println(e);
        }

        buttonPanel.setBackground(Color.darkGray);
        buttonProcess.setBackground(Color.darkGray);
        buttonComplete.setBackground(Color.darkGray);
        buttonQueues.setBackground(Color.darkGray);
        recycle_bin.setBackground(Color.darkGray);

        buttonPanel.add(buttonProcess);
        buttonPanel.add(buttonComplete);
        buttonPanel.add(buttonQueues);
        buttonPanel.add(recycle_bin);

        leftPanel.add(buttonPanel);
    }

    /**
     * this method build an object from Downloading class
     * and add this object to the array list that exist in this class
     * @param downloadName is the name of the file that downloading
     * @param size is the size of the file
     * @param url is the web address of the file
     * @param savingAddress is the saving address that file save there
     */
    private void DownloadBar(String downloadName,float size,String url,String savingAddress) {
        Downloading download = new Downloading(downloadName,size,url, savingAddress);
        downloads.add(download);
        downloadingList.add(download);
        if (counter < settingFile.downloadLimit) {
            download.startDownload(downloadName, url, savingAddress);
            counter++;
            updateProcessingFrame();
        }
    }

    /**
     * this method check limit of download
     */
    public void downloadCheck(){
        for (Downloading downloading:downloads) {
            if (downloading.download.downloadFinished)
                downloading.info.isFinished = true;
        }
    }

    /**
     * this method check the url that not filtered
     * @param url
     * @return
     */
    private boolean check(String url){
        for (String str:filter.filters) {
            if (url.toUpperCase().matches(str.toUpperCase() + ".*")) {
                return false;
            }
        }
        return true;
    }
    /**
     * this method build an object from Downloading class
     * and add this object to the Queue array list  that exist in this class
     * @param downloadName is the name of the file that downloading
     * @param size is the size of the file
     * @param url is the web address of the file
     * @param savingAddress is the saving address that file save there
     */
    private void Queue(String downloadName,float size,String url,String savingAddress){
        Downloading download = new Downloading(downloadName,size,url, savingAddress);
        download.startDownload(downloadName, url, savingAddress);
        download.pause();
        Queue.add(download);
        queueDownloadList.add(download);
        updateQueueFrame();
        QueueDownload();
    }

    /**
     * this method iterate on the Queue array list and download one by one
     */
    public static void QueueDownload(){
        if (!Queue.isEmpty() && i < Queue.size()){
            if (!Queue.get(i).download.downloadFinished)
                Queue.get(i).resume();
            else{
                Queue.get(i).info.isFinished = true;
                Queue.get(i).download.downloadFinished = true;
                i++;
            }
        }
    }

    /**
     * this method show the file that are downloading and don't finished
     */
    private void updateProcessingFrame(){
        downloadCheck();
        ArrayList<Downloading> empty = new ArrayList<>();
        downloadPanel.removeAll();
        completePanel.removeAll();
        QueuePanel.removeAll();
        searchResultPanel.removeAll();

        for (Downloading downloading:Queue) {
            if (downloading.info.isFinished){
                empty.add(downloading);
                completed.add(downloading);
                //completePanel.add(downloading.getDownloadProcess());
            }
            else
                QueuePanel.add(downloading.getDownloadProcess());
        }
        for (Downloading downloading:empty) {
            Queue.remove(downloading);
        }
        empty = new ArrayList<>();

        for (Downloading downloading:downloads) {
            if (!downloading.info.isFinished)
                downloadPanel.add(downloading.getDownloadProcess());
            else {
                empty.add(downloading);
                completed.add(downloading);
                //completePanel.add(downloading.getDownloadProcess());
            }
        }
        for (Downloading downloading:empty) {
            downloads.remove(downloading);
        }

        centerPanel.remove(searchResultPanel);
        centerPanel.remove(completePanel);
        centerPanel.remove(QueuePanel);
        //JToolBar();
        centerPanel.add(downloadPanel,BorderLayout.CENTER);
        frame.add(centerPanel,BorderLayout.CENTER);
        frame.add(leftPanel, BorderLayout.WEST);
        frame.repaint();
        frame.setVisible(true);
    }

    /**
     * this method show the file that are downloaded
     */
    private void updateCompletedFrame(){
        downloadCheck();
        ArrayList<Downloading> empty = new ArrayList<>();
        completePanel.removeAll();
        downloadPanel.removeAll();
        QueuePanel.removeAll();
        searchResultPanel.removeAll();

        for (Downloading downloading:Queue) {
            if (downloading.info.isFinished){
                empty.add(downloading);
                completed.add(downloading);
                //completePanel.add(downloading.getDownloadProcess());
            }
            else
                QueuePanel.add(downloading.getDownloadProcess());
        }
        for (Downloading downloading:empty) {
            Queue.remove(downloading);
        }

        empty = new ArrayList<>();

        for (Downloading downloading:downloads) {
            if (!downloading.info.isFinished)
                downloadPanel.add(downloading.getDownloadProcess());
            else {
                empty.add(downloading);
                downloading.progressBar.setValue(100);
                completed.add(downloading);
                //completePanel.add(downloading.getDownloadProcess());
            }
        }

        for (Downloading downloading:empty) {
            downloads.remove(downloading);
        }

        for (Downloading downloading:completed) {
            downloading.progressBar.setValue(100);
            completePanel.add(downloading.getDownloadProcess());
        }
        centerPanel.remove(downloadPanel);
        centerPanel.remove(QueuePanel);
        centerPanel.remove(searchResultPanel);

        centerPanel.add(completePanel,BorderLayout.CENTER);
        frame.add(centerPanel,BorderLayout.CENTER);
        frame.add(leftPanel, BorderLayout.WEST);
        frame.repaint();
        frame.setVisible(true);
    }

    /**
     * this method shod the downloads that are in the queue
     */
    private void updateQueueFrame(){
        ArrayList<Downloading> empty = new ArrayList<>();
        downloadPanel.removeAll();
        completePanel.removeAll();
        QueuePanel.removeAll();
        searchResultPanel.removeAll();

        for (Downloading downloading:Queue) {
            if (downloading.info.isFinished){
                empty.add(downloading);
                completed.add(downloading);
                //completePanel.add(downloading.getDownloadProcess());
            }
            else
                QueuePanel.add(downloading.getDownloadProcess());
        }

        for (Downloading downloading:empty) {
            Queue.remove(downloading);
        }
        empty = new ArrayList<>();
        for (Downloading downloading:downloads) {
            if (!downloading.info.isFinished)
                downloadPanel.add(downloading.getDownloadProcess());
            else {
                empty.add(downloading);
                completed.add(downloading);
                //completePanel.add(downloading.getDownloadProcess());
            }
        }

        for (Downloading downloading:empty) {
            downloads.remove(downloading);
        }

        centerPanel.remove(completePanel);
        centerPanel.remove(downloadPanel);
        centerPanel.remove(searchResultPanel);
        centerPanel.add(QueuePanel,BorderLayout.CENTER);
        frame.add(centerPanel,BorderLayout.CENTER);
        frame.add(leftPanel, BorderLayout.WEST);
        frame.repaint();
        frame.setVisible(true);
    }

    /**
     * this method open a new frame and build a new download
     * in this frame you can set the name and the url and the saving file address
     */
    private void newDownload(){
        newDownloadFrame = new JFrame();
        newDownloadFrame.setLayout(new GridLayout(6,1));
        newDownloadFrame.setBackground(Color.white);


        radioButtonQueue = new JRadioButton("Queue");
        radioButtonAutomatic = new JRadioButton("automatically");
        radioButtonQueue.addActionListener(action);
        radioButtonAutomatic.addActionListener(action);
        newDownloadOk = new JButton("ok");
        newDownloadCancel = new JButton("cancel");

        JPanel urlPanel = new JPanel(new BorderLayout());
        urlAddress = new JTextField("https://");

        JPanel namePanel = new JPanel(new BorderLayout());
        fileName = new JTextField();

        JPanel savingPanel = new JPanel(new BorderLayout());
        savingAddress = new JTextField(settingFile.savingAddress);

        JPanel buttonPanel_1 = new JPanel();
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonQueue);
        buttonGroup.add(radioButtonAutomatic);
        buttonPanel_1.add(radioButtonAutomatic);
        buttonPanel_1.add(radioButtonQueue);

        JPanel buttonPanel_2 = new JPanel();
        newDownloadOk.addActionListener(action);
        newDownloadOk.setBackground(Color.GREEN);
        newDownloadCancel.addActionListener(action);
        newDownloadCancel.setBackground(Color.RED);
        buttonPanel_2.add(newDownloadOk);
        buttonPanel_2.add(newDownloadCancel);

        Image image = null;
        try {
            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\addLink.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel jLabel = new JLabel(" ADDRESS: ");
        jLabel.setIcon(new ImageIcon(image));
        urlPanel.add(jLabel,BorderLayout.WEST);

        try {
            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\file.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel labelName = new JLabel(" name  ");
        labelName.setIcon(new ImageIcon(image));
        namePanel.add(labelName,BorderLayout.WEST);

        try {
            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\saveAddress.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JLabel savinglabel = new JLabel(" saving address: ");
        savinglabel.setIcon(new ImageIcon(image));
        savingPanel.add(savinglabel,BorderLayout.WEST);

        urlPanel.setBackground(Color.white);
        urlPanel.add(urlAddress);
        namePanel.setBackground(Color.white);
        namePanel.add(fileName);
        savingPanel.setBackground(Color.white);
        savingPanel.add(savingAddress);

        urlPanel.setBorder(new EmptyBorder(5,2,5,2));
        namePanel.setBorder(new EmptyBorder(5,2,5,2));
        savingPanel.setBorder(new EmptyBorder(5,2,5,2));


        newDownloadFrame.add(namePanel);
        newDownloadFrame.add(urlPanel);
        newDownloadFrame.add(savingPanel);
        newDownloadFrame.add(buttonPanel_1);
        newDownloadFrame.add(buttonPanel_2);
        newDownloadFrame.setSize(600, 300);
        newDownloadFrame.setResizable(true);
        newDownloadFrame.setTitle("New Download");
        newDownloadFrame.setLocation(50,50);
        newDownloadFrame.setVisible(true);


    }

    /**
     * pause a downloading
     */
    private void pause(){
        for (Downloading downloading:downloads) {
            if (downloading.selected)
                downloading.pause();
        }
    }

    /**
     * resume a downloading
     */
    private void resume(){
        for (Downloading downloading:downloads) {
            if (downloading.selected)
                downloading.resume();
        }
    }

    /**
     * remove a downloading
     */
    private void remove(){
        ArrayList<Downloading> cancelList = new ArrayList<>();
        for (Downloading downloading: downloads){
            if (downloading.selected) {
                cancelList.add(downloading);
            }
        }
        for (Downloading downloading:cancelList) {
            System.out.println(downloading.info.name + downloading.info.size + downloading.info.urlAddress + downloading.info.savingAddress);
            downloadingList.remove(downloads.indexOf(downloading));
            removedDownload.add(new Info(downloading.info.name,downloading.info.urlAddress));
            downloads.get(downloads.indexOf(downloading)).pause();
            downloads.remove(downloads.indexOf(downloading));
           // downloading.pause();
        }
        cancelList.clear();
        for (Downloading downloading: Queue){
            if (downloading.selected)
                cancelList.add(downloading);
        }
        for (Downloading downloading:cancelList) {
            queueDownloadList.remove(Queue.indexOf(downloading));
            removedDownload.add(new Info(downloading.info.name,downloading.info.urlAddress));
            Queue.get(Queue.indexOf(downloading)).pause();
            Queue.remove(Queue.indexOf(downloading));
            //downloading.pause();
        }
        cancelList.clear();
        for (Downloading downloading: completed){
            if (downloading.selected)
                cancelList.add(downloading);
        }
        for (Downloading downloading:cancelList) {
            //queueDownloadList.remove(Queue.indexOf(downloading));
            removedDownload.add(new Info(downloading.info.name,downloading.info.urlAddress));
            completed.remove(downloading);
            //downloading.pause();
        }
        updateQueueFrame();
        updateCompletedFrame();
        updateProcessingFrame();
        //System.out.println(removedDownload.infoes.size());
    }

    /**
     * cancel a downloading
     */
    private void cancel(){
        for (Downloading downloading:downloads) {
            if (downloading.selected)
                downloading.cancel();
        }
    }

    /**
     * change the look and feel of program
     * @param Lframe
     * @param lookAndFeelMood
     */
    private void lookAndFeel(JFrame Lframe,int lookAndFeelMood){
        if (lookAndFeelMood == 1){
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                SwingUtilities.updateComponentTreeUI(Lframe);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
        }
        else if (lookAndFeelMood == 2){
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                SwingUtilities.updateComponentTreeUI(Lframe);
            } catch (ClassNotFoundException ee) {
                ee.printStackTrace();
            } catch (InstantiationException ee) {
                ee.printStackTrace();
            } catch (IllegalAccessException ee) {
                ee.printStackTrace();
            } catch (UnsupportedLookAndFeelException ee) {
                ee.printStackTrace();
            }
        }
        else if(lookAndFeelMood == 3){
            try {
                UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
                SwingUtilities.updateComponentTreeUI(Lframe);
            } catch (ClassNotFoundException ee) {
                ee.printStackTrace();
            } catch (InstantiationException ee) {
                ee.printStackTrace();
            } catch (IllegalAccessException ee) {
                ee.printStackTrace();
            } catch (UnsupportedLookAndFeelException ee) {
                ee.printStackTrace();
            }}
    }

    /**
     * this method open a new frame and you can change the Look And Fell of your windows
     * adn set the download limit field and set the saving address
     */
    private void setting(){
        settingFrame = new JFrame();
        JTextField adrs = new JTextField(" saving address: ");
        adrs.setEditable(false);
        setingButtonAct = new JButton("set");

        JPanel filterPanel = new JPanel(new BorderLayout());
        JTextField filtertxt = new JTextField("filtering address: ");
        filtertxt.setEditable(false);
        filterInput = new JTextField();

        filterPanel.add(filtertxt,BorderLayout.WEST);
        filterPanel.add(filterInput,BorderLayout.CENTER);

        LAF_1 = new JButton();
        LAF_2 = new JButton();
        LAF_3 = new JButton();
        LAF_1.addActionListener(action);
        LAF_2.addActionListener(action);
        LAF_3.addActionListener(action);

        Image image = null;
        try {
            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\nimbus.png"));
            LAF_1.setIcon(new ImageIcon(image));
            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\metal.png"));
            LAF_2.setIcon(new ImageIcon(image));
            image = ImageIO.read(getClass().getResource("\\EagleGetIcons\\windows.png"));
            LAF_3.setIcon(new ImageIcon(image));
        } catch (IOException e) {
            e.printStackTrace();
        }

        setingButtonAct.setBackground(Color.green);
        setingButtonAct.addActionListener(action);
        JPanel num = new JPanel(new BorderLayout());
        JPanel setPanel = new JPanel();
        JPanel buttonPanel = new JPanel(new GridLayout(1,3));
        buttonPanel.setBorder(new EmptyBorder(5,2,5,2));
        buttonPanel.add(LAF_1);
        buttonPanel.add(LAF_2);
        buttonPanel.add(LAF_3);

        num.setBorder(new EmptyBorder(5,1,5,1));
        JPanel address = new JPanel(new BorderLayout());
        address.setBorder(new EmptyBorder(5,1,5,1));
        JPanel northPanel = new JPanel(new GridLayout(3,1));
        settingText = new JTextField(" The number of downloads: ");
        savingAddress = new JTextField(settingFile.savingAddress);
        address.add(savingAddress,BorderLayout.CENTER);
        address.add(adrs,BorderLayout.WEST);
        settingText.setEditable(false);
        spinner = new JSpinner();
        spinner.setValue(settingFile.downloadLimit);
        settingFrame.setLayout(new BorderLayout());
        num.add(settingText,BorderLayout.CENTER);
        num.add(spinner,BorderLayout.EAST);
        northPanel.add(num);
        northPanel.add(address);
        northPanel.add(filterPanel);
        setPanel.add(setingButtonAct);
        settingFrame.add(buttonPanel,BorderLayout.CENTER);
        settingFrame.add(setPanel,BorderLayout.SOUTH);
        settingFrame.add(northPanel,BorderLayout.NORTH);
        settingFrame.setSize(700,350);
        settingFrame.setResizable(true);
        settingFrame.setTitle("Setting");
        settingFrame.setLocation(75,50);
        settingFrame.setBackground(Color.lightGray);
        lookAndFeel(settingFrame,settingFile.LAF);
        settingFrame.setVisible(true);
    }

    /**
     * this method open a new frame that shows the details of producer and the details of processing of this project
     */
    private void about(){
        JFrame about = new JFrame();
        about.setLayout(new GridLayout(4,1));
        JTextField name = new JTextField("producer is Mehr@bs@fdel _ 9631045");
        about.add(name);
        JTextField history = new JTextField("starting date is : 27/02/97  _ finished at 29/02/97");
        about.add(history);
        JTextField details = new JTextField("use this app like using another download manager");
        about.add(details);

        name.setEditable(false);
        history.setEditable(false);
        details.setEditable(false);

        about.setTitle("About");
        about.setSize(300,200);
        about.setResizable(true);
        about.setLocation(75,50);
        about.setBackground(Color.lightGray);
        about.setVisible(true);
    }

    /**
     * build the sort frame
     */
    private void sortFrame(){
        sortFrame = new JFrame();
        sortFrame.setLayout(new BorderLayout());
        JPanel sortPanel = new JPanel();
        JPanel sortPanelButton = new JPanel();
        sortPanel.add(nameSort,BorderLayout.WEST);
        sortPanel.add(sizeSort,BorderLayout.CENTER);
        sortPanel.add(timeSort,BorderLayout.EAST);

        upwardSort.addActionListener(action);
        downwardSort.addActionListener(action);

        upwardSort.setBackground(Color.yellow);
        downwardSort.setBackground(Color.yellow);

        sortPanelButton.add(upwardSort,BorderLayout.LINE_END);
        sortPanelButton.add(downwardSort,BorderLayout.LINE_END);

        sortFrame.add(sortPanel,BorderLayout.NORTH);
        sortFrame.add(sortPanelButton,BorderLayout.CENTER);
        sortFrame.setTitle("Sort");
        sortFrame.setSize(300,200);
        sortFrame.setResizable(true);
        sortFrame.setLocation(150,150);
        sortFrame.setBackground(Color.lightGray);
        sortFrame.setVisible(true);
    }

    /**
     * sort with name
     * @param type
     */
    private void nameSort(int type){
        downloads.sort(new Comparator<Downloading>() {
            @Override
            public int compare(Downloading o1, Downloading o2) {
                if (!o1.info.name.equals(o2.info.name))
                    return type * (o1.info.name.compareTo(o2.info.name));
                else
                    return type * (int) (o1.info.size - (o2.info.size));
            }
        });
        Queue.sort(new Comparator<Downloading>() {
            @Override
            public int compare(Downloading o1, Downloading o2) {
                if (!o1.info.name.equals(o2.info.name))
                    return type * (o1.info.name.compareTo(o2.info.name));
                else
                    return type * (int) (o1.info.size - (o2.info.size));
            }
        });
       // System.out.println(downloads.toString());
        downloadingList.nameSort(type);
        queueDownloadList.nameSort(type);
        //sort
        updateProcessingFrame();
    }

    /**
     * sort with size
     * @param type
     */
    private void sizeSort(int type){
        downloads.sort(new Comparator<Downloading>() {
            @Override
            public int compare(Downloading o1, Downloading o2) {
                if (! (o1.info.size == o2.info.size))
                    return type * (int) (o1.info.size - (o2.info.size));
                else
                    return type * (o1.info.name.compareTo(o2.info.name));
            }
        });
        Queue.sort(new Comparator<Downloading>() {
            @Override
            public int compare(Downloading o1, Downloading o2) {
                if (! (o1.info.size == o2.info.size))
                    return type * (int) (o1.info.size - (o2.info.size));
                else
                    return type * (o1.info.name.compareTo(o2.info.name));
            }
        });
        // System.out.println(downloads.toString());
        downloadingList.sizeSort(type);
        queueDownloadList.sizeSort(type);
        //sort
        updateProcessingFrame();
    }

    /**
     * sort with time
     * @param type
     */
    private void timeSort(int type){
        downloads.sort(new Comparator<Downloading>() {
            @Override
            public int compare(Downloading o1, Downloading o2) {
                if (!o1.info.name.equals(o2.info.name))
                    return type * (o1.info.time - (o2.info.time));
                else
                    return type * (o1.info.name.compareTo(o2.info.name));
            }
        });
        Queue.sort(new Comparator<Downloading>() {
            @Override
            public int compare(Downloading o1, Downloading o2) {
                if (!o1.info.name.equals(o2.info.name))
                    return type * (o1.info.time - (o2.info.time));
                else
                    return type * (o1.info.name.compareTo(o2.info.name));
            }
        });
        // System.out.println(downloads.toString());
        downloadingList.timeSort(type);
        queueDownloadList.timeSort(type);
        //sort
        updateProcessingFrame();
    }

    /**
     * recycle bin of program
     */
    private void recycle_bin(){
        JFrame recycleFram = new JFrame();
        JPanel recyclePanel = new JPanel(new GridLayout(14,1));
        for (int i = 0; i < removedDownload.infoes.size(); i++) {
            JTextField recycleText = new JTextField();
            recycleText.setText("name: " + removedDownload.infoes.get(i).name + " ** url address: " + removedDownload.infoes.get(i).urlAddress);
            recycleText.setEditable(false);
            recyclePanel.add(recycleText);
        }
        recyclePanel.setBackground(Color.white);
        recycleFram.add(recyclePanel);
        recycleFram.setTitle("recycle bin");
        recycleFram.setSize(900,700);
        recycleFram.setResizable(true);
        recycleFram.setLocation(150,150);
        recycleFram.setVisible(true);
        //new frame for show the removed files
    }

    /**
     * save prigram details
     */
    private void exit(){
        FileOutputStream fout = null;
        ArrayList<Info> cancelList = new ArrayList<>();
        for (Info info:downloadingList.infos) {
            if (!info.isFinished)
                cancelList.add(info);
        }
        for (Info info:cancelList){
            downloadingList.infos.remove(info);
        }

        try {
            fout = new FileOutputStream("list.jdm",false );
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(downloadingList);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*
        try {
            fout = new FileOutputStream("complete.jdm",false );
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(completedList);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        cancelList.clear();
        for (Info info:queueDownloadList.infos) {
            if (!info.isFinished)
                cancelList.add(info);
        }
        for (Info info:cancelList){
            queueDownloadList.infos.remove(info);
        }

        try {
            fout = new FileOutputStream("queue.jdm",false );
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(queueDownloadList);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fout = new FileOutputStream("removed.jdm" );
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(removedDownload);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fout = new FileOutputStream("setting.jdm" );
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(settingFile);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fout = new FileOutputStream("filter.jdm",false );
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(filter);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //write queueDownloadList object on file
        //write removedDownload
        //write Downloading List

        System.exit(0);
    }

    /**
     * search frame that you can search with it
     */
    private void searchFrame(){
        searchframe = new JFrame();
        searchText = new JTextField();
        searchButton = new JButton("Search");
        searchButton.addActionListener(action);
        searchButton.setBackground(Color.yellow);

        searchframe.setBackground(Color.white);
        searchframe.add(searchText,BorderLayout.CENTER);
        searchframe.add(searchButton,BorderLayout.EAST);
        searchframe.setTitle("Search by name or address");
        searchframe.setSize(250,70);
        searchframe.setResizable(true);
        searchframe.setLocation(150,150);
        searchframe.setVisible(true);
    }

    /**
     * search on the downli=oading files
     */
    private void search(){
        String searchTextInput = searchText.getText();
        searchResultPanel = new JPanel(new GridLayout(7,1));
        ArrayList<Downloading> searchArray = new ArrayList<>();
        for (Downloading downloading:downloads) {
            if (downloading.info.name.contains(searchTextInput) || downloading.info.urlAddress.contains(searchTextInput))
                searchArray.add(downloading);
        }
        for (Downloading downloading:Queue) {
            if (downloading.info.name.contains(searchTextInput) || downloading.info.urlAddress.contains(searchTextInput))
                searchArray.add(downloading);
        }
        for (Downloading downloading:searchArray) {
            searchResultPanel.add(downloading.getDownloadProcess());
        }

        centerPanel.remove(QueuePanel);
        centerPanel.remove(completePanel);
        centerPanel.remove(downloadPanel);
        centerPanel.add(searchResultPanel,BorderLayout.CENTER);
        frame.add(centerPanel,BorderLayout.CENTER);
        frame.add(leftPanel, BorderLayout.WEST);
        frame.repaint();
        frame.setVisible(true);
    }


    /**
     * this INNER class is for set the Action of each button
     * and do the proper operation when a button clicked
     */
    class Action implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buttonNewDownload || e.getSource() == jMenuItemNewDownload){
                newDownload();
            }
            else if (e.getSource() == buttonPause || e.getSource() == jMenuItemPause)
                pause();
            else if (e.getSource() == buttonResume || e.getSource() == jMenuItemResume)
                resume();
            else if (e.getSource() == buttonCancel || e.getSource() == jMenuItemCancel)
                cancel();
            else if (e.getSource() == buttonRemove || e.getSource() == jMenuItemRemove)
                remove();
            else if (e.getSource() == buttonSetting || e.getSource() == jMenuItemSetting)
                setting();
            else if (e.getSource() == jMenuItemAbout)
                about();
            else if (e.getSource() == jMenuItemExit)
                exit();
            else if (e.getSource() == trayIcon)
                updateProcessingFrame();
            else if (e.getSource() == newDownloadOk){
                if (radioButtonQueue.isSelected() && check(urlAddress.getText())){
                    try {
                        Queue(fileName.getText(),Downloading.getFileSize(new URL(urlAddress.getText())),urlAddress.getText(),savingAddress.getText());
                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    }
                    newDownloadFrame.dispose();
                }
                else if (check(urlAddress.getText())) {
                    try {
                        DownloadBar(fileName.getText(), Downloading.getFileSize(new URL(urlAddress.getText())), urlAddress.getText(), savingAddress.getText());
                    } catch (MalformedURLException e1) {
                        e1.printStackTrace();
                    }
                    //settingFile.savingAddress = savingAddress.getText();
                    newDownloadFrame.dispose();
                }
            }
            else if (e.getSource() == newDownloadCancel)
                newDownloadFrame.dispose();
            else if (e.getSource() == buttonProcess) {
                updateProcessingFrame();
            }
            else if (e.getSource() == buttonComplete)
                updateCompletedFrame();
            else if (e.getSource() == buttonQueues)
                updateQueueFrame();
            else if (e.getSource() == recycle_bin)
                recycle_bin();
            else if (e.getSource() == srch)
                searchFrame();
            else if (e.getSource() == searchButton) {
                searchframe.dispose();
                search();
            }
            else if (e.getSource() == setingButtonAct){
                if ((Integer)spinner.getValue() != 0)
                    settingFile.downloadLimit = (Integer)spinner.getValue();
                settingFile.savingAddress = savingAddress.getText();
                settingFrame.dispose();
                if (!filterInput.getText().isEmpty())
                    filter.add(filterInput.getText());
            }
            else if (e.getSource() == buttonSort)
                sortFrame();

            else if (e.getSource() == upwardSort){
                if (nameSort.isSelected())
                    nameSort(1);
                else if (sizeSort.isSelected())
                    sizeSort(1);
                else if (timeSort.isSelected())
                    timeSort(1);

                sortFrame.dispose();
            }

            else if (e.getSource() == downwardSort){
                if (nameSort.isSelected())
                    nameSort(-1);
                else if (sizeSort.isSelected())
                    sizeSort(-1);
                else if (timeSort.isSelected())
                    timeSort(-1);


                sortFrame.dispose();
            }


            else if (e.getSource() == LAF_1){
                lookAndFeel(frame,1);
                lookAndFeel(settingFrame,1);
                settingFile.LAF = 1;
            }
            else if (e.getSource() == LAF_2){
                lookAndFeel(frame,2);
                lookAndFeel(settingFrame,2);
                settingFile.LAF = 2;
            }
            else if (e.getSource() == LAF_3){
                lookAndFeel(frame,3);
                lookAndFeel(settingFrame,3);
                settingFile.LAF = 3;
            }
        }
    }
}
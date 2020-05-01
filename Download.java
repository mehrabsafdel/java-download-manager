import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Download implements Serializable,Runnable {
    InputStream is = null;
    FileOutputStream fos = null;
    URLConnection urlConn;
    boolean downloadFinished = false;
    private JProgressBar jProgressBar;
    Thread download;
    String Address;
    int totalTemp = 0;
    float Size;
    float speed;
    long downloadedSize;

    /**
     * this constructor start a downloading
     * @param url is the url address
     * @param saveAddress is the saving address
     * @param name is the downloading name
     * @param progressBar is the progress bar of download
     * @param size is the size of download
     * @param isFinished
     * @throws IOException
     */
    public Download(URL url, String saveAddress, String name, JProgressBar progressBar,float size,boolean isFinished) throws IOException {
        Size = size;

        downloadFinished = isFinished;
        System.out.println(size);
        jProgressBar = progressBar;
        download = new Thread() {
            public void run() {
                try {

                    HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
                    int responseCode = httpConn.getResponseCode();

                    // always check HTTP response code first
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        String fileName = "";
                        String disposition = httpConn.getHeaderField("Content-Disposition");
                        String contentType = httpConn.getContentType();
                        int contentLength = httpConn.getContentLength();

                        if (disposition != null) {
                            // extracts file name from header field
                            int index = disposition.indexOf("filename=");
                            if (index > 0) {
                                fileName = disposition.substring(index + 10,
                                        disposition.length() - 1);
                            }
                        } else {
                            // extracts file name from URL

                            String fileURL ="https://s.icons8.com/download/Icons8App_for_Windows/Icons8Setup.exe";
                            fileName = fileURL.substring(fileURL.lastIndexOf("/") + 1,
                                    fileURL.length());
                        }

                        System.out.println("Content-Type = " + contentType);
                        System.out.println("Content-Disposition = " + disposition);
                        System.out.println("Content-Length = " + contentLength);
                        System.out.println("fileName = " + fileName);

                        // opens input stream from the HTTP connection
                        InputStream inputStream = httpConn.getInputStream();
                        String saveDir = saveAddress + "\\";
                        String saveFilePath;
                        if (name.isEmpty()) {
                            Address = saveDir + fileName;
                            saveFilePath = saveDir + fileName;
                        }
                        else {
                            Address = saveDir + fileName;
                            saveFilePath = saveDir + name;
                        }
                        //saveFilePath = "C:\\Users\\mehrab\\Desktop\\mamad.mp3";
                        // opens an output stream to save into file
//                        String saveFilePath = saveDir + File.separator + fileName;
                        OutputStream outputStream = new FileOutputStream(saveFilePath);

                        int bytesRead = -1;
                        byte[] buffer = new byte[4096];
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                            totalTemp += bytesRead;
                            progress((int) (totalTemp / size));
                              //Gui.QueueDownload();
                        }

                        outputStream.close();
                        inputStream.close();

                        System.out.println("File downloaded");
                        Gui.counter--;
                        downloadFinished = true;
                        Gui.QueueDownload();
                        Gui.QueueDownload();
                    } else {
                        System.out.println("No file to download. Server replied HTTP code: " + responseCode);
                    }
                    httpConn.disconnect();

                    } catch (IOException e) {
                        e.printStackTrace();
                }
                }

            };

        download.start();

    }



    /**
     * pause download
     */
    public void pause() {
        //timeOut = false;
        download.suspend();
    }

    /**
     * resume download
     */
    public void resume() {
        //timeOut = true;
        download.resume();
    }


    /**
     * set the progres bar value
     * @param percent
     */
    public void progress(int percent) {
        downloadedSize = percent;
        jProgressBar.setValue( percent/ 10400);
    }

    @Override
    public void run() {

    }

    /**
     * cancel downloading
     */
    public void cancel() {
        download.suspend();
    }
}


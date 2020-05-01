import java.io.Serializable;

/**
 * this class is for savinf setting details
 */
public class SettingFile implements Serializable{
    int downloadLimit;
    String savingAddress;
    int LAF;

    /**
     * @param downloadLimit is the liit of downloading
     * @param savingAddress is the saving address
     * @param LAF is the type of look and feel
     */
    public SettingFile(int downloadLimit,String savingAddress,int LAF){
        this.downloadLimit = downloadLimit;
        this.savingAddress = savingAddress;
        this.LAF = LAF;
    }

    /**
     * another constructor
     */
    public SettingFile(){
        downloadLimit =10000000;
        savingAddress = "C:\\Users\\mehrab\\Desktop";
        LAF = 1;
    }

}

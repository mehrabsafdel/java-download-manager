import java.io.Serializable;
import java.util.ArrayList;

/**
 * this class is for saving the removed download
 */
public class removedDownload implements Serializable{
    ArrayList<Info> infoes;

    /**
     * make an object from array list
     */
    public removedDownload(){
        infoes = new ArrayList<>();
    }

    /**
     * add a removed downloading
     * @param info is the info of removed
     */
    public void add(Info info){
        infoes.add(info);
    }

}

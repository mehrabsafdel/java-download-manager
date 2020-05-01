import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * this class is for saving the details of a downloading
 */
public class DownloadingList implements Serializable{
    ArrayList<Info> infos;

    /**
     * constructor
     */
    public DownloadingList(){
        infos = new ArrayList<>();
    }

    /**
     * add a new info of downloading
     * @param downloading is the downloading that we save its details
     */
    public void add(Downloading downloading){
        infos.add(downloading.info);
    }

    /**
     * remove by index from array list
     * @param index
     */
    public void remove(int index){
        infos.remove(index);
    }

    /**
     * sort the array list by name
     * @param type
     */

    public void nameSort(int type) {
        infos.sort(new Comparator<Info>() {
            @Override
            public int compare(Info o1, Info o2) {
                if (!o1.name.equals(o2.name))
                    return type * (o1.name.compareTo(o2.name));
                else
                    return type * (int) (o1.size - (o2.size));
            }
        });
        // System.out.println(downloads.toString());

    }

    /**
     * sort the array list by size
     * @param type
     */
    public void sizeSort(int type) {
        infos.sort(new Comparator<Info>() {
            @Override
            public int compare(Info o1, Info o2) {
                if (!o1.name.equals(o2.name))
                    return type * (int) (o1.size - (o2.size));
                else
                    return type * (o1.name.compareTo(o2.name));
            }
        });
        // System.out.println(downloads.toString());

    }

    /**
     * sort the array list by time
     * @param type
     */
    public void timeSort(int type) {
        infos.sort(new Comparator<Info>() {
            @Override
            public int compare(Info o1, Info o2) {
                if (!o1.name.equals(o2.name))
                    return type * (o1.time - (o2.time));
                else
                    return type * (o1.name.compareTo(o2.name));
            }
        });
        // System.out.println(downloads.toString());

    }
}

import java.io.Serializable;
import java.util.ArrayList;

/**
 * this class is for saving filtering sites
 */
public class Filter implements Serializable{
    ArrayList<String> filters;

    /**
     * constructor
     */
    public Filter(){
        filters = new ArrayList<>();
    }

    /**
     * add a new filtering
     * @param filter is the new filter that adding
     */
    public void add(String filter){
        filters.add(filter);
    }
}

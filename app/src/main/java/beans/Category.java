package beans;

/**
 * Created by Sir Edwin on 8/29/2015.
 */
public class Category {

    public String category_id;
    public String name;
    public String type;

    public Category(String category_id, String name, String type) {
        this.category_id = category_id;
        this.name = name;
        this.type = type;
    }

}
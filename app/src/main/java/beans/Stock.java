package beans;

/**
 * Created by Sir Edwin on 8/29/2015.
 */
public class Stock {

    public String id;
    public String name;
    public String quantity;
    public String metric;
    public String category;
    public String buying_price;



    public Stock(String id, String name, String quantity, String metric,String category, String buying_price) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.metric = metric;
        this.category = category;
        this.buying_price = buying_price;
    }

}
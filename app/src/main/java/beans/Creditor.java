package beans;

/**
 * Created by Sir Edwin on 8/29/2015.
 */
public class Creditor {

    public String id;
    public String name;
    public String amount;
    public String business_id;


    public Creditor(String id, String name, String amount, String business_id) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.business_id = business_id;
    }

}
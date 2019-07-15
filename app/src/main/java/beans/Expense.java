package beans;

/**
 * Created by Sir Edwin on 8/29/2015.
 */
public class Expense {

    public String id;
    public String name;
    public String expense_type;
    public String frequency;
    public String business_id;


    public Expense(String id, String name, String expense_type, String frequency, String business_id) {
        this.id = id;
        this.name = name;
        this.expense_type = expense_type;
        this.frequency = frequency;
        this.business_id = business_id;
    }

}
package beans;

/**
 * Created by Sir Edwin on 8/29/2015.
 */
public class ExpenseRecords {

    public String expense_type_id,name,total_amount;


    public ExpenseRecords(String expense_type_id,String name, String total_amount) {
        this.expense_type_id = expense_type_id;
        this.name = name;
        this.total_amount = total_amount;

    }

}
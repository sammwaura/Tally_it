package beans;

/**
 * Created by Sir Edwin on 8/29/2015.
 */
public class DetailsExpenses {

    public String expense_records_id,expense_type_id,expenses_type,amount,employee_name,stock_id,date;


    public DetailsExpenses(String expense_records_id, String expense_type_id, String expenses_type, String amount, String employee_name, String stock_id,String date) {
        this.stock_id = stock_id;
        this.expense_records_id = expense_records_id;
        this.expense_type_id = expense_type_id;
        this.amount = amount;
        this.employee_name = employee_name;
        this.expenses_type = expenses_type;
        this.date = date;
    }

}
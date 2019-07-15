package beans;

/**
 * Created by Sir Edwin on 8/29/2015.
 */
public class DetailsSales {

    public String sale_id,stock_id,stock_name,amount,employee_name,sale_type,date,quantity,metric,stock_date;


    public DetailsSales(String sale_id,String stock_id, String stock_name, String amount, String employee_name, String sale_type, String date,String quantity, String metric,String stock_date) {
        this.stock_id = stock_id;
        this.sale_id = sale_id;
        this.stock_name = stock_name;
        this.amount = amount;
        this.employee_name = employee_name;
        this.sale_type = sale_type;
        this.date = date;
        this.quantity = quantity;
        this.metric = metric;
        this.stock_date = stock_date;

    }

}
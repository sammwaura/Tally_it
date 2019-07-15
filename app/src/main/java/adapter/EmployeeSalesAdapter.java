package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import beans.Employee;
import beans.EmployeeSales;
import iobserver.SalesIObserver;
import ws.wolfsoft.creative.R;

public class EmployeeSalesAdapter extends RecyclerView.Adapter<EmployeeSalesAdapter.EmployeeSalesViewHolder> {

//    public SalesIObserver mObserver;

    List <beans.EmployeeSales> employeeSales;


    public static class EmployeeSalesViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView name,amount;

        EmployeeSalesViewHolder(View itemView){
            super(itemView);
            cv = itemView.findViewById(R.id.card);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.total_amount);
        }

    }

//    public void setListener(SalesIObserver obs) { mObserver = obs; }

    List<EmployeeSales> post;

    public EmployeeSalesAdapter(List<EmployeeSales> post){
        this.post = post;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @NonNull
    @Override
    public EmployeeSalesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employee_sales_cardview, viewGroup, false);
        EmployeeSalesViewHolder pvh = new EmployeeSalesViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeSalesViewHolder employeeSalesViewHolder, int i) {
        int val = i+1;
        String s = String.valueOf(val);
        employeeSalesViewHolder.name.setText(String.format("%s. %s", s, post.get(i).name));

        employeeSalesViewHolder.amount.setText(String.format("Total Amount: Ksh %s",s, post.get(i).id));


        final int posi=i;

    }

    @Override
    public int getItemCount() {
        return post.size();
    }
}

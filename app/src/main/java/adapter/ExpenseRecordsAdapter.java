package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import beans.ExpenseRecords;
import beans.Sales;
import iobserver.ExpenseRecordsIObserver;
import iobserver.SalesIObserver;
import ws.wolfsoft.creative.R;

//import com.andexert.library.RippleView;
//import com.github.marlonlom.utilities.timeago.TimeAgo;


public class ExpenseRecordsAdapter extends RecyclerView.Adapter<ExpenseRecordsAdapter.ExpenseRecordsViewHolder> {

    public ExpenseRecordsIObserver mObserver;

    private String name_tapped,amount_tapped;




    public static class ExpenseRecordsViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name,amount,total,balance;

        ExpenseRecordsViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.total_amount);
            total = itemView.findViewById(R.id.total);
            balance = itemView.findViewById(R.id.balance);
        }

    }

    public void setListener(ExpenseRecordsIObserver obs) { mObserver = obs; }

    List<ExpenseRecords> post;

    public ExpenseRecordsAdapter(List<ExpenseRecords> post){
        this.post = post;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public ExpenseRecordsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expense_records_cardview, viewGroup, false);
        ExpenseRecordsViewHolder pvh = new ExpenseRecordsViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ExpenseRecordsViewHolder expenseRecordsViewHolder, int i) {

        int val = i+1;
        String s = String.valueOf(val);

        expenseRecordsViewHolder.name.setText(String.format("%s. %s", s, post.get(i).name.split("#")[0]));
        expenseRecordsViewHolder.total.setText(String.format("Total Amount: Ksh %s", post.get(i).name.split("#")[1]));
        expenseRecordsViewHolder.balance.setText(String.format("Balance: Ksh %s",  post.get(i).name.split("#")[2]));

        expenseRecordsViewHolder.amount.setText(String.format("Amount Paid: Ksh %s", post.get(i).total_amount));



        name_tapped = post.get(i).name.split("#")[0];
        amount_tapped = post.get(i).total_amount;

        final int posi=i;


        expenseRecordsViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            mObserver.onCardClicked(posi, name_tapped);
            }
        });

    }

    @Override
    public int getItemCount() {
        return post.size();
    }
}

package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import beans.DetailsExpenses;
import iobserver.CardIObserver;
import ws.wolfsoft.creative.R;

//import com.andexert.library.RippleView;
//import com.github.marlonlom.utilities.timeago.TimeAgo;


public class DetailsExpenseAdapter extends RecyclerView.Adapter<DetailsExpenseAdapter.DetailsExpenseViewHolder> {

    public CardIObserver mObserver;

    private String name_tapped;
    private String phone_tapped;
    private String password_tapped;
    private String id_tapped;



    public static class DetailsExpenseViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView expense_records_id,expenses_type,amount,employee_name,date;

        DetailsExpenseViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card);
            expense_records_id = itemView.findViewById(R.id.sale_id);
            amount = itemView.findViewById(R.id.amount);
            employee_name = itemView.findViewById(R.id.employee_name);
            expenses_type = itemView.findViewById(R.id.sale_type);
            date = itemView.findViewById(R.id.date);
        }

    }
    

    List<DetailsExpenses> post;

    public DetailsExpenseAdapter(List<DetailsExpenses> post){
        this.post = post;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public DetailsExpenseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.details_expenses_cardview, viewGroup, false);
        DetailsExpenseViewHolder pvh = new DetailsExpenseViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailsExpenseViewHolder detailsExpenseViewHolder, int i) {
        detailsExpenseViewHolder.amount.setText(String.format("Expense At: Ksh.%s", post.get(i).amount));
        detailsExpenseViewHolder.date.setText(String.format("Time: %s", post.get(i).date));
        detailsExpenseViewHolder.employee_name.setText(post.get(i).employee_name);
        detailsExpenseViewHolder.expenses_type.setText(post.get(i).expenses_type);
        detailsExpenseViewHolder.expense_records_id.setText(post.get(i).expense_records_id);
    }



    @Override
    public int getItemCount() {
        return post.size();
    }
}

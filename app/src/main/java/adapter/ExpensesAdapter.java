package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import beans.Expense;
import iobserver.ExpensesIObserver;
import ws.wolfsoft.creative.R;

//import com.andexert.library.RippleView;
//import com.github.marlonlom.utilities.timeago.TimeAgo;


public class ExpensesAdapter extends RecyclerView.Adapter<ExpensesAdapter.ExpensesViewHolder> {

    public ExpensesIObserver mObserver;

    private String name_tapped;
    private String frequency_tapped;
    private String expense_type_tapped;
    private String id_tapped;



    public static class ExpensesViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name;
        TextView frequency;
        TextView expense_type;
        TextView no;

        ExpensesViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card);

            name = itemView.findViewById(R.id.name);
            frequency = itemView.findViewById(R.id.frequency);
            expense_type = itemView.findViewById(R.id.expenses_type);
            no = itemView.findViewById(R.id.no);


        }

    }

    public void setListener(ExpensesIObserver obs) { mObserver = obs; }

    List<Expense> post;

    public ExpensesAdapter(List<Expense> post){
        this.post = post;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public ExpensesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.expenses_cardview, viewGroup, false);
        ExpensesViewHolder pvh = new ExpensesViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ExpensesViewHolder expensesViewHolder, int i) {

        expensesViewHolder.name.setText(post.get(i).name);
        expensesViewHolder.frequency.setText(post.get(i).frequency);
        expensesViewHolder.expense_type.setText(post.get(i).expense_type);
        int val = i+1;
        String s = String.valueOf(val);
        id_tapped = post.get(i).id;
        name_tapped = post.get(i).name;
        frequency_tapped = post.get(i).frequency;
        expense_type_tapped = post.get(i).expense_type;

        final int posi=i;


        expensesViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            mObserver.onCardClicked(posi, id_tapped, name_tapped,expense_type_tapped, frequency_tapped);
            }
        });



    }



    @Override
    public int getItemCount() {
        return post.size();
    }
}

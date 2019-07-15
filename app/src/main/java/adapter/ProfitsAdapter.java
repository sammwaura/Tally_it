package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import beans.Profit;
import beans.Sales;
import iobserver.ProfitIObserver;
import ws.wolfsoft.creative.R;

//import com.andexert.library.RippleView;
//import com.github.marlonlom.utilities.timeago.TimeAgo;


public class ProfitsAdapter extends RecyclerView.Adapter<ProfitsAdapter.ProfitsViewHolder> {

    public ProfitIObserver mObserver;

    private String name_tapped,amount_tapped;




    public static class ProfitsViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name,amount;

        ProfitsViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.total_amount);
        }

    }

    public void setListener(ProfitIObserver obs) { mObserver = obs; }

    List<Profit> post;

    public ProfitsAdapter(List<Profit> post){
        this.post = post;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public ProfitsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profits_cardview, viewGroup, false);
        ProfitsViewHolder pvh = new ProfitsViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProfitsViewHolder profitsViewHolder, int i) {

        int val = i+1;
        String s = String.valueOf(val);
        profitsViewHolder.name.setText(String.format("%s. %s", s, post.get(i).name));
        profitsViewHolder.amount.setText(String.format("Total Amount: Ksh %s", post.get(i).total_amount));

        name_tapped = post.get(i).name;
        amount_tapped = post.get(i).total_amount;

        final int posi=i;


        profitsViewHolder.cv.setOnClickListener(new View.OnClickListener() {
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

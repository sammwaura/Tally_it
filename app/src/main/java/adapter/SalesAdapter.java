package adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import beans.Category;
import beans.Sales;
import iobserver.CategoryIObserver;
import iobserver.SalesIObserver;
import ws.wolfsoft.creative.R;

//import com.andexert.library.RippleView;
//import com.github.marlonlom.utilities.timeago.TimeAgo;


public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.SalesViewHolder> {

    public SalesIObserver mObserver;

    private String name_tapped,amount_tapped;




    public static class SalesViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name,amount;

        SalesViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.total_amount);
        }

    }

    public void setListener(SalesIObserver obs) { mObserver = obs; }

    List<Sales> post;

    public SalesAdapter(List<Sales> post){
        this.post = post;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public SalesViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sales_cardview, viewGroup, false);
        SalesViewHolder pvh = new SalesViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final SalesViewHolder salesViewHolder, int i) {

        int val = i+1;
        String s = String.valueOf(val);
        salesViewHolder.name.setText(String.format("%s. %s", s, post.get(i).name));

        salesViewHolder.amount.setText(String.format("Total Amount: Ksh %s", post.get(i).total_amount));



        name_tapped = post.get(i).name;
        amount_tapped = post.get(i).total_amount;

        final int posi=i;


        salesViewHolder.cv.setOnClickListener(new View.OnClickListener() {
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
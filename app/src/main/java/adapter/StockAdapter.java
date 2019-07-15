package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import beans.Stock;
import ws.wolfsoft.creative.R;
import iobserver.StockIObserver;

//import com.andexert.library.RippleView;
//import com.github.marlonlom.utilities.timeago.TimeAgo;


public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> {

    public StockIObserver mObserver;
    private String name_tapped;

    public static class StockViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name;
        TextView no;
        TextView quantity;
        TextView category;

        StockViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card);
            name = itemView.findViewById(R.id.name);
            quantity = itemView.findViewById(R.id.quantity);
            category = itemView.findViewById(R.id.category);
            no = itemView.findViewById(R.id.no);
        }

    }

    public void setListener(StockIObserver obs) { mObserver = obs; }

    List<Stock> stock;

    public StockAdapter(List<Stock> post){
        this.stock = post;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_cardview, viewGroup, false);
        StockViewHolder pvh = new StockViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final StockViewHolder stockViewHolder, int i) {

        stockViewHolder.name.setText(stock.get(i).name);
        stockViewHolder.quantity.setText(String.format("%s %s", stock.get(i).quantity, stock.get(i).metric));
        stockViewHolder.category.setText(stock.get(i).category);

        int val = i+1;
        String s = String.valueOf(val);
        stockViewHolder.no.setText(s);




        name_tapped = stock.get(i).name;


        final int posi=i;


        stockViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mObserver.onCardClicked(posi,  name_tapped);
            }
        });

    }

    @Override
    public int getItemCount() {
        return stock.size();
    }
}
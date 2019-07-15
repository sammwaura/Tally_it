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

import java.net.CookieHandler;
import java.util.ArrayList;
import java.util.List;

import beans.Category;
import beans.Stock;
import ws.wolfsoft.creative.MainActivity;
import ws.wolfsoft.creative.R;
import iobserver.StockIObserver;

//import com.andexert.library.RippleView;
//import com.github.marlonlom.utilities.timeago.TimeAgo;


public class StockAdapter extends RecyclerView.Adapter<StockAdapter.StockViewHolder> implements Filterable{

    public StockIObserver mObserver;
    private String name_tapped;
    AdapterListener listener;
    Context context;
    private List <Stock> stockFiltered;



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





    public StockAdapter(Context context,  List <Stock> stockFiltered, AdapterListener listener){
//        this.stock = stock;
        this.listener = listener;
        this.context = context;
        this.stockFiltered = stockFiltered;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public StockViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stock_cardview, viewGroup, false);
        StockViewHolder pvh = new StockViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final StockViewHolder stockViewHolder, final int i) {
        Stock stock = stockFiltered.get(i);
        stockViewHolder.name.setText(stockFiltered.get(i).name);
        stockViewHolder.quantity.setText(String.format("%s %s", stockFiltered.get(i).quantity, stockFiltered.get(i).metric));
        stockViewHolder.category.setText(stockFiltered.get(i).category);


        int val = i+1;
        String s = String.valueOf(val);
        stockViewHolder.no.setText(s);


        name_tapped = stockFiltered.get(i).name;

        final int posi=i;



        stockViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            mObserver.onCardClicked(stockViewHolder.getAdapterPosition(), name_tapped, stockFiltered);
            }
        });

    }
    @Override
    public int getItemCount() {
        return stockFiltered.size();
    }


    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();
                if (charString.isEmpty()){
                    stockFiltered = stockFiltered;
                } else {
                List<Stock> filteredList = new ArrayList<>();

                    for (Stock stock : stockFiltered){

                        if (stock.name.toLowerCase().contains(charString.toLowerCase())){

                            filteredList.add((Stock) stockFiltered);
                        }
                    }

                    stockFiltered = filteredList;

                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = stockFiltered;

                return (FilterResults) filterResults.values;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                stockFiltered = (ArrayList<Stock>)filterResults.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }



    public interface AdapterListener {
        void onCardClicked(int pos, String name);

        void onCategorySelected(Category category);
    }
}

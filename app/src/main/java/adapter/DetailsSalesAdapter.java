package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.andexert.library.RippleView;
//import com.github.marlonlom.utilities.timeago.TimeAgo;

import java.util.List;

import beans.DetailsSales;
import iobserver.CardIObserver;
import ws.wolfsoft.creative.R;


public class DetailsSalesAdapter extends RecyclerView.Adapter<DetailsSalesAdapter.DetailsViewHolder> {

    public CardIObserver mObserver;

    private String name_tapped;
    private String phone_tapped;
    private String password_tapped;
    private String id_tapped;



    public static class DetailsViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView sale_id,amount,employee_name,sale_type,date,quantity,stock_date;

        DetailsViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card);
            sale_id = itemView.findViewById(R.id.sale_id);
            amount = itemView.findViewById(R.id.amount);
            employee_name = itemView.findViewById(R.id.employee_name);
            sale_type = itemView.findViewById(R.id.sale_type);
            date = itemView.findViewById(R.id.date);
            quantity = itemView.findViewById(R.id.quantity);
            stock_date = itemView.findViewById(R.id.stock_date);
        }

    }

    public void setListener(CardIObserver obs) { mObserver = obs; }

    List<DetailsSales> post;

    public DetailsSalesAdapter(List<DetailsSales> post){
        this.post = post;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public DetailsViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.details_sales_cardview, viewGroup, false);
        DetailsViewHolder pvh = new DetailsViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailsViewHolder detailsViewHolder, int i) {
        detailsViewHolder.amount.setText(String.format("Sold At: Ksh.%s", post.get(i).amount));
        detailsViewHolder.date.setText(String.format("Time: %s", post.get(i).date));
        detailsViewHolder.employee_name.setText(post.get(i).employee_name);
        detailsViewHolder.sale_type.setText(post.get(i).sale_type);
        detailsViewHolder.quantity.setText(String.format("%s %s", post.get(i).quantity, post.get(i).metric));
        detailsViewHolder.sale_id.setText(post.get(i).sale_id);
        detailsViewHolder.stock_date.setText(post.get(i).stock_date);

    }



    @Override
    public int getItemCount() {
        return post.size();
    }
}

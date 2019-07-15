package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import beans.Category;
import iobserver.CategoryIObserver;
import ws.wolfsoft.creative.R;

//import com.andexert.library.RippleView;
//import com.github.marlonlom.utilities.timeago.TimeAgo;


public class DebtorValueAdapter extends RecyclerView.Adapter<DebtorValueAdapter.CategoryViewHolder> {

    public CategoryIObserver mObserver;

    private String name_tapped;




    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name,amount;
        TextView no;

        CategoryViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card);
            name = itemView.findViewById(R.id.name);
            amount = itemView.findViewById(R.id.amount);
            no = itemView.findViewById(R.id.no);
        }

    }

    public void setListener(CategoryIObserver obs) { mObserver = obs; }

    List<Category> post;

    public DebtorValueAdapter(List<Category> post){
        this.post = post;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.category_cardview, viewGroup, false);
        CategoryViewHolder pvh = new CategoryViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder categoryViewHolder, int i) {

        categoryViewHolder.name.setText(post.get(i).name+": ");
        categoryViewHolder.amount.setText(post.get(i).type);
        int val = i+1;
        String s = String.valueOf(val);
        categoryViewHolder.no.setText(s);
        name_tapped = post.get(i).name;
        final int posi=i;
        categoryViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mObserver == null)
                {
                    System.out.println("Mobserver is nulllllll");
                }
                else
                {
                    mObserver.onCardClicked(posi,  name_tapped);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return post.size();
    }
}

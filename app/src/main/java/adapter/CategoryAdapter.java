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
import iobserver.CategoryIObserver;
import ws.wolfsoft.creative.R;

//import com.andexert.library.RippleView;
//import com.github.marlonlom.utilities.timeago.TimeAgo;


public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> implements Filterable{
    public CategoryIObserver mObserver;
    private String name_tapped;
    private Context context;
    private List<Category> postFiltered;
    private AdapterListener listener;


    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name;
        TextView no;

        CategoryViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card);
            name = itemView.findViewById(R.id.amount);
            no = itemView.findViewById(R.id.no);
        }

    }

    public void setListener(CategoryIObserver obs) { mObserver = obs; }

    List<Category> post;

    public CategoryAdapter(Context context, AdapterListener listener, List <Category> post){
        this.post = post;
        this.context = context;
        this.postFiltered = post;
        this.listener = listener;

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
    public void onBindViewHolder(@NonNull final CategoryViewHolder categoryViewHolder, final int i) {

        final Category category = postFiltered.get(i);
        categoryViewHolder.name.setText(category.name);

        int val = i+1;
        String s = String.valueOf(val);
        categoryViewHolder.no.setText(s);
        name_tapped = post.get(i).name;
        final int posi=i;

        categoryViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            mObserver.onCardClicked(categoryViewHolder.getAdapterPosition(),  name_tapped, postFiltered);
            }
        });

    }

    @Override
    public int getItemCount() {
        return postFiltered.size();
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Category> filteredList = new ArrayList <>();

                if (constraint == null || constraint.length() == 0){
                    filteredList.addAll(post);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Category category : post){

                        if (category.name.toLowerCase().contains(filterPattern)){

                            filteredList.add(category);
                        }
                    }

                    postFiltered = filteredList;

                }
                FilterResults results = new FilterResults();
                results.values = postFiltered;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults filterResults) {
                postFiltered = (ArrayList<Category>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AdapterListener{
        void onCategorySelected(Category category);

        void onCardClicked(int pos, String name);
    }

}

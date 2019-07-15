package adapter;

import android.content.Context;
import android.content.Intent;
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


public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.CategoryViewHolder> implements Filterable {

    public CategoryIObserver mObserver;

    private String name_tapped;
    private List<Category> postFiltered;
    private AdapterListener listener;
    private Context context;



    public static class CategoryViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name;
        TextView no;

        CategoryViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card);
            name = itemView.findViewById(R.id.name);
            no = itemView.findViewById(R.id.no);
        }

    }

    public void setListener(CategoryIObserver obs) { mObserver = obs; }

    List<Category> post;

    public SubCategoryAdapter(Context context, List<Category> post, AdapterListener listener){
        this.post = post;
        this.context= context;
        this.listener= listener;
        this.postFiltered= post;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sub_category_cardview, viewGroup, false);
        CategoryViewHolder pvh = new CategoryViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder categoryViewHolder, int i) {

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
                            mObserver.onCardClicked(posi,  name_tapped);
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
            protected FilterResults performFiltering(CharSequence charSequence) {
                List<Category> filteredList = new ArrayList<>();

                if (charSequence == null || charSequence.length() == 0){
                    filteredList.addAll(post);
                } else {
                    String filterPattern = charSequence.toString().toLowerCase().trim();

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
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            postFiltered= (ArrayList<Category>) filterResults.values;
            notifyDataSetChanged();
            }
        };
    }

    public interface AdapterListener{
        void onCategorySelected(Category category);

        void onCardClicked(int pos, String name);
    }
}


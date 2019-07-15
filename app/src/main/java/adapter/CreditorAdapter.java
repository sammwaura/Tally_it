package adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import beans.Creditor;
import iobserver.CreditorIObserver;
import ws.wolfsoft.creative.R;

//import com.andexert.library.RippleView;
//import com.github.marlonlom.utilities.timeago.TimeAgo;


public class CreditorAdapter extends RecyclerView.Adapter<CreditorAdapter.CreditorViewHolder> {

    public CreditorIObserver mObserver;

    private String name_tapped;
    private String amount_tapped;
    private String id_tapped;



    public static class CreditorViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name,desc,date;
        TextView amount;
        //TextView no;
        ImageView icon;

        CreditorViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card);
            name = itemView.findViewById(R.id.name);
            desc = itemView.findViewById(R.id.desc);
            date = itemView.findViewById(R.id.date);
            amount = itemView.findViewById(R.id.amount);
            //no = itemView.findViewById(R.id.no);
            icon= itemView.findViewById(R.id.settIcon);
        }

    }

    public void setListener(CreditorIObserver obs) { mObserver = obs; }

    List<Creditor> post;

    public CreditorAdapter(List<Creditor> post){
        this.post = post;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public CreditorViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.creditor_cardview, viewGroup, false);
        CreditorViewHolder pvh = new CreditorViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final CreditorViewHolder creditorViewHolder, int i) {

        creditorViewHolder.name.setText(post.get(i).name.split("#")[0]);
        if(post.get(i).name.split("#").length >=1)
        {
            creditorViewHolder.desc.setText(post.get(i).name.split("#")[1]);
        }
        if(post.get(i).name.split("#").length >=2)
        {
            creditorViewHolder.date.setText(post.get(i).name.split("#")[2]);
        }

        creditorViewHolder.amount.setText(post.get(i).amount);

        if(post.get(i).amount.equals("0"))
        {
            creditorViewHolder.icon.setVisibility(View.GONE);
        }

        int val = i+1;
        String s = String.valueOf(val);
        id_tapped = post.get(i).id;
        name_tapped = post.get(i).name;
        amount_tapped = post.get(i).amount;

        final int posi=i;


        creditorViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            mObserver.onCardClicked(posi, id_tapped, name_tapped, amount_tapped);
            }
        });



    }



    @Override
    public int getItemCount() {
        return post.size();
    }
}

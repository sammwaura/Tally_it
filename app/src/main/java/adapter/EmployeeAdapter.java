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

import beans.Employee;
import iobserver.CardIObserver;
import ws.wolfsoft.creative.R;


public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.EmployeeViewHolder> {

    public CardIObserver mObserver;

    private String name_tapped;
    private String phone_tapped;
    private String password_tapped;
    private String id_tapped;



    public static class EmployeeViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView name;
        TextView phone;
        TextView password;
        TextView no;

        EmployeeViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.card);

            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            password = itemView.findViewById(R.id.password);
            no = itemView.findViewById(R.id.no);


        }

    }

    public void setListener(CardIObserver obs) { mObserver = obs; }

    List<Employee> post;

    public EmployeeAdapter(List<Employee> post){
        this.post = post;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public EmployeeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.employee_cardview, viewGroup, false);
        EmployeeViewHolder pvh = new EmployeeViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(@NonNull final EmployeeViewHolder employeeViewHolder, int i) {



        employeeViewHolder.name.setText(post.get(i).name);
        employeeViewHolder.phone.setText(post.get(i).phone);
        employeeViewHolder.password.setText(post.get(i).password);
        int val = i+1;
        String s = String.valueOf(val);
        employeeViewHolder.no.setText(s);



        id_tapped = post.get(i).id;
        name_tapped = post.get(i).name;
        phone_tapped = post.get(i).phone;
        password_tapped = post.get(i).password;

        final int posi=i;


        employeeViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            mObserver.onCardClicked(posi, id_tapped, name_tapped, phone_tapped, password_tapped);
            }
        });



    }



    @Override
    public int getItemCount() {
        return post.size();
    }
}

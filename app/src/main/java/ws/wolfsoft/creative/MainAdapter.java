package ws.wolfsoft.creative;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;

import beans.Note;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.CustomViewHolder> {

    private Context context;
    private ArrayList<Note> noter;
    private ItemClickListener itemClickListener;
    private int note_id;

    public void swapItems(ArrayList<Note> noter) {
        // compute diffs
        final NoteDiffCallback diffCallback = new NoteDiffCallback(this.noter, noter);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        // clear contacts and add
        this.noter.clear();
        this.noter.addAll(noter);

        diffResult.dispatchUpdatesTo(this); // calls adapter's notify methods after diff is computed
    }


    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tv_title, tv_note, tv_date;
        public CardView card_item;
        public RelativeLayout mainCard;
        public ItemClickListener itemClickListener;

        CustomViewHolder(View view, ItemClickListener itemClickListener) {
            super(view);

            tv_title = view.findViewById(R.id.title);
            mainCard = view.findViewById(R.id.mainCard);
            tv_note = view.findViewById(R.id.note);
            tv_date = view.findViewById(R.id.date);
            card_item = view.findViewById(R.id.card_item);
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemOnClick(v, getAdapterPosition());
        }
    }

    public MainAdapter(Context context, ArrayList<Note> noter) {
        this.context = context;
        this.noter = noter;
    }

    public class NoteDiffCallback extends DiffUtil.Callback {

        private ArrayList<Note> mOldList;
        private ArrayList<Note> mNewList;

        public NoteDiffCallback(ArrayList<Note> oldList, ArrayList<Note> newList) {
            this.mOldList = oldList;
            this.mNewList = newList;
        }

        @Override
        public int getOldListSize() {
            return mOldList.size();
        }

        @Override
        public int getNewListSize() {
            return mNewList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            // add a unique ID property on Note and expose a getId() method
            return mOldList.get(oldItemPosition).getId() == mNewList.get(newItemPosition).getId();
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            Note oldNote = mOldList.get(oldItemPosition);
            Note newNote = mNewList.get(newItemPosition);

            if
                    (oldNote.getTitle() == newNote.getTitle() &&
                    oldNote.getNote() == newNote.getNote() &&
                    oldNote.getColor() == newNote.getColor()) {
                return true;
            }
            return false;
        }
    }

    @NonNull

    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        final View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new CustomViewHolder(view, itemClickListener);

    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        final Note note = noter.get(position);
        holder.tv_title.setText(noter.get(position).getTitle());
        //  holder.tv_title.setText(note.getTitle());
        holder.tv_note.setText(note.getNote());
        //holder.tv_date.setText(note.getDate());

        holder.card_item.setCardBackgroundColor(note.getColor());

        holder.mainCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("note_id", noter.get(position).getId());
                intent.putExtra("title", noter.get(position).getTitle());
                intent.putExtra("note", noter.get(position).getNote());
                intent.putExtra("color", noter.get(position).getColor());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noter.size();
    }


    public interface ItemClickListener {
        void onItemOnClick(View view, int position);

        View.OnClickListener onItemOnClick();
    }
}









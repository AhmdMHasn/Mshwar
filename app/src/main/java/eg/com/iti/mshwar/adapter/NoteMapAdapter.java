package eg.com.iti.mshwar.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.ArrayList;

import eg.com.iti.mshwar.R;

public class NoteMapAdapter extends RecyclerView.Adapter<NoteMapAdapter.NoteViewHolder> {

    private ArrayList<String> notes;


    public NoteMapAdapter(ArrayList<String> notes) {
        this.notes = notes;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.note_item, viewGroup , false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder tripViewHolder, int position) {
        tripViewHolder.bind(position);
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder{
        TextView txtViewNote;
        NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            txtViewNote = itemView.findViewById(R.id.textView_note);
        }

        void bind(int position){
            txtViewNote.setText(notes.get(position));
        }
    }
}

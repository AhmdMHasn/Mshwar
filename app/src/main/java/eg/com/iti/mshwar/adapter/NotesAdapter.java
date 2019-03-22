package eg.com.iti.mshwar.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import eg.com.iti.mshwar.R;

public class NotesAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> notesList;
    private Context context;


    public NotesAdapter(ArrayList<String> notesList, Context context) {
        this.notesList = notesList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return notesList.size();
    }

    @Override
    public Object getItem(int pos) {
        return notesList.get(pos);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_notes_layout, null);
        }

        TextView textViewAddNote = view.findViewById(R.id.textView_add_note);
        textViewAddNote.setText(notesList.get(position));

        ImageView imgDeleteNote = view.findViewById(R.id.delete_note);

        imgDeleteNote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                notesList.remove(position);
                notifyDataSetChanged();
            }
        });

        return view;
    }

}
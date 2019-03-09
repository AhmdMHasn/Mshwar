package eg.com.iti.mshwar.adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.model.TripPojo;

import static android.support.constraint.Constraints.TAG;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<TripPojo> tripsList;
    private LayoutInflater inflater;

    public RecyclerAdapter(Context context, List<TripPojo> data){
        this.tripsList = data;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Log.d(TAG, "onCreateViewHolder");
        View view = inflater.inflate(R.layout.list_item_main, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder myViewHolder, int position) {
        Log.d(TAG, "onBindViewHolder " + position);

        TripPojo currentObject = tripsList.get(position);
//        holder.setData(currentObject, position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

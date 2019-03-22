package eg.com.iti.mshwar.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.card.MaterialCardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import eg.com.iti.mshwar.beans.TripBean;
import eg.com.iti.mshwar.R;
import eg.com.iti.mshwar.dao.TripDaoImpl;
import eg.com.iti.mshwar.util.Utils;

import static android.support.constraint.Constraints.TAG;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private List<TripBean> tripsList;
    private LayoutInflater inflater;
    private Context context;

    private final int VIEW_TYPE_EMPTY_LIST = 0;
    private final int VIEW_TYPE_FULL_VIEW = 1;


    public RecyclerAdapter(Context context){
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        View view = inflater.inflate(R.layout.list_item_main, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override // In order to check the view type in case of multiple list items
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.MyViewHolder myViewHolder, int position) {
        Log.d(TAG, "onBindViewHolder " + position);

        TripBean currentObject = tripsList.get(position);
        myViewHolder.setData(currentObject, position);
        myViewHolder.setListeners();
    }

    public void setUpdatedData(List<TripBean> tripList){
        this.tripsList = tripList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return tripsList.size();
    }

    public void removeItem(int position){
        tripsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, tripsList.size());
    }

    public void addItem(int position, TripBean trip){
        tripsList.add(position, trip);
        notifyItemInserted(position);
        notifyItemRangeChanged(position, tripsList.size());
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tripName, description, status;
        ImageView thumbnail, mapThumbnail;
        Button delete, note;
        ImageButton start;
        MaterialCardView container;
        int position;
        TripBean currentObject;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tripName    = itemView.findViewById(R.id.title_list_item_main);
            description = itemView.findViewById(R.id.desc_list_item_main);
            status      = itemView.findViewById(R.id.status_list_item_main);
            thumbnail   = itemView.findViewById(R.id.img_list_item_main);
            mapThumbnail= itemView.findViewById(R.id.img_list_item_map);
            start       = itemView.findViewById(R.id.start_list_item_main);
            delete      = itemView.findViewById(R.id.delete_list_item_main);
            note        = itemView.findViewById(R.id.note_list_item_main);
        }

        public void setData(TripBean currentObject, int position) {
            this.tripName.setText(currentObject.getName());
            this.description.setText(currentObject.getStartPoint() + " - " + currentObject.getEndPoint());
            this.status.setText((currentObject.getStatus()));
            this.thumbnail.setImageResource(currentObject.getStatusImage());
            this.position = position;
            this.currentObject = currentObject;

            String imgPath = "https://maps.googleapis.com/maps/api/staticmap?size=500x250" +
                    "&markers=color:blue%7Clabel:S%7C"
                    +currentObject.getStartPointLatitude()+","+currentObject.getStartPointLongitude()+
                    "&markers=color:red%7Clabel:E%7C"
                    +currentObject.getEndPointLatitude()+","+currentObject.getEndPointLongitude()+
                    "&key=AIzaSyDIJ9XX2ZvRKCJcFRrl-lRanEtFUow4piM";

            Log.i("Path: ", imgPath);

            Picasso.with(context)
                    .load(imgPath)
                    .placeholder(R.drawable.ic_img_default_img)
                    .error(R.drawable.ic_img_default_logo)
                    .into(mapThumbnail);

            // Hide start button if the status is not upcoming
            if (!currentObject.getStatus().equalsIgnoreCase(Utils.UPCOMING)){
                this.start.setVisibility(View.GONE);
            }

            // Change status text color based on status
            switch (currentObject.getStatus()){
                case Utils.UPCOMING:
                    this.status.setTextColor(Color.parseColor("#1081e0"));
                    break;
                case Utils.CANCELLED:
                    this.status.setTextColor(Color.parseColor("#d75a4a"));
                    break;
                case Utils.DONE:
                    this.status.setTextColor(Color.parseColor("#25ae88"));
                    break;
            }
        }

        public void setListeners() {
            start.setOnClickListener(MyViewHolder.this);
            delete.setOnClickListener(MyViewHolder.this);
            note.setOnClickListener(MyViewHolder.this);
            this.itemView.setOnClickListener(MyViewHolder.this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.start_list_item_main:
                    addItem(position, currentObject);
                    Toast.makeText(v.getContext(), "Start at Position " + position, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.delete_list_item_main:
                    removeItem(position);
                    Toast.makeText(v.getContext(), "Delete at Position " + position, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.note_list_item_main:
                    Toast.makeText(v.getContext(), "Note at Position " + position, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(v.getContext(), "Click at Position " + position, Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    }
}

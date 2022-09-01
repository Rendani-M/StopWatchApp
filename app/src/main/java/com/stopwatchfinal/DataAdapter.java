package com.stopwatchfinal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder>{

    private ArrayList<TimeData> data;
    private   RecyclerViewInterface recyclerViewInterface;
    private Context context;


    public DataAdapter(Context context,ArrayList<TimeData> data, RecyclerViewInterface recyclerViewInterface) {
        this.data = data;
        this.recyclerViewInterface= recyclerViewInterface;
        this.context= context;
    }

    @NonNull
    @Override
    public DataAdapter.DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.dataentry,parent,false);
        return new DataViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull DataAdapter.DataViewHolder holder, int position) {
        TimeData timeData= data.get(position);
        holder.image.setImageResource(R.drawable.ic_running_icon);
        holder.date.setText(timeData.getDate());
        holder.time.setText(timeData.getTime());
        holder.runningTime.setText(timeData.getRunningTime());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(recyclerViewInterface !=null) {
                    int pos = holder.getAbsoluteAdapterPosition();

                    if(pos != RecyclerView.NO_POSITION){
                        PopupMenu popupMenu= new PopupMenu(context,holder.delete);
                        popupMenu.inflate(R.menu.delete_menu);
                        popupMenu.setOnMenuItemClickListener(item->{
                            switch ((item.getItemId())){
                                case R.id.delete_option_menulayout:
                                    //do something
                                    recyclerViewInterface.onItemClick(pos, true);
                                    break;
                                case R.id.cancel_option_menulayout:
                                    //do something
                                    recyclerViewInterface.onItemClick(pos, false);
                                    break;
                            }
                            return false;

                        });
                        popupMenu.show();

                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class DataViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView date;
        TextView time;
        TextView runningTime;

        ImageView delete;

        public DataViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            image= itemView.findViewById(R.id.image_dataentry_layout);
            date= itemView.findViewById(R.id.date_dataentry_layout);
            time= itemView.findViewById(R.id.time_dataentry_layout);
            runningTime= itemView.findViewById(R.id.runningtime_dataentry_layout);
            delete= itemView.findViewById(R.id.delete_dataentry_layout);

        }
    }
}

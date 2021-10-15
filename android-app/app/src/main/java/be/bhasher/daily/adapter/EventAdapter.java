package be.bhasher.daily.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;

import be.bhasher.daily.calendar.EventModel;
import be.bhasher.daily.R;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final ArrayList<Object> eventList;

    public EventAdapter(ArrayList<Object> eventList){
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater view = LayoutInflater.from(parent.getContext());
        switch (viewType){
            case 0: return new ViewHolderEvent(view.inflate(R.layout.event, parent, false));
            case 1: return new ViewHolderDate(view.inflate(R.layout.date_event, parent, false));
            default: return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()){
            case 0:
                ViewHolderEvent viewHolderEvent = (ViewHolderEvent) holder;
                EventModel event = (EventModel) eventList.get(position);
                viewHolderEvent.eventTitle.setText(event.getName());
                viewHolderEvent.eventTime.setText(event.getParsedTime());
                viewHolderEvent.eventColor.setColorFilter(event.getColor());
                break;
            case 1:
                ViewHolderDate viewHolderDate = (ViewHolderDate) holder;
                String text = (String) eventList.get(position);
                viewHolderDate.dateTitle.setText(text);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    @Override
    public int getItemViewType(int position){
        if(eventList.get(position).getClass() == EventModel.class) return 0;
        if(eventList.get(position).getClass() == String.class) return 1;
        return -1;
    }

    static class ViewHolderEvent extends RecyclerView.ViewHolder {
        private final ImageView eventColor;
        private final TextView eventTitle;
        private final TextView eventTime;

        public ViewHolderEvent(View view) {
            super(view);
            this.eventColor = view.findViewById(R.id.event_color);
            this.eventTitle = view.findViewById(R.id.event_title);
            this.eventTime = view.findViewById(R.id.event_time);
        }
    }

    static class ViewHolderDate extends RecyclerView.ViewHolder {
        private final TextView dateTitle;

        public ViewHolderDate(View view) {
            super(view);
            this.dateTitle = view.findViewById(R.id.date_event_text);
        }
    }
}

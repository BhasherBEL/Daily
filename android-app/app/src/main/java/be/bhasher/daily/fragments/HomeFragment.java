package be.bhasher.daily.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;

import be.bhasher.daily.MainActivity;
import be.bhasher.daily.R;
import be.bhasher.daily.adapter.EventAdapter;
import be.bhasher.daily.calendar.CalendarManager;
import be.bhasher.daily.calendar.EventModel;

public class HomeFragment extends Fragment {

    private final MainActivity activity;

    public HomeFragment(MainActivity mainActivity) {
        this.activity = mainActivity;
    }

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        CalendarManager eventsController = new CalendarManager(requireContext());

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        final long[] currentBounds = {0, DateUtils.DAY_IN_MILLIS * 14};

        ArrayList<Object> events = eventsController.getEventsInRangeWithSep(currentBounds[0], currentBounds[1]);

        RecyclerView recyclerView = view.findViewById(R.id.events_recycler_view);

        ImageView reloadView = this.activity.findViewById(R.id.reload);

        EventAdapter eventAdapter = new EventAdapter(events, activity);

        recyclerView.setAdapter(eventAdapter);

        reloadView.setOnClickListener(v -> {
            v.startAnimation(new AlphaAnimation(1F, 0.5F));
            currentBounds[0] = 0;
            currentBounds[1] = DateUtils.DAY_IN_MILLIS * 14;
            events.clear();
            events.addAll(eventsController.getEventsInRangeWithSep(currentBounds[0], currentBounds[1]));
            eventAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(0);
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(!recyclerView.canScrollVertically(1)){
                    int previousSize = events.size();
                    events.addAll(eventsController.getEventsInRangeWithSep(currentBounds[1] + DateUtils.DAY_IN_MILLIS, currentBounds[1] + DateUtils.DAY_IN_MILLIS * 7));
                    currentBounds[1] += DateUtils.DAY_IN_MILLIS * 7;
                    eventAdapter.notifyItemRangeInserted(previousSize, events.size());
                }else if(!recyclerView.canScrollVertically(-1)){
                    ArrayList<Object> newEvents = eventsController.getEventsInRangeWithSep(currentBounds[0] - DateUtils.DAY_IN_MILLIS * 7, currentBounds[0] - DateUtils.DAY_IN_MILLIS);
                    events.addAll(0, newEvents);
                    currentBounds[0] -= DateUtils.DAY_IN_MILLIS * 7;
                    eventAdapter.notifyItemRangeInserted(0, newEvents.size());
                }
            }
        });


        return view;
    }
}

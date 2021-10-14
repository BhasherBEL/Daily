package be.bhasher.daily.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import be.bhasher.daily.CalendarController;
import be.bhasher.daily.EventModel;
import be.bhasher.daily.EventsController;
import be.bhasher.daily.R;
import be.bhasher.daily.adapter.EventAdapter;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        EventsController eventsController = new EventsController(Objects.requireNonNull(getContext()));

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ArrayList<Object> eventList = eventsController.getSortedEvents();

        RecyclerView recyclerView = view.findViewById(R.id.events_recycler_view);

        recyclerView.setAdapter(new EventAdapter(eventList));

        //recyclerView.scrollToPosition();

        return view;
    }
}

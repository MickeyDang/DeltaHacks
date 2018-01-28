package mdstudios.deltahacks;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;


public class LocationListFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private CheckBox mCheckBoxQuiet;
    private CheckBox mCheckBoxBusy;
    private CheckBox mCheckBoxFull;

    RecyclerView mRecyclerView;
    private Map<String, Location> mLocationMap = new HashMap<>();
    private List<Location> mLocations = new ArrayList<>();
    private MyLocationRecyclerViewAdapter mAdapter;

    private OnFragmentInteractionListener mListener;

    public LocationListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static LocationListFragment newInstance() {
        LocationListFragment fragment = new LocationListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_location_list2, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        mRecyclerView = view.findViewById(R.id.list);
        mCheckBoxBusy = view.findViewById(R.id.busy);
        mCheckBoxFull = view.findViewById(R.id.full);
        mCheckBoxQuiet = view.findViewById(R.id.quiet);

        mCheckBoxQuiet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    filterList("Quiet");

            }
        });

        mCheckBoxFull.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    filterList("Full");

            }
        });

        mCheckBoxBusy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    filterList("Busy");

            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new MyLocationRecyclerViewAdapter(mLocations, mListener, getContext());
        mRecyclerView.setAdapter(mAdapter);

        loadData();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void loadData() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("location");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d(LOG_TAG, "child added");
                Location loc = new Location(dataSnapshot.getKey(),
                        //weird hack bc db stores capacity as long
                        Integer.valueOf(String.valueOf(dataSnapshot.child("capacity").getValue())),
                        (String) dataSnapshot.child("status").getValue(),
                        Integer.valueOf(String.valueOf(dataSnapshot.child("imageID").getValue())));
                mLocationMap.put(dataSnapshot.getKey(), loc);
                updateAdapter(mLocationMap.values());

                //Integer.valueOf(String.valueOf(dataSnapshot.child("imageID").getValue()))
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Location loc = new Location(dataSnapshot.getKey(),
                        //weird hack bc db stores capacity as long
                        Integer.valueOf(String.valueOf(dataSnapshot.child("capacity").getValue())),
                        (String) dataSnapshot.child("status").getValue(),
                        Integer.valueOf(String.valueOf(dataSnapshot.child("imageID").getValue())));
                mLocationMap.put(dataSnapshot.getKey(), loc);

                updateAdapter(mLocationMap.values());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void filterList(String s) {

        boolean busy = mCheckBoxBusy.isChecked();
        boolean quiet = mCheckBoxQuiet.isChecked();
        boolean full = mCheckBoxFull.isChecked();

        Collection<Location> temp = new ArrayList<>();

        if (!busy && !quiet && !full) {
            temp.addAll(mLocationMap.values());
        } else {
            for (Location loc : mLocationMap.values()) {
                if ((busy && loc.getCapacity().equals("Busy"))
                        || (quiet && loc.getCapacity().equals("Quiet"))
                        || (full & loc.getCapacity().equals("Full"))) {

                    temp.add(loc);
                }
            }
        }
        updateAdapter(temp);
    }

    private void updateAdapter(Collection<Location> locations) {
        mAdapter.updateList(locations);
        Log.d(LOG_TAG, "updating list");
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

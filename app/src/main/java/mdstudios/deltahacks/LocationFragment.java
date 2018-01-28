package mdstudios.deltahacks;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mdstudios.deltahacks.dummy.DummyContent;
import mdstudios.deltahacks.dummy.DummyContent.DummyItem;

import java.security.cert.CollectionCertStoreParameters;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class LocationFragment extends Fragment {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private Map<String, Location> mLocationMap = new HashMap<>();
    private List<Location> mLocations = new ArrayList<>();
    private MyLocationRecyclerViewAdapter mAdapter;

    public LocationFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LocationFragment newInstance(int columnCount) {
        LocationFragment fragment = new LocationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_list, container, false);

        mAdapter = new MyLocationRecyclerViewAdapter(mLocations, mListener);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
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
                        (String) dataSnapshot.child("status").getValue());
                mLocationMap.put(dataSnapshot.getKey(), loc);

                updateAdapter();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Location loc = new Location(dataSnapshot.getKey(),
                        //weird hack bc db stores capacity as long
                        Integer.valueOf(String.valueOf(dataSnapshot.child("capacity").getValue())),
                        (String) dataSnapshot.child("status").getValue());
                mLocationMap.put(dataSnapshot.getKey(), loc);

                updateAdapter();
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


    private void updateAdapter() {
        mAdapter.updateList(mLocationMap.values());
        Log.d(LOG_TAG, "updating list");
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction();
    }
}

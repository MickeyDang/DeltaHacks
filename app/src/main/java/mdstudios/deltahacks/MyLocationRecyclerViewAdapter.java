package mdstudios.deltahacks;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import mdstudios.deltahacks.LocationFragment.OnListFragmentInteractionListener;
import mdstudios.deltahacks.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class MyLocationRecyclerViewAdapter extends RecyclerView.Adapter<MyLocationRecyclerViewAdapter.ViewHolder> {

    private final String LOG_TAG = this.getClass().getSimpleName();

    private List<Location> mLocations = new ArrayList<>();
    private final OnListFragmentInteractionListener mListener;

    public MyLocationRecyclerViewAdapter(List<Location> items, OnListFragmentInteractionListener listener) {
        mLocations = items;
        mListener = listener;
    }

    public void updateList(Collection<Location> list) {
        Log.d(LOG_TAG, "updating list in adapter");

        mLocations.clear();
        mLocations.addAll(list);

        for (Location loc : mLocations) {
            Log.d(LOG_TAG, loc.getName());
        }


        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_location, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Log.d(LOG_TAG, "onBindViewHolder");

        Location loc = mLocations.get(position);
        holder.mLocationName.setText(loc.getName());
        String s = "Status: " + loc.getCapacity() + " (" + loc.getPeople() + " people)";
        holder.mDetails.setText(s);

    }

    @Override
    public int getItemCount() {
        return mLocations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mLocationName;
        public final TextView mDetails;

        public ViewHolder(View view) {
            super(view);
            mLocationName = view.findViewById(R.id.locationName);
            mDetails = view.findViewById(R.id.details);
        }

    }
}

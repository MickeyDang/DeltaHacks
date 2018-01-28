package mdstudios.deltahacks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private Context mContext;

    public MyLocationRecyclerViewAdapter(List<Location> items, OnListFragmentInteractionListener listener, Context c) {
        mLocations = items;
        mListener = listener;
        mContext = c;
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

        if (loc.getCapacity().equals("Busy")) {
            holder.borderBottom.setBackgroundColor(mContext.getResources().getColor(R.color.colorBusy));
            holder.borderTop.setBackgroundColor(mContext.getResources().getColor(R.color.colorBusy));
            holder.borderLeft.setBackgroundColor(mContext.getResources().getColor(R.color.colorBusy));
            holder.borderRight.setBackgroundColor(mContext.getResources().getColor(R.color.colorBusy));
        } else if (loc.getCapacity().equals("Quiet")) {
            holder.borderBottom.setBackgroundColor(mContext.getResources().getColor(R.color.colorQuiet));
            holder.borderTop.setBackgroundColor(mContext.getResources().getColor(R.color.colorQuiet));
            holder.borderLeft.setBackgroundColor(mContext.getResources().getColor(R.color.colorQuiet));
            holder.borderRight.setBackgroundColor(mContext.getResources().getColor(R.color.colorQuiet));
        } else if (loc.getCapacity().equals("Full")) {
            holder.borderBottom.setBackgroundColor(mContext.getResources().getColor(R.color.colorFull));
            holder.borderTop.setBackgroundColor(mContext.getResources().getColor(R.color.colorFull));
            holder.borderLeft.setBackgroundColor(mContext.getResources().getColor(R.color.colorFull));
            holder.borderRight.setBackgroundColor(mContext.getResources().getColor(R.color.colorFull));
        }

        holder.mLocationView.setBackground(mContext.getResources().getDrawable(loc.getResourceID()));

    }

    @Override
    public int getItemCount() {
        return mLocations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mLocationName;
        public final TextView mDetails;
        public final ImageView mLocationView;
        public final ImageView borderTop;
        public final ImageView borderBottom;
        public final ImageView borderLeft;
        public final ImageView borderRight;

        public ViewHolder(View view) {
            super(view);
            mLocationName = view.findViewById(R.id.locationName);
            mDetails = view.findViewById(R.id.details);
            mLocationView = view.findViewById(R.id.buildingPicture);

            borderBottom = view.findViewById(R.id.borderBottom);
            borderTop = view.findViewById(R.id.borderTop);
            borderLeft = view.findViewById(R.id.borderLeft);
            borderRight = view.findViewById(R.id.borderRight);
        }

    }


}

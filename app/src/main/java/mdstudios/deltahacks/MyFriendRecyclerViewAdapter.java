package mdstudios.deltahacks;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.ivbaranov.mli.MaterialLetterIcon;

import mdstudios.deltahacks.FriendFragment.OnListFragmentInteractionListener;
import mdstudios.deltahacks.dummy.DummyContent.DummyItem;

import java.util.Collection;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyFriendRecyclerViewAdapter extends RecyclerView.Adapter<MyFriendRecyclerViewAdapter.ViewHolder> {

    private final List<User> mValues;
    private final OnListFragmentInteractionListener mListener;
    Context c;

    public MyFriendRecyclerViewAdapter(List<User> items, OnListFragmentInteractionListener listener, Context c) {
        mValues = items;
        mListener = listener;
        this.c = c;
    }

    public void updateList(Collection<User> users) {
        mValues.clear();
        mValues.addAll(users);

        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        String s = "@ " + mValues.get(position).getLocation() + "...";
        holder.mLocation.setText(s);

        holder.mName.setText(mValues.get(position).getName());
        holder.mIcon.setShapeColor(c.getResources().getColor(R.color.colorQuiet));
        holder.mIcon.setLetterColor(R.color.black);
        holder.mIcon.setLetter(String.valueOf(mValues.get(position).getName().charAt(0)));

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mName;
        public final TextView mLocation;
        public final MaterialLetterIcon mIcon;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mName = (TextView) view.findViewById(R.id.id);
            mLocation = (TextView) view.findViewById(R.id.content);
            mIcon = view.findViewById(R.id.icon);
        }

    }
}

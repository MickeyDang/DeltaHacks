package mdstudios.deltahacks;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class CheckInFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    public CheckInFragment() {
        // Required empty public constructor
    }

    public static CheckInFragment newInstance() {
        CheckInFragment fragment = new CheckInFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button scanButton = view.findViewById(R.id.scanButton);
        final CommunicationClient cc = new CommunicationClient();

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cc.textToServerPhone(makeBundle());
            }
        });

    }

    private Bundle makeBundle() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        Bundle bundle = new Bundle();
        bundle.putString(Utils.NAME_KEY, prefs.getString("name", "Mickey"));
        bundle.putString(Utils.STATUS_KEY, "true");
        bundle.putString(Utils.LOC_KEY, "E5 Room 6008");
        return bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_check_in, container, false);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

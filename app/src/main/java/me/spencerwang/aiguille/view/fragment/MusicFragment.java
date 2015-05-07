package me.spencerwang.aiguille.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.spencerwang.aiguille.R;


public class MusicFragment extends BaseFragment {

    // TODO: Rename and change types of parameters
    public static MusicFragment newInstance(int musicCategory) {
        MusicFragment fragment = new MusicFragment();
        Bundle args = new Bundle();
        args.putString("newsCategory", "");
        fragment.setArguments(args);
        return fragment;
    }

    public static MusicFragment newInstance(){
        MusicFragment fragment = new MusicFragment();
        return  fragment;
    }

    public MusicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music, container, false);
        view.setBackgroundResource(BGDRAWABLE[1]);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
  /*  public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

   /* @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
       // mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
/*    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }*/

    @Override
    public void notifyDataCategorySetChange(int tag) {

    }
}

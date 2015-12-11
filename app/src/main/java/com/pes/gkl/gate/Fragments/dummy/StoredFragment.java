package com.pes.gkl.gate.Fragments.dummy;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.pes.gkl.gate.Fragments.dummy.dummy.OfflineTestFragment;
import com.pes.gkl.gate.R;
import com.pes.gkl.gate.Fragments.dummy.dummy.DummyContent;
import com.pes.gkl.gate.modelclasses.Question;
import com.pes.gkl.gate.modelclasses.Topic;
import com.pes.gkl.gate.serverutils.Parsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class StoredFragment extends Fragment implements AbsListView.OnItemClickListener {
    ArrayList<Topic> topicList;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    File[] files;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static StoredFragment newInstance(String param1, String param2) {
        StoredFragment fragment = new StoredFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StoredFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        File f=getActivity().getFilesDir();
        files=f.listFiles();
        Topic[] saved=new Topic[files.length];
        topicList=new ArrayList<>();
        int j=0;
        for (int i=0;i<files.length;i++)
        {
            //saved[i]=new Topic(i,files[i].getName());
           // Toast.makeText(getActivity(), files[i].getName(), Toast.LENGTH_SHORT).show();
            if(files[i].getName().contains("ANSWER_FILE"))continue;
            //Don't get confused later. Storing with i as access is easier onclick
            topicList.add(new Topic(i, files[i].getName().substring(0, files[i].getName().length()-4)));

        }
        //topicList = new ArrayList<Topic>(Arrays.asList(saved));

        System.out.println(topicList);

        com.pes.gkl.gate.Fragments.dummy.DummyContent.ITEMS = topicList;

        mAdapter = new ArrayAdapter<Topic>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, com.pes.gkl.gate.Fragments.dummy.DummyContent.ITEMS);

        mListView.setAdapter(mAdapter);

        return view;
    }
    /*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            //throw new ClassCastException(activity.toString()
            //       + " must implement OnFragmentInteractionListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(DummyContent.ITEMS.get(position).id);
        }
        Log.e("touched", position + "");
        //Toast.makeText(getActivity(), topicList.get(position).toString(),
               // Toast.LENGTH_SHORT).show();
        File f = files[topicList.get(position).id];
        try {
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            StringBuilder sb = new StringBuilder();
            String data;
            while ((data = br.readLine()) != null) {
                sb.append(data);
            }
           // Toast.makeText(getActivity(), "FILE : " + sb.toString(), Toast.LENGTH_SHORT).show();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
            Question[] questions= Parsers.questionlistparser(sb.toString());
            for (Question q:questions){
                q.topic=f.getName();
            }
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        OfflineTestFragment q = new OfflineTestFragment();
        Bundle b = new Bundle();
        b.putSerializable("response", (Serializable) questions);
        q.setArguments(b);
        transaction.replace(((ViewGroup) (getView().getParent())).getId(), q);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
        catch (Exception e)
        {

        }


    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}

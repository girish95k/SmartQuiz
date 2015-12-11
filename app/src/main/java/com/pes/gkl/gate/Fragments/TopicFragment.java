package com.pes.gkl.gate.Fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pes.gkl.gate.R;

import com.pes.gkl.gate.Fragments.dummy.DummyContent;
import com.pes.gkl.gate.modelclasses.Topic;

import org.apache.http.Header;
import org.json.JSONObject;
import com.pes.gkl.gate.serverutils.Parsers;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class TopicFragment extends Fragment implements AbsListView.OnItemClickListener {

    ArrayList<Topic> topicList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
    public static TopicFragment newInstance(String param1, String param2) {
        TopicFragment fragment = new TopicFragment();
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
    public TopicFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Register with the Server
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        /*
        params.put("controller", "customer");
        params.put("action", "getverified");
        params.put("phno", hid);
        params.put("pass", pass);
        //Log.e("phno", phno);
        Log.e("pass", pass);
        */

        final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Logging In");
        pDialog.setCancelable(false);


        client.get("http://karthikradhakrishnan96.pythonanywhere.com/Quiz/get_topics", params, new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                //Toast.makeText(getApplicationContext(), "Sending Request",
                //Toast.LENGTH_SHORT).show();
                // called before request is started
                pDialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                //Toast.makeText(getApplicationContext(), new String(response),
                //Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                Log.e("response", new String(response));

                Topic topic[] = Parsers.topiclistparser(new String(response));

                topicList = new ArrayList<Topic>(Arrays.asList(topic));

                System.out.println(topicList);

                DummyContent.ITEMS = topicList;

                mAdapter = new ArrayAdapter<Topic>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);

                mListView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                pDialog.dismiss();
                Toast.makeText(getActivity(), "Error. ",
                        Toast.LENGTH_SHORT).show();
                if(errorResponse!=null) {
                    Log.e("Failure", new String(errorResponse));
                }
                e.printStackTrace();
            }

            @Override
            public void onRetry(int retryNo) {
                Toast.makeText(getActivity(), "Retrying",
                        Toast.LENGTH_SHORT).show();
                // called when request is retried
            }
        });

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        // TODO: Change Adapter to display your content
        mAdapter = new ArrayAdapter<Topic>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                // TODO Auto-generated method stub
                Log.e("Long", Integer.toString(index));
                String str = mListView.getItemAtPosition(index).toString();

                Log.e("long click : ", str);
                //Toast.makeText(getActivity(), str,
                     //   Toast.LENGTH_SHORT).show();
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();
                final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Logging In");
                pDialog.setCancelable(false);
                params.put("topic_name", str);

                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", getActivity().MODE_PRIVATE);
                String user = pref.getString("user", ":(");
                String pass = pref.getString("pass", "):");

                params.put("user_name", user);
                params.put("password", pass);
                final String name = str;
                client.post("http://karthikradhakrishnan96.pythonanywhere.com/Quiz/get_questions_of_topic", params, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        //Toast.makeText(getApplicationContext(), "Sending Request",
                        //Toast.LENGTH_SHORT).show();
                        // called before request is started
                        pDialog.show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                        // called when response HTTP status is "200 OK"
                        //Toast.makeText(getApplicationContext(), new String(response),
                        //Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                        Log.e("response", new String(response));
                        //Toast.makeText(getActivity(), new String(response),
                                //Toast.LENGTH_SHORT).show();
                        try {
                            OutputStreamWriter os = new OutputStreamWriter(getActivity().openFileOutput(name + ".txt", Context.MODE_PRIVATE));
                            os.write(new String(response));
                            os.close();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), "Error. ",
                                Toast.LENGTH_SHORT).show();
                        Log.e("Failure", new String(errorResponse));
                        e.printStackTrace();
                    }

                    @Override
                    public void onRetry(int retryNo) {
                        Toast.makeText(getActivity(), "Retrying",
                                Toast.LENGTH_SHORT).show();
                        // called when request is retried
                    }
                });
                return true;
            }
        });
        return view;
    }

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
            //TODO SEE THIS
            mListener.onFragmentInteraction(Integer.toString(DummyContent.ITEMS.get(position).id));
        }
        Log.e("touched", position+"");

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        QuestionsFragment q=new QuestionsFragment();
        Bundle b=new Bundle();
        b.putString("topic",topicList.get(position).toString());
        q.setArguments(b);
        transaction.replace(((ViewGroup) (getView().getParent())).getId(), q);
        transaction.addToBackStack(null);
        
        // Commit the transaction
        transaction.commit();
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

package com.pes.gkl.gate.Fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.gc.materialdesign.views.Button;
import com.gc.materialdesign.views.Slider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pes.gkl.gate.Fragments.dummy.AnswersFragment;
import com.pes.gkl.gate.MainActivity;
import com.pes.gkl.gate.R;

import org.apache.http.Header;

import java.io.File;
import java.io.Serializable;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CustomTestFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CustomTestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomTestFragment extends Fragment {

    Slider slider1;
    Slider slider2;
    Slider slider3;
    Slider slider4;
    Slider slider5;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CustomTestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CustomTestFragment newInstance(String param1, String param2) {
        CustomTestFragment fragment = new CustomTestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CustomTestFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_test, container, false);


        slider1 = (Slider)view.findViewById(R.id.slider1);
        slider2 = (Slider)view.findViewById(R.id.slider2);
        slider3 = (Slider)view.findViewById(R.id.slider3);
        slider4 = (Slider)view.findViewById(R.id.slider4);
        slider5 = (Slider)view.findViewById(R.id.slider5);
        File f=getActivity().getFilesDir();
        File[] files=f.listFiles();
        for (File fill:files)
        {
           // Toast.makeText(getActivity(), fill.getName(), Toast.LENGTH_SHORT).show();
        }
        Button button = (Button) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int s1 = slider1.getValue();
                int s2 = slider2.getValue();
                int s3 = slider3.getValue();
                int s4 = slider4.getValue();
                int s5 = slider5.getValue();

                Log.e("slider sum", ""+s1+s2+s3+s4+s5);

                if((s1+s2+s3+s4+s5)==0)
                {
                    Toast.makeText(getActivity(), "Please select at least one topic", Toast.LENGTH_SHORT).show();
                    return;
                }

                //Register with the Server
                AsyncHttpClient client = new AsyncHttpClient();
                RequestParams params = new RequestParams();

                SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", getActivity().MODE_PRIVATE);
                String user=pref.getString("user", ":(");
                String pass=pref.getString("pass", "):");

                params.put("user_name", user);
                params.put("password", pass);

                params.put("1", s1);
                params.put("2", s2);
                params.put("3", s3);
                params.put("4", s4);
                params.put("5", s5);

                //Log.e("phno", phno);
                Log.e("pass", pass);

                final SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText("Fetching...");
                pDialog.setCancelable(true);


                client.post("http://karthikradhakrishnan96.pythonanywhere.com/Quiz/get_slider", params, new AsyncHttpResponseHandler() {

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
                        //Toast.makeText(getActivity(), new String(response),
                          //      Toast.LENGTH_SHORT).show();
                        pDialog.dismiss();
                        Log.e("response", new String(response));

                        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                        // Replace whatever is in the fragment_container view with this fragment,
                        // and add the transaction to the back stack so the user can navigate back
                        CustomQuestionsFragment q=new CustomQuestionsFragment();
                        Bundle b=new Bundle();
                        b.putString("response", new String(response));
                        q.setArguments(b);
                        transaction.replace(((ViewGroup) (getView().getParent())).getId(), q);
                        transaction.addToBackStack(null);

                        // Commit the transaction
                        transaction.commit();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                        // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                        pDialog.dismiss();
                        Toast.makeText(getActivity(), "Error. ",
                                Toast.LENGTH_SHORT).show();
                        if(errorResponse!=null) {
                            Toast.makeText(getActivity(), "Error. " + new String(errorResponse),
                                    Toast.LENGTH_SHORT).show();
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
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            //throw new ClassCastException(activity.toString()
              //      + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
        public void onFragmentInteraction(Uri uri);
    }


}

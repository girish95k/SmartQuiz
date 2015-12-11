package com.pes.gkl.gate.Fragments;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
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

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pes.gkl.gate.Fragments.dummy.DummyStatContent;
import com.pes.gkl.gate.R;

import com.pes.gkl.gate.Fragments.dummy.DummyContent;
import com.pes.gkl.gate.modelclasses.TestStat;
import com.pes.gkl.gate.modelclasses.Topic;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.pes.gkl.gate.serverutils.Parsers;

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
public class StatsFragment extends Fragment implements AbsListView.OnItemClickListener {

    RadarChart mChart;
    Typeface tf;
    protected HorizontalBarChart mChart2;
    ArrayList<TestStat> topicList;

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
    public static TestFragment newInstance(String param1, String param2) {
        TestFragment fragment = new TestFragment();
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
    public StatsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Register with the Server

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stats, container, false);

        /*
        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        */




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
        SharedPreferences pref = getActivity().getApplicationContext().getSharedPreferences("MyPref", getActivity().MODE_PRIVATE);
        String user=pref.getString("user", ":(");
        String pass=pref.getString("pass", "):");
        Toast.makeText(getActivity(), user,
                Toast.LENGTH_SHORT).show();
        params.put("json", "{\"username\": \""+user+"\", \"password\": \""+pass+"\"}");
        mChart = (RadarChart)view.findViewById(R.id.chart1);
        tf = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        mChart.setDescription("Topic-wise performance comparison");
        mChart2 = (HorizontalBarChart) view.findViewById(R.id.chart2);
        mChart2.setDrawValueAboveBar(true);

        mChart2.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart2.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart2.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        // mChart.setDrawXLabels(false);

        mChart2.setDrawGridBackground(false);
        XAxis xl = mChart2.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setTypeface(tf);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGridLineWidth(0.3f);

        YAxis yl = mChart2.getAxisLeft();
        yl.setTypeface(tf);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setGridLineWidth(0.3f);
//        yl.setInverted(true);

        YAxis yr = mChart2.getAxisRight();
        yr.setTypeface(tf);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        client.post("http://karthikradhakrishnan96.pythonanywhere.com/Quiz/get_stats", params, new AsyncHttpResponseHandler() {

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

                TestStat stat[] = new TestStat[0];
                try {
                    stat = Parsers.teststatparser(new String(response));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                topicList = new ArrayList<TestStat>(Arrays.asList(stat));

                System.out.println(topicList);

                DummyStatContent.ITEMS = topicList;

                mAdapter = new ArrayAdapter<TestStat>(getActivity(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, DummyStatContent.ITEMS);

                //mListView.setAdapter(mAdapter);
                //mListView.setBackgroundColor(Color.WHITE);

//        yr.setInverted(true);
                setData();
                mChart2.animateY(2500);
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
        mAdapter = new ArrayAdapter<TestStat>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, DummyStatContent.ITEMS);










        return view;
    }

    private String[] mParties = new String[] {
            "System Software and OS", "DBMS", "Data Structures", "Digital", "C.Arch"
    };

    public void setData() {

        float mult = 150;
        int cnt = 5;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        // IMPORTANT: In a PieChart, no values (Entry) should have the same
        // xIndex (even if from different DataSets), since no values can be
        // drawn above each other.
        for (int i = 0; i < cnt; i++) {
            yVals1.add(new Entry((float) (topicList.get(i).rating), i));
        }

        for (int i = 0; i < cnt; i++) {
            yVals2.add(new Entry((float) (topicList.get(i).average), i));
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < cnt; i++) {
            if(topicList.get(i).name.equals("System Software and Operating System"))
                xVals.add("OS");
            else xVals.add(generateInitials(topicList.get(i).name));
        }
        RadarDataSet set1 = new RadarDataSet(yVals1, "You");
        set1.setColor(ColorTemplate.VORDIPLOM_COLORS[0]);
        set1.setDrawFilled(true);
        set1.setLineWidth(2f);

        RadarDataSet set2 = new RadarDataSet(yVals2, "World Average");
        set2.setColor(ColorTemplate.VORDIPLOM_COLORS[4]);
        set2.setDrawFilled(true);
        set2.setLineWidth(2f);

        ArrayList<RadarDataSet> sets = new ArrayList<RadarDataSet>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(xVals, sets);
        data.setValueTypeface(tf);
        data.setValueTextSize(8f);
        data.setDrawValues(false);

        mChart.setData(data);

        mChart.invalidate();


        ArrayList<BarEntry> yVals3 = new ArrayList<BarEntry>();
        ArrayList<String> xVals2 = new ArrayList<String>();

        for (int i = 0; i < topicList.size(); i++) {
            if(topicList.get(i).name.equals("System Software and Operating System"))
                xVals2.add("OS");
            else xVals2.add(generateInitials(topicList.get(i).name));
            yVals3.add(new BarEntry((float) topicList.get(i).percentile, i));
        }

        BarDataSet set3 = new BarDataSet(yVals3, "Your Percentile for different topics");

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set3);

        BarData data3 = new BarData(xVals2, dataSets);
        data3.setValueTextSize(10f);
        data3.setValueTypeface(tf);

        mChart2.setData(data3);
    }
    public static String generateInitials ( String original ){
        String initial = "";
        String[] split = original.split(" ");

        for(String value : split){
            initial += value.substring(0,1);
        }

        return initial;
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
        /*
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back

        transaction.replace(((ViewGroup)(getView().getParent())).getId(), new QuestionsFragment());
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
        */
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

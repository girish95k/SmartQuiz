package com.pes.gkl.gate;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.pes.gkl.gate.Fragments.CustomTestFragment;
import com.pes.gkl.gate.Fragments.PerformanceFragment;
import com.pes.gkl.gate.Fragments.RandomQuestionFragment;
import com.pes.gkl.gate.Fragments.StatsFragment;
import com.pes.gkl.gate.Fragments.TestFragment;
import com.pes.gkl.gate.Fragments.TopicFragment;
import com.pes.gkl.gate.Fragments.dummy.StoredFragment;

import org.apache.http.Header;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.pedant.SweetAlert.SweetAlertDialog;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;
import it.neokree.materialnavigationdrawer.elements.MaterialSection;


public class MainActivity extends MaterialNavigationDrawer {

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    String user;
    String pass;

    MaterialAccount account;
    MaterialSection target;

    @Override
    public void init(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        user = prefs.getString("user", "not");//"No name defined" is the default value.
        pass = prefs.getString("pass", "not");
        account = new MaterialAccount(this.getResources(),user," ",R.drawable.giri3, R.drawable.gategarden);
        this.addAccount(account);

        //MaterialAccount account2 = new MaterialAccount(this.getResources(),"Hatsune Miky","hatsune.miku@example.com",R.drawable.photo2,R.drawable.mat2);
        //this.addAccount(account2);

        //MaterialAccount account3 = new MaterialAccount(this.getResources(),"Example","example@example.com",R.drawable.photo,R.drawable.mat3);
        //this.addAccount(account3);

        // create sections
        this.addSection(newSection("Custom Test", new CustomTestFragment()).setSectionColor(Color.parseColor("#1E88E5")));
        this.addSection(newSection("Topic-wise Questions",new TopicFragment()).setSectionColor(Color.RED));
        target = newSection("Stats",new StatsFragment()).setSectionColor(Color.parseColor("#9c27b0"));
        this.addSection(target);
        this.addSection(newSection("Topic-wise Performance", new PerformanceFragment()).setSectionColor(Color.GREEN));
        //this.addSection(newSection("Section",R.drawable.ic_arrow_drop_up_white_24dp,new TestFragment()).setSectionColor(Color.parseColor("#03a9f4")));
        this.addSection(newSection("Take a Saved Test",new StoredFragment()).setSectionColor(Color.BLACK));
        enableToolbarElevation();

        //thread.start();
    }


    private Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                submitOfflineResults();
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    removeAccount(account);
//                    notifyAccountDataChanged();
//                    removeSection(target);
                    //setSection(target);
                }
            });
        }
    });

    @Override
    public void onBackPressed() {
        /*
        if(getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else {
            getFragmentManager().popBackStack();
        }
        */
    }
    public boolean isResultFile(File f)
    {
        return f.getName().contains("ANSWER_FILE");
    }
    public void submitOfflineResults()
    {
        File fl=this.getFilesDir();
        File[] files=fl.listFiles();
        for (File f:files)
        {
            if(isResultFile(f))
            {
                //Need to submit
                SyncHttpClient client = new SyncHttpClient();
                RequestParams params = new RequestParams();
                SharedPreferences pref = this.getApplicationContext().getSharedPreferences("MyPref", this.MODE_PRIVATE);
                String user=pref.getString("user", ":(");
                String pass=pref.getString("pass", "):");
                params.put("credentials", "{\"username\": \""+user+"\", \"password\": \""+pass+"\"}");
                StringBuilder sb = new StringBuilder();
                try {
                    FileReader fr = new FileReader(f);
                    BufferedReader br = new BufferedReader(fr);

                    String data;
                    while ((data = br.readLine()) != null) {
                        sb.append(data);
                    }
                    Toast.makeText(this, "FILE : " + sb.toString(), Toast.LENGTH_SHORT).show();
                }
                catch(Exception e)
                {

                }
                params.put("results", sb.toString().replace("\"", ""));
                //SEND THE RESPONSE TO THE SERVER> PLEASE WRITE THIS PART
                //DELETE THE FILE IF SUCCESSFUL
                final String dir = this.getFilesDir().getAbsolutePath();
                final String name=f.getName();
                client.post("http://karthikradhakrishnan96.pythonanywhere.com/Quiz/save_results", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                        File f0 = new File(dir, name);
                        boolean d0 = f0.delete();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
                }

                }
        }
    }


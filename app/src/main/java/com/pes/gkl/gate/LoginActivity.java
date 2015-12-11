package com.pes.gkl.gate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pes.gkl.gate.Fragments.RandomQuestionFragment;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicReference;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class LoginActivity extends ActionBarActivity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    String user = "not";
    String pass = "not";
    boolean done = false;
    com.github.florent37.materialtextfield.MaterialTextField username;
    com.github.florent37.materialtextfield.MaterialTextField password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setTitle("Sign In");

        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        user = prefs.getString("user", "not");//"No name defined" is the default value.
        pass = prefs.getString("pass", "not");
        done = prefs.getBoolean("done", false);


        if(!user.equals("not"))
        {
            if(done) {
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }
            else {
                startActivity(new Intent(this, RandomQuestionFragment.class));
                finish();
            }
        }

        username = (com.github.florent37.materialtextfield.MaterialTextField)findViewById(R.id.username);
        password = (com.github.florent37.materialtextfield.MaterialTextField)findViewById(R.id.pass);

        //startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }

    public void submit(View view)
    {
        user = username.getEditText().getText().toString();
        pass = password.getEditText().getText().toString();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("user",user);
        editor.putString("pass",pass);
        editor.commit();
        if(user.equals("") || pass.equals(""))
        {
            Toast.makeText(LoginActivity.this, "Fields cannot be blank.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!user.equals("not")) {
            //Register with the Server
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();

            params.put("username", user);
            params.put("password", pass);
            //Log.e("phno", phno);
            Log.e("pass", pass);

            final SweetAlertDialog pDialog = new SweetAlertDialog(LoginActivity.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Logging In");
            pDialog.setCancelable(true);


            client.post("http://karthikradhakrishnan96.pythonanywhere.com/Quiz/login", params, new AsyncHttpResponseHandler() {

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
                    Toast.makeText(getApplicationContext(), new String(response),
                            Toast.LENGTH_SHORT).show();
                    pDialog.dismiss();
                    Log.e("response", new String(response));

                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("user", user);
                    editor.putString("pass", pass);
                    editor.putBoolean("done", true);
                    editor.commit();

                    startActivity(new Intent(LoginActivity.this, MainActivity.class));

                    finish();
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Error. ",
                            Toast.LENGTH_SHORT).show();
                    if(errorResponse!=null) {
                        Toast.makeText(getApplicationContext(), "Error. " + new String(errorResponse),
                                Toast.LENGTH_SHORT).show();
                        Log.e("Failure", new String(errorResponse));
                    }
                    e.printStackTrace();
                }

                @Override
                public void onRetry(int retryNo) {
                    Toast.makeText(getApplicationContext(), "Retrying",
                            Toast.LENGTH_SHORT).show();
                    // called when request is retried
                }
            });
        }
    }

    public void register(View view)
    {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

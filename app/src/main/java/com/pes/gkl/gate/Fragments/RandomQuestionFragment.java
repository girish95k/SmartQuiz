package com.pes.gkl.gate.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.pes.gkl.gate.Fragments.dummy.DummyContent;
import com.pes.gkl.gate.MainActivity;
import com.pes.gkl.gate.R;
import com.pes.gkl.gate.modelclasses.Question;
import com.pes.gkl.gate.modelclasses.Topic;
import com.pes.gkl.gate.serverutils.Parsers;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * A placeholder fragment containing a simple view.
 */
public class RandomQuestionFragment extends Activity {

    public static final String MY_PREFS_NAME = "MyPrefsFile";

    Button previous;
    Button next;
    Button submit;
    TextView questionName;
    TextView answeredMessage;
    Button optionA;
    Button optionB;
    Button optionC;
    Button optionD;
    Button reset;
    View rootView;
    Question[] questions;
    int currentIndex;
    public RandomQuestionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_questions);

        //Register with the Server
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        //params.put("topic_name", "Algorithms");

        /*
        params.put("controller", "customer");
        params.put("action", "getverified");
        params.put("phno", hid);
        params.put("pass", pass);
        //Log.e("phno", phno);
        Log.e("pass", pass);
        */

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Logging In");
        pDialog.setCancelable(false);


        client.get("http://karthikradhakrishnan96.pythonanywhere.com/Quiz/calibration", params, new AsyncHttpResponseHandler() {

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

                try {
                    questions = Parsers.questionlistparser(new String(response));
                    currentIndex = 0;
                    questionName = (TextView) findViewById(R.id.QuestionName);
                    answeredMessage = (TextView) findViewById(R.id.Answered);
                    optionA = (Button)findViewById(R.id.optionA);
                    optionB = (Button) findViewById(R.id.optionB);
                    optionC = (Button) findViewById(R.id.optionC);
                    optionD = (Button) findViewById(R.id.optionD);
                    previous = (Button) findViewById(R.id.prev);
                    previous.setText("PREV");
                    submit = (Button) findViewById(R.id.submit);
                    next = (Button) findViewById(R.id.next);
                    next.setText("NEXT");
                    reset = (Button) findViewById(R.id.reset);
                    setQuestion();
                    previous.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            if (currentIndex > 0) {
                                currentIndex -= 1;
                                setQuestion();
                            } else {
                                currentIndex = questions.length - 1;
                                setQuestion();
                            }
                            //Toast.makeText(getActivity(), "No previous question", Toast.LENGTH_SHORT).show();
                        }
                    });
                    next.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            if (currentIndex < questions.length - 1) {
                                currentIndex += 1;
                                setQuestion();
                            } else {
                                currentIndex = 0;
                                setQuestion();
                            }
                        }
                    });
                    //What color should I put if user answers?

                    optionA.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            questions[currentIndex].userAnswer = 1;
                            answeredMessage.setText("You have answered " + 1);

                        }
                    });
                    optionB.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            questions[currentIndex].userAnswer = 2;
                            answeredMessage.setText("You have answered " + 2);
                        }
                    });
                    optionC.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            questions[currentIndex].userAnswer = 3;
                            answeredMessage.setText("You have answered " + 3);
                        }
                    });
                    optionD.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            questions[currentIndex].userAnswer = 4;
                            answeredMessage.setText("You have answered " + 4);
                        }
                    });

                    reset.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            questions[currentIndex].userAnswer = -1;
                            answeredMessage.setText("You have not answered yet");
                        }
                    });

                    submit.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            //Toast.makeText(getActivity(), "Submit button clicked. Add network call ",
                            //      Toast.LENGTH_SHORT).show();
                            JSONObject results = new JSONObject();
                            for (int i = 0; i < questions.length; i++) {
                                int correct = 0;
                                if (questions[i].answer == questions[i].userAnswer) correct = 1;
                                try {
                                    results.put(Integer.toString(questions[i].id), Integer.toString(correct));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            Toast.makeText(RandomQuestionFragment.this, results.toString(),
                                    Toast.LENGTH_SHORT).show();
                            AsyncHttpClient client = new AsyncHttpClient();
                            RequestParams params = new RequestParams();

        /*
        params.put("controller", "customer");
        params.put("action", "getverified");
        params.put("phno", hid);
        params.put("pass", pass);
        //Log.e("phno", phno);
        Log.e("pass", pass);
        */                  //TODO Enter the right credentials
                            params.put("credentials", "{\"username\": \"kai\", \"password\": \"password\"}");
                            params.put("results", results.toString().replace("\"", ""));
                            final SweetAlertDialog pDialog = new SweetAlertDialog(RandomQuestionFragment.this, SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("Submitting details");
                            pDialog.setCancelable(false);


                            client.post("http://karthikradhakrishnan96.pythonanywhere.com/Quiz/save_results", params, new AsyncHttpResponseHandler() {

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
                                    Toast.makeText(RandomQuestionFragment.this, new String(response),
                                            Toast.LENGTH_SHORT).show();
                                    pDialog.cancel();
                                    //TODO Take to results page
                                    SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                    editor.putBoolean("done", true);
                                    editor.commit();
                                    startActivity(new Intent(RandomQuestionFragment.this, MainActivity.class));
                                    finish();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                                    // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                                    pDialog.dismiss();
                                    Toast.makeText(RandomQuestionFragment.this, "Error. ",
                                            Toast.LENGTH_SHORT).show();
                                    if(errorResponse!=null)
                                        Log.e("Failure", new String(errorResponse));
                                    e.printStackTrace();
                                }

                                @Override
                                public void onRetry(int retryNo) {
                                    Toast.makeText(RandomQuestionFragment.this, "Retrying",
                                            Toast.LENGTH_SHORT).show();
                                    // called when request is retried
                                }
                            });


                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
                //TODO Send the results to the server .

                //questionList = new ArrayList<Topic>(Arrays.asList(question));

                //System.out.println(questionList);

                //DummyContent.ITEMS = topicList;

                //mAdapter = new ArrayAdapter<Topic>(getActivity(),
                //android.R.layout.simple_list_item_1, android.R.id.text1, DummyContent.ITEMS);

                //mListView.setAdapter(mAdapter);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                pDialog.dismiss();
                Toast.makeText(RandomQuestionFragment.this, "Error. ",
                        Toast.LENGTH_SHORT).show();
                Log.e("Failure", new String(errorResponse));
                e.printStackTrace();
            }

            @Override
            public void onRetry(int retryNo) {
                Toast.makeText(RandomQuestionFragment.this, "Retrying",
                        Toast.LENGTH_SHORT).show();
                // called when request is retried
            }
        });
        // TODO Auto-generated method stub
        /*
        questions = new Question[5];
        for (int i = 0; i < 5; i++) {
            questions[i] = new Question("NAME", i, i, "A", "B", "C", "D", i, "K");
        }
        */


        /*
        btn_service = (Button) findViewById(R.id.btn_service);
        btn_service.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                //your Code Goes Here
            }
        });
        */
        //return rootView;
    }
    public void setQuestion()
    {
        Question currentQuestion=questions[currentIndex];
        questionName.setText(currentQuestion.name);
        optionA.setText(currentQuestion.optionA);
        optionB.setText(currentQuestion.optionB);
        optionC.setText(currentQuestion.optionC);
        optionD.setText(currentQuestion.optionD);
        if(currentQuestion.userAnswer==-1)
            answeredMessage.setText("You have not answered yet");
        else
            answeredMessage.setText("You have answered "+currentQuestion.userAnswer);
    }


}

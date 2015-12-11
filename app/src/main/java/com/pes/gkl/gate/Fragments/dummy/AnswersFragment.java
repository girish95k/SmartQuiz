package com.pes.gkl.gate.Fragments.dummy;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.pes.gkl.gate.Fragments.CustomTestFragment;
import com.pes.gkl.gate.R;
import com.pes.gkl.gate.modelclasses.Question;
import com.pes.gkl.gate.serverutils.Parsers;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnswersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnswersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnswersFragment extends Fragment {
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
     * @return A new instance of fragment AnswersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnswersFragment newInstance(String param1, String param2) {
        AnswersFragment fragment = new AnswersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AnswersFragment() {
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

    Button previous;
    Button next;
    Button submit;
    TextView questionName;
    TextView answeredMessage;
    TextView solution;
    Button optionA;
    Button optionB;
    Button optionC;
    Button optionD;
    Button reset;
    View rootView;
    Question[] questions;
    int currentIndex;
    TextView stat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        rootView = inflater.inflate(R.layout.fragment_answers, container,
                false);

        Bundle b=this.getArguments();
        Object[] response=(Object[])b.getSerializable("response");
        questions = new Question[response.length];
        for(int i = 0; i < response.length; i++) {
            questions[i] = (Question)response[i];
        }

        currentIndex =0;
        questionName = (TextView) rootView.findViewById(R.id.QuestionName);
        answeredMessage = (TextView) rootView.findViewById(R.id.Answered);
        solution = (TextView) rootView.findViewById(R.id.solution);
        solution.setVisibility(View.GONE);
        optionA = (Button) rootView.findViewById(R.id.optionA);
        optionB = (Button) rootView.findViewById(R.id.optionB);
        optionC = (Button) rootView.findViewById(R.id.optionC);
        optionD = (Button) rootView.findViewById(R.id.optionD);
        previous = (Button) rootView.findViewById(R.id.prev);
        stat=(TextView)rootView.findViewById(R.id.textViewstat);
        previous.setText("PREV");
        submit = (Button) rootView.findViewById(R.id.submit);
        //submit.setVisibility(View.GONE);
        next = (Button) rootView.findViewById(R.id.next);
        next.setText("NEXT");
        reset=(Button)rootView.findViewById(R.id.reset);
        reset.setVisibility(View.GONE);
        setQuestion();
        previous.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (currentIndex > 0) {
                    currentIndex -= 1;
                    setQuestion();
                } else
                {
                    currentIndex=questions.length-1;
                    setQuestion();
                }
                //Toast.makeText(getActivity(), "No previous question", Toast.LENGTH_SHORT).show();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (currentIndex < questions.length-1) {
                    currentIndex+=1;
                    setQuestion();
                } else
                {
                    currentIndex=0;
                    setQuestion();
                }
            }
        });
        //What color should I put if user answers?

        optionA.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //questions[currentIndex].userAnswer = 1;
                //answeredMessage.setText("You have answered "+1);

            }
        });
        optionB.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //questions[currentIndex].userAnswer = 2;
                //answeredMessage.setText("You have answered "+2);
            }
        });
        optionC.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //questions[currentIndex].userAnswer = 3;
                //answeredMessage.setText("You have answered "+3);
            }
        });
        optionD.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //questions[currentIndex].userAnswer = 4;
                //answeredMessage.setText("You have answered "+4);
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                //questions[currentIndex].userAnswer = -1;
                //answeredMessage.setText("You have not answered yet");
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                /*
                Toast.makeText(getActivity(), "Submit button clicked. Add network call ",
                        Toast.LENGTH_SHORT).show();
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
                */
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                StoredFragment q=new StoredFragment();
                transaction.replace(((ViewGroup) (getView().getParent())).getId(), q);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }

        });

        return rootView;
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

            public void setQuestion()
            {
                Question currentQuestion=questions[currentIndex];
                questionName.setText(currentQuestion.name);
                solution.setText(currentQuestion.solution);
                optionA.setText(currentQuestion.optionA);
                optionB.setText(currentQuestion.optionB);
                optionC.setText(currentQuestion.optionC);
                optionD.setText(currentQuestion.optionD);
                stat.setText("     Rating "+currentQuestion.rating);
                optionA.setBackgroundResource(android.R.drawable.btn_default);
                optionB.setBackgroundResource(android.R.drawable.btn_default);
                optionC.setBackgroundResource(android.R.drawable.btn_default);
                optionD.setBackgroundResource(android.R.drawable.btn_default);

                if(currentQuestion.userAnswer==1)
                    optionA.setBackgroundColor(Color.RED);
                if(currentQuestion.userAnswer==2)
                    optionB.setBackgroundColor(Color.RED);
                if(currentQuestion.userAnswer==3)
                    optionC.setBackgroundColor(Color.RED);
                if(currentQuestion.userAnswer==4)
                    optionD.setBackgroundColor(Color.RED);

                if(currentQuestion.answer==1)
                    optionA.setBackgroundColor(Color.GREEN);
                if(currentQuestion.answer==2)
                    optionB.setBackgroundColor(Color.GREEN);
                if(currentQuestion.answer==3)
                    optionC.setBackgroundColor(Color.GREEN);
                if(currentQuestion.answer==4)
                    optionD.setBackgroundColor(Color.GREEN);

                if(currentQuestion.userAnswer==-1)
                    answeredMessage.setText("You have not answered yet");
                else
                    answeredMessage.setText("You have answered "+currentQuestion.userAnswer);
                answeredMessage.setText(currentQuestion.solution);
            }

}

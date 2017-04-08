package com.example.katecatlin.diversityapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.katecatlin.diversityapp.R;
import com.example.katecatlin.diversityapp.models.Datum;
import com.example.katecatlin.diversityapp.models.QuestionFlow;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import it.slyce.messaging.SlyceMessagingFragment;
import it.slyce.messaging.listeners.UserSendsMessageListener;
import it.slyce.messaging.message.MessageSource;
import it.slyce.messaging.message.TextMessage;

/**
 * Created by katecatlin on 4/8/17.
 */

public class ChatActivity extends AppCompatActivity implements UserSendsMessageListener {

    private Datum currentQuestion;

    private SlyceMessagingFragment messagingFragment;

    private List<Datum> questions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        messagingFragment = (SlyceMessagingFragment) getFragmentManager().findFragmentById(R.id.messaging_fragment);
        messagingFragment.setOnSendMessageListener(this);

        try {
            final InputStream inputStream = getAssets().open("question_flow.json");
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            questions = new Gson().fromJson(reader, QuestionFlow.class).getData();
        } catch (Exception e) {
        }

        updateCurrentQuestion();
        askCurrentQuestion();
    }

    private void askCurrentQuestion() {
//        if (currentQuestion.getResponse().getType().equals("user-entry")) {
            final TextMessage currentMessage = new TextMessage();
            currentMessage.setText(currentQuestion.getPrompt());
            currentMessage.setSource(MessageSource.EXTERNAL_USER);
            messagingFragment.addNewMessage(currentMessage);
//        } else if (currentQuestion.getResponse().getType().equals("choice")) {
//            final GeneralOptionsMessage
//        }
    }

    /**
     * check that areThereMoreQuestions returns true before calling me
     */
    private void updateCurrentQuestion() {
        currentQuestion = questions.get(0);
        questions.remove(0);
    }

    private boolean areThereMoreQuestions() {
        return !questions.isEmpty();
    }

    private void advanceToStats() {
        Intent intent = new Intent(this, StatActivity.class);
        startActivity(intent);
    }

    // UserSendsMessageListener

    @Override
    public void onUserSendsTextMessage(final String text) {
        if (areThereMoreQuestions()) {
            updateCurrentQuestion();
            askCurrentQuestion();
        } else {
            advanceToStats();
        }
    }

    @Override
    public void onUserSendsMediaMessage(final Uri imageUri) {}

}

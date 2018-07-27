package com.example.chris.mysqliteproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button addButton;
    Button removeButton;
    EditText inputEditText;
    TextView resultsTextView;
    MyDBHandler dbHandler;
    Button secondActivityButton;
    TextView welcomeTextView;

    TextView previouslyInitTextView;
    Button previouslyInitButton;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String FIRST_NAME = "firstName";

    public static ArrayList<Entry> entries = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("BudgetBuddy\\MainActivity");

        //region referencesToIds

        removeButton = (Button) findViewById(R.id.removeButton);
        inputEditText = (EditText) findViewById(R.id.amountEditText);
        resultsTextView = (TextView) findViewById(R.id.resultsTextView);
        dbHandler = new MyDBHandler(this, null, null, 1);
        secondActivityButton = (Button) findViewById(R.id.secondActivityButton);
        previouslyInitTextView = (TextView) findViewById(R.id.previouslyInitTextView);
        previouslyInitButton = (Button) findViewById(R.id.previouslyInitButton);
        welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
        //endregion

        //print the info in the SQLite table upon creating activity
        printDatabase();

        //region SharedPreferences
        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        String firstName = sharedPreferences.getString(FIRST_NAME, "unnamed");
        welcomeTextView.setText("Welcome, " + firstName + ".");

        boolean value = sharedPreferences.getBoolean(TEXT, false);
        if (value){
            previouslyInitButton.setEnabled(false);
            previouslyInitTextView.setText("The button has been previously pressed");
        }

        previouslyInitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putBoolean(TEXT, true);
                editor.apply();
                previouslyInitTextView.setText("The button has been previously pressed");
                previouslyInitButton.setEnabled(false);
            }
        });

        //endregion

        //region Button Onclicks

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler.dequeue();
                printDatabase();
            }
        });

        secondActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), SecondActivity.class);
                startIntent.putExtra("com.example.chris.mysqliteproject.INFO", "This information was passed from the first activity.");
                startActivity(startIntent);
            }
        });

        //endregion

    } // end of onCreate

    public void printDatabase(){
        String dbString = dbHandler.databaseToString();
        resultsTextView.setText(dbString);
        inputEditText.setText("");
    }

}

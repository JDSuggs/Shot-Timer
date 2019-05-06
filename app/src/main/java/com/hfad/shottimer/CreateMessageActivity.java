package com.hfad.shottimer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateMessageActivity extends Activity {

//    public static final String CSV = "";
//    public String csv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message);
    }

    //Call onSendMessage() when the button is clicked
    public void onSendMessage(View view) {
        String csv = getIntent().getExtras().getString("CSV");
        String csv1 = getIntent().getExtras().getString("CSV1");
        EditText messageView = findViewById(R.id.message);
        String messageText = messageView.getText().toString();
        String csvFinal = csv1 + messageText + "," + csv;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, csvFinal);
        String chooserTitle = getString(R.string.chooser);
        Intent chosenIntent = Intent.createChooser(intent, chooserTitle);
        startActivity(chosenIntent);
    }
}
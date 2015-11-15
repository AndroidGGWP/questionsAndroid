package hk.ust.cse.hunkim.questionroom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;
import android.widget.Button;
import android.view.View.OnClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class SearchActivity extends AppCompatActivity {
    private Button btnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Spinner StartTime_spinner = (Spinner) findViewById(R.id.StartTime_spinner);
        Spinner EndTime_spinner = (Spinner)  findViewById(R.id.EndTime_spinner);
        StartTime_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        EndTime_spinner.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        addListenerOnButton();
    }

    public class CustomOnItemSelectedListener implements OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
            Toast.makeText(parent.getContext(),
                    "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub
        }

    }

public void addListenerOnButton() {

    final Spinner StartTime_spinner = (Spinner) findViewById(R.id.StartTime_spinner);
    final Spinner EndTime_spinner = (Spinner) findViewById(R.id.EndTime_spinner);
    final EditText SearchContent=(EditText)    findViewById(R.id.ContentFilter);
    btnSubmit = (Button) findViewById(R.id.btnSubmit);

    btnSubmit.setOnClickListener(new OnClickListener() {

        @Override
        public void onClick(View v) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            String Time1  = dateFormat.format(new Date(System.currentTimeMillis()));
            Toast.makeText(SearchActivity.this,
                    "OnClickListener : " +
                            "\nStart Time : "+ String.valueOf(StartTime_spinner.getSelectedItem()) +
                            "\nEnd Time : "+ String.valueOf(EndTime_spinner.getSelectedItem())+
                            "\nSearch Content : "+SearchContent.getText().toString()+
                            "\nCurrent Time is :"+Time1,
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("StartTime", String.valueOf(StartTime_spinner.getSelectedItem()));
            intent.putExtra("EndTime", String.valueOf(EndTime_spinner.getSelectedItem()));
            intent.putExtra("Content", SearchContent.getText().toString());
            SearchActivity.this.setResult(RESULT_OK, intent);
            SearchActivity.this.finish();
        }

    });
}

}

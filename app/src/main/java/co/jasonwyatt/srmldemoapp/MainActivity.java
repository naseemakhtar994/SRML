package co.jasonwyatt.srmldemoapp;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import co.jasonwyatt.srml.SRML;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (v instanceof TextView) {
                    ((TextView) v).setMovementMethod(LinkMovementMethod.getInstance());
                }
                return v;
            }
        };

        // Only doing this in the background because the string array contains an {{image}} tag with
        // a url, and we use picasso for image fetching, which balks when you try to load an image
        // on the main thread.
        new AsyncTask<Void, Void, CharSequence[]>() {
            @Override
            protected void onPostExecute(CharSequence[] charSequences) {
                adapter.addAll(charSequences);
            }

            @Override
            protected CharSequence[] doInBackground(Void... params) {
                return SRML.getStringArray(MainActivity.this, R.array.test_strings);
            }
        }.execute();
        mListView.setAdapter(adapter);
    }
}

package ttuananhle.android.tokenautocomplete;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.tokenautocomplete.FilteredArrayAdapter;
import com.tokenautocomplete.TokenCompleteTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements TokenCompleteTextView.TokenListener {


    ContactsCompletionView completionView;
    List<Person> personList;
    ArrayAdapter<Person> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        personList = new ArrayList<>();
        personList.add(new Person("Marshall Weir", "marshall@example.com"));
        personList.add( new Person("Margaret Smith", "margaret@example.com"));
        personList.add( new Person("Max Jordan", "max@example.com"));
        personList.add(new Person("Meg Peterson", "meg@example.com"));
        personList.add(new Person("Amanda Johnson", "amanda@example.com"));

        adapter = new FilteredArrayAdapter<Person>(this, android.R.layout.simple_list_item_1, personList) {
            @Override
            protected boolean keepObject(Person obj, String mask) {
                mask = mask.toLowerCase();
                return obj.getName().toLowerCase().startsWith(mask) || obj.getEmail().toLowerCase().startsWith(mask);
            }
        };

        completionView = (ContactsCompletionView)findViewById(R.id.searchView);
        completionView.setAdapter(adapter);

        completionView.setTokenListener(this);
    }

    @Override
    public void onTokenAdded(Object token) {
        Log.i("Token",  "added " + token);
    }

    @Override
    public void onTokenRemoved(Object token) {
        Log.i("Token", "Removed: " + token);
    }
}

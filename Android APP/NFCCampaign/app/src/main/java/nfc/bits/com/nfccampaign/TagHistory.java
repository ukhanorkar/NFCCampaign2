package nfc.bits.com.nfccampaign;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import nfc.bits.com.model.Vendor;


public class TagHistory extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_history);
        ListView listView = (ListView) findViewById(R.id.listView);
        Bundle extras = getIntent().getExtras();
        List<Vendor> vendorList = null;
        if (extras != null) {
          vendorList  = (List<Vendor>) extras.getSerializable("vendor_list");
        }
        if (vendorList.size()==0){
            Toast.makeText(this, "No vendor registered!!!", Toast.LENGTH_LONG).show();
        }else {
            ArrayAdapter<Vendor> adapter = new ArrayAdapter<Vendor>(this,
                    android.R.layout.simple_list_item_1, vendorList);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {

                    String item = ((TextView)view).getText().toString();

                    Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

                }
            });
        }

        System.out.println(vendorList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tag_history, menu);
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

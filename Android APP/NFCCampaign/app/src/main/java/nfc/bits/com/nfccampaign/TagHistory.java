package nfc.bits.com.nfccampaign;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import nfc.bits.com.model.Vendor;
import nfc.bits.com.model.VendorLocation;


public class TagHistory extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_history);
        ListView listView = (ListView) findViewById(R.id.listView);
        Bundle extras = getIntent().getExtras();
        final List<Vendor> vendorList = (List<Vendor>) extras.getSerializable("vendor_list");
        /*if (extras != null) {
          vendorList  = (List<Vendor>) extras.getSerializable("vendor_list");
        }*/
        if (vendorList.size()==0){
            Toast.makeText(this, "No vendor registered!!!", Toast.LENGTH_LONG).show();
        }else {
            ArrayAdapter<Vendor> adapter = new ArrayAdapter<Vendor>(this,
                    android.R.layout.simple_list_item_1, vendorList);/*{

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                   *//* if(convertView ==null){
                        LayoutInflater vi = getLayoutInflater();
                        convertView = vi.inflate(R.layout.activity_tag_history, null);
                    }
                    Vendor vendors;
                    vendors = vendorList.get(position);
                    List<VendorLocation> vendorLocations = vendors.getVendorLocations();
                    convertView.setTag(vendorLocations);*//*
                    return convertView;
                };

            };*/
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position,
                                        long id) {
                    Vendor vendors;
                    vendors = vendorList.get(position);
                    List<VendorLocation> vendorLocations = vendors.getVendorLocations();
                    String item = ((TextView)view).getText().toString();
                   // List<VendorLocation> vendorLocations = (List<VendorLocation>) view.getTag();
                    Intent detailIntent = new Intent(TagHistory.this, VendorLocationDetails.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("vendor_detail_list", (java.io.Serializable) vendorLocations);
                    detailIntent.putExtras(bundle);
                    startActivity(detailIntent);

                    //Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();

                }
            });
        }

        //System.out.println(vendorList);
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

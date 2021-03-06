package nfc.bits.com.nfccampaign;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nfc.bits.com.model.User;
import nfc.bits.com.model.Vendor;


public class NFCReader extends ActionBarActivity {

    private NfcAdapter mNfcAdapter;
    public static final String MIME_TEXT_PLAIN = "text/plain";
    public String NFC_PREF = "NFCPreferences";
    String userId;
    private static final String USER_ID = "UserId";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcreader);

        SharedPreferences sharedPreferences = getSharedPreferences(NFC_PREF, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(USER_ID, "notSet");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // NFC is not supported
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            return;

        }

        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC is Disabled!!", Toast.LENGTH_LONG).show();
        }

       handleIntent(getIntent());
    }


    @Override
    protected void onResume() {
        super.onResume();

        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        handleIntent(intent);
    }

    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    /**
     * @param activity The corresponding {@link Activity} requesting to stop the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nfcreader, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_tag_history) {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://env-2178813.ind-cloud.everdata.com/nfc/vendorservice/getUserVendorHistory/" + userId);
            try {
                HttpResponse response = httpClient.execute(httpGet);
                HttpEntity entity = response.getEntity();
                if (response.getStatusLine().getStatusCode()==200) {
                    String resp_body = EntityUtils.toString(response.getEntity());
                    Log.v("resp_body", resp_body.toString());
                    Gson gson = new Gson();
                    List<Vendor> vendorList = gson.fromJson(resp_body, new TypeToken<ArrayList<Vendor>>() {
                    }.getType());

                    Intent historyIntent = new Intent(NFCReader.this, TagHistory.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("vendor_list", (java.io.Serializable) vendorList);
                    historyIntent.putExtras(bundle);
                    startActivity(historyIntent);
                }
                else{
                    Toast.makeText(this,response.getStatusLine().getStatusCode(), Toast.LENGTH_SHORT ).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Unable to show the Tag History.", Toast.LENGTH_SHORT).show();
                Log.e("Failed", "Failed***************");
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        String vendorId;
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                Ndef ndef = Ndef.get(tag);
                if (ndef == null) {
                    // NDEF is not supported by this Tag.
                    //return null;
                    Log.e("TAG", "NDEF is not supported by this Tag.");
                }

                NdefMessage ndefMessage = ndef.getCachedNdefMessage();

                NdefRecord[] records = ndefMessage.getRecords();
                for (NdefRecord ndefRecord : records) {
                    if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT))
                        try {
                            vendorId = readText(ndefRecord);
                            subscribeVendor(vendorId);
                            Log.e("TAG", vendorId);
                        } catch (UnsupportedEncodingException e) {
                            Log.e("TAG", "Unsupported Encoding", e);
                        }
                }

            } else {
                Log.d("TAG", "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    break;
                }
            }
        }
    }

    private void subscribeVendor(String vendorId) {

        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://env-2178813.ind-cloud.everdata.com/nfc/vendorservice/saveUserVendorHistory/" + userId + "/"+ vendorId);
        try {
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (response.getStatusLine().getStatusCode()==204) {
                Toast.makeText(this,"Congratulations! You have subscribed successfully!", Toast.LENGTH_SHORT ).show();
            }
            else{
                Toast.makeText(this,response.getStatusLine().getStatusCode()+"Error while subscribing for this vendor", Toast.LENGTH_SHORT ).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Network Error while subscribing for this vendor", Toast.LENGTH_SHORT).show();
            Log.e("Failed", "Failed***************");
        }
    }


    private String readText(NdefRecord record) throws UnsupportedEncodingException {
        byte[] payload = record.getPayload();

        // Get the Text Encoding
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

        // Get the Language Code
        int languageCodeLength = payload[0] & 0063;

        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
        // e.g. "en"

        // Get the Text
        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
    }



}

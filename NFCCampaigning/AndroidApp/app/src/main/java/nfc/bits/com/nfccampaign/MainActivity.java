package nfc.bits.com.nfccampaign;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import nfc.bits.com.model.User;


public class MainActivity extends ActionBarActivity {

    private final String NFC_PREF = "NFCPreferences";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = getSharedPreferences(NFC_PREF, Context.MODE_PRIVATE);
        String userName = sharedPreferences.getString("UserName", "notSet");
        if (userName.equalsIgnoreCase("notSet")){
            //boolean isRegistered = isUserRegistered(user);
            setContentView(R.layout.activity_main);
            final Button button = (Button) findViewById(R.id.registerButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final EditText email = (EditText) findViewById(R.id.email);
                    final EditText phone = (EditText) findViewById(R.id.contactNumber);
                    if(email.getText().toString()!= "" && phone.getText().toString() != ""){
                        User user =  new User();
                        user.setContactNumber(Long.valueOf(phone.getText().toString()));
                        user.setEmail(email.getText().toString());
                        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                        String deviceId = telephonyManager.getDeviceId();
                        user.setDeviceId(deviceId);
                        String registrationNumber = getGCMRegistrationNumber();
                        new RegisterUser().execute();
                    }else {
                        Toast.makeText(getApplicationContext(), "Please Enter Email/Contact Number", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else{
           // setContentView(R.layout.nfc_reader);
        }
    }

    private String getGCMRegistrationNumber(){

        return null;
    }

    private boolean isUserRegistered(User user) {

        return false;

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private class RegisterUser extends AsyncTask<User, Void, String> {

        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n>0) {
                byte[] b = new byte[4096];
                n =  in.read(b);
                if (n>0) out.append(new String(b, 0, n));
            }
            return out.toString();
        }

        @Override
        protected String doInBackground(User... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            JSONObject userData = new JSONObject(params);
            HttpPost httpPost = new HttpPost("http://localHost:8080/userdaoservice/registerUser");
            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpPost, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
            return text;
        }

        protected void onPostExecute(String results) {
            if (results!=null) {
                Toast.makeText(getApplicationContext(), "Registration Successful!!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

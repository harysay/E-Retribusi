package id.go.kebumenkab.retribusipasar;

import androidx.appcompat.app.AppCompatActivity;
import id.go.kebumenkab.retribusipasar.handler.HttpHandler;
import id.go.kebumenkab.retribusipasar.handler.Server;
import id.go.kebumenkab.retribusipasar.handler.SessionManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity {
    SessionManager session;
    EditText username, passkode;
    Button btnMasuk;
    String inputanUserName, inputanPassKode;
//    private UserLoginTask mAuthTask;
    private String nilaiKembalian;
    String stat, tokenlog, usr,msgLogin;
    ProgressBar mProgressAuth;
    private ProgressDialog progressBar;
    String returnTokenNik,returnTokenStatus,returnTokenCode;
    public static final String TAG = LoginActivity.class.getSimpleName();
    private CheckBox showPass;
    String namaPegawai, gelarBelakang,idEselon,urlImage,jabatanku;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        session = new SessionManager(getApplicationContext());
        btnMasuk = (Button) findViewById(R.id.btnMasuk);
        username = (EditText) findViewById(R.id.username);
        passkode = (EditText) findViewById(R.id.password);
        showPass = findViewById(R.id.showPass);
        mProgressAuth = (ProgressBar) findViewById(R.id.login_progress);
        //Set onClickListener, untuk menangani kejadian saat Checkbox diklik
        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showPass.isChecked()){
                    //Saat Checkbox dalam keadaan Checked, maka password akan di tampilkan
                    passkode.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    //Jika tidak, maka password akan di sembuyikan
                    passkode.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        btnMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputanUserName = username.getText().toString();
                inputanPassKode = passkode.getText().toString();
                loginAuth(view,inputanUserName, inputanPassKode);
//                attemptLogin();
//                // Reset errors.
//                username.setError(null);
//                passkode.setError(null);
//                boolean cancel = false;
//                View focusView = null;
//
//                // Check for a valid password, if the user entered one.
//                if (TextUtils.isEmpty(inputanPassKode) && !isPasswordValid(inputanPassKode)) {
//                    passkode.setError("Password tidak boleh kosong atau terlalu pendek");
//                    focusView = passkode;
//                    cancel = true;
//                }
//
//                // Check for a valid ID.
//                if (TextUtils.isEmpty(inputanUserName)) {
//                    username.setError("Harus terisi");
//                    focusView = username;
//                    cancel = true;
//                }
//
//                if (cancel) {
//                    // There was an error; don't attempt login and focus the first
//                    // form field with an error.
//                    focusView.requestFocus();
//                } else {
//                    loginAuth(view,inputanUserName, inputanPassKode);
//                }

            }
        });
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
    }

    private void loginAuth(View viewku,String usrName, String passCode) {

        progressBar = new ProgressDialog(viewku.getContext());//Create new object of progress bar type
        progressBar.setCancelable(false);//Progress bar cannot be cancelled by pressing any wher on screen
        progressBar.setMessage("Verifikasi user,...");//Title shown in the progress bar
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);//Style of the progress bar
        progressBar.setProgress(0);//attributes
        progressBar.setMax(100);//attributes
        progressBar.show();//show the progress bar
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Server.URL+"login";
        JSONObject jsonBody = new JSONObject();
        final String requestBody = jsonBody.toString();
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    //menaruh data JSON ke dalam variabel JSON Object
                    JSONObject jsonPost = new JSONObject(response.toString());
                    stat = jsonPost.getString("status");
                    tokenlog = jsonPost.getString("token");
                    //usr = jsonPost.getString("user");
                    msgLogin = jsonPost.getString("message");

                    JSONObject objUser = jsonPost.getJSONObject("user");
                    String usrID = objUser.getString("user_id");
                    String usrName = objUser.getString("user_name");
                    String usrReal = objUser.getString("user_realname");
                    String usrGroup = objUser.getString("user_group");
                    String usrStatus = objUser.getString("user_st");
                    String usrPhoto = objUser.getString("user_photo");
                    String usrLasLogin = objUser.getString("last_login");
                    String usrSubGroup = objUser.getString("sub_group");

                    Log.e("coba", jsonPost.toString());

                    if(stat.equals("success")){
                        progressBar.cancel();//Progress bar will be cancelled (hide from screen) when this run function will execute after 3.5seconds
//                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
//                        finish();
                        session.createLoginSession(usrID,usrName,usrReal,usrGroup,usrStatus,usrPhoto,usrLasLogin, usrSubGroup,tokenlog);
                        Intent masuk = new Intent(LoginActivity.this, MainActivity.class);
//                        masuk.putExtra("userReal", usrReal);
                        startActivity(masuk);
                        finish();
                    }else {
                        progressBar.cancel();//Progress bar will be cancelled (hide from screen) when this run function will execute after 3.5seconds
                        Toast.makeText(LoginActivity.this, msgLogin, Toast.LENGTH_SHORT).show();
//                        finish();
//                        startActivity(getIntent());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar.cancel();//Progress bar will be cancelled (hide from screen) when this run function will execute after 3.5seconds
                    Log.e("coba", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error Response", error.toString());
                progressBar.cancel();//Progress bar will be cancelled (hide from screen) when this run function will execute after 3.5seconds
                Toast.makeText(LoginActivity.this, "Kesalahan server silahkan hubungi admin", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
//                String user =mUsername.getText().toString();
//                String pss = mPassword.getText().toString();
                params.put("username", usrName);
                params.put("password", passCode);
                return params;
            }
        };
        Log.d("kembalianLogin", stringRequest.toString());
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

}
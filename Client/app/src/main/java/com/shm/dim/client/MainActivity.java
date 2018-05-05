package com.shm.dim.client;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private String deviceID;
    private LocalDBHelper dbHelper;
    private SQLiteDatabase db;
    private TextView mHeader, mResponse;
    private BroadcastReceiver broadcastReceiver;


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED
                    && grantResults[1] != PackageManager.PERMISSION_GRANTED){
                enablePermissions();
            }
        }
    }

    public void enablePermissions() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
        }
    }


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        mHeader = findViewById(R.id.header);
        mResponse = findViewById(R.id.response);

        enablePermissions();

        dbHelper = new LocalDBHelper(this);
        db = dbHelper.getWritableDatabase();

        //Демонстрация работы регистрации пользователя при первой установке приложения
        //dbHelper.onUpgrade(db, 1, 1);

        /* Если количество строк в таблице USER <= 0 — пользователь не зарегестрирован,
        то запускается Activity для регистрации */
        if(getCountLineOnUserTable(db) <= 0) {
            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();
        mHeader.setText("Здравствуйте, " + getUserName());
        if(broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String envelope = getSOAPEnvelopeString(deviceID,
                            intent.getExtras().get("latitude").toString(),
                            intent.getExtras().get("longitude").toString(),
                            intent.getExtras().get("speed").toString());

                    SOAPServiceRequest("http://192.168.43.234:46001/SOAPService.asmx",
                            "http://192.168.43.234/Coordinates",
                            envelope);
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        if(broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        super.onDestroy();
    }


    public void onClickBeginWork(View view) {
        Intent intent = new Intent(getApplicationContext(), GPSService.class);
        Toast.makeText(getApplicationContext(), "Service started", Toast.LENGTH_SHORT).show();
        startService(intent);
    }

    public void onClickCompleteWork(View view) {
        Intent intent = new Intent(getApplicationContext(), GPSService.class);
        stopService(intent);
        Toast.makeText(getApplicationContext(), "Service stopped", Toast.LENGTH_SHORT).show();
    }


    // Возвращает имя текущего пользователя
    protected String getUserName() {
        String name = "";
        Cursor cursor = db.rawQuery("select Name from USER", null);
        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex("Name");
            do {
                name = cursor.getString(nameIndex);
            } while (cursor.moveToNext());
        }
        return name;
    }

    // Возвращает строку тело-SOAP запроса
    protected String getSOAPEnvelopeString(String deviceID, String latitude, String longitude, String speed) {
        String envelope = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "   <soap:Body>" +
                "       <Coordinates xmlns=\"http://192.168.43.234/\">" +
                "           <deviceID>" + deviceID + "</deviceID>" +
                "           <latitude>" + latitude + "</latitude>" +
                "           <longitude>" + longitude + "</longitude>" +
                "           <speed>" + speed + "</speed>" +
                "       </Coordinates>" +
                "   </soap:Body>" +
                "</soap:Envelope>";
        return envelope;
    }

    // Возвращает количество существующих записей в таблице USER
    protected int getCountLineOnUserTable(SQLiteDatabase db) {
        if (db == null || !db.isOpen()) {
            return 0;
        }
        Cursor cursor = db.rawQuery("select count(*) from USER;", null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return 0;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    // Вызов SOAP сервиса
    protected void SOAPServiceRequest(String url, String soapAction, String envelope){
        new SOAPServiceRequestTask().execute(url, soapAction, envelope);
    }


    // AsyncTask для вызова SOAP сервиса
    @SuppressLint("StaticFieldLeak")
    public class SOAPServiceRequestTask extends AsyncTask<String, Void, Void> {

        private URL url;
        private HttpURLConnection connection;
        private int responseCode;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            url = null;
            connection = null;
            responseCode = 0;
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                /* Headlines */
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("SOAPAction", params[1]);
                connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
                /* Body */
                try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"))) {
                    bw.write(params[2]);
                }

                /* Execute */
                responseCode = connection.getResponseCode();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (connection != null) {
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        mResponse.setText(br.readLine());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (responseCode == 0) {
                    mResponse.setText("Код ошибки: " + String.valueOf(responseCode) + ";\n" +
                            "Проверьте состояние сети или обратитесь к администратору");
                } else {
                    mResponse.setText("Код ошибки: " + String.valueOf(responseCode) + ";\n" +
                            "Обратититесь к администратору для решения проблемы");
                }
            }
        }
    }
}
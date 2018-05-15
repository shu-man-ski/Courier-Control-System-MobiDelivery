package com.shm.dim.client.Activitys;

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
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shm.dim.client.Services.GPSService;
import com.shm.dim.client.DBHelper.LocalDBHelper;
import com.shm.dim.client.Models.Order;
import com.shm.dim.client.Adapters.OrdersDataAdapter;
import com.shm.dim.client.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private String deviceID;
    private LocalDBHelper dbHelper;
    private SQLiteDatabase db;
    private TextView mHeader;
    private ProgressBar mProgressBar;
    private RecyclerView mOrdersList;
    private Spinner mOrderStatus;
    private Button mSend;
    private String[] orderStatuses = {"Новый", "Комплектуется", "В доставке", "Получен клиентом", "Оплачен", "Отменен"};
    private String selectedOrderCode;
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
        mOrdersList = findViewById(R.id.orders_list);
        mProgressBar = findViewById(R.id.progress);
        mOrderStatus = findViewById(R.id.order_status);
        mOrderStatus.setEnabled(false);
        mSend = findViewById(R.id.send);
        mSend.setEnabled(false);

        enablePermissions();

        initOrderStatusSpinner();

        dbHelper = new LocalDBHelper(this);
        db = dbHelper.getWritableDatabase();

        //Демонстрация работы регистрации пользователя при первой установке приложения
        //dbHelper.onUpgrade(db, 1, 1);

        /* Если пользователь не зарегестрирован — запускается Activity для регистрации */
        if(!userIsRegistered()) {
            startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        super.onResume();

        mHeader.setText("Здравствуйте, " + getUserName());

        ArrayList<Order> orders = dbHelper.getOrders(db);
        if(orders.size() != 0) {
            OrdersDataAdapter adapter = new OrdersDataAdapter(MainActivity.this.getApplicationContext(), orders, new OrdersDataAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Order order, int position) {
                    selectedOrderCode = order.getOrderCode();
                    mOrderStatus.setEnabled(true);
                    mSend.setEnabled(true);

                    // В Spinner устанавливаем статус выбранного заказа
                    for (int i = 0; i < orderStatuses.length; i++) {
                        if (order.getStatus().equals(orderStatuses[i])) {
                            mOrderStatus.setSelection(i);
                        }
                    }
                }
            });
            mOrdersList.setAdapter(adapter);
        } else {
            Toast.makeText(MainActivity.this.getApplicationContext(), "Список Ваших заказов пуст\n Нажмите кнопку обновить для проверки наличия Ваших заказов",
                    Toast.LENGTH_LONG).show();
        }

        if(broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String envelope = getSOAPEnvelopeString(deviceID,
                            intent.getExtras().get("latitude").toString(),
                            intent.getExtras().get("longitude").toString(),
                            intent.getExtras().get("speed").toString());

                    SOAPServiceRequest("http://mobi-delivery.somee.com/SOAPService.asmx",
                            "http://mobi-delivery.somee.com/Coordinates",
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

        Intent intent = new Intent(getApplicationContext(), GPSService.class);
        stopService(intent);

        super.onDestroy();
    }


    public void onClickGetOrders(View view) {
        selectedOrderCode = "";
        mOrderStatus.setEnabled(false);
        mSend.setEnabled(false);

        // Запуск сервиса получения местоположения
        Intent intent = new Intent(getApplicationContext(), GPSService.class);
        startService(intent);

        mProgressBar.setVisibility(View.VISIBLE);
        GetRESTServiceRequest("http://mobi-delivery.somee.com/api/orders?deviceId=" + deviceID);
    }

    public void onClickSend(View view) {
        mProgressBar.setVisibility(View.VISIBLE);
        PutRESTServiceRequest("http://mobi-delivery.somee.com/api/orders?orderCode=" + selectedOrderCode,
                "=" + mOrderStatus.getSelectedItem());
    }


    // Инициализация Spinner'а массивом статусов заказа
    protected void initOrderStatusSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, orderStatuses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mOrderStatus.setAdapter(adapter);
    }

    // Является ли пользователь зарегистрированным
    protected boolean userIsRegistered() {
        // Если количество строк в таблице USER <= 0 — пользователь не зарегестрирован
        return (getCountLineOnUserTable(db) > 0);
    }

    // Возвращает количество существующих записей в таблице USER
    protected int getCountLineOnUserTable(SQLiteDatabase db) {
        if (db == null || !db.isOpen()) {
            return 0;
        }
        Cursor cursor = db.rawQuery("SELECT count(*) FROM USER;", null);
        if (!cursor.moveToFirst()) {
            cursor.close();
            return 0;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    // Возвращает имя текущего пользователя
    protected String getUserName() {
        String name = "";
        Cursor cursor = db.rawQuery("SELECT Name FROM USER", null);
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
                "       <Coordinates xmlns=\"http://mobi-delivery.somee.com\">" +
                "           <deviceID>" + deviceID + "</deviceID>" +
                "           <latitude>" + latitude + "</latitude>" +
                "           <longitude>" + longitude + "</longitude>" +
                "           <speed>" + speed + "</speed>" +
                "       </Coordinates>" +
                "   </soap:Body>" +
                "</soap:Envelope>";
        return envelope;
    }

    // Вызов SOAP сервиса
    protected void SOAPServiceRequest(String url, String soapAction, String envelope){
        new SOAPServiceRequestTask(MainActivity.this.getApplicationContext()).execute(url, soapAction, envelope);
    }

    // Вызов REST сервиса
    protected void GetRESTServiceRequest(String url){
        new GetRESTServiceRequestTask(MainActivity.this.getApplicationContext()).execute(url);
    }

    // Вызов REST сервиса
    protected void PutRESTServiceRequest(String url, String body){
        new PutRESTServiceRequestTask(MainActivity.this.getApplicationContext()).execute(url, body);
    }

    // JSON парсер
    protected ArrayList<Order> getOrdersFromResponseString(String response) {
        ArrayList<Order>orders;
        if(!response.equals("[]")) {
            orders = new Gson().fromJson(response, new TypeToken<ArrayList<Order>>() {}.getType());
        } else {
            orders = null;
        }
        return orders;
    }


    // AsyncTask для вызова SOAP сервиса
    @SuppressLint("StaticFieldLeak")
    public class SOAPServiceRequestTask extends AsyncTask<String, Void, Void> {

        private URL url;
        private HttpURLConnection connection;
        private int responseCode;
        private final Context context;


        SOAPServiceRequestTask(Context context) {
            this.context = context;
        }


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
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    Toast.makeText(context, "Проблема с сетью. Не удалось отправить данные о местоположении",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // AsyncTask для вызова REST сервиса
    @SuppressLint("StaticFieldLeak")
    public class GetRESTServiceRequestTask extends AsyncTask<String, Void, Void> {

        private URL url;
        private HttpURLConnection connection;
        private int responseCode;
        private final Context context;


        GetRESTServiceRequestTask(Context context) {
            this.context = context;
        }


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
                connection.setRequestMethod("GET");

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
                    String response = null;
                    try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                        response = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ArrayList<Order> orders = getOrdersFromResponseString(response);
                    dbHelper.addOrders(db, orders);
                    MainActivity.this.onResume();
                } else {
                    Toast.makeText(context, "Проблема с сетью. Код ошибки: " + String.valueOf(responseCode),
                            Toast.LENGTH_LONG).show();
                }
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }

    // AsyncTask для вызова REST сервиса
    @SuppressLint("StaticFieldLeak")
    public class PutRESTServiceRequestTask extends AsyncTask<String, Void, Void> {

        private URL url;
        private HttpURLConnection connection;
        private int responseCode;
        private final Context context;


        PutRESTServiceRequestTask(Context context) {
            this.context = context;
        }


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
                connection.setDoOutput(true);
                connection.setRequestMethod("PUT");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                /* Body */
                try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"))) {
                    bw.write(params[1]);
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
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == 204/*204 - получен ответ без тела*/) {
                    Toast.makeText(context, "Статус заказа был успешно отправлен", Toast.LENGTH_LONG).show();
                    // Обновляем список заказов
                    GetRESTServiceRequest("http://mobi-delivery.somee.com/api/orders?deviceId=" + deviceID);
                    mOrderStatus.setEnabled(false);
                    mSend.setEnabled(false);
                } else {
                    Toast.makeText(context, "Проблема с сетью. Код ошибки: " + String.valueOf(responseCode),
                            Toast.LENGTH_LONG).show();
                }
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }
}
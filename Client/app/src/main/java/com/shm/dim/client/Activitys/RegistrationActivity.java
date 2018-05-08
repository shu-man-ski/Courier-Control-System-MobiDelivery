package com.shm.dim.client.Activitys;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.shm.dim.client.DBHelper.LocalDBHelper;
import com.shm.dim.client.R;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;

public class RegistrationActivity extends AppCompatActivity {

    private LocalDBHelper dbHelper;
    private SQLiteDatabase db;
    private TextView mInfoText;
    private EditText mDeviceID, mSurname, mName, mPatronymic,
        mBirthdate, mPhoneNumber, mAddress;
    private String deviceID, surname, name, patronymic,
            birthdate, phoneNumber, address;


    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        dbHelper = new LocalDBHelper(this);
        db = dbHelper.getWritableDatabase();

        mInfoText = findViewById(R.id.info_text);
        mDeviceID = findViewById(R.id.device_id);
        mSurname = findViewById(R.id.surname);
        mName = findViewById(R.id.name);
        mPatronymic = findViewById(R.id.patronymic);
        mBirthdate = findViewById(R.id.birthdate);
        mPhoneNumber = findViewById(R.id.phone_number);
        mAddress = findViewById(R.id.address);

        mDeviceID.setText(deviceID);
        mDeviceID.setEnabled(false);
    }

    @Override
    public void onBackPressed() {
        createDialogMsg("Необходимо завершить регистрацию");
    }


    public void onClickCompleteRegistration(View view) {
        if(!isEmptyValuesOnEditViews()) {
            surname = mSurname.getText().toString();
            name = mName.getText().toString();
            patronymic = mPatronymic.getText().toString();
            birthdate = mBirthdate.getText().toString();
            phoneNumber = mPhoneNumber.getText().toString();
            address = mAddress.getText().toString();

            if (formatDateIsCorrect(birthdate) && formatPhoneNumberIsCorrect(phoneNumber)) {
                String envelope = getSOAPEnvelopeString(deviceID, surname, name, patronymic,
                        birthdate, phoneNumber, address);

                SOAPServiceRequest("http://192.168.43.234:46001/SOAPService.asmx",
                        "http://192.168.43.234/RegisterCourier",
                        envelope);
            } else {
                createDialogMsg("Проверьте правильность ввода телефона и даты рождения");
            }
        } else {
            createDialogMsg("Заполните все поля");
        }
    }


    // Возвращает строку тело-SOAP запроса
    protected String getSOAPEnvelopeString(String deviceID, String surname, String name, String patronymic,
                                           String birthdate, String phoneNumber, String address) {
        String envelope = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                "   <soap:Body>" +
                "       <RegisterCourier xmlns=\"http://192.168.43.234/\">" +
                "           <deviceID>" + deviceID + "</deviceID>" +
                "           <surname>" + surname +"</surname>" +
                "           <name>" + name + "</name>" +
                "           <patronymic>" + patronymic + "</patronymic>" +
                "           <birthdate>" + birthdate + "</birthdate>" +
                "           <phoneNumber>" + phoneNumber + "</phoneNumber>" +
                "           <address>" + address + "</address>" +
                "       </RegisterCourier>" +
                "   </soap:Body>" +
                "</soap:Envelope>";
        return envelope;
    }

    // Пусты ли все поля
    private boolean isEmptyValuesOnEditViews() {
        return (mSurname.getText().toString().isEmpty() || mName.getText().toString().isEmpty() ||
                mPatronymic.getText().toString().isEmpty() || mBirthdate.getText().toString().isEmpty() ||
                mPhoneNumber.getText().toString().isEmpty() || mAddress.getText().toString().isEmpty());
    }

    // Проверка формата введенного номера телефона
    private boolean formatPhoneNumberIsCorrect(String str) {
        String reg = "^[+][0-9]{10,13}$";
        return str.matches(reg);
    }

    // Проверка формата введенной даты рождения
    private boolean formatDateIsCorrect(String str) {
        String DATE_FORMAT = "dd.MM.yyyy";
        SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
        df.setLenient(false);
        return df.parse(str, new ParsePosition(0)) != null;
    }

    // Диалоговое окно для сообщения
    private void createDialogMsg(String msg) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage(msg).setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                    }
                });
        AlertDialog ad = b.create();
        ad.show();
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

                mInfoText.setText("Идет отправка, ожидайте...");

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
                connection.disconnect();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    dbHelper.addUser(db, deviceID, surname, name, patronymic, birthdate, phoneNumber, address);
                    mInfoText.setText("");
                    finish();
                } else if (responseCode == 0) {
                    mInfoText.setText("Код ошибки: " + String.valueOf(responseCode) + ";\n" +
                            "Проверьте состояние сети или обратитесь к администратору");
                } else {
                    mInfoText.setText("Код ошибки: " + String.valueOf(responseCode) + ";\n" +
                        "Обратититесь к администратору для решения проблемы");
                }
            }
        }
    }
}
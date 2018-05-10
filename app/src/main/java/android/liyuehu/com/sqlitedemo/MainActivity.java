package android.liyuehu.com.sqlitedemo;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.textwatcher.ClassOfTextWatcher;
import com.timer.service.TimerService;

import java.util.Date;

import static android.liyuehu.com.sqlitedemo.MyContentProvider.URI_MY_MENU;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "MainActivity";
    Button[] button=new Button[4];
    private static final int[] id = {R.id.query, R.id.insert, R.id.update, R.id.delete};
    private SqliteHelp mSqliteHelp;
    private EditText mDageTimesEditText;
    private EditText mDapiTimesEditText;
    private EditText mBreakFastEditText;
    private EditText mLunchEditText;
    private EditText mDinnerTimesEditText;
    private TextView mDateTextView;
    private static int dageTimes = 0;
    private static int dapiTimes = 0;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss");
    private SharedPreferences mSharedPreference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreference = getSharedPreferences("myHealth", Context.MODE_PRIVATE);
        long mFirstEnterTime=mSharedPreference.getLong("firstEnterTime",0);
        if(mFirstEnterTime<=0){
            mSharedPreference.edit().putLong("firstEnterTime", SystemClock.elapsedRealtime()).apply();
        }else {
            mFirstEnterTime=mSharedPreference.getLong("firstEnterTime",0);
        }
        Log.v("lilea","mFirstEnterTime==="+mFirstEnterTime);
        setContentView(R.layout.activity_main);
        initView();
        initValue();
        Intent intent = new Intent(this, TimerService.class);
        startService(intent);

    }

    void initView() {
        for (int i = 0; i < id.length; i++) {
            button[i] = (Button) findViewById(id[i]);
        }
        Button mDageAddButton = (Button) findViewById(R.id.dage_add_times);
        Button mDapiAddButton = (Button) findViewById(R.id.dapi_add_times);
        mDageTimesEditText = (EditText) findViewById(R.id.dage_content);
        mDapiTimesEditText = (EditText) findViewById(R.id.dapi_content);
        mBreakFastEditText = (EditText) findViewById(R.id.breakfast_content);
        mLunchEditText = (EditText) findViewById(R.id.lunch_content);
        mDinnerTimesEditText = (EditText) findViewById(R.id.dinner_content);
        mDateTextView = (TextView) findViewById(R.id.date_content);
        mDageAddButton.setOnClickListener(this);
        mDapiAddButton.setOnClickListener(this);
        mBreakFastEditText.addTextChangedListener(new ClassOfTextWatcher(mBreakFastEditText, this));
        mLunchEditText.addTextChangedListener(new ClassOfTextWatcher(mLunchEditText, this));
        mDinnerTimesEditText.addTextChangedListener(new ClassOfTextWatcher(mDinnerTimesEditText, this));
        mDageTimesEditText.addTextChangedListener(new ClassOfTextWatcher(mDageTimesEditText, this));
        mDapiTimesEditText.addTextChangedListener(new ClassOfTextWatcher(mDapiTimesEditText, this));
        mSqliteHelp=new SqliteHelp(this);
    }

    void initValue() {
        for (int i = 0; i < button.length; i++) {
            button[i].setOnClickListener(this);
        }
        String date = format.format(new Date());
        mDateTextView.setText(date);
        if (mSharedPreference.getString("date", null) == null) {
            mSharedPreference.edit().putString("date", date).apply();
        }
        else if (!date.equals(mSharedPreference.getString("date", null))) {
            mDateTextView.setText(mSharedPreference.getString("date", null));
        }
        mBreakFastEditText.setText(mSharedPreference.getString("breakfast", null));
        mLunchEditText.setText(mSharedPreference.getString("lunch", null));
        mDinnerTimesEditText.setText(mSharedPreference.getString("dinner", null));
        Log.i(TAG, "mDinnerTimesEditText: "+mSharedPreference.getString("dinner", null));
        dageTimes = mSharedPreference.getInt("dage", 0);
        dapiTimes = mSharedPreference.getInt("dapi", 0);
        mDageTimesEditText.setText(String.valueOf(dageTimes));
        mDapiTimesEditText.setText(String.valueOf(dapiTimes));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.query:
                mSqliteHelp.query();
                break;
            case R.id.insert:
                mSqliteHelp.insert();
                break;
            case R.id.delete:
                mSqliteHelp.delete();
                break;
            case R.id.update:
                mSqliteHelp.update();
                break;
            case R.id.dage_add_times:
                dageTimes++;
                mDageTimesEditText.setText(String.valueOf(dageTimes));
                mSharedPreference.edit().putInt("dage", dageTimes).apply();
                break;
            case R.id.dapi_add_times:
                dapiTimes++;
                mDapiTimesEditText.setText(String.valueOf(dapiTimes));
                mSharedPreference.edit().putInt("dapi", dapiTimes).apply();
                break;
            default:
                break;

        }
    }

    @Override
    protected void onDestroy() {
        mDapiTimesEditText.setText(null);
        mDageTimesEditText.setText(null);
        super.onDestroy();
    }
}

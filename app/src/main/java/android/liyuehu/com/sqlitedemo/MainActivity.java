package android.liyuehu.com.sqlitedemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import static android.liyuehu.com.sqlitedemo.MyContentProvider.URI_MY_MENU;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button queryButton;
    Button insertButton;
    Button updateButton;
    Button deleteButton;
    Button[] button = new Button[]{queryButton, insertButton, updateButton, deleteButton};
    private static final int[] id = {R.id.query, R.id.insert, R.id.update, R.id.delete};
    private SqliteHelp mSqliteHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initValue();

    }

    void initView() {
        for (int i = 0; i < id.length; i++) {
            button[i] = (Button) findViewById(id[i]);
        }
    }

    void initValue() {
        for (int i = 0; i < button.length; i++) {
            button[i].setOnClickListener(this);
        }
        mSqliteHelp=new SqliteHelp(this);
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
            default:
                break;

        }
    }
}

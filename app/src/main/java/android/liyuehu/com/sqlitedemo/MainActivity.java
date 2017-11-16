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
    private static String[] content = new String[]{"番茄炒蛋", "土豆丝", "酸菜鱼", "烤鱼", "清汤羊肉"};
    private static String[] updateContent = new String[]{"苹果", "香蕉", "草莓", "香草", "榴莲"};

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
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.query:
                //projection表示要查询返回的列
                String[] projection = new String[]{"content", "price", "remain"};
                String[] selectionArg = new String[]{"红烧肉"};
                String selection = "content" + " not null";
                Cursor cursor = getContentResolver().query(URI_MY_MENU, projection, selection, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    do {
//                        int price = cursor.getColumnIndexOrThrow(cursor.getColumnName(1));
                        int price=cursor.getInt(1);
                        String content = cursor.getString(0);
//                        int remain = cursor.getColumnIndexOrThrow(cursor.getColumnName(2));
                        int remain = cursor.getInt(2);
                        Log.v("lilealyh", " content==" + content + " price===" + price + " remain==" + remain);
                    } while (cursor.moveToNext());
                }
                break;
            case R.id.insert:
                int price = 30;
                int remain = 10;
                for (int i = 0; i < content.length; i++) {
                    //这里如果不同步，插入的值会不对
                    synchronized (this) {
                        ContentValues values = new ContentValues();
                        values.put("content", content[i]);
                        values.put("price", ++price);
                        values.put("remain", remain--);
                        Uri insertResult = getContentResolver().insert(URI_MY_MENU, values);
                        Log.v("lilealyh", "insertResult===" + insertResult);
                    }
                }

                break;
            case R.id.delete:
                int deleteResult = getContentResolver().delete(URI_MY_MENU, "content" +" LIKE "+"'%鱼%'" , null);
                if(deleteResult==0){
                    deleteResult = getContentResolver().delete(URI_MY_MENU, "content" +" LIKE "+"'%香%'" , null);
                }
                Log.v("lilealyh", "deleteResult===" + deleteResult);
                break;
            case R.id.update:
                int fruitPrice = 5;
                int fruitRemain = 10;
                for (int i = 0; i <updateContent.length ; i++) {
                    synchronized (this){
                        ContentValues updateValues = new ContentValues();
                        updateValues.put("content", updateContent[i]);
                        updateValues.put("price", fruitPrice++);
                        updateValues.put("remain", fruitRemain--);
                        String where="content" + "=?";
                        String[] arg=new String[]{content[i]};
                        int updateValue = getContentResolver().update(URI_MY_MENU, updateValues, where, arg);
                        Log.v("lilealyh", "updateValue===" + updateValue);
                    }
                }
                break;
            default:
                break;

        }


    }
}

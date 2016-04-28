package com.ershov.max.foodkeeper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    DB db;
    SimpleCursorAdapter scAdapter;
    ListView productListView;
    Cursor cursor;
    AlertDialog.Builder confirmDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddFoodItemActivity.class);
                startActivity(intent);
            }
        });

        db = new DB(this);
        db.open();
        cursor = db.getAllItems();

        String[] dbLine = new String[] { DB.COLUMN_NAME, DB.COLUMN_EXPIRE_DATE };
        int[] viewLine = new int[] { R.id.itemNameText, R.id.itemDateText };

        scAdapter = new SimpleCursorAdapter(this, R.layout.item, cursor, dbLine, viewLine, 0);
        scAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                if (columnIndex == 3) {
                    TextView tv = (TextView) view;
                    String days = FoodItem.fromDateToDuration(cursor.getString(columnIndex));
                    tv.setText(days);
                    return true;
                }
                return false;
            }
        }

        );

        productListView = (ListView) findViewById(R.id.productListView);
        if (productListView != null) {
            productListView.setAdapter(scAdapter);
        }
        productListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                delItemWithAlert(MainActivity.this, id, db);
                return true;
            }
        });
        db.close();
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, 1, 0, "Удалить запись");
    }


    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == 1) {
            // получаем из пункта контекстного меню данные по пункту списка
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) item
                    .getMenuInfo();
            // извлекаем id записи и удаляем соответствующую запись в БД
            db.delRec(acmi.id);
            // получаем новый курсор с данными
            //getSupportLoaderManager().getLoader(0).forceLoad();
            return true;
        }
        return super.onContextItemSelected(item);
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

    private void delItemWithAlert(Context context, final long id, final DB db) {
        String title = "Подтверждение";
        String message = "Вы уверены, что хотите удалить это блюдо?";
        String button1String = "Да";
        String button2String = "Нет";

        confirmDel = new AlertDialog.Builder(context);
        confirmDel.setTitle(title);  // заголовок
        confirmDel.setMessage(message); // сообщение
        confirmDel.setPositiveButton(button1String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                //db = new DB();
                //db.open();
                //db.delRec(id);
            }
        });
        confirmDel.setNegativeButton(button2String, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        confirmDel.setCancelable(true);
        confirmDel.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {

            }
        });

        confirmDel.show();
    }

}

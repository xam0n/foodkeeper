package com.ershov.max.foodkeeper;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class AddFoodItemActivity extends AppCompatActivity {
    private EditText nameText;
    private EditText inputDateText;
    private EditText daysBeforeText;
    private boolean flagForChange = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		nameText = (EditText) findViewById(R.id.nameText);

        //

        inputDateText = (EditText) findViewById(R.id.inputDateText);
        inputDateText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(AddFoodItemActivity.this,
                        new mDateSetListener(), mYear, mMonth, mDay);
                dialog.show();
                return false;
            }
        });
        inputDateText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    if (flagForChange) {
                        flagForChange = false;
                        daysBeforeText.setText(fromDateToDuration(s.toString()));
                    } else flagForChange = true;
                }
            }
        });

        //



        //

        daysBeforeText = (EditText) findViewById(R.id.daysBeforeText);
        daysBeforeText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    if (flagForChange) {
                        flagForChange = false;
                        inputDateText.setText(fromDurationToDate(s.toString()));
                    } else flagForChange = true;
                }
            }
        });
    }

    public void onClickFinishAdding(View view) {
        saveItem();
        Intent intent = new Intent(AddFoodItemActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onClickContinueAdding(View view) {
        saveItem();
        finish();
        startActivity(getIntent());
    }

    private void saveItem()  {
        try {
            FoodItem item = new FoodItem(inputDateText.getText().toString(), nameText.getText().toString());
            DBHelper dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL(item.addItemQuery());
            Toast toast = Toast.makeText(this, "Data inserted to DB", Toast.LENGTH_SHORT);
            toast.show();
            db.close();
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private class mDateSetListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String day = (dayOfMonth < 10) ? "0"+ String.valueOf(dayOfMonth) : String.valueOf(dayOfMonth);
            monthOfYear++;
            String month = (monthOfYear < 10) ? "0"+ String.valueOf(monthOfYear) : String.valueOf(monthOfYear);;
            inputDateText.setText(new StringBuilder()
                    // Month is 0 based so add 1
                    .append(day).append("-").append(month).append("-")
                    .append(year).append(" "));
        }
    }

    private String fromDateToDuration(String date) {
        Date currentDate = new Date();
        Date endDate = FoodItem.stringToDate(date,FoodItem.DATE_FORMAT);

        long startTime = currentDate.getTime();
        long endTime = endDate.getTime();
        long diffTime = endTime - startTime;
        long diffDays = diffTime / (1000 * 60 * 60 * 24) + 1;
        if (diffDays > 0) {
            return String.valueOf(diffDays);
        } else return "0";
    }

    private String fromDurationToDate(String duration) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        try {
            c.add(Calendar.DATE, Integer.parseInt(duration));
        } catch (NumberFormatException e) {
            return "";
        }
        date = c.getTime();
        return FoodItem.dateToString(date,FoodItem.DATE_FORMAT);
    }
}

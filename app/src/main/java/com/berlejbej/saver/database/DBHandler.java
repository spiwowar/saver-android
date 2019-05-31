package com.berlejbej.saver.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.berlejbej.saver.objects.AdditionalIncome;
import com.berlejbej.saver.objects.ConstantCost;
import com.berlejbej.saver.objects.Cost;
import com.berlejbej.saver.objects.Income;
import com.berlejbej.saver.objects.RemainingMonth;
import com.berlejbej.saver.objects.Target;
import com.berlejbej.saver.utils.DateUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Szymon on 2016-10-12.
 */
public class DBHandler extends SQLiteOpenHelper{

    SQLiteDatabase db;
    private static DBHandler dbInstance;

    private static final String TAG = "DBHandler";

    // Database version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MoneySaver";
    // Tables names
    private static final String TABLE_START_DATE = "StartDate";
    private static final String TABLE_COSTS = "Costs";
    private static final String TABLE_TARGETS = "Targets";
    private static final String TABLE_INCOME = "Income";
    private static final String TABLE_CONSTANT_COSTS = "ConstantCosts";
    private static final String TABLE_ADDITIONAL_INCOME = "AdditionalIncome";
    private static final String TABLE_REMAINING_MONTH = "RemainingMonth";
    // Costs Table Columns names
    private static final String START_DATE = "Date";
    // Costs Table Columns names
    private static final String COST_ID = "ID";
    private static final String COST_CATEGORY = "Category";
    private static final String COST_VALUE = "Value";
    private static final String COST_DATE = "Date";
    // Targets Table Columns names
    private static final String TARGET_ID = "ID";
    private static final String TARGET_NAME = "Name";
    private static final String TARGET_VALUE = "Value";
    private static final String TARGET_REMAINING_VALUE = "RemainingValue";
    private static final String TARGET_START_DATE = "StartDate";
    private static final String TARGET_END_DATE = "EndDate";
    // MonthlyIncome Table Columns names
    private static final String INCOME = "Income";
    private static final String INCOME_FREQUENCY = "Frequency";
    // ConstantCosts Table Columns names
    private static final String CONSTANT_COSTS_ID = "ID";
    private static final String CONSTANT_COSTS_NAME = "Name";
    private static final String CONSTANT_COSTS_VALUE = "Value";
    private static final String CONSTANT_COSTS_CATEGORY = "Category";
    // AdditionalIncome Table Columns names
    private static final String ADDITIONAL_INCOME_ID = "ID";
    private static final String ADDITIONAL_INCOME_NAME = "Name";
    private static final String ADDITIONAL_INCOME_VALUE = "Value";
    private static final String ADDITIONAL_INCOME_DATE = "Date";
    // RemainingMonth Table Columns names
    private static final String REMAINING_MONTH_VALUE = "Value";

    public static synchronized DBHandler getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (dbInstance == null) {
            dbInstance = new DBHandler(context.getApplicationContext());
        }
        return dbInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating database");
        String CREATE_START_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_START_DATE +
                        "(" + START_DATE + " TEXT)";
        String CREATE_COSTS_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_COSTS +
                        "(" + COST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                        + COST_CATEGORY + " TEXT, "
                        + COST_VALUE + " REAL, "
                        + COST_DATE + " TEXT)";
        String CREATE_TARGETS_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_TARGETS +
                        "(" + TARGET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                        + TARGET_NAME + " TEXT, "
                        + TARGET_VALUE + " REAL, "
                        + TARGET_REMAINING_VALUE + " REAL, "
                        + TARGET_START_DATE + " TEXT, "
                        + TARGET_END_DATE + " TEXT)";
        String CREATE_CONSTANT_COSTS_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_CONSTANT_COSTS +
                        "(" + CONSTANT_COSTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                        + CONSTANT_COSTS_NAME + " TEXT, "
                        + CONSTANT_COSTS_VALUE + " REAL, "
                        + CONSTANT_COSTS_CATEGORY + " TEXT)";
        String CREATE_INCOME_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_INCOME +
                        "(" + INCOME + " REAL, "
                        + INCOME_FREQUENCY + " INTEGER)";
        String CREATE_ADDITIONAL_INCOME_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_ADDITIONAL_INCOME +
                        "(" + ADDITIONAL_INCOME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                        + ADDITIONAL_INCOME_NAME + " TEXT, "
                        + ADDITIONAL_INCOME_VALUE + " REAL, "
                        + ADDITIONAL_INCOME_DATE + " TEXT)";
        String CREATE_REMAINING_MONTH_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_REMAINING_MONTH +
                        "(" + REMAINING_MONTH_VALUE + " REAL)";
        db.execSQL(CREATE_START_TABLE);
        db.execSQL(CREATE_COSTS_TABLE);
        db.execSQL(CREATE_TARGETS_TABLE);
        db.execSQL(CREATE_CONSTANT_COSTS_TABLE);
        db.execSQL(CREATE_INCOME_TABLE);
        db.execSQL(CREATE_ADDITIONAL_INCOME_TABLE);
        db.execSQL(CREATE_REMAINING_MONTH_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL(“DROP TABLE IF EXISTS ” + TABLE_SHOPS);
        // Creating tables again
        //onCreate(db);
    }

    /**
     * UTILITIES
     */
    private boolean isTableEmpty(String tableName){
        db = this.getWritableDatabase();
        String countString = "SELECT count(*) FROM " + tableName;
        Cursor cursor = db.rawQuery(countString, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        db.close();
        if (count > 0) {
            Log.d(TAG, "Table " + tableName + " is not empty");
            return false;
        }
        else {
            Log.d(TAG, "Table " + tableName + " is empty");
            return true;
        }
    }

    private void clearTable(String tableName){
        db = this.getWritableDatabase();
        String deleteString = "DELETE FROM " + tableName;
        db.execSQL(deleteString);
        db.close();
    }

    /**
     * STARTING DATE
     */
    public void setStartDate(String date) {

        if (!isTableEmpty(TABLE_START_DATE)) {
            clearTable(TABLE_START_DATE);
        }
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(START_DATE, date);
        db.insert(TABLE_START_DATE, null, values);
        db.close();
    }

    public String getStartDate() {
        String selectQuery = "SELECT * FROM " + TABLE_START_DATE;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        String date = null;
        if (cursor.moveToFirst()) {
            date = new String(cursor.getString(0));
        }
        return date;
    }

    /**
     * INCOME
     */
    public void setIncome(Income income) {
        if (!isTableEmpty(TABLE_INCOME)) {
            clearTable(TABLE_INCOME);
        }
        Log.d(TAG, "Setting income to database: " + income.getIncome());
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(INCOME, income.getIncome());
        values.put(INCOME_FREQUENCY, income.getFrequency());
        db.insert(TABLE_INCOME, null, values);
        db.close();
    }

    public Income getIncome() {
        Log.d(TAG, "Getting income from database");
        String selectQuery = "SELECT * FROM " + TABLE_INCOME;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Income income = null;
        if (cursor.moveToFirst()) {
            income = new Income();
            income.setIncome(cursor.getDouble(0)).
                   setFrequency(cursor.getInt(1));
        }
        return income;
    }

    /**
     * COSTS
     */
    public void addCost(Cost cost) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (cost.getID() != null) {
            values.put(COST_ID, cost.getID());
        }
        values.put(COST_CATEGORY, cost.getCategory());
        values.put(COST_VALUE, cost.getValue());
        values.put(COST_DATE, cost.getDate());
        db.insert(TABLE_COSTS, null, values);
        db.close();
    }

    public boolean removeCost(Cost cost) {
        db = this.getWritableDatabase();
        boolean returnValue = db.delete(TABLE_COSTS, COST_ID + "=" + cost.getID(), null) > 0;
        db.close();
        return returnValue;
    }

    // Getting All Costs
    public List<Cost> getAllCosts() {
        List<Cost> costList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_COSTS;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Cost cost;
        if (cursor.moveToFirst()) {
            do {
                cost = new Cost(cursor.getString(1), cursor.getDouble(2), cursor.getString(3));
                cost.setID(cursor.getInt(0));
                costList.add(cost);
            } while (cursor.moveToNext());
        }
        db.close();
        // Reverse, so the new would be at first
        Collections.reverse(costList);
        return costList;
    }

    /**
     * TARGETS
     */
    public void addTarget(Target target) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (target.getID() != null) {
            values.put(TARGET_ID, target.getID());
        }
        values.put(TARGET_NAME, target.getName());
        values.put(TARGET_VALUE, target.getTargetValue());
        values.put(TARGET_REMAINING_VALUE, target.getRemainingValue());
        values.put(TARGET_START_DATE, target.getStartDate());
        values.put(TARGET_END_DATE, target.getEndDate());
        db.insert(TABLE_TARGETS, null, values);
        db.close();
    }

    public boolean removeTarget(Target target) {
        db = this.getWritableDatabase();
        boolean returnValue = db.delete(TABLE_TARGETS, TARGET_ID + "=" + target.getID(), null) > 0;
        db.close();
        return returnValue;
    }

    public Target getTarget(int id) {
        String selectQuery = "SELECT * FROM " + TABLE_TARGETS + " WHERE " + TARGET_ID + "=" + id;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Target target = null;
        if (cursor.moveToFirst()) {
            target = new Target(cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getString(4), cursor.getString(5));
            target.setID(cursor.getInt(0));
        }
        db.close();
        return target;
    }

    public List<Target> getAllTargets() {
        List<Target> targetList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TARGETS;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Target target;
        if (cursor.moveToFirst()) {
            do {
                target = new Target(cursor.getString(1), cursor.getDouble(2), cursor.getDouble(3), cursor.getString(4), cursor.getString(5));
                target.setID(cursor.getInt(0));
                targetList.add(target);
            } while (cursor.moveToNext());
        }
        db.close();
        return targetList;
    }

    public List<Target> getTargetsSortedByDate(){
        List<Target> allTargets = getAllTargets();
        List<Target> allTargetsSortedByDate = new ArrayList<>();

        Date targetDate;
        Date sortedTargetDate;
        for (Target target: allTargets){
            targetDate = DateUtils.string2Date(target.getEndDate());
            if (allTargetsSortedByDate.isEmpty()){
                allTargetsSortedByDate.add(target);
                continue;
            }
            for (Target sortedTarget: allTargetsSortedByDate){
                sortedTargetDate = DateUtils.string2Date(sortedTarget.getEndDate());
                if (DateUtils.compareMonths(targetDate, sortedTargetDate) <= 0) {
                    allTargetsSortedByDate.add(allTargetsSortedByDate.indexOf(sortedTarget), target);
                    break;
                }
                else if (allTargetsSortedByDate.indexOf(sortedTarget) == allTargetsSortedByDate.size()-1){
                    allTargetsSortedByDate.add(target);
                }
            }
        }
        return allTargetsSortedByDate;
    }

    /**
     * CONSTANT COSTS
     */
    public void addConstantCost(ConstantCost cost) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (cost.getID() != null) {
            values.put(CONSTANT_COSTS_ID, cost.getID());
        }
        values.put(CONSTANT_COSTS_NAME, cost.getName());
        values.put(CONSTANT_COSTS_VALUE, cost.getValue());
        values.put(CONSTANT_COSTS_CATEGORY, cost.getCategory());
        db.insert(TABLE_CONSTANT_COSTS, null, values);
        db.close();
    }

    public boolean removeConstantCost(ConstantCost constantCost) {
        db = this.getWritableDatabase();
        boolean returnValue = db.delete(TABLE_CONSTANT_COSTS, CONSTANT_COSTS_ID + "=" + constantCost.getID(), null) > 0;
        db.close();
        return returnValue;
    }

    public List<ConstantCost> getAllConstantCosts() {
        List<ConstantCost> constantCostsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CONSTANT_COSTS;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        ConstantCost constantCost = null;
        if (cursor.moveToFirst()) {
            do {
                constantCost = new ConstantCost(cursor.getString(1), cursor.getDouble(2), cursor.getString(3));
                constantCost.setID(cursor.getInt(0));
                constantCostsList.add(constantCost);
            } while (cursor.moveToNext());
        }
        db.close();
        return constantCostsList;
    }

    /**
     * ADDITIONAL INCOMES
     */
    public void addAdditionalIncome(AdditionalIncome additionalIncome) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (additionalIncome.getID() != null) {
            values.put(ADDITIONAL_INCOME_ID, additionalIncome.getID());
        }
        values.put(ADDITIONAL_INCOME_NAME, additionalIncome.getName());
        values.put(ADDITIONAL_INCOME_VALUE, additionalIncome.getValue());
        values.put(ADDITIONAL_INCOME_DATE, additionalIncome.getDate());
        db.insert(TABLE_ADDITIONAL_INCOME, null, values);
        db.close();
    }

    public boolean removeAdditionalIncome(AdditionalIncome additionalIncome) {
        db = this.getWritableDatabase();
        boolean returnValue = db.delete(TABLE_ADDITIONAL_INCOME, ADDITIONAL_INCOME_ID + "=" + additionalIncome.getID(), null) > 0;
        db.close();
        return returnValue;
    }

    public List<AdditionalIncome> getAllAdditionalIncomes() {
        List<AdditionalIncome> additionalIncomeList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ADDITIONAL_INCOME;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        AdditionalIncome additionalIncome = null;
        if (cursor.moveToFirst()) {
            do {
                additionalIncome = new AdditionalIncome(cursor.getString(1), cursor.getDouble(2), cursor.getString(3));
                additionalIncome.setID(cursor.getInt(0));
                additionalIncomeList.add(additionalIncome);
            } while (cursor.moveToNext());
        }
        db.close();
        // Reverse, so the new would be at first
        Collections.reverse(additionalIncomeList);
        return additionalIncomeList;
    }

    /**
     * AVAILABLE MONEY
     */
    public void addRemainingMonth(RemainingMonth remainingMonth) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(REMAINING_MONTH_VALUE, remainingMonth.getValue());
        db.insert(TABLE_REMAINING_MONTH, null, values);
        db.close();
    }

    public RemainingMonth getRemainingMonth() {
        String selectQuery = "SELECT * FROM " + TABLE_REMAINING_MONTH;
        db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        RemainingMonth remainingMonth = null;
        if (cursor.moveToFirst()) {
            do {
                remainingMonth = new RemainingMonth(cursor.getDouble(0));
            } while (cursor.moveToNext());
        }
        db.close();
        return remainingMonth;
    }
}

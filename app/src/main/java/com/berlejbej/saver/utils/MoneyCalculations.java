package com.berlejbej.saver.utils;

import android.content.Context;
import android.util.Log;

import com.berlejbej.saver.objects.ConstantCost;
import com.berlejbej.saver.objects.Cost;
import com.berlejbej.saver.database.DBHandler;
import com.berlejbej.saver.objects.RemainingMonth;
import com.berlejbej.saver.objects.Target;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Szymon on 2016-10-08.
 */
public class MoneyCalculations {

    private static final String TAG = "MoneyCalculations";

    public static double getRemainingTodayValue(Context context){
        Calendar c = Calendar.getInstance();
        int currentDayOfMonth = c.get(Calendar.DAY_OF_MONTH);
        int maxDaysOfMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        double remainingIncome = getRemainingMonthlyValue(context);
        double remainingMoneyPerDay = remainingIncome/(maxDaysOfMonth - currentDayOfMonth);

        return remainingMoneyPerDay;
    }

    public static double getRemainingMonthlyValue(Context context){

        //NOTE: nie uwzględnia kosztów stałych, tylko miesięczny dochód i sume targetów oraz sume kosztów w tym miesiącu
        //NOTE: za pierwszym razem nie jest uwzględnione ile zostało, tylko income

        DBHandler dbHandler = DBHandler.getInstance(context);

        RemainingMonth remainingMonth = dbHandler.getRemainingMonth();
        List<Target> listOfTargets = dbHandler.getAllTargets();
        List<Cost> listOfAllCosts = dbHandler.getAllCosts();

        double remainingMonthValue = remainingMonth.getValue();
        double targetsThisMonthCost = getTargetsSumThisMonth(listOfTargets);
        double costsThisMonth = getSumOfAllCostsThisMonth(listOfAllCosts);

        double remainingMonthlyValue = remainingMonthValue - targetsThisMonthCost - costsThisMonth;
        return remainingMonthlyValue;
    }

    public static double getDailyBalance(Context context){
        DBHandler dbHandler = DBHandler.getInstance(context);
        double dailyBalance;
        double availableMoneyWithoutCosts;

        Calendar currentCalendar = Calendar.getInstance();
        int daysOfCurrentMonth = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Get available money we have
        RemainingMonth remainingMonth = dbHandler.getRemainingMonth();
        availableMoneyWithoutCosts = remainingMonth.getValue();

        // Substract from targets
        List<Target> listOfTargets = dbHandler.getAllTargets();
        availableMoneyWithoutCosts -= getTargetsSumThisMonth(listOfTargets);

        // Substract from constant costs
        List<ConstantCost> listOfConstantCosts = dbHandler.getAllConstantCosts();
        availableMoneyWithoutCosts -= getConstantCostsSum(listOfConstantCosts);

        // Check whether it is the first month or not
        if (DateUtils.isFirstMonth(context)) {
            Log.d(TAG, "It's first month");

            Date startApplicationDate = DateUtils.string2Date(dbHandler.getStartDate());
            Calendar startCalendar = Calendar.getInstance();
            startCalendar.setTime(startApplicationDate);

            int startDayOfMonth = startCalendar.get(Calendar.DAY_OF_MONTH);
            dailyBalance = availableMoneyWithoutCosts/(daysOfCurrentMonth - startDayOfMonth);
        }
        else {
            dailyBalance = availableMoneyWithoutCosts/daysOfCurrentMonth;
        }
        return dailyBalance;
    }

    private static double getConstantCostsSum(List<ConstantCost> listOfConstantCosts){
        double constantCostsSum = 0.0;

        if (listOfConstantCosts.isEmpty()){
            return 0;
        }

        for (ConstantCost constantCost: listOfConstantCosts){
            constantCostsSum += constantCost.getValue();
        }

        return constantCostsSum;
    }

    private static double getTargetsSumThisMonth(List<Target> listOfTargets){

        double sumOfTargets = 0.0;
        int maxMonths = 1;

        Date targetDate;
        int diffMonths;

        if (listOfTargets.isEmpty()){
            return 0;
        }

        for (Target target: listOfTargets){
            sumOfTargets += target.getTargetValue();
            targetDate = DateUtils.string2Date(target.getEndDate());
            diffMonths = DateUtils.getMonthsCount(DateUtils.getTodayDate(), targetDate);
            if (diffMonths > maxMonths){
                maxMonths = diffMonths;
            }
        }

        return sumOfTargets / maxMonths;
    }

    private static double getDailyTargetsCosts(Context context) {
        // NOTE: today is not counted
        // NOTE: nie uwzględnia już opłaconych kwot
        // NOTE: bierze pod uwage jedynie od dzisiaj obliczenia kwot całościowych
        DBHandler dbHandler = DBHandler.getInstance(context);
        List<Target> targets = dbHandler.getAllTargets();

        Calendar c = Calendar.getInstance();

        String targetDate;
        long dayCount = 0;
        long nextTargetDayCount;

        long targetsCostsSum = 0;
        double dailyTargetsCosts = 0;

        for (Target target: targets){
            targetDate = target.getEndDate();

            SimpleDateFormat simpleDateFormat = DateUtils.getDateFormat();
            Date date = null;

            try {
                date = simpleDateFormat.parse(targetDate);
            }
            catch(ParseException e){

            }
            nextTargetDayCount = DateUtils.getDayCount(c.getTime(), date);
            if (nextTargetDayCount > dayCount) {
                dayCount = nextTargetDayCount;
            }
            targetsCostsSum += target.getTargetValue();
        }
        try {
            dailyTargetsCosts = targetsCostsSum / dayCount;
        }
        catch(ArithmeticException e){
            //TODO: change it!!!!!!!!!!!
            return 0;
        }

        return dailyTargetsCosts;
    }

    public static double getSumOfAllCostsThisMonth(List<Cost> costsListItems){
        double sumOfCostsThisMonth = 0.0;
        Date costDate;

        for (Cost cost: costsListItems){
            costDate = DateUtils.string2Date(cost.getDate());

            if (DateUtils.isDateInThisMonth(costDate)) {
                sumOfCostsThisMonth += cost.getValue();
            }
        }
        return sumOfCostsThisMonth;
    }

    public static List<Double> getPaymentsFromToday(Context context, Target target){
        Map<Target, List<Double>> allTargetsPayments = getAllTargetsPayments(context);

        Target checkTarget;
        Set mapKeys = allTargetsPayments.keySet();
        List<Double> foundTargetPaymentList;

        Iterator<Target> setIterator = mapKeys.iterator();
        while (setIterator.hasNext()){
            checkTarget = setIterator.next();
            if (checkTarget.getID() == target.getID()){
                Log.d(TAG, "Found the target in payments list");
                foundTargetPaymentList = allTargetsPayments.get(checkTarget);
                // Let's clear empty months from the end
                for (int i=foundTargetPaymentList.size()-1; i>0; i--){
                    if(foundTargetPaymentList.get(i) == 0.0){
                        foundTargetPaymentList.remove(i);
                        continue;
                    }
                    break;
                }
                Log.d(TAG, "Found target payments list: " + foundTargetPaymentList);
                Log.d(TAG, "Found target payments list: " + foundTargetPaymentList);
                return foundTargetPaymentList;
            }
        }
        return null;
    }

    public static Map<Target, List<Double>> getAllTargetsPayments(Context context){

        //TODO: trzeba uwzględnić żeby nie patrzeć od dzisiaj, ale od początku najwcześniejszego targetu

        Map<Target, List<Double>> paymentsMap = new HashMap();

        DBHandler dbHandler = DBHandler.getInstance(context);
        List<Target> sortedTargets = dbHandler.getTargetsSortedByDate();
        List<Double> allPayments = new ArrayList<>();
        List<Double> targetPayments = null;
        Date todayDate = DateUtils.getTodayDate();

        int monthsCount = DateUtils.getMonthsCount(todayDate,
                DateUtils.string2Date(sortedTargets.get(sortedTargets.size()-1).getEndDate()));
        Log.d(TAG, "Overall target payments months: " + monthsCount);
        if (monthsCount <= 0){
            return null;
        }

        for (int i=0; i<monthsCount; i++) {
            allPayments.add(0.0);
        }
        Log.d(TAG, "Initialized allPayments: " + allPayments);

        List<Double> newPaymentsList;

        for (Target sortedTarget: sortedTargets){
            targetPayments = new ArrayList<>();
            newPaymentsList = fillPaymentsList(allPayments, sortedTarget);
            Log.d(TAG, "Refilled targets payments list: " + newPaymentsList);
            restructurePaymentList(newPaymentsList);
            Log.d(TAG, "Restructured targets payments list: " + newPaymentsList);

            for (int i=0; i<allPayments.size(); i++){
                targetPayments.add(newPaymentsList.get(i) - allPayments.get(i));
            }
            allPayments = newPaymentsList;
            Log.d(TAG, "Added to new target the following payment list: " + targetPayments);
            paymentsMap.put(sortedTarget, targetPayments);
        }

        return paymentsMap;
    }

    private static boolean checkPaymentsList(List<Double> paymentsList){
        if (paymentsList.size() == 1){
            return true;
        }
        for (int i=1; i<paymentsList.size(); i++){
            if (paymentsList.get(i) > paymentsList.get(i-1)){
                return false;
            }
        }
        return true;
    }

    private static void restructurePaymentList(List<Double> paymentsList){
        if (checkPaymentsList(paymentsList)){
            return;
        }
        double lastPayment = 0.0;
        double previousPayment = 0.0;

        // Months and sum of money to restructure
        int lastMonthCount = 0;
        int previousMonthCount = 0;
        int startIndex = 0;
        int endIndex = 0;

        double sumOfPayments = 0.0;
        int sumOfMonths = 0;
        double currentPayment = 0.0;

        // get payments values and months counts
        for (int i=paymentsList.size()-1; i>=0; i--){
            currentPayment = paymentsList.get(i);
            if (currentPayment <= 0.0) {
                continue;
            }
            else {
                if (lastPayment == 0.0){
                    lastPayment = currentPayment;
                    endIndex = i;
                    lastMonthCount++;
                }
                else if (lastPayment == currentPayment){
                    lastMonthCount++;
                }
                else if (previousPayment == 0.0 && lastPayment > currentPayment){
                    previousPayment = currentPayment;
                    previousMonthCount++;
                }
                else if (previousPayment != 0.0 && previousPayment == currentPayment){
                    previousMonthCount++;
                }
                else {
                    startIndex = i+1;
                    break;
                }
            }
        }

        Log.d(TAG, "Restructuring payment list - start index: " + startIndex);
        Log.d(TAG, "Restructuring payment list - end index: " + endIndex);
        sumOfPayments = lastPayment*lastMonthCount + previousPayment*previousMonthCount;
        sumOfMonths = lastMonthCount + previousMonthCount;

        double moneyPerMonth = sumOfPayments / sumOfMonths;

        for (int i=startIndex; i<=endIndex; i++){
            paymentsList.set(i, moneyPerMonth);
        }
        restructurePaymentList(paymentsList);
    }


    private static List<Double> fillPaymentsList(List<Double> payments, Target target){

        List<Double> paymentsList = new ArrayList<>(payments);

        Date todayDate = DateUtils.getTodayDate();
        int targetPaymentMonthsCount = DateUtils.getMonthsCount(todayDate, DateUtils.string2Date(target.getEndDate()));
        double targetRemainingValue = target.getRemainingValue();

        // It is the first target to fill the list
        if (paymentsList.get(0) == 0.0){
            double targetPaymentPerMonth = targetRemainingValue / targetPaymentMonthsCount;
            for (int i=0; i<targetPaymentMonthsCount; i++){
                paymentsList.set(i, targetPaymentPerMonth);
            }
            return paymentsList;
        }

        // There are other target payments in the list
        // Getting how many months are filled already
        int monthsFilledCount = 0;
        for (int i=0; i<paymentsList.size(); i++){
            if (paymentsList.get(i) != 0.0){
                monthsFilledCount++;
                continue;
            }
            break;
        }
        Log.d(TAG, "Got months filled count: " + monthsFilledCount);
        Log.d(TAG, "Target payments months count: " + targetPaymentMonthsCount);

        // Checking whether the end date of out target is already filled, if so, we divide between
        // previous count
        if (monthsFilledCount == targetPaymentMonthsCount){
            Log.d(TAG, "Target payments months count is the same as the filled count");
            double lastPayment = 0.0;
            int startIndex = 0;
            int endIndex = 0;
            int sumOfMonths = 0;
            double currentPayment = 0.0;

            // get payments values and months counts
            for (int i=paymentsList.size()-1; i>=0; i--){
                currentPayment = paymentsList.get(i);
                if (currentPayment <= 0.0) {
                    continue;
                }
                else {
                    if (lastPayment == 0.0){
                        lastPayment = currentPayment;
                        endIndex = i;
                        sumOfMonths++;
                    }
                    else if (lastPayment == currentPayment){
                        sumOfMonths++;
                    }
                    else {
                        startIndex = i+1;
                        break;
                    }
                }
            }
            Log.d(TAG, "Filling payment list - start index: " + startIndex);
            Log.d(TAG, "Filling payment list - end index: " + endIndex);
            double moneyPerMonth = targetRemainingValue / sumOfMonths;
            for (int i=startIndex; i<=endIndex; i++){
                paymentsList.set(i, paymentsList.get(i) + moneyPerMonth);
            }
            return paymentsList;
        }
        else {
            int monthsDiff = targetPaymentMonthsCount - monthsFilledCount;
            double targetPaymentPerMonth = targetRemainingValue / monthsDiff;
            Log.d(TAG, "MonthsDiff: " + monthsDiff);
            for (int i=monthsFilledCount; i<targetPaymentMonthsCount; i++){
                Log.d(TAG, "Added to month " + i + " payment: " + targetPaymentPerMonth);
                paymentsList.set(i, paymentsList.get(i) + targetPaymentPerMonth);
            }
            return paymentsList;
        }
    }
}

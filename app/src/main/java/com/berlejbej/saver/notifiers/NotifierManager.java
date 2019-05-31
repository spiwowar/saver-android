package com.berlejbej.saver.notifiers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Szymon on 2017-01-28.
 */
public class NotifierManager {

    private static List<ChangeRecipient> recipients = new ArrayList<>();

    public static void addRecipient(ChangeRecipient recipient){
        recipients.add(recipient);
    }

    public static void notifyChange(){
        Iterator iterator = recipients.iterator();
        while (iterator.hasNext()){
            ((ChangeRecipient)iterator.next()).changePerformed();
        }
    }
}

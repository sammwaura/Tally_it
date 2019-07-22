package iobserver;

import org.json.JSONObject;

import java.util.List;

import beans.Stock;

/**
 * Created by Sir Edwin on 8/30/2015.
 */

public interface StockIObserver {
    // change signature of method as per your need
    void onCardClicked(int pos, String name);

    void notificationOpened(String message, JSONObject additionalData, boolean isActive);
}

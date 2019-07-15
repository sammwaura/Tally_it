package iobserver;

import java.util.List;

import beans.Stock;

/**
 * Created by Sir Edwin on 8/30/2015.
 */

public interface StockIObserver {
    // change signature of method as per your need
    void onCardClicked(int posi, String name,  List <Stock> stockFiltered);

}

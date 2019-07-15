package iobserver;

import android.view.View;

import java.util.List;

import beans.Category;

/**
 * Created by Sir Edwin on 8/30/2015.
 */

public interface CategoryIObserver {
    void onCategorySelected(Category category);

    // change signature of method as per your need
    void onCardClicked(int pos,  String name, List<Category> postFiltered);

}

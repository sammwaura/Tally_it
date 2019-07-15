package adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MySpinnerAdapterOther<S> extends ArrayAdapter<String> {
  // Initialise custom font, for example:
  Typeface font = Typeface.createFromAsset(getContext().getAssets(),
          "fonts/Lato-Light.ttf");

  // (In reality I used a manager which caches the Typeface objects)
  // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

  public MySpinnerAdapterOther(Context context, int resource, List<String> items) {
      super(context, resource, items);
  }

  // Affects default (closed) state of the spinner
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
      TextView view = (TextView) super.getView(position, convertView, parent);
      view.setTextColor(Color.parseColor("#a9aaac"));
      view.setTypeface(font);
      return view;
  }

  // Affects opened state of the spinner
  @Override
  public View getDropDownView(int position, View convertView, ViewGroup parent) {
      TextView view = (TextView) super.getDropDownView(position, convertView, parent);
      view.setTextColor(Color.parseColor("#444a59"));
      view.setTypeface(font);
      return view;
  }
}

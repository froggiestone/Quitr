package Utillities;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

/**
 * Created by parti on 18-12-2017.
 */

public class CravingsValueFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    public CravingsValueFormatter() {
        mFormat = new DecimalFormat("#");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        // write your logic here
        return mFormat.format(value);
    }
}

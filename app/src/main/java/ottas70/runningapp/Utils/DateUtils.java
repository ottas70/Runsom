package ottas70.runningapp.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Ottas on 10.12.2016.
 */

public class DateUtils {

    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        DateFormat formatData = new SimpleDateFormat("d.MM.yyyy");
        return formatData.format(calendar.getTime());
    }

}

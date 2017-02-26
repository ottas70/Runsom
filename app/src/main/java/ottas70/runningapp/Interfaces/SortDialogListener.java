package ottas70.runningapp.Interfaces;

import android.app.DialogFragment;

import ottas70.runningapp.Models.SortInfo;

/**
 * Created by ottovodvarka on 13.02.17.
 */

public interface SortDialogListener {
    public void onSortButtonClick(DialogFragment dialog, SortInfo sortInfo);
}

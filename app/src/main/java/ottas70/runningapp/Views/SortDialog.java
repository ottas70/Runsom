package ottas70.runningapp.Views;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

import ottas70.runningapp.Interfaces.MyDialogListener;
import ottas70.runningapp.Interfaces.SortDialogListener;
import ottas70.runningapp.R;

/**
 * Created by ottovodvarka on 13.02.17.
 */

public class SortDialog extends DialogFragment {

    private Button sortButton;

    private CheckBox type1;
    private CheckBox type2;
    private CheckBox type3;
    private CheckBox type4;
    private CheckBox type5;
    private CheckBox corporation;
    private CheckBox desc;
    private CheckBox asc;

    private EditText runner;
    private EditText minPrice;
    private EditText maxPrice;
    private EditText address;

    private SortDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_sortdialog, null);
        builder.setView(view);

        sortButton = (Button) view.findViewById(R.id.sortButton);

        type1 = (CheckBox) view.findViewById(R.id.type1CheckBox);
        type2 = (CheckBox) view.findViewById(R.id.type2CheckBox);
        type3 = (CheckBox) view.findViewById(R.id.type3CheckBox);
        type4 = (CheckBox) view.findViewById(R.id.type4CheckBox);
        type5 = (CheckBox) view.findViewById(R.id.type5CheckBox);
        corporation = (CheckBox) view.findViewById(R.id.corporationCheckBox);
        desc = (CheckBox) view.findViewById(R.id.priceDescCheckBox);
        asc = (CheckBox) view.findViewById(R.id.priceAscCheckBox);

        runner = (EditText) view.findViewById(R.id.runnerEditText);
        minPrice = (EditText) view.findViewById(R.id.minPriceEditText);
        maxPrice = (EditText) view.findViewById(R.id.maxPriceEditText);
        address = (EditText) view.findViewById(R.id.buildingAddressEditText);

        runner.getBackground().setColorFilter(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.backgroundPrimary),
                PorterDuff.Mode.SRC_IN);
        minPrice.getBackground().setColorFilter(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.backgroundPrimary),
                PorterDuff.Mode.SRC_IN);
        maxPrice.getBackground().setColorFilter(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.backgroundPrimary),
                PorterDuff.Mode.SRC_IN);
        address.getBackground().setColorFilter(ContextCompat.getColor(getActivity().getApplicationContext(), R.color.backgroundPrimary),
                PorterDuff.Mode.SRC_IN);

        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSortButtonClick(SortDialog.this);
            }
        });


        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (SortDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SortDialogListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            listener = (SortDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement SortDialogListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}

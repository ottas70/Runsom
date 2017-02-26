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

import java.lang.reflect.Field;
import java.util.ArrayList;

import ottas70.runningapp.Interfaces.SortDialogListener;
import ottas70.runningapp.R;
import ottas70.runningapp.Models.SortInfo;
import ottas70.runningapp.SortInfoBuilder;

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
    private CheckBox every;
    private CheckBox desc;
    private CheckBox asc;

    private EditText runner;
    private EditText minPrice;
    private EditText maxPrice;
    private EditText address;

    private SortDialogListener listener;
    private SortInfo sortInfo;
    private SortInfo startSortInfo;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_sortdialog, null);
        builder.setView(view);

        startSortInfo = (SortInfo) getArguments().getSerializable("sortInfo");

        sortButton = (Button) view.findViewById(R.id.sortButton);

        type1 = (CheckBox) view.findViewById(R.id.type1CheckBox);
        type2 = (CheckBox) view.findViewById(R.id.type2CheckBox);
        type3 = (CheckBox) view.findViewById(R.id.type3CheckBox);
        type4 = (CheckBox) view.findViewById(R.id.type4CheckBox);
        type5 = (CheckBox) view.findViewById(R.id.type5CheckBox);
        every = (CheckBox) view.findViewById(R.id.corporationCheckBox);
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

        initializeDialog();

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(asc.isChecked() && desc.isChecked()){
                    desc.setChecked(false);
                }
            }
        });

        asc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(desc.isChecked() && asc.isChecked()){
                    asc.setChecked(false);
                }
            }
        });


        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSortInfo();
                listener.onSortButtonClick(SortDialog.this,sortInfo);
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

    private void createSortInfo(){
        SortInfoBuilder builder = new SortInfoBuilder();
        builder.setTypes(getTypes());
        if(every.isChecked()){
            builder.setEvery(true);
        }else{
            builder.setEvery(false);
        }
        if(!runner.getText().toString().equals("")){
            builder.setRunner(runner.getText().toString());
        }
        if(asc.isChecked()){
            builder.setAsc(true);
        }else{
            builder.setAsc(false);
        }
        if(desc.isChecked()){
            builder.setDesc(true);
        }else{
            builder.setDesc(false);
        }
        if(!minPrice.getText().toString().equals("")){
            builder.setMinPrice(Integer.parseInt(minPrice.getText().toString()));
        }else{
            builder.setMinPrice(-1);
        }
        if(!maxPrice.getText().toString().equals("")){
            builder.setMaxPrice(Integer.parseInt(maxPrice.getText().toString()));
        }else{
            builder.setMaxPrice(-1);
        }
        if(!address.getText().toString().equals("")){
            builder.setAddress(address.getText().toString());
        }
        sortInfo = builder.createSortInfo();
    }

    private ArrayList<Integer> getTypes(){
        ArrayList<Integer> types = new ArrayList<>();
        if(type1.isChecked()){
            types.add(1);
        }
        if(type2.isChecked()){
            types.add(2);
        }
        if(type3.isChecked()){
            types.add(3);
        }
        if(type4.isChecked()){
            types.add(4);
        }
        if(type5.isChecked()){
            types.add(5);
        }
        return types;
    }

    private void initializeDialog(){
        if(startSortInfo.getTypes().contains(1)){
            type1.setChecked(true);
        }
        if(startSortInfo.getTypes().contains(2)){
            type2.setChecked(true);
        }
        if(startSortInfo.getTypes().contains(3)){
            type3.setChecked(true);
        }
        if(startSortInfo.getTypes().contains(4)){
            type4.setChecked(true);
        }
        if(startSortInfo.getTypes().contains(5)){
            type5.setChecked(true);
        }
        if(startSortInfo.isEvery()) {
            every.setChecked(true);
        }
        if( startSortInfo.getRunner() != null){
            runner.setText(startSortInfo.getRunner());
        }
        if(startSortInfo.isAsc()){
            asc.setChecked(true);
        }
        if(startSortInfo.isDesc()){
            desc.setChecked(true);
        }
        if(startSortInfo.getMinPrice() != -1){
            minPrice.setText(String.valueOf(startSortInfo.getMinPrice()));
        }
        if(startSortInfo.getMaxPrice() != -1){
            maxPrice.setText(String.valueOf(startSortInfo.getMaxPrice()));
        }
        if(startSortInfo.getAddress() != null){
            address.setText(startSortInfo.getAddress());
        }
    }

}

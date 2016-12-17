package ottas70.runningapp.Views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Field;

import ottas70.runningapp.Interfaces.MyDialogListener;
import ottas70.runningapp.R;

/**
 * Created by ottovodvarka on 14.12.16.
 */

public class FinishRunDialog extends DialogFragment {

    private TextView title;
    private TextView message;
    private Button negativeButton;

    private MyDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_finish_run_dialog, null);
        builder.setView(view);

        title = (TextView) view.findViewById(R.id.titleTextView);
        message = (TextView) view.findViewById(R.id.messageTextView);
        negativeButton = (Button) view.findViewById(R.id.negativeButton);

        title.setText(getArguments().getString("title"));
        message.setText(getArguments().getString("message"));

        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onDialogNegativeClick(FinishRunDialog.this);
            }
        });

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (MyDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement MyDialogListener");
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

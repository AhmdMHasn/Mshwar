package eg.com.iti.mshwar.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import eg.com.iti.mshwar.R;

public class AddNoteDialog extends DialogFragment {

    private static final String TAG = "AddNoteDialog";

    //widgets

    private EditText editTxtNoteDescription;

    private AddNoteDialogListener mAddNoteDialogListener;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_note, null);
        editTxtNoteDescription = view.findViewById(R.id.note_description);
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String noteDescription = editTxtNoteDescription.getText().toString();
                          mAddNoteDialogListener.makeNote(noteDescription);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });


        return builder.create();
    }

    public interface AddNoteDialogListener {
        void makeNote(String noteDescription);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mAddNoteDialogListener = (AddNoteDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement NoticeDialogListener");
        }
    }


    private boolean isEmpty(String string) {
        return string.equals("");
    }
}

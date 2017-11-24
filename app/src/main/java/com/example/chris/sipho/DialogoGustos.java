package com.example.chris.sipho;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by salv8 on 21/11/2017.
 */

public class DialogoGustos extends DialogFragment {
    ArrayList mSelectedItems;
    List cargar = new ArrayList();
    boolean[] selec= new boolean[9];
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mSelectedItems = new ArrayList();  // Where we track the selected items
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        TinyDB tinydb = new TinyDB(getActivity());
        cargar=tinydb.getListInt("SIPHO_GUSTOS");
        if(cargar.size()!=0){
            for (int i=0;i<cargar.size();i++){
                String b = cargar.get(i).toString();
                int a = Integer.valueOf(b);
                selec[a]=true;
                mSelectedItems.add(a);
            }
        }else{
            Arrays.fill(selec,true);
            for(int v=0;v<9;v++){
                mSelectedItems.add(v);

            }
        }

        // Set the dialog title
        builder.setTitle(R.string.gusto)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setMultiChoiceItems(R.array.opcionesCategoria, selec,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems.add(which);

                                } else if (mSelectedItems.contains(which)) {
                                    // Else, if the item is already in the array, remove it
                                    mSelectedItems.remove(Integer.valueOf(which));
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.aceptar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        TinyDB tinydb2 = new TinyDB(getActivity());
                        tinydb2.putListInt("SIPHO_GUSTOS",mSelectedItems);
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();

                    }
                });

        return builder.create();
    }

}

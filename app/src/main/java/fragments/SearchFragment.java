package fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import adapters.SwipeToDissmissAdapter;
import adapters.SwipeToDissmissAdapterSearchNotepad;
import db.PropertyVisits;
import db.PropertyVisits_Table;
import db.SearchNotepad;
import db.SearchNotepad_Table;
import localestates.localestates.AdvanceSearchActivity;
import localestates.localestates.R;
import localestates.localestates.SearchResultActivity;
import utils.HelpFunctions;

/**
 * Created by macbook on 2/18/16.
 */
public class SearchFragment extends Fragment {

    private List<SearchNotepad> allPropertyVisitsAdvertsObj;
    private DynamicListView mDynamicListView;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allPropertyVisitsAdvertsObj = SQLite.select()
                .from(SearchNotepad.class)
                .where()
                .orderBy(SearchNotepad_Table.search_add_time, true)
                .queryList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_notepad, container, false);

        mDynamicListView = (DynamicListView)rootView.findViewById(R.id.showAllSearchNotepadListView);
        mDynamicListView.setDividerHeight(0);

        final SwipeToDissmissAdapterSearchNotepad adapter = new SwipeToDissmissAdapterSearchNotepad(getContext(), allPropertyVisitsAdvertsObj);
        SimpleSwipeUndoAdapter swipeUndoAdapter = new SimpleSwipeUndoAdapter(adapter, getContext(), new OnDismissCallback() {
            @Override
            public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    adapter.remove(position);
                }
            }
        });
        swipeUndoAdapter.setAbsListView(mDynamicListView);
        mDynamicListView.setAdapter(swipeUndoAdapter);
        mDynamicListView.enableSimpleSwipeUndo();

        mDynamicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final SearchNotepad selectedSearchNotepad = allPropertyVisitsAdvertsObj.get(position);

                final Dialog dialog=new Dialog(getActivity(),android.R.style.Theme_DeviceDefault_Light_NoActionBar);
                dialog.setContentView(R.layout.notepad_search_add_pop_up);
                final EditText titleSearch = (EditText) dialog.findViewById(R.id.titleSearch);
                final EditText noteText = (EditText) dialog.findViewById(R.id.noteText);
                TextView saveAppDialog = (TextView) dialog.findViewById(R.id.saveSearchDialog);
                TextView closeAppointmentDialog = (TextView) dialog.findViewById(R.id.closeSearchDialog);
                TextView makeSearchSearchDialog = (TextView) dialog.findViewById(R.id.makeSearchSearchDialog);
                TextView deleteSearchDialog = (TextView) dialog.findViewById(R.id.deleteSearchDialog);

                makeSearchSearchDialog.setVisibility(View.VISIBLE);
                deleteSearchDialog.setVisibility(View.VISIBLE);

                titleSearch.setText(selectedSearchNotepad.search_title);
                noteText.setText(selectedSearchNotepad.search_note);

                // Check if no view has focus:
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                saveAppDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SQLite.update(SearchNotepad.class)
                                .set(SearchNotepad_Table.search_title.eq(titleSearch.getText().toString()),
                                    SearchNotepad_Table.search_note.eq(noteText.getText().toString()))
                                .where(SearchNotepad_Table.search_auto_id.is(selectedSearchNotepad.search_auto_id))
                                .query();
                        Toast.makeText(getContext(),"Търсенето беше успешно запазено в бележника!",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                });

                closeAppointmentDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                makeSearchSearchDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        String searchString = selectedSearchNotepad.search_content;
                        Log.e("HEREHERE",searchString);
                        ArrayList<HashMap<String,String>> searchValues  = HelpFunctions.convertStringToHashSaearch(searchString);
                        Intent advanceSearchResultIntent = new Intent(getActivity(),AdvanceSearchActivity.class);
                        advanceSearchResultIntent.putExtra("searchValues",searchValues);
                        getActivity().finish();
                        startActivity(advanceSearchResultIntent);
                    }
                });

                deleteSearchDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialoglayout = inflater.inflate(R.layout.dialog_delete_search_notepad, null);
                        final AlertDialog.Builder builderDialog = new AlertDialog.Builder(getContext());
                        builderDialog.setView(dialoglayout);
                        final AlertDialog show = builderDialog.show();
                        Button closeButton = (Button) dialoglayout.findViewById(R.id.closeAppointmentButton);
                        closeButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                show.dismiss();
                            }
                        });
                        Button callButton= (Button) dialoglayout.findViewById(R.id.deleteAppointmentButton);
                        callButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                show.dismiss();
                                SQLite.delete()
                                        .from(SearchNotepad.class)
                                        .where(SearchNotepad_Table.search_auto_id.is(selectedSearchNotepad.search_auto_id))
                                        .query();

                                Toast.makeText(getContext(),"Търсенето беше изтрито от бележника!",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                allPropertyVisitsAdvertsObj.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        });

                        dialog.dismiss();
                    }
                });

                dialog.show();





            }
        });

        return rootView;
    }
}

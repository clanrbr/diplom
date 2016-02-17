package fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
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
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import adapters.SwipeToDissmissAdapter;
import db.PropertyVisits;
import db.PropertyVisits_Table;
import localestates.localestates.R;

/**
 * Created by macbook on 2/15/16.
 */
public class PropertiesViewsFragment  extends Fragment {

    private  List<PropertyVisits> allPropertyVisitsAdvertsObj;
    private DynamicListView mDynamicListView;
    private SimpleDateFormat dateFormatter;
    private String advertID;
    private String advertTitle;
    private String advertPhone;
    private String advertContentString;

    public static PropertiesViewsFragment newInstance() {
        return new PropertiesViewsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        allPropertyVisitsAdvertsObj = SQLite.select()
                .from(PropertyVisits.class)
                .where()
                .orderBy(PropertyVisits_Table.added_time, true)
                .queryList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_property_view, container, false);

        mDynamicListView = (DynamicListView)rootView.findViewById(R.id.showAllPropertyViewsListView);
        mDynamicListView.setDividerHeight(0);

        final SwipeToDissmissAdapter adapter = new SwipeToDissmissAdapter(getContext(), allPropertyVisitsAdvertsObj);
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
                advertID=allPropertyVisitsAdvertsObj.get(position).advert_id;
                advertContentString=allPropertyVisitsAdvertsObj.get(position).advert_list;

                if (advertContentString!=null) {
                    try {
                        JSONObject advertContent = new JSONObject(advertContentString);
                        if ( advertContent.has("id") ) {
                            advertID=advertContent.getString("id");
                        }

                        if ( advertContent.has("phone") ) {
                            advertPhone=advertContent.getString("phone");
                        }

                        if (advertContent.has("rub")) {
                            String value = advertContent.getString("rub");
                            if (advertContent.has("type_home")) {
                                value = value + " " + advertContent.getString("type_home");
                            }
                            advertTitle=value;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


                final Dialog dialog=new Dialog(getContext(),android.R.style.Theme_DeviceDefault_Light_NoActionBar);
                dialog.setContentView(R.layout.notepad_appointment_add_pop_up);
                TextView titleAdvert = (TextView) dialog.findViewById(R.id.titleAdvert);
                final EditText noteText = (EditText) dialog.findViewById(R.id.noteText);
                final EditText appointmentDatePick = (EditText) dialog.findViewById(R.id.appointmentDatePick);
                final EditText appointmentHourPick = (EditText) dialog.findViewById(R.id.appointmentHourPick);
                TextView saveAppDialog = (TextView) dialog.findViewById(R.id.saveAppointmentDialog);
                TextView closeAppointmentDialog = (TextView) dialog.findViewById(R.id.closeAppointmentDialog);
                TextView deleteAppointmentDialog = (TextView) dialog.findViewById(R.id.deleteAppointmentDialog);
                final EditText additionalPhone = (EditText) dialog.findViewById(R.id.additionalPhone);

                // Check if no view has focus:
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                titleAdvert.setText(advertTitle);

                final Calendar newCalendar = Calendar.getInstance();
                appointmentDatePick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePickerDialog fromDatePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(year, monthOfYear, dayOfMonth);
                                dateFormatter = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
                                appointmentDatePick.setText(dateFormatter.format(newDate.getTime()));
                            }

                        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
                        fromDatePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Запази", fromDatePickerDialog);
                        fromDatePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Затвори", fromDatePickerDialog);
                        fromDatePickerDialog.show();
                    }
                });

                appointmentHourPick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TimePickerDialog fromDatePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Calendar newDate = Calendar.getInstance();
                                newDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                newDate.set(Calendar.MINUTE, minute);
                                dateFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());
                                appointmentHourPick.setText(dateFormatter.format(newDate.getTime()));
                            }

                        },newCalendar.get(Calendar.HOUR), newCalendar.get(Calendar.MINUTE), DateFormat.is24HourFormat(getContext()));
                        fromDatePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Запази", fromDatePickerDialog);
                        fromDatePickerDialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Затвори", fromDatePickerDialog);
                        fromDatePickerDialog.show();
                    }
                });

                final PropertyVisits existsAdvert = SQLite.select()
                        .from(PropertyVisits.class)
                        .where(PropertyVisits_Table.advert_id.is(advertID))
                        .querySingle();

                if (existsAdvert!=null) {
                    deleteAppointmentDialog.setVisibility(View.VISIBLE);
                    appointmentDatePick.setText(existsAdvert.advert_appointment_time_date);
                    appointmentHourPick.setText(existsAdvert.advert_appointment_time_hour);
                    additionalPhone.setText(existsAdvert.advert_phone_number);
                    noteText.setText(existsAdvert.advert_note);
                } else {
                    additionalPhone.setText(advertPhone);
                }

                saveAppDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                            SQLite.update(PropertyVisits.class)
                                    .set(PropertyVisits_Table.advert_list.eq(allPropertyVisitsAdvertsObj.get(position).advert_list),
                                            PropertyVisits_Table.advert_note.eq(noteText.getText().toString()),
                                            PropertyVisits_Table.advert_appointment_time_date.eq(appointmentDatePick.getText().toString()),
                                            PropertyVisits_Table.advert_appointment_time_hour.eq(appointmentHourPick.getText().toString()),
                                            PropertyVisits_Table.advert_phone_number.eq(additionalPhone.getText().toString()),
                                            PropertyVisits_Table.advert_additional_address.eq(appointmentHourPick.getText().toString()))
                                    .where(PropertyVisits_Table.advert_id.is(advertID))
                                    .query();
                            Toast.makeText(getContext(),"Обявата беше успешно запазена в огледи!",Toast.LENGTH_LONG).show();

                        dialog.dismiss();
                    }
                });

                closeAppointmentDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                deleteAppointmentDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LayoutInflater inflater = getActivity().getLayoutInflater();
                        View dialoglayout = inflater.inflate(R.layout.dialog_delete_appointment, null);
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
                                        .from(PropertyVisits.class)
                                        .where(PropertyVisits_Table.advert_id.is(advertID))
                                        .query();

                                Toast.makeText(getContext(),"Обявата беше успешно изтрита от огледи!",Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                allPropertyVisitsAdvertsObj.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                });


                dialog.show();
            }
        });

        return rootView;
    }
}
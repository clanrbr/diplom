package localestates.localestates;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import adapters.PropertyTypeAdapter;
import adapters.SimpleMultiChoiceAdapter;
import interfaces.AsyncResponse;
import interfaces.AsyncResponseBuildType;
import interfaces.AsyncResponseElectricity;
import interfaces.AsyncResponseExtri;
import interfaces.AsyncResponseFloor;
import interfaces.AsyncResponseLandCategory;
import interfaces.AsyncResponseLandLeaseContract;
import interfaces.AsyncResponseLandPermanentUsage;
import interfaces.AsyncResponsePhone;
import interfaces.AsyncResponseRegulation;
import interfaces.AsyncResponseTec;
import interfaces.AsyncResponseWater;
import localEstatesHttpRequests.HTTPGETBuildType;
import localEstatesHttpRequests.HTTPGETElectricity;
import localEstatesHttpRequests.HTTPGETExtri;
import localEstatesHttpRequests.HTTPGETFloor;
import localEstatesHttpRequests.HTTPGETLandCategory;
import localEstatesHttpRequests.HTTPGETLandLeaseContract;
import localEstatesHttpRequests.HTTPGETLandPermanentUsage;
import localEstatesHttpRequests.HTTPGETPhone;
import localEstatesHttpRequests.HTTPGETRegulation;
import localEstatesHttpRequests.HTTPGETTec;
import localEstatesHttpRequests.HTTPGETWater;
import localEstatesHttpRequests.HTTPGetProperties;
import localEstatesHttpRequests.MakeASearchHttpRequest;
import utils.ConstantsImportant;
import utils.HelpFunctions;

/**
 * Created by macbook on 1/19/16.
 */
public class AdvanceSearchActivity extends ActionBarActivity implements
        View.OnClickListener, AsyncResponse, AsyncResponseExtri, AsyncResponseFloor,
        AsyncResponseBuildType, AsyncResponseTec, AsyncResponsePhone, AsyncResponseElectricity,
        AsyncResponseWater, AsyncResponseRegulation, AsyncResponseLandPermanentUsage, AsyncResponseLandCategory,
        AsyncResponseLandLeaseContract {
    public static final String ARG_SECTION_NUMBER = "section_number";

    private LinearLayout secondTownSpinnerLayout;
    private ScrollView scrollView;
    private RadioGroup radioGroup;
    private int typeAdvert=1;
    private ArrayList<JSONObject> advertsJsonArray = new ArrayList<JSONObject>();
    private MakeASearchHttpRequest asyncTask;
    private HTTPGETExtri asyncTaskGetExtri;
    private HTTPGETFloor asyncTaskGetFloor;
    private HTTPGETBuildType asyncTaskGetBuildType;
    private HTTPGETTec asyncTaskGetTec;
    private HTTPGETPhone asyncTaskGetPhone;
    private HTTPGETElectricity asyncTaskGetElectricity;
    private HTTPGETWater asyncTaskGetWater;
    private HTTPGETRegulation asyncTaskGetRegulation;
    private HTTPGETLandPermanentUsage asyncTaskGetlandPermanentUsage;
    private HTTPGETLandCategory asyncTaskGetLandCategory;
    private HTTPGETLandLeaseContract asyncTaskGetLandLeaseContract;
    private ArrayList<HashMap<String,String>> searchValues;
    private ArrayList<HashMap<String, String>> searchValuesEdit;

    private EditText priceFrom;
    private EditText priceTo;
    private EditText areaFrom;
    private EditText areaTo;

    private RelativeLayout moreSearchOptions;

    private ArrayList<String> secondTownArray;
    private ArrayAdapter<CharSequence> adapter;
    private ArrayAdapter<String> spinnerAdapter;
    private Spinner townSpinner;
    private Spinner secondTownSpinner;
    private TextView choosePropertyType;
    private ArrayList<String> extriArray;
    private ArrayList<String> buildTypeArray;

    // Group 1 additional fields
    private ArrayList<CharSequence> floorArray;
    private Spinner floorFromSpinner;
    private ArrayAdapter<CharSequence> floorFromAdapter;
    private Spinner floorToSpinner;
    private ArrayAdapter<CharSequence> floorToAdapter;
    private EditText yearFrom;
    private EditText yearTo;
    private Spinner tecSpinner;
    private ArrayAdapter<CharSequence> tecAdapter;
    private ArrayList<CharSequence> tecArray;
    private Spinner phoneSpinner;
    private ArrayAdapter<CharSequence> phoneAdapter;
    private ArrayList<CharSequence> phoneArray;
    private TextView extriProperty;
    private TextView buildTypeProperty;
    private PropertyTypeAdapter adapterPropertiesType;

    // Electricity
    private Spinner electricitySpinner;
    private ArrayAdapter<CharSequence> electricityAdapter;
    private ArrayList<CharSequence> electricityArray;

    // Water
    private Spinner waterSpinner;
    private ArrayAdapter<CharSequence> waterAdapter;
    private ArrayList<CharSequence> waterArray;

    // Regulation
    private Spinner regulationSpinner;
    private ArrayAdapter<CharSequence> regulationAdapter;
    private ArrayList<CharSequence> regulationArray;

    // Land Permanent Usage
    private ArrayList<String> landPermanentUsageArray;
    public ArrayList<String> landPermanentUsageSelectedValue;
    private TextView landPermanentUsageProperty;

    // Land Category
    private ArrayList<String> landCategoryArray;
    public ArrayList<String> landCategorySelectedValue;
    private TextView landCategoryProperty;

    //    Land Lease Contract
    private Spinner landLeaseContractSpinner;
    private ArrayAdapter<CharSequence> landLeaseContractAdapter;
    private ArrayList<CharSequence> landLeaseContractArray;


    private Spinner sortResult;
    private ArrayAdapter<CharSequence> sortAdapter;
    public static ArrayList<String> checkboxSelectedValue;
    public ArrayList<String> extriSelectedValue;
    public ArrayList<String> buildTypeSelectedValue;
    private int groupNumber;
    private ArrayList<String> propertyTypes;


    private TextView searchAdvancedButton;
    private TextView clearButton;
    private CircularProgressBar progressBar;

    private int asyncStarted=0;


    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.main_color_700));
        }
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_advance_search);


        scrollView = (ScrollView) findViewById(R.id.scrollViewParent);
        ImageView menuItemSearch = (ImageView) findViewById(R.id.searchActionBar);
        menuItemSearch.setImageResource(R.drawable.ic_search_white_24dp);
        ImageView menuItemFavourite = (ImageView) findViewById(R.id.favouriteActionBar);
        ImageView menuItemNotification = (ImageView) findViewById(R.id.notificationActionBar);
        ImageView menuItemHome = (ImageView) findViewById(R.id.homeActionBar);

        menuItemHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favouriteIntent = new Intent(getBaseContext(),StartActivity.class);
                finish();
                startActivity(favouriteIntent);
            }
        });

        menuItemFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favouriteIntent = new Intent(getBaseContext(),FavouriteActivity.class);
                finish();
                startActivity(favouriteIntent);
            }
        });

        menuItemNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favouriteIntent = new Intent(getBaseContext(),FavouriteActivity.class);
                finish();
                startActivity(favouriteIntent);

            }
        });

        menuItemSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        progressBar = (CircularProgressBar) findViewById(R.id.progressBar);

//        asyncTaskGetExtri = new HTTPGETExtri(progressBar,asyncStarted);
//        asyncTaskGetFloor = new HTTPGETFloor(progressBar,asyncStarted);
//        asyncTaskGetBuildType = new HTTPGETBuildType(progressBar,asyncStarted);
//        asyncTaskGetTec = new HTTPGETTec(progressBar,asyncStarted);
//        asyncTaskGetPhone = new HTTPGETPhone(progressBar,asyncStarted);
//        asyncTaskGetElectricity = new HTTPGETElectricity(progressBar,asyncStarted);
//        asyncTaskGetWater = new HTTPGETWater(progressBar,asyncStarted);
//        asyncTaskGetRegulation = new HTTPGETRegulation(progressBar,asyncStarted);
//        asyncTaskGetlandPermanentUsage = new HTTPGETLandPermanentUsage(progressBar,asyncStarted);
//        asyncTaskGetLandCategory = new HTTPGETLandCategory(progressBar,asyncStarted);
//        asyncTaskGetLandLeaseContract = new HTTPGETLandLeaseContract(progressBar,asyncStarted);
//        asyncTask = new MakeASearchHttpRequest(progressBar,0);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            searchValuesEdit=null;
        } else {
            searchValuesEdit = (ArrayList<HashMap<String, String>>) extras.getSerializable("searchValues");
        }

        checkboxSelectedValue=new ArrayList<>();

        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        if ( searchValuesEdit!=null ) {
            Log.e("HEREHERE",searchValuesEdit.get(0).get("rub"));
            Integer rubValue =  Integer.parseInt(searchValuesEdit.get(0).get("rub"));
            Log.e("HEREHERE",String.valueOf(rubValue));
            if (rubValue==1) {
                radioGroup.check(R.id.sell_radio_button);
                typeAdvert=1;
            } else {
                radioGroup.check(R.id.rent_radio_button);
                typeAdvert=2;
            }
        } else {
            radioGroup.check(R.id.sell_radio_button);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.sell_radio_button) {
                    typeAdvert=1;
//                    searchValuesEdit=null;
                } else {
                    typeAdvert=2;
//                    searchValuesEdit=null;
                }
            }
        });

        townSpinner = (Spinner) findViewById(R.id.town_spinner);
        secondTownSpinnerLayout = (LinearLayout) findViewById(R.id.secondTownSpinner);
        secondTownSpinner = (Spinner) findViewById(R.id.second_town_spinner);
        sortResult = (Spinner) findViewById(R.id.sortResult);
        moreSearchOptions = (RelativeLayout) findViewById(R.id.moreSearchOptions);

        searchAdvancedButton = (TextView) findViewById(R.id.searchAdvancedButton);
        clearButton = (TextView) findViewById(R.id.clearButton);


        // First Adapter for Towns
        townSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            HTTPGetProperties getProperty;
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] mTestArray = getResources().getStringArray(R.array.town);
                String urlBuild="http://api.imot.bg/mobile_api/dictionary/raion?town="+ Uri.encode(mTestArray[position]);

                if ( position==0 ) {
                    secondTownSpinnerLayout.setVisibility(View.GONE);
                } else {
                    getProperty = new HTTPGetProperties(progressBar) {
                        @Override
                        protected void onPostExecute(String result) {
                            if (result != null) {
                                try {
                                    JSONArray jsonArray = new JSONArray(result);
                                    secondTownArray = new ArrayList<>();
                                    secondTownArray.add("Всички");
                                    for(int i = 0, count = jsonArray.length(); i< count; i++) {
                                        try {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            if ( jsonObject!=null ) {
                                                secondTownArray.add(jsonObject.getString("value"));
                                            }
                                        }
                                        catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    secondTownSpinnerLayout.setVisibility(View.VISIBLE);
                                    spinnerAdapter.clear();
                                    spinnerAdapter.addAll(secondTownArray);
                                    if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>3) && (searchValuesEdit.get(3)!=null) ) {
                                        String rubValue =  searchValuesEdit.get(3).get("raioni");
                                        if (rubValue!=null) {
                                            for(int i=0;i<secondTownArray.size();i++) {
                                                if ( secondTownArray.get(i).equals(rubValue) ) {
                                                    secondTownSpinner.setSelection(i);
                                                    break;
                                                }
                                            }
                                        }
                                    } else {
                                        secondTownSpinner.setSelection(0);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Log.e("HEREHERE", "EMPTY");
                            }
                        }
                    };
                    getProperty.execute(urlBuild);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });
        adapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.town, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        townSpinner.setAdapter(adapter);
        if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>2) && (searchValuesEdit.get(2)!=null) ) {
            String rubValue =  searchValuesEdit.get(2).get("town");
            if (rubValue!=null) {
                ArrayList<String> arrayValueTown = new ArrayList<String>();
                Collections.addAll(arrayValueTown, getResources().getStringArray(R.array.town));
                for ( int i=0;i<arrayValueTown.size();i++ ) {
                    if ( arrayValueTown.get(i).equals(rubValue) ) {
                        townSpinner.setSelection(i);
                        break;
                    }
                }
            }
        } else {
            townSpinner.setSelection(0);
        }

        spinnerAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, android.R.id.text1);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        secondTownSpinner.setAdapter(spinnerAdapter);

        propertyTypes = new ArrayList<>();
        if ( typeAdvert==1 ) {
            Log.e("HEREHERE","LOAD 1");
            Collections.addAll(propertyTypes, getResources().getStringArray(R.array.sell_property_types));
        } else {
            Log.e("HEREHERE","LOAD 2");
            Collections.addAll(propertyTypes, getResources().getStringArray(R.array.rent_property_types));
        }
        choosePropertyType = (TextView) findViewById(R.id.choosePropertyType);
        if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>4) && (searchValuesEdit.get(4)!=null) ) {
            String rubValue =  searchValuesEdit.get(4).get("type_home");
            if (rubValue!=null) {
                String[] parts = rubValue.split(",");
                if (parts.length>0) {
                    for ( int i=0;i<parts.length;i++) {
                        Iterator it;
                        if (typeAdvert==1) {
                            it = ConstantsImportant.orderTypeHome.entrySet().iterator();
                        } else {
                            it = ConstantsImportant.orderTypeHomeRent.entrySet().iterator();
                        }
                        while ( it.hasNext() ) {
                            Map.Entry pair = (Map.Entry)it.next();
                            if ( parts[i].equals(pair.getValue().toString()) ) {
                                Log.e("HEREHERE","DADA");
                                Log.e("HEREHERE",parts[i]);
                                Log.e("HEREHERE",pair.getValue().toString());
                                Log.e("HEREHERE",pair.getKey().toString());
                                rubValue=rubValue.replace(parts[i].toString(),pair.getKey().toString());
                            }
                        }
                    }
                }

                choosePropertyType.setText(rubValue);

                if (rubValue!="") {
                    groupNumber=0;
                    String [] propertyTypeFullName = rubValue.split(",");
                    for (int i=0;i<propertyTypeFullName.length;i++) {
                        if ( groupNumber==0 ) {
                            groupNumber= HelpFunctions.returnGroupNumberOfProperty(propertyTypeFullName[i],null);
                        }
                    }

                    if ( groupNumber>0 ) {
                        moreSearchOptions.removeAllViews();
                        View optionView = HelpFunctions.loadCorrectView(AdvanceSearchActivity.this,groupNumber);
                        moreSearchOptions.addView(optionView);

                        // get extras
                        extriProperty = (TextView) optionView.findViewById(R.id.extriProperty);
                        int extriGroup= HelpFunctions.returnExtriGroup(typeAdvert,groupNumber);
                        String urlBuildExtri="http://api.imot.bg/mobile_api/dictionary/extri?type_extri="+String.valueOf(extriGroup);
                        asyncStarted++;
                        asyncTaskGetExtri = new HTTPGETExtri(progressBar,asyncStarted);
                        asyncTaskGetExtri.delegate=AdvanceSearchActivity.this;
                        asyncTaskGetExtri.execute(urlBuildExtri);

                        if (groupNumber==1) {
                            loadGroup1(optionView);
                        } else if (groupNumber==2) {
                            loadGroup2(optionView);
                        } else if (groupNumber==3) {
                            loadGroup3(optionView);
                        } else if (groupNumber==4) {
                            loadGroup4(optionView);
                        } else if (groupNumber==5) {
                            loadGroup5(optionView);
                        } else if (groupNumber==6) {
                            loadGroup6(optionView);
                        } else {
                            loadGroup1(optionView);
                        }
                    } else {
                        moreSearchOptions.removeAllViews();
                    }
                }
            }
        }

        choosePropertyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( typeAdvert==1 ) {
                    propertyTypes = new ArrayList<String>();
                    Collections.addAll(propertyTypes, getResources().getStringArray(R.array.sell_property_types));
                } else {
                    propertyTypes = new ArrayList<String>();
                    Collections.addAll(propertyTypes, getResources().getStringArray(R.array.rent_property_types));
                }

                if ( (choosePropertyType.getText()!=null) && (choosePropertyType.getText().length()>0) && (!choosePropertyType.getText().toString().toUpperCase().contains("ВСИЧКИ")) ) {
                    String getSelectedValuesPropertyType = choosePropertyType.getText().toString();
                    checkboxSelectedValue=new ArrayList<>();
                    if ( getSelectedValuesPropertyType.contains(",") ) {
                        String[] parts = getSelectedValuesPropertyType.split(",");
                        for(int i=0;i<parts.length;i++) {
                            checkboxSelectedValue.add(parts[i]);
                        }
                    } else {
                        checkboxSelectedValue.add(getSelectedValuesPropertyType);
                    }
                }

                final AlertDialog.Builder dialog_ = new AlertDialog.Builder(AdvanceSearchActivity.this);
                View vi = getLayoutInflater().inflate(R.layout.listview_popup, null);
                adapterPropertiesType = new PropertyTypeAdapter(getBaseContext(), R.layout.propertytype_single_item, propertyTypes,checkboxSelectedValue);
                ListView listViewPopup = (ListView) vi.findViewById(R.id.listViewPropertyType);
                listViewPopup.setAdapter(adapterPropertiesType);
                dialog_.setView(vi);

                final AlertDialog dialog = dialog_.show();
                Button closeButton = (Button) vi.findViewById(R.id.closePopupButton);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapterPropertiesType.clearSelectedFields();
                        dialog.dismiss();
                    }
                });

                Button saveButton = (Button) vi.findViewById(R.id.savePopupButton);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        int oldGroupNumber=0;
                        if ( groupNumber>0) {
                            oldGroupNumber=groupNumber;
                        }
                        groupNumber=0;
                        checkboxSelectedValue=adapterPropertiesType.returnSelectedFields();
                        if (checkboxSelectedValue!=null) {
                            String valueCheckBox="";
                            for (int i=0;i<checkboxSelectedValue.size();i++) {
                                valueCheckBox+=checkboxSelectedValue.get(i)+",";
                                if ( groupNumber==0 ) {
                                    groupNumber= HelpFunctions.returnGroupNumberOfProperty(checkboxSelectedValue.get(i),checkboxSelectedValue);
                                }
                            }

                            if (valueCheckBox.length()>0) {
                                valueCheckBox = valueCheckBox.substring(0, valueCheckBox.length()-1);
                            } else {
                                valueCheckBox="Всички";
                            }
                            choosePropertyType.setText(valueCheckBox);
                        }

                        if ( (groupNumber!=groupNumber) && (searchValuesEdit!=null && (searchValuesEdit.size()>0)  ) ) {

                            if (searchValuesEdit.size()>10) {
                                for (int j = searchValuesEdit.size()-1; j >= 11; j--) {
                                    searchValuesEdit.remove(j);
                                }
                            }
                        }

                        if ( groupNumber>0 ) {
                            moreSearchOptions.removeAllViews();
                            View optionView = HelpFunctions.loadCorrectView(AdvanceSearchActivity.this,groupNumber);
                            moreSearchOptions.addView(optionView);

                            // get extras
                            extriProperty = (TextView) optionView.findViewById(R.id.extriProperty);
                            int extriGroup= HelpFunctions.returnExtriGroup(typeAdvert,groupNumber);
                            String urlBuildExtri="http://api.imot.bg/mobile_api/dictionary/extri?type_extri="+String.valueOf(extriGroup);
                            asyncStarted++;
                            asyncTaskGetExtri= new HTTPGETExtri(progressBar,asyncStarted);
                            asyncTaskGetExtri.delegate=AdvanceSearchActivity.this;
                            asyncTaskGetExtri.execute(urlBuildExtri);

                            Log.e("HEREHERE","GROUP NUMBER");
                            Log.e("HEREHERE",String.valueOf(groupNumber));

                            if ( groupNumber==1 ) {
                                loadGroup1(optionView);
                            } else if (groupNumber==2) {
                                loadGroup2(optionView);
                            } else if (groupNumber==3) {
                                loadGroup3(optionView);
                            } else if (groupNumber==4) {
                                loadGroup4(optionView);
                            } else if (groupNumber==5) {
                                loadGroup5(optionView);
                            } else if (groupNumber==6) {
                                loadGroup6(optionView);
                            } else {}
                        } else {
                            moreSearchOptions.removeAllViews();
                        }
                    }
                });
            }
        });

        // Price of property
        priceFrom = (EditText) findViewById(R.id.priceFrom);
        priceFrom.setGravity(Gravity.CENTER);
        if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>6) && (searchValuesEdit.get(6)!=null) ) {
            String rubValue = searchValuesEdit.get(6).get("price_min");
            if (rubValue != null) {
                priceFrom.setText(rubValue);
            }
        }

        priceTo = (EditText) findViewById(R.id.priceTo);
        priceTo.setGravity(Gravity.CENTER);
        if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>7) && (searchValuesEdit.get(7)!=null) ) {
            String rubValue = searchValuesEdit.get(7).get("price_max");
            if (rubValue != null) {
                priceTo.setText(rubValue);
            }
        }

        // Area of property
        areaFrom = (EditText) findViewById(R.id.areaFrom);
        areaFrom.setGravity(Gravity.CENTER);
        if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>8) && (searchValuesEdit.get(8)!=null) ) {
            String rubValue = searchValuesEdit.get(8).get("kv_min");
            if (rubValue != null) {
                areaFrom.setText(rubValue);
            }
        }

        areaTo = (EditText) findViewById(R.id.areaTo);
        areaTo.setGravity(Gravity.CENTER);
        if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>9) && (searchValuesEdit.get(9)!=null) ) {
            String rubValue = searchValuesEdit.get(9).get("kv_max");
            if (rubValue != null) {
                areaTo.setText(rubValue);
            }
        }

        // sort property results
        sortAdapter = ArrayAdapter.createFromResource(getBaseContext(), R.array.sortValues, android.R.layout.simple_spinner_item);
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortResult.setAdapter(sortAdapter);


        if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>10) && (searchValuesEdit.get(10)!=null) ) {
            String rubValue = searchValuesEdit.get(10).get("sort");
            if (rubValue!=null) {
                ArrayList<String> arrayValue = new ArrayList<>();
                Collections.addAll(arrayValue, getResources().getStringArray(R.array.sortValues));
                sortResult.setSelection(Integer.parseInt(rubValue)-1);
            }
        } else {
            sortResult.setSelection(0);
        }


        searchAdvancedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.setVisibility(View.GONE);
                searchValues= new ArrayList<HashMap<String, String>>();
                searchValues.add(HelpFunctions.generateHashForSearch("rub",typeAdvert));
                searchValues.add(HelpFunctions.generateHashForSearch("page",1));
                ArrayList<String> arrayValueTown = new ArrayList<String>();
                Collections.addAll(arrayValueTown, getResources().getStringArray(R.array.town));
                searchValues.add(HelpFunctions.generateHashForSearch("town",townSpinner,null,arrayValueTown));
                searchValues.add(HelpFunctions.generateHashForSearch("raioni",secondTownSpinner,null,secondTownArray));
                searchValues.add(HelpFunctions.generateHashForSearchTypeHome("type_home",choosePropertyType,propertyTypes,typeAdvert));
                searchValues.add(HelpFunctions.generateHashForSearch("extri",extriProperty));
                searchValues.add(HelpFunctions.generateHashForSearch("price_min",priceFrom));
                searchValues.add(HelpFunctions.generateHashForSearch("price_max",priceTo));
                searchValues.add(HelpFunctions.generateHashForSearch("kv_min",areaFrom));
                searchValues.add(HelpFunctions.generateHashForSearch("kv_max",areaTo));
                ArrayList<String> arrayValue = new ArrayList<String>();
                Collections.addAll(arrayValue, getResources().getStringArray(R.array.sortValues));
                searchValues.add(HelpFunctions.generateHashForSearch("sort",sortResult,1));

                if ( groupNumber==1 ) { //11
                    searchValues.add(HelpFunctions.generateHashForSearch("type_build",buildTypeProperty));
                    searchValues.add(HelpFunctions.generateHashForSearch("year_from",yearFrom));
                    searchValues.add(HelpFunctions.generateHashForSearch("year_to",yearTo));
                    searchValues.add(HelpFunctions.generateHashForSearch("tec",tecSpinner,tecArray,null));
                    searchValues.add(HelpFunctions.generateHashForSearch("phone",phoneSpinner,phoneArray,null));
                    searchValues.add(HelpFunctions.generateHashForSearch("floor_from",floorFromSpinner));
                    searchValues.add(HelpFunctions.generateHashForSearch("floor_to",floorToSpinner));
                } else if ( groupNumber==2 ) { //11
                    searchValues.add(HelpFunctions.generateHashForSearch("type_build",buildTypeProperty));
                    searchValues.add(HelpFunctions.generateHashForSearch("year_from",yearFrom));
                    searchValues.add(HelpFunctions.generateHashForSearch("year_to",yearTo));
                    searchValues.add(HelpFunctions.generateHashForSearch("tec",tecSpinner,tecArray,null));
                    searchValues.add(HelpFunctions.generateHashForSearch("phone",phoneSpinner,phoneArray,null));
                } else if ( groupNumber==3 ) { //11
                } else if ( groupNumber==4 ) { //11
                    searchValues.add(HelpFunctions.generateHashForSearch("electricity",electricitySpinner,electricityArray,null));
                    searchValues.add(HelpFunctions.generateHashForSearch("watter",waterSpinner,waterArray,null));
                    searchValues.add(HelpFunctions.generateHashForSearch("regulation",regulationSpinner,regulationArray,null));
                } else if ( groupNumber==5 ) { //11
                    searchValues.add(HelpFunctions.generateHashForSearch("type_build",buildTypeProperty));
                    searchValues.add(HelpFunctions.generateHashForSearch("year_from",yearFrom));
                    searchValues.add(HelpFunctions.generateHashForSearch("year_to",yearTo));
                    searchValues.add(HelpFunctions.generateHashForSearch("tec",tecSpinner,tecArray,null));
                    searchValues.add(HelpFunctions.generateHashForSearch("phone",phoneSpinner,phoneArray,null));
                } else if ( groupNumber==6 ) { //11
                    searchValues.add(HelpFunctions.generateHashForSearch("land_category",landCategoryProperty));
                    searchValues.add(HelpFunctions.generateHashForSearch("land_permanent_usage",landPermanentUsageProperty));
                    searchValues.add(HelpFunctions.generateHashForSearch("land_lease_contract",landLeaseContractSpinner));
                }

                String searchUrl = HelpFunctions.convertToUrl(searchValues);
                Log.e("HEREHERE",searchUrl);
                asyncStarted++;
//                progressBar.setVisibility(View.VISIBLE);
                asyncTask = new MakeASearchHttpRequest(progressBar,0);
                asyncTask.delegate=AdvanceSearchActivity.this;
                asyncTask.execute(searchUrl);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Intent myIntent = getIntent();
        if (myIntent.hasExtra("results")) {

        }
    }

    public void loadGroup1(View optionView) {
        // get floor
        floorFromSpinner = (Spinner) optionView.findViewById(R.id.floor_from);
        floorToSpinner = (Spinner) optionView.findViewById(R.id.floor_to);
        String urlBuildFloor="http://api.imot.bg/mobile_api/dictionary/floor";
        asyncStarted++;
        asyncTaskGetFloor = new HTTPGETFloor(progressBar,asyncStarted);
        asyncTaskGetFloor.delegate=AdvanceSearchActivity.this;
        asyncTaskGetFloor.execute(urlBuildFloor);

        // get build type
        buildTypeProperty = (TextView) optionView.findViewById(R.id.buildTypeProperty);
        String urlBuildType="http://api.imot.bg/mobile_api/dictionary/type_build";
        asyncStarted++;
        asyncTaskGetBuildType = new HTTPGETBuildType(progressBar,asyncStarted);
        asyncTaskGetBuildType.delegate=AdvanceSearchActivity.this;
        asyncTaskGetBuildType.execute(urlBuildType);

        // get year of build
        yearFrom = (EditText) optionView.findViewById(R.id.yearFrom);
        if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>12) && (searchValuesEdit.get(12)!=null) ) {
            String rubValue = searchValuesEdit.get(12).get("year_from");
            if (rubValue != null) {
                yearFrom.setText(rubValue);
            }
        }
        yearTo = (EditText) optionView.findViewById(R.id.yearTo);
        if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>13) && (searchValuesEdit.get(13)!=null) ) {
            String rubValue = searchValuesEdit.get(13).get("year_to");
            if (rubValue != null) {
                yearTo.setText(rubValue);
            }
        }

        // get tec
        tecSpinner = (Spinner) optionView.findViewById(R.id.tec);
        String urlBuildTec="http://api.imot.bg/mobile_api/dictionary/tec";
        asyncStarted++;
        asyncTaskGetTec = new HTTPGETTec(progressBar,asyncStarted);
        asyncTaskGetTec.delegate=AdvanceSearchActivity.this;
        asyncTaskGetTec.execute(urlBuildTec);

        // get phone
        phoneSpinner = (Spinner) optionView.findViewById(R.id.phone);
        String urlBuildPhone="http://api.imot.bg/mobile_api/dictionary/phone";
        asyncStarted++;
        asyncTaskGetPhone = new HTTPGETPhone(progressBar,asyncStarted);
        asyncTaskGetPhone.delegate=AdvanceSearchActivity.this;
        asyncTaskGetPhone.execute(urlBuildPhone);
    }

    public void loadGroup2(View optionView) {
        // get build type
        buildTypeProperty = (TextView) optionView.findViewById(R.id.buildTypeProperty);
        String urlBuildType="http://api.imot.bg/mobile_api/dictionary/type_build";
        asyncStarted++;
        asyncTaskGetBuildType = new HTTPGETBuildType(progressBar,asyncStarted);
        asyncTaskGetBuildType.delegate=AdvanceSearchActivity.this;
        asyncTaskGetBuildType.execute(urlBuildType);

        // get year of build
        yearFrom = (EditText) optionView.findViewById(R.id.yearFrom);
        if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>12) && (searchValuesEdit.get(12)!=null) ) {
            String rubValue = searchValuesEdit.get(12).get("year_from");
            if (rubValue != null) {
                yearFrom.setText(rubValue);
            }
        }
        yearTo = (EditText) optionView.findViewById(R.id.yearTo);
        if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>13) && (searchValuesEdit.get(13)!=null) ) {
            String rubValue = searchValuesEdit.get(13).get("year_to");
            if (rubValue != null) {
                yearTo.setText(rubValue);
            }
        }

        // get tec
        tecSpinner = (Spinner) optionView.findViewById(R.id.tec);
        String urlBuildTec="http://api.imot.bg/mobile_api/dictionary/tec";
        asyncStarted++;
        asyncTaskGetTec = new HTTPGETTec(progressBar,asyncStarted);
        asyncTaskGetTec.delegate=AdvanceSearchActivity.this;
        asyncTaskGetTec.execute(urlBuildTec);

        // get phone
        phoneSpinner = (Spinner) optionView.findViewById(R.id.phone);
        String urlBuildPhone="http://api.imot.bg/mobile_api/dictionary/phone";
        asyncStarted++;
        asyncTaskGetPhone = new HTTPGETPhone(progressBar,asyncStarted);
        asyncTaskGetPhone.delegate=AdvanceSearchActivity.this;
        asyncTaskGetPhone.execute(urlBuildPhone);
    }

    public void loadGroup3 (View optionView) {}

    public void loadGroup4 (View optionView) {
        // get electricity
        electricitySpinner = (Spinner) optionView.findViewById(R.id.electricity);
        String urlElectricity="http://api.imot.bg/mobile_api/dictionary/electricity";
        asyncStarted++;
        asyncTaskGetElectricity = new HTTPGETElectricity(progressBar,asyncStarted);
        asyncTaskGetElectricity.delegate=AdvanceSearchActivity.this;
        asyncTaskGetElectricity.execute(urlElectricity);

        // get water
        waterSpinner = (Spinner) optionView.findViewById(R.id.water);
        String urlWater="http://api.imot.bg/mobile_api/dictionary/watter";
        asyncStarted++;
        asyncTaskGetWater = new HTTPGETWater(progressBar,asyncStarted);
        asyncTaskGetWater.delegate=AdvanceSearchActivity.this;
        asyncTaskGetWater.execute(urlWater);

        // get Regulation
        regulationSpinner = (Spinner) optionView.findViewById(R.id.regulation);
        String urlRegulation="http://api.imot.bg/mobile_api/dictionary/regulation";
        asyncStarted++;
        asyncTaskGetRegulation = new HTTPGETRegulation(progressBar,asyncStarted);
        asyncTaskGetRegulation.delegate=AdvanceSearchActivity.this;
        asyncTaskGetRegulation.execute(urlRegulation);
    }

    public void loadGroup5 (View optionView) {
        // get build type
        buildTypeProperty = (TextView) optionView.findViewById(R.id.buildTypeProperty);
        String urlBuildType="http://api.imot.bg/mobile_api/dictionary/type_build";
        asyncStarted++;
        asyncTaskGetBuildType = new HTTPGETBuildType(progressBar,asyncStarted);
        asyncTaskGetBuildType.delegate=AdvanceSearchActivity.this;
        asyncTaskGetBuildType.execute(urlBuildType);

        // get year of build
        yearFrom = (EditText) optionView.findViewById(R.id.yearFrom);
        if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>12) && (searchValuesEdit.get(12)!=null) ) {
            String rubValue = searchValuesEdit.get(12).get("year_from");
            if (rubValue != null) {
                yearFrom.setText(rubValue);
            }
        }
        yearTo = (EditText) optionView.findViewById(R.id.yearTo);
        if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>13) && (searchValuesEdit.get(13)!=null) ) {
            String rubValue = searchValuesEdit.get(13).get("year_to");
            if (rubValue != null) {
                yearTo.setText(rubValue);
            }
        }

        // get tec
        tecSpinner = (Spinner) optionView.findViewById(R.id.tec);
        String urlBuildTec="http://api.imot.bg/mobile_api/dictionary/tec";
        asyncStarted++;
        asyncTaskGetTec = new HTTPGETTec(progressBar,asyncStarted);
        asyncTaskGetTec.delegate=AdvanceSearchActivity.this;
        asyncTaskGetTec.execute(urlBuildTec);

        // get phone
        phoneSpinner = (Spinner) optionView.findViewById(R.id.phone);
        String urlBuildPhone="http://api.imot.bg/mobile_api/dictionary/phone";
        asyncStarted++;
        asyncTaskGetPhone = new HTTPGETPhone(progressBar,asyncStarted);
        asyncTaskGetPhone.delegate=AdvanceSearchActivity.this;
        asyncTaskGetPhone.execute(urlBuildPhone);
    }

    public void loadGroup6 (View optionView) {
        // get Land Permanent Usage
        landPermanentUsageProperty = (TextView) optionView.findViewById(R.id.land_permanent_usage);
        String urlLandPermanentUsage="http://api.imot.bg/mobile_api/dictionary/land_permanent_usage";
        asyncStarted++;
        asyncTaskGetlandPermanentUsage = new HTTPGETLandPermanentUsage(progressBar,asyncStarted);
        asyncTaskGetlandPermanentUsage.delegate=AdvanceSearchActivity.this;
        asyncTaskGetlandPermanentUsage.execute(urlLandPermanentUsage);

        // get Land Category
        landCategoryProperty = (TextView) optionView.findViewById(R.id.land_category);
        String urlLandCategory="http://api.imot.bg/mobile_api/dictionary/land_category";
        asyncStarted++;
        asyncTaskGetLandCategory= new HTTPGETLandCategory(progressBar,asyncStarted);
        asyncTaskGetLandCategory.delegate=AdvanceSearchActivity.this;
        asyncTaskGetLandCategory.execute(urlLandCategory);

        //get Land Lease Contract
        landLeaseContractSpinner = (Spinner) optionView.findViewById(R.id.landLeaseContractSpinner);
        String urlBuildPhone="http://api.imot.bg/mobile_api/dictionary/land_lease_contract";
        asyncStarted++;
        asyncTaskGetLandLeaseContract = new HTTPGETLandLeaseContract(progressBar,asyncStarted);
        asyncTaskGetLandLeaseContract.delegate=AdvanceSearchActivity.this;
        asyncTaskGetLandLeaseContract.execute(urlBuildPhone);
    }

    @Override
    public void processFinish(String output, String numberOfAdverts, String searchText) {
        progressBar.setVisibility(View.GONE);
        Intent searchResultIntent = new Intent(AdvanceSearchActivity.this,SearchResultActivity.class);
        searchResultIntent.putExtra("results",output);
        searchResultIntent.putExtra("results_text",searchText);
        searchResultIntent.putExtra("results_numberOfAdverts",numberOfAdverts);
        searchResultIntent.putExtra("searchValues",searchValues);
        finish();
        startActivity(searchResultIntent);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void processGetExtriFinish(ArrayList<String> extriArrayResult) {
        handleBar();
        extriArray=extriArrayResult;
        if (extriArray!=null) {
            extriProperty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder dialogSimple_ = new AlertDialog.Builder(AdvanceSearchActivity.this);
                    View vi = getLayoutInflater().inflate(R.layout.listview_popup, null);
                    dialogSimple_.setTitle("Избери особености:");
                    dialogSimple_.setCancelable(false);
                    String textExtriFieldValue = (String) extriProperty.getText();
                    if ( (textExtriFieldValue.length()>0) && (textExtriFieldValue.contains(",")) ) {
                        extriSelectedValue= new ArrayList<>();
                        String[] parts = textExtriFieldValue.split(",");
                        if ( parts.length>0 ) {
                            for ( int i=0;i<parts.length;i++) {
                                extriSelectedValue.add(parts[i]);
                            }
                        } else {
                            extriSelectedValue.add(textExtriFieldValue);
                        }
                    } else if ( (textExtriFieldValue.length()>0) && ( !textExtriFieldValue.equals("Всички") ) )  {
                        extriSelectedValue = new ArrayList<>();
                        extriSelectedValue.add(textExtriFieldValue);
                    } else {
                        extriSelectedValue = new ArrayList<>();
                    }

                    if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>5) && (searchValuesEdit.get(5)!=null) ) {
                        String rubValue =  searchValuesEdit.get(5).get("extri");
                        if (rubValue!=null) {
                            extriProperty.setText(rubValue);
                        }
                    }

                    final SimpleMultiChoiceAdapter adapterExtriProperties = new SimpleMultiChoiceAdapter(getBaseContext(), R.layout.propertytype_single_item, extriArray,extriSelectedValue);
                    ListView listViewPopup = (ListView) vi.findViewById(R.id.listViewPropertyType);
                    listViewPopup.setAdapter(adapterExtriProperties);
                    dialogSimple_.setView(vi);
                    final AlertDialog dialogSimple = dialogSimple_.show();

                    Button saveButton = (Button) vi.findViewById(R.id.savePopupButton);
                    saveButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            extriSelectedValue = adapterExtriProperties.returnSelectedFields();
                            if (extriSelectedValue!=null) {
                                String valueCheckBox="";
                                for (int i=0;i<extriSelectedValue.size();i++) {
                                    valueCheckBox+=extriSelectedValue.get(i)+",";
                                }

                                if (valueCheckBox.length()>0) {
                                    valueCheckBox = valueCheckBox.substring(0, valueCheckBox.length()-1);
                                } else {
                                    valueCheckBox="Всички";
                                }
                                extriProperty.setText(valueCheckBox);
                            }
                            dialogSimple.dismiss();
                        }
                    });

                    Button closeButton = (Button) vi.findViewById(R.id.closePopupButton);
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapterExtriProperties.clearSelectedFields();
                            dialogSimple.dismiss();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void processGetFloorFinish(ArrayList<CharSequence> floorArrayResult) {
        handleBar();
        floorArray=floorArrayResult;
        if (floorArray!=null) {
            floorFromAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, floorArray);
            floorFromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            floorFromSpinner.setAdapter(floorFromAdapter);
            if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>15) && (searchValuesEdit.get(15)!=null) ) {
                String rubValue =  searchValuesEdit.get(15).get("floor_from");
                if (rubValue!=null) {
                    for(int i=0;i<floorArray.size();i++) {
                        if ( floorArray.get(i).equals(rubValue) ) {
                            floorFromSpinner.setSelection(i);
                            break;
                        }
                    }
                }
            } else {
                floorFromSpinner.setSelection(0);
                Log.e("HEREHERE","||"+floorFromSpinner.getSelectedItem().toString()+"||");
            }

            floorToAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, floorArray);
            floorToAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            floorToSpinner.setAdapter(floorToAdapter);
            if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>16) && (searchValuesEdit.get(16)!=null) ) {
                String rubValue =  searchValuesEdit.get(16).get("floor_to");
                if (rubValue!=null) {
                    for(int i=0;i<floorArray.size();i++) {
                        if ( floorArray.get(i).equals(rubValue) ) {
                            floorToSpinner.setSelection(i);
                            break;
                        }
                    }
                }
            } else {
                floorToSpinner.setSelection(0);
            }
        }
    }

    @Override
    public void processGetBuildTypeFinish(ArrayList<String> buildTypeArrayResult) {
        handleBar();
        buildTypeArray = buildTypeArrayResult;
        if ( buildTypeArray!=null ) {
            if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>11) && (searchValuesEdit.get(11)!=null) ) {
                String rubValue = searchValuesEdit.get(11).get("type_build");
                if (rubValue != null) {
                    buildTypeProperty.setText(rubValue);
                }
            }
            buildTypeProperty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder dialogSimple_ = new AlertDialog.Builder(AdvanceSearchActivity.this);
                    View vi = getLayoutInflater().inflate(R.layout.listview_popup, null);
                    dialogSimple_.setTitle("Вид строителство:");
                    dialogSimple_.setCancelable(false);

                    String textbuildTypeFieldValue = (String) buildTypeProperty.getText();
                    if ( (textbuildTypeFieldValue.length()>0) && (textbuildTypeFieldValue.contains(",")) ) {
                        buildTypeSelectedValue= new ArrayList<>();
                        String[] parts = textbuildTypeFieldValue.split(",");
                        if ( parts.length>0 ) {
                            for ( int i=0;i<parts.length;i++) {
                                buildTypeSelectedValue.add(parts[i]);
                            }
                        } else {
                            buildTypeSelectedValue.add(textbuildTypeFieldValue);
                        }
                    } else if ( (textbuildTypeFieldValue.length()>0) && ( !textbuildTypeFieldValue.equals("Всички") ) )  {
                        buildTypeSelectedValue= new ArrayList<>();
                        buildTypeSelectedValue.add(textbuildTypeFieldValue);
                    } else {
                        buildTypeSelectedValue= new ArrayList<>();
                    }

                    final SimpleMultiChoiceAdapter adapterbuildTypeProperties = new SimpleMultiChoiceAdapter(getBaseContext(), R.layout.propertytype_single_item, buildTypeArray,buildTypeSelectedValue);
                    ListView listViewPopup = (ListView) vi.findViewById(R.id.listViewPropertyType);
                    listViewPopup.setAdapter(adapterbuildTypeProperties);
                    dialogSimple_.setView(vi);
                    final AlertDialog dialogSimple = dialogSimple_.show();

                    Button saveButton = (Button) vi.findViewById(R.id.savePopupButton);
                    saveButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            buildTypeSelectedValue=adapterbuildTypeProperties.returnSelectedFields();
                            if (buildTypeSelectedValue!=null) {
                                Log.e("HEREHERE","Click Event "+buildTypeSelectedValue.toString());
                                String valueCheckBox="";
                                for (int i=0;i<buildTypeSelectedValue.size();i++) {
                                    valueCheckBox+=buildTypeSelectedValue.get(i)+",";
                                }

                                if (valueCheckBox.length()>0) {
                                    valueCheckBox = valueCheckBox.substring(0, valueCheckBox.length()-1);
                                } else {
                                    valueCheckBox="Всички";
                                }
                                buildTypeProperty.setText(valueCheckBox);
                            }
                            dialogSimple.dismiss();
                        }
                    });

                    Button closeButton = (Button) vi.findViewById(R.id.closePopupButton);
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapterbuildTypeProperties.clearSelectedFields();
                            dialogSimple.dismiss();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void processGetTecFinish(ArrayList<CharSequence> tecArrayResult) {
        handleBar();
        tecArray = tecArrayResult;
        if ( tecArray!=null ) {
            tecAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, tecArray);
            tecAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tecSpinner.setAdapter(tecAdapter);
            if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>13) && (searchValuesEdit.get(13)!=null) ) {
                String rubValue =  searchValuesEdit.get(13).get("tec");
                if (rubValue!=null) {
                    for(int i=0;i<tecArray.size();i++) {
                        if ( tecArray.get(i).equals(rubValue) ) {
                            tecSpinner.setSelection(i);
                            break;
                        }
                    }
                }
            } else {
                tecSpinner.setSelection(0);
            }

        }
    }

    @Override
    public void processGetPhoneFinish(ArrayList<CharSequence> phoneArrayResult) {
        handleBar();
        phoneArray = phoneArrayResult;
        if ( phoneArray!=null ) {
            phoneAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, phoneArray);
            phoneAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            phoneSpinner.setAdapter(phoneAdapter);
            if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>14) && (searchValuesEdit.get(14)!=null) ) {
                String rubValue =  searchValuesEdit.get(14).get("phone");
                if (rubValue!=null) {
                    for(int i=0;i<phoneArray.size();i++) {
                        if ( phoneArray.get(i).equals(rubValue) ) {
                            phoneSpinner.setSelection(i);
                            break;
                        }
                    }
                }
            } else {
                phoneSpinner.setSelection(0);
            }
        }
    }

    @Override
    public void processGetElectricityFinish(ArrayList<CharSequence> electricityArrayResult) {
        handleBar();
        electricityArray = electricityArrayResult;
        if ( electricityArray!=null ) {
            electricityAdapter = new ArrayAdapter<CharSequence>(getBaseContext(), android.R.layout.simple_spinner_item, electricityArray);
            electricityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            electricitySpinner.setAdapter(electricityAdapter);
            if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>11) && (searchValuesEdit.get(11)!=null) ) {
                String rubValue =  searchValuesEdit.get(11).get("electricity");
                if (rubValue!=null) {
                    for(int i=0;i<electricityArray.size();i++) {
                        if ( electricityArray.get(i).equals(rubValue) ) {
                            electricitySpinner.setSelection(i);
                            break;
                        }
                    }
                }
            } else {
                electricitySpinner.setSelection(0);
            }
        }
    }

    @Override
    public void processGetWaterFinish(ArrayList<CharSequence> waterArrayResult) {
        handleBar();
        waterArray = waterArrayResult;
        if ( waterArray!=null ) {
            waterAdapter = new ArrayAdapter<CharSequence>(getBaseContext(), android.R.layout.simple_spinner_item, waterArray);
            waterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            waterSpinner.setAdapter(waterAdapter);
            if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>12) && (searchValuesEdit.get(12)!=null) ) {
                String rubValue =  searchValuesEdit.get(12).get("watter");
                if (rubValue!=null) {
                    for(int i=0;i<waterArray.size();i++) {
                        if ( waterArray.get(i).equals(rubValue) ) {
                            waterSpinner.setSelection(i);
                            break;
                        }
                    }
                }
            } else {
                waterSpinner.setSelection(0);
            }
        }
    }

    @Override
    public void processGetRegulationFinish(ArrayList<CharSequence> regulationArrayResult) {
        handleBar();
        regulationArray = regulationArrayResult;
        if ( regulationArray!=null ) {
            regulationAdapter = new ArrayAdapter<CharSequence>(getBaseContext(), android.R.layout.simple_spinner_item, regulationArray);
            regulationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            regulationSpinner.setAdapter(regulationAdapter);
            if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>13) && (searchValuesEdit.get(13)!=null) ) {
                String rubValue =  searchValuesEdit.get(13).get("regulation");
                if (rubValue!=null) {
                    for(int i=0;i<regulationArray.size();i++) {
                        if ( regulationArray.get(i).equals(rubValue) ) {
                            regulationSpinner.setSelection(i);
                            break;
                        }
                    }
                }
            } else {
                regulationSpinner.setSelection(0);
            }
        }
    }

    @Override
    public void processGetLandPermanentUsagePropertyFinish(ArrayList<String> landPermanentUsagePropertyTypeArrayResult) {
        handleBar();
        landPermanentUsageArray = landPermanentUsagePropertyTypeArrayResult;
        if ( landPermanentUsageArray!=null ) {
            if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>12) && (searchValuesEdit.get(12)!=null) ) {
                String rubValue = searchValuesEdit.get(12).get("land_permanent_usage");
                if (rubValue != null) {
                    landPermanentUsageProperty.setText(rubValue);
                }
            }

            landPermanentUsageProperty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder dialogSimple_ = new AlertDialog.Builder(AdvanceSearchActivity.this);
                    View vi = getLayoutInflater().inflate(R.layout.listview_popup, null);
                    dialogSimple_.setTitle("Начин на трайно ползване:");
                    dialogSimple_.setCancelable(false);

                    String textlandPermanentUsageFieldValue = (String) landPermanentUsageProperty.getText();
                    if ( (textlandPermanentUsageFieldValue.length()>0) && (textlandPermanentUsageFieldValue.contains(",")) ) {
                        landPermanentUsageSelectedValue = new ArrayList<String>();
                        String[] parts = textlandPermanentUsageFieldValue.split(",");
                        if ( parts.length>0 ) {
                            for ( int i=0;i<parts.length;i++) {
                                landPermanentUsageSelectedValue.add(parts[i]);
                            }
                        } else {
                            landPermanentUsageSelectedValue.add(textlandPermanentUsageFieldValue);
                        }
                    } else if ( (textlandPermanentUsageFieldValue.length()>0) && ( !textlandPermanentUsageFieldValue.equals("Всички") ) )  {
                        landPermanentUsageSelectedValue= new ArrayList<String>();
                        landPermanentUsageSelectedValue.add(textlandPermanentUsageFieldValue);
                    } else {
                        landPermanentUsageSelectedValue= new ArrayList<String>();
                    }

                    final SimpleMultiChoiceAdapter adapterlandPermanentUsageProperties = new SimpleMultiChoiceAdapter(getBaseContext(), R.layout.propertytype_single_item, landPermanentUsageArray,landPermanentUsageSelectedValue);
                    ListView listViewPopup = (ListView) vi.findViewById(R.id.listViewPropertyType);
                    listViewPopup.setAdapter(adapterlandPermanentUsageProperties);
                    dialogSimple_.setView(vi);
//                    dialogSimple_.show();
                    final Dialog dialogSimple = dialogSimple_.show();

                    Button saveButton = (Button) vi.findViewById(R.id.savePopupButton);
                    saveButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            landPermanentUsageSelectedValue=adapterlandPermanentUsageProperties.returnSelectedFields();
                            if (landPermanentUsageSelectedValue!=null) {
                                String valueCheckBox="";
                                for (int i=0;i<landPermanentUsageSelectedValue.size();i++) {
                                    valueCheckBox+=landPermanentUsageSelectedValue.get(i)+",";
                                }

                                if (valueCheckBox.length()>0) {
                                    valueCheckBox = valueCheckBox.substring(0, valueCheckBox.length()-1);
                                } else {
                                    valueCheckBox="Всички";
                                }
                                landPermanentUsageProperty.setText(valueCheckBox);
                            } else {
                                landPermanentUsageProperty.setText("Всички");
                            }
                            dialogSimple.dismiss();
                        }
                    });

                    Button closeButton = (Button) vi.findViewById(R.id.closePopupButton);
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapterlandPermanentUsageProperties.clearSelectedFields();
                            dialogSimple.dismiss();
                        }
                    });
                }
            });
        }

    }

    @Override
    public void processGetLandCategoryFinish(ArrayList<String> landCategoryArrayResult) {
        handleBar();
        landCategoryArray=landCategoryArrayResult;
        if (landCategoryArray!=null)  {
            if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>11) && (searchValuesEdit.get(11)!=null) ) {
                String rubValue = searchValuesEdit.get(11).get("land_category");
                if (rubValue != null) {
                    landCategoryProperty.setText(rubValue);
                }
            }

            landCategoryProperty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder dialogSimple_ = new AlertDialog.Builder(AdvanceSearchActivity.this);
                    View vi = getLayoutInflater().inflate(R.layout.listview_popup, null);
                    dialogSimple_.setTitle("Начин на трайно ползване:");
                    dialogSimple_.setCancelable(false);

                    String textlandCategoryFieldValue = (String) landCategoryProperty.getText();
                    if ( (textlandCategoryFieldValue.length()>0) && (textlandCategoryFieldValue.contains(",")) ) {
                        landCategorySelectedValue = new ArrayList<String>();
                        String[] parts = textlandCategoryFieldValue.split(",");
                        if ( parts.length>0 ) {
                            for ( int i=0;i<parts.length;i++) {
                                landCategorySelectedValue.add(parts[i]);
                            }
                        } else {
                            landCategorySelectedValue.add(textlandCategoryFieldValue);
                        }
                    } else if ( (textlandCategoryFieldValue.length()>0) && ( !textlandCategoryFieldValue.equals("Всички") ) )  {
                        landCategorySelectedValue= new ArrayList<String>();
                        landCategorySelectedValue.add(textlandCategoryFieldValue);
                    } else {
                        landCategorySelectedValue= new ArrayList<String>();
                    }

                    final SimpleMultiChoiceAdapter adapterlandCategoryProperties = new SimpleMultiChoiceAdapter(getBaseContext(), R.layout.propertytype_single_item, landCategoryArray,landCategorySelectedValue);
                    ListView listViewPopup = (ListView) vi.findViewById(R.id.listViewPropertyType);
                    listViewPopup.setAdapter(adapterlandCategoryProperties);
                    dialogSimple_.setView(vi);
                    final Dialog dialogSimple = dialogSimple_.show();

                    Button saveButton = (Button) vi.findViewById(R.id.savePopupButton);
                    saveButton.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            landCategorySelectedValue=adapterlandCategoryProperties.returnSelectedFields();
                            if (landCategorySelectedValue!=null) {
                                String valueCheckBox="";
                                for (int i=0;i<landCategorySelectedValue.size();i++) {
                                    valueCheckBox+=landCategorySelectedValue.get(i)+",";
                                }

                                if (valueCheckBox.length()>0) {
                                    valueCheckBox = valueCheckBox.substring(0, valueCheckBox.length()-1);
                                } else {
                                    valueCheckBox="Всички";
                                }
                                landCategoryProperty.setText(valueCheckBox);
                            } else {
                                landCategoryProperty.setText("Всички");
                            }
                            dialogSimple.dismiss();
                        }
                    });

                    Button closeButton = (Button) vi.findViewById(R.id.closePopupButton);
                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapterlandCategoryProperties.clearSelectedFields();
                            dialogSimple.dismiss();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void processGetLandLeaseContractFinish(ArrayList<CharSequence> landLeaseContractArrayResult) {
        handleBar();
        landLeaseContractArray = landLeaseContractArrayResult;
        if ( landLeaseContractArrayResult!=null ) {
            landLeaseContractAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, landLeaseContractArray);
            landLeaseContractAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            landLeaseContractSpinner.setAdapter(landLeaseContractAdapter);
            if ( (searchValuesEdit!=null) && (searchValuesEdit.size()>13) && (searchValuesEdit.get(13)!=null) ) {
                String rubValue =  searchValuesEdit.get(13).get("tec");
                if (rubValue!=null) {
                    landLeaseContractSpinner.setSelection(Integer.parseInt(rubValue));
                }
            } else {
                landLeaseContractSpinner.setSelection(0);
            }

        }
    }

    public void handleBar() {
        asyncStarted--;
        if (asyncStarted==0) {
            progressBar.setVisibility(View.GONE);
        }
    }
}

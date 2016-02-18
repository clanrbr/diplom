package utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import localestates.localestates.R;

/**
 * Created by Ado on 11/30/2015.
 */
public class HelpFunctions {

    public static boolean checkGroupOfPropertyType(String type, ArrayList<String> selectedFields) {
        int match=0;
        if ( selectedFields.size()>0 ) {
            switch (selectedFields.get(0)) {
                case "ВСИЧКИ":
                    match=-1;
                    break;
                case "СТАЯ":
                    match=1;
                    break;
                case "1-СТАЕН":
                    match=1;
                    break;
                case "2-СТАЕН":
                    match=1;
                    break;
                case "3-СТАЕН":
                    match=1;
                    break;
                case "4-СТАЕН":
                    match=1;
                    break;
                case "МНОГОСТАЕН":
                    match=1;
                    break;
                case "МЕЗОНЕТ":
                    match=1;
                    break;
                case "АТЕЛИЕ ТАВАН":
                    match=1;
                    break;

                case "ОФИС":
                    match=2;
                    break;
                case "МАГАЗИН":
                    match=2;
                    break;
                case "ЗАВЕДЕНИЕ":
                    match=2;
                    break;
                case "СКЛАД":
                    match=2;
                    break;
                case "ПРОМ. ПОМЕЩЕНИЕ":
                    match=2;
                    break;
                case "ХОТЕЛ":
                    match=2;
                    break;

                case "ЕТАЖ ОТ КЪЩА":
                    match=3;
                    break;
                case "КЪЩА":
                    match=3;
                    break;
                case "ВИЛА":
                    match=3;
                    break;

                case "ПАРЦЕЛ":
                    match=4;
                    break;

                case "МЯСТО":
                    match=4;
                    break;

                case "ГАРАЖ":
                    match=5;
                    break;


                case "ЗЕМЕДЕЛСКА ЗЕМЯ":
                    match=6;
                    break;
            }

            if ( type.equals("ВСИЧКИ") ) {
                return true;
            }
            if ( match==-1 ) {
                return true;
            }
            if ( (match==1) && (  type.equals("СТАЯ") || type.equals("1-СТАЕН") || type.equals("2-СТАЕН") || type.equals("3-СТАЕН") || type.equals("4-СТАЕН") || type.equals("МНОГОСТАЕН") || type.equals("МЕЗОНЕТ") || type.equals("АТЕЛИЕ ТАВАН") ) ) {
                return true;
            } else if ( (match==2 ) && ( type.equals("ХОТЕЛ") || type.equals("ПРОМ. ПОМЕЩЕНИЕ") || type.equals("СКЛАД") || type.equals("ЗАВЕДЕНИЕ") || type.equals("МАГАЗИН") || type.equals("ОФИС") ) ) {
                return true;
            } else if ( (match==3) && ( type.equals("ВИЛА") || type.equals("КЪЩА") || type.equals("ЕТАЖ ОТ КЪЩА") ) ) {
                return true;
            } else if ( (match==4) && ( type.equals("ПАРЦЕЛ") || type.equals("МЯСТО") ) ) {
                return true;
            } else if ( (match==5) && type.equals("ГАРАЖ")  ) {
                return true;
            } else if ( (match==6) && type.equals("ЗЕМЕДЕЛСКА ЗЕМЯ") ) {
                return true;
            }

            return false;
        }

        return true;
    }

    public static int returnGroupNumberOfProperty(String typeProperty, ArrayList<String> selectedFields) {
        int match=0;
        String type="";
        if (typeProperty!=null) {
            type=typeProperty;
        } else {
            if (selectedFields.size()>0) {
                type=selectedFields.get(0);
            }
        }

        if ( type.equals("СТАЯ") ||  type.equals("1-СТАЕН") || type.equals("2-СТАЕН") || type.equals("3-СТАЕН") || type.equals("4-СТАЕН") || type.equals("МНОГОСТАЕН") || type.equals("МЕЗОНЕТ") || type.equals("АТЕЛИЕ ТАВАН") )  {
            match=1;
        } else if (  type.equals("ХОТЕЛ") || type.equals("ПРОМ. ПОМЕЩЕНИЕ") || type.equals("СКЛАД") || type.equals("ЗАВЕДЕНИЕ") || type.equals("МАГАЗИН") || type.equals("ОФИС") )  {
            match=2;
        } else if ( type.equals("ВИЛА") || type.equals("КЪЩА") || type.equals("ЕТАЖ ОТ КЪЩА") ) {
            match=3;
        } else if ( ( type.equals("ПАРЦЕЛ") || type.equals("МЯСТО") ) ) {
            match=4;
        } else if ( type.equals("ГАРАЖ") ) {
            match=5;
        } else if ( type.equals("ЗЕМЕДЕЛСКА ЗЕМЯ") ) {
            match=6;
        }

        return match;
    }

    public static int returnExtriGroup(int typeAdvert,int groupNumber) {
        if ( typeAdvert==1 ) {
            if ( (groupNumber==1) || (groupNumber==2) || (groupNumber==3) || (groupNumber==5) ) {
                return 1;
            } else if ( groupNumber==4 ) {
                return 3;
            } else {
                return 5;
            }
        } else {
            if ( (groupNumber==1) || (groupNumber==2) || (groupNumber==3) || (groupNumber==5) ) {
                return 2;
            } else if ( groupNumber==4 ) {
                return 4;
            } else {
                return 6;
            }
        }
    }

    public static HashMap<String, String> generateHashForSearch(String searchName, int value) {
        HashMap<String, String> result;
        if ( value>0 ) {
            result = new HashMap<String,String>();
            result.put(searchName,String.valueOf(value));
        } else {
            result=null;
        }

        return result;
    }

    public static HashMap<String, String> generateHashForSearch(String searchName, TextView field) {
        HashMap<String, String> result=null;
        if (field!=null) {
            if ( field.getText().toString()!="" ) {
                result = new HashMap<String,String>();
                result.put(searchName,field.getText().toString());
            }
        }

        return result;
    }

    public static HashMap<String, String> generateHashForSearch(String searchName, TextView field,ArrayList<String> values) {
        HashMap<String, String> result=null;
        if (field!=null) {
            if ( field.getText().toString()!="" ) {
                String valuesNumbers=field.getText().toString();
                for(int i=0;i<values.size();i++) {
                    if ( valuesNumbers.contains(values.get(i).toString()) ) {
                        valuesNumbers=valuesNumbers.replace(values.get(i).toString(),String.valueOf(i));
                    }
                }
                result = new HashMap<String,String>();
                result.put(searchName,valuesNumbers);
            }
        }

        return result;
    }

    public static HashMap<String, String> generateHashForSearch(String searchName, EditText field) {
        HashMap<String, String> result=null;
        if (field!=null) {
            if (field.getText().toString() != "") {
                result = new HashMap<String, String>();
                result.put(searchName, field.getText().toString());
            }
        }

        return result;
    }

    public static HashMap<String, String> generateHashForSearch(String searchName, Spinner field) {
        HashMap<String, String> result=null;
        if (field!=null) {
            int position = field.getSelectedItemPosition();
            result = new HashMap<String,String>();
            if ( searchName.equals("floor_from") || searchName.equals("floor_to") && (field.getItemAtPosition(position)!=null) ) {;
                result.put(searchName,String.valueOf(field.getItemAtPosition(position).toString()));
            } else {
                result.put(searchName,String.valueOf(position));
            }
        }
        return result;
    }

    public static HashMap<String, String> generateHashForSearch(String searchName, Spinner field,int param) {
        HashMap<String, String> result=null;
        if (field!=null) {
            int position = field.getSelectedItemPosition();
            position++;
            result = new HashMap<String,String>();
            result.put(searchName,String.valueOf(position));
        }
        return result;
    }

    public static HashMap<String, String> generateHashForSearch(String searchName, Spinner field, ArrayList<CharSequence> values, ArrayList<String> valuesString) {
        HashMap<String, String> result=null;
        if (field!=null) {
            int position = field.getSelectedItemPosition();
            if ( (values!=null) && (position<values.size()) && (values.get(position).toString()!="") && (!values.get(position).toString().contains("Всички")) ) {
                result = new HashMap<String,String>();
                result.put(searchName,values.get(position).toString());
            } else if ( (valuesString!=null)  && (position<valuesString.size()) && (valuesString.get(position).toString()!="") && (!valuesString.get(position).toString().contains("Всички")) ) {
                result = new HashMap<String,String>();
                result.put(searchName,valuesString.get(position).toString());
            }
        }

        return result;
    }

    public static HashMap<String, String> generateHashForSearchTypeHome(String searchName, TextView field,ArrayList<String> values,int typeAdvert) {

        HashMap<String, String> result=null;
        if (field!=null) {
            if ( field.getText().toString()!="" ) {
                String valuesNumbers=field.getText().toString();
                Iterator it ;
                if ( typeAdvert==1 ) {
                    it = ConstantsImportant.orderTypeHome.entrySet().iterator();
                } else {
                    it = ConstantsImportant.orderTypeHomeRent.entrySet().iterator();
                }
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();
                    if ( valuesNumbers.contains(pair.getKey().toString()) ) {
                        valuesNumbers=valuesNumbers.replace(pair.getKey().toString(),pair.getValue().toString());
                    }
                }
                result = new HashMap<String,String>();
                result.put(searchName,valuesNumbers);
            }
        }

        return result;
    }

    public static String convertToUrl(ArrayList<HashMap<String,String>> values) {
        String buildUrl="";
        for (int y=0;y<values.size();y++) {
            HashMap<String,String> hashMapValues=values.get(y);
            if (hashMapValues!=null) {
                for (Map.Entry me : hashMapValues.entrySet()) {
                    if ( (me.getValue()!=null) && (me.getValue()!="") ) {
                        String encodedValue=me.getValue().toString();
                        if (me.getValue().toString().contains(",")) {
                            String[] parts = encodedValue.split(",");
                            for ( int i=0;i<parts.length;i++) {
                                try {
                                    buildUrl+=me.getKey().toString()+"="+ URLEncoder.encode(parts[i], "UTF-8")+"&";
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            try {
                                encodedValue=URLEncoder.encode(encodedValue, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            buildUrl+=me.getKey().toString()+"="+encodedValue+"&";
                        }
                    }
                }
            }
        }
        if ( buildUrl!="" ) {
            buildUrl="?"+buildUrl;
            buildUrl = ConstantsImportant.imotApiUrlSearch+buildUrl.substring(0, buildUrl.length()-1);
        }

        return buildUrl;
    }


    public static String convertSearchToString(ArrayList<HashMap<String,String>> values) {
        String buildUrl="";
        for (int y=0;y<values.size();y++) {
            HashMap<String,String> hashMapValues=values.get(y);
            if (hashMapValues!=null) {
                for (Map.Entry me : hashMapValues.entrySet()) {
                    buildUrl+=me.getKey().toString()+"="+me.getValue().toString()+"&";
                }
            }
        }
        if ( !buildUrl.equals("") ) {
            buildUrl = buildUrl.substring(0, buildUrl.length()-1);
        }

        return buildUrl;
    }

    public static ArrayList<HashMap<String,String>> convertStringToHashSaearch(String values) {
        ArrayList<HashMap<String,String>> result = new ArrayList<>();
        if (values.length()>0) {
            String[] hashValues = values.split("&");
            if (hashValues.length>0) {
                for (int i=0;i<hashValues.length;i++) {
                    String key = "";
                    String value= "";
                    String[] hashParts = hashValues[i].split("=");
                    if (hashParts.length==1) {
                        key=hashParts[0];
                        value="";
                    } else if (hashParts.length==2) {
                        key=hashParts[0];
                        value=hashParts[1];
                    }

                    HashMap<String,String> hashGroup =  new HashMap<>();
                    hashGroup.put(key,value);
                    result.add(hashGroup);
                }
            }
        }

        return result;
    }

    public static View loadCorrectView(Activity context,int groupNumber) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View optionView;
        if ( groupNumber==1  ) {
            optionView = context.getLayoutInflater().inflate(R.layout.search_options_group_1, null);
        } else if (groupNumber==2) {
            optionView = context.getLayoutInflater().inflate(R.layout.search_options_group_2, null);
        } else if (groupNumber==3) {
            optionView = context.getLayoutInflater().inflate(R.layout.search_options_group_3, null);
        } else if (groupNumber==4) {
            optionView = context.getLayoutInflater().inflate(R.layout.search_options_group_4, null);
        } else if (groupNumber==5) {
            optionView = context.getLayoutInflater().inflate(R.layout.search_options_group_5, null);
        } else if (groupNumber==6) {
            optionView = context.getLayoutInflater().inflate(R.layout.search_options_group_6, null);
        } else {
            optionView = context.getLayoutInflater().inflate(R.layout.search_options_group_1, null);
        }
        return optionView;
    }

//    public static HashMap<String, String> generateHashForSearch(String searchName, TextView field, ArrayList values) {
//        HashMap<String, String> result;
//        if ( field.getText().toString()!="Всички" ) {
//            result = new HashMap<String,String>();
//            if ( field.getText().toString().contains(",") ) {
//                result.put(searchName,field.getText().toString());
//            } else {
//                result.put(searchName,field.getText().toString());
//            }
//        } else {
//            result=null;
//        }
//
//        return result;
//    }


//    public static String getString(InputStream stream) throws IOException {
//        if (stream == null) {
//            return "";
//        }
//        BufferedReader reader =new BufferedReader(new InputStreamReader(stream));
//        StringBuilder content = new StringBuilder();
//        String newLine;
//        do {
//            newLine = reader.readLine();
//            if (newLine != null) {
//                content.append(newLine).append('\n');
//            }
//        } while (newLine != null);
//
//        reader.close();
//        if (content.length() > 0) {
//            // strip last newline
//            content.setLength(content.length() - 1);
//        }
//
//        Log.e("HEREHERE", content.toString());
//
//        return content.toString();
//    }
}

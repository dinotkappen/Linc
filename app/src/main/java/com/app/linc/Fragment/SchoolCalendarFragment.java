package com.app.linc.Fragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.linc.Adapter.SchoolCalendarAdapter;
import com.app.linc.Model.Calendar.CalendarModel;
import com.app.linc.Model.StudentHomeModel.SchoolCalander;
import com.app.linc.R;
import com.marcohc.robotocalendar.RobotoCalendarView;
import com.orhanobut.hawk.Hawk;
import com.savvi.rangedatepicker.CalendarPickerView;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.app.linc.Activities.MainActivity.titlHide;

/**
 * A simple {@link Fragment} subclass.
 */
public class SchoolCalendarFragment extends Fragment implements RobotoCalendarView.RobotoCalendarListener {

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private RecyclerView recyclerViewSchoolCalendar;
    SchoolCalendarAdapter adapterSchoolCalendar;
    public List<SchoolCalander> schoolCalendarList = new ArrayList<>();

    private int mYear, mMonth, mDay, mHour, mMinute;
    final Calendar myCalendar = Calendar.getInstance();

    public static CalendarPickerView calendarPickerView;
    Button button;
    public static ArrayList<Date> dateArrayList = new ArrayList<>();
    public static Calendar lastYear;
    public static Calendar nextYear;
    public static boolean fragmentset = false;
    TextView txtDilgHeading, txtDilgFrom, txtDilgTo, txtDilgContent;
    public static RobotoCalendarView robotoCalendarView;

    public SchoolCalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_school_calendar, container, false);
        Hawk.put("currentFragment",false);


        robotoCalendarView = rootView.findViewById(R.id.robotoCalendarPicker);
        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dilg_calendar);
        txtDilgHeading = dialog.findViewById(R.id.txtDilgHeading);
        txtDilgFrom = dialog.findViewById(R.id.txtDilgFrom);
        txtDilgTo = dialog.findViewById(R.id.txtDilgTo);
        txtDilgContent = dialog.findViewById(R.id.txtDilgContent);

        recyclerViewSchoolCalendar = rootView.findViewById(R.id.recyclerViewSchoolCalendar);
        recyclerViewSchoolCalendar.setLayoutManager(new LinearLayoutManager(getActivity()));

        titlHide(getResources().getString(R.string.school_calendar));
//              datePicker=rootView.findViewById(R.id.datePicker);
//        datePicker.updateDate(2019, 12, 25);
//        datePicker.updateDate(2019, 12, 26);

        nextYear = Calendar.getInstance();
        nextYear.add(Calendar.YEAR, 10);

        lastYear = Calendar.getInstance();
        lastYear.add(Calendar.YEAR, -10);

        // calendarPickerView = rootView.findViewById(R.id.calendar_view);
        schoolCalendarList = Hawk.get("schoolCalendarList", schoolCalendarList);

        if (schoolCalendarList.size() > 0) {
            adapterSchoolCalendar = new SchoolCalendarAdapter(getActivity(), schoolCalendarList);
            recyclerViewSchoolCalendar.setAdapter(adapterSchoolCalendar);
        }
        fragmentset = true;

//        calendarPickerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
//            @Override
//            public void onGlobalLayout(){
//                Toast.makeText(getContext(),"Clicled",Toast.LENGTH_SHORT).show();
//            }
//        });

//        calendarPickerView.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
//
//            @Override
//            public void onDateUnselected(Date date) {
//                dialog.show();
//            }
//
//            @Override
//            public void onDateSelected(Date date) {
//                dialog.show();
//                for(int i=0;i<schoolCalendarList.size();i++) {
//                    String start=schoolCalendarList.get(i).getStartTime();
//                    String end=schoolCalendarList.get(i).getEndTime();
//
//
//                    List<Date> datesList = new ArrayList<Date>();
//                    DateFormat formatter;
//
//                    formatter = new SimpleDateFormat("dd-MM-yyyy");
//                    Date startDate = null;
//                    try {
//                        startDate = (Date) formatter.parse(start);
//                    } catch (ParseException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    Date endDate = null;
//                    try {
//                        endDate = (Date) formatter.parse(end);
//                    } catch (ParseException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
//                    long endTime = endDate.getTime(); // create your endtime here, possibly using Calendar or Date
//                    long curTime = startDate.getTime();
//                    while (curTime <= endTime) {
//                        datesList.add(new Date(curTime));
//                        curTime += interval;
//                    }
//                    for (int j = 0; j < datesList.size(); j++) {
//                        int h=datesList.size();
//                        Log.v("h",""+h);
//                        Date val=datesList.get(h-1);
//                        Log.v("val",""+val);
//
//                        SimpleDateFormat sdf3 = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
//
//                        Date d1 = null;
//                        try{
//                            d1 = sdf3.parse("Wed Dec 25 00:00:00 GMT+05:30 31");
//
//                        }catch (Exception e) {
//                            e.printStackTrace();
//                        }
//
//                        String clickedDate=""+calendarPickerView.getSelectedDate().getTime();
//                        Log.v("clickedDate",""+clickedDate);
//
////                        Date lDate = (Date) datesList.get(i);
////                        String ds = formatter.format(lDate);
////                        dateArrayList.add(lDate);
//
//                    }
//
//                }
//
//            }
//
//        });

//        Date date=calendarPickerView.getSelectedDate();
//        Toast.makeText(getContext(),""+date,Toast.LENGTH_LONG).show();

        //    Dates();

        // Set listener, in this case, the same activity
        robotoCalendarView.setRobotoCalendarListener(this);

        robotoCalendarView.setShortWeekDays(false);

        robotoCalendarView.showDateTitle(true);

        robotoCalendarView.setDate(new Date());


//        for (int i = 10; i < 16; i++) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.DAY_OF_MONTH, i);
//            robotoCalendarView.markCircleImage1(calendar.getTime());
//
//        }
//
//        for (int i = 24; i < 30; i++) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.DAY_OF_MONTH, i);
//            robotoCalendarView.markCircleImage2(calendar.getTime());
//
//        }
//        for (int i = 11; i < 13; i++) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.DAY_OF_MONTH, i);
//            robotoCalendarView.markCircleImage2(calendar.getTime());
//        }
        setDates(schoolCalendarList);
        return rootView;
    }

//    public static void Dates() {
//
//        dateArrayList = Hawk.get("dateArrayList", dateArrayList);
//        calendarPickerView.init(lastYear.getTime(), nextYear.getTime(), new SimpleDateFormat("MMMM, yyyy", Locale.getDefault())) //
//                .inMode(CalendarPickerView.SelectionMode.SINGLE) //
//                //  .withSubTitles(getSubTitles())
//                .withHighlightedDates(dateArrayList);
//
//        calendarPickerView.scrollToDate(new Date());
//
//    }

    @Override
    public void onDayClick(Date date) {

    }

    @Override
    public void onDayLongClick(Date date) {

    }

    @Override
    public void onRightButtonClick() {
        setDates(schoolCalendarList);

    }

    @Override
    public void onLeftButtonClick() {
        setDates(schoolCalendarList);

//        Date date = null;
//        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
//        String temp = "" + robotoCalendarView.getDate();
//        try {
//            date = formatter.parse(temp);
//            String formateDate = new SimpleDateFormat("dd-MM-yyyy").format(date);
//
//            String[] separated = formateDate.split("-");
//            String currentMonth = separated[1];
//            Log.v("currentMonth", currentMonth);
//
//            if (currentMonth.equals("12")) {
//                setDates(schoolCalendarList);
//            }
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }


    }

    public static void setDates(List<SchoolCalander> schoolCalendarList) {
        List<CalendarModel> calendarModelList = new ArrayList<>();
        ArrayList<Date> dates = new ArrayList<Date>();
        for (int i = 0; i < schoolCalendarList.size(); i++) {
            String str_startDate = schoolCalendarList.get(i).getStartTime();
            String str_endDate = schoolCalendarList.get(i).getEndTime();
            Log.v("str_endDate",str_endDate);
            String colorCode = schoolCalendarList.get(i).getColor();



            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = null;//You will get date object relative to server/client timezone wherever it is parsed
            Date endDate=null;
            try {
                startDate = dateFormat.parse(str_startDate);
                endDate= dateFormat.parse(str_endDate);
                DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy"); //If you need time just put specific format for time like 'HH:mm:ss'
                str_startDate = formatter.format(startDate);
                str_endDate = formatter.format(endDate);

            } catch (ParseException e) {
                e.printStackTrace();
            }


            DateFormat formatter;

            formatter = new SimpleDateFormat("dd-MM-yyyy");

            try {
                startDate = (Date) formatter.parse(str_startDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                endDate = (Date) formatter.parse(str_endDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            long interval = 24 * 1000 * 60 * 60; // 1 hour in millis
            long endTime = endDate.getTime(); // create your endtime here, possibly using Calendar or Date
            long curTime = startDate.getTime();
            while (curTime <= endTime) {
                dates.add(new Date(curTime));
                String ds = formatter.format(curTime);
                String[] items1 = ds.split("-");
                String d1 = items1[0];
                String m1 = items1[1];
                String y1 = items1[2];
                int d = Integer.parseInt(d1);
                int mo = Integer.parseInt(m1);
                int y = Integer.parseInt(y1);
                String fullDate=""+d+"-"+""+mo+"-"+""+y;

                CalendarModel calendarModel=new CalendarModel();
                calendarModel.setColorCode(colorCode);
                calendarModel.setDate(d);
                calendarModel.setYear(y);
                calendarModel.setMonth(mo);
                calendarModel.setFullDate(fullDate);


                if(!calendarModelList.contains(fullDate))
                {
                    calendarModelList.add(calendarModel);
                    Log.v("ds",""+ds);
                    curTime += interval;
                }



            }

            schoolCalendarList.get(i).setCalanderModel(calendarModelList);

        }

        List<CalendarModel> calendarModel_List=new ArrayList<>();
        for (int i = 0; i < schoolCalendarList.size(); i++) {

            calendarModel_List=schoolCalendarList.get(i).getCalanderModel();
            for(int j=0;j<calendarModel_List.size();j++)
            {
                int month=calendarModel_List.get(j).getMonth();
                Log.v("month",""+month);
                int date=calendarModel_List.get(j).getDate();
                Date cuurentDate = null;
                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
                String temp = "" + robotoCalendarView.getDate();
                try {
                    cuurentDate = formatter.parse(temp);
                    String formateDate = new SimpleDateFormat("dd-MM-yyyy").format(cuurentDate);

                    String[] separated = formateDate.split("-");
                    int currentMonth = Integer.parseInt(separated[1]);
                    Log.v("currentMonth", ""+currentMonth);

                    if (currentMonth==month) {
                        Calendar calendar = Calendar.getInstance();
                        Log.v("date",""+date);
                        String Month=""+Calendar.DAY_OF_MONTH;

                        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date startDate = null;//You will get date object relative to server/client timezone wherever it is parsed
                        Date endDate=null;
                        try {
                            startDate = dateFormat.parse(calendarModel_List.get(j).getFullDate());
                            DateFormat formatterr = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy"); //If you need time just put specific format for time like 'HH:mm:ss'
                            String finalDate = formatterr.format(startDate);
                            Log.v("finalDate",""+startDate);
                            calendar.set(Calendar.DAY_OF_MONTH, date);
                            int colorCalendar = Color.parseColor(schoolCalendarList.get(i).getColor());
                            robotoCalendarView.markCircleImage2(startDate,colorCalendar);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }


        }



//        for (int i = 24; i < 30; i++) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.DAY_OF_MONTH, i);
//            robotoCalendarView.markCircleImage2(calendar.getTime());
//
//        }
//        for (int i = 11; i < 13; i++) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(Calendar.DAY_OF_MONTH, i);
//            robotoCalendarView.markCircleImage2(calendar.getTime());
//        }
    }
}




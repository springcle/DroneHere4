package com.example.ksj_notebook.dronehere.Dronedb;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ksj_notebook.dronehere.MyApplication;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.data.DroneDB;
import com.example.ksj_notebook.dronehere.data.DroneRecommendResult;
import com.example.ksj_notebook.dronehere.manager.NetworkManager;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Request;

/**
 * Created by NAKNAK on 2017-03-07.
 */
public class SelectCategory {
    int cnt;
    Spinner spinner;
    EditText editText;
    TabDroneAdapter db2;
    LinearLayoutManager layoutManager;
    RecyclerView recyclerView;
    Double rating;
    String manufacture;
    String usage;
    Context context;
    String previous_selected;


    public SelectCategory(Context context, Spinner spinner, EditText editText, TabDroneAdapter db2, LinearLayoutManager linearLayoutManager, RecyclerView recyclerView) {
        this.context = context;
        this.spinner = spinner;
        this.editText = editText;
        this.db2 = db2;
        this.layoutManager = linearLayoutManager;
        this.recyclerView = recyclerView;
    }
    public void spinner_rollback(Spinner spinner, String previous_selected){
        Toast.makeText(context,previous_selected,Toast.LENGTH_SHORT).show();
        if(previous_selected.equals("이름(전체)")){
            cnt=1;
            spinner.setSelection(0);
        } else if(previous_selected.equals("별점")){
            cnt=1;
            spinner.setSelection(1);
        } else if(previous_selected.equals("제조사")){
            cnt=1;
            spinner.setSelection(2);
        } else if(previous_selected.equals("용도")){
            cnt=1;
            spinner.setSelection(3);
        }
    }

    /**
     * 추천화면(모든 드론) 스피너 정렬 순
     **/

    public void recommand_list() {
        cnt = 0;
        previous_selected = "이름(전체)";
        //final String selected_spinner = spinner.getSelectedItem().toString();
        NetworkManager.getInstance().getDroneRecommendName(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
            @Override
            public void onSuccess(Request request, DroneRecommendResult result) {
                db2.setDb2(result.getResult());
                layoutManager.scrollToPositionWithOffset(0, 0);
            }

            @Override
            public void onFail(Request request, IOException exception) {
            }
        });

    }

    /**
     * 에디트 텍스트 입력 시 드론리스트 변경부분
     **/
    public void text_listener() {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                /*
                layoutManager.setStackFromEnd(false);
                recyclerView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                recyclerView.setLayoutManager(layoutManager);
                */
                final String input_text = editText.getText().toString().toUpperCase();
                String selected_spinner;
                selected_spinner = spinner.getSelectedItem().toString();
                switch (selected_spinner) {
                    case "이름(전체)":
                        NetworkManager.getInstance().getDroneRecommendName(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
                            @Override
                            public void onSuccess(Request request, DroneRecommendResult result) {
                                ArrayList<DroneDB> full_list = new ArrayList<DroneDB>();
                                ArrayList<DroneDB> filter_list = new ArrayList<DroneDB>();
                                full_list = result.getResult();
                                if (input_text.equals("")) {
                                    recommand_list();
                                } else {
                                    for (int i = 0; i < full_list.size(); i++) {
                                        if (full_list.get(i).getDr_name().toUpperCase().contains(input_text) == true) {
                                            filter_list.add(full_list.get(i));
                                        }
                                    }
                                    db2.setDb2(filter_list);
                                    layoutManager.scrollToPositionWithOffset(0, 0);
                                }
                            }

                            @Override
                            public void onFail(Request request, IOException exception) {
                            }
                        });
                        break;
                    case "별점":
                        NetworkManager.getInstance().getDroneRecommendRate(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
                            @Override
                            public void onSuccess(Request request, DroneRecommendResult result) {
                                ArrayList<DroneDB> full_list = new ArrayList<DroneDB>();
                                ArrayList<DroneDB> filter_list = new ArrayList<DroneDB>();
                                full_list = result.getResult();
                                String selected_rate = rating + "";
                                if (input_text.equals("")) {
                                    for (int i = 0; i < full_list.size(); i++) {
                                        String rate = full_list.get(i).getDr_rate() + "";
                                        if (rate.equals(selected_rate) == true) {
                                            filter_list.add(full_list.get(i));
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < full_list.size(); i++) {
                                        String rate = full_list.get(i).getDr_rate() + "";
                                        if (rate.equals(selected_rate) == true && full_list.get(i).getDr_name().toUpperCase().contains(input_text) == true) {
                                            filter_list.add(full_list.get(i));
                                        }
                                    }
                                }
                                db2.setDb2(filter_list);
                                layoutManager.scrollToPositionWithOffset(0, 0);
                            }

                            @Override
                            public void onFail(Request request, IOException exception) {
                            }
                        });
                        break;
                    case "제조사":
                        NetworkManager.getInstance().getDroneRecommendName(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
                            @Override
                            public void onSuccess(Request request, DroneRecommendResult result) {
                                ArrayList<DroneDB> full_list = new ArrayList<DroneDB>();
                                ArrayList<DroneDB> filter_list = new ArrayList<DroneDB>();
                                full_list = result.getResult();
                                if (input_text.equals("")) {
                                    for (int i = 0; i < full_list.size(); i++) {
                                        if (full_list.get(i).getDr_manufacture().toUpperCase().equals(manufacture.toUpperCase()) == true) {
                                            filter_list.add(full_list.get(i));
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < full_list.size(); i++) {
                                        if (full_list.get(i).getDr_manufacture().toUpperCase().equals(manufacture.toUpperCase()) == true && full_list.get(i).getDr_name().toUpperCase().contains(input_text) == true) {
                                            filter_list.add(full_list.get(i));
                                        }
                                    }
                                }
                                db2.setDb2(filter_list);
                                layoutManager.scrollToPositionWithOffset(0, 0);
                            }

                            @Override
                            public void onFail(Request request, IOException exception) {
                            }
                        });
                        break;
                    case "용도":
                        NetworkManager.getInstance().getDroneRecommendName(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
                            @Override
                            public void onSuccess(Request request, DroneRecommendResult result) {
                                ArrayList<DroneDB> full_list = new ArrayList<DroneDB>();
                                ArrayList<DroneDB> filter_list = new ArrayList<DroneDB>();
                                full_list = result.getResult();
                                if (input_text.equals("")) {
                                    for (int i = 0; i < full_list.size(); i++) {
                                        if (full_list.get(i).getDr_use().toUpperCase().equals(usage.toUpperCase()) == true) {
                                            filter_list.add(full_list.get(i));
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < full_list.size(); i++) {
                                        if (full_list.get(i).getDr_use().toUpperCase().equals(usage.toUpperCase()) == true && full_list.get(i).getDr_name().toUpperCase().contains(input_text) == true) {
                                            filter_list.add(full_list.get(i));
                                        }
                                    }
                                }
                                db2.setDb2(filter_list);
                                layoutManager.scrollToPositionWithOffset(0, 0);
                            }

                            @Override
                            public void onFail(Request request, IOException exception) {
                            }
                        });

                        break;
                }
            }
        });
    }

    /**
     * 스피너 클릭 시 드론리스트 변경부분
     **/
    public void select_spinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (cnt == 0){
                    switch (position) {
                        case 0: // 이름 순
                            NetworkManager.getInstance().getDroneRecommendName(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
                                @Override
                                public void onSuccess(Request request, DroneRecommendResult result) {
                                    previous_selected = "이름";
                                    ArrayList<DroneDB> full_list = new ArrayList<DroneDB>();
                                    ArrayList<DroneDB> filter_list = new ArrayList<DroneDB>();
                                    full_list = result.getResult();
                                    String str = editText.getText().toString().toUpperCase();
                                    if (str.equals("")) {
                                        db2.setDb2(full_list);
                                        layoutManager.scrollToPositionWithOffset(0, 0);
                                    } else {
                                        for (int i = 0; i < full_list.size(); i++) {
                                            if (full_list.get(i).getDr_name().toUpperCase().contains(str) == true) {
                                                filter_list.add(full_list.get(i));
                                            }
                                        }
                                        db2.setDb2(filter_list);
                                        layoutManager.scrollToPositionWithOffset(0, 0);
                                    }
                                }

                                @Override
                                public void onFail(Request request, IOException exception) {
                                }
                            });
                            break;
                        case 1: // 별점 순
                            NetworkManager.getInstance().getDroneRecommendRate(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
                                @Override
                                public void onSuccess(Request request, DroneRecommendResult result) {
                                    ArrayList<DroneDB> full_list = new ArrayList<DroneDB>();
                                    final ArrayList<DroneDB> filter_list = new ArrayList<DroneDB>();
                                    full_list = result.getResult();
                                    final String str = editText.getText().toString().toUpperCase();
                                    RatingDialog dialog = new RatingDialog(context);
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.setCancelable(true);
                                    dialog.show();
                                    final ArrayList<DroneDB> finalFull_list = full_list;
                                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            spinner_rollback(spinner, previous_selected);
                                        }
                                    });
                                }
                                @Override
                                public void onFail(Request request, IOException exception) {
                                }
                            });
                            break;
                        case 2: // 제조사 순
                            NetworkManager.getInstance().getDroneRecommendBrand(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
                                @Override
                                public void onSuccess(Request request, DroneRecommendResult result) {
                                    boolean duplication;
                                    ArrayList<DroneDB> full_list = new ArrayList<DroneDB>();
                                    final ArrayList<DroneDB> filter_list = new ArrayList<DroneDB>();
                                    final ArrayList<String> manufacture_list = new ArrayList<String>();
                                    full_list = result.getResult();
                                    final String str = editText.getText().toString().toUpperCase();
                                    for (int i = 0; i < full_list.size(); i++) {
                                        duplication = false;
                                        for (int j = 0; j < manufacture_list.size(); j++) {
                                            /** 중복된 제조사 명 제거 **/
                                            if (manufacture_list.get(j).toString().toUpperCase().equals(full_list.get(i).getDr_manufacture().toString().toUpperCase()) == true) {
                                                duplication = true;
                                                break;
                                            }
                                        }
                                        if (duplication == false) {
                                            manufacture_list.add(full_list.get(i).getDr_manufacture().toString());
                                        }
                                    }
                                    ManufactureDialog dialog = new ManufactureDialog(context, manufacture_list);
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.setCancelable(true);
                                    dialog.show();
                                    final ArrayList<DroneDB> finalFull_list = full_list;
                                    final ArrayList<DroneDB> finalFull_list1 = full_list;
                                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            spinner_rollback(spinner, previous_selected);
                                        }
                                    });
                                }

                                @Override
                                public void onFail(Request request, IOException exception) {

                                }
                            });
                            break;
                        case 3: // 용도 순
                            NetworkManager.getInstance().getDroneRecommendUsage(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
                                @Override
                                public void onSuccess(Request request, DroneRecommendResult result) {
                                    boolean duplication;
                                    ArrayList<DroneDB> full_list = new ArrayList<DroneDB>();
                                    final ArrayList<DroneDB> filter_list = new ArrayList<DroneDB>();
                                    final ArrayList<String> usage_list = new ArrayList<String>();
                                    full_list = result.getResult();
                                    final String str = editText.getText().toString().toUpperCase();
                                    for (int i = 0; i < full_list.size(); i++) {
                                        duplication = false;
                                        for (int j = 0; j < usage_list.size(); j++) {
                                            /** 중복된 제조사 명 제거 **/
                                            if (usage_list.get(j).toString().toUpperCase().equals(full_list.get(i).getDr_use().toString().toUpperCase()) == true) {
                                                duplication = true;
                                                break;
                                            }
                                        }
                                        if (duplication == false) {
                                            usage_list.add(full_list.get(i).getDr_use().toString());
                                        }
                                    }
                                    UsageDialog dialog = new UsageDialog(context, usage_list);
                                    dialog.setCanceledOnTouchOutside(false);
                                    dialog.setCancelable(true);
                                    dialog.show();
                                    final ArrayList<DroneDB> finalFull_list = full_list;
                                    final ArrayList<DroneDB> finalFull_list1 = full_list;
                                    dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            spinner_rollback(spinner, previous_selected);
                                        }
                                    });
                                }

                                @Override
                                public void onFail(Request request, IOException exception) {

                                }
                            });
                            break;
                    }
                } else if(cnt==1) cnt=0;
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    class RatingDialog extends Dialog {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Button ok_btn;
            final RatingBar ratingBar;
            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.gravity = Gravity.CENTER;
            lpWindow.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lpWindow.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(lpWindow);
            setContentView(R.layout.rating_dialog);
            ok_btn = (Button) findViewById(R.id.rating_ok_btn);
            ratingBar = (RatingBar) findViewById(R.id.drone_ratingbar);
            ok_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rating = Double.valueOf(ratingBar.getRating());
                    previous_selected = "별점";
                    dismiss();

                    NetworkManager.getInstance().getDroneRecommendRate(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
                        @Override
                        public void onSuccess(Request request, DroneRecommendResult result) {
                            ArrayList<DroneDB> full_list = new ArrayList<DroneDB>();
                            final ArrayList<DroneDB> filter_list = new ArrayList<DroneDB>();
                            full_list = result.getResult();
                            final String str = editText.getText().toString().toUpperCase();
                            if (rating != null) {
                                String selected_rate = rating + "";
                                if (str.equals("")) {
                                    for (int i = 0; i < full_list.size(); i++) {
                                        String rate = full_list.get(i).getDr_rate() + "";
                                        if (rate.equals(selected_rate) == true) {
                                            filter_list.add(full_list.get(i));
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < full_list.size(); i++) {
                                        String rate = full_list.get(i).getDr_rate() + "";
                                        if (rate.equals(selected_rate) == true && full_list.get(i).getDr_name().toUpperCase().contains(str) == true) {
                                            filter_list.add(full_list.get(i));
                                        }
                                    }
                                }
                                db2.setDb2(filter_list);
                                layoutManager.scrollToPositionWithOffset(0, 0);
                            }
                        }

                        @Override
                        public void onFail(Request request, IOException exception) {
                        }
                    });

                }
            });
        }

        public RatingDialog(Context context) {
            super(context);
        }
    }

    class ManufactureDialog extends Dialog {
        ArrayList<String> manufacture_list;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ArrayAdapter<String> adapter;
            ListView listView;
            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.gravity = Gravity.CENTER;
            lpWindow.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lpWindow.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(lpWindow);
            setContentView(R.layout.manufacture_dialog);
            listView = (ListView) findViewById(R.id.manufacture_list);
            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
            listView.setAdapter(adapter);
            for (int i = 0; i < manufacture_list.size(); i++) {
                adapter.add(manufacture_list.get(i).toString());
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    previous_selected = "제조사";
                    for (int i = 0; i < manufacture_list.size(); i++) {
                        if (position == i) {
                            manufacture = manufacture_list.get(i).toString();
                            dismiss();
                        }
                    }
                    if (manufacture != null) {
                        NetworkManager.getInstance().getDroneRecommendName(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
                            @Override
                            public void onSuccess(Request request, DroneRecommendResult result) {
                                ArrayList<DroneDB> full_list = new ArrayList<DroneDB>();
                                ArrayList<DroneDB> filter_list = new ArrayList<DroneDB>();
                                full_list = result.getResult();
                                String str;
                                str = editText.getText().toString().toUpperCase();
                                if (str.equals("")) {
                                    for (int i = 0; i < full_list.size(); i++) {
                                        if (full_list.get(i).getDr_manufacture().toUpperCase().equals(manufacture.toUpperCase()) == true) {
                                            filter_list.add(full_list.get(i));
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < full_list.size(); i++) {
                                        if (full_list.get(i).getDr_manufacture().toUpperCase().equals(manufacture.toUpperCase()) == true && full_list.get(i).getDr_name().toUpperCase().contains(str) == true) {
                                            filter_list.add(full_list.get(i));
                                        }
                                    }
                                }
                                db2.setDb2(filter_list);
                                layoutManager.scrollToPositionWithOffset(0, 0);
                            }

                            @Override
                            public void onFail(Request request, IOException exception) {

                            }
                        });
                    }

                }
            });
        }

        public ManufactureDialog(Context context, ArrayList<String> manufacture_list) {
            super(context);
            //this.manufacture_list = new ArrayList<String>();
            this.manufacture_list = manufacture_list;
        }
    }

    class UsageDialog extends Dialog {
        ArrayList<String> usage_list;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ArrayAdapter<String> adapter;
            ListView listView;
            WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
            lpWindow.gravity = Gravity.CENTER;
            lpWindow.width = WindowManager.LayoutParams.WRAP_CONTENT;
            lpWindow.height = WindowManager.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(lpWindow);
            setContentView(R.layout.usage_dialog);
            listView = (ListView) findViewById(R.id.usage_list);
            adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
            listView.setAdapter(adapter);
            for (int i = 0; i < usage_list.size(); i++) {
                adapter.add(usage_list.get(i).toString());
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    previous_selected = "용도";
                    for (int i = 0; i < usage_list.size(); i++) {
                        if (position == i) {
                            usage = usage_list.get(i).toString();
                            dismiss();
                        }
                    }
                    if (usage != null) {
                        NetworkManager.getInstance().getDroneRecommendName(MyApplication.getContext(), new NetworkManager.OnResultListener<DroneRecommendResult>() {
                            @Override
                            public void onSuccess(Request request, DroneRecommendResult result) {
                                ArrayList<DroneDB> full_list = new ArrayList<DroneDB>();
                                ArrayList<DroneDB> filter_list = new ArrayList<DroneDB>();
                                full_list = result.getResult();
                                String str;
                                str = editText.getText().toString().toUpperCase();
                                if (str.equals("")) {
                                    for (int i = 0; i < full_list.size(); i++) {
                                        if (full_list.get(i).getDr_use().toUpperCase().equals(usage.toUpperCase()) == true) {
                                            filter_list.add(full_list.get(i));
                                        }
                                    }
                                } else {
                                    for (int i = 0; i < full_list.size(); i++) {
                                        if (full_list.get(i).getDr_use().toUpperCase().equals(usage.toUpperCase()) == true && full_list.get(i).getDr_name().toUpperCase().contains(str) == true) {
                                            filter_list.add(full_list.get(i));
                                        }
                                    }
                                }
                                db2.setDb2(filter_list);
                                layoutManager.scrollToPositionWithOffset(0, 0);
                            }

                            @Override
                            public void onFail(Request request, IOException exception) {

                            }
                        });
                    }
                }
            });
        }

        public UsageDialog(Context context, ArrayList<String> usage_list) {
            super(context);
            this.usage_list = usage_list;
        }
    }

}

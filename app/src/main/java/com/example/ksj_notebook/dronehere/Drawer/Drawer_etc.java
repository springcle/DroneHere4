package com.example.ksj_notebook.dronehere.Drawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ksj_notebook.dronehere.Drawercontent.Dc4;
import com.example.ksj_notebook.dronehere.Drawercontent.Dc5;
import com.example.ksj_notebook.dronehere.Drawercontent.Dc6;
import com.example.ksj_notebook.dronehere.Drawercontent.Dc8;
import com.example.ksj_notebook.dronehere.Drawercontent.Dc9;
import com.example.ksj_notebook.dronehere.MainActivity;
import com.example.ksj_notebook.dronehere.R;
import com.example.ksj_notebook.dronehere.login.StartActivity;
import com.example.ksj_notebook.dronehere.manager.PropertyManager;

/**
 * Created by NAKNAK on 2017-01-23.
 */
public class Drawer_etc extends AppCompatActivity{
    private ListView listview;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_etc);
        setTitle("기타설정");
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        listview = (ListView) findViewById(R.id.listView);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 :
                        Intent intent4 = new Intent(MainActivity.getContext(), Dc4.class);
                        MainActivity.getContext().startActivity(intent4);
                        break;
                    case 1 :
                        Intent intent5 = new Intent(MainActivity.getContext(), Dc5.class);
                        MainActivity.getContext().startActivity(intent5);
                        break;
                    case 2 :
                        Intent intent6 = new Intent(MainActivity.getContext(), Dc6.class);
                        MainActivity.getContext().startActivity(intent6);
                        break;
                    case 3 :
                        Intent intent7 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.molit.go.kr/USR/policyTarget/m_24066/dtl.jsp?idx=584"));
                        MainActivity.getContext().startActivity(intent7);
                        break;
                    case 4 :
                        Intent intent8 = new Intent(MainActivity.getContext(), Dc8.class);
                        MainActivity.getContext().startActivity(intent8);
                        break;
                    case 5 :
                        Intent intent9 = new Intent(MainActivity.getContext(), Dc9.class);
                        MainActivity.getContext().startActivity(intent9);
                        break;
                    case 6 :
                        PropertyManager.getInstance().setId("");
                        Intent intent10 = new Intent(MainActivity.getContext(), StartActivity.class);
                        MainActivity.getContext().startActivity(intent10);
                        finish();
                        MainActivity mainActivity = (MainActivity)MainActivity.mainactivity;
                        mainActivity.finish();
                        break;
                    case 7 :
                        break;
                }
            }
        });
        adapter.add("조종사 준수 사항");
        adapter.add("지도구역 안내");
        adapter.add("항공 촬영 지침");
        adapter.add("국토부 링크");
        adapter.add("회원약관");
        adapter.add("프로그램 정보");
        adapter.add("로그아웃");
        adapter.add(" ");
    }

}

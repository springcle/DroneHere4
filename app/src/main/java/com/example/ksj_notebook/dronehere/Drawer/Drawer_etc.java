package com.example.ksj_notebook.dronehere.Drawer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ksj_notebook.dronehere.Drawercontent.Dc5;
import com.example.ksj_notebook.dronehere.Drawercontent.Dc6;
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
                        Intent intent5 = new Intent(MainActivity.getContext(), Dc5.class);
                        MainActivity.getContext().startActivity(intent5);
                        break;
                    case 1 :
                        Intent intent6 = new Intent(MainActivity.getContext(), Dc6.class);
                        MainActivity.getContext().startActivity(intent6);
                        break;
                    case 2 :
                        Intent intent7 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.molit.go.kr/USR/policyTarget/m_24066/dtl.jsp?idx=584"));
                        MainActivity.getContext().startActivity(intent7);
                        break;
                    case 3 :
                        PropertyManager.getInstance().setId("");
                        Intent intent10 = new Intent(MainActivity.getContext(), StartActivity.class);
                        MainActivity.getContext().startActivity(intent10);
                        finish();
                        MainActivity mainActivity = (MainActivity)MainActivity.mainactivity;
                        mainActivity.finish();
                        break;
                    case 4 :
                        // 빈 항목 마지막에 추가안하면 밑에 선이 안생기므로 추가 후 비활성화
                        view.setEnabled(false);
                        break;
                }
            }
        });
        adapter.add("지도구역 안내");
        adapter.add("항공 촬영 지침");
        adapter.add("국토부 링크");
        adapter.add("로그아웃");
        adapter.add(" ");
    }

}

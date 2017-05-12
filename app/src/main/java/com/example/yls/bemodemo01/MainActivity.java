package com.example.yls.bemodemo01;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private List<Person> personList = new ArrayList<>();
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bmob.initialize(this,"0667bfaf81b5ab3fe26bd0a21c8f09b2");

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new MyAdapter(personList, MainActivity.this);

        LinearLayoutManager lm = new LinearLayoutManager(MainActivity.this);
        mRecyclerView.setLayoutManager(lm);
        mRecyclerView.setAdapter(mAdapter);
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddActivity.class));
            }
        });
    }

    private void deleteOne(){
        Person p = new Person();
        p.delete("624e0d4f1bd689c8", new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    ((TextView)findViewById(R.id.txt)).setText("success");
                }else{
                    ((TextView)findViewById(R.id.txt)).setText(e.toString());
                }
            }
        });
    }

    private void updateOne() {
        Person p = new Person();
        p.setAge(30);
        p.setAddress("体育西路");
        p.update("624e0d4f1bd689c8", new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    ((TextView)findViewById(R.id.txt)).setText("success");
                }else{
                    ((TextView)findViewById(R.id.txt)).setText(e.toString());
                }
            }
        });

    }

    protected void onResume() {
        super.onResume();
        queryAll();
    }

    private void queryAll() {


        BmobQuery<Person> query = new BmobQuery<>();
        query.findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> list, BmobException e) {
                if(e == null){
                    personList = list;  // 改变指向0XABEF
                    mAdapter.changData(personList);
                    ((TextView)findViewById(R.id.txt)).setText("数据返回 " + personList.size());
                }else{
                    Log.e("queryAll", e.toString());
                }
            }
        });
    }
    private void queryOne() {
        BmobQuery<Person> query = new BmobQuery<>();
        query.getObject("13fd2f557b", new QueryListener<Person>() {
            @Override
            public void done(Person person, BmobException e) {
                if(e == null){
                    ((TextView)findViewById(R.id.txt)).setText(person.getName() + "  " + person.getAddress());
                }else{
                    ((TextView)findViewById(R.id.txt)).setText(e.toString());
                }
            }
        });
    }

    private void add() {
        Person p1 = new Person();
        p1.setName("桃平");
        p1.setAge(19);
        p1.setAddress("天源路789");
        p1.setScore(98);
        p1.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                ((TextView)findViewById(R.id.txt)).setText(s);
            }
        });
    }

    public void del(String name) {
        // 操作数据库实现删除
    }

    public void refresh() {
        queryAll();
    }
}

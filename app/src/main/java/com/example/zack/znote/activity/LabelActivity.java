package com.example.zack.znote.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zack.znote.R;
import com.example.zack.znote.adapter.LabelAdapter;
import com.example.zack.znote.model.LabelItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zack on 2016/10/23.
 */
public class LabelActivity extends AppCompatActivity implements LabelAdapter.OnItemClickListener{

    private LinearLayout llLabelActivity;
    private LinearLayout llAddLabel;
    private LabelAdapter labelAdapter;
    private RecyclerView recyclerView;
    private EditText editText;
    private List<LabelItem> labelItems;
    private TextView tvNewLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);

        llLabelActivity = (LinearLayout) findViewById(R.id.ll_label_activity);
        llAddLabel = (LinearLayout) findViewById(R.id.ll_add_label);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_label);
        editText = (EditText) findViewById(R.id.edit_text);
        tvNewLabel = (TextView) findViewById(R.id.tv_new_label);

        initData();
        initToolbar();
        setListener();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        labelItems = new ArrayList<>();
        LabelItem label = new LabelItem("aaaaa", false);
        for (int i = 0; i < 40; i++)
            labelItems.add(label);
        labelItems.add(new LabelItem("bbbbb", false));
        labelItems.add(new LabelItem("ccccc", false));
        labelAdapter = new LabelAdapter(labelItems, this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(labelAdapter);
        labelAdapter.setOnItemClickListener(this);
    }

    /**
     * 初始化 Toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // 系统版本大于5.0时才设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar_1));
        }
        llLabelActivity.setBackgroundColor(ContextCompat.getColor(this, R.color.background_1));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onItemClick(View view, LabelItem item) {
        CheckBox cbLabel = (CheckBox) view.findViewById(R.id.cb_label);
        cbLabel.setChecked(!cbLabel.isChecked());
    }

    private void setListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
}

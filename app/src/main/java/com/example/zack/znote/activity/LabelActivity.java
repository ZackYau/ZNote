package com.example.zack.znote.activity;

import android.content.Intent;
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
import com.example.zack.znote.db.LabelDB;
import com.example.zack.znote.db.NotesDB;
import com.example.zack.znote.model.Label;
import com.example.zack.znote.model.LabelItem;
import com.example.zack.znote.model.Notes;

import java.util.ArrayList;
import java.util.Arrays;
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
    private List<LabelItem> labelTempItems; //标签暂存空间
    private TextView tvNewLabel;
    private Notes notes;
    private NotesDB notesDB;
    private LabelDB labelDB;

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

    @Override
    public void onBackPressed() {
        updateNotesLabel();
        super.onBackPressed();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        long id = intent.getLongExtra("notesId", 0);

        notesDB = notesDB.getInstance(this);
        notes = notesDB.query(id);
        labelDB = labelDB.getInstance(this);
        List<Label> labelList = labelDB.queryAll();

        labelItems = new ArrayList<>();
        labelTempItems = new ArrayList<>();
        String[] labelStr = notes.getLabels().split(",");
        List<String> checkedLabels = Arrays.asList(labelStr);
        boolean isChecked;
        LabelItem labelItem;
        for (Label label : labelList) {
            isChecked = checkedLabels.contains(label.getTitle());
            labelItem = new LabelItem(label.getTitle(), isChecked);
            labelItems.add(labelItem);
        }
        if (!labelItems.isEmpty()) {
            labelTempItems.addAll(labelItems);
        }
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
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onItemClick(View view, LabelItem item, int position) {
        CheckBox cbLabel = (CheckBox) view.findViewById(R.id.cb_label);
        cbLabel.setChecked(!cbLabel.isChecked());
        labelTempItems.get(position).setChecked(cbLabel.isChecked());
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
                updateLabelView(editable.toString());
            }
        });
        llAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = (String) tvNewLabel.getTag();
                createNewLabel(title);
            }
        });
    }

    /**
     * 新建一个标签
     */
    private void createNewLabel(String title) {
        labelDB.insert(new Label(title));
        editText.setText("");
        labelItems.clear();
        if (!labelTempItems.isEmpty()) {
            labelItems.addAll(labelTempItems);
        }
        LabelItem labelItem = new LabelItem(title, true);
        labelItems.add(0, labelItem);
        labelTempItems.add(0, labelItem);
        llAddLabel.setVisibility(View.GONE);
        labelAdapter.notifyDataSetChanged();
        updateNotesLabel();
    }

    /**
     * 监测添加标签内容
     */
    private void updateLabelView(String newLabel) {
        List<Label> labelList = labelDB.queryAllByKeyword(newLabel);
        String[] labelStr = notes.getLabels().split(",");
        List<String> checkedLabels = Arrays.asList(labelStr);
        labelItems.clear();
        boolean isChecked;
        boolean flag = true;
        LabelItem labelItem;
        for (Label label : labelList) {
            isChecked = checkedLabels.contains(label.getTitle());
            labelItem = new LabelItem(label.getTitle(), isChecked);
            labelItems.add(labelItem);
            if (newLabel.equals(label.getTitle())) {
                flag = false;
            }
        }
        if (flag) {
            llAddLabel.setVisibility(View.VISIBLE);
            tvNewLabel.setText(getResources().getString(R.string.create_label) + "\"" + newLabel + "\"");
            tvNewLabel.setTag(newLabel);
        } else {
            llAddLabel.setVisibility(View.GONE);
        }
        labelAdapter.notifyDataSetChanged();
    }

    /**
     * 更新记事标签
     */
    private void updateNotesLabel() {
        StringBuilder sb = new StringBuilder();
        for (LabelItem labelItem : labelTempItems) {
            if (labelItem.isChecked()) {
                sb.append(labelItem.getTitle()).append(",");
            }
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        notes.setLabels(sb.toString());
        notesDB.update(notes);
    }
}

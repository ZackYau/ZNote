package com.example.zack.znote.activity;

import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.zack.znote.R;
import com.example.zack.znote.adapter.ItemAdapter;
import com.example.zack.znote.model.Item;
import com.example.zack.znote.model.Notes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Zack on 2016/7/19.
 */
public class EditNotesActivity extends AppCompatActivity implements ItemAdapter.OnItemClickListener,View.OnClickListener{

    private ImageButton imgBtnAdd;
    private ImageButton imgBtnAddEtc;

    //View
    private CoordinatorLayout coordinatorLayout;
    private RelativeLayout rlBottomToolbar;
    private LinearLayout llBottomSheetAdd;
    private LinearLayout llBottomSheetAddEtc;
    private ItemAdapter itemAdapter;
    private RecyclerView recyclerViewAdd;
    private BottomSheetBehavior bottomSheetAddBehavior;
    private View bottomSheetAdd;

    private RecyclerView recyclerViewAddEtc;
    private BottomSheetBehavior bottomSheetAddEtcBehavior;
    private View bottomSheetAddEtc;

    private Notes notes;
    int[] pressColors = {R.color.press_1, R.color.press_2, R.color.press_3, R.color.press_4,
            R.color.press_5, R.color.press_6, R.color.press_7, R.color.press_8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_notes);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.edit_notes);
        rlBottomToolbar = (RelativeLayout) findViewById(R.id.bottom_toolbar);
        llBottomSheetAdd = (LinearLayout) findViewById(R.id.bottom_sheet_add);
        llBottomSheetAddEtc = (LinearLayout) findViewById(R.id.bottom_sheet_add_etc);
        imgBtnAdd = (ImageButton) findViewById(R.id.imgBtn_add);
        imgBtnAddEtc = (ImageButton) findViewById(R.id.imgBtn_add_etc);
        imgBtnAdd.setOnClickListener(this);
        imgBtnAddEtc.setOnClickListener(this);

        notes = new Notes("", "", 1, System.currentTimeMillis(), "", 0);
        initBottomSheet();
        initToolbar();

    }

    /**
     * 初始化 Toolbar
     */
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        int colorId = notes.getColorId();
        int[] backgroundColors = {R.color.background_1, R.color.background_2, R.color.background_3, R.color.background_4,
                R.color.background_5, R.color.background_6, R.color.background_7, R.color.background_8};
        int[] statusBarColors = {R.color.status_bar_1, R.color.status_bar_2, R.color.status_bar_3, R.color.status_bar_4,
                R.color.status_bar_5, R.color.status_bar_6, R.color.status_bar_7, R.color.status_bar_8};

        int backgroundColor = ContextCompat.getColor(this, backgroundColors[colorId - 1]);
        int statusColor = ContextCompat.getColor(this, statusBarColors[colorId - 1]);
        int pressColor = ContextCompat.getColor(this, pressColors[colorId - 1]);
        toolbar.setBackgroundColor(backgroundColor);
        coordinatorLayout.setBackgroundColor(backgroundColor);
        rlBottomToolbar.setBackgroundColor(backgroundColor);
        llBottomSheetAdd.setBackgroundColor(backgroundColor);
        llBottomSheetAddEtc.setBackgroundColor(backgroundColor);
        // 系统版本大于5.0时才设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(statusColor);
        }
        if (imgBtnAddEtc.isSelected())
            imgBtnAddEtc.setBackgroundColor(pressColor);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * 初始化 BottomSheet
     */
    private void initBottomSheet() {

        recyclerViewAdd = (RecyclerView) findViewById(R.id.recycler_view_add);
        recyclerViewAdd.setHasFixedSize(true);
        recyclerViewAdd.setLayoutManager(new LinearLayoutManager(this));

        bottomSheetAdd = findViewById(R.id.bottom_sheet_add);
        bottomSheetAddBehavior = BottomSheetBehavior.from(bottomSheetAdd);
        bottomSheetAddBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    imgBtnAdd.setSelected(false);
                    imgBtnAdd.setBackgroundColor(00000000);
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });

        recyclerViewAddEtc = (RecyclerView) findViewById(R.id.recycler_view_add_etc);
        recyclerViewAddEtc.setHasFixedSize(true);
        recyclerViewAddEtc.setLayoutManager(new LinearLayoutManager(this));
        bottomSheetAddEtc = findViewById(R.id.bottom_sheet_add_etc);
        bottomSheetAddEtcBehavior = BottomSheetBehavior.from(bottomSheetAddEtc);
        bottomSheetAddEtcBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    imgBtnAddEtc.setSelected(false);
                    imgBtnAddEtc.setBackgroundColor(00000000);
                }
            }

            @Override
            public void onSlide(View bottomSheet, float slideOffset) {

            }
        });
        itemAdapter = new ItemAdapter(updateItems(true), this);
        recyclerViewAdd.setAdapter(itemAdapter);

        itemAdapter = new ItemAdapter(updateItems(false), this);
        recyclerViewAddEtc.setAdapter(itemAdapter);
        itemAdapter.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit_notes, menu);
        for(int i = 0; i < menu.size(); i++){
            Drawable drawable = menu.getItem(i).getIcon();
            if(drawable != null) {
                drawable.mutate();
                drawable.setColorFilter(ContextCompat.getColor(this, R.color.iconColor), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        int colorId = notes.getColorId();
        switch (view.getId()) {
            case R.id.imgBtn_add:
                if (imgBtnAdd.isSelected()) {
                    bottomSheetAddBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    imgBtnAdd.setSelected(false);
                    imgBtnAdd.setBackgroundColor(00000000);
                } else {
                    bottomSheetAddBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    imgBtnAdd.setBackgroundColor(ContextCompat.getColor(this, pressColors[colorId - 1]));
                    imgBtnAdd.setSelected(true);
                }
                imgBtnAddEtc.setBackgroundColor(00000000);
                imgBtnAddEtc.setSelected(false);
                bottomSheetAddEtcBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.imgBtn_add_etc:
                if (imgBtnAddEtc.isSelected()) {
                    bottomSheetAddEtcBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    imgBtnAddEtc.setSelected(false);
                    imgBtnAddEtc.setBackgroundColor(00000000);
                } else {
                    bottomSheetAddEtcBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    imgBtnAddEtc.setBackgroundColor(ContextCompat.getColor(this, pressColors[colorId - 1]));
                    imgBtnAddEtc.setSelected(true);
                    backgroundColor();
                }
                imgBtnAdd.setBackgroundColor(00000000);
                imgBtnAdd.setSelected(false);
                bottomSheetAddBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(Item item) {

    }

    public List<Item> updateItems(boolean type) {
        ArrayList<Item> items = new ArrayList<>();
        if (type)
        {
            items.add(new Item(R.drawable.ic_camera_alt_white_24dp, getResources().getString(R.string.action_take_photo)));
            items.add(new Item(R.drawable.ic_image_white_24dp, getResources().getString(R.string.action_choose_image)));
            items.add(new Item(R.drawable.ic_keyboard_voice_white_24dp, getResources().getString(R.string.action_recording)));
            items.add(new Item(R.drawable.ic_label_white_24dp, getResources().getString(R.string.action_labels)));
            items.add(new Item(R.drawable.ic_list_white_24dp, getResources().getString(R.string.action_checkboxes)));
        } else {
            items.add(new Item(R.drawable.ic_delete_white_24dp, getResources().getString(R.string.action_delete)));
            items.add(new Item(R.drawable.ic_content_copy_white_24dp, getResources().getString(R.string.action_copy)));
            items.add(new Item(R.drawable.ic_send_white_24dp, getResources().getString(R.string.action_send)));

        }
        return items;
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetAddBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetAddBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (bottomSheetAddBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                Rect bottomSheetRect = new Rect();
                bottomSheetAdd.getGlobalVisibleRect(bottomSheetRect);
                if(!bottomSheetRect.contains((int)ev.getRawX(), (int)ev.getRawY()))
                    bottomSheetAddBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            if (bottomSheetAddEtcBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                Rect bottomSheetRect = new Rect();
                bottomSheetAddEtc.getGlobalVisibleRect(bottomSheetRect);
                if(!bottomSheetRect.contains((int)ev.getRawX(), (int)ev.getRawY()))
                    bottomSheetAddEtcBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 选择记事背景颜色
     */
    private void backgroundColor() {
        final Integer[] ids = {R.id.iv_color_1, R.id.iv_color_2, R.id.iv_color_3, R.id.iv_color_4, R.id.iv_color_5,
                R.id.iv_color_6, R.id.iv_color_7, R.id.iv_color_8};
        final Integer[] colorText = {R.string.color_name_1, R.string.color_name_2, R.string.color_name_3, R.string.color_name_4,
                R.string.color_name_5, R.string.color_name_6, R.string.color_name_7, R.string.color_name_8};
        final List<Integer> idList = new ArrayList<>(Arrays.asList(ids));
        for (int id : ids) {
            findViewById(id).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageView imageView;
                    for (int id : ids) {
                        imageView = (ImageView) findViewById(id);
                        imageView.setImageResource(R.drawable.ic_lens_black_36dp);
                    }
                    imageView = (ImageView) view;
                    imageView.setImageResource(R.drawable.ic_check_circle_white_36dp);
                    notes.setColorId(idList.indexOf(view.getId()) + 1);
                    initToolbar();
                }
            });
            findViewById(id).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //显示颜色信息
                    int colorId = notes.getColorId();
                    final int[] locations = new int[2];
                    view.getLocationOnScreen(locations);
                    DisplayMetrics dm = getResources().getDisplayMetrics();
                    String text = getResources().getString(colorText[idList.indexOf(view.getId())]);
                    if (colorId == idList.indexOf(view.getId()) + 1) {
                        text = text + " Selected";
                    }
                    Toast toast = Toast.makeText(EditNotesActivity.this, text, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP | Gravity.LEFT, locations[0] - dm.widthPixels / 15, locations[1] - dm.heightPixels / 8);
                    toast.show();
                    return true;
                }
            });
        }
    }

}

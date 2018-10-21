package com.order.badminton.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.order.badminton.R;
import com.order.badminton.about.AboutActivity;
import com.order.badminton.bufferknife.MyBindView;
import com.order.badminton.bufferknife.MyBufferKnifeUtils;

import java.util.ArrayList;
import java.util.List;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener {


    @MyBindView(R.id.tvStartTime)
    TextView tvStartTime;
    @MyBindView(R.id.tvEndTime)
    TextView tvEndTime;
    @MyBindView(R.id.tvOrderTimes)
    TextView tvTimes;
    @MyBindView(R.id.cbTwiceOK)
    CheckBox checkBox;

    @MyBindView(R.id.tvFirstNum)
    TextView tvFirstSelect;
    @MyBindView(R.id.tvRepeatTimes)
    TextView tvRepeatTime;
    @MyBindView(R.id.tvPrefer)
    TextView tvPrefer;
    @MyBindView(R.id.tvSound)
    Switch tvSound;


    SettingConfig config = SettingConfig.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        MyBufferKnifeUtils.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_back);
        toolbar.setLogo(null);
        toolbar.setTitle("设置");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.inflateMenu(R.menu.activity_setting_bar);
        //点击事件
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                return true;
            }
        });
        checkBox.setChecked(config.isDoubleCheck);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                config.setDoubleCheck(isChecked);
                config.save();
            }
        });

        CheckBox checkBoxVip = findViewById(R.id.cbVIP);

        checkBoxVip.setChecked(config.isOrderVipFlag);
        checkBoxVip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()

        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                config.setOrderVipFlag(isChecked);
                config.save();
            }
        });


        tvStartTime.setText(config.startTime);

        tvEndTime.setText(config.endTime);

        tvPrefer.setText(config.prefer);
        tvSound.setChecked(config.isSound);

        tvTimes.setText(config.maxOrderCount + "");
        tvRepeatTime.setText(config.repeatTime + "");
        tvFirstSelect.setText(bulidSelectString());


        tvStartTime.setOnClickListener(this);
        tvEndTime.setOnClickListener(this);
        tvTimes.setOnClickListener(this);
        tvRepeatTime.setOnClickListener(this);
        tvPrefer.setOnClickListener(this);

        tvSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                config.setSound(isChecked);
                config.save();
            }
        });
        tvFirstSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String arrs[] = buildSelections();
                new AlertDialog.Builder(SettingActivity.this).setTitle("多选")
                        .setMultiChoiceItems(arrs, buildSelectBools(arrs), new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    config.firstChang.add(arrs[which]);
                                } else {
                                    config.firstChang.remove(arrs[which]);
                                }
                                tvFirstSelect.setText(bulidSelectString());
                                config.save();

                            }
                        }).setPositiveButton("ok", null).create().show();
            }
        });


        tvTestMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    stopMusic();
                } else {
                    playMusic();
                }
            }
        });
    }

    boolean isPlaying = false;
    @MyBindView(R.id.btnTestMusic)
    TextView tvTestMusic;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopMusic();
    }

    MediaPlayer player;

    private void playMusic() {
        tvTestMusic.setText("停止");
        isPlaying = true;
        try {
            player = MediaPlayer.create(this, R.raw.super_mario_bgm);
            player.start();
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    stopMusic();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopMusic() {
        tvTestMusic.setText("测试");
        isPlaying = false;
        try {
            if (player != null) {
                player.setOnCompletionListener(null);
                player.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String[] buildSelections() {
        String arrs[] = new String[13];
        for (int i = 0; i < arrs.length; i++) {
            arrs[i] = Integer.toString(i + 1);
        }
        return arrs;
    }

    private boolean[] buildSelectBools(String arrs[]) {
        boolean[] booleans = new boolean[arrs.length];
        int index = 0;
        for (String string : arrs) {
            booleans[index] = config.firstChang.contains(string);
            index++;
        }
        return booleans;
    }


    private String bulidSelectString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (String con : config.firstChang) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(con);
        }
        return stringBuilder.substring(0);
    }


    private String[] createTimeArr() {
        List<String> lists = new ArrayList<>();
        for (int i = 9; i <= 21; i++) {
            lists.add(String.format("%02d:00", i));
            lists.add(String.format("%02d:30", i));
        }
        lists.add(String.format("%02d:00", 22));
        String arrs[] = new String[lists.size()];
        lists.toArray(arrs);
        return arrs;
    }

    private int getCurrentIndex(String arrs[], String choose) {
        int index = 0;
        for (String ss : arrs) {
            if (choose.equals(ss)) {
                return index;
            }
            index++;
        }
        return index;
    }


    @Override
    public void onClick(View v) {

        String arrs[] = null;
        String title = null;
        switch (v.getId()) {
            case R.id.tvStartTime:
                arrs = createTimeArr();
                title = "请选择起始时间";
                break;
            case R.id.tvEndTime:
                arrs = createTimeArr();
                title = "请选择结束时间";
                break;
            case R.id.tvOrderTimes:
                arrs = new String[]{"1", "2", "3"};
                title = "请选择需要的场数";
                break;
            case R.id.tvRepeatTimes:
                arrs = new String[]{"1", "2", "3"};
                title = "请选择重复次数";
                break;
            case R.id.tvPrefer:
                arrs = new String[]{"时间优先", "场次优先"};
                title = "请选择偏好设置";
                break;
        }

        final TextView textView = ((TextView) v);

        final String arrDatas[] = arrs;
        int index = getCurrentIndex(arrs, textView.getText().toString());
        new AlertDialog.Builder(this).setTitle(title)
                .setSingleChoiceItems(arrDatas, index, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        String data = arrDatas[which];
                        textView.setText(data);
                        switch (textView.getId()) {
                            case R.id.tvStartTime:
                                config.setStartTime(data);
                                break;
                            case R.id.tvEndTime:
                                config.setEndTime(data);
                                break;
                            case R.id.tvOrderTimes:
                                config.setMaxOrderCount(Integer.parseInt(data));
                                break;
                            case R.id.tvRepeatTimes:
                                config.setRepeatTime(Integer.parseInt(data));
                                break;
                            case R.id.tvPrefer:
                                config.setPrefer(data);
                                break;
                        }
                        config.save();
                    }
                }).setPositiveButton("ok", null).create().show();

    }
}

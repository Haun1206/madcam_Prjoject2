package com.example.tabswithanimatedswipe;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import yuku.ambilwarna.AmbilWarnaDialog;

import static android.os.Environment.DIRECTORY_PICTURES;

public class TabFragment3 extends Fragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
    /* Define Variables */
    private static final int MY_PERMISSION_EXTERNAL_WRITE = 4444;

    private Button draw_red_btn;
    private Button draw_blue_btn;
    private Button draw_green_btn;
    private Button clearbtn;
    private Button eraser_btn;
    private Button custombtn;
    private Button save_color_btn;
    private Button option_btn;
    int tColor;
    private SimpleDrawingView paintview;
    private SimpleDrawingView new_paintview;
    SeekBar seek;
    String token;

    SeekBar.OnSeekBarChangeListener mSeekBar = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            paintview.change_size(i);
            paintview.invalidate();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_object3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        paintview = (SimpleDrawingView) view.findViewById(R.id.simpleDrawingView1);
        new_paintview = (SimpleDrawingView) view.findViewById(R.id.simpleDrawingView1);
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintview.init(metrics, null);

        /* Buttons */
        draw_red_btn = (Button) view.findViewById(R.id.draw_red_btn);
        draw_red_btn.setOnClickListener(this);

        draw_blue_btn = (Button) view.findViewById(R.id.draw_blue_btn);
        draw_blue_btn.setOnClickListener(this);

        draw_green_btn = (Button) view.findViewById(R.id.draw_green_btn);
        draw_green_btn.setOnClickListener(this);

        clearbtn = (Button) view.findViewById(R.id.clearbtn);
        clearbtn.setOnClickListener(this);

        eraser_btn = (Button) view.findViewById(R.id.eraser_btn);
        eraser_btn.setOnClickListener(this);

        custombtn = (Button) view.findViewById(R.id.custombtn);
        custombtn.setOnClickListener(this);

        save_color_btn = (Button) view.findViewById(R.id.save_color_btn);
        save_color_btn.setOnClickListener(this);

        option_btn = (Button) view.findViewById(R.id.option_btn);
        option_btn.setOnClickListener(this);

        /* SeekBar */
        seek = (SeekBar) view.findViewById(R.id.simpleSeekBar);
        seek.setOnSeekBarChangeListener(mSeekBar);
        seek.setMax(40);
        seek.setProgress(10);
    }

    // Obtain MotionEvent object
    long downTime = SystemClock.uptimeMillis();
    long eventTime = SystemClock.uptimeMillis() + 100;
    float x = 0.0f;
    float y = 0.0f;
    // List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
    int metaState = 0;
    MotionEvent motionEvent = MotionEvent.obtain(
            downTime,
            eventTime,
            MotionEvent.ACTION_UP,
            x,
            y,
            metaState
    );


    /* for setting background */
    private void openColorPicker_bg() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getActivity(), tColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                paintview.setbgcolor(color);
                paintview.dispatchTouchEvent(motionEvent);
            }
        });
        colorPicker.show();
    }

    /*for picking custom color */
    private void openColorPicker() {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getActivity(), tColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                paintview.setColor(color);
                save_color_btn.setBackgroundColor(color);
            }
        });
        colorPicker.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.draw_red_btn:
                paintview.red();
                break;
            case R.id.draw_blue_btn:
                paintview.blue();
                break;
            case R.id.draw_green_btn:
                paintview.green();
                break;
            case R.id.clearbtn:
                paintview.clear();
                break;
            case R.id.eraser_btn:
                paintview.erase();
                break;
            case R.id.custombtn:
                openColorPicker();
                break;
            case R.id.save_color_btn:
                ColorDrawable saved_color = (ColorDrawable)save_color_btn.getBackground();
                int save = saved_color.getColor();
                paintview.saveColor(save);
                break;
            case R.id.option_btn:
                PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.setOnMenuItemClickListener(this);
                popup.inflate(R.menu.menu_main);
                popup.show();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.normal:
                paintview.normal();
                return true;
            case R.id.emboss:
                paintview.emboss();
                return true;
            case R.id.blur:
                paintview.blur();
                return true;
            case R.id.save:
                token = createAdditionDialog();
                return true;
            case R.id.open:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 111);
                return true;
            case R.id.setBackground:
                openColorPicker_bg();
                return true;
            default:
                return false;
        }
    }

    @Override //갤러리에서 이미지 불러온 후 행동
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 111) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getActivity().getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지뷰에 세팅
                    DisplayMetrics metrics = new DisplayMetrics();
                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
                    new_paintview.init(metrics, img);
                    new_paintview.invalidate();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String createAdditionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        final String[] name = new String[1];
        MenuItem item = null;

        /* Inflate and set the layout for the dialog */
        /* Pass null as the parent view because its going in the dialog layout */
        View dialog_layout = inflater.inflate(R.layout.save_picture, null);
        builder.setView(dialog_layout)
        /* Add Action Buttons */
                .setPositiveButton(R.string.dialog_positive_button_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /* Add filename to list and save picture */
                        EditText editText_filename = dialog_layout.findViewById(R.id.filename);

                        name[0] = editText_filename.getText().toString();

                        if (name[0].isEmpty()) {
                            Toast.makeText(getActivity(), "정보를 모두 입력해 주세요.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        checkExternalWritePermission(item, name[0]);
                    }
                })
                .setNegativeButton(R.string.dialog_negative_button_string, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                    }
                })
                .setCancelable(false)
                .create()
                .show();
        return name[0];
    }


    private void checkExternalWritePermission(MenuItem item, String name) {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(getActivity())
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                startActivity(intent);
                                Toast.makeText(getActivity(), "외부 저장소 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(getActivity(), "외부 저장소 권한을 활성화 하셔야 합니다.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_EXTERNAL_WRITE);
            }
        } else {
            // permssion 있을 때 할 일
            paintview.invalidate();
            paintview.buildDrawingCache();
            Bitmap bitmap = paintview.getDrawingCache();
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/Pictures/" + name + ".png");
            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
                if(out != null){
                    bitmap.compress(Bitmap.CompressFormat.PNG, 75, out);
                    Toast.makeText(getActivity(), "Save Success", Toast.LENGTH_SHORT).show();
                }
                out.close();
            }
            catch (IOException e){
                e.printStackTrace();
                Toast.makeText(getActivity(), "File Not Found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
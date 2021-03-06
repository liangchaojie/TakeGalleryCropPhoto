package com.ediantong.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ediantong.R;
import com.ediantong.adapter.ImagePickAdapter;
import com.ediantong.bean.ImageBean;
import com.ediantong.library.AlbumImageUtils;

import java.util.ArrayList;
import java.util.List;

public class SelectPictureActivity extends AppCompatActivity {

    private final int CODE_GALLERY_REQUEST = 100;

    private Thread mWorkThread;
    private List<String> mList = new ArrayList<>();
    private RecyclerView recycler_view;
    private int mCowCount = 4;
    private ImagePickAdapter adapter = new ImagePickAdapter(R.layout.item_image_pick,mList,mCowCount);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture);
        initView();
    }

    private void initView() {
        recycler_view = findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new GridLayoutManager(this,mCowCount));
        recycler_view.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadAlbums();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if(mWorkThread!=null){
             mWorkThread.interrupt();
        }
    }

    private void loadAlbums() {
        AlbumImageUtils.getImageList(this, new AlbumImageUtils.OnAlbumListener() {
            @Override
            public void returnImageListOnBackThread(final List<ImageBean> imagePathList,Thread workThread) {
                mWorkThread = workThread;
                mList.clear();
                for (int i = 0; i < imagePathList.size(); i++) {
                    mList.add(imagePathList.get(i).path);
                }
                adapter.setNewData(mList);
                adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        turn2CropActivity(position);
                    }
                });
            }
        });
    }

    /**
     * 跳转到裁剪activity
     * @param position
     */
    private void turn2CropActivity(int position) {
        if(mList==null||mList.size()==0){
            return;
        }
        Intent intent = new Intent();
        intent.setData(Uri.parse(mList.get(position)));
        setResult(CODE_GALLERY_REQUEST,intent);
        finish();
    }
}

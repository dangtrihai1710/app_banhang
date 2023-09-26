package com.example.app_banhang.activity;
import com.example.app_banhang.adapter.SanPhamMoiAdapter;

import com.example.app_banhang.model.SanPhamMoi;

import com.example.app_banhang.network.NetworkUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.os.Bundle;

import com.example.app_banhang.R;
import com.example.app_banhang.adapter.LoaiSpAdapter;
import com.example.app_banhang.model.LoaiSp;
import com.example.app_banhang.retrofit.ApiBanHang;
import com.example.app_banhang.retrofit.RetrofitClient;
import com.example.app_banhang.utils.Utils;
import com.google.android.material.navigation.NavigationView;
import java.util.List;
import java.util.ArrayList;
import com.bumptech.glide.Glide;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MainActivity extends AppCompatActivity {
    Toolbar toolbar;
    ViewFlipper viewFlipper;
    RecyclerView recylerViewManHinhChinh;
    NavigationView navigationView;
    ListView listViewManHinhChinh;
    DrawerLayout drawerLayout;
    LoaiSpAdapter loaiSpAdapter;
    List<LoaiSp> mangloaisp;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    ApiBanHang apiBanHang;
    List<SanPhamMoi> mangSpMoi;
    SanPhamMoiAdapter spAdapter;

    private NetworkUtils networkUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiBanHang = RetrofitClient.getInstance(Utils.BASE_URL).create(ApiBanHang.class);


        Anhxa();
        ActionBar();
        ActionViewFlipper();


        networkUtils = new NetworkUtils(this);
        networkUtils.startNetworkListener(new NetworkUtils.NetworkListener() {
            @Override
            public void onNetworkConnected() {
                // Xử lý khi có kết nối mạng
                ActionViewFlipper();
                getLoaiSanPham();
                getSpMoi();
                getEventClick();
            }



            @Override
            public void onNetworkDisconnected() {
                // Xử lý khi mất kết nối mạng
                Toast.makeText(getApplicationContext(), "Không có Internet, vui lòng kết nối lại", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void getEventClick() {
        listViewManHinhChinh.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        Intent trangchu = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(trangchu);
                        break;
                    case 1:
                        Intent gamethethao = new Intent(getApplicationContext(), GameTheThaoActivity.class);
                        startActivity(gamethethao);
                        break;
                    case 2:
                        Intent gamekinhdi = new Intent(getApplicationContext(), GameKinhDiActivity.class);
                        startActivity(gamekinhdi);
                        break;
                }
            }
        });
    }

    private void getSpMoi() {
        compositeDisposable.add(apiBanHang.getSpMoi()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        sanPhamMoiModel -> {
                            if(sanPhamMoiModel.isSuccess()) {
                                mangSpMoi = sanPhamMoiModel.getResult();
                                spAdapter = new SanPhamMoiAdapter(getApplicationContext(), mangSpMoi);
                                recylerViewManHinhChinh.setAdapter(spAdapter);
                            } else {
                                Toast.makeText(getApplicationContext(), "Không thể kết nối với server", Toast.LENGTH_LONG).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không thể kết nối với server: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }

    private void getLoaiSanPham() {
        compositeDisposable.add(apiBanHang.getLoaiSp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        loaiSpModel -> {
                            if (loaiSpModel.isSuccess()) {
                                mangloaisp = loaiSpModel.getResult();
                                loaiSpAdapter = new LoaiSpAdapter(getApplicationContext(), mangloaisp);
                                listViewManHinhChinh.setAdapter(loaiSpAdapter);
                            } else {
                                Toast.makeText(getApplicationContext(), "Không thể kết nối với server", Toast.LENGTH_LONG).show();
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), "Không thể kết nối với server: " + throwable.getMessage(), Toast.LENGTH_LONG).show();
                        }
                ));
    }


    private void ActionViewFlipper() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<String> mangquangcao = new ArrayList<>();
                mangquangcao.add("https://lsx.vn/wp-content/uploads/2022/08/Vi-pham-ban-quyen-game.jpg");
                mangquangcao.add("https://img.upanh.tv/2023/09/21/Noi-dung-doan-van-ban-ca-bane2dfe78311c6ee17.jpg");
                mangquangcao.add("https://ggpay-dev.s3.ap-southeast-1.amazonaws.com/public/images/2022-6-13/14/c7239d6322cdfead6899b150965ce23e_origin");
                for(int i = 0; i <mangquangcao.size(); i++) {
                    ImageView imageView = new ImageView(getApplicationContext());
                    Glide.with(getApplicationContext()).load(mangquangcao.get(i)).into(imageView);
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    viewFlipper.addView(imageView);
                }
                viewFlipper.setFlipInterval(3000);
                viewFlipper.setAutoStart(true);
                Animation slide_in = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_in_right);
                Animation slide_out = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
                viewFlipper.setInAnimation(slide_in);
                viewFlipper.setOutAnimation(slide_out);
            }
        });
    }


    private void ActionBar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(android.R.drawable.ic_menu_sort_by_size);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbarmanhinhchinh);
        viewFlipper = findViewById(R.id.viewflipper);
        recylerViewManHinhChinh = findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recylerViewManHinhChinh.setLayoutManager(layoutManager);
        recylerViewManHinhChinh.setHasFixedSize(true);
        listViewManHinhChinh = findViewById(R.id.listviewmanhinhchinh);
        navigationView = findViewById(R.id.navigationview);
        drawerLayout = findViewById(R.id.drawerlayout);
        // khoi tao list
        mangloaisp = new ArrayList<>();
        mangSpMoi = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
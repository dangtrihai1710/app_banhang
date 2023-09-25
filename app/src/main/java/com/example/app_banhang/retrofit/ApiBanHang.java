package com.example.app_banhang.retrofit;

import com.example.app_banhang.model.LoaiSpModel;
import com.example.app_banhang.model.SanPhamMoiModel;


import retrofit2.http.GET;
import io.reactivex.rxjava3.core.Observable;

public interface ApiBanHang {
    @GET("getloaisp.php")
    Observable<LoaiSpModel> getLoaiSp();

    @GET("getspmoi.php")
    Observable<SanPhamMoiModel> getSpMoi();
}

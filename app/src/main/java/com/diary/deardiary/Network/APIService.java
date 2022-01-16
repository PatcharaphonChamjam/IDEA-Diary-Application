package com.diary.deardiary.Network;

import com.diary.deardiary.Model.Edit;
import com.diary.deardiary.Model.test;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by USER on 28/7/2560.
 */

public interface APIService {

    @FormUrlEncoded
    @POST("Diary/app/Login.php")
    Observable<RSM> userLogin(@Field("username") String username,
                               @Field("password") String password);

    @FormUrlEncoded
    @POST("Diary/app/Register.php")
    Observable<RSM> userRegister(@Field("username") String username,
                                 @Field("email") String email,
                                 @Field("password") String password);

    @FormUrlEncoded
    @POST("Diary/app/insertDiary.php")
    Observable<RSM> insertDiary(@Field("userID") String userid,
                                 @Field("titleDiary") String titlediary,
                                 @Field("storyDiary") String storydiary);


    @Multipart
    @POST("Diary/app/InsertPicDiary.php")
    Observable<test> uploadFile(@Part MultipartBody.Part file,
                                @Part("picDiary") RequestBody name,
                                @Part("userID") String userid,
                                @Part("titleDiary") String titlediary,
                                @Part("storyDiary") String storydiary
                                );


    @FormUrlEncoded
    @POST("Diary/app/showcal.php")
    Observable<RSM> showall(@Field("userID") String userid);

    @FormUrlEncoded
    @POST("Diary/app/showclick.php")
    Observable<RSM> showclick(@Field("userID") String userid,
                              @Field("Datetime") String datetime);

    @FormUrlEncoded
    @POST("Diary/app/editreview.php")
    Observable<RSM> editreview(@Field("diaryID") String idreview);

    @FormUrlEncoded
    @POST("Diary/app/Changepass.php")
    Observable<RSM> changepass(@Field("userID") String userid,
                               @Field("password") String password);

    @FormUrlEncoded
    @POST("Diary/app/Deleteclick.php")
    Observable<RSM> deletediary(@Field("diaryID") String idreview);

    @Multipart
    @POST("Diary/app/Updatepicdi.php")
    Observable<Edit> updatediary(@Part MultipartBody.Part file,
                                 @Part("picDiary") RequestBody name,
                                 @Part("userID") String userid,
                                 @Part("diaryID") String idreview,
                                 @Part("titleDiary") String titlediary,
                                 @Part("storyDiary") String storydiary
    );

    @FormUrlEncoded
    @POST("Diary/app/Updatedi.php")
    Observable<RSM> updateno(@Field("userID") String userid,
                             @Field("diaryID") String idreview,
                             @Field("titleDiary") String titlediary,
                             @Field("storyDiary") String storydiary);

    @FormUrlEncoded
    @POST("Diary/app/Updateme.php")
    Observable<RSM> updateme(@Field("userID") String userid,
                             @Field("diaryID") String idreview,
                             @Field("titleDiary") String titlediary,
                             @Field("storyDiary") String storydiary);
}

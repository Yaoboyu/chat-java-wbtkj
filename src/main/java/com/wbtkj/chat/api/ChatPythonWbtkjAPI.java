package com.wbtkj.chat.api;

import com.wbtkj.chat.pojo.dto.chatPythonWbtkj.ExtractUrl;
import com.wbtkj.chat.pojo.dto.chatPythonWbtkj.FileContentRes;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import retrofit2.http.*;

public interface ChatPythonWbtkjAPI {
    /**
     * 解析网页
     * @param extractUrl
     * @return
     */
    @POST("/parse/url")
    Single<FileContentRes> extractUrl(@Body() ExtractUrl extractUrl);

    /**
     * 解析文档
     * @param file
     * @return
     */
    @Multipart
    @POST("/parse/file")
    Single<FileContentRes> extractFile(@Part() MultipartBody.Part file);
}

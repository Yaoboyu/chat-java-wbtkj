package com.wbtkj.chat.api;

import com.wbtkj.chat.pojo.dto.chatPythonWbtkj.ExtractUrl;
import com.wbtkj.chat.pojo.dto.chatPythonWbtkj.FileContent;
import com.wbtkj.chat.pojo.dto.chatPythonWbtkj.FileContentRes;
import io.reactivex.Single;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
@Component
public class ChatPythonWbtkjClient {
    private String apiHost;

    private OkHttpClient okHttpClient;

    private ChatPythonWbtkjAPI chatPythonWbtkjAPI;

    public ChatPythonWbtkjClient(@Value("${chat-py-wbtkj.apiHost}") String apiHost) {
        this.apiHost = apiHost;

        this.okHttpClient = new OkHttpClient
                .Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(600, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .build();

        this.chatPythonWbtkjAPI = new Retrofit.Builder()
                .baseUrl(apiHost)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build().create(ChatPythonWbtkjAPI.class);
    }

    public FileContent extractUrl(String url) {
        ExtractUrl extractUrl = ExtractUrl.builder().url(url).build();
        Single<FileContentRes> resSingle = this.chatPythonWbtkjAPI.extractUrl(extractUrl);
        FileContentRes fileContentRes = resSingle.blockingGet();
        return fileContentRes.getData();
    }

    public FileContent extractFile(MultipartFile multipartFile) {
        String fileName = multipartFile.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        File file = null;
        try {
            file = File.createTempFile(fileName, prefix);
            multipartFile.transferTo(file);

            // 创建 RequestBody，用于封装构建RequestBody
            RequestBody fileBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
            MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), fileBody);

            Single<FileContentRes> uploadFileResponse = this.chatPythonWbtkjAPI.extractFile(multipartBody);
            FileContentRes result = uploadFileResponse.blockingGet();

            return result.getData();
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            // 操作完上面的文件 需要删除在根目录下生成的临时文件
            file.delete();
        }

        return null;
    }

}

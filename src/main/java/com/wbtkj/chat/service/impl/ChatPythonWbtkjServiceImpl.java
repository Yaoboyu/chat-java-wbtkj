package com.wbtkj.chat.service.impl;

import com.wbtkj.chat.api.ChatPythonWbtkjAPI;
import com.wbtkj.chat.constant.GeneralConstant;
import com.wbtkj.chat.exception.MyServiceException;
import com.wbtkj.chat.mapper.FileEmbeddingMapper;
import com.wbtkj.chat.pojo.dto.chatPythonWbtkj.ExtractUrl;
import com.wbtkj.chat.pojo.dto.chatPythonWbtkj.FileContent;
import com.wbtkj.chat.pojo.dto.chatPythonWbtkj.FileContentRes;
import com.wbtkj.chat.pojo.dto.openai.embeddings.TextAndEmbedding;
import com.wbtkj.chat.pojo.model.FileEmbedding;
import com.wbtkj.chat.pojo.model.FileEmbeddingExample;
import com.wbtkj.chat.service.ChatPythonWbtkjService;
import com.wbtkj.chat.service.FileService;
import com.wbtkj.chat.service.OpenAIService;
import com.wbtkj.chat.service.UserService;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import javax.annotation.Resource;
import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ChatPythonWbtkjServiceImpl implements ChatPythonWbtkjService {
    private String apiHost;

    private OkHttpClient okHttpClient;

    private ChatPythonWbtkjAPI chatPythonWbtkjAPI;

    @Resource
    FileEmbeddingMapper fileEmbeddingMapper;
    @Resource
    FileService fileService;
    @Resource
    OpenAIService openAIService;
    @Resource
    UserService userService;

    public ChatPythonWbtkjServiceImpl(@Value("${chat-py-wbtkj.apiHost}") String apiHost) {
        this.apiHost = apiHost;

        this.okHttpClient = new OkHttpClient
                .Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
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

    @Override
    @Async
    public void extractUrl(String url, long userFileId, long userId) {
        ExtractUrl extractUrl = ExtractUrl.builder().url(url).build();
        Single<FileContentRes> resSingle = this.chatPythonWbtkjAPI.extractUrl(extractUrl);
        FileContentRes result = resSingle.blockingGet();
        log.info("url: {}, hashId: {}, content size: {}", extractUrl.getUrl(), result.getData().getHashId(), result.getData().getContents().size());

        try {
            storage(result.getData(), userFileId, userId);
        } catch (Exception e) {
            e.printStackTrace();
            storage(new FileContent(), userFileId, userId);
        }

        return;
    }

    @Override
    @Async
    public void extractFile(MultipartFile multipartFile, long userFileId, long userId) {
        if (multipartFile == null) {
            return;
        }

        String fileName = multipartFile.getOriginalFilename();
        String prefix = fileName.substring(fileName.lastIndexOf("."));

        File file = null;
        try {
            log.info("start file {}", fileName);
            file = File.createTempFile(fileName, prefix);
            log.info("created file {}", fileName);
            multipartFile.transferTo(file);
            log.info("copyed file {}", fileName);

            // 创建 RequestBody，用于封装构建RequestBody
            RequestBody fileBody = RequestBody.create(file, MediaType.parse("multipart/form-data"));
            MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file", file.getName(), fileBody);

            Single<FileContentRes> uploadFileResponse = this.chatPythonWbtkjAPI.extractFile(multipartBody);
            FileContentRes result = uploadFileResponse.blockingGet();

            log.info("文件名: {}, hashId: {}, content size: {}", fileName, result.getData().getHashId(), result.getData().getContents().size());

            storage(result.getData(), userFileId, userId);

            return;
        } catch (Exception e) {
            log.info("exception {}", e.getMessage());
            e.printStackTrace();
            storage(new FileContent(), userFileId, userId);

        } finally {
            // 操作完上面的文件 需要删除在根目录下生成的临时文件
            file.delete();
        }

        return;
    }

    @Override
    @Transactional
    public void storage(FileContent fileContent, long userFileId, long userId) {
        if (CollectionUtils.isEmpty(fileContent.getContents())) { // 不支持解析此网页或文件
            fileService.addNameAfterParse(userFileId, "error");
            return;
        }

        FileEmbeddingExample fileEmbeddingExample = new FileEmbeddingExample();
        fileEmbeddingExample.createCriteria().andNameEqualTo(genName(fileContent.getHashId(), fileContent.getLang()));
        List<FileEmbedding> fileEmbeddings = fileEmbeddingMapper.selectByExample(fileEmbeddingExample);
        if (!CollectionUtils.isEmpty(fileEmbeddings)) { // 已经解析过相同的文件
            fileService.addNameAfterParse(userFileId, genName(fileContent.getHashId(), fileContent.getLang()));
            return;
        }

        // 获得embeddings
        List<TextAndEmbedding> embeddings = openAIService.embeddings(fileContent.getContents());
        // TODO：添加消息队列重试
        if (CollectionUtils.isEmpty(embeddings)) {
            fileService.addNameAfterParse(userFileId, "error");
            return;
        }

        fileService.addNameAfterParse(userFileId, genName(fileContent.getHashId(), fileContent.getLang()));

        for (TextAndEmbedding textAndEmbedding : embeddings) {
            FileEmbedding fileEmbedding = new FileEmbedding();
            fileEmbedding.setName(genName(fileContent.getHashId(), fileContent.getLang()));
            fileEmbedding.setText(textAndEmbedding.getText());
            fileEmbedding.setEmbedding(textAndEmbedding.getEmbedding());
            fileEmbeddingMapper.insert(fileEmbedding);
        }

        log.info("添加embedding完毕，name：{}", genName(fileContent.getHashId(), fileContent.getLang()));

        try {
            userService.deductBalance(userId, GeneralConstant.PARSE_FILE_VALUE);
        } catch (MyServiceException e) {

        }
    }

    private String genName(String hash, String lang) {
        return "file-" + hash + "&" + lang;
    }
}

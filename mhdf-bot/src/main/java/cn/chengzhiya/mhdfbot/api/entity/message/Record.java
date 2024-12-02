package cn.chengzhiya.mhdfbot.api.entity.message;

import com.alibaba.fastjson2.JSONObject;
import lombok.Getter;

@Getter
public final class Record {
    private final String file;
    private final String url;
    private final String fileSize;
    private final String fileName;
    private final String base64;

    public Record(JSONObject data) {
        this.file = data.getString("file");
        this.url = data.getString("url");
        this.fileSize = data.getString("file_size");
        this.fileName = data.getString("file_name");
        this.base64 = data.getString("base64");
    }
}

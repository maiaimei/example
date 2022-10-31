package cn.maiaimei.example.service;

import cn.maiaimei.example.mapper.FileMapper;
import cn.maiaimei.example.model.FileModel;
import cn.maiaimei.framework.beans.PaginationResult;
import cn.maiaimei.framework.mybatisplus.util.PaginationUtils;
import cn.maiaimei.framework.util.SFID;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FileService extends ServiceImpl<FileMapper, FileModel> {
    @Transactional(rollbackFor = Exception.class)
    public void batchCreate(List<FileModel> fileModels) {
        for (FileModel fileModel : fileModels) {
            fileModel.setId(SFID.nextId());
            fileModel.setGmtCreate(LocalDateTime.now());
        }
        saveBatch(fileModels);
    }

    public void create(FileModel fileModel) {
        fileModel.setId(SFID.nextId());
        fileModel.setGmtCreate(LocalDateTime.now());
        save(fileModel);
    }

    public PaginationResult pageQuery(Integer current,
                                      Integer size) {
        Page page = page(new Page(current, size));
        return PaginationUtils.build(page, FileModel.class);
    }
}

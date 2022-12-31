package com.lyra.system.impl;

import com.lyra.entity.FileDefinition;
import com.lyra.system.NativeFileSystem;

import java.io.File;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NativeFileSystemImpl implements NativeFileSystem {
    /**
     * 根节点
     */
    private File root;





    public NativeFileSystemImpl(File root) {
        if (root.isDirectory()) {
            this.root = root;
            if (!root.exists()) {
                if (root.mkdirs()) {
                    System.out.println("目录不存在，创建目录成功.");
                } else {
                    System.out.println("目录不存在，创建目录失败.");
                }
            }
            getDirFilesDefinitionList(root);
        } else {
            throw new RuntimeException("该文件不是目录格式，传入的路径必须为目录格式。");
        }
    }


    /**
     * 获取文件信息列表
     *
     * @return 文件列表
     */
    public List<FileDefinition> getDirFilesDefinitionList(File dir) {
        List<FileDefinition> list = new ArrayList<>();

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            FileDefinition fileDefinition = new FileDefinition();
            fileDefinition.setDir(file.isDirectory());
            fileDefinition.setFileName(file.getName());
            fileDefinition.setModifyTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(file.lastModified()), ZoneId.systemDefault()));
            fileDefinition.setSize(file.length());

            list.add(fileDefinition);
        }

        return list;
    }


    public File getRoot() {
        return root;
    }

    public void setRoot(File root) {
        this.root = root;
    }


}

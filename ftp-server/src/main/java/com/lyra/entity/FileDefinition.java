package com.lyra.entity;

import java.time.LocalDateTime;

public class FileDefinition {
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件大小
     */
    private Long size;
    /**
     * 是否为目录
     */
    private Boolean isDir;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;

    @Override
    public String toString() {
        return "FileDefinition{" +
                "fileName='" + fileName + '\'' +
                ", size=" + size +
                ", isDir=" + isDir +
                ", modifyTime=" + modifyTime +
                '}';
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Boolean getDir() {
        return isDir;
    }

    public void setDir(Boolean dir) {
        isDir = dir;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }
}

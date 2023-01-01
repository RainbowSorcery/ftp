package com.lyra.system;

import com.lyra.entity.FileDefinition;

import java.io.File;
import java.util.List;

public interface NativeFileSystem {
    List<FileDefinition> getDirFilesDefinitionList(File dir);

    String getRootPath();

    File getRoot();

    void setRoot(File root);

    File getCurrentFile();
    String getCurrentFilePath();

    void setCurrentFile(File currentFile);
}

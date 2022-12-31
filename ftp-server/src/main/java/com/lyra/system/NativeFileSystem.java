package com.lyra.system;

import com.lyra.entity.FileDefinition;

import java.io.File;
import java.util.List;

public interface NativeFileSystem {
    List<FileDefinition> getDirFilesDefinitionList(File dir);
}

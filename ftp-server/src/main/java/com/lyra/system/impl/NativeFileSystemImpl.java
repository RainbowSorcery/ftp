package com.lyra.system.impl;

import com.lyra.system.NativeFileSystem;

import java.io.File;
import java.util.Map;

public class NativeFileSystemImpl implements NativeFileSystem {
    private File root;

    public NativeFileSystemImpl(File root) {
        this.root = root;
    }

}

package com.hannikkala.poc;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;

public class TestUtil {

    public static String getFileContents(String file) throws IOException {
        Resource res = new ClassPathResource(file);
        return FileUtils.readFileToString(res.getFile());
    }
}
package com.jwhh;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        String[] data = {
                "Line 1",
                "Line 2 2",
                "Line 3 3 3",
                "Line 4 4 4 4",
                "Line 5 5 5 5 5"
        };
        try (FileSystem zipFs = openZip(Paths.get("myData.zip"))){
            copyToZip(zipFs);
        } catch (Exception e) {
            System.out.println(e.getClass()+"-"+e.getMessage());
        }
    }

    private static FileSystem openZip(Path zipPath) throws IOException, URISyntaxException {
        Map<String, String> providerProps = new HashMap<>();
        providerProps.put("create", "true");        //specify properties
        URI zipUri= new URI("jar:file", zipPath.toUri().getPath(), null);  //get the URI
        FileSystem zipFs = FileSystems.newFileSystem(zipUri, providerProps);          //create the zip file

        return zipFs;
    }

    private static void copyToZip(FileSystem zipFs) throws IOException {
        Path sourceFile=Paths.get("file1.txt");
        Path destFile = zipFs.getPath("/fileCopied.txt");
        Files.copy(sourceFile,destFile, StandardCopyOption.REPLACE_EXISTING);
    }

    //write in old way
    private static void writeToFileInZip1(FileSystem zipFs, String[] data) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(zipFs.getPath("/newFile1.txt"))) {
            for (String d : data) {
                writer.write(d);
                writer.newLine();
            }
        }
    }

    //write in new way
    private static void writeToFileInZip2(FileSystem zipFs, String[] data) throws IOException {
        Files.write(zipFs.getPath("/newFile2.txt"), Arrays.asList(data),Charset.defaultCharset(),
                StandardOpenOption.CREATE);
    }

}
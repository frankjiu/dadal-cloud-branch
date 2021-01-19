package com.function.ftp.old;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class ZipUtil {
    private static Logger logger = LoggerFactory.getLogger(ZipUtil.class);

    public static void unZipFile(File file, String path, String targetFileName) throws IOException {
        log.info("开始解压{}",file.getName());
        unZip(file, path, targetFileName);
    }

    public static void unZipFile(File file, String path) throws IOException {
        unZip(file, path, null);
    }


    /**
     * 解压缩zipFile
     *
     * @param file      要解压的zip文件对象
     * @param outputDir 要解压到某个指定的目录下
     * @throws IOException
     */
    private static void unZip(File file, String outputDir, String targetFileName) throws IOException {
        org.apache.tools.zip.ZipFile zipFile = null;
        try {
            zipFile = new org.apache.tools.zip.ZipFile(file.getPath());
            // 创建输出目录
            createDirectory(outputDir, null);
            Enumeration<?> enums = zipFile.getEntries();
            while (enums.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) enums.nextElement();
                // 是目录
                if (entry.isDirectory()) {
                    if (targetFileName != null) {
                        createDirectory(outputDir, targetFileName);
                    } else {
                        createDirectory(outputDir, entry.getName());
                    }

                    // 是文件
                } else {
                    File tmpFile = new File(outputDir + "/" + (targetFileName == null ? entry.getName() : targetFileName));
                    // 创建输出目录
                    createDirectory(tmpFile.getParent() + "/", null);
                    InputStream in = zipFile.getInputStream((org.apache.tools.zip.ZipEntry) entry);
                    OutputStream out = new FileOutputStream(tmpFile);
                    IOUtils.copy(in, out);
                }
            }
        } catch (IOException e) {
            throw new IOException("解压缩文件出现异常", e);
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
            } catch (IOException ex) {
                throw new IOException("关闭zipFile出现异常");
            }
        }
    }

    public static void unGzipFile(String sourceFile, String targetFile) throws IOException {
        // 建立gzip压缩文件输入流
        FileInputStream fin = new FileInputStream(sourceFile);
        // 建立gzip解压工作流
        GZIPInputStream gzin = new GZIPInputStream(fin);
        // 建立解压文件输出流
        FileOutputStream fout = new FileOutputStream(targetFile);

        int num;
        byte[] buf = new byte[1024];

        while ((num = gzin.read(buf, 0, buf.length)) != -1) {
            fout.write(buf, 0, num);
        }

        gzin.close();
        fout.close();
        fin.close();
    }


    /**
     * 压缩成tar文件
     *
     * @param entry 文件路径
     * @return 压缩后的文件路径
     * @throws IOException
     */
    public static String archive(String entry) throws IOException {
        File file = new File(entry);
        TarArchiveOutputStream tos = new TarArchiveOutputStream(new FileOutputStream(file.getAbsolutePath() + ".tar"));
        String base = file.getName();
        archiveHandle(tos, file, base);
        tos.close();
        return file.getAbsolutePath() + ".tar";
    }


    /**
     * 压缩成tar文件
     *
     * @param entry          文件路径
     * @param targetFileName 目标文件名
     * @return 压缩后的文件路径
     * @throws IOException
     */
    public static String archive(String entry, String targetFileName) throws IOException {
        File file = new File(entry);
        return archive(entry, file.getParent(), targetFileName);
    }

    /**
     * 压缩成tar文件
     *
     * @param entry          文件路径
     * @param targetFileName 目标文件名
     * @return 压缩后的文件路径
     * @throws IOException
     */
    public static String archive(String entry, String targetPath, String targetFileName) throws IOException {
        File file = new File(entry);
        String targetFileFullName = targetPath + (targetPath.endsWith("/") ? "" : "/") + targetFileName + ".tar";
        TarArchiveOutputStream tos = new TarArchiveOutputStream(new FileOutputStream(targetFileFullName));
        String base = file.getName();
        archiveHandle(tos, file, base);
        tos.close();
        return targetFileFullName;
    }

    /**
     * 将tar文件压缩成tar.gz
     *
     * @param path 文件路径
     * @return 压缩后的文件路径
     * @throws IOException
     */
    public static String compressArchive(String path) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path));
        GzipCompressorOutputStream gcos = new GzipCompressorOutputStream(new BufferedOutputStream(new FileOutputStream(path + ".gz")));
        byte[] buffer = new byte[1024];
        int read = -1;
        while ((read = bis.read(buffer)) != -1) {
            gcos.write(buffer, 0, read);
        }
        gcos.close();
        bis.close();
        return path + ".gz";
    }

    public static String gz(File file, String targetFileName, boolean deleteOriginalFile) throws IOException {
        String archiveName = archive(file.getAbsolutePath(), targetFileName);
        String gzFileName = compressArchive(archiveName);
        new File(archiveName).delete();
        if (deleteOriginalFile) {
            file.delete();
        }
        return gzFileName;
    }

    private static void archiveHandle(TarArchiveOutputStream tos, File fi, String basePath) throws IOException {
        TarArchiveEntry tEntry = new TarArchiveEntry(basePath + File.separator + fi.getName());
        tEntry.setSize(fi.length());

        tos.putArchiveEntry(tEntry);

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fi));

        byte[] buffer = new byte[1024];
        int read = -1;
        while ((read = bis.read(buffer)) != -1) {
            tos.write(buffer, 0, read);
        }
        bis.close();
        tos.closeArchiveEntry();
    }

    /**
     * 构建目录
     *
     * @param outputDir
     * @param subDir
     */
    public static void createDirectory(String outputDir, String subDir) {
        File file = new File(outputDir);
        // 子目录不为空
        if (!(subDir == null || "".equals(subDir.trim()))) {
            file = new File(outputDir + "/" + subDir);
        }
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    private static final int BUFFER_SIZE = 2 * 1024;


    /**
     * 将文件压缩成ZIP
     *
     * @param srcDir 压缩文件夹路径
     * @param desDir 目标路径（若为空则直接压缩到同级目录下）
     * @return String 压缩后的文件路径
     * @throws RuntimeException 压缩失败会抛出运行时异常
     */
    public static String zip(String srcDir, String desDir)
            throws RuntimeException, FileNotFoundException {
        FileOutputStream out;
        if (StringUtils.isNotBlank(desDir)) {
            if (desDir.contains(srcDir)) {
                throw new RuntimeException("目标路径不能在源文件路径下面");
            }
            desDir = desDir.endsWith(".zip") ? desDir : desDir + ".zip";
        } else {
            String filename = FilenameUtils.getBaseName(srcDir);
            String fullPath = FilenameUtils.getFullPath(srcDir);
            desDir = fullPath + filename + ".zip";
        }
        out = new FileOutputStream(new File(desDir));
        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            File sourceFile = new File(srcDir);
            compress(sourceFile, zos, sourceFile.getName());
            long end = System.currentTimeMillis();
            logger.info("压缩完成，耗时：{} ms", (end - start));
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return desDir;
    }

    /**
     * 递归压缩方法
     *
     * @param sourceFile 源文件
     * @param zos        zip输出流
     * @param name       压缩后的名称
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name
    ) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 空文件夹的处理
                zos.putNextEntry(new ZipEntry(name + "/"));
                // 没有文件，不需要文件的copy
                zos.closeEntry();

            } else {
                for (File file : listFiles) {
                    // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                    // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                    compress(file, zos, name + "/" + file.getName());
                }
            }
        }
    }

    public static String decompress(String pathname) {
        //check parameters
        File filed = new File(pathname);
        System.out.println("解压前的文件名称："+pathname);
        //String destDir = "";
        if(!filed.exists() || !filed.getName().endsWith(".tar.gz")) {
            // Log.D("not found compress file or not is a '.tar.gz' file.");
            return "";
        }
//        File destDirFile = new File(destDir);
//        if(!destDirFile.exists()) {
//            destDirFile.mkdirs();
//        }

        //begin decompress
        String fileName =null;
        FileInputStream fis ;
        ArchiveInputStream in = null;
        BufferedInputStream bufferedInputStream = null ;
        BufferedOutputStream bufferedOutputStream = null ;
        try {
            fis = new FileInputStream(filed);
            GZIPInputStream is = new GZIPInputStream(new BufferedInputStream(fis));
            in = new ArchiveStreamFactory().createArchiveInputStream("tar", is);

            String outFileName = getFileName(pathname);
            bufferedInputStream = new BufferedInputStream(in);
            TarArchiveEntry entry = (TarArchiveEntry)in.getNextEntry();
            while(entry != null) {
                String name = entry.getName();
                String[] names = name.split("/");
                //System.out.println(names);
                fileName = outFileName;
                //System.out.println(fileName);
                for(int i = 0; i < names.length; i++) {
                    String str = names[i];
                    fileName = fileName + File.separator + str;

                }
                if(name.endsWith("/")) {
                    mkFolder(fileName);
                } else {
                    File file = mkFile(fileName);
                    System.out.println(fileName);
                    bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
                    int b = 0;
                    while((b = bufferedInputStream.read()) != -1) {
                        bufferedOutputStream.write(b);
                    }
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                }
                entry = (TarArchiveEntry)in.getNextEntry();
            }

            System.out.println("解压后的文件名称："+ fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ArchiveException e) {
            e.printStackTrace();
        } finally {
            if(bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return fileName;
    }

    private static void mkFolder(String fileName) {
        File f = new File(fileName);
        if(!f.exists()) {
            f.mkdirs();
        }
    }

    private static File mkFile(String fileName) {
        File f = new File(fileName);
        try {
            f.getParentFile().mkdirs();
            f.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public static String getFileName(String f) {
        String fname = "";
        int i = f.lastIndexOf('.');

        if (i > 0 &&  i < f.length() - 1) {
            fname = f.substring(0,i-4);
        }
        return fname;
    }

    public static void main(String[] args) {
        String str = decompress("/Users/macx/Downloads/CZ_BP_2020-02_Batch_1_2020-02-18.tar.gz");
        System.out.println(str);
    }


}

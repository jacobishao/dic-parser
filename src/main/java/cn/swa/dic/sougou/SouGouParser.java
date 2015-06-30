package cn.swa.dic.sougou;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by swa on 15/6/30.
 * http://www.tuicool.com/articles/EF7v6nB
 */
public class SouGouParser {
    public static void main(String[] args) throws IOException{
        String inputPath = "/Users/swa/Downloads/中国高等院校（大学）大全【官方推荐】.scel";
        String outputPath="/Users/swa/data/dic/test.dic";
        Boolean isAppend = true;
        SouGouParser.sogou(inputPath,outputPath,isAppend);
    }
    private static void sogou(String inputPath, String outputPath, boolean isAppend) throws IOException {

        File file = new File(inputPath);
        if (!isAppend) {
            if (Files.exists(Paths.get(outputPath), LinkOption.values())) {
                System.out.println("存储此文件已经删除");
                Files.deleteIfExists(Paths.get(outputPath));

            }
        }
        RandomAccessFile raf = new RandomAccessFile(outputPath, "rw");

        int count = 0;
        SougouScelMdel model = new SougouScelReader().read(file);
        Map<String, List<String>> words = model.getWordMap(); //词<拼音,词>
        Set<Map.Entry<String, List<String>>> set = words.entrySet();
        Iterator<Map.Entry<String, List<String>>> iter = set.iterator();
        while (iter.hasNext()) {
            Map.Entry<String, List<String>> entry = iter.next();
            List<String> list = entry.getValue();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                String word = list.get(i);

                //System.out.println(word);
                raf.seek(raf.getFilePointer());
                raf.write((word + "\n").getBytes());//写入txt文件
                count++;


            }
        }
        raf.close();
        System.out.println("生成txt成功！,总计写入: " + count + " 条数据！");
    }
}

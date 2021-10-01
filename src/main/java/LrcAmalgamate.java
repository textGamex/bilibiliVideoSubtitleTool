import com.lrc.LrcTimeTag;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;

/**
 * @author 留恋千年
 * @since 2021-9-20
 * @version 1.1.0
 */
public class LrcAmalgamate
{
    private static final Scanner in = new Scanner(System.in);
    public static void main(String[] args)
    {
        var rawData = new ArrayList<String>(32);
        var timeArray = new ArrayList<LrcTimeTag>(32);

        System.out.print("输入要被插入的文件名(头): ");
        var writerFile = in.nextLine() + ".lrc";

        String fileName = getInputFilePath();

        String addNumberString = getInputAddTime();
        //TODO:懒
        addNumberString = addNumberString.split("[\\[\\]]")[1];

        try (Scanner lyricFile = new Scanner(Path.of(fileName), StandardCharsets.UTF_8))
        {
            while (lyricFile.hasNextLine())
            {
                var string = lyricFile.nextLine();
                rawData.add(string);
                timeArray.add(LrcTimeTag.of(string));
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        var addTime = new LrcTimeTag(addNumberString);

        try (FileWriter newFile = new FileWriter(writerFile, true))
        {
            for (int count = 0, max = timeArray.size(); count < max; ++count)
            {
                LrcTimeTag newTimeTag = timeArray.get(count);
                newTimeTag = newTimeTag.plusTime(addTime);
                var newText = newTimeTag.replaceTimeTag(rawData.get(count));
                newFile.write(newText + System.lineSeparator());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static String getInputFilePath()
    {
        boolean invalid = true;
        String fileName;
        do
        {
            System.out.print("输入要插入的文件的路径(尾): ");
            fileName = in.nextLine() + ".lrc";
            if (Path.of(fileName).toFile().exists())
            {
                invalid = false;
            }
            else
            {
                System.out.println("文件不存在, 请重新输入");
            }
        } while (invalid);
        return fileName;
    }

    private static String getInputAddTime()
    {
        String addNumberString;
        boolean invalid = true;
        do
        {
            System.out.print("要添加的时间: ");
            addNumberString = in.nextLine();
            if (LrcTimeTag.isValidData(addNumberString))
            {
                invalid = false;
            }
            else
            {
                System.out.println("时间标签格式错误!");
            }
        } while (invalid);
        return addNumberString;
    }
}

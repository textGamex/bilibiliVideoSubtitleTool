import com.lrc.LanguageCode;
import com.lrc.tools.LrcTranslate;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * @author 留恋千年
 */
public class Translate
{
    public static void main(String[] args)
    {
        var in = new Scanner(System.in);
        var rawData = new ArrayList<String>(32);

        System.out.println("请输入歌词文件");
        var fileName = in.nextLine();

        var start = Instant.now();

        try (var file = new Scanner(Path.of(fileName), StandardCharsets.UTF_8))
        {
            while (file.hasNextLine())
            {
                var s = file.nextLine();
                rawData.add(s);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        for (var code : LanguageCode.values())
        {
            var data = LrcTranslate.getTranslateLyricArray(rawData, code.getLanguageCode());
            try (var out = new PrintWriter(code.getLanguageCode() + ".lrc", StandardCharsets.UTF_8))
            {
                for (var s : data)
                {
                    out.println(s);
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        var end = Instant.now();
        var time = Duration.between(start, end);
        System.out.println(time.getSeconds() + "s" + time.toMillisPart() + "ms");
    }
}

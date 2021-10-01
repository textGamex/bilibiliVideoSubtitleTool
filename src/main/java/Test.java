import com.lrc.LrcTimeTag;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

public class Test
{
    public static void main(String[] args)
    {
        var a = new LrcTimeTag(3,47,0);
        System.out.println(a.plusTime(new LrcTimeTag(4,44,00)).toTimeTag());
    }
}

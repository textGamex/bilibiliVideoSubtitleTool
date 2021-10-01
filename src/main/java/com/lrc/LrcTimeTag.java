package com.lrc;

import java.time.DateTimeException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static java.util.Objects.requireNonNull;

/**
 * @author 留恋千年
 * @version 1.1.3
 * @since 2021-9-21
 */
public class LrcTimeTag
{
    /**分钟最大值*/
    public static final byte MINUTE_MAX = 59;
    /**秒最大值*/
    public static final byte SECOND_MAX = 59;
    /**毫秒最大值*/
    public static final short MILLISECOND_MAX = 999;
    private static final Pattern PATTERN = Pattern.compile("\\[\\d{2}:\\d{2}\\.\\d{3}]");

    /**分钟*/
    private final byte minute;
    /**秒*/
    private final byte second;
    /**毫秒*/
    private final short millisecond;

    /**
     * @param minute 分钟
     * @param second 秒
     * @param millisecond 毫秒
     */
    public LrcTimeTag(int minute, int second, int millisecond)
    {
        if (minute > MINUTE_MAX || second > SECOND_MAX || millisecond > MILLISECOND_MAX)
        {
            throw new DateTimeException("分钟:" + minute + ", 秒:" + second + ", 毫秒:" + millisecond);
        }
        this.minute = (byte) minute;
        this.second = (byte) second;
        this.millisecond = (short) millisecond;
    }

    /**
     * 传入格式为{@code mm:ss.SSS}的字符串.
     *
     * @param timeTagString 要转化的字符串
     */
    public LrcTimeTag(String timeTagString)
    {
        var timeArray = requireNonNull(timeTagString).split("[:.]");
        try
        {
            minute = Byte.parseByte(timeArray[0]);
            second = Byte.parseByte(timeArray[1]);
            millisecond = Short.parseShort(timeArray[2]);
        }
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("数值异常," + e.getMessage());
        }

        if (minute > MINUTE_MAX || second > SECOND_MAX || millisecond > MILLISECOND_MAX)
        {
            throw new DateTimeException("分钟:" + minute + ", 秒:" + second + ", 毫秒:" + millisecond);
        }
    }

    /**
     * @param timeTagString 歌词文本
     * @return 一个由歌词文本的时间标签解析来的 {@code com.lrc.LrcTimeTag}对象
     */
    public static LrcTimeTag of(String timeTagString)
    {
        return new LrcTimeTag(separateTimeTag(requireNonNull(timeTagString)));
    }

    public LrcTimeTag plusMinute(int minute)
    {
        int newMinute = this.minute + minute;
        if (newMinute > MINUTE_MAX)
        {
            throw new DateTimeException("分钟相加后超出最大值, 值为:" + newMinute);
        }
        return new LrcTimeTag(newMinute, second, millisecond);
    }

    public LrcTimeTag plusSecond(int second)
    {
        int newSecond = this.second + second;
        int addMinute = 0;

        while (newSecond > SECOND_MAX)
        {
            newSecond -= 60;
            ++addMinute;
        }
        int newMinute = minute + addMinute;
        if (newMinute > MINUTE_MAX)
        {
            throw new DateTimeException("分钟相加后超出最大值, 值为:" + newMinute);
        }
        return new LrcTimeTag(newMinute, newSecond, millisecond);
    }

    /**
     * @param millisecond 增加的毫秒
     * @return 基于 {@code this}加上毫秒的副本
     */
    public LrcTimeTag plusMillisecond(int millisecond)
    {
        int newMillisecond = this.millisecond + millisecond;
        int newMinute = minute;
        int newSecond = second;

        while (newMillisecond > MILLISECOND_MAX)
        {
            newMillisecond -= 1000;
            ++newSecond;
            if (newSecond > SECOND_MAX)
            {
                ++newMinute;
                newSecond -= 60;
            }
        }
        if (newMinute > MINUTE_MAX)
        {
            throw new DateTimeException(newMinute + " > " + MINUTE_MAX);
        }
        return new LrcTimeTag(newMinute, newSecond, newMillisecond);
    }

    public LrcTimeTag plusTime(LrcTimeTag time)
    {
        int totalMillisecond = toMillisecond(time.minute, time.second, time.millisecond);
        return plusMillisecond(totalMillisecond);
    }

    /**
     * 返回总和的毫秒.
     *
     * @param minute 分
     * @param second 秒
     * @param millisecond 毫秒
     * @return 返回总和的毫秒
     */
    private static int toMillisecond(int minute, int second, int millisecond)
    {
        return (minute*60 + second)*1000 + millisecond;
    }

    /**
     * 把传入的LRC文件中的一行文本中的时间标签替换成{@code this}的时间标签.
     *
     * @param text LRC文件中的一行文本
     * @return 被替换后的字符串
     */
    public String replaceTimeTag(String text)
    {
        Matcher matcher = PATTERN.matcher(requireNonNull(text));
        return matcher.replaceAll(toTimeTag());
    }

    /**
     * @param text 歌词文本
     * @return 歌词的时间标签, 不带[]
     * @throws NullPointerException 如果{@code text}为null
     */
    public static String separateTimeTag(String text)
    {
        return requireNonNull(text).split("[\\[\\]]")[1];
    }

    /**
     * 检测字符串形式的时间标签是否有效, 有效格式为 {@code [mm:ss.SSS]}.
     *
     * @param string 要检测的时间标签字符串
     * @return
     */
    public static boolean isValidData(String string)
    {
        if (!PATTERN.matcher(string).find())
        {
            return false;
        }
        String[] array = separateTimeTag(string).split("[:.]");
        try
        {
            Byte.parseByte(array[0]);
            Byte.parseByte(array[1]);
            if (Short.parseShort(array[2]) > MILLISECOND_MAX)
            {
                return false;
            }
        }
        catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }

    /**
     * 返回时间标签.
     *
     * @return 返回时间标签
     */
    public String toTimeTag()
    {
        return String.format("[%02d:%02d.%03d]", minute, second, millisecond);
    }

    public int getMinute()
    {
        return minute;
    }

    public int getSecond()
    {
        return second;
    }

    public int getMillisecond()
    {
        return millisecond;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        LrcTimeTag that = (LrcTimeTag) o;
        return minute == that.minute && second == that.second && millisecond == that.millisecond;
    }

    @Override
    public int hashCode()
    {
        int result = minute;
        result = 31 * result + (int) second;
        result = 31 * result + (int) millisecond;
        return result;
    }

    @Override
    public String toString()
    {
        return "com.lrc.LrcTimeTag{" +
                "minute=" + minute +
                ", second=" + second +
                ", millisecond=" + millisecond +
                '}';
    }
}

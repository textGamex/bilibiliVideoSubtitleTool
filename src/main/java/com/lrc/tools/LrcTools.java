package com.lrc.tools;

import static java.util.Objects.requireNonNull;

/**
 * @author 留恋千年
 * @since 2021-9-30
 * @version 1.2.1
 */
public final class LrcTools
{
    private LrcTools()
    {
        throw new AssertionError();
    }

    /**
     * 得到LRC文件一行中的歌词.
     *
     * @param text LRC文件中的一行文本
     * @return LRC文件一行中的歌词
     *
     */
    public static String getLyricString(String text)
    {
        var array = text.split("]");
        //把带时间标签但却缺少歌词的行给过滤
        if (array.length < 2)
        {
            return "";
        }
        return array[1].strip();
    }

    /**
     * 把传入的LRC文件中的一行歌词替换成{@code replacedText}.
     *
     * @param sourceText LRC文件中的一行文本
     * @param replacedText 要替换原来文本的字符串
     * @return 被替换后的字符串
     */
    public static String replaceLyric(String sourceText, String replacedText)
    {
        //TODO:可以用正则表达式改进, 但我不会
        var s = sourceText.split("]")[0];
        return s + ']' + replacedText;
    }
}

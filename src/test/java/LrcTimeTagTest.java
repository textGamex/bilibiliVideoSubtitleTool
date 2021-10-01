import com.lrc.LrcTimeTag;

import static org.junit.jupiter.api.Assertions.*;

class LrcTimeTagTest
{
    LrcTimeTag obj = new LrcTimeTag(1, 1, 1);
    @org.junit.jupiter.api.Test
    void testReplaceTimeTag()
    {
        assertEquals("[01:01.001]模拟歌词", obj.replaceTimeTag("[11:11.111]模拟歌词"));
    }

    @org.junit.jupiter.api.Test
    void textSeparateTimeTag()
    {
        assertEquals("11:22.333", LrcTimeTag.separateTimeTag("[11:22.333]模拟歌词"));
    }

    @org.junit.jupiter.api.Test
    void testToTimeTag()
    {
        assertEquals("[01:01.001]", obj.toTimeTag());
    }
}
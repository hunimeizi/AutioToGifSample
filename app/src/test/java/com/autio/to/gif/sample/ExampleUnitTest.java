package com.autio.to.gif.sample;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    public static void main(String[] args) {
        String data = "10月至次年12月";
        String data1 = "10月至次年12月";
        String substring1 = data.substring(0, data.indexOf("月", 1));
        String startData = substring1.contains("次年") ? substring1.substring(2) : substring1;

        String substring2 = data.substring(data.contains("年") ? data.indexOf("年") + 1 : data.indexOf("至") + 1);
        String substring3 = substring2.substring(0, substring2.length() - 1);
        String endData = substring3.contains("次年") ? substring3.substring(substring3.indexOf("次年") + 2) : substring3;
        System.out.println(startData);

        System.out.println(endData);
    }

}
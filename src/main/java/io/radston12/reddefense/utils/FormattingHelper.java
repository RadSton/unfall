package io.radston12.reddefense.utils;

public class FormattingHelper {

    public static String divideToPrecision(int divide, float divisor, int precision) {
        float realDivisor = divisor / (precision * 10);
        float result = (int) Math.floor((float) divide / realDivisor);
        float floatingPointResult = result / (precision * 10);

        String string = Float.toString(floatingPointResult);

        if(string.endsWith("0"))
            string = string.split("\\.")[0];

        return string;
    }
}

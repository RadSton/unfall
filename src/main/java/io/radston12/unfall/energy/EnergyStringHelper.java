package io.radston12.unfall.energy;

import io.radston12.unfall.utils.FormattingHelper;

public class EnergyStringHelper {

    public static String getSize(int energy) {
        switch (Integer.toString(energy).split("").length) {
            case 1:
            case 2:
            case 3:
                return energy + " FE";
            case 4:
            case 5:
            case 6:
                return FormattingHelper.divideToPrecision(energy, 1_000F, 2) + " kFE";
            case 7:
            case 8:
            case 9:
                return FormattingHelper.divideToPrecision(energy, 1_000_000F, 2) + " MFE";
            case 10:
            case 11:
            case 12:
                return FormattingHelper.divideToPrecision(energy, 1_000_000_000F, 2) + " GFE";
            case 13:
            case 14:
            case 15:
                return FormattingHelper.divideToPrecision(energy, 1_000_000_000_000F, 2) + " TFE";
            default:
                return FormattingHelper.divideToPrecision(energy, 1_000_000_000_000_000F, 2) + " PFE";
        }
    }

    public static String generateEnergyHoverText(int stored, int max) {
        return getSize(stored) + "/" + getSize(max);
    }
}

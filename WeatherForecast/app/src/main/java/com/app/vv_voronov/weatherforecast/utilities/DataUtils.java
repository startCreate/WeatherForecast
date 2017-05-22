package com.app.vv_voronov.weatherforecast.utilities;

import android.content.Context;
import com.app.vv_voronov.weatherforecast.R;
import com.app.vv_voronov.weatherforecast.data.WeatherPreference;

import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public final class DataUtils {


    public static final long DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1);

    public static long getNormalizedUtcDateForToday() {

        long utcNowMillis = System.currentTimeMillis();
        TimeZone currentTimeZone = TimeZone.getDefault();
        long gmtOffsetMillis = currentTimeZone.getOffset(utcNowMillis);
        long timeSinceEpochLocalTimeMillis = utcNowMillis + gmtOffsetMillis;
        long daysSinceEpochLocal = TimeUnit.MILLISECONDS.toDays(timeSinceEpochLocalTimeMillis);
        long normalizedUtcMidnightMillis = TimeUnit.DAYS.toMillis(daysSinceEpochLocal);

        return normalizedUtcMidnightMillis;
    }

    private static long elapsedDaysSinceEpoch(long utcDate) {
        return TimeUnit.MILLISECONDS.toDays(utcDate);
    }

    public static long normalizeDate(long date) {
        long daysSinceEpoch = elapsedDaysSinceEpoch(date);
        long millisFromEpochToTodayAtMidnightUtc = daysSinceEpoch * DAY_IN_MILLIS;
        return millisFromEpochToTodayAtMidnightUtc;
    }

    public static boolean isDateNormalized(long millisSinceEpoch) {
        boolean isDateNormalized = false;
        if (millisSinceEpoch % DAY_IN_MILLIS == 0) {
            isDateNormalized = true;
        }

        return isDateNormalized;
    }

    private static long getLocalMidnightFromNormalizedUtcDate(long normalizedUtcDate) {
        TimeZone timeZone = TimeZone.getDefault();
        long gmtOffset = timeZone.getOffset(normalizedUtcDate);
        long localMidnightMillis = normalizedUtcDate - gmtOffset;
        return localMidnightMillis;
    }


    public static String getFriendlyDateString(Context context, long normalizedUtcMidnight, boolean showFullDate) {

       long localDate = getLocalMidnightFromNormalizedUtcDate(normalizedUtcMidnight);

       long daysFromEpochToProvidedDate = elapsedDaysSinceEpoch(localDate);


        long daysFromEpochToToday = elapsedDaysSinceEpoch(System.currentTimeMillis());

        if (daysFromEpochToProvidedDate == daysFromEpochToToday || showFullDate) {

            String dayName = getDayName(context, localDate);
            String readableDate = getReadableDateString(context, localDate);
            if (daysFromEpochToProvidedDate - daysFromEpochToToday < 2) {

                String localizedDayName = new SimpleDateFormat("EEEE").format(localDate);
                return readableDate.replace(localizedDayName, dayName);
            } else {
                return readableDate;
            }
        } else if (daysFromEpochToProvidedDate < daysFromEpochToToday + 7) {
             return getDayName(context, localDate);
        } else {
            int flags = android.text.format.DateUtils.FORMAT_SHOW_DATE
                    | android.text.format.DateUtils.FORMAT_NO_YEAR
                    | android.text.format.DateUtils.FORMAT_ABBREV_ALL
                    | android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY;

            return android.text.format.DateUtils.formatDateTime(context, localDate, flags);
        }
    }


    private static String getReadableDateString(Context context, long timeInMillis) {
        int flags = android.text.format.DateUtils.FORMAT_SHOW_DATE
                | android.text.format.DateUtils.FORMAT_NO_YEAR
                | android.text.format.DateUtils.FORMAT_SHOW_WEEKDAY;

        return android.text.format.DateUtils.formatDateTime(context, timeInMillis, flags);
    }


    private static String getDayName(Context context, long dateInMillis) {
           SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
           return dayFormat.format(dateInMillis);
           }

    public static String getStringForWeatherCondition(Context context, int weatherId) {
        int stringId;
        if (weatherId >= 200 && weatherId <= 232) {
            stringId = R.string.condition_2xx;
        } else if (weatherId >= 300 && weatherId <= 321) {
            stringId = R.string.condition_3xx;}
            else if ((weatherId >= 502 && weatherId <= 504)||weatherId==531)
            stringId = R.string.condition_502;

        else if (weatherId >= 520 && weatherId <= 500)
            stringId = R.string.condition_500;
        else if (weatherId >= 620 && weatherId <=622)
            stringId = R.string.condition_602;
        else if (weatherId >= 802 && weatherId <=804)
            stringId = R.string.condition_804;

         else switch (weatherId) {
                case 500:
                    stringId = R.string.condition_500;
            case 501:
                stringId = R.string.condition_501;
                break;
            case 511:
                stringId = R.string.condition_511;
                break;
            case 600:
                stringId = R.string.condition_600;
                break;
            case 601:
                stringId = R.string.condition_601;
                break;
            case 602:
                stringId = R.string.condition_602;
                break;
            case 611:
                stringId = R.string.condition_611;
                break;
            case 612:
                stringId = R.string.condition_612;
                break;
            case 615:
                stringId = R.string.condition_615;
                break;
            case 616:
                stringId = R.string.condition_616;
                break;

            case 701:
                stringId = R.string.condition_701;
                break;
            case 711:
                stringId = R.string.condition_711;
                break;
            case 721:
                stringId = R.string.condition_721;
                break;
            case 731:
                stringId = R.string.condition_731;
                break;
            case 741:
                stringId = R.string.condition_741;
                break;
            case 751:
                stringId = R.string.condition_751;
                break;
            case 761:
                stringId = R.string.condition_761;
                break;
            case 762:
                stringId = R.string.condition_762;
                break;
            case 771:
                stringId = R.string.condition_771;
                break;
            case 781:
                stringId = R.string.condition_781;
                break;
            case 800:
                stringId = R.string.condition_800;
                break;
            case 801:
                stringId = R.string.condition_801;
                break;
            case 900:
                stringId = R.string.condition_900;
                break;
            case 901:
                stringId = R.string.condition_901;
                break;
            case 902:
                stringId = R.string.condition_902;
                break;
            case 903:
                stringId = R.string.condition_903;
                break;
            case 904:
                stringId = R.string.condition_904;
                break;
            case 905:
                stringId = R.string.condition_905;
                break;
            case 906:
                stringId = R.string.condition_906;
                break;
            case 951:
                stringId = R.string.condition_951;
                break;
            case 952:
                stringId = R.string.condition_952;
                break;
            case 953:
                stringId = R.string.condition_953;
                break;
            case 954:
                stringId = R.string.condition_954;
                break;
            case 955:
                stringId = R.string.condition_955;
                break;
            case 956:
                stringId = R.string.condition_956;
                break;
            case 957:
                stringId = R.string.condition_957;
                break;
            case 958:
                stringId = R.string.condition_958;
                break;
            case 959:
                stringId = R.string.condition_959;
                break;
            case 960:
                stringId = R.string.condition_960;
                break;
            case 961:
                stringId = R.string.condition_961;
                break;
            case 962:
                stringId = R.string.condition_962;
                break;
            default:
                return context.getString(R.string.condition_unknown, weatherId);
        }

        return context.getString(stringId);
    }

    public static String formatHighLows(Context context, double high, double low) {
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String formattedHigh = formatTemperature(context, roundedHigh);
        String formattedLow = formatTemperature(context, roundedLow);

        String highLowStr = formattedHigh + " / " + formattedLow;
        return highLowStr;
    }
    public static String formatTemperature(Context context, double temperature) {
        if (!WeatherPreference.isMetric(context)) {
            temperature = (temperature * 1.8) + 32;
        }

        int temperatureFormatResourceId = R.string.format_temperature;

        return String.format(context.getString(temperatureFormatResourceId), temperature);
    }

    public static String getFormattedWind(Context context, float windSpeed, float degrees) {
        int windFormat = R.string.format_wind_kmh;

        if (!WeatherPreference.isMetric(context)) {
            windFormat = R.string.format_wind_mph;
            windSpeed = .621371192237334f * windSpeed;
        }

        String direction = "Unknown";
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "N";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "NE";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "E";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "SE";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "S";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            direction = "SW";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "W";
        } else if (degrees >= 292.5 && degrees < 337.5) {
            direction = "NW";
        }

        return String.format(context.getString(windFormat), windSpeed, direction);
    }
}
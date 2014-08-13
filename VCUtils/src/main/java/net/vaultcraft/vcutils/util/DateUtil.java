package net.vaultcraft.vcutils.util;

import net.vaultcraft.vcutils.events.TimeUnit;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tacticalsk8er on 7/22/2014.
 */
public class DateUtil {

    private static Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?" + "(?:([0-9]+)\\s*(?:s[a-z]*)?)?", Pattern.CASE_INSENSITIVE);

    public static long parseDateDiff(String time, boolean future) throws Exception
    {
        Matcher m = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;
        while (m.find())
        {
            if (m.group() == null || m.group().isEmpty())
            {
                continue;
            }
            for (int i = 0; i < m.groupCount(); i++)
            {
                if (m.group(i) != null && !m.group(i).isEmpty())
                {
                    found = true;
                    break;
                }
            }
            if (found)
            {
                if (m.group(1) != null && !m.group(1).isEmpty())
                {
                    years = Integer.parseInt(m.group(1));
                }
                if (m.group(2) != null && !m.group(2).isEmpty())
                {
                    months = Integer.parseInt(m.group(2));
                }
                if (m.group(3) != null && !m.group(3).isEmpty())
                {
                    weeks = Integer.parseInt(m.group(3));
                }
                if (m.group(4) != null && !m.group(4).isEmpty())
                {
                    days = Integer.parseInt(m.group(4));
                }
                if (m.group(5) != null && !m.group(5).isEmpty())
                {
                    hours = Integer.parseInt(m.group(5));
                }
                if (m.group(6) != null && !m.group(6).isEmpty())
                {
                    minutes = Integer.parseInt(m.group(6));
                }
                if (m.group(7) != null && !m.group(7).isEmpty())
                {
                    seconds = Integer.parseInt(m.group(7));
                }
                break;
            }
        }
        if (!found)
        {
            throw new Exception("Illegal Date");
        }
        Calendar c = new GregorianCalendar();
        if (years > 0)
        {
            c.add(Calendar.YEAR, years * (future ? 1 : -1));
        }
        if (months > 0)
        {
            c.add(Calendar.MONTH, months * (future ? 1 : -1));
        }
        if (weeks > 0)
        {
            c.add(Calendar.WEEK_OF_YEAR, weeks * (future ? 1 : -1));
        }
        if (days > 0)
        {
            c.add(Calendar.DAY_OF_MONTH, days * (future ? 1 : -1));
        }
        if (hours > 0)
        {
            c.add(Calendar.HOUR_OF_DAY, hours * (future ? 1 : -1));
        }
        if (minutes > 0)
        {
            c.add(Calendar.MINUTE, minutes * (future ? 1 : -1));
        }
        if (seconds > 0)
        {
            c.add(Calendar.SECOND, seconds * (future ? 1 : -1));
        }
        Calendar max = new GregorianCalendar();
        max.add(Calendar.YEAR, 10);
        if (c.after(max))
        {
            return max.getTimeInMillis();
        }
        return c.getTimeInMillis();
    }

    public static String fromTime(TimeUnit unit, double ticks) {
        switch (unit) {
            case HOURS:
                ticks = ticks/(20*3600);
                break;
            case MINUTES:
                ticks = ticks/(20*60);
                break;
            case SECONDS:
                ticks = ticks/(20);
        }

        ticks = ticks*20; //for seconds
        int hours = (int) (ticks / 3600);
        int remainder = (int) (ticks - hours * 3600);
        int mins = remainder / 60;
        remainder = (remainder - mins * 60);
        int secs = remainder;

        String value = ((hours == 0 ? "" : hours+" hr, ") + (mins == 0 ? "" : mins+" min, ") + (secs == 0 ? "" : secs+" sec"));
        return value.endsWith(", ") ? value.substring(0, value.length()-2) : value;
    }
}

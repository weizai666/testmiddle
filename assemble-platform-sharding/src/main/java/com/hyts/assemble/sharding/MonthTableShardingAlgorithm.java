package com.hyts.assemble.sharding;

import com.google.common.collect.Range;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.RangeShardingValue;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;

/**
 * @Author zj
 * @Date 2021/8/16 19:03
 * 根据时间进行分表，上半月一个表，下半月1个表
 */
public class MonthTableShardingAlgorithm implements PreciseShardingAlgorithm<Date>, RangeShardingAlgorithm<Date> {
    @Override
    public String doSharding(Collection<String> tableNames, PreciseShardingValue<Date> preciseShardingValue) {
        for (String table : tableNames) {
            String timeValue = getTimeBetween(preciseShardingValue.getValue());
            if (table.endsWith(String.valueOf(timeValue))) {
                return table;
            }
        }

        return "";
    }

    private String getTimeBetween(Date date){
        String dateValue = "0";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd");
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(date.toInstant(),zoneId);
        String format = localDateTime.format(dtf);
        Integer integer = Integer.valueOf(format);
        if (integer>15){
            dateValue ="1";
        }
        return dateValue;
    }

    @Override
    public Collection<String> doSharding(Collection<String> collection, RangeShardingValue<Date> rangeShardingValue) {
       try {
           Collection<String> result = new LinkedHashSet<>(collection.size());
           Range<Date> valueRange = rangeShardingValue.getValueRange();
           Date lowerDate = valueRange.lowerEndpoint();
           Date upperDate = valueRange.upperEndpoint();
           Integer lowMonthInter = getMonthInter(lowerDate);
           Integer upperMonthInter = getMonthInter(upperDate);
           if (!lowMonthInter.equals(upperMonthInter)){
               Date firstDayDateOfMonth = getFirstDayDateOfMonth(upperDate);
               Date lastDayOfMonth = getLastDayOfMonth(lowerDate);
               Integer low = Integer.valueOf(getTimeInter(lowerDate));
               Integer lowEnd = Integer.valueOf(getTimeInter(lastDayOfMonth));
               for (int i = low; i <= lowEnd; i++) {
                   for (String each : collection) {
                       String dateValue = "0";
                       if (i>15){
                           dateValue = "1";
                       }
                       if (each.endsWith(dateValue)){
                           result.add(each);
                       }
                   }
               }
               Integer upperStart = Integer.valueOf(getTimeInter(firstDayDateOfMonth));
               Integer upper = Integer.valueOf(getTimeInter(upperDate));
               for (int i = upperStart; i <= upper; i++) {
                   for (String each : collection) {
                       String dateValue = "0";
                       if (i>15){
                           dateValue = "1";
                       }
                       if (each.endsWith(dateValue)){
                           result.add(each);
                       }
                   }
               }
               return result;
           }else if (lowMonthInter.equals(upperMonthInter)){
               Integer low = Integer.valueOf(getTimeInter(lowerDate));
               Integer upper = Integer.valueOf(getTimeInter(upperDate));
               for (int i = low; i <= upper; i++) {
                   for (String each : collection) {
                       String dateValue = "0";
                       if (i>15){
                           dateValue = "1";
                       }
                       if (each.endsWith(dateValue)){
                           result.add(each);
                       }
                   }
               }
               return result;
           }

       }catch (Exception e){
           e.printStackTrace();
       }
       return null;

    }


    private Integer getTimeInter(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String format = sdf.format(date);
        Integer integer = Integer.valueOf(format);
        return integer;
    }

    private Integer getMonthInter(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        String format = sdf.format(date);
        Integer integer = Integer.valueOf(format);
        return integer;
    }

    /**
     * 获取所在月开始时间
     * @param date
     * @return
     */
    public static Date getFirstDayDateOfMonth(final Date date) {

        final Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        final int last = cal.getActualMinimum(Calendar.DAY_OF_MONTH);

        cal.set(Calendar.DAY_OF_MONTH, last);

        return cal.getTime();

    }

    /**
     * 获取所在月最后一天
     */
    public static Date getLastDayOfMonth(final Date date) {

        final Calendar cal = Calendar.getInstance();

        cal.setTime(date);

        final int last = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        cal.set(Calendar.DAY_OF_MONTH, last);

        return cal.getTime();

    }
}

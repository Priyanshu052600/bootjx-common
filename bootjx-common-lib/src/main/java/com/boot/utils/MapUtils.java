package com.boot.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;



public class MapUtils {

			
	public  static Map<Object, Long> mapMerge(Map<String, Long> dateRanMap1,Map<Object, Long> dateRanMap2) {
		Map<Object, Long> dateRanMap3 = new HashMap<>(dateRanMap1);
		 dateRanMap2.forEach((key, value) -> dateRanMap3.merge(key, value, (v1, v2) -> v1+v2));
		 return dateRanMap3;
	}
	
	public  static Map<Object, Long> mapMergeV1(Map<Object, Long> dateRanMap1,Map<Object, Long> dateRanMap2) {
		Map<Object, Long> dateRanMap3 = new HashMap<>(dateRanMap1);
		 dateRanMap2.forEach((key, value) -> dateRanMap3.merge(key, value, (v1, v2) -> v1+v2));
		 return dateRanMap3;
	}
	
	
	public static Map<Object, Map<Object, Long>> defaultValue(Map<String, Map<String, Long>> dayWiseCountMap, List<String> channelLst,Map<Object, Long> dateRanMap,String tnt) {
		Map<Object, Map<Object, Long>> dayWiseMap = new HashMap<>();
		for (Map.Entry<String, Map<String, Long>> keyValue : dayWiseCountMap.entrySet()) {
			Map<Object, Long> dateWiseCnt = new HashMap<>();
			String key = keyValue.getKey();
			Map<String, Long> keyMap = dayWiseCountMap.get(key);
			if (ArgUtil.is(keyMap)) {
				dateWiseCnt = mapMerge(keyMap,dateRanMap);
				dayWiseMap.put(key, dateWiseCnt);
			}
		}
		for (String channel : channelLst) {
			String  tnt_channel=tnt + "_" + channel;
			if(!dayWiseMap.containsKey(tnt_channel)) {
				dayWiseMap.put(tnt_channel, dateRanMap);
			}
			
		}
		return dayWiseMap;
		
	}
	
	public static Map<Object, Map<Object, Long>> getHourdefaultValue(Map<String, Map<Object, Long>> hourWiseCountMap, List<String> channelLst,Map<Object, Long> hourRanMap,String tnt) {
		Map<Object, Map<Object, Long>> dayWiseMap = new HashMap<>();
		for (Map.Entry<String, Map<Object, Long>> keyValue : hourWiseCountMap.entrySet()) {
			Map<Object, Long> dateWiseCnt = new HashMap<>();
			String key = keyValue.getKey();
			Map<Object, Long> keyMap = hourWiseCountMap.get(key);
			if (ArgUtil.is(keyMap)) {
				dateWiseCnt = mapMergeV1(keyMap,hourRanMap);
				dayWiseMap.put(key, dateWiseCnt);
			}
		}
		for (String channel : channelLst) {
			String  tnt_channel=tnt + "_" + channel;
			if(!dayWiseMap.containsKey(tnt_channel)) {
				dayWiseMap.put(tnt_channel, hourRanMap);
			}
			
		}
		return dayWiseMap;
		
	}
	

	/** get dates between two dates **/
			public static Map<Object, Long> getDatesRange(long curTiStmp, long lasDayTiStmp) {
				Map<Object, Long> mapDt = new HashMap<>();
				for (long lasDayTiSt = lasDayTiStmp; lasDayTiSt <= curTiStmp; lasDayTiSt += DateUtil.ONEDAY) {
					String ds = DateUtil.foramtTimeStampDateAsString(lasDayTiSt, DateUtil.YYYYMMDD_DATE_FORMAT);
					mapDt.put(ds, new Long(0));
				}
				Map<Object, Long> result = new TreeMap<Object, Long>(mapDt);
				return result;
			}
}

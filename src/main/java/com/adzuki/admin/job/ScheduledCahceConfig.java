package com.adzuki.admin.job;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.adzuki.admin.cache.Cache;
import com.adzuki.admin.common.utils.ApplicationContextUtils;

@Component
public class ScheduledCahceConfig{
	
	private static final Log logger = LogFactory.getLog(ScheduledCahceConfig.class);
	
	@Scheduled(fixedDelayString  = "${schedule.cache.fixedDelay:30000}" , initialDelay = 3000)
	public void reload(){
		logger.info("cache reload...");
		Map<String, Cache> result = ApplicationContextUtils.getApplicationContext().getBeansOfType(Cache.class);
		if(!result.keySet().isEmpty()){
			result.forEach((k,v)->{
				v.reload();
			});
		}
	}
	
		
	
}

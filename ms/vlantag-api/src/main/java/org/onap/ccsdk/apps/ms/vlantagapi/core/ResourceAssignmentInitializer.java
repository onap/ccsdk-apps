/*******************************************************************************
 * Copyright © 2017-2018 AT&T Intellectual Property.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.onap.ccsdk.apps.ms.vlantagapi.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.onap.ccsdk.sli.adaptors.lock.comp.LockHelper;
import org.onap.ccsdk.sli.adaptors.lock.comp.LockHelperImpl;
import org.onap.ccsdk.sli.adaptors.lock.dao.ResourceLockDao;
import org.onap.ccsdk.sli.adaptors.lock.dao.ResourceLockDaoImpl;
import org.onap.ccsdk.sli.adaptors.ra.ResourceAllocator;
import org.onap.ccsdk.sli.adaptors.ra.alloc.DbAllocationRule;
import org.onap.ccsdk.sli.adaptors.ra.comp.AllocationRule;
import org.onap.ccsdk.sli.adaptors.ra.comp.EndPointAllocator;
import org.onap.ccsdk.sli.adaptors.ra.comp.EndPointAllocatorImpl;
import org.onap.ccsdk.sli.adaptors.ra.rule.dao.RangeRuleDao;
import org.onap.ccsdk.sli.adaptors.ra.rule.dao.RangeRuleDaoImpl;
import org.onap.ccsdk.sli.adaptors.ra.rule.dao.ResourceRuleDao;
import org.onap.ccsdk.sli.adaptors.ra.rule.dao.ResourceRuleDaoImpl;
import org.onap.ccsdk.sli.adaptors.rm.comp.ResourceManager;
import org.onap.ccsdk.sli.adaptors.rm.comp.ResourceManagerImpl;
import org.onap.ccsdk.sli.adaptors.rm.dao.ResourceDao;
import org.onap.ccsdk.sli.adaptors.rm.dao.jdbc.AllocationItemJdbcDao;
import org.onap.ccsdk.sli.adaptors.rm.dao.jdbc.AllocationItemJdbcDaoImpl;
import org.onap.ccsdk.sli.adaptors.rm.dao.jdbc.ResourceDaoImpl;
import org.onap.ccsdk.sli.adaptors.rm.dao.jdbc.ResourceJdbcDao;
import org.onap.ccsdk.sli.adaptors.rm.dao.jdbc.ResourceJdbcDaoImpl;
import org.onap.ccsdk.sli.adaptors.rm.dao.jdbc.ResourceLoadJdbcDao;
import org.onap.ccsdk.sli.adaptors.rm.dao.jdbc.ResourceLoadJdbcDaoImpl;
import org.onap.ccsdk.sli.adaptors.util.speed.SpeedUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * ResourceAssignmentInitializer.java Purpose: Represents and Initializes
 * Resource Assignment Spring Module Beans
 * 
 * @author Saurav Paira
 * @version 1.0
 */

@Component
@Configuration
public class ResourceAssignmentInitializer {
	
	@Bean
    public JdbcTemplate getJdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
	
	@Bean
	public ResourceLockDaoImpl getResourceLockDaoImpl(JdbcTemplate jdbcTemplate) {
		ResourceLockDaoImpl resourceLockDaoImpl = new ResourceLockDaoImpl();
		resourceLockDaoImpl.setJdbcTemplate(jdbcTemplate);
		
		return resourceLockDaoImpl;
	}
	
	@Bean
	public LockHelperImpl getLockHelperImpl(ResourceLockDao resourceLockDao) {
		LockHelperImpl lockHelperImpl = new LockHelperImpl();
		lockHelperImpl.setLockWait(5);
		lockHelperImpl.setRetryCount(10);
		lockHelperImpl.setResourceLockDao(resourceLockDao);
		
		return lockHelperImpl;
	}
	
	@Bean
	public ResourceJdbcDaoImpl getResourceJdbcDaoImpl(JdbcTemplate jdbcTemplate) {
		ResourceJdbcDaoImpl resourceJdbcDaoImpl = new ResourceJdbcDaoImpl();
		resourceJdbcDaoImpl.setJdbcTemplate(jdbcTemplate);
		return resourceJdbcDaoImpl;
	}
	
	@Bean
	public AllocationItemJdbcDaoImpl getAllocationItemJdbcDaoImpl(JdbcTemplate jdbcTemplate) {
		AllocationItemJdbcDaoImpl allocationItemJdbcDaoImpl = new AllocationItemJdbcDaoImpl();
		allocationItemJdbcDaoImpl.setJdbcTemplate(jdbcTemplate);
		return allocationItemJdbcDaoImpl;
	}
	
	@Bean
	public ResourceLoadJdbcDaoImpl getResourceLoadJdbcDaoImpl(JdbcTemplate jdbcTemplate) {
		ResourceLoadJdbcDaoImpl resourceLoadJdbcDaoImpl = new ResourceLoadJdbcDaoImpl();
		resourceLoadJdbcDaoImpl.setJdbcTemplate(jdbcTemplate);
		return resourceLoadJdbcDaoImpl;
	}
	
	
	@Bean
	public ResourceDaoImpl getResourceDaoImpl(ResourceJdbcDao resourceJdbcDao, AllocationItemJdbcDao allocationItemJdbcDao, ResourceLoadJdbcDao resourceLoadJdbcDao) {
		ResourceDaoImpl resourceDaoImpl = new ResourceDaoImpl();
		resourceDaoImpl.setAllocationItemJdbcDao(allocationItemJdbcDao);
		resourceDaoImpl.setResourceJdbcDao(resourceJdbcDao);
		resourceDaoImpl.setResourceLoadJdbcDao(resourceLoadJdbcDao);
		
		return resourceDaoImpl;
	}
	
	@Bean
	public ResourceManagerImpl getResourceManagerImpl(LockHelper lockHelper, ResourceDao resourceDao) {
		ResourceManagerImpl resourceManagerImpl = new ResourceManagerImpl();
		resourceManagerImpl.setLockHelper(lockHelper);
		resourceManagerImpl.setLockTimeout(600);
		resourceManagerImpl.setResourceDao(resourceDao);
		
		return resourceManagerImpl;
	}
	
	@Bean
	public ResourceRuleDaoImpl getResourceRuleDaoImpl(JdbcTemplate jdbcTemplate) {
		ResourceRuleDaoImpl resourceRuleDaoImpl = new ResourceRuleDaoImpl();
		resourceRuleDaoImpl.setJdbcTemplate(jdbcTemplate);
				
		return resourceRuleDaoImpl;
	}
	
	@Bean
	public RangeRuleDaoImpl getRangeRuleDaoImpl(JdbcTemplate jdbcTemplate) {
		RangeRuleDaoImpl rangeRuleDaoImpl = new RangeRuleDaoImpl();
		rangeRuleDaoImpl.setJdbcTemplate(jdbcTemplate);
				
		return rangeRuleDaoImpl;
	}
	
	@Bean
	public ResourceAllocator getResourceAllocator(ResourceManager resourceManager, EndPointAllocator endPointAllocator, SpeedUtil speedUtil) {
		ResourceAllocator resourceAllocator = new ResourceAllocator();
		resourceAllocator.setEndPointAllocator(endPointAllocator);
		resourceAllocator.setResourceManager(resourceManager);
		resourceAllocator.setSpeedUtil(speedUtil);
				
		return resourceAllocator;
	}
	
	@Bean
	public SpeedUtil getSpeedUtil() {
		return new SpeedUtil();
	}
	
	@Bean
	public EndPointAllocatorImpl getEndPointAllocatorImpl(ResourceManager resourceManager, DbAllocationRule dbAllocationRule) {
		EndPointAllocatorImpl endPointAllocatorImpl = new EndPointAllocatorImpl();
		
		Map<String, List<AllocationRule>> allocationRuleMap = new HashMap<>();
		allocationRuleMap.put("DEFAULT", Arrays.asList(dbAllocationRule));
				
		endPointAllocatorImpl.setResourceManager(resourceManager);
		endPointAllocatorImpl.setAllocationRuleMap(allocationRuleMap);

		return endPointAllocatorImpl;
	}
	
	@Bean
	public DbAllocationRule getDbAllocationRule(ResourceRuleDao resourceRuleDao, RangeRuleDao rangeRuleDao) {
		DbAllocationRule dbAllocationRule = new DbAllocationRule();
		dbAllocationRule.setRangeRuleDao(rangeRuleDao);
		dbAllocationRule.setResourceRuleDao(resourceRuleDao);
		
		return dbAllocationRule;
	}
}

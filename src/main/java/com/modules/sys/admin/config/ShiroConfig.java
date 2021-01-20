package com.modules.sys.admin.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置
 */
@Configuration
public class ShiroConfig {

	/**
	 * 配置ShiroFilterFactoryBean
	 */
	@Bean
	public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager")DefaultWebSecurityManager securityManager){
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		//设置安全管理器
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		//设置登录页,不设置默认寻找"/login.jsp"页面
		shiroFilterFactoryBean.setLoginUrl("/login");
		//设置跳转页
		shiroFilterFactoryBean.setSuccessUrl("/index");
		//设置提示页
		shiroFilterFactoryBean.setUnauthorizedUrl("/noAuth");

		Map<String, Filter> filters = new HashMap<>();
		filters.put("permitionFilter", new PermitionFilter());
		shiroFilterFactoryBean.setFilters(filters);

		// 使用 LinkedHashMap 配置访问权限, 保证按从上向下的顺序执行
		// Shiro内置过滤器: anon: 无需认证即可访问; authc: 通过认证才能访问;
		// user: 配置记住我或认证通过可以访问; perms: 资源获得授权才能访问; role: 资源获得角色权限才能访问.
		LinkedHashMap<String, String> filterMap = new LinkedHashMap<>();
		// 认证过滤
		filterMap.put("/login", "anon");
		filterMap.put("/js/**", "anon");
		filterMap.put("/css/**", "anon");
		filterMap.put("/img/**", "anon");
		filterMap.put("/images/**", "anon");

		//放行资源
		filterMap.put("/login", "anon");
		filterMap.put("/random/code","anon");
		filterMap.put("/import2", "anon");
		filterMap.put("/images/**", "anon");
		filterMap.put("/userInfo/**", "anon");

		//授权过滤
		filterMap.put("/add", "perms[demo:add]");
		filterMap.put("/update", "perms[demo:update]");

		//其它资源都需要通过authc认证才能进行访问; user表示通过认证和记住我可以访问
		filterMap.put("/**", "authc");

		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
		return shiroFilterFactoryBean;
	}

	/**
	 * 创建DefaultWebSecurityManager
	 */
	@Bean(name="securityManager")
	public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("shiroRealm")ShiroRealm shiroRealm){
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		//设置自定义realm
		securityManager.setRealm(shiroRealm);
		return securityManager;
	}
	
	/**
	 * 自定义Realm
	 */
	@Bean(name="shiroRealm")
	public ShiroRealm getRealm(){
		ShiroRealm shiroRealm = new ShiroRealm();
		return shiroRealm;
	}

	/**
	 * 配置ShiroDialect,支持thymeleaf和shiro标签組合,控制按钮的显示隐藏
	 */
	@Bean
	public ShiroDialect getShiroDialect(){
		return new ShiroDialect();
	}

}

package com.modules.sys.admin.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
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
		//filterMap.put("/logout", "logout"); //这里需要进行缓存清理, 使用后台接口
		filterMap.put("/js/**", "anon");
		filterMap.put("/css/**", "anon");
		filterMap.put("/img/**", "anon");

		//放行资源
		filterMap.put("/login", "anon");
		filterMap.put("/random/code","anon");
		filterMap.put("/import2", "anon");

		//授权过滤
		filterMap.put("/add", "perms[demo:add]");
		filterMap.put("/update", "perms[demo:update]");

		//其它资源都需要通过authc认证才能进行访问; user表示通过认证和记住我可以访问
		filterMap.put("/**", "authc");
		filterMap.put("/**", "user");

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
		securityManager.setRememberMeManager(rememberMeManager());
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

	/**
	 * cookie对象
	 */
	@Bean
	public SimpleCookie rememberMeCookie() {
		//这个参数是cookie的名称, 对应前端的checkbox的name = rememberMe
		SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
		//setcookie的httponly属性如果设为true的话，会增加对xss防护的安全系数。它有以下特点：
		//设为true后, 只能通过http访问, javascript无法访问, 防止xss读取cookie
		simpleCookie.setHttpOnly(true);
		simpleCookie.setPath("/");
		//记住时间30天, 单位秒
		simpleCookie.setMaxAge(2592000);
		return simpleCookie;
	}

	/**
	 * rememberMe cookie管理器
	 */
	@Bean
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		//rememberMe cookie加密的密钥 建议每个项目都不一样 默认AES算法 密钥长度(128 256 512 位)
		cookieRememberMeManager.setCipherKey(Base64.decode("4AvVhmFLUs0KTA3Kprsdag=="));
		return cookieRememberMeManager;
	}

	/**
	 * FormAuthenticationFilter 过滤器 过滤记住我
	 */
	@Bean
	public FormAuthenticationFilter formAuthenticationFilter() {
		FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
		//对应前端的checkbox的name = rememberMe
		formAuthenticationFilter.setRememberMeParam("rememberMe");
		return formAuthenticationFilter;
	}


}

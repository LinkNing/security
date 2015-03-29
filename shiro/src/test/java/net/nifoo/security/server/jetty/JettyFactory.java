package net.nifoo.security.server.jetty;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.EnvConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.NetworkTrafficServerConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

import com.google.common.collect.Lists;

/**
 * 创建Jetty Server的工厂类.
 * 
 * @author Ning Feng
 * 
 */
public class JettyFactory {

	private static final String DEFAULT_WEBAPP_PATH = "src/main/webapp";

	@SuppressWarnings("unused")
	private static final String WINDOWS_WEBDEFAULT_PATH = "jetty/webdefault-windows.xml";

	/**
	 * 创建用于开发运行调试的Jetty Server, 以src/main/webapp为Web应用目录.
	 */
	public static Server createServerInSource(int port, String contextPath) {
		Server server = new Server(port);
		// 设置在JVM退出时关闭Jetty的钩子。
		server.setStopAtShutdown(true);

		ServerConnector connector = new NetworkTrafficServerConnector(server);
		connector.setPort(port);
		// 解决Windows下重复启动Jetty居然不报告端口冲突的问题.
		connector.setReuseAddress(false);
		server.setConnectors(new Connector[] { connector });

		// WebAppContext webContext = new WebAppContext(DEFAULT_WEBAPP_PATH, contextPath);
		WebAppContext webContext = new WebAppContext();
		webContext.setResourceBase(DEFAULT_WEBAPP_PATH);
		webContext.setDescriptor(DEFAULT_WEBAPP_PATH + "/WEB-INF/web.xml");
		webContext.setContextPath(contextPath);
		// 修改webdefault.xml，解决Windows下Jetty Lock住静态文件的问题.
		// webContext.setDefaultsDescriptor(WINDOWS_WEBDEFAULT_PATH);

		webContext.setConfigurations(new Configuration[] { new AnnotationConfiguration(), //
				new WebXmlConfiguration(), //
				new WebInfConfiguration(), //
				new PlusConfiguration(), //
				new MetaInfConfiguration(),//
				new FragmentConfiguration(), //
				new EnvConfiguration() });

		webContext.setParentLoaderPriority(true);

		server.setHandler(webContext);

		return server;
	}

	/**
	 * 设置除jstl-*.jar外其他含tld文件的jar包的名称. jar名称不需要版本号，如sitemesh, shiro-web
	 */
	public static void setTldJarNames(Server server, String... jarNames) {
		WebAppContext context = (WebAppContext) server.getHandler();
		List<String> jarNameExprssions = Lists.newArrayList(".*/classes/.*", ".*/jstl-[^/]*\\.jar$",
				".*/.*taglibs[^/]*\\.jar$");
		for (String jarName : jarNames) {
			jarNameExprssions.add(".*/" + jarName + "-[^/]*\\.jar$");
		}

		context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
				StringUtils.join(jarNameExprssions, '|'));

	}

	/**
	 * 快速重新启动application，重载target/classes与target/test-classes.
	 */
	public static void reloadContext(Server server) throws Exception {
		WebAppContext context = (WebAppContext) server.getHandler();

		System.out.println("[INFO] Application reloading");
		context.stop();

		WebAppClassLoader classLoader = new WebAppClassLoader(context);
		classLoader.addClassPath("target/classes");
		classLoader.addClassPath("target/test-classes");
		context.setClassLoader(classLoader);

		context.start();

		System.out.println("[INFO] Application reloaded");
	}
}

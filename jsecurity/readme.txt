一. 在Tomcat环境时：
1. 要修改catalina.policy， 将JSP中要用的包和类的权限放开，例如：
    permission java.lang.RuntimePermission "accessClassInPackage.org.apache.catalina.realm";
    permission java.lang.RuntimePermission "accessClassInPackage.org.apache.catalina.users";
    
2. MemoryUserDatabase
   在 server.xml 里配置 MemoryUserDatabaseFactory 和  UserDatabaseRealm，然后打开 /admin/memoryUser.jsp
   
3. LDAP
    在 server.xml 里配置 JNDIRealm，然后打开 /admin/principal.jsp
    
二. jaas
添加JVM参数:
-Djava.security.manager 
-Djava.security.policy==${project_loc:ldap}\target\classes\policy.txt  
-Djava.security.auth.login.config==${project_loc:ldap}\target\classes\jaas.config  
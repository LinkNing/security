package net.nifoo.security.web;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.nifoo.security.service.UserService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;

public class SysUserFilter extends PathMatchingFilter {

    @Autowired
    private UserService userService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        String username = (String)SecurityUtils.getSubject().getPrincipal();
        request.setAttribute("CURRENT_USER", userService.findByUsername(username));
        return true;
    }
}

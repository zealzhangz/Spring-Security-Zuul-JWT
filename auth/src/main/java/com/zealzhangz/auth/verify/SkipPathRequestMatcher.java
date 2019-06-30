package com.zealzhangz.auth.verify;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Created by https://zhangaoo.com.<br/>
 * @version Version: 0.0.1
 * @date DateTime: 2019/06/28 19:22:00<br/>
 */
public class SkipPathRequestMatcher implements RequestMatcher {
    /**
     * 多个url匹配器，只要有一个匹配就返回true
     * 这里用来排除不需要匹配的URL
     */
    private OrRequestMatcher matchers;
    /**
     * 用来匹配需要处理的URL，同上，多个匹配器的情况下只要有一个匹配就返回true
     */
    private OrRequestMatcher processingMatcher;

    public SkipPathRequestMatcher(List<String> pathsToSkip, List<String> processingPath) {
        Assert.notNull(pathsToSkip);
        List<RequestMatcher> m = pathsToSkip.stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
        List<RequestMatcher> processUrlList = processingPath.stream().map(path -> new AntPathRequestMatcher(path)).collect(Collectors.toList());
        matchers = new OrRequestMatcher(m);
        processingMatcher = new OrRequestMatcher(processUrlList);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if (matchers.matches(request)) {
            return false;
        }
        return processingMatcher.matches(request) ? true : false;
    }
}

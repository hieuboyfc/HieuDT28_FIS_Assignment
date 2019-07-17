package hieuboy.developer.restcontrollers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginRestController {

    @GetMapping(value = "/login")
    public ModelAndView login(@RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "permission", required = false) String permission,
                              @RequestParam(value = "logout", required = false) String logout,
                              HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        String targetUrl = getRememberMeTargetUrlFromSession(request);
        if (error != null)
            model.addObject("error", "Tên đăng nhập hoặc mật khẩu không đúng");

        if (permission != null)
            model.addObject("permission", "Tài khoản không có quyền truy cập vào hệ thống");

        if (logout != null)
            model.addObject("msg", "Bạn đã đăng xuất thành công");

        if (StringUtils.hasText(targetUrl))
            model.addObject("targetUrl", targetUrl);

        model.setViewName("login/login");
        return model;
    }

    @GetMapping(value = "/logout")
    public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    /**
     * get targetURL from session
     */
    private String getRememberMeTargetUrlFromSession(HttpServletRequest request) {
        String targetUrl = "/admin/home";
        HttpSession session = request.getSession(false);
        if (session != null) {
            targetUrl = session.getAttribute("targetUrl") == null ? "/admin/home" : session.getAttribute("href").toString();
        }
        return targetUrl;
    }
}

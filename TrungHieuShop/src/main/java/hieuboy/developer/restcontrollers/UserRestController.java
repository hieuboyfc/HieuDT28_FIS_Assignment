package hieuboy.developer.restcontrollers;

import hieuboy.developer.entities.User;
import hieuboy.developer.models.*;
import hieuboy.developer.repositories.UserRepository;
import hieuboy.developer.services.IRoleService;
import hieuboy.developer.services.IUserService;
import hieuboy.developer.utils.AuthenticationHelper;
import hieuboy.developer.utils.Value;
import hieuboy.developer.utils.ValueAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.List;

@RestController
public class UserRestController {

    private static final Logger log4j = LoggerFactory.getLogger(UserRestController.class);

    private IUserService userService;

    private IRoleService roleService;

    private UserRepository userRepository;

    public UserRestController(IUserService userService, IRoleService roleService, UserRepository userRepository) {
        this.userService = userService;
        this.roleService = roleService;
        this.userRepository = userRepository;
    }

    /*================================ QUẢN LÝ NGƯỜI DÙNG ================================*/

    @GetMapping(value = ValueAPI.USER_ADMIN, produces = "application/json")
    public ModelAndView getListUser() {
        ModelAndView mav = new ModelAndView("admin/views/front_end/user/user");
        try {
            UserModel userModel = new UserModel();
            userModel.setPageIndex(0);
            userModel.setPageSize(Value.PAGE_SIZE);
            userModel.setDesending(1);
            userModel.setColumn("id");

            List<RoleModel> lstRole = roleService.lstRole();
            mav.addObject("lstRole", lstRole);

            AjaxResultPagingModel pagingModel = userService.getListUserModel(userModel);
            mav.addObject("lstUser", pagingModel);
            mav.addObject("active", "user");
            return mav;
        } catch (Exception e) {
            log4j.error("CAN NOT GET DATA: " + e.getMessage());
            return mav;
        }
    }

    @GetMapping(value = ValueAPI.USER_API + ValueAPI.LOAD_DATA, produces = "application/json")
    public ModelAndView loadUserData(@ModelAttribute UserModel userModel) {
        ModelAndView mav = new ModelAndView("admin/views/front_end/user/user_load_data");
        try {
            if (userModel.getColumn().equals(""))
                userModel.setColumn("id");
            userModel.setPageIndex(userModel.getPageIndex() - 1);
            userModel.setPageSize(userModel.getPageSize());
            userModel.setDesending(userModel.getDesending());
            AjaxResultPagingModel pagingModel = userService.getListUserModel(userModel);
            mav.addObject("lstUser", pagingModel);
            return mav;
        } catch (Exception e) {
            log4j.error("CAN NOT LOAD DATA: " + e.getMessage());
            return mav;
        }
    }

    @PostMapping(value = ValueAPI.USER_API + ValueAPI.SAVE_DATA, produces = "application/json")
    public AjaxResult saveUser(@ModelAttribute UserModel userModel) {
        AjaxResult result = new AjaxResult();
        try {
            boolean checkSave = userService.saveUser(userModel);
            result.setResult(checkSave);
            return result;
        } catch (Exception e) {
            result.setResult(false);
            log4j.error("CAN NOT INSERT USER: " + e.getMessage());
            return result;
        }
    }

    @PutMapping(value = ValueAPI.USER_API + ValueAPI.UPDATE_DATA, produces = "application/json")
    public AjaxResult updateUser(@ModelAttribute UserModel userModel) {
        AjaxResult result = new AjaxResult();
        try {
            Integer code = userService.updateUser(userModel);
            result.setCode(code);
            result.setResult(true);
            return result;
        } catch (Exception e) {
            result.setResult(false);
            log4j.error(e.getMessage());
            return result;
        }
    }

    @PostMapping(value = ValueAPI.USER_API_CHECK_USERNAME, produces = "application/json")
    public boolean checkUsername(String username) {
        try {
            return userService.checkUsername(username);
        } catch (Exception e) {
            log4j.error("CAN NOT FIND USERNAME: " + e.getMessage());
            return false;
        }
    }

    @PostMapping(value = ValueAPI.USER_API_CHECK_EMAIL, produces = "application/json")
    public boolean checkEmail(String email) {
        try {
            return userService.checkEmail(email);
        } catch (Exception e) {
            log4j.error("CAN NOT FIND EMAIL: " + e.getMessage());
            return false;
        }
    }

    @GetMapping(value = ValueAPI.USER_API + ValueAPI.GET_DETAIL_DATA, produces = "application/json")
    public AjaxResultModel<UserModel> getDetailUser(@PathVariable Integer id) {
        AjaxResultModel<UserModel> result = new AjaxResultModel<>();
        try {
            UserModel userModel = userService.getDetailUser(id);
            result.setResultData(userModel);
            result.setResult(true);
            return result;
        } catch (Exception e) {
            result.setResult(false);
            log4j.error("CAN NOT GET USER DETAIL: " + e.getMessage());
            return result;
        }
    }

    @PutMapping(value = ValueAPI.USER_API + ValueAPI.LOCK_DATA, produces = "application/json")
    public boolean lockUser(@RequestParam List<Integer> ids) {
        try {
            return userService.lockByIds(ids);
        } catch (Exception e) {
            log4j.error("CAN NOT LOCK USER: " + e.getMessage());
            return false;
        }
    }

    @PutMapping(value = ValueAPI.USER_API + ValueAPI.UNLOCK_DATA, produces = "application/json")
    public boolean unlockUser(@RequestParam List<Integer> ids) {
        try {
            return userService.unlockByIds(ids);
        } catch (Exception e) {
            log4j.error("CAN NOT UNLOCK USER: " + e.getMessage());
            return false;
        }
    }

    @PutMapping(value = ValueAPI.USER_API + ValueAPI.LOCK_OR_UNLOCK_DATA, produces = "application/json")
    public boolean lockOrUnlockUser(@RequestParam List<Integer> ids) {
        try {
            return userService.lockOrUnlockByIds(ids);
        } catch (Exception e) {
            log4j.error("CAN NOT LOCK OR UNLOCK USER: " + e.getMessage());
            return false;
        }
    }

    @PutMapping(value = ValueAPI.USER_API_RESET_PASSWORD, produces = "application/json")
    public boolean resetPasswordUser(@RequestParam List<Integer> ids) {
        try {
            return userService.resetPasswordUser(ids);
        } catch (Exception e) {
            log4j.error("CAN NOT RESET PASSWORD USER: " + e.getMessage());
            return false;
        }
    }

    /*================================ THÔNG TIN CÁ NHÂN ================================*/

    @GetMapping(value = ValueAPI.USER_PROFILE_ADMIN, produces = "application/json")
    public ModelAndView userProfile() {
        ModelAndView mav = new ModelAndView("admin/views/front_end/user/profile");
        User user = new AuthenticationHelper(userRepository).getUsernameLoginInfo();
        mav.addObject("user", user);
        return mav;
    }

    @PostMapping(value = ValueAPI.USER_PROFILE_API + ValueAPI.UPLOAD_FILE)
    public String uploadFileProfile(@RequestParam("file") MultipartFile file) {
        try {
            User user = new AuthenticationHelper(userRepository).getUsernameLoginInfo();
            String workingDir = System.getProperty("user.dir");
            String absolutePath = workingDir + Value.PATH_UPLOAD;
            File uploadRootDir = new File(absolutePath);
            if (!uploadRootDir.exists()) uploadRootDir.mkdirs();
            String fileName = user.getUsername() + "_" + file.getOriginalFilename();
            File ioFile = new File(absolutePath, fileName);
            file.transferTo(ioFile);
            return Value.IMAGE_URL + fileName;
        } catch (Exception e) {
            log4j.error("CAN NOT UPLOAD FILE IMAGE: " + e.getMessage());
            return "ERROR";
        }
    }

    @PostMapping(value = ValueAPI.USER_PROFILE_CHANGE_INFO_API, produces = "application/json")
    public AjaxResult changeProfile(@ModelAttribute UserModel userModel) {
        AjaxResult result = new AjaxResult();
        try {
            boolean check = userService.changeProfile(userModel);
            result.setResult(check);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT CHANGE PROFILE: " + e.getMessage());
            result.setResult(false);
            return result;
        }
    }

    @PostMapping(value = ValueAPI.USER_PROFILE_CHANGE_PASS_API, produces = "application/json")
    public AjaxResult changePassword(@ModelAttribute UserModel userModel) {
        AjaxResult result = new AjaxResult();
        try {
            boolean check = userService.changePassword(userModel);
            result.setResult(check);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT CHANGE PASSWORD: " + e.getMessage());
            result.setResult(false);
            return result;
        }
    }

    @PostMapping(value = ValueAPI.USER_API_CHECK_PASSWORD, produces = "application/json")
    public boolean checkPassword(String password) {
        try {
            return userService.checkPassword(password);
        } catch (Exception e) {
            log4j.error("CAN NOT CHECK PASSWORD: " + e.getMessage());
            return false;
        }
    }

}

package hieuboy.developer.restcontrollers;

import hieuboy.developer.models.AjaxResult;
import hieuboy.developer.models.AjaxResultModel;
import hieuboy.developer.models.AjaxResultPagingModel;
import hieuboy.developer.models.RoleModel;
import hieuboy.developer.services.IGroupPermissionService;
import hieuboy.developer.services.IRoleService;
import hieuboy.developer.utils.Value;
import hieuboy.developer.utils.ValueAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class RoleRestController {

    private final static Logger log4j = LoggerFactory.getLogger(RoleRestController.class);

    private IRoleService roleService;

    private IGroupPermissionService groupPermissionService;

    public RoleRestController(IRoleService roleService, IGroupPermissionService groupPermissionService) {
        this.roleService = roleService;
        this.groupPermissionService = groupPermissionService;
    }

    @GetMapping(value = ValueAPI.ROLE_ADMIN, produces = "application/json")
    public ModelAndView getListRole() {
        ModelAndView mav = new ModelAndView("admin/views/front_end/role/role");
        try {
            RoleModel roleModel = new RoleModel();
            roleModel.setPageIndex(0);
            roleModel.setPageSize(Value.PAGE_SIZE);
            roleModel.setDesending(1);
            roleModel.setColumn("id");
            AjaxResultPagingModel pagingModel = roleService.getListRoleModel(roleModel);
            mav.addObject("active", "role");
            mav.addObject("lstRole", pagingModel);
            return mav;
        } catch (Exception e) {
            log4j.error("CAN NOT GET DATA: " + e.getMessage());
            return mav;
        }
    }

    @GetMapping(value = ValueAPI.ROLE_API + ValueAPI.LOAD_DATA, produces = "application/json")
    public ModelAndView loadDataRole(@ModelAttribute RoleModel roleModel) {
        ModelAndView mav = new ModelAndView("admin/views/front_end/role/role_load_data");
        try {
            if (roleModel.getColumn().equals("")) roleModel.setColumn("id");
            roleModel.setPageSize(roleModel.getPageSize());
            roleModel.setPageIndex(roleModel.getPageIndex() - 1);
            roleModel.setDesending(roleModel.getDesending());
            AjaxResultPagingModel pagingModel = roleService.getListRoleModel(roleModel);
            mav.addObject("lstRole", pagingModel);
            return mav;
        } catch (Exception e) {
            log4j.error("CAN NOT GET DATA: " + e.getMessage());
            return mav;
        }
    }

    @GetMapping(value = ValueAPI.ROLE_API + ValueAPI.GET_DETAIL_DATA, produces = "application/json")
    public AjaxResultModel<RoleModel> getDetailRole(@PathVariable Integer id) {
        AjaxResultModel<RoleModel> result = new AjaxResultModel<>();
        try {
            RoleModel roleModel = roleService.getDetailRole(id);
            result.setResult(true);
            result.setMessage(groupPermissionService.getPermissionByGroupAndConvertToJSONString(0, roleModel.getLstPermission()));
            result.setResultData(roleModel);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT FIND ID ROLE: " + e.getMessage());
            result.setResult(false);
            return result;
        }
    }

    @PostMapping(value = ValueAPI.ROLE_API + ValueAPI.SAVE_DATA, produces = "application/json")
    public AjaxResult saveDataRole(@ModelAttribute RoleModel roleModel) {
        AjaxResult result = new AjaxResult();
        try {
            boolean check = roleService.saveRole(roleModel);
            result.setResult(check);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT SAVE DATA ROLE: " + e.getMessage());
            return result;
        }
    }

    @PostMapping(value = ValueAPI.ROLE_API + ValueAPI.UPDATE_DATA, produces = "application/json")
    public AjaxResult editRoleWithGroup(Integer roleId, Integer permissionId, boolean value) {
        AjaxResult result = new AjaxResult();
        try {
            boolean check = roleService.updateRolePermission(roleId, permissionId, value);
            setResultValue(result, check);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT UPDATE DATA ROLE: " + e.getMessage());
            result.setResult(false);
            return result;
        }
    }

    @PutMapping(value = ValueAPI.ROLE_API + ValueAPI.EDIT_DATA, produces = "application/json")
    public AjaxResult editNameRole(@ModelAttribute RoleModel roleModel) {
        AjaxResult result = new AjaxResult();
        try {
            int code = roleService.updateNameRole(roleModel);
            result.setCode(code);
            result.setResult(true);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT EDIT NAME ROLE: " + e.getMessage());
            return result;
        }
    }

    @DeleteMapping(value = ValueAPI.ROLE_API + ValueAPI.DELETES_DATA, produces = "application/json")
    public boolean deleteDataRole(@PathVariable List<Integer> ids) {
        try {
            return roleService.deleteRole(ids);
        } catch (Exception e) {
            log4j.error("CAN NOT DELETE DATA ROLE: " + e.getMessage());
            return false;
        }
    }

    @GetMapping(value = ValueAPI.ROLE_API_LOAD_TREE, produces = "application/json")
    public String loadTree() {
        return groupPermissionService.getPermissionByGroupAndConvertToJSONString(0, null);
    }

    /* Help Method */
    private void setResultValue(AjaxResult result, boolean check) {
        if (check) {
            result.setCode(1);
            result.setResult(true);
        } else {
            result.setResult(false);
            result.setCode(0);
        }
    }

}

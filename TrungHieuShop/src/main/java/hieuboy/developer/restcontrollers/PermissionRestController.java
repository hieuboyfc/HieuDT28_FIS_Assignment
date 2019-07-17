package hieuboy.developer.restcontrollers;

import hieuboy.developer.models.AjaxResult;
import hieuboy.developer.models.AjaxResultModel;
import hieuboy.developer.models.GroupPermissionModel;
import hieuboy.developer.models.PermissionModel;
import hieuboy.developer.services.IGroupPermissionService;
import hieuboy.developer.services.IPermissionService;
import hieuboy.developer.utils.ValueAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class PermissionRestController {

    private static final Logger log4j = LoggerFactory.getLogger(PermissionRestController.class);

    private IGroupPermissionService groupPermissionService;

    private IPermissionService permisionService;


    public PermissionRestController(IGroupPermissionService groupPermissionService,
                                    IPermissionService permisionService) {
        this.groupPermissionService = groupPermissionService;
        this.permisionService = permisionService;
    }

    @GetMapping(value = ValueAPI.PERMISSION_ADMIN, produces = "application/json")
    public ModelAndView getListPermision() {
        ModelAndView mav = new ModelAndView("admin/views/front_end/permission/permission");
        try {
            List<GroupPermissionModel> lstGroupPermission = groupPermissionService.getListGroupPermission(0);
            mav.addObject("active", "permission");
            mav.addObject("lstGroupPermission", lstGroupPermission);
            return mav;
        } catch (Exception e) {
            log4j.error("CAN NOT GET DATA: " + e.getMessage());
            return mav;
        }
    }

    /* Phần nhóm quyền - Group Permission */

    @GetMapping(value = ValueAPI.GROUP_PERMISSION_API_LOAD_SELECT, produces = "application/json")
    public ModelAndView loadSelectGroupPermission() {
        ModelAndView mav = new ModelAndView("admin/views/front_end/permission/permission_select_group");
        try {
            List<GroupPermissionModel> lstGroupPermission = groupPermissionService.getListGroupPermission(0);
            mav.addObject("lstSelectGroupPermission", lstGroupPermission);
            return mav;
        } catch (Exception e) {
            log4j.error("CAN NOT LOAD SELECT GROUP PERMISSION: " + e.getMessage());
            return mav;
        }
    }

    @GetMapping(value = ValueAPI.GROUP_PERMISSION_API + ValueAPI.GET_DETAIL_DATA, produces = "application/json")
    public AjaxResultModel<GroupPermissionModel> getDetailGroupPermission(@PathVariable Integer id) {
        AjaxResultModel<GroupPermissionModel> resultModel = new AjaxResultModel<>();
        try {
            GroupPermissionModel groupPermissionModel = groupPermissionService.getDetailGroupPermission(id);
            resultModel.setResultData(groupPermissionModel);
            resultModel.setResult(true);
            return resultModel;
        } catch (Exception e) {
            log4j.error("CAN NOT GET GROUP PERMISSIO DETAIL :" + e.getMessage());
            resultModel.setResult(false);
            return resultModel;
        }
    }


    @PostMapping(value = ValueAPI.GROUP_PERMISSION_API + ValueAPI.SAVE_DATA, produces = "application/json")
    public AjaxResult saveGroupPermission(@ModelAttribute GroupPermissionModel groupPermissionModel) {
        AjaxResult result = new AjaxResult();
        try {
            boolean saveData = groupPermissionService.saveGroupPermission(groupPermissionModel);
            result.setResult(saveData);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT SAVE DATA: " + e.getMessage());
            result.setResult(false);
            return result;
        }
    }

    @PutMapping(value = ValueAPI.GROUP_PERMISSION_API + ValueAPI.UPDATE_DATA, produces = "application/json")
    public AjaxResult updateGroupPermission(@ModelAttribute GroupPermissionModel groupPermissionModel) {
        AjaxResult result = new AjaxResult();
        try {
            boolean updateData = groupPermissionService.updateGroupPermission(groupPermissionModel);
            result.setResult(updateData);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT UPDATE GROUP PERMISSION: " + e.getMessage());
            result.setResult(false);
            return result;
        }
    }

    @DeleteMapping(value = ValueAPI.GROUP_PERMISSION_API + ValueAPI.DELETE_DATA, produces = "application/json")
    public boolean deleteGroupPermission(@PathVariable Integer id) {
        try {
            return groupPermissionService.deleteGroupPermission(id);
        } catch (Exception e) {
            log4j.error("CAN NOT DELETE GROUP PERMISSION: " + e.getMessage());
            return false;
        }
    }


    /* Phân quyền - Permission */

    @GetMapping(value = ValueAPI.PERMISSION_API + ValueAPI.GET_DETAIL_DATA, produces = "application/json")
    public AjaxResultModel<PermissionModel> getDetailPermission(@PathVariable Integer id) {
        AjaxResultModel<PermissionModel> resultModel = new AjaxResultModel<>();
        try {
            PermissionModel permissionModel = permisionService.getDetailPermission(id);
            resultModel.setResultData(permissionModel);
            resultModel.setResult(true);
            return resultModel;
        } catch (Exception e) {
            log4j.error("CAN NOT GET DETAIL PERMISSION: " + e.getMessage());
            resultModel.setResult(false);
            return resultModel;
        }
    }

    @PostMapping(value = ValueAPI.PERMISSION_API + ValueAPI.SAVE_DATA, produces = "application/json")
    public AjaxResult savePermission(@ModelAttribute PermissionModel permissionModel) {
        AjaxResult result = new AjaxResult();
        try {
            boolean saveData = permisionService.savePermission(permissionModel);
            result.setResult(saveData);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT SAVE PERMISSION: " + e.getMessage());
            result.setResult(false);
            return result;
        }
    }

    @PutMapping(value = ValueAPI.PERMISSION_API + ValueAPI.UPDATE_DATA, produces = "application/json")
    public AjaxResult updatePermission(@ModelAttribute PermissionModel permissionModel) {
        AjaxResult result = new AjaxResult();
        try {
            boolean updateData = permisionService.updatePermission(permissionModel);
            result.setResult(updateData);
            return result;
        } catch (Exception e) {
            log4j.error("CAN NOT UPDATE PERMISSION: " + e.getMessage());
            result.setResult(false);
            return result;
        }
    }

    @DeleteMapping(value = ValueAPI.PERMISSION_API + ValueAPI.DELETE_DATA, produces = "application/json")
    public boolean deletePermission(@PathVariable Integer id) {
        try {
            return permisionService.deletePermission(id);
        } catch (Exception e) {
            log4j.error("CAN NOT DELETE PERMISSION: " + e.getMessage());
            return false;
        }
    }

    @PutMapping(value = ValueAPI.PERMISSION_API + ValueAPI.LOCK_OR_UNLOCK_DATA, produces = "application/json")
    public boolean lockOrUnlock(@RequestParam Integer id) {
        try {
            return permisionService.lockOrUnlockPermission(id);
        } catch (Exception e) {
            log4j.error("CAN NOT LOCK OR UNLOCK PERMISSION: " + e.getMessage());
            return false;
        }
    }

    @PostMapping(value = ValueAPI.PERMISSION_API_CHECK_LINK, produces = "application/json")
    public boolean checkLink(String link) {
        try {
            return permisionService.checkLink(link);
        } catch (Exception e) {
            log4j.error("CAN NOT FIND LINK: " + e.getMessage());
            return false;
        }
    }
}

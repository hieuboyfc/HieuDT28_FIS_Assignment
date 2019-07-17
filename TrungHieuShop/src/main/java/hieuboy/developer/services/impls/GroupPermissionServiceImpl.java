package hieuboy.developer.services.impls;

import hieuboy.developer.entities.GroupPermission;
import hieuboy.developer.entities.Permission;
import hieuboy.developer.mappers.GroupPermissionMapper;
import hieuboy.developer.models.GroupPermissionModel;
import hieuboy.developer.repositories.GroupPermissionRepository;
import hieuboy.developer.repositories.PermissionRepository;
import hieuboy.developer.services.IGroupPermissionService;
import hieuboy.developer.utils.HelperService;
import hieuboy.developer.utils.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupPermissionServiceImpl implements IGroupPermissionService {

    private static final Logger log4j = LoggerFactory.getLogger(GroupPermissionServiceImpl.class);

    private GroupPermissionRepository groupPermissionRepository;

    private PermissionRepository permissionRepository;


    public GroupPermissionServiceImpl(GroupPermissionRepository groupPermissionRepository,
                                      PermissionRepository permissionRepository) {
        this.groupPermissionRepository = groupPermissionRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<GroupPermissionModel> getListGroupPermission(Integer parentId) {
        List<GroupPermission> groupPermissions = groupPermissionRepository.findByParentID(parentId);
        List<GroupPermissionModel> groupPermissionModels = new ArrayList<>();
        for (GroupPermission groupPermission : groupPermissions) {
            GroupPermissionModel model = new GroupPermissionModel();
            BeanUtils.copyProperties(groupPermission, model);
            model.setLstPermissions(groupPermission.getPermissions());
            model.setLstGroupPermissionModel(getListGroupPermission(groupPermission.getId()));
            groupPermissionModels.add(model);
        }
        return groupPermissionModels;
    }

    @Override
    public GroupPermissionModel getDetailGroupPermission(Integer id) {
        try {
            GroupPermission groupPermission = groupPermissionRepository.findByGroupPermissionId(id);
            if (groupPermission != null)
                return GroupPermissionMapper.MAPPER.mapEntityToModel(groupPermission);
        } catch (Exception e) {
            log4j.error(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean saveGroupPermission(GroupPermissionModel groupPermissionModel) {
        try {
            GroupPermission groupPermission = GroupPermissionMapper.MAPPER.mapModelToEntity(groupPermissionModel);
            groupPermissionRepository.save(groupPermission);
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateGroupPermission(GroupPermissionModel groupPermissionModel) {
        try {
            GroupPermission groupPermissiop =
                    groupPermissionRepository.findByGroupPermissionId(groupPermissionModel.getId());
            if (groupPermissiop != null) {
                GroupPermissionMapper.MAPPER.updateFromDTOToEntity(groupPermissionModel, groupPermissiop);
                groupPermissionRepository.save(groupPermissiop);
            }
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteGroupPermission(Integer id) {
        try {
            new HelperService(groupPermissionRepository, permissionRepository)
                    .deletePermissionById(Value.GROUP_PERMISSION, id);
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }
    }

    @Override
    public String getPermissionByGroupAndConvertToJSONString(Integer parentNode, List<Integer> permission) {
        List<GroupPermission> groupPermissions = groupPermissionRepository.findByParentID(parentNode);
        StringBuilder grPermissionJson = new StringBuilder();
        grPermissionJson.append("{");
        int i = 1;
        for (GroupPermission groupPermission : groupPermissions) {
            // Convert GroupPermission To GroupPermissionModel
            GroupPermissionModel model = new GroupPermissionModel();
            BeanUtils.copyProperties(groupPermission, model);
            model.setLstPermissions(groupPermission.getPermissions());
            model.setLstGroupPermissionModel(getListGroupPermission(groupPermission.getId()));
            //Gen Json
            grPermissionJson.append(genJsonGroupPermission(model, permission));
            if (i < groupPermissions.size()) grPermissionJson.append(",");
            i++;
        }
        grPermissionJson.append("}");
        return grPermissionJson.toString();
    }

    /*Help method*/
    /*
     * Gen group ra định dạng json
     */
    private String genJsonGroupPermission(GroupPermissionModel groupPermission, List<Integer> permission) {
        StringBuilder groupPermissionJson = new StringBuilder();
        if (groupPermission == null)
            return groupPermission.toString();
        groupPermissionJson.append("\"" + groupPermission.getId() + "\":{\"text\":\"" + groupPermission.getName() + "\",\"type\":\"folder\",\"grid\":\"" + groupPermission.getId() + "\"");
        //lấy danh sách các group con
        if (!(getListGroupPermission(groupPermission.getId()).isEmpty() && groupPermission.getLstPermissions().isEmpty())) {
            groupPermissionJson.append(",\"additionalParameters\":{\"children\":{");
            //kiểm tra có phải đang edit ko và gen danh sách quyền
            groupPermissionJson.append(genJsonPermission(groupPermission.getLstPermissions(), permission));
            int i = 1;
            for (GroupPermissionModel grPermission : getListGroupPermission(groupPermission.getId())) {
                groupPermissionJson.append(genJsonGroupPermission(grPermission, permission));
                if (i < getListGroupPermission(groupPermission.getId()).size()) {
                    groupPermissionJson.append(",");
                }
                i++;
            }
            groupPermissionJson.append("}}}");
        } else groupPermissionJson.append("}");
        return groupPermissionJson.toString();
    }

    /*
     * Gen danh sách quyền ra định dạng json
     * */
    private String genJsonPermission(List<Permission> lstPermission, List<Integer> lstIdPermission) {
        StringBuilder permissionJson = new StringBuilder();
        if (lstPermission.isEmpty())
            return permissionJson.toString();
        int i = 1;
        for (Permission permission : lstPermission) {
            if (lstIdPermission == null) {
                permissionJson.append("\"" + permission.getId() + "\":{\"text\":\"" + permission.getName() + "\",\"type\":\"item\",\"item_selected\":\"" + false + "\",\"perid\":\"" + permission.getId() + "\"}");
            } else {
                permissionJson.append("\"" + permission.getId() + "\":{\"text\":\"" + permission.getName() + "\",\"type\":\"item\",\"item_selected\":\"" + lstIdPermission.stream().anyMatch(num -> permission.getId().equals(num)) + "\",\"perid\":\"" + permission.getId() + "\"}");
            }
            if (i < lstPermission.size()) {
                permissionJson.append(",");
            }
            i++;
        }
        return permissionJson.toString();
    }
}

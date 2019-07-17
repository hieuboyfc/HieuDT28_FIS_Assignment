package hieuboy.developer.services.impls;

import hieuboy.developer.entities.Permission;
import hieuboy.developer.entities.Role;
import hieuboy.developer.mappers.RoleMapper;
import hieuboy.developer.models.AjaxResultPagingModel;
import hieuboy.developer.models.RoleModel;
import hieuboy.developer.repositories.PermissionRepository;
import hieuboy.developer.repositories.RoleRepository;
import hieuboy.developer.services.IRoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements IRoleService {

    private final static Logger log4j = LoggerFactory.getLogger(RoleServiceImpl.class);

    private RoleRepository roleRepository;

    private PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<RoleModel> lstRole() {
        return RoleMapper.MAPPER.mapListEntityToListModel(roleRepository.findAll().stream());
    }

    @Override
    public AjaxResultPagingModel getListRoleModel(RoleModel roleModel) {
        Sort sort = new Sort(roleModel.getDesending() == 1 ? Sort.Direction.DESC : Sort.Direction.ASC,
                roleModel.getColumn());
        Pageable pageable = PageRequest.of(roleModel.getPageIndex(), roleModel.getPageSize(), sort);

        Page<Role> lstRole = roleRepository.getListRoleAndSearch(roleModel.getName(), pageable);
        AjaxResultPagingModel pagingModel = new AjaxResultPagingModel();
        pagingModel.setColumn(roleModel.getColumn());
        pagingModel.setDesending(roleModel.getDesending());
        pagingModel.setPageIndex(roleModel.getPageIndex() + 1);
        pagingModel.setTotalPage(lstRole.getTotalPages());
        pagingModel.setPageSize(roleModel.getPageSize());
        pagingModel.setTotalRecord(lstRole.getTotalElements());
        pagingModel.setResultList(RoleMapper.MAPPER.mapListEntityToListModel(lstRole.getContent().stream()));
        return pagingModel;
    }

    @Override
    public RoleModel getDetailRole(Integer id) {
        try {
            Role role = roleRepository.findByRoleId(id);
            if (role != null)
                return RoleMapper.MAPPER.mapEntityToModel(role);
        } catch (Exception e) {
            log4j.error(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean saveRole(RoleModel roleModel) {
        try {
            Role role = RoleMapper.MAPPER.mapModelToEntity(roleModel);
            saveData(role, roleModel);
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }
    }

    @Override
    public int updateNameRole(RoleModel roleModel) {
        try {
            Role role = roleRepository.findByRoleId(roleModel.getId());
            if (role != null) {
                RoleMapper.MAPPER.updateFromDTOToEntity(roleModel, role);
                role.setName(roleModel.getName());
                roleRepository.save(role);
                return 1;
            }
            return 2;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public boolean updateRolePermission(Integer roleId, Integer permissionId, boolean value) {
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isPresent()) {
            Role roleUpdate = role.get();
            Set<Permission> permissions = roleUpdate.getPermissions();
            List<Integer> lstIdPermission = permissions.stream().map(Permission::getId).collect(Collectors.toList());
            if (value) {
                // Nếu là thêm mới quyền và quyền chưa có trong hệ thống thì bổ sung
                if (lstIdPermission.stream().noneMatch(item -> item == permissionId)) {
                    if (permissionRepository.getOne(permissionId) != null)
                        permissions.add(permissionRepository.getOne(permissionId));
                }
            } else {
                // Nếu quyền đã có trong hệ thống thì xóa bỏ
                if (!lstIdPermission.stream().noneMatch(item -> item != permissionId)) {
                    if (permissionRepository.getOne(permissionId) != null)
                        permissions.remove(permissionRepository.getOne(permissionId));
                }
            }
            //cập nhật lại danh sách quyền và lưu lại thông tin vào hệ thống
            roleUpdate.setPermissions(permissions);
            roleRepository.save(roleUpdate);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteRole(List<Integer> ids) {
        try {
            for (Integer id : ids)
                roleRepository.deleteByRoleId(id);
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }
    }

    private void saveData(Role role, RoleModel roleModel) {
        Set<Permission> permissions = permissionRepository.findByPermissionByIds(roleModel.getLstPermission());
        role.setPermissions(permissions);
        roleRepository.save(role);
    }
}

package hieuboy.developer.services.impls;

import hieuboy.developer.entities.GroupPermission;
import hieuboy.developer.entities.Permission;
import hieuboy.developer.mappers.PermissionMapper;
import hieuboy.developer.models.PermissionModel;
import hieuboy.developer.repositories.GroupPermissionRepository;
import hieuboy.developer.repositories.PermissionRepository;
import hieuboy.developer.services.IPermissionService;
import hieuboy.developer.utils.HelperService;
import hieuboy.developer.utils.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionServiceImpl implements IPermissionService {

    private static final Logger log4j = LoggerFactory.getLogger(PermissionServiceImpl.class);

    private PermissionRepository permissionRepository;

    private GroupPermissionRepository groupPermissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository,
                                 GroupPermissionRepository groupPermissionRepository) {
        this.permissionRepository = permissionRepository;
        this.groupPermissionRepository = groupPermissionRepository;
    }

    @Override
    public PermissionModel getDetailPermission(Integer id) {
        try {
            Permission permission = permissionRepository.findByPermissionId(id);
            if (permission != null)
                return PermissionMapper.MAPPER.mapEntityToModel(permission);
        } catch (Exception e) {
            log4j.error(e.getMessage());
        }
        return null;
    }

    @Override
    @Cacheable(value = "permission.getPermissionByLink", key = "#link")
    public List<PermissionModel> getPermissionByLink(String link) {
        List<PermissionModel> permissionModels = new ArrayList<>();
        List<Permission> permissions = permissionRepository.findByLink(link);
        for (Permission permission : permissions) {
            PermissionModel permissionModel = new PermissionModel();
            BeanUtils.copyProperties(permission, permissionModel);
            permissionModels.add(permissionModel);
        }
        return permissionModels;
    }

    @Override
    @CacheEvict(value = "permission.getPermissionByLink", allEntries = true)
    public boolean savePermission(PermissionModel permissionModel) {
        try {
            Permission permission = PermissionMapper.MAPPER.mapModelToEntity(permissionModel);
            saveData(permission, permissionModel);
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }

    }

    @Override
    @CacheEvict(value = "permission.getPermissionByLink", allEntries = true)
    public boolean updatePermission(PermissionModel permissionModel) {
        try {
            Permission permission = permissionRepository.findByPermissionId(permissionModel.getId());
            GroupPermission groupPermission =
                    groupPermissionRepository.findByGroupPermissionId(permissionModel.getGroupID());
            if (permission != null) {
                PermissionMapper.MAPPER.updateFromDTOToEntity(permissionModel, permission);
                permission.setGroupPermission(groupPermission);
                permissionRepository.save(permission);
            }
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }
    }

    @Override
    @CacheEvict(value = "permission.getPermissionByLink", allEntries = true)
    public boolean deletePermission(Integer id) {
        try {
            new HelperService(groupPermissionRepository, permissionRepository)
                    .deletePermissionById(Value.PERMISSION, id);
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }
    }

    @Override
    @CacheEvict(value = "permission.getPermissionByLink", allEntries = true)
    public boolean lockOrUnlockPermission(Integer id) {
        try {
            Permission permission = permissionRepository.findByPermissionId(id);
            if (permission.isIslock()) {
                permission.setIslock(false);
                permissionRepository.save(permission);
            } else if (!permission.isIslock()) {
                permission.setIslock(true);
                permissionRepository.save(permission);
            }
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }

    }

    @Override
    public boolean checkLink(String link) {
        Permission permission = permissionRepository.findByLinkCheck(link);
        return permission == null;
    }

    private void saveData(Permission permission, PermissionModel permissionModel) {
        GroupPermission groupPermission =
                groupPermissionRepository.findByGroupPermissionId(permissionModel.getGroupID());
        permission.setGroupPermission(groupPermission);
        permission.setIslock(true);
        permissionRepository.save(permission);
    }

}

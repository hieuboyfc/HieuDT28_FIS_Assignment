package hieuboy.developer.utils;

import hieuboy.developer.models.AjaxResultPagingModel;
import hieuboy.developer.repositories.GroupPermissionRepository;
import hieuboy.developer.repositories.PermissionRepository;

import java.util.List;

public class HelperService {

    private GroupPermissionRepository groupPermissionRepository;

    private PermissionRepository permissionRepository;

    public HelperService(GroupPermissionRepository groupPermissionRepository, PermissionRepository permissionRepository) {
        this.groupPermissionRepository = groupPermissionRepository;
        this.permissionRepository = permissionRepository;
    }

    public HelperService() {
    }

    public void deletePermissionById(String name, Integer id) {
        if (name.equals(Value.PERMISSION))
            permissionRepository.deleteById(id);
        else if (name.equals(Value.GROUP_PERMISSION))
            groupPermissionRepository.deleteById(id);
    }

    public AjaxResultPagingModel pagingModel(String column,
                                             Integer desending,
                                             Integer pageIndex,
                                             Integer pageSize,
                                             Integer totalPage,
                                             Long totalRecord,
                                             List<?> resultList) {
        AjaxResultPagingModel pagingModel = new AjaxResultPagingModel();
        pagingModel.setColumn(column);
        pagingModel.setDesending(desending);
        pagingModel.setPageIndex(pageIndex + 1);
        pagingModel.setPageSize(pageSize);
        pagingModel.setTotalPage(totalPage);
        pagingModel.setTotalRecord(totalRecord);
        pagingModel.setResultList(resultList);
        return pagingModel;
    }

}

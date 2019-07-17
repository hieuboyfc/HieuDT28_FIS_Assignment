package hieuboy.developer.services;

import hieuboy.developer.models.AjaxResultPagingModel;
import hieuboy.developer.models.UserModel;

import java.util.List;

public interface IUserService {

    AjaxResultPagingModel getListUserModel(UserModel userModel);

    boolean saveUser(UserModel userModel);

    int updateUser(UserModel userModel);

    boolean lockByIds(List<Integer> ids);

    boolean unlockByIds(List<Integer> ids);

    boolean lockOrUnlockByIds(List<Integer> ids);

    boolean resetPasswordUser(List<Integer> ids);

    boolean checkUsername(String username);

    boolean checkPassword(String password);

    boolean checkEmail(String email);

    UserModel getDetailUser(Integer id);

    boolean changeProfile(UserModel userModel);

    boolean changePassword(UserModel userModel);
}

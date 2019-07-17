package hieuboy.developer.services.impls;

import hieuboy.developer.entities.Role;
import hieuboy.developer.entities.User;
import hieuboy.developer.mappers.UserMapper;
import hieuboy.developer.models.AjaxResultPagingModel;
import hieuboy.developer.models.UserModel;
import hieuboy.developer.repositories.RoleRepository;
import hieuboy.developer.repositories.UserRepository;
import hieuboy.developer.services.IUserService;
import hieuboy.developer.utils.AuthenticationHelper;
import hieuboy.developer.utils.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserServiceImpl implements IUserService {


    private static final Logger log4j = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public AjaxResultPagingModel getListUserModel(UserModel userModel) {
        Sort sort = new Sort(userModel.getDesending() == 1 ? Sort.Direction.DESC : Sort.Direction.ASC,
                userModel.getColumn());
        Pageable pageable = PageRequest.of(userModel.getPageIndex(), userModel.getPageSize(), sort);

        Page<User> lstUser = userRepository.getListUserAndSearch(userModel.getUsername(),
                userModel.getEmail(), userModel.getPhone(), pageable);
        AjaxResultPagingModel pagingModel = new AjaxResultPagingModel();
        pagingModel.setColumn(userModel.getColumn());
        pagingModel.setDesending(userModel.getDesending());
        pagingModel.setPageIndex(userModel.getPageIndex() + 1);
        pagingModel.setPageSize(userModel.getPageSize());
        pagingModel.setTotalPage(lstUser.getTotalPages());
        pagingModel.setTotalRecord(lstUser.getTotalElements());
        pagingModel.setResultList(UserMapper.MAPPER.mapListEntityToListModel(lstUser.getContent().stream()));
        return pagingModel;
    }

    @Override
    public boolean saveUser(UserModel userModel) {
        try {
            Calendar calendar = Calendar.getInstance();
            User user = UserMapper.MAPPER.mapModelToEntity(userModel);
            saveData(user, calendar, userModel);
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }
    }

    @Override
    @Transactional
    public int updateUser(UserModel userModel) {
        try {
            Set<Role> lstRole = roleRepository.findAllRoleById(userModel.getLstRole());
            User user = userRepository.findByUserId(userModel.getId());
            if (user != null) {
                UserMapper.MAPPER.updateFromDTOToEntity(userModel, user);
                user.setRoles(lstRole);
                userRepository.save(user);
                return 1;
            }
            return 3;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return 0;
        }
    }

    @Override
    public boolean lockByIds(List<Integer> ids) {
        try {
            List<User> users = userRepository.findByUserIds(ids);
            for (User user : users) {
                user.setIslock(true);
                userRepository.save(user);
            }
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }

    }

    @Override
    public boolean unlockByIds(List<Integer> ids) {
        try {
            List<User> users = userRepository.findByUserIds(ids);
            for (User user : users) {
                user.setIslock(false);
                userRepository.save(user);
            }
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean lockOrUnlockByIds(List<Integer> ids) {
        try {
            List<User> users = userRepository.findByUserIds(ids);
            for (User user : users) {
                if (user.getIslock()) user.setIslock(false);
                else if (!user.getIslock()) user.setIslock(true);
                userRepository.save(user);
            }
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }

    }

    @Override
    public boolean resetPasswordUser(List<Integer> ids) {
        try {
            List<User> users = userRepository.findByUserIds(ids);
            for (User user : users) {
                user.setPassword(new BCryptPasswordEncoder().encode(Value.RESET_PASSWORD));
                userRepository.save(user);
            }
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }

    }

    @Override
    public boolean checkUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user == null;
    }

    @Override
    public boolean checkPassword(String password) {
        User user = new AuthenticationHelper(userRepository).getUsernameLoginInfo();
        if (user != null) {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            return encoder.matches(password, user.getPassword());
        }
        return false;
    }

    @Override
    public boolean checkEmail(String email) {
        User user = userRepository.findByEmail(email);
        return user == null;
    }

    @Override
    public UserModel getDetailUser(Integer id) {
        try {
            User user = userRepository.findByUserId(id);
            if (user != null)
                return UserMapper.MAPPER.mapEntityToModel(user);
        } catch (Exception e) {
            log4j.error(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean changeProfile(UserModel userModel) {
        try {
            User user = new AuthenticationHelper(userRepository).getUsernameLoginInfo();
            if (user != null) {
                UserMapper.MAPPER.changeProfileFromDTOToEntity(userModel, user);
                if (userModel.getAvatar() != null) user.setAvatar(userModel.getAvatar());
                userRepository.save(user);
            }
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean changePassword(UserModel userModel) {
        try {
            User user = new AuthenticationHelper(userRepository).getUsernameLoginInfo();
            if (user != null) {
                BeanUtils.copyProperties(user, userModel, "password");
                if (userModel.getPassword() != null)
                    user.setPassword(new BCryptPasswordEncoder().encode(userModel.getPassword()));
                user.setPassword(userModel.getPassword());
                userRepository.save(user);
            }
            return true;
        } catch (Exception e) {
            log4j.error(e.getMessage());
            return false;
        }
    }

    private void saveData(User user, Calendar calendar, UserModel userModel) {
        User userLogin = new AuthenticationHelper(userRepository).getUsernameLoginInfo();
        Set<Role> lstRole = roleRepository.findAllRoleById(userModel.getLstRole());
        user.setUsercreated(userLogin.getUsername());
        user.setRoles(lstRole);
        user.setDatecreated(calendar.getTime());
        user.setPassword(userModel.getPassword());
        userRepository.save(user);
    }

}

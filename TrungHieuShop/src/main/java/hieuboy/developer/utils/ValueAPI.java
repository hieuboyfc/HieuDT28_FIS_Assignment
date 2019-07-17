package hieuboy.developer.utils;

public final class ValueAPI {

    /* Value Common */
    public static final String SAVE_DATA = "/save";
    public static final String UPDATE_DATA = "/update";
    public static final String EDIT_DATA = "/edit";
    public static final String DELETE_DATA = "/delete/{id}";
    public static final String DELETES_DATA = "/delete/{ids}";
    public static final String GET_DETAIL_DATA = "/getdetail/{id}";
    public static final String GET_TYPE_PARENTID_DATA = "/load/{type}/{parentID}";
    public static final String LOAD_ID_PARENTID_DATA = "/load/{id}";
    public static final String LOAD_DATA = "/loaddata";
    public static final String LOCK_DATA = "/lockdata";
    public static final String UNLOCK_DATA = "/unlockdata";
    public static final String LOCK_OR_UNLOCK_DATA = "/lockorunlockdata";
    public static final String UPLOAD_FILE = "/upload-file";
    public static final String CHECK_PHONE = "/check-phone";

    /* User */
    public static final String USER_PROFILE_ADMIN = "/admin/user/profile";
    public static final String USER_PROFILE_API = "/api/admin/user/profile";
    public static final String USER_PROFILE_CHANGE_INFO_API = "/api/admin/user/profile/change-info";
    public static final String USER_PROFILE_CHANGE_PASS_API = "/api/admin/user/profile/change-password";
    public static final String USER_ADMIN = "/admin/user";
    public static final String USER_API = "/api/admin/user";
    public static final String USER_API_CHECK_USERNAME = "/api/admin/user/checkusername";
    public static final String USER_API_CHECK_PASSWORD = "/api/admin/user/checkpassword";
    public static final String USER_API_CHECK_EMAIL = "/api/admin/user/checkemail";
    public static final String USER_API_RESET_PASSWORD = "/api/admin/user/resetpassword";

    /* Role */
    public static final String ROLE_ADMIN = "/admin/role";
    public static final String ROLE_API = "/api/admin/role";
    public static final String ROLE_API_LOAD_TREE = "/api/admin/role/loadtree";

    /* Group Permission */
    public static final String PERMISSION_ADMIN = "/admin/permission";
    public static final String PERMISSION_API = "/api/admin/permission";
    public static final String PERMISSION_API_CHECK_LINK = "/api/admin/permission/checklink";
    public static final String GROUP_PERMISSION_API = "/api/admin/permission/group";
    public static final String GROUP_PERMISSION_API_LOAD_SELECT = "/api/admin/permission/group/loadselect";

    /* Category */
    public static final String CATEGORY_ADMIN = "/admin/category";
    public static final String CATEGORY_API = "/api/admin/category";
    public static final String CATEGORY_API_CHECK_NAME = "/api/admin/category/checkname";

    /* Manufacturer */
    public static final String MANUFACTURER_ADMIN = "/admin/manufacturer";
    public static final String MANUFACTURER_API = "/api/admin/manufacturer";
    public static final String MANUFACTURER_API_CHECK_NAME = "/api/admin/manufacturer/checkname";

    /* Manufacturer */
    public static final String PRODUCT_ADMIN = "/admin/product";
    public static final String PRODUCT_API = "/api/admin/product";
    public static final String PRODUCT_API_CHECK_NAME = "/api/admin/product/checkname";

}

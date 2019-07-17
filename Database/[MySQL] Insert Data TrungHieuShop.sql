/*----------------- INSERT DATA -----------------*/

-- INSERT USER - PASS: 123@123aA
INSERT INTO User(username,password,email,phone,fullname,gender,address,birthday,datecreated,usercreated,islock) 
VALUES	('admin','$2a$10$zTUEyFAth3T8atxrbp2vue/5ssHyjMRONjOqJ.i4eYITRsdIKgHB.','admin@gmail.com','0793321897','Administrator',1,'Tiên Lãng - Hải Phòng', '1996-03-21','2018-11-30','hieuboyfc',0);
INSERT INTO User(username,password,email,phone,fullname,gender,address,birthday,datecreated,usercreated,islock) 
VALUES	('hieuboyfc','$2a$10$zTUEyFAth3T8atxrbp2vue/5ssHyjMRONjOqJ.i4eYITRsdIKgHB.','hieuboyfc@gmail.com','0793321897','Hiếu Boy',1,'Tiên Lãng - Hải Phòng', '1996-03-21','2018-11-30','hieuboyfc',0);
INSERT INTO User(username,password,email,phone,fullname,gender,address,birthday,datecreated,usercreated,islock) 
VALUES	('hieudt28','$2a$10$zTUEyFAth3T8atxrbp2vue/5ssHyjMRONjOqJ.i4eYITRsdIKgHB.','hieudt28@fpt.com.vn','0793321897','Đỗ Trung Hiếu',1,'Tiên Lãng - Hải Phòng', '1996-03-21','2018-11-30','hieuboyfc',0);

-- INSERT ROLE
INSERT INTO Role(name) VALUES	('Quản Trị Viên');
INSERT INTO Role(name) VALUES	('Quản Lý');
INSERT INTO Role(name) VALUES	('Khách Hàng');

-- INSERT USER ROLE
INSERT INTO User_Role(user_id,role_id) VALUES	('1','1');
INSERT INTO User_Role(user_id,role_id) VALUES	('2','2');
INSERT INTO User_Role(user_id,role_id) VALUES	('3','3');

-- INSERT GROUP_PERMISSION
INSERT INTO Group_Permission(name,parent_id) VALUES	('Màn hình trang chủ',0); 
INSERT INTO Group_Permission(name,parent_id) VALUES	('Thông tin cá nhân',0); 
INSERT INTO Group_Permission(name,parent_id) VALUES	('Quản lý người dùng',0);
INSERT INTO Group_Permission(name,parent_id) VALUES	('Quản lý tài khoản',3); 
INSERT INTO Group_Permission(name,parent_id) VALUES	('Quản lý nhóm người dùng',3); 
INSERT INTO Group_Permission(name,parent_id) VALUES	('Quản lý nhóm quyền',3); 
INSERT INTO Group_Permission(name,parent_id) VALUES	('Quản lý chức năng',3); 
INSERT INTO Group_Permission(name,parent_id) VALUES	('Quản lý danh mục',0); 
INSERT INTO Group_Permission(name,parent_id) VALUES	('Quản lý loại sản phẩm',8); 
INSERT INTO Group_Permission(name,parent_id) VALUES	('Quản lý hãng sản xuất',8); 
INSERT INTO Group_Permission(name,parent_id) VALUES	('Quản lý sản phẩm',8); 


-- INSERT PERMISSION
/*------------- Xem Màn Hình Trang Chủ -------------*/
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Xem màn hình trang chủ','/admin/home',1,0);
/*------------- Con Của Thông Tin Cá Nhân -------------*/
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Xem thông tin cá nhân','/admin/user/profile',2,0);
/*------------- Con Của Khai Báo Người Dùng -------------*/
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Xem màn hình khai báo người dùng','/admin/user',4,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Thêm mới người dùng','/api/admin/user/save',4,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Cập nhật người dùng','/api/admin/user/update',4,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Khóa người dùng','/api/admin/user/lockdata',4,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Mở khóa người dùng','/api/admin/user/unlockdata',4,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Khóa hoặc mở khóa người dùng','/api/admin/user/lockorunlockdata',4,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Khôi phục mật khẩu người dùng','/api/admin/user/resetpassword',4,0);
/*------------- Con Của Phân Nhóm Người Dùng -------------*/
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Xem màn hình phân nhóm người dùng','/admin/role',5,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Thêm mới nhóm người dùng','/api/admin/role/save',5,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Cập nhật nhóm người dùng','/api/admin/role/update',5,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Xóa nhóm người dùng','/api/admin/role/delete',5,0);
/*------------- Con Của Quản Lý Nhóm Quyền -------------*/
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Xem màn hình quản lý nhóm quyền','/admin/permission',6,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Thêm mới nhóm chức năng','/api/admin/permission/group/save',6,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Cập nhật nhóm chức năng','/api/admin/permission/group/update',6,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Xóa nhóm chức năng','/api/admin/permission/group/delete',6,0);
/*------------- Con Của Quản Lý Nhóm Quyền - Quản Lý Chức Năng -------------*/
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Thêm mới chức năng','/api/admin/permission/save',7,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Cập nhật chức năng','/api/admin/permission/update',7,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Xóa chức năng','/api/admin/permission/delete',7,0);
/*------------- Con Của Quản Lý Loại Sản Phẩm -------------*/
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Xem màn hình quản lý loại sản phẩm','/admin/category',9,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Thêm mới loại sản phẩm','/api/admin/category/save',9,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Cập nhật loại sản phẩm','/api/admin/category/update',9,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Xóa loại loại sản phẩm','/api/admin/category/delete',9,0);

/*------------- Con Của Quản Lý Hãn Sản Xuất -------------*/
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Xem màn hình quản lý hãng sản xuất','/admin/manufacturer',10,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Thêm mới hãng sản xuất','/api/admin/manufacturer/save',10,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Cập nhật hãng sản xuất','/api/admin/manufacturer/update',10,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Xóa hãng sản xuất','/api/admin/manufacturer/delete',10,0);

/*------------- Con Của Quản Lý Sản Phẩm -------------*/
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Xem màn hình quản sản phẩm','/admin/product',11,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Thêm mới sản phẩm','/api/admin/product/save',11,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Cập nhật sản phẩm','/api/admin/product/update',11,0);
INSERT INTO Permission(name,link,group_id,islock) VALUES	('Xóa loại sản phẩm','/api/admin/product/delete',11,0);


-- INSERT ROLE_PERMISSION
/*--- Quyền Quản Trị Viên ---*/
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','1');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','2');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','3');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','4');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','5');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','6');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','7');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','8');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','9');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','10');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','11');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','12');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','13');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','14');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','15');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','16');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','17');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','18');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','19');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','20');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','21');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','22');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','23');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','24');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','25');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','26');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','27');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','28');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','29');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','30');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','31');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('1','32');

/*--- Quyền Quản Lý ---*/
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('2','1');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('2','2');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('2','21');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('2','22');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('2','23');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('2','24');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('2','25');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('2','26');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('2','27');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('2','28');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('2','29');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('2','30');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('2','31');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('2','32');

/*--- Quyền Thành Viên ---*/
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('3','1');
INSERT INTO Role_Permission(role_id,permission_id) VALUES	('3','2');

/*--- Loại Sản Phẩm ---*/
INSERT INTO Category(name,usercreated,datecreated) VALUES	('Máy tính','hieuboyfc','2019-07-17');
INSERT INTO Category(name,usercreated,datecreated) VALUES	('Điện thoại','hieuboyfc','2019-07-17');
INSERT INTO Category(name,usercreated,datecreated) VALUES	('Tivi','hieuboyfc','2019-07-17');
INSERT INTO Category(name,usercreated,datecreated) VALUES	('Đồng hồ','hieuboyfc','2019-07-17');
INSERT INTO Category(name,usercreated,datecreated) VALUES	('Tủ lạnh','hieuboyfc','2019-07-17');

/*--- Hãng Sản Xuất ---*/
INSERT INTO Manufacturer(name,usercreated,datecreated) VALUES	('Apple','hieuboyfc','2019-07-17');
INSERT INTO Manufacturer(name,usercreated,datecreated) VALUES	('Samsung','hieuboyfc','2019-07-17');
INSERT INTO Manufacturer(name,usercreated,datecreated) VALUES	('LG','hieuboyfc','2019-07-17');
INSERT INTO Manufacturer(name,usercreated,datecreated) VALUES	('Oppo','hieuboyfc','2019-07-17');
INSERT INTO Manufacturer(name,usercreated,datecreated) VALUES	('Xiaomi','hieuboyfc','2019-07-17');

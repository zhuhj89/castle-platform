package com.whenling.castle.management.webapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.whenling.castle.integration.ApplicationInitializer;
import com.whenling.castle.main.entity.MenuEntity;
import com.whenling.castle.main.entity.OrganizationEntity;
import com.whenling.castle.main.entity.RoleEntity;
import com.whenling.castle.main.entity.UserEntity;
import com.whenling.castle.main.entity.UserRoleEntity;
import com.whenling.castle.main.entity.OrganizationEntity.OrgType;
import com.whenling.castle.main.init.InitDataTools;
import com.whenling.castle.main.security.PasswordService;
import com.whenling.castle.main.service.OrganizationEntityService;
import com.whenling.castle.main.service.RoleEntityService;
import com.whenling.castle.main.service.UserEntityService;
import com.whenling.castle.main.service.UserRoleEntityService;

@Component
public class CastleApplicationInitializer extends ApplicationInitializer {

	@Autowired
	private UserEntityService userEntityService;

	@Autowired
	private PasswordService passwordService;

	@Autowired
	private OrganizationEntityService organizationEntityService;

	@Autowired
	private RoleEntityService roleEntityService;

	@Autowired
	private UserRoleEntityService userRoleEntityService;

	@Autowired
	private InitDataTools tools;

	@Override
	public void onRootContextRefreshed() {
		if (roleEntityService.count() == 0) {
			RoleEntity role = roleEntityService.newEntity();
			role.setName("管理员");
			role.setCode("admin");
			role.setLocked(true);
			roleEntityService.save(role);

			OrganizationEntity org = organizationEntityService.newEntity();
			org.setName("广州当凌信息科技有限公司");
			org.setCode("whenling_company");
			org.setType(OrgType.company);
			organizationEntityService.save(org);

			UserEntity admin = userEntityService.newEntity();
			passwordService.changeUserPassword(admin, null, "asd123");
			admin.setUsername("admin");
			admin.setMobile("13265323553");
			admin.setEmail("ken@whenling.com");
			admin.setName("孔祥溪");
			admin.setOrg(org);
			userEntityService.save(admin);

			org.setPrimaryLeader(admin);
			organizationEntityService.save(org);

			UserRoleEntity userRole = userRoleEntityService.newEntity();
			userRole.setRole(role);
			userRole.setUser(admin);
			userRoleEntityService.save(userRole);

		}

		if (!tools.existMenu()) {
			int sortNo = 0;

			MenuEntity systemManagement = tools.createMenuByParent("系统管理", "system_management", "fa fa-desktop", null, null, sortNo++, null);
			tools.createMenuByParent("参数设置", "parameter_setting", "fa fa-cogs", "app.view.setting.SettingList", null, sortNo++, systemManagement);
			tools.createMenuByParent("菜单管理", "menu_management", "fa fa-navicon", "app.view.menu.MenuList", null, sortNo++, systemManagement);
			tools.createMenuByParent("区域管理", "area_management", "fa fa-map", "app.view.area.AreaList", null, sortNo++, systemManagement);
			tools.createMenuByParent("字典管理", "dict_management", "fa fa-book", "app.view.dict.DictList", null, sortNo++, systemManagement);
			tools.createMenuByParent("系统日志", "system_log", "fa fa-building", "app.view.log.LogList", null, sortNo++, systemManagement);

			MenuEntity userManagement = tools.createMenuByParent("用户管理", "user_management", "fa fa-users", null, null, sortNo++, null);
			tools.createMenuByParent("会员列表", "member_list", "fa fa-user", "app.view.user.UserList", null, sortNo++, userManagement);
			tools.createMenuByParent("组织机构列表 ", "org_list", "fa fa-object-group", "app.view.org.OrgList", null, sortNo++, userManagement);
			tools.createMenuByParent("角色权限", "role_list", "fa fa-gavel", "app.view.role.RoleList", null, sortNo++, userManagement);
			
			MenuEntity pluginManagement = tools.createMenuByParent("插件管理", "plugin_management", "fa fa-plug", null, null, sortNo++, null);
			tools.createMenuByParent("oauth认证", "plugin_oauth", "fa fa-cogs", "app.view.plugin.OauthList", null, sortNo++, pluginManagement);
			tools.createMenuByParent("支付方式", "plugin_payment", "fa fa-credit-card", "app.view.menu.MenuList", null, sortNo++, pluginManagement);

		}

	}
}

package com.zml.oa.action;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zml.oa.entity.Group;
import com.zml.oa.entity.Message;
import com.zml.oa.pagination.Pagination;
import com.zml.oa.pagination.PaginationThreadUtils;
import com.zml.oa.service.IGroupAndResourceService;
import com.zml.oa.service.IGroupService;
import com.zml.oa.util.BeanUtils;

@Controller
@RequiresPermissions("admin:*")
@RequestMapping("/groupAction")
public class GroupAction {

	@Autowired
	private IGroupService groupService;
	
	@Autowired
	private IGroupAndResourceService grService;
	
	@RequestMapping("/getAll")
	public String toList(Model model) throws Exception{
		List<Group> list = this.groupService.getGroupList();
		model.addAttribute("groupList", list);
		return "group/list_group";
	}
	
	@RequestMapping("/toList_page")
	public String toListPage(Model model) throws Exception{
		List<Group> list = this.groupService.getGroupListPage();
		Pagination pagination = PaginationThreadUtils.get();
		model.addAttribute("page", pagination.getPageStr());
		model.addAttribute("groupList", list);
		return "group/list_group";
	}
	
	@RequestMapping("/chooseGroup_page")
	public String chooseGroup(@RequestParam("key") String key, Model model) throws Exception{
		List<Group> list = this.groupService.getGroupListPage();
		Pagination pagination = PaginationThreadUtils.get();
		model.addAttribute("page", pagination.getPageStr());
		model.addAttribute("groupList", list);
		model.addAttribute("key", key);
		return "group/choose_group";
	}
	
	/**
	 * 加载组信息-easyui
	 * @return
	 */
	@RequestMapping("/getGroupList")
	@ResponseBody
	public List<Group> getList(String sort, String order) throws Exception{
		List<Group> list = this.groupService.getGroupList();
		return list;
	}
	
	/**
	 * 跳转组权限管理页面-easyui
	 * @return
	 */
	@RequestMapping(value ="/permissionAssignment")
	public String permissionAssignment() throws Exception{
		return "permission/permissionAssignment";
	}
	
	/**
	 * 跳转到添加组页面-easyui
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toAdd")
	public String toAdd() throws Exception{
		return "permission/add_group";
	}
	
	/**
	 * 跳转到添加组页面-easyui
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/toUpdate")
	public String toUpdate() throws Exception{
		return "permission/update_group";
	}
	
	/**
	 * 添加Group-easyui
	 * @param group
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/doAdd", method = RequestMethod.POST)
	@ResponseBody
	public Message doAdd(@ModelAttribute Group group) throws Exception{
		this.groupService.doAdd(group);
		return new Message(Boolean.TRUE, "添加成功！");
	}
	
	/**
	 * 更新Group-easyui
	 * @param group
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/doUpdate", method = RequestMethod.POST)
	@ResponseBody
	public Message doUpdate(@ModelAttribute Group group) throws Exception{
		this.groupService.doUpdate(group);
		return new Message(Boolean.TRUE, "更新成功！");
	}
	
	/**
	 * 删除组和组权限-easyui
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete/{id}")
	@ResponseBody
	public Message delete(@PathVariable("id") Integer id) throws Exception{
		if(!BeanUtils.isBlank(id)){
			this.grService.doDelByGroup(id);
			this.groupService.doDelete(new Group(id));
			return new Message(Boolean.TRUE, "删除成功！");
		}else{
			return new Message(Boolean.FALSE, "删除失败！ID为空！");
		}
	}
}

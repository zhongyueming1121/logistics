package com.lgts.entity.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 运营后台用户表
 * </p>
 *
 * @author ymz
 * @since 2019-01-03
 */
@TableName("sys_user")
@ToString
public class SysUserEntity extends Model<SysUserEntity> {

    private static final long serialVersionUID = 1L;

	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 用户名
     */
	private String username;
    /**
     * 密码
     */
	private String password;
    /**
     * 昵称
     */
	private String nickname;
    /**
     * 角色ID
     */
	@TableField("role_id")
	private Integer roleId;
    /**
     * 创建时间
     */
	@TableField("create_time")
	private Date createTime;
    /**
     * 修改时间
     */
	@TableField("update_time")
	private Date updateTime;
    /**
     * 是否有效  1有效  2无效
     */
	@TableField("delete_status")
	private String deleteStatus;


	public Integer getId() {
		return id;
	}

	public SysUserEntity setId(Integer id) {
		this.id = id;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public SysUserEntity setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public SysUserEntity setPassword(String password) {
		this.password = password;
		return this;
	}

	public String getNickname() {
		return nickname;
	}

	public SysUserEntity setNickname(String nickname) {
		this.nickname = nickname;
		return this;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public SysUserEntity setRoleId(Integer roleId) {
		this.roleId = roleId;
		return this;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public SysUserEntity setCreateTime(Date createTime) {
		this.createTime = createTime;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public SysUserEntity setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getDeleteStatus() {
		return deleteStatus;
	}

	public SysUserEntity setDeleteStatus(String deleteStatus) {
		this.deleteStatus = deleteStatus;
		return this;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}

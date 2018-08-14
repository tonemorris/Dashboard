/**
 * 
 */
package com.tivo.ui.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "user_roles")
public class UserRoles {
	
	@Id
	@Column(name = "user_role_id")
	private int userRoleId; 
	
	@Column(name = "userid", updatable = false,  insertable = false)
	private String userid; 
	
	@Column(name = "role")
	private String role;
	
	@OneToOne
    @JoinColumn(name = "userid")
    private User user;

	public int getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(int userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	} 
	
	

}

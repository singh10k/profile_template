package com.profile.in.model;

import jakarta.persistence.*;

import java.util.Set;


@Table(name = "SECURITY_USERS")
@Entity
public class UserDetails {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer uid;

	@Column(length = 20, unique = true, nullable = false)
	private String uname;

	@Column(length = 150, nullable = false)
	private String pwd;

	@Column(length = 100, unique = true, nullable = false)
	private String email;

	private boolean status = true;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "SECURITY_ROLES",
			joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "uid"))
	@Column(name = "role")
	private Set<String> roles;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getUname() {
		return uname;
	}

	public void setUname(String uname) {
		this.uname = uname;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "UserDetails [uid=" + uid + ", uname=" + uname + ", pwd=" + pwd + ", email=" + email + ", status="
				+ status + ", roles=" + roles + "]";
	}
}


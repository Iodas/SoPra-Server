package ch.uzh.ifi.seal.soprafs19.entity;

import ch.uzh.ifi.seal.soprafs19.constant.UserStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class User implements Serializable {
	

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(nullable = false) 
	private String name;
	
	@Column(nullable = false, unique = true) 
	private String username;

	//token ignore property so we cant just fetch token from all users to edit credentials
	@JsonProperty( value = "token", access = JsonProperty.Access.WRITE_ONLY)
	@Column(nullable = false, unique = true)
	private String token;

	@Column(nullable = false)
	private UserStatus status;

	//Local date class import to get current date
	@GeneratedValue
	private LocalDate date;

	@Column
	private String birthday;

	//password ignore property so we cant just fetch passwords from all users
	@JsonProperty( value = "password", access = JsonProperty.Access.WRITE_ONLY)
	@Column(nullable = false)
	private String password;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public UserStatus getStatus() {
		return status;
	}

	public void setStatus(UserStatus status) {
		this.status = status;
	}

	//getting Date of creation for user
	public void setDate(LocalDate date) { this.date = date; }

	public LocalDate getDate() { return date; }

	public void setBirthday(String birthday){ this.birthday = birthday; }

	public String getBirthday() { return birthday; }

	//password get and set
	public String getPassword() {return password;}

	public void setPassword(String password) {this.password = password;}


	@Override
	public boolean equals(Object o) {
		if (o == this) return true;
		if (!(o instanceof User)) {
			return false;
		}
		User user = (User) o;
		return this.getId().equals(user.getId());
	}
}

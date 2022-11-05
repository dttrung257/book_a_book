package com.uet.book_a_book.entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users", 
	uniqueConstraints = @UniqueConstraint(columnNames = { "email"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUser implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type = "uuid-char")
	@Column(updatable = false)
	private UUID id;

	@Column(nullable = false)
	private String firstName;

	@Column(nullable = false)
	private String lastName;

	@NaturalId
	@Column(nullable = false)
	private String email;

	@Column(nullable = true)
	private String emailVerificationCode;

	@Column(nullable = false)
	@JsonIgnore
	private String password;

	@Column(nullable = true)
	private String gender;

	@Column(nullable = true)
	private String phoneNumber;

	@Column(nullable = true)
	private String address;

	@Lob
	@Column(nullable = true)
	private String avatar;

	@Column(nullable = false)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date createdAt;

	@Column(nullable = true)
	@Temporal(value = TemporalType.TIMESTAMP)
	private Date updatedAt;

	@Column(nullable = false)
	private boolean locked = false;

	@Column(nullable = false)
	private boolean emailVerified = false;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", referencedColumnName = "id")
	private Role role;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Comment> comments;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Order> orders;
	
	@JsonIgnore
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "reset_password_token_id", referencedColumnName = "id")
	private ResetPasswordToken resetPasswordToken;

	public AppUser(String firstName, String lastName, String email, String emailVerificationCode, String password,
			String gender, String phoneNumber, String address, String avatar, Date createdAt, Date updatedAt,
			boolean locked, boolean emailVerified, Role role) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.emailVerificationCode = emailVerificationCode;
		this.password = password;
		this.gender = gender;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.avatar = avatar;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.locked = locked;
		this.emailVerified = emailVerified;
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.singleton(new SimpleGrantedAuthority(this.role.getRoleName()));
	}

	@Override
	public String getUsername() {
		return this.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !locked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return emailVerified;
	}

}

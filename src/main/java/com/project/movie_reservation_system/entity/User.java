package com.project.movie_reservation_system.entity;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.project.movie_reservation_system.enums.UserRole;
import com.project.movie_reservation_system.enums.UserStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table( name = "app_users",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = "userEmail")
})
public class User implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	private String username;

	private String password;

	private String firstName;

	private String lastName;

	private String userEmail;

	@Enumerated(value = EnumType.STRING)
	private UserStatus userStatus;

	private LocalDateTime userCreatedAt;

	private LocalDateTime userUpdatedAt;

	@Enumerated(value = EnumType.STRING)
	private UserRole userRole;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(userRole.name()));
	}

}

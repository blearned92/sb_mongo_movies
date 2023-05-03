package dev.brandonlearned.moviebackend.models;

import java.util.Collection; 
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import dev.brandonlearned.moviebackend.token.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection="users")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity implements UserDetails{

	@Id
	private ObjectId id;
	private String firstname;
	private String lastname;
	private String username;
	private String password;
	private Role role;
	private List<Token> tokens;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// May cause errors return the role or saving it since using mongo and not jpa, keep an eye on this for potential fixing
		return List.of(new SimpleGrantedAuthority(role.name()));
	}
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
}

package ch.bfh.ti.soed.hs16.srs.purple.model;

public class User {
	private int userID;
	private String lastName;
	private String firstName;
	private String emailAddress;
	private String username;
	private String password;
	private UserRole userRole;
	public static enum UserRole {
		USER_ROLE_ADMIN,
		USER_ROLE_HOST,
		USER_ROLE_PARTICIPANT
	};
}

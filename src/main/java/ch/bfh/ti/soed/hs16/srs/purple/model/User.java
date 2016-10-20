package ch.bfh.ti.soed.hs16.srs.purple.model;
/**
 * Ein User ist ein Benutzer des Systems.
 * Ein User registriert sich mit den folgenden Attributen: 
 * - Nachname 
 * - Vorname 
 * - Email-Adresse 
 * - Username 
 * - Passwort 
 * Die Rolle des Benutzers wird in der Klasse UserRole definiert. Die Reservations-Applikation erlaubt die folgenden
 * drei Benutzerrollen: 
 * - USER_ROLE_HOST: Der Veranstalter kann Reservationen tätigen.
 * - USER_ROLE_PARTICIPANT: Ist der Teilnehmer einer Reservation/Veranstaltung.
 * - USER_ROLE_ADMIN: Der Admin hat alle Rechte und wird verwendet um einem User die Veranstaltungsrechte zu geben.
 * 
 * @author Aebischer Patrik, Bösiger Elia, Gestach Lukas, Schildknecht Elias
 * @date 20.10.2016
 * @version 1.0
 *
 */
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
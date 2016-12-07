#language:de
#noinspection SpellCheckingInspection
Funktionalität: Anmelden
	Szenario: Anmeldung mit validen Daten
		Gegeben sei der registrierte Teilnehmer "Max" mit dem Passwort "geheim".
		Wenn ich mich als Teilnehmer "Max" mit dem Passwort "geheim" anmelde,
		Dann sehe ich "Max" in der Liste der Teilnehmer, die gerade Online sind
		
	Szenario: Anmeldung mit invalidem Passwort
		Gegeben sei der registrierte Teilnehmer "Max" mit dem Passwort "geheim".
		Wenn ich mich als Teilnehmer "Max" mit dem Passwort "nichtgeheim" anmelde,
		Dann die Nachricht "Ungültige Zugangsdaten." erscheint.
	
	Szenario: Anmeldung als unbekannter Benutzer
		Gegeben sei der registrierte Teilnehmer "Max" mit dem Passwort "geheim".
		Wenn ich mich als Teilnehmer "Marianne" mit dem Passwort "geheim" anmelde,
		Dann die Nachricht "Ungültige Zugangsdaten." erscheint.
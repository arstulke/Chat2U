#language:de
#noinspection SpellCheckingInspection
Funktionalität: Anmelden
	Szenario: Anmeldung mit validen Daten
		Gegeben sei der registrierte Teilnehmer "Carsten" mit dem Passwort "geheim".
		Wenn ich mich mit gültigen Zugangsdaten als Teilnehmer "Carsten" anmelde,
		Dann sehe ich "Carsten" in der Liste der Teilnehmer, die gerade Online sind
		
	Szenario: Anmeldung mit invalidem Passwort
		Gegeben sei der registrierte Teilnehmer "Carsten" mit dem Passwort "geheim".
		Wenn ich mich mit ungültigen Zugangsdaten als Teilnehmer "Carsten" anmelde,
		Dann wird der Zugriff verweigert
		Und die Nachricht "Ungültige Zugangsdaten" erscheint.
	
	Szenario: Anmeldung als unbekannter Benutzer
		Gegeben sei der registrierte Teilnehmer "Carsten" mit dem Passwort "geheim".
		Wenn ich mich mit ungültigen Zugangsdaten als Teilnehmer "Marianne" anmelde,
		Dann wird der Zugriff verweigert
		Und die Nachricht "Ungültige Zugangsdaten" erscheint.
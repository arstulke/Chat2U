#language:de
#noinspection SpellCheckingInspection
Funktionalität: Registrieren
	Szenario: Als Teilnehmer mit einzigartigem Benutzernamen registrieren
		Gegeben seien keine registrierten Benutzer
		Wenn ich mich als Teilnehmer "Karsten_aber_mit_K" und dem Passwort "geheim" registriere,
		Dann wird das Registrieren abgeschlossen

	Szenario: Als Teilnehmer mit belegten Benutzernamen registrieren
		Gegeben sei der registrierte Teilnehmer "Karsten_aber_mit_K"
		Wenn ich mich als Teilnehmer "Karsten_aber_mit_K" und dem Passwort "geheim2" registriere,
		Dann wird das Registrieren abgeschlossen mit der Nachricht "Benutzername bereits vergeben."



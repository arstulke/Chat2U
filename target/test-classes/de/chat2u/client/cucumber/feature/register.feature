#language:de
#noinspection SpellCheckingInspection
Funktionalität: Registrieren
	Szenario: Als Teilnehmer mit einzigartigem Benutzernamen registrieren
		Wenn ich mich als Teilnehmer "Carsten" und dem Passwort "geheim" registriere,
		Dann wird "Carsten" zur Teilnehmerliste hinzugefügt.
		
	Szenario: Als Teilnehmer mit belegten Benutzernamen registrieren
		Gegeben sei der registrierte Teilnehmer "Carsten" mit dem Passwort "geheim".
		Wenn ich mich als Teilnehmer "Carsten" und dem Passwort "geheim2" registriere,
		Dann wird das Registrieren verweigert
		Und die Nachricht "Benutzername bereits vergeben" erscheint.

	Szenario: Als Administrator mit validen Token registrieren
		Gegeben sei der Registriertoken "UNIQUETOKEN123".
		Wenn ich mich als Teilnehmer "Carsten" und dem Passwort "geheim" registriere,
		Und ich den Token "UNIQUETOKEN123" eingebe
		Dann wird "Carsten" zur Teilnehmerliste hinzugefügt.
		Und wird unter "Carsten" zur Administratorenliste hinzugefügt
		
	Szenario: Als Administrator mit invaliden Token registrieren
		Gegeben sei der Registriertoken "UNIQUETOKEN123".
		Wenn ich mich als Teilnehmer "Carsten" und dem Passwort "geheim" registriere,
		Und ich den Token "RANDOMTOKEN123" eingebe
		Dann wird das Registrieren verweigert
		Und die Nachricht "Ungültiger Token" erscheint.
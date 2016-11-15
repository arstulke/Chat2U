#language:de
#noinspection SpellCheckingInspection
Funktionalität: Registrieren
	Szenario: Als Teilnehmer mit einzigartigem Benutzernamen registrieren
		Wenn ich mich als Teilnehmer "Carsten" und dem Passwort "geheim" registriere,
		Dann wird das Registrieren genehmigt mit der Nachricht "Registrierung erfolgreich; 'Carsten' ist Teilnehmer"

	Szenario: Als Teilnehmer mit belegten Benutzernamen registrieren
		Gegeben sei der registrierte Teilnehmer "Carsten" mit dem Passwort "geheim".
		Wenn ich mich als Teilnehmer "Carsten" und dem Passwort "geheim2" registriere,
		Dann wird das Registrieren verweigert mit der Nachricht "Benutzername bereits vergeben"

	Szenario: Als Administrator mit validen Token registrieren
		Gegeben sei der Registriertoken "UNIQUETOKEN123".
		Wenn ich mich als Teilnehmer "Carsten" und dem Passwort "geheim" mit dem Token "UNIQUETOKEN123" registriere,
		Dann wird das Registrieren genehmigt mit der Nachricht "Registrierung erfolgreich; 'Carsten' ist Aministrator"
		
	Szenario: Als Administrator mit invaliden Token registrieren
		Gegeben sei der Registriertoken "UNIQUETOKEN123".
		Wenn ich mich als Teilnehmer "Carsten" und dem Passwort "geheim" mit dem Token "RANDOMTOKEN123" registriere,
		Dann wird das Registrieren verweigert mit der Nachricht "Ungültiger Token"
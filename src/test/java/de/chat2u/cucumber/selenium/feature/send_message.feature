#language:de
#noinspection SpellCheckingInspection
Funktionalität: send_message

  @WaitForServer
  Szenario: Nachricht an alle Senden
    Wenn "Meike" sich mit dem Passwort "geheim" registriert und einloggt
    Und "KreiselhammerXD2000" sich mit dem Passwort "geheim" registriert und einloggt
    Und "Meike" die Nachricht "Hallo, dein Name ist einzigartig..." an alle sendet
    Dann wird bei "KreiselhammerXD2000" die Nachricht "Hallo, dein Name ist einzigartig..." erscheinen

  @CleanSeleniumAfterThat
  Szenario: Nachricht an einen Benutzer
    Wenn "Marianne" sich mit dem Passwort "geheim" registriert und einloggt
    Und "Meike" die Nachricht "Ich mag den mit dem komischen Namen nicht..." an "Marianne" sendet
    Dann wird bei "Marianne" die Nachricht "Ich mag den mit dem komischen Namen nicht..." erscheinen
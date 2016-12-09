#language:de
#noinspection SpellCheckingInspection
Funktionalität: search_user

  @WaitForServer
  Szenario: Registrieren und einloggen
    Wenn "Carsten" sich mit dem Passwort "geheim" registriert und einloggt
    Dann ist die Anmeldeaufforderung für "Carsten" verschwunden
    Und "Carsten" erscheint in der Liste der Benutzer, welche online sind

  @CleanSeleniumAfterThat
  Szenario: Nach Benutzern suchen
    Wenn "Carsten" nach "Mi" sucht
    Dann werden bei "Carsten" die Benutzer angezeigt:
      | Michelle |
      | Mike     |
      | Michael  |
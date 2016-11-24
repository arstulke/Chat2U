#language:de
#noinspection SpellCheckingInspection
Funktionalität: search_user
    @startSzenario
    Szenario: Registrieren und einloggen
      Wenn "Carsten" sich mit dem Passwort "geheim" registriert und einlogggt
      Dann ist die Anmeldeaufforderung für "Carsten" verschwunden
      Und er erscheint in der Liste der Benutzer, welche online sind

    @endSzenario
    Szenario: Nach Benutzern suchen
      Gegeben seien die angemeldeten Benutzer
        | Carsten    |
        | Marianne   |
        | Thorsten   |
      Wenn "Carsten" nach "Tho" sucht
      Dann werden bei "Carsten" die Benutzer angezeigt:
        | Thorsten   |
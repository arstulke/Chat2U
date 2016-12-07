#language:de
#noinspection SpellCheckingInspection
Funktionalität: search_user

  @startSzenario
  Szenario: Registrieren und einloggen
    Wenn "Carsten" sich mit dem Passwort "geheim" registriert und einlogggt
    Dann ist die Anmeldeaufforderung für "Carsten" verschwunden
    Und er erscheint in der Liste der Benutzer, welche online sind

  Szenario: Nach Benutzern suchen
    Gegeben seien die angemeldeten Benutzer
      | Carsten  |
      | Marianne |
      | Thorsten |
    Wenn "Carsten" nach "Tho" sucht
    Dann werden bei "Carsten" die Benutzer angezeigt:
      | Thorsten |

  Szenario: Nachricht an alle Senden
    Gegeben seien die angemeldeten Benutzer
      | Carsten  |
    Wenn "KreiselhammerXD2000" sich mit dem Passwort "geheim" registriert und einlogggt
    Und "Carsten" die Nachricht an alle "Hallo, Das ist einzigartig" sendet
    Dann wird bei "KreiselhammerXD2000" die Nachricht "Hallo, Das ist einzigartig" erscheinen

  @endSzenario
  Szenario: Ende
    Gegeben sei das Ende vom Lied.
#language:de
#noinspection SpellCheckingInspection
Funktionalität: Erstelle eine Gruppe
    @WaitForServer
    Szenario: Erstelle eine Gruppe mit keinen Mitgliedern
        Wenn "Jörg_der_Gute" sich mit dem Passwort "geheim" registriert und einloggt
        Und "Jörg_der_Gute" eine Gruppe mit den Namen "Jörgs Superbrause Party" erstellt und die Teilnehmer einlädt:
        |  |
        Dann verschwindet das Fenster bei "Jörg_der_Gute" zum Erstellen einer Gruppe nicht
        Und es erscheint bei "Jörg_der_Gute" die Nachricht "Bitte wähle mehr Benutzer aus."

    Szenario: Erstelle eine Gruppe mit einem zu kurzen Namen
        Wenn "Jörg_der_Gute" eine Gruppe mit den Namen "JP" erstellt und die Teilnehmer einlädt:
            | Michelle |
        Dann verschwindet das Fenster bei "Jörg_der_Gute" zum Erstellen einer Gruppe nicht
        Und es erscheint bei "Jörg_der_Gute" die Nachricht "Gruppenname zu kurz."

    @CleanSeleniumAfterThat
    Szenario: Erstelle eine Gruppe mit validen Parametern
        Und "Jörg_der_Gute" eine Gruppe mit den Namen "Jörgs Superbrause Party" erstellt und die Teilnehmer einlädt:
            | Michelle |
        Dann verschwindet das Fenster bei "Jörg_der_Gute" zum Erstellen einer Gruppe
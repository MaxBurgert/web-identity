# Required steps to setup and configure Apache
For our project we decided to use a server running Ubuntu 16.04 LTS because this was one of the possible OSs you can receive when ordering a virtual machine at ITS (the other one is CentOS). 
First you need to follow the basic setup steps described by `Switch` [here](https://www.switch.ch/aai/guides/sp/installation/?os=ubuntu).

Once the the basic setup is done, we need a certificate to be able to use `HTTPS`. Therefore we used `Let's Encrypt` which easily integrates into Apache Server and is also available for our VM.

Next we need to generate the correct configuration for our server. `Switch` already offers a guide and generator to easily get the correct files. The generator can be found [here](https://www.switch.ch/aai/guides/sp/configuration/).

In Profil generator einfügen auf switch Seite

Sites-avail/dmi-overlords.conf ssl hinzufügen —> full chain Pfad mit angeben und privkey

Apache module ssl enablen

rewrite module enablen

Ufw enable apache full

SSL DONE


Alles in /etc/shibboleth…

Shibboleth xml config vom configurator downloaden und ersetztem

Switch aaa cert rinterlafen

Attribute maps runterladen und als xml speichern

Attribute policy runterlasen und als xml speichern


shibd -t —> will check shibboleth configuration


Wir nutzen xenial daher nur Shibboleth 2.6.1


Ressource bei Switch registrieren mit Metadaten


Attribute viewer switch


Persistent id als identifier für user


apache2 enable shib2


Php print $_SERVER for variablen
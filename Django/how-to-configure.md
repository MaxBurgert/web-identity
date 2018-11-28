# Required steps to setup and configure Apache
For our project we decided to use a server running Ubuntu 16.04 LTS because this was one of the possible OSs you can receive when ordering a virtual machine at ITS (the other one is CentOS). 
First you need to follow the basic setup steps described by `Switch` [here](https://www.switch.ch/aai/guides/sp/installation/?os=ubuntu).

Once the the basic setup is done, we need a certificate to be able to use `HTTPS`. Therefore we used `Let's Encrypt` which easily integrates into Apache Server and is also available for our VM.

Its is now important to add you ssl certificate to your sites configuration in the apache configuration (e.g. `Sites-avail/dmi-overlords.conf` full chain path and path to private key file). To use the ssl certificate the apache module `ssl` needs to be enabled. To automatically redirect/rewirte all trafic to `https` you can also enable the apache rewrite module. Last but not least the firewall must now allow all traffic to the apache server. You can easily configure it by using the `ufw` command. (e.g. `ufw enable apache full`).
The SSL configuration is now complete and each request should only use `https` now.

Next we need to generate the correct configuration for our server. `Switch` already offers a guide and generator to easily get the correct files. The generator can be found [here](https://www.switch.ch/aai/guides/sp/configuration/).
This generator will create a `XML` file containing the configuration which must be saved in the `/etc/shibboleth` configuration directory.
Next you also need to download the `SWITCHaaiRootCA.crt.pem` and also store it in `/etc/shibboleth`.
Next download `attribute-map.xml` and `attribute-policy.xml` and also add it to the shibboleth directory.
With `shibd -t` you can check you configuration. Now fix all errors. Because we are using Ubuntu 16.04 LTS the newest Shibboleth version available is `2.6.1`. But the instructions are written for `3.0`. Therefore you may need to remove some xml sections from your configuration which are unknown to you shibboleth installation.

For the next part you need to contact your organization's representative managing the SWITCH AAI services. The representative must now add register you as a service provider and define which attributes your service should receive after a successful user login. In general your service's metadata must be defined.
To see all available attributes one can visit the [Switch AAI Attribute Viewer](https://attribute-viewer.aai.switch.ch/).

Note: In case you want to identify a user you should use it's `Persistent ID` because it will be unique during all sessions and at all organizations participating at `Switch AAI`.

Now you can enable the Shibboleth apache module with `apache2 enable shib2`.

To test your instance you can print the variables by using a simple php file containing ` print $_SERVER`.
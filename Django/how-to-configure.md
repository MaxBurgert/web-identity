# Required steps to set up and configure Apache
For our project, we decided to use a server running Ubuntu 16.04 LTS because this was one of the possible OSs you can receive when ordering a virtual machine at ITS (the other one is CentOS). 
First, you need to follow the basic setup steps described by `SWITCH` [here](https://www.switch.ch/aai/guides/sp/installation/?os=ubuntu).

Once the basic setup is done, we need a certificate to be able to use `HTTPS`. Therefore we used `Let's Encrypt` which easily integrates into Apache Server and is also available for our VM.

It is now important to add you SSL certificate to your site's configuration in the Apache configuration (e.g. `Sites-avail/dmi-overlords.conf` full chain path and path to the private key file). To use the SSL certificate the apache module `SSL` needs to be enabled. To automatically redirect/rewrite all traffic to `https` you can also enable the apache rewrite module. Last but not least the firewall must now allow all traffic to the Apache server. You can easily configure it by using the `ufw` command. (e.g. `ufw enable apache full`).
The SSL configuration is now complete and each request should only use `https` now.

Next, we need to generate the correct configuration for our server. `Switch` already offers a guide and generator to easily get the correct files. The generator can be found [here](https://www.switch.ch/aai/guides/sp/configuration/).
This generator will create a `XML` file containing the configuration which must be saved in the `/etc/shibboleth` configuration directory.
Next, you also need to download the `SWITCHaaiRootCA.crt.pem` and also store it in `/etc/shibboleth`.
Next, download `attribute-map.xml` and `attribute-policy.xml` and also add it to the shibboleth directory.
With `shibd -t` you can check your configuration. Now fix all errors. Because we are using Ubuntu 16.04 LTS the newest Shibboleth version available is `2.6.1`. But the instructions are written for `3.0`. Therefore you may need to remove some XML sections from your configuration which are unknown to your shibboleth installation.

For the next part, you need to contact your organization's representative managing the SWITCH AAI services. The representative must now add and register you as a service provider and define which attributes your service should receive after a successful user login. In general, your service's metadata must be defined.
To see all available attributes one can visit the [SWITCH AAI Attribute Viewer](https://attribute-viewer.aai.switch.ch/).

Note: In case you want to identify a user you should use it's `Persistent ID` because it will be unique during all sessions and at all organizations participating at `SWITCH AAI`.

Now you can enable the Shibboleth Apache module with `apache2 enable shib2`.

To test your instance you can print the variables by using a simple PHP file containing ` print $_SERVER`.

from django.conf import settings
from django.db import models


class OverlordsUserModel(models.Model):
    user = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
    login_method_simple = models.BooleanField(default=False, blank=True)
    login_method_aai = models.BooleanField(default=False, blank=True)
    totp_secret = models.CharField(max_length=255)
    home_organization = models.CharField(max_length=255, blank=True, default=None)
    affiliation = models.CharField(max_length=255, blank=True, default=None)
    persistent_id = models.CharField(max_length=255, blank=True, default=None)

    def __str__(self):
        return self.user.username
from django.conf.urls import url

from authorizer import views

urlpatterns = [
    url(r'^(?![\s\S])', views.index),
    url(r'^signup/$', views.signup, name='signup'),
    url(r'^qrcode/(?P<secret>[\w\d]+)$', views.qrcode, name='qrcode'),
    url(r'^totpcheck/$', views.compare_totp_code, name='totpcheck')
]

from django.conf.urls import url

from authorizer import views

urlpatterns = [
    url(r'^(?![\s\S])', views.index),
    url(r'^login/$', views.simple_login_overlord, name='login'),
    url(r'^login/aailogin/$', views.aai_login, name='aailogin'),
    url(r'^signup/$', views.signup, name='signup'),
    url(r'^qrcode/$', views.qrcode, name='qrcode'),
]

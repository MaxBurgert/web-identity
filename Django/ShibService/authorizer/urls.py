from django.conf.urls import url

from authorizer import views

urlpatterns = [
    url(r'^(?![\s\S])', views.index),
    url(r'^signup/$', views.signup, name='signup'),
    url(r'^qrcode/$', views.qrcode, name='qrcode')
]

from django.conf.urls import url
from django.urls import path

from authorizer import views

urlpatterns = [
    url(r'^(?![\s\S])', views.index),
]

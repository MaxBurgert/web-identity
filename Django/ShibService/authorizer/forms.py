from django import forms
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth.models import User
from django.forms import EmailField


class OverlordUserCreationForm(UserCreationForm):
    class Meta:
        model = User
        fields = ('username', 'password1', 'password2')
        field_classes = {'username': EmailField}

    def save(self, commit=True):
        user = super(UserCreationForm, self).save(commit=False)
        user.email = user.username
        if commit:
            user.save()
        return user


class LoginForm(forms.Form):
    mail = forms.EmailField()
    password = forms.PasswordInput()
    totp_code = forms.CharField()

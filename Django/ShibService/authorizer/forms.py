from crispy_forms.helper import FormHelper
from django import forms
from django.contrib.auth.forms import UserCreationForm
from django.contrib.auth.models import User
from django.forms import EmailField

from authorizer.models import OverlordsUserModel


class OverlordUserCreationForm(UserCreationForm):
    class Meta:
        model = User
        fields = ('username', 'password1', 'password2')
        field_classes = {'username': EmailField}

    def create_user_without_pw(self, email):
        user = super(UserCreationForm, self).save(commit=False)
        user.email = email
        user.username = email
        self.fields['password1'].required = False
        self.fields['password2'].required = False
        self.fields['password1'].widget.attrs['autocomplete'] = 'off'
        self.fields['password2'].widget.attrs['autocomplete'] = 'off'
        return user

    def save(self, commit=True):
        user = super(UserCreationForm, self).save(commit=False)
        user.set_password(self.cleaned_data['password1'])
        user.email = user.username
        if commit:
            user.save()
        return user


class UserDisplayForm(forms.ModelForm):
    class Meta:
        model = User
        fields = '__all__'

    def __init__(self, *args, **kwargs):
        super(UserDisplayForm, self).__init__(*args, **kwargs)

        self.helper = FormHelper(self)


class OverlordUserDisplayForm(forms.ModelForm):
    class Meta:
        model = OverlordsUserModel
        exclude = ['user']
        widgets = {
            'login_method_simple': forms.TextInput(attrs={'readonly': 'readonly'}),
            'login_method_aai': forms.TextInput(attrs={'readonly': 'readonly'}),
            'totp_secret': forms.TextInput(attrs={'readonly': 'readonly'}),
            'home_organization': forms.TextInput(attrs={'readonly': 'readonly'}),
            'affiliation': forms.TextInput(attrs={'readonly': 'readonly'}),
            'persistent_id': forms.TextInput(attrs={'readonly': 'readonly'}),
        }

    def __init__(self, *args, **kwargs):
        super(OverlordUserDisplayForm, self).__init__(*args, **kwargs)

        self.helper = FormHelper(self)


class LoginForm(forms.Form):
    username = forms.CharField()
    password = forms.CharField(widget=forms.PasswordInput())
    totp_code = forms.CharField()


class TOTPForm(forms.Form):
    totp_code = forms.CharField()

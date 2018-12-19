from django import forms


class LoginForm(forms.Form):
    username = forms.CharField()
    password = forms.PasswordInput()
    totp_code = forms.CharField()

class TOTPForm(forms.Form):
    code = forms.CharField(max_length=6)

from django import forms


class TOTPForm(forms.Form):
    code = forms.CharField(max_length=6)

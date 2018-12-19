import os

from django.contrib.auth import login, authenticate
from django.contrib.auth.forms import UserCreationForm
from django.shortcuts import render, redirect
from qr_code.qrcode.utils import QRCodeOptions


def signup(request):
    if request.method == 'POST':
        form = UserCreationForm(request.POST)
        if form.is_valid():
            form.save()
            username = form.cleaned_data.get('username')
            raw_password = form.cleaned_data.get('password1')
            user = authenticate(username=username, password=raw_password)
            login(request, user)
            return redirect(index)
    else:
        form = UserCreationForm()
    return render(request, 'registration/signup.html', {'form': form})


def index(request):
    for key in request.META:
        print(str(key))
        print(str(request.META[key]))

    if os.access(__file__, os.R_OK):
        print('Can Read')
    if os.access(__file__, os.W_OK):
        print('Can Write')
    if os.access(__file__, os.X_OK):
        print('Can execute')

    return render(request, 'auth-index.html', {'meta': request.META})


def qrcode(request):
    context = dict(
        my_options=QRCodeOptions(size='H', border=6, error_correction='H'),
        secret=123456789
    )
    return render(request, 'registration/qrcode.html', context=context)

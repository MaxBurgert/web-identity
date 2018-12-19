import base64
import os

from django.contrib.auth import login, authenticate
from django.contrib.auth.forms import UserCreationForm
from django.http import HttpResponseRedirect
from django.shortcuts import render, redirect
from qr_code.qrcode.utils import QRCodeOptions

from authorizer.TOTP import TOTP
from authorizer.forms import TOTPForm


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
    secret_key = base64.b32encode("1234567890".encode("UTF-8"))
    print(secret_key)
    secret_key = secret_key.decode("UTF-8")
    print(secret_key)
    context = dict(
        my_options=QRCodeOptions(size='M', border=6, error_correction='H'),
        secret=f'otpauth://totp/InternetOverlords?secret={secret_key}&issuer=UniversityOfBasel'
    )
    return render(request, 'registration/qrcode.html', context=context)


def compare_totp_code(request):
    # if this is a POST request we need to process the form data
    if request.method == 'POST':
        # create a form instance and populate it with data from the request:
        form = TOTPForm(request.POST)
        # check whether it's valid:
        if form.is_valid():
            # process the data in form.cleaned_data as required
            # ...
            # redirect to a new URL:
            print(form.cleaned_data)
            totp = TOTP('1234567890')
            print(totp.getKey())
            return render(request, 'totpres.html', {'status': totp.getKey() == form.cleaned_data.get('code')})


    # if a GET (or any other method) we'll create a blank form
    else:
        form = TOTPForm()

    return render(request, 'totpcheck.html', {'form': form})

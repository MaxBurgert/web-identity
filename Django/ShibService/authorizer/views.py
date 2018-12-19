import base64
import os

from django.contrib.auth import login, authenticate
from django.contrib.auth.forms import UserCreationForm
from django.shortcuts import render, redirect
from django.utils.crypto import get_random_string
from qr_code.qrcode.utils import QRCodeOptions

from authorizer.TOTP import TOTP
from authorizer.forms import TOTPForm, LoginForm
from authorizer.models import OverlordsUserModel


def signup(request):
    if request.method == 'POST':
        form = UserCreationForm(request.POST)
        if form.is_valid():
            form.save()
            username = form.cleaned_data.get('username')
            raw_password = form.cleaned_data.get('password1')
            # user_in_db = User.objects.get(username=username)
            user = authenticate(username=username, password=raw_password)
            secret = get_random_string(50)
            overlord_user = OverlordsUserModel.objects.get_or_create(user=user, login_method_simple=True,
                                                                     totp_secret=secret)

            login(request, user)
            return redirect('qrcode', secret=secret)
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


def qrcode(request, secret):
    secret_key = base64.b32encode("1234567890".encode("UTF-8"))
    secret_key = secret_key.decode("UTF-8")
    print(secret)
    context = dict(
        my_options=QRCodeOptions(size='M', border=3, error_correction='H'),
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


def login_overlord(request):
    if request.method == 'POST':
        # create a form instance and populate it with data from the request:
        form = TOTPForm(request.POST)
        # check whether it's valid:
        if form.is_valid():
            username = form.cleaned_data.get('username')
            password = form.cleaned_data.get('password')
            totp_code = form.cleaned_data.get('totp_code')
            user = authenticate(username=username, password=password)

            overlord = OverlordsUserModel.objects.get(user=user)
            totp_checker = TOTP(overlord.totp_secret)

            if totp_checker.getKey() == totp_code:
                login(request, user)
            else:
                return render(request, 'totpres.html', {'status': False})

            return redirect(index)
    else:
        form = LoginForm()

    return render(request, 'registration/login.html', {'form': form})

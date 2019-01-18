import base64

from django.contrib.auth import login, authenticate
from django.contrib.auth.models import User
from django.http import Http404
from django.shortcuts import render, redirect
from django.utils.crypto import get_random_string
from qr_code.qrcode.utils import QRCodeOptions

from authorizer.TOTP import TOTP
from authorizer.forms import LoginForm, OverlordUserCreationForm, TOTPForm
from authorizer.models import OverlordsUserModel


def signup(request):
    if request.method == 'POST':
        form = OverlordUserCreationForm(request.POST)
        if form.is_valid():
            form.save()

            username = form.cleaned_data.get('username')
            raw_password = form.cleaned_data.get('password1')
            # user_in_db = User.objects.get(username=username)
            user = authenticate(username=username, password=raw_password)
            secret = get_random_string(50)
            OverlordsUserModel.objects.get_or_create(user=user, login_method_simple=True, totp_secret=secret)

            login(request, user)
            request.session['_totp_secret'] = secret
            return redirect('qrcode')
    else:
        form = OverlordUserCreationForm()

    return render(request, 'registration/signup.html', {'form': form})


def aai_login(request):
    mail = request.META['mail']
    home_organization = request.META['homeOrganization']
    affiliation = request.META['affiliation']
    first_name = request.META['givenName']
    last_name = request.META['surname']
    persistent_id = request.META['persistent-id']

    users = User.objects.all()

    registered = False
    for user in users:
        if user.email == mail:
            # User already signup with his mail, set flag in form
            registered = True

            if user.password:
                user.password = ''

            if user.first_name != first_name:
                user.first_name = first_name
                user.last_name = last_name

            user.save()
            request.session['aai_persistent_id'] = OverlordsUserModel.objects.get(user=user).persistent_id
            return redirect('aai_login_totp')

    if not registered:
        # if a user logs in first with shibboleth, an account without a password will be created and the user
        form = OverlordUserCreationForm()
        new_user = form.create_user_without_pw(mail)
        new_user.first_name = first_name
        new_user.last_name = last_name
        new_user.save()

        # Create the corresponding OverlordUser which references new_user
        secret = get_random_string(50)
        OverlordsUserModel.objects.get_or_create(user=new_user, login_method_aai=True,
                                                 totp_secret=secret, home_organization=home_organization,
                                                 affiliation=affiliation, persistent_id=persistent_id)

        login(request, new_user)
        request.session['_totp_secret'] = secret
        return redirect('qrcode')  # show the qr code upon first registration


def aai_login_totp(request):
    if request.method == 'GET':
        form = TOTPForm()
    elif request.method == 'POST':
        form = TOTPForm(request.POST)
        if form.is_valid():
            overlord_user = OverlordsUserModel.objects.get(persistent_id=request.session['aai_persistent_id'])

            totp_checker = TOTP(overlord_user.totp_secret)
            if totp_checker.getKey() == form.cleaned_data.get('totp_code'):
                login(request, overlord_user.user)
                return redirect(index)
            else:
                form.add_error('totp_code', 'TOTP Code incorrect.')

    return render(request, 'registration/totpChecker.html', {'form': form, 'user': OverlordsUserModel.objects.get(
        persistent_id=request.session['aai_persistent_id']).user})


def index(request):
    return render(request, 'auth-index.html', {'meta': request.META})


def qrcode(request):
    secret = request.session.get('_totp_secret')
    if secret:
        secret_key = base64.b32encode(secret.encode("UTF-8"))
        secret_key = secret_key.decode("UTF-8")
        context = dict(
            my_options=QRCodeOptions(size='M', border=3, error_correction='H'),
            secret=f'otpauth://totp/InternetOverlords?secret={secret_key}&issuer=UniversityOfBasel'
        )
        return render(request, 'registration/qrcode.html', context=context)
    else:
        raise Http404('No totp secret was found in the current session.')


def simple_login_overlord(request):
    if request.method == 'POST':
        # create a form instance and populate it with data from the request:
        form = LoginForm(request.POST)
        # check whether it's valid:
        if form.is_valid():
            username = form.cleaned_data.get('username')
            password = form.cleaned_data.get('password')
            totp_code = form.cleaned_data.get('totp_code')

            user = authenticate(username=username, password=password)

            if user:
                # Found user in database
                overlord = OverlordsUserModel.objects.get(user=user)
                totp_checker = TOTP(overlord.totp_secret)

                if totp_checker.getKey() == totp_code:
                    login(request, user)
                    return redirect(index)
                else:
                    form.add_error('totp_code', 'TOTP Code incorrect.')
            else:
                users = User.objects.all()

                registered = False
                for user in users:
                    if user.email == username and OverlordsUserModel.objects.get(user=user).login_method_aai:
                        # User already signup with his mail probably via AAI , set flag in form
                        registered = True
                        form.add_error('username', 'Please use your SWITCH Login.')

                if not registered:
                    form.add_error('password', 'Something is fishy with your login credentials.')
    else:
        form = LoginForm()

    return render(request, 'registration/login.html', {'form': form})

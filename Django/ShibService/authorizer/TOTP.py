import array
import hashlib
import hmac
import time

from numpy import long


# inspiration: https://blog.gingerlime.com/2010/once-upon-a-time/
class TOTP:

    def __init__(self, secret_key, digits=6, window=30):
        self.digits = digits
        self.secret_key = bytes(secret_key, 'utf-8')
        self.window = window

    def getKey(self):
        tmp = long(self.now() / self.window)
        bytes_tmp = self.to_byte_array(tmp)
        hash = hmac.new(self.secret_key, bytes_tmp, hashlib.sha1).hexdigest()

        return self.truncate(hash)[-self.digits:]

    def truncate(self, hash):
        offset = int(hash[-1], 16)
        binary = int(hash[(offset * 2):((offset * 2) + 8)], 16) & 0x7fffffff
        return str(binary)

    def now(self):
        return time.time()

    def to_byte_array(self, num):
        byte_array = array.array('B')
        for i in reversed(range(0, 8)):
            byte_array.insert(0, num & 0xff)
            num >>= 8
        return byte_array


if __name__ == '__main__':
    totp = TOTP('1234')
    print(totp.getKey())

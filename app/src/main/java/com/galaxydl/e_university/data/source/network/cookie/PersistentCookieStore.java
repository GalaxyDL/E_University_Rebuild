package com.galaxydl.e_university.data.source.network.cookie;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**
 * Created by Galaxy on 2018/3/15.
 */

final class PersistentCookieStore {
    private static final String TAG = "PersistentCookieStore";
    private static final String COOKIE_PREFS = "CookiePrefs";
    private static final String COOKIE_NAME_PREFIX = "cookie_";
    private static final String COOKIE_NAME_DELIMITER = ",";

    private final SharedPreferences sharedPreferences;

    private HashMap<String, ConcurrentMap<String, Cookie>> cookies;

    public PersistentCookieStore(Context context) {
        sharedPreferences = context.getSharedPreferences(COOKIE_PREFS, Context.MODE_PRIVATE);
        cookies = new HashMap<>();

        initAllCookies();
    }

    public void add(List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie cookie : cookies) {
                Log.d(TAG, "add: adding" + cookie);
                add(cookie);
            }
        }
    }

    private void add(Cookie cookie) {
        if (!cookies.containsKey(cookie.domain()))
            cookies.put(cookie.domain(), new ConcurrentHashMap<String, Cookie>());

        if (cookie.expiresAt() > System.currentTimeMillis()) {
            cookies.get(cookie.domain()).put(cookie.name(), cookie);
        } else {
            if (cookies.containsKey(cookie.domain()))
                cookies.get(cookie.domain()).remove(cookie.domain());
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(cookie.domain(), TextUtils.join(COOKIE_NAME_DELIMITER,
                cookies.get(cookie.domain()).keySet()));
        editor.putString(COOKIE_NAME_PREFIX + cookie.name(),
                encodeCookie(new ExternalizableCookie(cookie)));
        editor.apply();
    }

    public List<Cookie> get(HttpUrl url) {
        ArrayList<Cookie> result = new ArrayList<>();
        for (String key : cookies.keySet()) {
            if (url.host().contains(key)) {
                result.addAll(cookies.get(key).values());
            }
        }
        Log.d(TAG, "get: " + result);
        return result;
    }

    private void initAllCookies() {
        ConcurrentHashMap<String, Cookie> cookies;
        Map<String, ?> map = sharedPreferences.getAll();
        for (String domin : map.keySet()) {
            if (!domin.startsWith(COOKIE_NAME_PREFIX)) {
                cookies = initCookies(domin);
                if (cookies != null) {
                    this.cookies.put(domin, cookies);
                }
            }
        }
    }

    private ConcurrentHashMap<String, Cookie> initCookies(String domain) {
        ConcurrentHashMap<String, Cookie> cookies = new ConcurrentHashMap<>();
        Cookie cookie;
        String names = sharedPreferences.getString(domain, "");
        for (String name : names.split(COOKIE_NAME_DELIMITER)) {
            cookie = initCookie(name);
            if (cookie != null) {
                Log.d(TAG, "initCookies: " + cookie);
                cookies.put(name, cookie);
            }
        }
        return cookies;
    }

    private Cookie initCookie(String name) {
        Cookie cookie;
        String encodedCookie = sharedPreferences.getString(COOKIE_NAME_PREFIX + name,
                "");
        cookie = decodeCookie(encodedCookie);
        return cookie;
    }

    private String encodeCookie(ExternalizableCookie cookie) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream os = new ObjectOutputStream(bos);
            os.writeObject(cookie);
        } catch (IOException e) {
            Log.d(TAG, "encodeCookie: " + e.getMessage());
        }
        return toHexString(bos.toByteArray());
    }

    private Cookie decodeCookie(String encodedCookie) {
        ByteArrayInputStream bis = new ByteArrayInputStream(toByteArray(encodedCookie));
        Cookie cookie = null;
        try {
            ObjectInputStream ois = new ObjectInputStream(bis);
            cookie = ((ExternalizableCookie) ois.readObject()).getCookie();
        } catch (ClassNotFoundException | IOException e) {
            Log.d(TAG, "decodeCookie: " + e.getMessage());
        }
        return cookie;
    }

    private String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        int v;
        for (byte i : bytes) {
            v = i & 0xff;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    private byte[] toByteArray(String hexString) {
        final int len = hexString.length();
        byte[] result = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            result[i / 2] = (byte) (Character.digit(hexString.charAt(i), 16) * 16
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return result;
    }
}
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/neamar/Documents/Apps/android-sdk-linux/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}


# Make sure stack traces looks goods
-dontobfuscate


-keep public class com.google.android.gms.* { public *; }
-keep class android.support.v7.widget.SearchView { *; }
-dontwarn com.google.android.gms.**


# Newrelic
-keep class com.newrelic.** { *; }
-dontwarn com.newrelic.**
-keepattributes Exceptions, Signature, InnerClasses, LineNumberTable

# Mixpanel
-dontwarn com.mixpanel.**

# Amplitude
-keep class com.google.android.gms.ads.** { *; }
-dontwarn okio.**
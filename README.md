# Movie Hotness
An Android app to sort and browse movies by most popular or highest rated. Watch all the trailers, read all the reviews, and then save to your favourites and share with your friends. Movies saved locally are available offline (excluding trailers). Designed for phones and tablets.
# Install
###### Build variant
By default the app will be set to "mockDebug" which loads mock data. To switch to "prodDebug" and load live data follow these steps in Android Studio:

1. Go to "View" -> "Tool Windows" -> "Build Variants"
2. Under "Build Variant" select "prodDebug"

I'm not aware of a way to change the default from simply alphabetical. There seems to be a ticket open for this here: [https://code.google.com/p/android/issues/detail?id=64917](https://code.google.com/p/android/issues/detail?id=64917)

###### API keys
For API calls to work you need an API key. This app uses [themoviedb](https://www.themoviedb.org/) API. Add this line to the "$HOME/.gradle/gradle.properites" file:
```
themoviedbApiKey="<yourKey>"
```
# Licence
[![AUR](https://img.shields.io/aur/license/yaourt.svg)]()

[GNU General Public License v3.0](http://choosealicense.com/licenses/gpl-3.0/)
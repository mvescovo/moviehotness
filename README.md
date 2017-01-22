# Movie Hotness
An Android app to sort and browse movies by most popular or highest rated. Watch all the trailers, read all the reviews, and then save to your favourites and share with your friends. Movies saved locally are available offline (excluding trailers). Designed for phones and tablets.

# Screenshots
<img src="https://cloud.githubusercontent.com/assets/15829736/22179804/0d1871b6-e0b2-11e6-8c84-591d14c69008.png" height="500" width="281">
<img src="https://cloud.githubusercontent.com/assets/15829736/22179812/25238638-e0b2-11e6-81fc-40f5f90c3cf4.png" height="500" width="281">
<img src="https://cloud.githubusercontent.com/assets/15829736/22179824/5a5cfa82-e0b2-11e6-8ce2-cf94061c2e40.png" height="500" width="281">
<img src="https://cloud.githubusercontent.com/assets/15829736/22179841/ae68159e-e0b2-11e6-94e2-5d6eedd1870a.png" height="352" width="500">

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
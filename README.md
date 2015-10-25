# Map and Localization Integration

Repository dedicated to the workshop on Map and Localization Integration for the Mobile Computation course. In it you'll find four distinct apps: 
- ***Map Controls Demo***, a simple stand-alone example of how to use the **My Location** map control and pairing it up with **GPS location updates**.
- ***Map Types Demo***, a simple stand-alone example of the different existing map types.
- ***Markers Demo***, a simple stand-alone example of Marker properties and how to define a route between two of them.
- ***GoogleMaps Demo***, a stand-alone app that brings all of the previous ones together and going further, showcasing most of what is possible to achieve using ***GoogleMaps API V2***.

## Getting Started

All applications that make use of ***GoogleMaps API V2*** need a registered **Key** which can be created via **Google's Developer Console**
```xml
AIzaSyBlBiAM1NjTqUiR8J9Wcx8Z6wxISldzQ_s
```
and should include the following lines in the manifest.xml file.
```xml
<!-- The ACCESS_COARSE/FINE_LOCATION permissions are not required to use
 Google Maps Android API v2, but are recommended. -->
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

But worry not, ***Android Studio*** and ***Google*** are pretty helpful and will guide you through the whole process easily.

## (Hopefully) Useful Links

- [GoogleMaps Android API]
- [Location]
- [Marker]
- [Map Options]
- [GeoCoding]
- [Places]
- [Directions]
- [UiSettings]
- [Interactivity]
- [Location Strategies]
- [Receive Location Updates]
- [Indoor Maps]


## License
***MIT*** (Free Software, Hell Yeah!)

[//]: # (These are reference links used in the body of this note and get stripped out when the markdown processor does it's job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)

[GoogleMaps Android API]: <https://developers.google.com/maps/documentation/android-api/>
[Location]: <https://developer.android.com/guide/topics/location/index.html>
[Marker]: <https://developers.google.com/maps/documentation/android-api/marker>
[Map Options]: <https://developers.google.com/android/reference/com/google/android/gms/maps/GoogleMapOptions>
[GeoCoding]: <https://developers.google.com/maps/documentation/geocoding/intro>
[Places]: <https://developers.google.com/places/android-api/>
[Directions]: <https://developers.google.com/maps/documentation/directions/intro>
[UiSettings]: <https://developers.google.com/android/reference/com/google/android/gms/maps/UiSettings>
[Interactivity]: <https://developers.google.com/maps/documentation/android-api/interactivity>
[Location Strategies]: <http://developer.android.com/guide/topics/location/strategies.html>
[Receive Location Updates]: <https://developer.android.com/training/location/receive-location-updates.html>
[Indoor Maps]: <https://www.google.com/maps/about/partners/indoormaps/>




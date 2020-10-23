# TonyCameraUtil

This is a simple record button you can customize for android application.



![](demo.gif)



# Import
#### 1.Add this in your project `build.gradle`:
    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
  
#### 2.Add this dependency in your app level `build.gradle`:
    dependencies {
        ...
       implementation 'com.github.SGTjeong:TonyCameraUtil:0.1.0'
    }
   
# How to use
### 1. In your layout XML file:
```xml
<com.example.tonywidgets.TonyRecordButton
    android:layout_width="70dp"
    android:layout_height="70dp"
    app:maxDuration="5000"
    app:buttonColor="#ffffff"
    app:strokeColor="#ffffff"
    app:strokeWidth="5dp"
    app:strokeAlpha="128"
    app:recordColor="#ff0000" />
```
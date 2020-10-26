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
### 2. In your Activity class:
```kotlin

val btn = findViewById<TonyRecordButton>(R.id.btn)

/**
 *  callback for record event
 * */
btn.setActionListener(object : TonyRecordButton.ActionListener{
    override fun onCapture() {
        Log.e(TAG, "onCapture")
    }

    override fun onHandsFree() {
        Log.e(TAG, "onHandsFree")
    }

    override fun onStartRecord() {
        Log.e(TAG, "onStartRecord")
    }

    override fun onFinishRecord() {
        Log.e(TAG, "onFinishRecord")
    }
})

/**
 *  set hands-free condition
 * */
btn.setHandsFreeDetector(object : HandsFreeDetector() {
    override fun isMatchingCondition(event: MotionEvent): Boolean {
        return (event.x < 0 && event.actionMasked == MotionEvent.ACTION_UP)
    }
})
```

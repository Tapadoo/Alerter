# Alerter - An Android Alerter Library, now in Kotlin!

This library aims to overcome the limitations of Toasts and Snackbars, while reducing the
complexity of your layouts.

[![Download](https://api.bintray.com/packages/tapadoo/maven/alerter/images/download.svg)](https://bintray.com/tapadoo/maven/alerter/_latestVersion) [![API](https://img.shields.io/badge/API-14%2B-orange.svg?style=flat)](https://android-arsenal.com/api?level=14) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Alerter-blue.svg?style=flat)](https://android-arsenal.com/details/1/5302) [![Android Weekly](https://img.shields.io/badge/Android%20Weekly-%23245-blue.svg)](http://androidweekly.net/issues/issue-245)

[![Header](./documentation/header.png)](https://play.google.com/store/apps/details?id=com.tapadoo.alerter_demo)

## General

With simplicity in mind, the Alerter employs the builder pattern to facilitate easy integration into any app.
A customisable Alert view is dynamically added to the Decor View of the Window, overlaying all content. 

## Gradle

```groovy
dependencies {
    implementation 'com.tapadoo.android:alerter:2.0.6'
}
```

# Usage

![Default Alert](./documentation/alert_default.gif)

From an Activity -

```kotlin
Alerter.create(this@DemoActivity)
       .setTitle("Alert Title")
       .setText("Alert text...")
       .show()
```

Or from a Fragment -

```kotlin
Alerter.create(activity)
       .setTitle("Alert Title")
       .setText("Alert text...")
       .show()
```

To check if an alert is showing - 


```kotlin
Alerter.isShowing()
```

To hide a currently showing Alert - 

```kotlin
Alerter.hide()
```

# Customisation

### Background Colour

```kotlin
Alerter.create(this@DemoActivity)
       .setTitle("Alert Title")
       .setText("Alert text...")
       .setBackgroundColorRes(R.color.colorAccent) // or setBackgroundColorInt(Color.CYAN)
       .show()
```

![Coloured Alert](./documentation/alert_coloured.gif)

### Icon

```kotlin
Alerter.create(this@DemoActivity)
       .setText("Alert text...")
       .setIcon(R.drawable.alerter_ic_mail_outline)
       .setIconColorFilter(0) // Optional - Removes white tint
       .show()
```

![Custom Icon Alert](./documentation/alert_icon.gif)

### On screen duration, in milliseconds

```kotlin
Alerter.create(this@DemoActivity)
       .setTitle("Alert Title")
       .setText("Alert text...")
       .setDuration(10000)
       .show()
```

### Without title

```kotlin
Alerter.create(this@DemoActivity)
       .setText("Alert text...")
       .show()
```

![Text Only Alert](./documentation/alert_text_only.gif)

### Adding an On Click Listener

```kotlin
 Alerter.create(this@DemoActivity)
        .setTitle("Alert Title")
        .setText("Alert text...")
        .setDuration(10000)
        .setOnClickListener(View.OnClickListener {
            Toast.makeText(this@DemoActivity, "OnClick Called", Toast.LENGTH_LONG).show();
        })
        .show()
```

![On Click Alert](./documentation/alert_on_click.gif)

### Verbose text

```kotlin
 Alerter.create(this@DemoActivity)
        .setTitle("Alert Title")
        .setText("The alert scales to accommodate larger bodies of text. " +
                 "The alert scales to accommodate larger bodies of text. " +
                 "The alert scales to accommodate larger bodies of text.")
        .show()
```

![Verbose Alert](./documentation/alert_verbose.gif)

### Visibility Callbacks

```kotlin
 Alerter.create(this@KotlinDemoActivity)
        .setTitle("Alert Title")
        .setText("Alert text...")
        .setDuration(10000)
        .setOnShowListener(OnShowAlertListener {
            Toast.makeText(this@KotlinDemoActivity, "Show Alert", Toast.LENGTH_LONG).show()
        })
        .setOnHideListener(OnHideAlertListener {
            Toast.makeText(this@KotlinDemoActivity, "Hide Alert", Toast.LENGTH_LONG).show()
        })
        .show()
```

### Custom Fonts and Text Appearance

```kotlin 
 Alerter.create(this@DemoActivity)
        .setTitle("Alert Title")
        .setTitleAppearance(R.style.AlertTextAppearance_Title)
        .setTitleTypeface(Typeface.createFromAsset(getAssets(), "Pacifico-Regular.ttf"))
        .setText("Alert text...")
        .setTextAppearance(R.style.AlertTextAppearance_Text)
        .setTextTypeface(Typeface.createFromAsset(getAssets(), "ScopeOne-Regular.ttf"))
        .show()
```

![Verbose Alert](./documentation/alert_custom_font.gif)

### Swipe to Dismiss

```kotlin
 Alerter.create(this@DemoActivity)
        .setTitle("Alert Title")
        .setText("Alert text...")
        .enableSwipeToDismiss()
        .show()
```
![Verbose Alert](./documentation/alert_swipe_to_dismiss.gif)

### Progress Bar

```kotlin
 Alerter.create(this@DemoActivity)
        .setTitle("Alert Title")
        .setText("Alert text...")
        .enableProgress(true)
        .setProgressColorRes(R.color.colorAccent)
        .show()
```

![Verbose Alert](./documentation/alert_progress_bar.gif)

## Sample

Clone this repo and check out the `app` module.

## Licence

See the [LICENSE](LICENSE.md) file for license rights and limitations (MIT).

Copyright 2017 Tapadoo, Dublin.

![Alt Text](http://tapadoo.com/wp-content/themes/tapadoo/img/tapadoo-logo@2x.png)

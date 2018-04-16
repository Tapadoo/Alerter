# Alerter

![Icon](./app-base/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

[Play Store Demo](https://play.google.com/store/apps/details?id=com.tapadoo.alerter_demo)

### General

[ ![Download](https://api.bintray.com/packages/tapadoo/maven/alerter/images/download.svg) ](https://bintray.com/tapadoo/maven/alerter/_latestVersion)

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Alerter-blue.svg?style=flat)](https://android-arsenal.com/details/1/5302)

This library aims to overcome the limitations of Toasts and Snackbars, while reducing the
complexity of your layouts.

![Default Alert](./documentation/alert_default.gif)

A customisable Alert view is dynamically added to the Decor View of the Window, overlaying
all content.

## Gradle

```groovy
dependencies {
    implementation 'com.tapadoo.android:alerter:2.0.5'
}
```

# Usage

With simplicity in mind, the Alerter employs the builder pattern to facilitate easy integration
into any app.

From an Activity -

```java
Alerter.create(this)
       .setTitle("Alert Title")
       .setText("Alert text...")
       .show();
```

Or from a Fragment -

```java
Alerter.create(getActivity())
       .setTitle("Alert Title")
       .setText("Alert text...")
       .show();
```

To check if an alert is showing - 


```java
Alerter.isShowing();
```

To hide a currently showing Alert - 

```java
Alerter.hide();
```

# Customisation

### Background Colour

```java
Alerter.create(this)
       .setTitle("Alert Title")
       .setText("Alert text...")
       .setBackgroundColorRes(R.color.colorAccent) // or setBackgroundColorInt(Color.CYAN)
       .show();
```

![Coloured Alert](./documentation/alert_coloured.gif)

### Icon

```java
Alerter.create(this)
       .setText("Alert text...")
       .setIcon(R.drawable.alerter_ic_mail_outline)
       .setIconColorFilter(0) // Optional - Removes white tint
       .show();
```

![Custom Icon Alert](./documentation/alert_icon.gif)

### On screen duration, in milliseconds

```java
Alerter.create(this)
       .setTitle("Alert Title")
       .setText("Alert text...")
       .setDuration(10000)
       .show();
```

### Without title

```java
Alerter.create(this)
       .setText("Alert text...")
       .show();
```

![Text Only Alert](./documentation/alert_text_only.gif)

### Adding an On Click Listener

```java
 Alerter.create(ExampleActivity.this)
        .setTitle("Alert Title")
        .setText("Alert text...")
        .setDuration(10000)
        .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ExampleActivity.this, "OnClick Called", Toast.LENGTH_LONG).show();
            }
        })
        .show();
```

![On Click Alert](./documentation/alert_on_click.gif)

### Verbose text

```java
 Alerter.create(ExampleActivity.this)
        .setTitle("Alert Title")
        .setText("The alert scales to accommodate larger bodies of text. " +
                 "The alert scales to accommodate larger bodies of text. " +
                 "The alert scales to accommodate larger bodies of text.")
        .show();
```

![Verbose Alert](./documentation/alert_verbose.gif)

### Visibility Callbacks

```java
 Alerter.create(ExampleActivity.this)
        .setTitle("Alert Title")
        .setOnShowListener(new OnShowAlertListener() {
            @Override
            public void onShow() {
                Toast.makeText(ExampleActivity.this, "Alert Shown", Toast.LENGTH_LONG).show();
            }
        })
        .setOnHideListener(new OnHideAlertListener() {
            @Override
            public void onHide() {
                Toast.makeText(ExampleActivity.this, "Alert Hidden", Toast.LENGTH_LONG).show();
            }
         })
        .show();
```

### Custom Fonts and Text Appearance

```java 
 Alerter.create(ExampleActivity.this)
                .setTitle("Alert Title")
                .setTitleAppearance(R.style.AlertTextAppearance_Title)
                .setTitleTypeface(Typeface.createFromAsset(getAssets(), "Pacifico-Regular.ttf"))
                .setText("Alert text...")
                .setTextAppearance(R.style.AlertTextAppearance_Text)
                .setTextTypeface(Typeface.createFromAsset(getAssets(), "ScopeOne-Regular.ttf"))
                .show();
```

![Verbose Alert](./documentation/alert_custom_font.gif)

### Swipe to Dismiss

```java
 Alerter.create(ExampleActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .enableSwipeToDismiss()
                .show();
```
![Verbose Alert](./documentation/alert_swipe_to_dismiss.gif)

### Progress Bar

```java
Alerter.create(ExampleActivity.this)
                .setTitle("Alert Title")
                .setText("Alert text...")
                .enableProgress(true)
                .setProgressColorRes(R.color.colorAccent)
                .show();
```

![Verbose Alert](./documentation/alert_progress_bar.gif)

## Sample

Clone this repo and check out the `app` module.

## Licence

See the [LICENSE](LICENSE.md) file for license rights and limitations (MIT).

Copyright 2017 Tapadoo, Dublin.

![Alt Text](http://tapadoo.com/wp-content/themes/tapadoo/img/tapadoo-logo@2x.png)
